package com.cretin.www.expandabletextview.model;

/**
 * @date: on 2018/9/20
 * @author: cretin
 * @email: mxnzp_life@163.com
 * @desc: ViewModel 不需要记住之前的状态
 */
public class ViewModel {
    private String title;

    public ViewModel(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
