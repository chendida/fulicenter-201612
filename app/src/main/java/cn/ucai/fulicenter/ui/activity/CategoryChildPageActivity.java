package cn.ucai.fulicenter.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.ui.fragment.NewGoodsFragment;
import cn.ucai.fulicenter.ui.view.MFGT;

public class CategoryChildPageActivity extends AppCompatActivity {
    boolean softPrice;//按价格排序标志位
    boolean sortAddTime;//按时间排序标志位
    int softBy = I.SORT_BY_ADDTIME_DESC;
    @BindView(R.id.tvTitleName)
    TextView tvTitleName;
    NewGoodsFragment mNewGoodsFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_child_page);
        ButterKnife.bind(this);
        mNewGoodsFragment = new NewGoodsFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, mNewGoodsFragment)
                .commit();
        tvTitleName.setText(getIntent().getStringExtra(I.Boutique.TITLE));
    }

    @OnClick(R.id.ivBack)
    public void onClick() {
        MFGT.finish(CategoryChildPageActivity.this);
    }

    @OnClick({R.id.btn_sort_price, R.id.btn_sort_addtime})
    public void softList(View view) {
        switch (view.getId()) {
            case R.id.btn_sort_price:
                softBy = softPrice?I.SORT_BY_PRICE_ASC:I.SORT_BY_PRICE_DESC;
                softPrice = !softPrice;
                break;
            case R.id.btn_sort_addtime:
                softBy = sortAddTime?I.SORT_BY_ADDTIME_ASC:I.SORT_BY_ADDTIME_DESC;
                sortAddTime = !sortAddTime;
                break;
        }
        mNewGoodsFragment.softBy(softBy);
    }
}

