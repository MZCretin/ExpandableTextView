package com.cretin.www.expandabletextview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.text.DynamicLayout;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.method.Touch;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @date: on 2018-08-24
 * @author: cretin
 * @email: mxnzp_life@163.com
 * @desc: 一个支持展开 收起 网页链接 和 @用户 点击识别 的TextView
 */
public class ExpandableTextView extends AppCompatTextView {
    private static final int DEF_MAX_LINE = 4;
    public static final String TEXT_CONTRACT = "收起";
    public static final String TEXT_EXPEND = "展开";
    public static final String Space = " ";
    public static final String TEXT_TARGET = "网页链接";
    public static final String IMAGE_TARGET = "图";
    public static final String TARGET = IMAGE_TARGET + TEXT_TARGET;

    public static final String regexp = "((http[s]{0,1}|ftp)://[a-zA-Z0-9\\.\\-]+\\.([a-zA-Z]{2,4})(:\\d+)?(/[a-zA-Z0-9\\.\\-~!@#$%^&*+?:_/=<>]*)?)|((www.)|[a-zA-Z0-9\\.\\-]+\\.([a-zA-Z]{2,4})(:\\d+)?(/[a-zA-Z0-9\\.\\-~!@#$%^&*+?:_/=<>]*)?)";

    public static final String regexp_mention = "@[\\w\\p{InCJKUnifiedIdeographs}-]{1,26}";

    private TextPaint mPaint;

    boolean linkHit;

    private Context mContext;

    /**
     * 计算的layout
     */
    private DynamicLayout mDynamicLayout;

    //hide状态下，展示多少行开始省略
    private int mLimitLines;

    private int currentLines;

    private int mWidth;

    private Drawable mLinkDrawable = null;

    /**
     * 链接和@用户的事件点击
     */
    private OnLinkClickListener linkClickListener;

    /**
     * 是否需要收起
     */
    private boolean mNeedContract = true;

    private FormatData mFormatData;

    /**
     * 是否需要展开功能
     */
    private boolean mNeedExpend = true;

    /**
     * 是否需要动画 默认开启动画
     */
    private boolean mNeedAnimation = true;

    private int mLineCount;

    private CharSequence mContent;

    /**
     * 展开文字的颜色
     */
    private int mExpandTextColor;

    /**
     * 链接的字体颜色
     */
    private int mLinkTextColor;

    /**
     * 收起的文字的颜色
     */
    private int mContractTextColor;

    /**
     * 展开的文案
     */
    private String mExpandString;
    /**
     * 收起的文案
     */
    private String mContractString;

    /**
     * 在收回和展开前面添加的内容
     */
    private String mEndExpandContent;

    /**
     * 在收回和展开前面添加的内容的字体颜色
     */
    private int mEndExpandTextColor;

    public ExpandableTextView(Context context) {
        this(context, null);
    }

    public ExpandableTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public ExpandableTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
        setMovementMethod(LocalLinkMovementMethod.getInstance());
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        if (attrs != null) {
            TypedArray a =
                    getContext().obtainStyledAttributes(attrs, R.styleable.ExpandableTextView,
                            defStyleAttr, 0);

            mLimitLines = a.getInt(R.styleable.ExpandableTextView_ep_max_line, DEF_MAX_LINE);
            mNeedExpend = a.getBoolean(R.styleable.ExpandableTextView_ep_need_expand, true);
            mNeedContract = a.getBoolean(R.styleable.ExpandableTextView_ep_need_contract, false);
            mNeedAnimation = a.getBoolean(R.styleable.ExpandableTextView_ep_need_animation, true);
            mContractString = a.getString(R.styleable.ExpandableTextView_ep_contract_text);
            mExpandString = a.getString(R.styleable.ExpandableTextView_ep_expand_text);
            mExpandTextColor = a.getColor(R.styleable.ExpandableTextView_ep_expand_color,
                    Color.parseColor("#999999"));
            mEndExpandTextColor = a.getColor(R.styleable.ExpandableTextView_ep_expand_color,
                    Color.parseColor("#999999"));
            mContractTextColor = a.getColor(R.styleable.ExpandableTextView_ep_contract_color,
                    Color.parseColor("#999999"));
            mLinkTextColor = a.getColor(R.styleable.ExpandableTextView_ep_link_color,
                    Color.parseColor("#FF6200"));
            int resId = a.getResourceId(R.styleable.ExpandableTextView_ep_link_res, R.mipmap.link);
            mLinkDrawable = getResources().getDrawable(resId);
            currentLines = mLimitLines;
            a.recycle();
        } else {
            mLinkDrawable = context.getResources().getDrawable(R.mipmap.link);
        }

        mContext = context;

        mPaint = getPaint();
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        //初始化link的图片
        mLinkDrawable.setBounds(0, 0, 30, 30); //必须设置图片大小，否则不显示
    }

    private SpannableStringBuilder setRealContent(CharSequence content) {
        //处理给定的数据
        mFormatData = formatData(content);
        //用来计算内容的大小
        mDynamicLayout =
                new DynamicLayout(mFormatData.formatedContent, mPaint, mWidth, Layout.Alignment.ALIGN_NORMAL, 1f, 0.0f,
                        true);
        //获取行数
        mLineCount = mDynamicLayout.getLineCount();

        if (!mNeedExpend || mLineCount <= mLimitLines) {
            //不需要展开功能 直接处理链接模块
            return dealLink(mFormatData, false);
        } else {
            return dealLink(mFormatData, true);
        }
    }

    /**
     * 设置追加的内容
     *
     * @param endExpendContent
     */
    public void setEndExpendContent(String endExpendContent) {
        this.mEndExpandContent = endExpendContent;
    }

    /**
     * 设置内容
     *
     * @param content
     */
    public void setContent(String content) {
        mContent = content;

        currentLines = mLimitLines;

        if (mWidth == 0) {
            mWidth = getWidth();
        }

        if (mWidth == 0) {
            post(() -> setContent(content));
        } else {
            setRealContent(content);
        }
    }

    /**
     * 设置最后的收起文案
     *
     * @return
     */
    private String getExpandEndContent() {
        if (TextUtils.isEmpty(mEndExpandContent)) {
            return String.format(Locale.getDefault(), "  %s",
                    TEXT_CONTRACT);
        } else {
            return String.format(Locale.getDefault(), "  %s  %s",
                    mEndExpandContent, TEXT_CONTRACT);
        }
    }

    /**
     * 设置展开的文案
     *
     * @return
     */
    private String getHideEndContent() {
        if (TextUtils.isEmpty(mEndExpandContent)) {
            return String.format(Locale.getDefault(), "...  %s",
                    TEXT_EXPEND);
        } else {
            return String.format(Locale.getDefault(), "...  %s  %s",
                    mEndExpandContent, TEXT_EXPEND);
        }
    }

    /**
     * 处理文字中的链接问题
     *
     * @param formatData
     * @param ignoreMore
     */
    private SpannableStringBuilder dealLink(FormatData formatData, boolean ignoreMore) {
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        //处理折叠操作
        if (ignoreMore) {
            if (currentLines < mLineCount) {
                int index = currentLines - 1;
                int endPosition = mDynamicLayout.getLineEnd(index);
                int startPosition = mDynamicLayout.getLineStart(index);
                float lineWidth = mDynamicLayout.getLineWidth(index);

                String endString = getHideEndContent();

                //计算原内容被截取的位置下标
                int fitPosition =
                        getFitPosition(endPosition, startPosition, lineWidth, mPaint.measureText(endString), 0);

                ssb.append(formatData.formatedContent.substring(0, fitPosition));

                //在被截断的文字后面添加 展开 文字
                ssb.append(endString);

                int expendLength = TextUtils.isEmpty(mEndExpandContent) ? 0 : 2 + mEndExpandContent.length();
                ssb.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        action();
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        ds.setColor(mExpandTextColor);
                        ds.setUnderlineText(false);
                    }
                }, ssb.length() - TEXT_EXPEND.length() - expendLength, ssb.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            } else {
                ssb.append(formatData.formatedContent);
                if (mNeedContract) {
                    String endString = getExpandEndContent();
                    ssb.append(endString);

                    int expendLength = TextUtils.isEmpty(mEndExpandContent) ? 0 : 2 + mEndExpandContent.length();
                    ssb.setSpan(new ClickableSpan() {
                        @Override
                        public void onClick(View widget) {
                            action();
                        }

                        @Override
                        public void updateDrawState(TextPaint ds) {
                            super.updateDrawState(ds);
                            ds.setColor(mContractTextColor);
                            ds.setUnderlineText(false);
                        }
                    }, ssb.length() - TEXT_CONTRACT.length() - expendLength, ssb.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                } else {
                    if (!TextUtils.isEmpty(mEndExpandContent)) {
                        ssb.append(mEndExpandContent);
                        ssb.setSpan(new ForegroundColorSpan(mEndExpandTextColor), ssb.length() - mEndExpandContent.length(), ssb.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    }
                }
            }
        } else {
            ssb.append(formatData.formatedContent);
            if (!TextUtils.isEmpty(mEndExpandContent)) {
                ssb.append(mEndExpandContent);
                ssb.setSpan(new ForegroundColorSpan(mEndExpandTextColor), ssb.length() - mEndExpandContent.length(), ssb.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            }
        }
        //处理链接的处理
        List<FormatData.PositionData> positionDatas = formatData.getPositionDatas();
        HH:
        for (FormatData.PositionData data : positionDatas) {
            if (ssb.length() >= data.getEnd()) {
                if (data.getType().equals(LinkType.LINK_TYPE)) {
                    int fitPosition = ssb.length() - getHideEndContent().length();
                    if (data.getStart() < fitPosition) {
                        SelfImageSpan imageSpan = new SelfImageSpan(mLinkDrawable, ImageSpan.ALIGN_BASELINE);
                        //设置链接图标
                        ssb.setSpan(imageSpan, data.getStart(), data.getStart() + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                        //设置链接文字样式
                        int endPosition = data.getEnd();
                        if (fitPosition > data.getStart() + 1 && fitPosition < data.getEnd()) {
                            endPosition = fitPosition;
                        }
                        if (data.getStart() + 1 < fitPosition) {
                            ssb.setSpan(new ClickableSpan() {
                                @Override
                                public void onClick(View widget) {
                                    if (linkClickListener != null)
                                        linkClickListener.onLinkClickListener(LinkType.LINK_TYPE, data.getUrl());
                                }

                                @Override
                                public void updateDrawState(TextPaint ds) {
                                    ds.setColor(mLinkTextColor);
                                    ds.setUnderlineText(false);
                                }
                            }, data.getStart() + 1, endPosition, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                        }
                    }
                } else {
                    int fitPosition = ssb.length() - getHideEndContent().length();
                    if (data.getStart() < fitPosition) {
                        int endPosition = data.getEnd();
                        if (fitPosition < data.getEnd()) {
                            endPosition = fitPosition;
                        }
                        //关注
                        ssb.setSpan(new ClickableSpan() {
                            @Override
                            public void onClick(View widget) {
                                if (linkClickListener != null)
                                    linkClickListener.onLinkClickListener(LinkType.MENTION_TYPE, data.getUrl());
                            }

                            @Override
                            public void updateDrawState(TextPaint ds) {
                                ds.setColor(mLinkTextColor);
                                ds.setUnderlineText(false);
                            }
                        }, data.getStart(), endPosition, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    }
                }
            }
        }
        //将内容设置到控件中
        setText(ssb);
        //清除链接点击时背景效果
        setHighlightColor(Color.TRANSPARENT);
        return ssb;
    }

    /**
     * 执行展开和收回的动作
     */
    private void action() {
        boolean isHide = currentLines < mLineCount;
        if (mNeedAnimation) {
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
            valueAnimator.addUpdateListener(animation -> {
                Float value = (Float) animation.getAnimatedValue();
                if (isHide) {
                    currentLines = mLimitLines + (int) ((mLineCount - mLimitLines) * value);
                } else {
                    if (mNeedContract)
                        currentLines = mLimitLines + (int) ((mLineCount - mLimitLines) * (1 - value));
                }
                setText(setRealContent(mContent));
            });
            valueAnimator.setDuration(100);
            valueAnimator.start();
        } else {
            if (isHide) {
                currentLines = mLimitLines + ((mLineCount - mLimitLines));
            } else {
                if (mNeedContract)
                    currentLines = mLimitLines;
            }
            setText(setRealContent(mContent));
        }
    }

    /**
     * 计算原内容被裁剪的长度
     *
     * @param endPosition
     * @param startPosition
     * @param lineWidth
     * @param endStringWith
     * @param offset
     * @return
     */
    private int getFitPosition(int endPosition, int startPosition, float lineWidth,
                               float endStringWith, float offset) {
        int position = (int) ((lineWidth - (endStringWith + offset)) * (endPosition - startPosition)
                / lineWidth);

        if (position < 0) return endPosition;

        float measureText = mPaint.measureText(
                (mFormatData.formatedContent.substring(startPosition, startPosition + position)));

        if (measureText <= lineWidth - endStringWith) {
            return startPosition + position;
        } else {
            return getFitPosition(endPosition, startPosition, lineWidth, endStringWith, offset + mPaint.measureText(Space));
        }
    }

    /**
     * 对传入的数据进行正则匹配并处理
     *
     * @param content
     * @return
     */
    private FormatData formatData(CharSequence content) {
        FormatData formatData = new FormatData();
        List<FormatData.PositionData> datas = new ArrayList<>();
        //对链接进行正则匹配
        Pattern pattern = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(content);
        StringBuffer newResult = new StringBuffer();
        int start = 0;
        int end = 0;
        int temp = 0;
        while (matcher.find()) {
            start = matcher.start();
            end = matcher.end();
            newResult.append(content.toString().substring(temp, start));
            //将匹配到的内容进行统计处理
            datas.add(new FormatData.PositionData(newResult.length() + 1, newResult.length() + 2 + TARGET.length(), matcher.group(), LinkType.LINK_TYPE));
            newResult.append(" " + TARGET + " ");
            temp = end;
        }
        newResult.append(content.toString().substring(end, content.toString().length()));
        //对@用户 进行正则匹配
        pattern = Pattern.compile(regexp_mention, Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(newResult.toString());
        List<FormatData.PositionData> datasMention = new ArrayList<>();
        while (matcher.find()) {
            //将匹配到的内容进行统计处理
            datasMention.add(new FormatData.PositionData(matcher.start(), matcher.end(), matcher.group(), LinkType.MENTION_TYPE));
        }
        formatData.setFormatedContent(newResult.toString());
        datas.addAll(0, datasMention);
        formatData.setPositionDatas(datas);
        return formatData;
    }

    /**
     * 记录可以点击的内容 和 位置
     */
    static class FormatData {
        private String formatedContent;
        private List<PositionData> positionDatas;

        public String getFormatedContent() {
            return formatedContent;
        }

        public void setFormatedContent(String formatedContent) {
            this.formatedContent = formatedContent;
        }

        public List<PositionData> getPositionDatas() {
            return positionDatas;
        }

        public void setPositionDatas(List<PositionData> positionDatas) {
            this.positionDatas = positionDatas;
        }

        static class PositionData {
            private int start;
            private int end;
            private String url;
            private LinkType type;

            public LinkType getType() {
                return type;
            }

            public void setType(LinkType type) {
                this.type = type;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public PositionData(int start, int end, String url, LinkType type) {
                this.start = start;
                this.end = end;
                this.url = url;
                this.type = type;
            }

            public int getStart() {
                return start;
            }

            public void setStart(int start) {
                this.start = start;
            }

            public int getEnd() {
                return end;
            }

            public void setEnd(int end) {
                this.end = end;
            }
        }
    }

    /**
     * 自定义ImageSpan 让Image 在行内居中显示
     */
    class SelfImageSpan extends ImageSpan {
        private Drawable drawable;

        public SelfImageSpan(Drawable d, int verticalAlignment) {
            super(d, verticalAlignment);
            this.drawable = d;
        }

        @Override
        public Drawable getDrawable() {
            return drawable;
        }

        @Override
        public void draw(@NonNull Canvas canvas, CharSequence text,
                         int start, int end, float x,
                         int top, int y, int bottom, @NonNull Paint paint) {
            // image to draw
            Drawable b = getDrawable();
            // font metrics of text to be replaced
            Paint.FontMetricsInt fm = paint.getFontMetricsInt();
            int transY = (y + fm.descent + y + fm.ascent) / 2
                    - b.getBounds().bottom / 2;
            canvas.save();
            canvas.translate(x, transY);
            b.draw(canvas);
            canvas.restore();
        }
    }

    public static class LocalLinkMovementMethod extends LinkMovementMethod {
        static LocalLinkMovementMethod sInstance;


        public static LocalLinkMovementMethod getInstance() {
            if (sInstance == null)
                sInstance = new LocalLinkMovementMethod();

            return sInstance;
        }

        @Override
        public boolean onTouchEvent(TextView widget,
                                    Spannable buffer, MotionEvent event) {
            int action = event.getAction();

            if (action == MotionEvent.ACTION_UP ||
                    action == MotionEvent.ACTION_DOWN) {
                int x = (int) event.getX();
                int y = (int) event.getY();

                x -= widget.getTotalPaddingLeft();
                y -= widget.getTotalPaddingTop();

                x += widget.getScrollX();
                y += widget.getScrollY();

                Layout layout = widget.getLayout();
                int line = layout.getLineForVertical(y);
                int off = layout.getOffsetForHorizontal(line, x);

                ClickableSpan[] link = buffer.getSpans(
                        off, off, ClickableSpan.class);

                if (link.length != 0) {
                    if (action == MotionEvent.ACTION_UP) {
                        link[0].onClick(widget);
                    } else if (action == MotionEvent.ACTION_DOWN) {
                        Selection.setSelection(buffer,
                                buffer.getSpanStart(link[0]),
                                buffer.getSpanEnd(link[0]));
                    }

                    if (widget instanceof ExpandableTextView) {
                        ((ExpandableTextView) widget).linkHit = true;
                    }
                    return true;
                } else {
                    Selection.removeSelection(buffer);
                    Touch.onTouchEvent(widget, buffer, event);
                    return false;
                }
            }
            return Touch.onTouchEvent(widget, buffer, event);
        }
    }

    boolean dontConsumeNonUrlClicks = true;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        linkHit = false;
        boolean res = super.onTouchEvent(event);

        if (dontConsumeNonUrlClicks)
            return linkHit;
        return res;
    }

    interface OnLinkClickListener {
        void onLinkClickListener(LinkType type, String content);
    }

    //定义类型的枚举类型
    enum LinkType {
        //普通链接
        LINK_TYPE,
        //@用户
        MENTION_TYPE
    }

    public OnLinkClickListener getLinkClickListener() {
        return linkClickListener;
    }

    public void setLinkClickListener(OnLinkClickListener linkClickListener) {
        this.linkClickListener = linkClickListener;
    }
}
