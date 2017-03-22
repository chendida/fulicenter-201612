package cn.ucai.fulicenter.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.utils.ImageLoader;
import cn.ucai.fulicenter.ui.view.MFGT;

/**
 * A simple {@link Fragment} subclass.
 */
public class MimeFragment extends Fragment {

    private static final String TAG = MimeFragment.class.getSimpleName();
    @BindView(R.id.btn_setting)
    Button btnSetting;
    @BindView(R.id.ll_avatar)
    LinearLayout llAvatar;
    @BindView(R.id.collect_goods)
    LinearLayout collectGoods;
    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.tv_username)
    TextView tvUsername;
    User user;
    public MimeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mime, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    private void showUserInfo() {
        Log.e(TAG,"showUserInfo()" + user.getMuserName());
        tvUsername.setText(user.getMuserName());
        ImageLoader.downloadImg(getActivity(),ivAvatar,user.getAvatar());
    }

    @OnClick({R.id.btn_setting, R.id.ll_avatar})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_setting:
                MFGT.gotoSettings(getActivity());
                break;
            case R.id.ll_avatar:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        user = FuLiCenterApplication.getUser();
        if (user == null) {
            MFGT.gotoLoginActivity(getActivity());
        } else {
            showUserInfo();
        }
    }
}
