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
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.Result;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.net.IUserRegister;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.net.UserRegister;
import cn.ucai.fulicenter.model.utils.CommonUtils;
import cn.ucai.fulicenter.model.utils.MD5;
import cn.ucai.fulicenter.model.utils.ResultUtils;
import cn.ucai.fulicenter.ui.fragment.BoutiqueFragment;
import cn.ucai.fulicenter.ui.view.MFGT;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    @BindView(R.id.et_UserName)
    EditText etUserName;
    @BindView(R.id.et_Nick)
    EditText etNick;
    @BindView(R.id.et_Password)
    EditText etPassword;
    @BindView(R.id.et_Config_Password)
    EditText etConfigPassword;
    String userName;
    String nick;
    String password;
    IUserRegister mRegisterModel;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        mRegisterModel = new UserRegister();
    }

    @OnClick({R.id.ivBack, R.id.btn_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                MFGT.finish(RegisterActivity.this);
                break;
            case R.id.btn_register:
                register();
                break;
        }
    }

    private void registerData() {
        Log.e(TAG,"register"+ "6666");
        mRegisterModel.register(RegisterActivity.this, userName, nick,
                MD5.getMessageDigest(password), new OnCompleteListener<String>() {
            @Override
            public void onSuccess(String result) {
                showDialog();
                Result user = ResultUtils.getResultFromJson(result,User.class);
                Log.e(TAG,"register"+ user.isRetMsg());
                if (user != null) {
                    if (user.isRetMsg()) {//注册成功
                        Log.e(TAG,"register"+ user.isRetMsg());
                        registerSuccess();
                    } else {//注册失败
                        if (user.getRetCode()== I.MSG_REGISTER_USERNAME_EXISTS){//用户已存在
                            CommonUtils.showShortToast(R.string.register_fail_exists);
                        }else {
                            registerFail();
                        }
                    }
                }
                dialog.dismiss();
            }

            @Override
            public void onError(String error) {
                Log.e(TAG,"register"+ error);
                registerFail();
                dialog.dismiss();
            }
        });
    }

    private void showDialog() {
        dialog = new ProgressDialog(RegisterActivity.this);
        dialog.setProgress(R.string.registering);
        dialog.show();
    }

    private void register() {
        if (checkInput()){//输入都正确
            registerData();//将数据发到服务端
        }else {//
            registerFail();
        }
    }

    private void registerFail() {//注册失败
        CommonUtils.showShortToast(R.string.register_fail);
    }

    private void registerSuccess() {
        setResult(RESULT_OK,new Intent().putExtra(I.User.USER_NAME,userName));
        CommonUtils.showShortToast(R.string.register_success);
        MFGT.finish(RegisterActivity.this);
    }

    private boolean checkInput() {
        userName = etUserName.getText().toString().trim();
        nick = etNick.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        String configPassword = etConfigPassword.getText().toString().trim();
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
        if (TextUtils.isEmpty(nick)){
            etNick.requestFocus();
            etNick.setError(getString(R.string.nick_name_connot_be_empty));
            return false;
        }
        if (TextUtils.isEmpty(password)){
            etPassword.requestFocus();
            etPassword.setError(getString(R.string.password_connot_be_empty));
            return false;
        }
        if (TextUtils.isEmpty(configPassword)){
            etConfigPassword.requestFocus();
            etConfigPassword.setError(getString(R.string.confirm_password_connot_be_empty));
            return false;
        }
        if (!configPassword.equals(password)){
            etConfigPassword.requestFocus();
            etConfigPassword.setError(getString(R.string.two_input_password));
            return false;
        }
        return true;
    }
}
