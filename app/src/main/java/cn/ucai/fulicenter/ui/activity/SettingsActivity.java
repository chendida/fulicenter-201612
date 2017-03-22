package cn.ucai.fulicenter.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
import cn.ucai.fulicenter.model.utils.ImageLoader;
import cn.ucai.fulicenter.model.utils.OnSetAvatarListener;
import cn.ucai.fulicenter.model.utils.ResultUtils;
import cn.ucai.fulicenter.ui.view.MFGT;

public class SettingsActivity extends AppCompatActivity {
    private static final String TAG = SettingsActivity.class.getSimpleName();
    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.user_nick)
    TextView userNick;
    @BindView(R.id.iv_user_profile_avatar)
    ImageView ivUserProfileAvatar;
    OnSetAvatarListener onSetAvatarListener;
    User user;
    String avatarName;
    IUserRegister updateAvatarModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        user = FuLiCenterApplication.getUser();
        if (user != null) {
            showUserInfo();
        } else {
            back();//结束
        }
    }

    private void showUserInfo() {
        userName.setText(user.getMuserName());
        ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user),SettingsActivity.this,ivUserProfileAvatar);
        userNick.setText(user.getMuserNick());
    }

    @OnClick(R.id.ivBack)
    public void back() {
        MFGT.finish(SettingsActivity.this);
    }

    @OnClick(R.id.btn_logout)
    public void onClick() {
        UserDao.getInstance(SettingsActivity.this).logout();
        MFGT.finish(SettingsActivity.this);
        MFGT.gotoLoginActivity(SettingsActivity.this, I.REQUEST_CODE_LOGIN);
    }

    @OnClick(R.id.update_nick)
    public void updateNick() {
        MFGT.gotoUpdateNickActivity(SettingsActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    @OnClick(R.id.layout_user_profile_avatar)
    public void avatarOnClick() {
        onSetAvatarListener = new OnSetAvatarListener(SettingsActivity.this
                ,R.id.layout_user_profile_avatar,user.getMuserName(),I.AVATAR_TYPE_USER_PATH);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "requestCode" + requestCode + ",data" + data + "resultCode" + resultCode);
        onSetAvatarListener.setAvatar(requestCode, data, ivUserProfileAvatar);
        if (requestCode == OnSetAvatarListener.REQUEST_CROP_PHOTO) {
            uploadAvatar();
        }
    }

    private void uploadAvatar() {
        updateAvatarModel  = new UserRegister();
        //File dir = new File(I.AVATAR_TYPE);
        final File file = new File(OnSetAvatarListener.getAvatarPath(SettingsActivity.this,
                I.AVATAR_TYPE_USER_PATH +"/"+user.getMuserName()
                        +I.AVATAR_SUFFIX_JPG));
        Log.e(TAG,"file="+file.exists());
        Log.e(TAG,"file="+file.getAbsolutePath());
        final ProgressDialog pd = new ProgressDialog(SettingsActivity.this);
        pd.setMessage(getResources().getString(R.string.update_user_avatar));
        pd.show();
        updateAvatarModel.updateAvatar(SettingsActivity.this, user.getMuserName(), file, new OnCompleteListener<String>() {
            @Override
            public void onSuccess(String s) {
                Log.e(TAG,"s="+s);
                Result result = ResultUtils.getResultFromJson(s,User.class);
                Log.e(TAG,"result="+result);
                if(result==null){
                    CommonUtils.showLongToast(R.string.update_user_avatar_fail);
                }else{
                    User u = (User) result.getRetData();
                    if(result.isRetMsg()){
                        FuLiCenterApplication.setUser(u);
                        ImageLoader.setAvatar(ImageLoader.getAvatarUrl(u),SettingsActivity.this,ivUserProfileAvatar);
                        CommonUtils.showLongToast(R.string.update_user_avatar_success);
                    }else{
                        CommonUtils.showLongToast(R.string.update_user_avatar_fail);
                    }
                }
                pd.dismiss();
            }

            @Override
            public void onError(String error) {
                pd.dismiss();
                CommonUtils.showLongToast(R.string.update_user_avatar_fail);
                Log.e(TAG,"error="+error);
            }
        });
    }

    private String getAvatatName() {
        avatarName = user.getMuserName() + System.currentTimeMillis();
        return avatarName;
    }

    @OnClick(R.id.rl_user_name)
    public void userNameOnClick() {
        CommonUtils.showShortToast(R.string.username_connot_be_modify);
    }
}
