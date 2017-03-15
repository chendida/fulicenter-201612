package cn.ucai.fulicenter.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.ui.fragment.BoutiqueFragment;
import cn.ucai.fulicenter.ui.fragment.NewGoodsFragment;

public class MainActivity extends AppCompatActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bind = ButterKnife.bind(this);
        initFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment, mNewGoodsFragment)
                .add(R.id.fragment, mBoutiqueFragment)
                .hide(mBoutiqueFragment)
                .show(mNewGoodsFragment)
                .commit();
    }

    private void initFragment() {
        mFragments = new Fragment[2];
        mBoutiqueFragment = new BoutiqueFragment();
        mNewGoodsFragment = new NewGoodsFragment();
        mFragments[0] = mNewGoodsFragment;
        mFragments[1] = mBoutiqueFragment;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bind != null) {
            bind.unbind();
        }
    }

    @OnClick({R.id.rbNewGoods, R.id.rbBoutique})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rbNewGoods:
                index = 0;
                break;
            case R.id.rbBoutique:
                index = 1;
                break;
        }
        setFragment();
    }

    private void setFragment() {
        if (currentIndex != index){
            getSupportFragmentManager().beginTransaction()
                    .hide(mFragments[currentIndex])
                    .show(mFragments[index])
                    .commit();
            currentIndex = index;
        }
    }
}
