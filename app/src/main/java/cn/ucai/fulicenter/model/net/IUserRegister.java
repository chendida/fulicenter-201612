package cn.ucai.fulicenter.model.net;

import android.content.Context;

import java.io.File;

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
}
