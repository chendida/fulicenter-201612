package cn.ucai.fulicenter.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.model.net.GoodsModel;
import cn.ucai.fulicenter.model.net.IGoodsModel;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.ui.view.MFGT;

public class GoodsDetailsActivity extends AppCompatActivity {
    IGoodsModel model;
    int goodsId = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_details);
        goodsId = getIntent().getIntExtra(I.Goods.KEY_GOODS_ID,0);
        if (goodsId == 0){
            MFGT.finish(GoodsDetailsActivity.this);
            return;
        }
        model = new GoodsModel();
        initData();
    }

    private void initData() {
        model.loadData(GoodsDetailsActivity.this, goodsId,
                new OnCompleteListener<GoodsDetailsBean>() {
            @Override
            public void onSuccess(GoodsDetailsBean result) {

            }

            @Override
            public void onError(String error) {

            }
        });
    }
}
