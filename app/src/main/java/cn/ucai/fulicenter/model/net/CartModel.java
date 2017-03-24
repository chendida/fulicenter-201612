package cn.ucai.fulicenter.model.net;

import android.content.Context;

import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.CartBean;
import cn.ucai.fulicenter.model.bean.MessageBean;
import cn.ucai.fulicenter.model.utils.OkHttpUtils;

/**
 * Created by Administrator on 2017/3/24.
 */

public class CartModel implements ICartModel {
    @Override
    public void loadCastData(Context context, String userName, OnCompleteListener<CartBean[]> listener) {
        OkHttpUtils<CartBean[]>utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_CARTS)
                .addParam(I.Cart.USER_NAME,userName)
                .targetClass(CartBean[].class)
                .execute(listener);
    }

    @Override
    public void cartAction(Context context, int action, String catId, String goodsId,
                           String userName, int count, OnCompleteListener<MessageBean> listener) {
        OkHttpUtils<MessageBean>utils = new OkHttpUtils<>(context);
        if (action == I.ACTION_CART_ADD){
            addCart(utils,userName,goodsId,listener);
        }
        if (action == I.ACTION_CART_DEL){
            deleteCart(utils,catId,listener);
        }
        if (action == I.ACTION_CART_UPDATA){
            updateCart(utils,catId,count,listener);
        }
    }

    private void addCart(OkHttpUtils<MessageBean> utils, String userName, String goodsId,
                         OnCompleteListener<MessageBean> listener) {
        utils.setRequestUrl(I.REQUEST_ADD_CART)
                .addParam(I.Cart.USER_NAME,userName)
                .addParam(I.Goods.KEY_GOODS_ID,goodsId)
                .addParam(I.Cart.COUNT,String.valueOf(1))
                .addParam(I.Cart.IS_CHECKED,String.valueOf(0))
                .targetClass(MessageBean.class)
                .execute(listener);
    }

    private void deleteCart(OkHttpUtils<MessageBean> utils, String catId,
                            OnCompleteListener<MessageBean> listener) {
        utils.setRequestUrl(I.REQUEST_DELETE_CART)
                .addParam(I.Cart.ID,catId)
                .targetClass(MessageBean.class)
                .execute(listener);
    }

    private void updateCart(OkHttpUtils<MessageBean> utils, String catId,
                            int count, OnCompleteListener<MessageBean> listener) {
        utils.setRequestUrl(I.REQUEST_UPDATE_CART)
                .addParam(I.Cart.ID,catId)
                .addParam(I.Cart.COUNT,String.valueOf(count))
                .targetClass(MessageBean.class)
                .execute(listener);
    }
}
