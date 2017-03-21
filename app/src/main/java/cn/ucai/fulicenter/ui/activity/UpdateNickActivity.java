package cn.ucai.fulicenter.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

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
import cn.ucai.fulicenter.model.utils.ResultUtils;
import cn.ucai.fulicenter.ui.view.MFGT;

public class UpdateNickActivity extends AppCompatActivity {

    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.user_nick)
    EditText userNick;
    IUserRegister updateNickModel;
    String name;
    String nick;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_nick);
        ButterKnife.bind(this);
        name  = getIntent().getStringExtra(I.User.USER_NAME);
        userName.setText(name);
        initView();
    }

    private void initView() {
        updateNickModel = new UserRegister();
    }

    @OnClick(R.id.btn_ok)
    public void onClick() {
        if (inputCheck()){
            updateNickModel.updateNick(UpdateNickActivity.this, name, nick, new OnCompleteListener<String>() {
                @Override
                public void onSuccess(String s) {
                    Result result = ResultUtils.getResultFromJson(s, User.class);
                    if (result != null){
                        if (result.isRetMsg()){
                            updateSuccess();
                        }
                    }
                }

                @Override
                public void onError(String error) {

                }
            });
        }
    }

    private void updateSuccess() {
        MFGT.finish(UpdateNickActivity.this);
    }

    private boolean inputCheck() {
        nick = userNick.getText().toString().trim();
        if (TextUtils.isEmpty(nick)){
            userNick.requestFocus();
            userNick.setError(getString(R.string.nick_name_connot_be_empty));
            return false;
        }
        return true;
    }

    @OnClick(R.id.ivBack)
    public void back() {
        MFGT.finish(UpdateNickActivity.this);
    }
}
