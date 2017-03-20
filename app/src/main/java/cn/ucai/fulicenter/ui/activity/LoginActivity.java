package cn.ucai.fulicenter.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.ui.view.MFGT;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_login, R.id.btn_register,R.id.ivBack})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                MFGT.finish(LoginActivity.this);
                break;
            case R.id.btn_login:
                login();
                break;
            case R.id.btn_register:
                register();
                break;
        }
    }

    private void register() {
        MFGT.gotoRegisterActivity(LoginActivity.this);
    }

    private void login() {

    }

}
