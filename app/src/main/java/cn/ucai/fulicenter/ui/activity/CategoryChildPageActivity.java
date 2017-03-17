package cn.ucai.fulicenter.ui.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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
    @BindView(R.id.btn_sort_price)
    Button btnSortPrice;
    @BindView(R.id.btn_sort_addtime)
    Button btnSortAddTime;

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
        Drawable end = null;
        switch (view.getId()) {
            case R.id.btn_sort_price:
                softBy = softPrice ? I.SORT_BY_PRICE_ASC : I.SORT_BY_PRICE_DESC;
                softPrice = !softPrice;
                end = getResources().getDrawable(softPrice?R.drawable.arrow_order_up
                        :R.drawable.arrow_order_down);
                btnSortPrice.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,end,null);
                break;
            case R.id.btn_sort_addtime:
                softBy = sortAddTime ? I.SORT_BY_ADDTIME_ASC : I.SORT_BY_ADDTIME_DESC;
                sortAddTime = !sortAddTime;
                end = getResources().getDrawable(sortAddTime?R.drawable.arrow_order_up
                        :R.drawable.arrow_order_down);
                btnSortAddTime.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,end,null);
                break;
        }
        mNewGoodsFragment.softBy(softBy);
    }
}

