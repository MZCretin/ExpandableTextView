package com.cretin.cretin.expandabletextview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hou on 2015/7/1.
 * <p>
 * 表情数据，因为放入asset中处理时CPU功率有点高，所以直接将表情放入MIPMAP文件夹中
 */
public class EmoticonsData {


    private static String[][] emoticons = {
            {"[啊]", String.valueOf(R.mipmap.a)},
            {"[哎呀]", String.valueOf(R.mipmap.aiya)},
            {"[唉]", String.valueOf(R.mipmap.ai)},
            {"[闭嘴]", String.valueOf(R.mipmap.bizui)},
            {"[憋嘴]", String.valueOf(R.mipmap.biezui)},
            {"[不高兴]", String.valueOf(R.mipmap.bugaoxing)},
            {"[不屑]", String.valueOf(R.mipmap.buxie)},
            {"[沉思]", String.valueOf(R.mipmap.chensi)},
            {"[吃惊]", String.valueOf(R.mipmap.chijing)},
            {"[大哭]", String.valueOf(R.mipmap.daku)},
            {"[大笑]", String.valueOf(R.mipmap.daxiao)},
            {"[飞吻]", String.valueOf(R.mipmap.feiwen)},
            {"[愤怒]", String.valueOf(R.mipmap.fennu)},
            {"[尴尬]", String.valueOf(R.mipmap.ganga)},
            {"[鬼脸]", String.valueOf(R.mipmap.guilian)},
            {"[哈哈]", String.valueOf(R.mipmap.haha)},
            {"[害羞]", String.valueOf(R.mipmap.haixiu)},
            {"[好吃]", String.valueOf(R.mipmap.haochi)},
            {"[嘿嘿]", String.valueOf(R.mipmap.heihei)},
            {"[哼哼]", String.valueOf(R.mipmap.hengheng)},
            {"[花心]", String.valueOf(R.mipmap.huaxin)},
            {"[激动]", String.valueOf(R.mipmap.jidong)},
            {"[紧张]", String.valueOf(R.mipmap.jinzhang)},
            {"[惊悚]", String.valueOf(R.mipmap.jingsong)},
            {"[惊讶]", String.valueOf(R.mipmap.jingya)},
            {"[开心]", String.valueOf(R.mipmap.kaixin)},
            {"[苦逼]", String.valueOf(R.mipmap.kubi)},
            {"[困惑]", String.valueOf(R.mipmap.kunhuo)},
            {"[困倦]", String.valueOf(R.mipmap.kunjuan)},
            {"[雷到]", String.valueOf(R.mipmap.leidao)},
            {"[冷汗]", String.valueOf(R.mipmap.lenghan)},
            {"[难过]", String.valueOf(R.mipmap.nanguo)},
            {"[难受]", String.valueOf(R.mipmap.nanshou)},
            {"[哦]", String.valueOf(R.mipmap.o)},
            {"[切]", String.valueOf(R.mipmap.qie)},
            {"[亲]", String.valueOf(R.mipmap.qin)},
            {"[亲亲]", String.valueOf(R.mipmap.qinqin)},
            {"[亲一口]", String.valueOf(R.mipmap.qinyikou)},
            {"[傻笑]", String.valueOf(R.mipmap.shaxiao)},
            {"[生病]", String.valueOf(R.mipmap.shengbing)},
            {"[生气]", String.valueOf(R.mipmap.shengqi)},
            {"[失望]", String.valueOf(R.mipmap.shiwang)},
            {"[帅气]", String.valueOf(R.mipmap.shuaiqi)},
            {"[睡觉]", String.valueOf(R.mipmap.shuijue)},
            {"[天使的笑]", String.valueOf(R.mipmap.tianshidexiao)},
            {"[偷偷笑]", String.valueOf(R.mipmap.toutouxiao)},
            {"[吐舌头]", String.valueOf(R.mipmap.tushetou)},
            {"[微笑]", String.valueOf(R.mipmap.weixiao)},
            {"[我汗]", String.valueOf(R.mipmap.wohan)},
            {"[我晕]", String.valueOf(R.mipmap.woyun)},
            {"[无语]", String.valueOf(R.mipmap.wuyu)},
            {"[嘻嘻]", String.valueOf(R.mipmap.xixi)},
            {"[羞涩]", String.valueOf(R.mipmap.xiuse)},
            {"[咦]", String.valueOf(R.mipmap.yi)},
            {"[眨眼]", String.valueOf(R.mipmap.zhayan)},
            {"[恶魔]", String.valueOf(R.mipmap.emo)},
            {"[幽灵]", String.valueOf(R.mipmap.youling)},
            {"[心]", String.valueOf(R.mipmap.xin)},
            {"[心碎]", String.valueOf(R.mipmap.xinsui)},
            {"[听不下去]", String.valueOf(R.mipmap.tingbuxiaqu)},
            {"[没眼看]", String.valueOf(R.mipmap.meiyankan)},
            {"[好臭]", String.valueOf(R.mipmap.haochou)},
            {"[拜托]", String.valueOf(R.mipmap.baituo)},
            {"[鄙视]", String.valueOf(R.mipmap.bishi)},
            {"[鼓掌]", String.valueOf(R.mipmap.guzhang)},
            {"[好]", String.valueOf(R.mipmap.hao)},
            {"[挥手]", String.valueOf(R.mipmap.huishou)},
            {"[强壮]", String.valueOf(R.mipmap.qiangzhuang)},
            {"[拳头]", String.valueOf(R.mipmap.quantou)},
            {"[上面]", String.valueOf(R.mipmap.shangmian)},
            {"[食指]", String.valueOf(R.mipmap.shizhi)},
            {"[手势]", String.valueOf(R.mipmap.shoushi)},
            {"[下面]", String.valueOf(R.mipmap.xiamian)},
            {"[耶]", String.valueOf(R.mipmap.ye)},
            {"[不要]", String.valueOf(R.mipmap.buyao)},
            {"[眼睛]", String.valueOf(R.mipmap.yanjing)},
            {"[兔女郎]", String.valueOf(R.mipmap.tunvlang)},
            {"[一坨屎]", String.valueOf(R.mipmap.yituoshi)},
            {"[困]", String.valueOf(R.mipmap.kun)},
            {"[闪电]", String.valueOf(R.mipmap.shandian)},
            {"[火焰]", String.valueOf(R.mipmap.huoyan)},
            {"[干杯]", String.valueOf(R.mipmap.ganbei)},
            {"[咖啡]", String.valueOf(R.mipmap.kafei)},
            {"[炸弹]", String.valueOf(R.mipmap.zhadan)},
            {"[爆炸]", String.valueOf(R.mipmap.baozha)},
            {"[危险]", String.valueOf(R.mipmap.weixian)},
            {"[心情]", String.valueOf(R.mipmap.xinqing)},
            {"[对]", String.valueOf(R.mipmap.dui)},
            {"[错]", String.valueOf(R.mipmap.cuo)},
            {"[问号]", String.valueOf(R.mipmap.wenhao)},
            {"[感叹号]", String.valueOf(R.mipmap.gantanhao)},
            {"[放大镜]", String.valueOf(R.mipmap.fangdajing)},
            {"[男孩]", String.valueOf(R.mipmap.nanhai)},
            {"[女孩]", String.valueOf(R.mipmap.nvhai)},
            {"[兔子]", String.valueOf(R.mipmap.tuzi)},
            {"[乌龟]", String.valueOf(R.mipmap.wugui)},
            {"[小狗]", String.valueOf(R.mipmap.xiaogou)},
            {"[熊]", String.valueOf(R.mipmap.xiong)},
            {"[熊猫]", String.valueOf(R.mipmap.xiongmao)},
            {"[猪鼻子]", String.valueOf(R.mipmap.zhubizi)},
            {"[猪头]", String.valueOf(R.mipmap.zhutou)},
            {"[中国]", String.valueOf(R.mipmap.zhongguo)},
            {"[德国]", String.valueOf(R.mipmap.deguo)},
            {"[俄罗斯]", String.valueOf(R.mipmap.eluosi)},
            {"[法国]", String.valueOf(R.mipmap.faguo)},
            {"[韩国]", String.valueOf(R.mipmap.hanguo)},
            {"[美国]", String.valueOf(R.mipmap.meiguo)},
            {"[日本]", String.valueOf(R.mipmap.riben)},
            {"[西班牙]", String.valueOf(R.mipmap.xibanya)},
            {"[意大利]", String.valueOf(R.mipmap.yidali)},
            {"[英国]", String.valueOf(R.mipmap.yingguo)},
            {"[英镑]", String.valueOf(R.mipmap.yingbang)},
            {"[日元]", String.valueOf(R.mipmap.riyuan)},
            {"[欧元]", String.valueOf(R.mipmap.ouyuan)},
            {"[美金]", String.valueOf(R.mipmap.meijin)},
            {"[信用卡]", String.valueOf(R.mipmap.xinyongka)},
            {"[美金符号]", String.valueOf(R.mipmap.meijinfuhao)},
            {"[钱]", String.valueOf(R.mipmap.qian)},
            {"[美元大涨]", String.valueOf(R.mipmap.meiyuandazhang)},
            {"[老虎机]", String.valueOf(R.mipmap.laohuji)},
            {"[空]", String.valueOf(R.mipmap.kong)},
            {"[满分]", String.valueOf(R.mipmap.manfen)},
            {"[趋势]", String.valueOf(R.mipmap.qushi)},
            {"[骰子]", String.valueOf(R.mipmap.touzi)},
            {"[图表]", String.valueOf(R.mipmap.tubiao)},
            {"[涨]", String.valueOf(R.mipmap.zhang)},
            {"[跌]", String.valueOf(R.mipmap.die)},
            {"[电脑]", String.valueOf(R.mipmap.diannao)},
            {"[top]", String.valueOf(R.mipmap.top)},
            {"[iPhone]", String.valueOf(R.mipmap.iphone)},
            {"[下降]", String.valueOf(R.mipmap.xiajiang)},
            {"[上涨]", String.valueOf(R.mipmap.shangzhang)},
            {"[上升]", String.valueOf(R.mipmap.shangsheng)},
            {"[up]", String.valueOf(R.mipmap.up)},
            {"[ok]", String.valueOf(R.mipmap.ok)},
            {"[cool]", String.valueOf(R.mipmap.cool)},
            {"[12点]", String.valueOf(R.mipmap._12dian)},
            {"[9点]", String.valueOf(R.mipmap._9dian)},
            {"[6点]", String.valueOf(R.mipmap._6dian)},
            {"[3点]", String.valueOf(R.mipmap._3dian)},
            {"[0]", String.valueOf(R.mipmap._0)},
            {"[1]", String.valueOf(R.mipmap._1)},
            {"[2]", String.valueOf(R.mipmap._2)},
            {"[3]", String.valueOf(R.mipmap._3)},
            {"[4]", String.valueOf(R.mipmap._4)},
            {"[5]", String.valueOf(R.mipmap._5)},
            {"[6]", String.valueOf(R.mipmap._6)},
            {"[7]", String.valueOf(R.mipmap._7)},
            {"[8]", String.valueOf(R.mipmap._8)},
            {"[9]", String.valueOf(R.mipmap._9)}
    };

    public static Map<String, List<EmoticonsModel>> map = new HashMap<String, List<EmoticonsModel>>() {
        {


            put("表情符号",
                    new ArrayList<EmoticonsModel>() {
                        {
                            int length = emoticons.length;
                            for (int i = 0; i < length; i++) {
                                add(new EmoticonsModel(emoticons[i][0], Integer.valueOf(emoticons[i][1])));
                            }
                        }
                    });
        }
    };

    public static int getImageResByName(String name) {
        for (int i = 0; i < emoticons.length; i++) {
            String key = emoticons[i][0];
            if (name.equals(key))
                return Integer.valueOf(emoticons[i][1]);
        }
        return -1;
    }
}
