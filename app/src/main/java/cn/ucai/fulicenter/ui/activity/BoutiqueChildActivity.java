package cn.ucai.fulicenter.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.ui.fragment.NewGoodsFragment;
import cn.ucai.fulicenter.ui.view.MFGT;

/**
 * Created by Administrator on 2017/3/16.
 */
public class BoutiqueChildActivity extends AppCompatActivity {
    @BindView(R.id.tvTitleName)
    TextView tvTitleName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boutique_child);
        ButterKnife.bind(this);
        int i = (int) getIntent().getSerializableExtra(I.NewAndBoutiqueGoods.CAT_ID);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, new NewGoodsFragment())
                .commit();
        tvTitleName.setText(getIntent().getStringExtra(I.Boutique.TITLE));
    }

    @OnClick(R.id.ivBack)
    public void onClick() {
        MFGT.finish(BoutiqueChildActivity.this);
    }
}
