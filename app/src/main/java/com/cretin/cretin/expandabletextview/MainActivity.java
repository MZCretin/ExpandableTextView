package com.cretin.cretin.expandabletextview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private SuperExpandTextView expandableTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        expandableTextView = findViewById(R.id.lorem_ipsum);

//        String yourText = "@cretin 哈哈";
        String yourText = "@cretin http://www.baidu.com http://www.baidu.com http://www.baidu.com http://www.baidu.com http://www.baidu.com http://www.baidu.com http://www.baidu.com http://www.baidu.com http://www.baidu.com http://www.baidu.com http://www.baidu.com 我们生活的我们生活的世界太是需要这我们生活的世界太需要这我们生活的世界太需要这我们生活的世界太[开心]😊需要这我们生活的世界太需要这样的清泉http://www.baidu.com清泉清泉清泉，我http://www.baidu.com 们的周围有太多的欺太多 http://www.baidu.com 的欺骗和谎言，我们多么想拥有一片承诺于人，守信于人的蓝天http://www.baidu.com ，有人因为欺骗而失去了最珍贵的友谊，有人因为谎言而众叛亲离，也有人因为欺 http://www.baidu.com骗而走上了生与死的边缘，试问，我们身边没有欺骗，那该有多好啊!有人为了信守承诺而至死不渝，那桃园里的三拜首，岂能忘记?关老爷的诚信又怎样?面对高官厚禄，面对加官进爵，他毅然过五关斩六将，留下千古美名，只为一个\"信\"字。我们生活的世界太需要这样的清泉，我们的周围有太多的欺骗和谎言，我们多么想拥有一片承诺于人，人因为欺骗而走上了生与死的边缘，试问，我们身边没有欺骗，那该有多好样?面对高官厚禄，面对加官进爵，他毅然过五关斩六将，留下千古美名，只为一个\"信\"字。我们生活的世界太需要这样的清泉，我们的周围有太多的欺骗和谎言，我们多么想拥有一片承诺于人，守信于人的蓝天，有人因为欺骗而失去了最珍贵的友谊，有人因为谎言而众叛亲离，也有人因为欺骗而走上了生与死的边缘，试问，我们身边没有欺骗，那该有多好啊!有人为了信守承诺而至死不渝，园里的三拜首，岂能忘记?关老爷的诚信又怎样?面对高官厚禄，面对加官进爵，他毅然过五关斩六将，留下千古美名，只为一信字。";
//        String yourText = "@cretin http://www.baidu.com [开心]😊@cretin http://www.baidu.com[开心]😊";
//        String yourText = "[啊][啊][哎呀][哎呀]";
//        String yourText = "有人因为[开心]欺骗而失去了最珍贵 @cretin 的友谊，有人因为谎言而众叛亲离，有人因为欺骗而失去了最珍贵的友谊，有人因为谎言而众叛亲离，有人因为欺骗而失去了最珍贵的友谊，有人因为谎言而众叛亲离，有人因为欺骗而失去了最珍贵的友谊，有人因为谎言而众叛亲离，有人因为欺骗而失去了最珍贵的友谊，有人因为谎言而众叛亲离，有人因为欺骗而失去了最珍贵的友谊，有人因为谎言而众叛亲离，有人因为欺骗而失去了最珍贵的友谊，有人因为谎言而众叛亲离，有人因为欺骗而失去了最珍贵的友谊，有人因为谎言而众叛亲离，有人因为欺骗而失去了最珍贵的友谊，有人因为谎言而众叛亲离，有人因为欺骗而失去了最珍贵的友谊，有人因为谎言而众叛亲离，";

        expandableTextView.setContent(yourText);

    }
}
