package cn.ucai.fulicenter.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.dao.UserDao;
import cn.ucai.fulicenter.model.utils.ImageLoader;
import cn.ucai.fulicenter.ui.view.MFGT;

public class SettingsActivity extends AppCompatActivity {
    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.user_nick)
    TextView userNick;
    @BindView(R.id.iv_user_profile_avatar)
    ImageView ivUserProfileAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        User user = FuLiCenterApplication.getUser();
        if (user != null) {
            showUserInfo(user);
        } else {
            back();//结束
        }
    }

    private void showUserInfo(User user) {
        userName.setText(user.getMuserName());
        ImageLoader.downloadImg(SettingsActivity.this, ivUserProfileAvatar, user.getAvatar());
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
        MFGT.gotoLoginActivity(SettingsActivity.this);
    }
}
