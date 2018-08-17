//package com.cretin.cretin.expandabletextview;
//
//import android.graphics.Color;
//import android.graphics.drawable.Drawable;
//import android.support.v4.text.util.LinkifyCompat;
//import android.text.Spannable;
//import android.text.SpannableString;
//import android.text.Spanned;
//import android.text.TextUtils;
//import android.text.style.ClickableSpan;
//import android.text.style.ForegroundColorSpan;
//import android.text.util.Linkify;
//import android.widget.TextView;
//
//import com.blankj.utilcode.util.Utils;
//import com.followme.basiclib.application.FollowMeApp;
//import com.followme.basiclib.utils.HtmlUtil;
//import com.followme.basiclib.utils.LogUtils;
//import com.followme.basiclib.utils.RxUtils;
//import com.followme.basiclib.utils.StringUtils;
//import com.followme.basiclib.utils.pattern.BlogPattern;
//import com.followme.basiclib.widget.span.ImageSpanAlignCenter;
//import com.followme.basiclib.widget.span.NoUnderlineSpan;
//import com.followme.componentsocial.widget.emoji.EmoticonsData;
//
//import java.lang.ref.WeakReference;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.regex.Matcher;
//
//import io.reactivex.Observable;
//import io.reactivex.android.schedulers.AndroidSchedulers;
//
///**
// * Created by hou on 2015/7/9.
// */
//public class SpannaleStringUtil {
//
//
//    public static void convertNormalStringToSpannableString(TextView textView, String txt, Object tag) {
//        convertNormalStringToSpannableString(textView, txt, true, null, tag);
//    }
//
////    public static void convertNormalStringToSpannableString(TextView textView, String txt, boolean parserLink) {
////        convertNormalStringToSpannableString(textView, txt, parserLink, null);
////    }
//
//    public static void convertNormalStringToSpannableString(TextView textView, String txt, String searchKey, Object tag) {
//        convertNormalStringToSpannableString(textView, txt, true, searchKey, tag);
//    }
//
//    public static void convertNormalStringToSpannableString(final TextView textView, String txt, final boolean parserLink, final String searchKey, Object tag) {
//        if (null == txt) return;
//        txt = txt.replace("\n", "");
//        String finalTxt = txt;
//        textView.setTag(tag);
//        Observable.just(txt)
//                .map(t -> getSpannableString(textView, finalTxt, parserLink, searchKey))
//                .subscribeOn(RxUtils.getProcessorSchedulers())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(value -> {
//                    if (textView.getTag() == null || tag == null || textView.getTag() == tag) {
//                        textView.setText(value);
//                    }
//                }, Throwable::printStackTrace);
//    }
//
//    private static SpannableString getSpannableString(TextView textView, String txt, boolean parserLink, String searchKey) {
//        String hackTxt;
//        if (txt.startsWith("[") && txt.endsWith("]")) {
//            hackTxt = txt + " ";
//        } else {
//            hackTxt = txt;
//        }
//
//        //关键字着色
//        final List<Integer> listKeyword = new ArrayList<>();
//        hackTxt = parseKeyword(searchKey, hackTxt, listKeyword);
//
//        final SpannableString value = SpannableString.valueOf(hackTxt);
//
//        if (parserLink) {
//            LinkifyCompat.addLinks(value, BlogPattern.MENTION_URL, BlogPattern.MENTION_SCHEME);
//            LinkifyCompat.addLinks(value, BlogPattern.WEB_URL, BlogPattern.WEB_SCHEME);
////            LinkifyCompat.addLinks(value, BlogPattern.EMOTION_URL, null, new EmotionMatcherFilter(value), null);
////            Linkify.addLinks(value, BlogPattern.TOPIC_URL, BlogPattern.TOPIC_SCHEME, null, linkTransform);
////            Linkify.addLinks(value, BlogPattern.DOLLAR, "", new DollarMatcherFilter(value), null);
////            Linkify.addLinks(value, BlogPattern.SYMBOL, "");
//        }
//
//        changeKeywordColor(listKeyword, value);
//
//        changeUnderLine(textView, value);
//
//        addEmotions(value);
//
//        return value;
//    }
//
//    /**
//     * 解析keyword的位置，并且删除所有的html标签
//     *
//     * @param searchKey
//     * @param hackTxt
//     * @param listKeyword
//     * @return
//     */
//    private static String parseKeyword(String searchKey, String hackTxt, List<Integer> listKeyword) {
//        if (!StringUtils.isBlank(searchKey)) {
//            try {
//                hackTxt = HtmlUtil.delHTMLTag(hackTxt);
//                String temp = hackTxt;
//                int sTextLength = TextUtils.isEmpty(searchKey) ? 0 : searchKey.length();
//                int lengthFront = 0;//记录被找出后前面的字段的长度
//                int startss = -1;
//                do {
//                    if (TextUtils.isEmpty(searchKey))
//                        break;
//                    startss = temp.toUpperCase().indexOf(searchKey.toUpperCase());
//
//                    if (startss != -1) {
//                        startss = startss + lengthFront;
//                        listKeyword.add(startss);
//                        lengthFront = startss + sTextLength;
//                        listKeyword.add(lengthFront);
//                        temp = hackTxt.substring(lengthFront);
//                    }
//                } while (startss != -1);
//            } catch (Exception e) {
//                LogUtils.e(e);
//            }
//        }
//        return hackTxt;
//    }
//
//    /**
//     * 去link下划线，设置link颜色
//     *
//     * @param textView
//     * @param value
//     */
//    private static void changeUnderLine(TextView textView, SpannableString value) {
//        if (textView.getText() instanceof Spannable) {
//            ClickableSpan[] list = value.getSpans(0, value.length(), ClickableSpan.class);
//            for (int i = 0; i < list.length; i++) {
//                ClickableSpan span = list[i];
//                NoUnderlineSpan mNoUnderlineSpan = new NoUnderlineSpan();
//                value.setSpan(mNoUnderlineSpan, value.getSpanStart(span), value.getSpanEnd(span), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            }
//        }
//    }
//
//    /**
//     * 修改关键字颜色
//     *
//     * @param listKeyword
//     * @param value
//     */
//    private static void changeKeywordColor(List<Integer> listKeyword, SpannableString value) {
//        for (int i = 0; i < listKeyword.size() / 2; i++) {
//            try {
//                value.setSpan(new ForegroundColorSpan(Color.parseColor("#0083f2")), listKeyword.get(i * 2), listKeyword.get(i * 2 + 1), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            } catch (Exception e) {
//                LogUtils.e(e);
//            }
//        }
//    }
//
//
//    /**
//     * dollar颜色修改
//     */
//    private static class DollarMatcherFilter implements Linkify.MatchFilter {
//
//        WeakReference<SpannableString> w;
//
//        public DollarMatcherFilter(SpannableString s) {
//            w = new WeakReference<>(s);
//        }
//
//        @Override
//        public boolean acceptMatch(CharSequence charSequence, int i, int i1) {
//            if (w != null && w.get() != null)
//                w.get().setSpan(new ForegroundColorSpan(Color.parseColor("#0083f2")), i, i1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            return false;
//        }
//    }
//
//    /**
//     * 链接改为文字展示  -- 网页链接
//     */
//
//    private static class LinkMacherFilter implements Linkify.MatchFilter {
//
//        WeakReference<SpannableString> w;
//
//        public LinkMacherFilter(SpannableString s) {
//            w = new WeakReference<>(s);
//        }
//
//        @Override
//        public boolean acceptMatch(CharSequence charSequence, int i, int i1) {
//            if (w != null && w.get() != null) {
//                SpannableString span = w.get();
////                Drawable drawable = FollowMeApp.getApplication().getResources().getDrawable(R.mipmap.followme_v2_icon_girl);
////                NoUnderlineSpan mNoUnderlineSpan = new NoUnderlineSpan();
////                span.setSpan(mNoUnderlineSpan, i, i1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
////                span.setSpan(new ImageSpan(drawable), i - 1, i1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
////                span.setSpan("网页链接", i, i1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
////                span.setSpan(new URLSpan(charSequence.toString()), i, i1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            }
//            return false;
//        }
//    }
//
//    /**
//     * 表情替换
//     *
//     * @param value
//     */
//    private static void addEmotions(SpannableString value) {
//        Matcher localMatcher = BlogPattern.EMOTION_URL.matcher(value);
//        while (localMatcher.find()) {
//            String str2 = localMatcher.group(0);
//            int k = localMatcher.start();
//            int m = localMatcher.end();
//            if (m - k < 8) {
//                try {
//                    int imageRes = EmoticonsData.getImageResByName(str2);
//                    Drawable drawable = Utils.getApp().getResources().getDrawable(imageRes);
//                    drawable.setBounds(0, 0, 60, 60);
//                    ImageSpanAlignCenter imageSpan = new ImageSpanAlignCenter(drawable);
//                    value.setSpan(imageSpan, k, m, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                } catch (Exception e) {
//
//                }
//            }
//        }
//    }
//
//    private static class EmotionMatcherFilter implements Linkify.MatchFilter {
//
//        WeakReference<SpannableString> w;
//
//        public EmotionMatcherFilter(SpannableString s) {
//            w = new WeakReference<>(s);
//        }
//
//        @Override
//        public boolean acceptMatch(CharSequence charSequence, int k, int m) {
//            if (w.get() == null) return false;
//            int imageRes = EmoticonsData.getImageResByName(charSequence.toString().trim());
//            if (imageRes <= 0) return false;
//            Drawable drawable = Utils.getApp().getResources().getDrawable(imageRes);
//            drawable.setBounds(0, 0, 60, 60);
//            ImageSpanAlignCenter imageSpan = new ImageSpanAlignCenter(drawable);
//            try {
//                w.get().setSpan(imageSpan, k, m, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return false;
//        }
//    }
//}
