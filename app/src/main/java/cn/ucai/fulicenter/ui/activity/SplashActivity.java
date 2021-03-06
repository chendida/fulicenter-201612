package cn.ucai.fulicenter.ui.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.dao.UserDao;
import cn.ucai.fulicenter.model.utils.SharePrefrenceUtils;
import cn.ucai.fulicenter.ui.view.MFGT;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = SplashActivity.class.getSimpleName();
    int time = 1000;
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.btnOnClick)
    Button btnOnClick;
    Unbinder bind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        bind = ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //将数据保存到内存中
                String userName = SharePrefrenceUtils.getInstance().getUserName();
                if (userName != null){
                    User user = UserDao.getInstance(SplashActivity.this).getUserInfo(userName);
                    Log.e(TAG,"SplashActivity,user = " + user);
                    FuLiCenterApplication.setUser(user);
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 3; i > 0 ; i--){
                            final int tv_time = i;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tvTime.setText(tv_time +"");
                                }
                            });
                            SystemClock.sleep(1000);
                        }
                    MFGT.gotoMain(SplashActivity.this);
                    SplashActivity.this.finish();
                    }
                }).start();
            }
        },time);
    }

    @OnClick(R.id.btnOnClick)
    public void onClick() {
        MFGT.gotoMain(SplashActivity.this);
        SplashActivity.this.finish();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bind != null){
            bind.unbind();
        }
    }
}
