package cn.ucai.fulicenter.model.net;

import android.content.Context;

import java.io.File;

import cn.ucai.fulicenter.model.bean.CollectBean;
import cn.ucai.fulicenter.model.bean.MessageBean;

/**
 * Created by Administrator on 2017/3/20.
 */

public interface IUserRegister {
    void login(Context context,String userName,String password,OnCompleteListener<String>listener);
    void register(Context context,String userName,String nick,String password,
                  OnCompleteListener<String>listener);
    void updateNick(Context context,String userName,String newNick,
                  OnCompleteListener<String>listener);
    void updateAvatar(Context context,String userName,File file,
                    OnCompleteListener<String>listener);
    /*
    加载用户收藏数量
     */
    void loadCollectCount(Context context,String userName, OnCompleteListener<MessageBean> listener);
    /*
    下载用户收藏商品列表
     */
    void loadCollectList(Context context,String userName,int pageId,int pageSize,
                         OnCompleteListener<CollectBean[]> listener);

}
