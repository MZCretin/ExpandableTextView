package com.cretin.cretin.expandabletextview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.text.util.LinkifyCompat;
import android.support.v7.widget.AppCompatTextView;
import android.text.DynamicLayout;
import android.text.Html;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.method.Touch;
import android.text.style.ClickableSpan;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

/**
 * //(int) Math.ceil(StaticLayout.getDesiredWidth(text, mPaint))
 * 可以伸缩的textview
 * 作者: linhongfei
 * 日期: 2017/12/12 0012.
 */

public class SuperExpandTextView2 extends AppCompatTextView {
    private static final int DEF_MAX_LINE = 10;
    public static final String TEXT_CONTRACT = "收起";
    public static final String TEXT_EXPEND = "展开";
    public static final String Space = " ";

    private TextPaint mPaint;
    boolean linkHit;

    /**
     * 计算的layout
     */
    private DynamicLayout mDynamicLayout;

    private CharSequence mContent;

    //hide状态下，展示多少行开始省略
    private int mLimitLines;


    private int currentLines;

    private int mWidth;

    /**
     * 是否需要收回
     */
    private boolean mNeedContract = true;

    /**
     * 是否需要展开功能
     */
    private boolean mNeedExpend = true;

    /**
     * 是否需要动画 默认开启动画
     */
    private boolean mNeedAnimation = true;

    private int mLineCount;

    public SuperExpandTextView2(Context context) {
        this(context, null);
    }

    public SuperExpandTextView2(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public SuperExpandTextView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
        setMovementMethod(SuperExpandTextView2.LocalLinkMovementMethod.getInstance());
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        if (attrs != null) {
            TypedArray a =
                    getContext().obtainStyledAttributes(attrs, R.styleable.ExpandableTextViewAttr2,
                            defStyleAttr, 0);

            mLimitLines = a.getInt(R.styleable.ExpandableTextViewAttr2_max_line, DEF_MAX_LINE);
            currentLines = mLimitLines;
            a.recycle();
        }

        mPaint = getPaint();
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    private CharSequence getRealContent() {
        //没有 超过limit_line ,直接返回
//        SpannableStringBuilder temp = new SpannableStringBuilder(mContent);
//        LinkifyCompat.addLinks(temp, WebPattern.MENTION_URL, WebPattern.MENTION_SCHEME);
//        LinkifyCompat.addLinks(temp, WebPattern.WEB_URL, WebPattern.WEB_SCHEME);
//        changeUnderLine(temp);
        String content = mContent.toString().replaceAll("http://www.baidu.com", "网页链接");
        mDynamicLayout =
                new DynamicLayout(content, mPaint, mWidth, Layout.Alignment.ALIGN_NORMAL, 1.2f, 0.0f,
                        true);
        mLineCount = mDynamicLayout.getLineCount();
        if (mLineCount <= mLimitLines) {
            SpannableString result = new SpannableString(mContent);
            return result;
        }
        SpannableStringBuilder ssb = null;
        //用ImageSpan替换文本
        //if (mImageDownSpan != null) {
        //    ssb.setSpan(mImageDownSpan, text.length() - 2, text.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        //}
        //如果当前行小于最大行数,这显示隐藏内容，否则显示完全展开内容
        if (currentLines < mLineCount) {
            ssb = new SpannableStringBuilder();
            ssb = getHideString(ssb);
        } else {
            ssb = new SpannableStringBuilder(mContent);
            ssb = getExpandEndContent(ssb);
        }

        LinkifyCompat.addLinks(ssb, WebPattern.MENTION_URL, WebPattern.MENTION_SCHEME);
        LinkifyCompat.addLinks(ssb, WebPattern.WEB_URL, WebPattern.WEB_SCHEME);
        changeUnderLine(ssb);

        ssb.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                startClickAnimation();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.parseColor("#666666"));
                ds.setUnderlineText(false);
            }
        }, ssb.length() - 2, ssb.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        setMovementMethod(LinkMovementMethod.getInstance());
        return ssb;
    }

    /**
     * 去link下划线，设置link颜色
     *
     * @param value
     */
    private static void changeUnderLine(SpannableStringBuilder value) {
        ClickableSpan[] list = value.getSpans(0, value.length(), ClickableSpan.class);
        for (int i = 0; i < list.length; i++) {
            ClickableSpan span = list[i];
            NoUnderlineSpan mNoUnderlineSpan = new NoUnderlineSpan();
            value.replace(value.getSpanStart(span), value.getSpanEnd(span), "网页链接");
            Log.e("HHHHH", "666666666 " + value.subSequence(value.getSpanStart(span), value.getSpanEnd(span)));
            value.setSpan(mNoUnderlineSpan, value.getSpanStart(span), value.getSpanEnd(span), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    @NonNull
    private SpannableStringBuilder getExpandEndContent(SpannableStringBuilder builder) {
        if (mNeedContract) {
            String endString = getExpandEndContent();
            builder.append(endString);
        }
        return builder;
    }

    private int getFitSpaceCount(float emptyWidth, float endStringWidth) {
        float measureText = mPaint.measureText(Space);
        int count = 0;
        while (endStringWidth + measureText * count < emptyWidth) {
            count++;
        }
        return --count;
    }

    private String getExpandEndContent() {
        return String.format(Locale.getDefault(), "  %s",
                TEXT_CONTRACT);
    }

    private String getHideEndContent() {
        return String.format(Locale.getDefault(), "...   %s",
                TEXT_EXPEND);
    }

    @NonNull
    private SpannableStringBuilder getHideString(SpannableStringBuilder builder) {
        int index = currentLines - 1;
        int endPosition = mDynamicLayout.getLineEnd(index);
        int startPosition = mDynamicLayout.getLineStart(index);
        float lineWidth = mDynamicLayout.getLineWidth(index);

        String endString = getHideEndContent();

        int fitPosition =
                getFitPosition(builder, endPosition, startPosition, lineWidth, mPaint.measureText(endString), 0);
        builder.append(mContent.subSequence(0, fitPosition));
        builder.append(endString);
        return builder;
    }

    private int getFitPosition(SpannableStringBuilder builder, int endPosition, int startPosition, float lineWidth,
                               float endStringWith, float offset) {
        int position = (int) ((lineWidth - (endStringWith + offset)) * (endPosition - startPosition)
                / lineWidth);

        if (position < 0) return endPosition;

        String content = mContent.toString().replaceAll("http://www.baidu.com", "网页链接");
        float measureText = mPaint.measureText(
                (content.substring(startPosition, startPosition + position)));

        if (measureText <= lineWidth - endStringWith) {
            return startPosition + position;
        } else {
            return getFitPosition(builder, endPosition, startPosition, lineWidth, endStringWith, offset + mPaint.measureText(Space));
        }
    }

    public CharSequence getContent() {
        return mContent;
    }

    public void setContent(CharSequence content) {
        currentLines = mLimitLines;
        if (mWidth == 0) {
            mWidth = getWidth();
        }

        if (mWidth == 0) {
            post(() -> setContent(content));
        } else {
            setRealContent(content);
        }

        setHighlightColor(Color.TRANSPARENT);
    }

    private void setRealContent(CharSequence content) {
        mContent = content;
        if (mNeedExpend) {
            setText(getRealContent());
        } else {
            setText(mContent);
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
                setText(getRealContent());
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
            setText(getRealContent());
        }
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        linkHit = false;
//        boolean res = super.onTouchEvent(event);
//
//        if (dontConsumeNonUrlClicks)
//            return linkHit;
//        return res;
//
//    }

    public void setTextViewHTML(String html) {
        CharSequence sequence = Html.fromHtml(html);
        SpannableStringBuilder strBuilder =
                new SpannableStringBuilder(sequence);
        setText(strBuilder);
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

                    if (widget instanceof SuperExpandTextView2) {
                        ((SuperExpandTextView2) widget).linkHit = true;
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

    public boolean isNeedContract() {
        return mNeedContract;
    }

    public void setNeedContract(boolean mNeedContract) {
        this.mNeedContract = mNeedContract;
    }

    public boolean isNeedExpend() {
        return mNeedExpend;
    }

    public void setNeedExpend(boolean mNeedExpend) {
        this.mNeedExpend = mNeedExpend;
    }

    public boolean ismNeedAnimation() {
        return mNeedAnimation;
    }

    public void setmNeedAnimation(boolean mNeedAnimation) {
        this.mNeedAnimation = mNeedAnimation;
    }

    static class NoUnderlineSpan extends UnderlineSpan {

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(ds.linkColor);
            ds.setUnderlineText(false);
        }
    }

}
