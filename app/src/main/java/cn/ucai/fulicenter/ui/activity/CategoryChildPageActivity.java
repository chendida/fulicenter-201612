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

public class CategoryChildPageActivity extends AppCompatActivity {

    @BindView(R.id.tvTitleName)
    TextView tvTitleName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boutique_child);
        ButterKnife.bind(this);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, new NewGoodsFragment())
                .commit();
        tvTitleName.setText(getIntent().getStringExtra(I.Boutique.TITLE));
    }

    @OnClick(R.id.ivBack)
    public void onClick() {
        MFGT.finish(CategoryChildPageActivity.this);
    }
}

