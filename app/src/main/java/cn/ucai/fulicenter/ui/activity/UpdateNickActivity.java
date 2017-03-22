package cn.ucai.fulicenter.ui.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

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
import cn.ucai.fulicenter.model.utils.ResultUtils;
import cn.ucai.fulicenter.model.utils.SharePrefrenceUtils;
import cn.ucai.fulicenter.ui.view.MFGT;

public class UpdateNickActivity extends AppCompatActivity {
    private static final String TAG = UpdateNickActivity.class.getSimpleName();
    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.user_nick)
    EditText userNick;
    IUserRegister updateNickModel;
    String name;
    String nick;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_nick);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        updateNickModel = new UserRegister();
        if (FuLiCenterApplication.getUser() != null){
            name = FuLiCenterApplication.getUser().getMuserName();
            userName.setText(name);
        }else {
            MFGT.finish(UpdateNickActivity.this);
        }
    }

    @OnClick(R.id.btn_ok)
    public void onClick() {
        if (inputCheck()){
            showDialog();
            updateNickModel.updateNick(UpdateNickActivity.this, name, nick, new OnCompleteListener<String>() {
                @Override
                public void onSuccess(String s) {
                    Result result = ResultUtils.getResultFromJson(s, User.class);
                    if (result != null){
                        if (result.isRetMsg()){
                            User user = (User) result.getRetData();
                            if (user != null) {
                                updateSuccess(user);
                            }
                        }else {
                            if (result.getRetCode() == I.MSG_USER_UPDATE_NICK_FAIL){
                                CommonUtils.showShortToast(R.string.update_fail);
                            }
                            if (result.getRetCode() == I.MSG_USER_SAME_NICK){
                                CommonUtils.showShortToast(R.string.update_nick_fail_unmodify);
                            }
                        }
                        dialog.dismiss();
                    }
                }

                @Override
                public void onError(String error) {
                    CommonUtils.showShortToast(R.string.update_fail);
                    dialog.dismiss();
                }
            });
        }
    }

    private void showDialog() {
        dialog = new ProgressDialog(UpdateNickActivity.this);
        dialog.setProgress(R.string.update_user_nick);
        dialog.show();
    }

    private void updateSuccess(final User u) {
        FuLiCenterApplication.setUser(u);
        CommonUtils.showShortToast(R.string.update_user_nick_success);
        SharePrefrenceUtils.getInstance().setUserName(u.getMuserName());//利用首选项保存用户名
        Log.e(TAG,"loginSuccess,user 3= " + u.getMavatarSuffix());
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean b = UserDao.getInstance(UpdateNickActivity.this).saveUserInfo(u);
                Log.e(TAG,"loginSuccess,b = " + b);
            }
        }).start();
        MFGT.finish(UpdateNickActivity.this);
    }

    private boolean inputCheck() {
        nick = userNick.getText().toString().trim();
        if (TextUtils.isEmpty(nick)){
            userNick.requestFocus();
            userNick.setError(getString(R.string.nick_name_connot_be_empty));
            return false;
        }if (FuLiCenterApplication.getUser().getMuserName().trim().equals(nick)){
            userNick.requestFocus();
            userNick.setError(getString(R.string.update_nick_fail_unmodify));
            return false;
        }
        return true;
    }

    @OnClick(R.id.ivBack)
    public void back() {
        MFGT.finish(UpdateNickActivity.this);
    }
}
