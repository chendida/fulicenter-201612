package cn.ucai.fulicenter.application;

import android.app.Application;

/**
 * Created by Administrator on 2017/3/14.
 */

public class FuLiCenterApplication extends Application {
    static FuLiCenterApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static FuLiCenterApplication getInstance() {
        return instance;
    }
}
