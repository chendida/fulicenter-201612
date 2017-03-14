package cn.ucai.fulicenter.ui.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.ucai.fulicenter.R;

public class SplashActivity extends AppCompatActivity {
    int time = 3000;
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
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        SplashActivity.this.finish();
                    }
        }, time);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                for (int i = 3; i > 0 ;i --) {
                    handler.sendEmptyMessage(i);
                }
                Looper.loop();
            }
        }).start();
    }




    @OnClick(R.id.btnOnClick)
    public void onClick() {
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
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
