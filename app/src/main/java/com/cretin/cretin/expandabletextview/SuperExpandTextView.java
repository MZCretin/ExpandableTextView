package com.cretin.cretin.expandabletextview;

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
import android.text.method.LinkMovementMethod;
import android.text.method.Touch;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * //(int) Math.ceil(StaticLayout.getDesiredWidth(text, mPaint))
 * 可以伸缩的textview
 * 作者: linhongfei
 * 日期: 2017/12/12 0012.
 */

public class SuperExpandTextView extends AppCompatTextView {
    private static final int DEF_MAX_LINE = 4;
    public static final String TEXT_CONTRACT = "收起";
    public static final String TEXT_EXPEND = "展开";
    public static final String Space = " ";
    public static final String TEXT_TARGET = "网页链接";
    public static final String IMAGE_TARGET = "图";
    public static final String TARGET = IMAGE_TARGET + TEXT_TARGET;

    public static final String regexp = "((http[s]{0,1}|ftp)://[a-zA-Z0-9\\.\\-]+\\.([a-zA-Z]{2,4})(:\\d+)?(/[a-zA-Z0-9\\.\\-~!@#$%^&*+?:_/=<>]*)?)|((www.)|[a-zA-Z0-9\\.\\-]+\\.([a-zA-Z]{2,4})(:\\d+)?(/[a-zA-Z0-9\\.\\-~!@#$%^&*+?:_/=<>]*)?)";
    public static final String regexp_mention = "@[\\w\\p{InCJKUnifiedIdeographs}-]{1,26}";
    public static final String regexp_emotion = "\\[(\\S+?)\\]";
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
     * 是否需要收起
     */
    private boolean mNeedContract = false;

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

    public SuperExpandTextView(Context context) {
        this(context, null);
    }

    public SuperExpandTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public SuperExpandTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
        setMovementMethod(LocalLinkMovementMethod.getInstance());
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        if (attrs != null) {
            TypedArray a =
                    getContext().obtainStyledAttributes(attrs, R.styleable.ExpandableTextViewAttr2,
                            defStyleAttr, 0);

            mLimitLines = a.getInt(R.styleable.ExpandableTextViewAttr2_max_line, DEF_MAX_LINE);
            currentLines = mLimitLines;
            a.recycle();
        }

        mContext = context;

        mPaint = getPaint();
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        //初始化link的图片
        mLinkDrawable = context.getResources().getDrawable(R.mipmap.link);
        mLinkDrawable.setBounds(0, 0, 30, 30); //必须设置图片大小，否则不显示
    }

    private SpannableStringBuilder setRealContent(CharSequence content) {
        //处理给定的数据
        mFormatData = formatData(content);

        mDynamicLayout =
                new DynamicLayout(mFormatData.formatedContent, mPaint, mWidth, Layout.Alignment.ALIGN_NORMAL, 1f, 0.0f,
                        true);

        mLineCount = mDynamicLayout.getLineCount();

        if (mLineCount <= mLimitLines) {
            //不需要展开功能 直接处理链接模块
            return dealLink(mFormatData, false);
        } else {
            return dealLink(mFormatData, true);
        }
    }

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

    private String getExpandEndContent() {
        return String.format(Locale.getDefault(), "  %s",
                TEXT_CONTRACT);
    }

    private String getHideEndContent() {
        return String.format(Locale.getDefault(), "...   %s",
                TEXT_EXPEND);
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

                int fitPosition =
                        getFitPosition(endPosition, startPosition, lineWidth, mPaint.measureText(endString), 0);
                ssb.append(formatData.formatedContent.substring(0, fitPosition));
                ssb.append(endString);

                ssb.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        startClickAnimation();
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        ds.setColor(Color.parseColor("#999999"));
                        ds.setUnderlineText(false);
                    }
                }, ssb.length() - TEXT_EXPEND.length(), ssb.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            } else {
                ssb.append(formatData.formatedContent);
                if (mNeedContract) {
                    String endString = getExpandEndContent();
                    ssb.append(endString);
                    ssb.setSpan(new ClickableSpan() {
                        @Override
                        public void onClick(View widget) {
                            startClickAnimation();
                        }

                        @Override
                        public void updateDrawState(TextPaint ds) {
                            super.updateDrawState(ds);
                            ds.setColor(Color.parseColor("#999999"));
                            ds.setUnderlineText(false);
                        }
                    }, ssb.length() - TEXT_CONTRACT.length(), ssb.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                }
            }
        } else {
            ssb.append(formatData.formatedContent);
        }
        //处理链接的处理
        List<FormatData.PositionData> positionDatas = formatData.getPositionDatas();
        HH:
        for (FormatData.PositionData data : positionDatas) {
            if (ssb.length() >= data.getEnd()) {
                if (data.getType() == FormatData.PositionData.TYPE_URL) {
                    SelfImageSpan imageSpan = new SelfImageSpan(mLinkDrawable, ImageSpan.ALIGN_BASELINE);
                    //设置链接图标
                    ssb.setSpan(imageSpan, data.getStart(), data.getStart() + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                    //设置链接文字样式
                    ssb.setSpan(new ClickableSpan() {
                        @Override
                        public void onClick(View widget) {
                            Toast.makeText(mContext, data.getStart() + "-" + data.getEnd() + " " + data.getUrl(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void updateDrawState(TextPaint ds) {
                            ds.setColor(Color.parseColor("#FF6200"));
                            ds.setUnderlineText(false);
                        }
                    }, data.getStart() + 1, data.getEnd(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                } else {
                    //关注
                    ssb.setSpan(new ClickableSpan() {
                        @Override
                        public void onClick(View widget) {
                            Toast.makeText(mContext, data.getStart() + "-" + data.getEnd() + " " + data.getUrl(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void updateDrawState(TextPaint ds) {
                            ds.setColor(Color.parseColor("#FF6200"));
                            ds.setUnderlineText(false);
                        }
                    }, data.getStart(), data.getEnd(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                }
            } else {
                //退出循环
                break HH;
            }
        }
        addEmotions(ssb);
        setText(ssb);
        setHighlightColor(Color.TRANSPARENT);
        return ssb;
    }

    /**
     * 表情替换
     *
     * @param value
     */
    private void addEmotions(SpannableStringBuilder value) {
        Matcher localMatcher = Pattern.compile(regexp_emotion).matcher(value);
        while (localMatcher.find()) {
            String str2 = localMatcher.group(0);
            int k = localMatcher.start();
            int m = localMatcher.end();
            if (m - k < 8) {
                try {
                    int imageRes = EmoticonsData.getImageResByName(str2);
                    Drawable drawable = mContext.getResources().getDrawable(imageRes);
                    drawable.setBounds(0, 0, 60, 60);
                    ImageSpanAlignCenter imageSpan = new ImageSpanAlignCenter(drawable);
                    value.setSpan(imageSpan, k, m, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                } catch (Exception e) {

                }
            }
        }
    }

    private void startClickAnimation() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        boolean isHide = currentLines < mLineCount;
        if (mNeedAnimation) {
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
            datas.add(new FormatData.PositionData(newResult.length() + 1, newResult.length() + 2 + TARGET.length(), matcher.group(), FormatData.PositionData.TYPE_URL));
            newResult.append(" " + TARGET + " ");
            temp = end;
        }
        newResult.append(content.toString().substring(end, content.toString().length()));

        pattern = Pattern.compile(regexp_mention, Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(newResult.toString());
        List<FormatData.PositionData> datasMention = new ArrayList<>();
        while (matcher.find()) {
            datasMention.add(new FormatData.PositionData(matcher.start(), matcher.end(), matcher.group(), FormatData.PositionData.TYPE_MENTION));
        }
        formatData.setFormatedContent(newResult.toString());
        datas.addAll(0, datasMention);
        formatData.setPositionDatas(datas);
        return formatData;
    }

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
            public static final int TYPE_URL = 1;
            public static final int TYPE_MENTION = 2;
            private int start;
            private int end;
            private String url;
            private int type;

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public PositionData(int start, int end, String url, int type) {
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

                    if (widget instanceof SuperExpandTextView) {
                        ((SuperExpandTextView) widget).linkHit = true;
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
}
