package cn.ucai.fulicenter.ui;

import java.util.ArrayList;
import java.util.List;

import cn.ucai.fulicenter.model.bean.CategoryChildBean;
import cn.ucai.fulicenter.model.bean.CategoryGroupBean;

/**
 * Created by Administrator on 2017/4/26.
 */

public class EventBus {
    ArrayList<CategoryChildBean> childList;
    List<CategoryGroupBean> groupList;

    public ArrayList<CategoryChildBean> getChildList() {
        return childList;
    }

    public void setChildList(ArrayList<CategoryChildBean> childList) {
        this.childList = childList;
    }

    public List<CategoryGroupBean> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<CategoryGroupBean> groupList) {
        this.groupList = groupList;
    }
}
