package cn.ucai.fulicenter.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.utils.L;
import cn.ucai.fulicenter.ui.fragment.BoutiqueFragment;
import cn.ucai.fulicenter.ui.fragment.CartFragment;
import cn.ucai.fulicenter.ui.fragment.CategoryGroupFragment;
import cn.ucai.fulicenter.ui.fragment.MimeFragment;
import cn.ucai.fulicenter.ui.fragment.NewGoodsFragment;
import cn.ucai.fulicenter.ui.view.MFGT;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    int index = 0;
    int currentIndex = 0;
    @BindView(R.id.rbNewGoods)
    RadioButton rbNewGoods;
    @BindView(R.id.rbBoutique)
    RadioButton rbBoutique;
    @BindView(R.id.rbCategory)
    RadioButton rbCategory;
    @BindView(R.id.rbCart)
    RadioButton rbCart;
    @BindView(R.id.rbPerson_Center)
    RadioButton rbPersonCenter;
    @BindView(R.id.fragment)
    FrameLayout fragment;
    Unbinder bind;

    Fragment[] mFragments;
    NewGoodsFragment mNewGoodsFragment;
    BoutiqueFragment mBoutiqueFragment;
    CategoryGroupFragment mCategoryFragment;
    CartFragment mCartFragment;
    MimeFragment mMimeFragment;
    RadioButton[] radioButtons;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bind = ButterKnife.bind(this);
        initFragment();
        initRadioButton();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment, mNewGoodsFragment)
                .add(R.id.fragment, mBoutiqueFragment)
                .add(R.id.fragment,mCategoryFragment)
                .hide(mCategoryFragment)
                .hide(mBoutiqueFragment)
                .show(mNewGoodsFragment)
                .commit();
    }

    private void initRadioButton() {
        radioButtons = new RadioButton[5];
        radioButtons[0] = rbNewGoods;
        radioButtons[1] = rbBoutique;
        radioButtons[2] = rbCategory;
        radioButtons[3] = rbCart;
        radioButtons[4] = rbPersonCenter;

    }

    private void initFragment() {
        mFragments = new Fragment[5];
        mBoutiqueFragment = new BoutiqueFragment();
        mNewGoodsFragment = new NewGoodsFragment();
        mCategoryFragment = new CategoryGroupFragment();
        mCartFragment = new CartFragment();
        mMimeFragment = new MimeFragment();
        mFragments[0] = mNewGoodsFragment;
        mFragments[1] = mBoutiqueFragment;
        mFragments[2] = mCategoryFragment;
        mFragments[3] = mCartFragment;
        mFragments[4] = mMimeFragment;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bind != null) {
            bind.unbind();
        }
    }

    @OnClick({R.id.rbNewGoods, R.id.rbBoutique,R.id.rbCategory,R.id.rbCart,R.id.rbPerson_Center})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rbNewGoods:
                index = 0;
                break;
            case R.id.rbBoutique:
                index = 1;
                break;
            case R.id.rbCategory:
                index = 2;
                break;
            case R.id.rbCart:
                if (FuLiCenterApplication.getUser() == null){
                    Log.e(TAG,"goto"+index);
                    MFGT.gotoLoginActivity(MainActivity.this, I.REQUEST_CODE_LOGIN_FROM_CART);
                }else {
                    index = 3;
                }
                break;
            case R.id.rbPerson_Center:
                if (FuLiCenterApplication.getUser() == null){
                    MFGT.gotoLoginActivity(MainActivity.this,I.REQUEST_CODE_LOGIN);
                }else {
                    index = 4;
                }
                break;
        }
        setFragment();
    }

    private void setFragment() {
        if (currentIndex != index){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.hide(mFragments[currentIndex]);
            if (!mFragments[index].isAdded()){//该fragment没有被添加过
                ft.add(R.id.fragment,mFragments[index]);
            }
            ft.show(mFragments[index]).commitAllowingStateLoss();
            currentIndex = index;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG,"index =" + index+ ",currentIndex" + currentIndex);
        if (index == 4){
            //退出登录
            if (FuLiCenterApplication.getUser() == null){
                index = 0;
            }
            setFragment();
        }
        setRadioButton();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            //点击个人中心
            if (requestCode == I.REQUEST_CODE_LOGIN){
                index = 4;
            }
            //点击购物车
            if (requestCode == I.REQUEST_CODE_LOGIN_FROM_CART){
                index = 3;
            }
            setFragment();
            setRadioButton();
        }
    }

    private void setRadioButton() {
        for (int i = 0; i < radioButtons.length; i++){
            if (i == currentIndex){
                radioButtons[i].setChecked(true);
            }else {
                radioButtons[i].setChecked(false);
            }
        }
    }
}
