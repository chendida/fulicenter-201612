package cn.ucai.fulicenter.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.Result;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.dao.UserDao;
import cn.ucai.fulicenter.model.net.IUserRegister;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.net.UserRegister;
import cn.ucai.fulicenter.model.utils.CommonUtils;
import cn.ucai.fulicenter.model.utils.MD5;
import cn.ucai.fulicenter.model.utils.ResultUtils;
import cn.ucai.fulicenter.model.utils.SharePrefrenceUtils;
import cn.ucai.fulicenter.ui.view.MFGT;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();
    @BindView(R.id.etUserName)
    EditText etUserName;
    @BindView(R.id.et_password)
    EditText etPassword;
    String userName;
    String password;
    IUserRegister userModel;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        userModel = new UserRegister();
    }

    @OnClick({R.id.btn_login, R.id.btn_register, R.id.ivBack})
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
        if (checkInput()){
            showDialog();
            userModel.login(LoginActivity.this, userName, MD5.getMessageDigest(password)
                    , new OnCompleteListener<String>() {
                @Override
                public void onSuccess(String s) {
                    Result result = ResultUtils.getResultFromJson(s, User.class);
                    if (result != null){
                        Log.e(TAG,"login" + 6666);
                        if (result.isRetMsg()) {
                            User user = (User) result.getRetData();
                            if (user != null) {
                                Log.e(TAG,"login" + 7777);
                                loginSuccess(user);
                            }
                        }else {
                            Log.e(TAG,"login" + 8888);
                            if (result.getRetCode() == I.MSG_LOGIN_UNKNOW_USER){
                                CommonUtils.showShortToast(R.string.login_fail_unknow_user);
                            }else if (result.getRetCode() == I.MSG_LOGIN_ERROR_PASSWORD){
                                CommonUtils.showShortToast(R.string.login_fail_error_password);
                            }
                        }
                    }
                    dialog.dismiss();
                }

                @Override
                public void onError(String error) {
                    CommonUtils.showShortToast(R.string.login_fail);
                    dialog.dismiss();
                }
            });
        }
    }

    private void loginSuccess(final User user) {
        setResult(RESULT_OK,new Intent().putExtra("login_result",4));
        Log.e(TAG,"loginSuccess,user = " + user.getMavatarSuffix());
        FuLiCenterApplication.setUser(user);
        Log.e(TAG,"loginSuccess,user2 = " + user.getMavatarSuffix());
        CommonUtils.showShortToast(R.string.login_success);
        SharePrefrenceUtils.getInstance().setUserName(user.getMuserName());//利用首选项保存用户名
        Log.e(TAG,"loginSuccess,user 3= " + user.getMavatarSuffix());
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean b = UserDao.getInstance(LoginActivity.this).saveUserInfo(user);
                Log.e(TAG,"loginSuccess,b = " + b);
            }
        }).start();
        MFGT.finish(LoginActivity.this);
    }

    private void showDialog() {
        dialog = new ProgressDialog(LoginActivity.this);
        dialog.setProgress(R.string.logining);
        dialog.show();
    }


    private boolean checkInput() {
        userName = etUserName.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        if (TextUtils.isEmpty(userName)){
            etUserName.requestFocus();
            etUserName.setError(getString(R.string.user_name_connot_be_empty));
            return false;
        }
        if (!userName.matches("[a-zA-Z]\\w{5,15}")){
            etUserName.requestFocus();
            etUserName.setError(getString(R.string.illegal_user_name));
            return false;
        }
        if (TextUtils.isEmpty(password)){
            etPassword.requestFocus();
            etPassword.setError(getString(R.string.password_connot_be_empty));
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        String userName = intent.getStringExtra(I.User.USER_NAME);
        etUserName.setText(userName);
    }
}
