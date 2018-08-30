# ExpandableTextView
 实现类似微博内容，@用户，链接高亮，@用户和链接可点击跳转，可展开和收回的TextView。觉得好用别忘了star哦，你的star是对我最大的激励。

 ### 实现效果：

 <img src="./extra/demo.png"/>

### 使用方式：

#### Step 1. Add the JitPack repository to your build file

 Add it in your root build.gradle at the end of repositories:

 ```
 allprojects {
 		repositories {
 			...
 			maven { url 'https://jitpack.io' }
 		}
 	}
 ```

#### Step 2. Add the dependency

 ```
 dependencies {
 	        implementation 'com.github.MZCretin:ExpandableTextView:v1.0'
 	}
 ```

### 代码说明

+ 以下属性都可以在xml中设置
```xml
        <!--保留的行数-->
        <attr name="ep_max_line" format="integer" />
        <!--是否需要展开-->
        <attr name="ep_need_expand" format="boolean" />
        <!--是否需要收起 这个是建立在开启展开的基础上的-->
        <attr name="ep_need_contract" format="boolean" />
        <!--是否需要动画-->
        <attr name="ep_need_animation" format="boolean" />
        <!--收起的文案-->
        <attr name="ep_contract_text" format="string" />
        <!--展开的文案-->
        <attr name="ep_expand_text" format="string" />
        <!--展开的文字的颜色-->
        <attr name="ep_expand_color" format="color" />
        <!--收起的文字的颜色-->
        <attr name="ep_contract_color" format="color" />
        <!--在收回和展开前面添加的内容的字体颜色-->
        <attr name="ep_end_color" format="color" />
        <!--链接的文字的颜色-->
        <attr name="ep_link_color" format="color" />
        <!--链接的图标-->
        <attr name="ep_link_res" format="reference" />
```

+ java代码
```java
        ExpandableTextView expandableTextView = findViewById(R.id.ep_01);
        //需要显示的内容
        String yourText = "  我所认识的中国，强大、友好。@奥特曼 “一带一路”经济带带动了沿线国家的经济发展，促进我国与他国的友好往来和贸易发展，可谓“双赢”。http://www.baidu.com 自古以来，中国以和平、友好的面孔示人。汉武帝派张骞出使西域，开辟丝绸之路，增进与西域各国的友好往来。http://www.baidu.com 胡麻、胡豆、香料等食材也随之传入中国，汇集于中华美食。@RNG 漠漠古道，驼铃阵阵，这条路奠定了“一带一路”的基础，让世界认识了中国。";
        //将内容设置给控件
        expandableTextView.setContent(yourText);
        //xml中的属性也可以通过代码设置 比如
        expandableTextView.setmNeedExpend(true);
        //还有很多。。。。
        //添加点击监听
        expandableTextView.setLinkClickListener(new ExpandableTextView.OnLinkClickListener() {
            @Override
            public void onLinkClickListener(ExpandableTextView.LinkType linkType, String content) {
                //根据类型去判断
                if (linkType.equals(ExpandableTextView.LinkType.LINK_TYPE)) {
                    Toast.makeText(MainActivity.this, "你点击了链接 内容是：" + content, Toast.LENGTH_SHORT).show();
                } else if (linkType.equals(ExpandableTextView.LinkType.MENTION_TYPE)) {
                    Toast.makeText(MainActivity.this, "你点击了@用户 内容是：" + content, Toast.LENGTH_SHORT).show();
                }
            }
        });
```

### 实现思路讲解

**简书：** [【需求解决系列之三】Android 自定义可展开收回的ExpandableTextView](https://www.jianshu.com/p/5519fbab6907)

**掘金：** [【需求解决系列之三】Android 自定义可展开收回的ExpandableTextView](https://juejin.im/post/5b876a4de51d4571c5137660)
