package com.cretin.cretin.expandabletextview;

import java.util.regex.Pattern;

/**
 * @date: on 2018/8/20
 * @author: cretin
 * @email: mxnzp_life@163.com
 * @desc: 添加描述
 */
public class WebPattern {public static final Pattern WEB_URL = Pattern
//            .compile("http?://([-\\\\w\\\\.]+)+(:\\\\d+)?(/([\\\\w/_\\\\.]*(\\\\?\\\\S+)?)?)?");
//            .compile("^((https|http|ftp|rtsp|mms)?:\\/\\/)[^\\s]+");
        .compile("((http[s]{0,1}|ftp)://[a-zA-Z0-9\\.\\-]+\\.([a-zA-Z]{2,4})(:\\d+)?(/[a-zA-Z0-9\\.\\-~!@#$%^&*+?:_/=<>]*)?)|((www.)|[a-zA-Z0-9\\.\\-]+\\.([a-zA-Z]{2,4})(:\\d+)?(/[a-zA-Z0-9\\.\\-~!@#$%^&*+?:_/=<>]*)?)");

    public static final Pattern TOPIC_URL = Pattern
            .compile("#[\\p{Print}\\p{InCJKUnifiedIdeographs}&&[^#]]+#");
    public static final Pattern MENTION_URL = Pattern
            .compile("@[\\w\\p{InCJKUnifiedIdeographs}-]{1,26}");
    public static final Pattern EMOTION_URL = Pattern.compile("\\[(\\S+?)\\]");

    public static final Pattern DOLLAR = Pattern
            .compile("(\\$+(-[1-9]\\d*[.]\\d*))|(\\$+([1-9]\\d*.\\d*|0.\\d*[1-9]\\d*))");
    public static final Pattern SYMBOL = Pattern.compile("\\${1}\\w*/*\\w*\\${1}");

    private static final String APPLICATION_ID = "com.followme.followme";
    public static final String WEB_SCHEME = APPLICATION_ID + ".http://";
    //    public static final String WEB_SCHEME = "http://";
    public static final String TOPIC_SCHEME = APPLICATION_ID + ".topic://";
    public static final String MENTION_SCHEME = APPLICATION_ID + ".mention://";
}
