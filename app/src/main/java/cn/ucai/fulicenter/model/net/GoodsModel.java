package cn.ucai.fulicenter.model.net;

import android.content.Context;

import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.model.bean.MessageBean;
import cn.ucai.fulicenter.model.utils.OkHttpUtils;

/**
 * Created by Administrator on 2017/3/16.
 */

public class GoodsModel implements IGoodsModel {
    @Override
    public void loadData(Context context, int goodsId,
                         OnCompleteListener<GoodsDetailsBean> listener) {
        OkHttpUtils<GoodsDetailsBean>utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_GOOD_DETAILS)
                .addParam(I.Goods.KEY_GOODS_ID,String.valueOf(goodsId))
                .targetClass(GoodsDetailsBean.class)
                .execute(listener);

    }

    @Override
    public void loadCollectAction(Context context,int action, int goodsId, String userName,
                                  OnCompleteListener<MessageBean> listener) {
        String request = I.REQUEST_IS_COLLECT;
        if (action == I.ACTION_ADD_COLLECT){
            request = I.REQUEST_ADD_COLLECT;
        }else if (action == I.ACTION_DELETE_COLLECT){
            request = I.REQUEST_DELETE_COLLECT;
        }
        OkHttpUtils<MessageBean>utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(request)
                .addParam(I.Goods.KEY_GOODS_ID,String.valueOf(goodsId))
                .addParam(I.Collect.USER_NAME,userName)
                .targetClass(MessageBean.class)
                .execute(listener);
    }
}
