package cn.ucai.fulicenter.model.net;

import android.content.Context;

import cn.ucai.fulicenter.model.bean.CartBean;
import cn.ucai.fulicenter.model.bean.MessageBean;

/**
 * Created by Administrator on 2017/3/24.
 */

public interface ICartModel {
    void loadCastData(Context context, String userName, OnCompleteListener<CartBean[]> listener);

    void cartAction(Context context, int action, String catId, String goodsId,
                    String userName, int count, OnCompleteListener<MessageBean> listener);
}
