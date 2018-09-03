package com.cretin.www.expandabletextview;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.text.util.LinkifyCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.util.Linkify;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ctetin.expandabletextviewlibrary.ExpandableTextView;

import java.util.regex.Pattern;


/**
 * 联系我：
 * 792075058@qq.com
 * <p>
 * 博客地址：
 * https://www.jianshu.com/p/b7a8ddc639db
 * <p>
 * Github地址(您的star是对我最大的鼓励)：
 * https://github.com/MZCretin/ExpandableTextView
 */
public class MainActivity extends AppCompatActivity {
    private ExpandableTextView[] views;
    private TextView[] tips;
    private String[] indexs = new String[]{
            "3,5;6,9;10,12",
            "3,5;6,9;10,11;19,20",
            "3,5;6,9;10,11;19,21",
            "3,5;6,9;10,11;14,16",
            "3,5;6,9;10,11;14,15;20,21",
            "3,5;6,9;10,11;14,16;21,22",
    };

    private TextView tvTips00;

    private ExpandableTextView.OnLinkClickListener linkClickListener = (type, content) -> {
        if (type.equals(ExpandableTextView.LinkType.LINK_TYPE)) {
            Toast.makeText(MainActivity.this, "你点击了链接 内容是：" + content, Toast.LENGTH_SHORT).show();
        } else if (type.equals(ExpandableTextView.LinkType.MENTION_TYPE)) {
            Toast.makeText(MainActivity.this, "你点击了@用户 内容是：" + content, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        views = new ExpandableTextView[6];
        tips = new TextView[6];
        views[0] = findViewById(R.id.ep_01);
        views[1] = findViewById(R.id.ep_02);
        views[2] = findViewById(R.id.ep_03);
        views[3] = findViewById(R.id.ep_04);
        views[4] = findViewById(R.id.ep_05);
        views[5] = findViewById(R.id.ep_06);
        tips[0] = findViewById(R.id.tv_tips01);
        tips[1] = findViewById(R.id.tv_tips02);
        tips[2] = findViewById(R.id.tv_tips03);
        tips[3] = findViewById(R.id.tv_tips04);
        tips[4] = findViewById(R.id.tv_tips05);
        tips[5] = findViewById(R.id.tv_tips06);
        tvTips00 = findViewById(R.id.tv_tips00);

        setTips();

        String yourText = "  我所认识的中国，强大、友好。@奥特曼 “一带一路”经济带带动了沿线国家的经济发展，促进我国与他国的友好往来和贸易发展，可谓“双赢”。http://www.baidu.com 自古以来，中国以和平、友好的面孔示人。汉武帝派张骞出使西域，开辟丝绸之路，增进与西域各国的友好往来。http://www.baidu.com 胡麻、胡豆、香料等食材也随之传入中国，汇集于中华美食。@RNG 漠漠古道，驼铃阵阵，这条路奠定了“一带一路”的基础，让世界认识了中国。";
//        String yourText = "1\n2\n3\n4\n5\n6\n7\n8";
        views[0].setContent(yourText);
        views[0].setLinkClickListener(linkClickListener);

        views[1].setContent(yourText);
        views[1].setLinkClickListener(linkClickListener);

        views[2].setContent(yourText);
        views[2].setLinkClickListener(linkClickListener);

        views[3].setContent(yourText);
        views[3].setLinkClickListener(linkClickListener);

        views[4].setContent(yourText);
        views[4].setEndExpendContent(" 1小时前");
        views[4].setLinkClickListener(linkClickListener);

        views[5].setContent(yourText);
        views[5].setEndExpendContent(" 1小时前");
        views[5].setLinkClickListener(linkClickListener);

        findViewById(R.id.ll_ad).setOnClickListener(v -> {
            Uri uri = Uri.parse("http://a.app.qq.com/o/simple.jsp?pkgname=com.cretin");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });
    }

    /**
     * 设置tips
     */
    private void setTips() {
        //处理最上边的Tips
        final SpannableString value = SpannableString.valueOf(tvTips00.getText());
        LinkifyCompat.addLinks(value, Linkify.ALL);
        tvTips00.setMovementMethod(LinkMovementMethod.getInstance());
        tvTips00.setText(value);

        //处理剩下的
        for (int i = 0; i < indexs.length; i++) {
            String index = indexs[i];
            TextView view = tips[i];
            String[] split = index.split(";");
            SpannableStringBuilder spannableStringBuilder =
                    new SpannableStringBuilder(view.getText());
            for (String s :
                    split) {
                int x = Integer.parseInt(s.split(",")[0]) + 2;
                int y = Integer.parseInt(s.split(",")[1]) + 2;
                spannableStringBuilder.setSpan(
                        new ForegroundColorSpan(Color.parseColor("#FF6200")),
                        x, y, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            }
            view.setText(spannableStringBuilder);
        }
    }
}
