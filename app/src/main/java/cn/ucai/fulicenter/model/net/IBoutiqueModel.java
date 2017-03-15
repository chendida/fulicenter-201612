package cn.ucai.fulicenter.model.net;

import android.content.Context;

import cn.ucai.fulicenter.model.bean.BoutiqueBean;
import cn.ucai.fulicenter.model.bean.NewGoodsBean;

/**
 * Created by Administrator on 2017/3/15.
 */

public interface IBoutiqueModel {
    /**
     * 获取新品首页或精选二级页面的数据
     * @param context   上下文菜单
     * @param listener  OkHttpUtils.OnCompleteListener<NewGoodsBean[]>的子接口</>
     */
    void loadData(Context context,OnCompleteListener<BoutiqueBean[]> listener);
}
