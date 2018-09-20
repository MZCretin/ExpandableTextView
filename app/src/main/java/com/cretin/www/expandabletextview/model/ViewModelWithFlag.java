package com.cretin.www.expandabletextview.model;

import com.ctetin.expandabletextviewlibrary.app.StatusType;
import com.ctetin.expandabletextviewlibrary.model.ExpandableStatusFix;

/**
 * @date: on 2018/9/20
 * @author: cretin
 * @email: mxnzp_life@163.com
 * @desc: 如果你需要在滑动的时候保持之前的展开或者收起的状态
 * 则
 * 一、实现 ExpandableStatusFix
 * 二、在你的model中定义一个
 * private StatusType status;
 * 三、实现对应的方法，将你刚刚定义的status返回，
 * 四、并在给ExpandableTextView设置内容之前，调用bind方法
 */
public class ViewModelWithFlag implements ExpandableStatusFix {
    private StatusType status;

    public ViewModelWithFlag(String title) {
        this.title = title;
    }

    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void setStatus(StatusType status) {
        this.status = status;
    }

    @Override
    public StatusType getStatus() {
        return status;
    }
}
