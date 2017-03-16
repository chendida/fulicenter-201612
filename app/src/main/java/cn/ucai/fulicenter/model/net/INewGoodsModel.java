package cn.ucai.fulicenter.model.net;

import android.content.Context;

import cn.ucai.fulicenter.model.bean.NewGoodsBean;

/**
 * Created by Administrator on 2017/3/15.
 */

public interface INewGoodsModel {
    /**
     * 获取新品首页或精选二级页面的数据
     * @param context   上下文菜单
     * @param pageId    分页的起始下标
     * @param listener  OkHttpUtils.OnCompleteListener<NewGoodsBean[]>的子接口</>
     */
    void loadData(Context context,int catId,int pageId,OnCompleteListener<NewGoodsBean[]> listener);
}
