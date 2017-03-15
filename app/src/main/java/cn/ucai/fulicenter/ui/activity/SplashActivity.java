package cn.ucai.fulicenter.ui.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
    protected void onResume() {
        super.onResume();
        if (btnOnClick.isEnabled()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    tvTime.setText(3 + "");
                }
            }, 1000);
        }
        if (btnOnClick.isEnabled()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    tvTime.setText(2 + "");
                }
            }, 2000);
        }
        if (btnOnClick.isEnabled()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    tvTime.setText(1 + "");
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    SplashActivity.this.finish();
                }
            }, 3000);
        }
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
