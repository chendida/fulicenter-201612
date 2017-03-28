package cn.ucai.fulicenter.application;

import android.app.Application;
import android.util.Log;

import cn.sharesdk.framework.ShareSDK;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.dao.UserDao;
import cn.ucai.fulicenter.model.utils.SharePrefrenceUtils;

/**
 * Created by Administrator on 2017/3/14.
 */

public class FuLiCenterApplication extends Application {
    static FuLiCenterApplication instance;
    static User user;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        ShareSDK.initSDK(this);
    }

    public static FuLiCenterApplication getInstance() {
        return instance;
    }

    public static User getUser() {
        if (user == null){
            String userName = SharePrefrenceUtils.getInstance().getUserName();
            Log.e("application","userName =" + userName);
            User user = UserDao.getInstance(instance).getUserInfo(userName);
        }
        return user;
    }

    public static void setUser(User user) {
        FuLiCenterApplication.user = user;
    }
}
