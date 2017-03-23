package cn.ucai.fulicenter.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.AlbumsBean;
import cn.ucai.fulicenter.model.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.model.bean.MessageBean;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.net.GoodsModel;
import cn.ucai.fulicenter.model.net.IGoodsModel;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.utils.CommonUtils;
import cn.ucai.fulicenter.ui.view.FlowIndicator;
import cn.ucai.fulicenter.ui.view.MFGT;
import cn.ucai.fulicenter.ui.view.SlideAutoLoopView;

public class GoodsDetailsActivity extends AppCompatActivity {
    IGoodsModel model;
    int goodsId = 0;
    @BindView(R.id.tvGoodEnglishName)
    TextView tvGoodEnglishName;
    @BindView(R.id.tvGoodName)
    TextView tvGoodName;
    @BindView(R.id.tvGoodPrice)
    TextView tvGoodPrice;
    @BindView(R.id.tvGoodCurrentPrice)
    TextView tvGoodCurrentPrice;
    @BindView(R.id.salv)
    SlideAutoLoopView salv;
    @BindView(R.id.indicator)
    FlowIndicator indicator;
    @BindView(R.id.wbGoodInfo)
    WebView wbGoodInfo;

    GoodsDetailsBean bean;
    @BindView(R.id.ivGoodCollect)
    ImageView ivGoodCollect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_details);
        ButterKnife.bind(this);
        goodsId = getIntent().getIntExtra(I.Goods.KEY_GOODS_ID, 0);
        if (goodsId == 0) {
            MFGT.finish(GoodsDetailsActivity.this);
            return;
        }
        model = new GoodsModel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        if (bean == null) {
            model.loadData(GoodsDetailsActivity.this, goodsId,
                    new OnCompleteListener<GoodsDetailsBean>() {
                        @Override
                        public void onSuccess(GoodsDetailsBean result) {
                            if (result != null) {
                                bean = result;
                                showDetails();
                            }
                        }

                        @Override
                        public void onError(String error) {
                            CommonUtils.showShortToast(error);
                        }
                    });
        }
        loadCollectStatus();
    }

    private void loadCollectStatus() {
        User user = FuLiCenterApplication.getUser();
        model.loadCollectStatus(GoodsDetailsActivity.this, goodsId, user.getMuserName(),
                new OnCompleteListener<MessageBean>() {
            @Override
            public void onSuccess(MessageBean result) {
                if (result != null && result.isSuccess()) {
                    setCollectStatus(true);
                } else {
                    setCollectStatus(false);
                }
            }

            @Override
            public void onError(String error) {
                setCollectStatus(false);
            }
        });
    }

    private void setCollectStatus(boolean isCollect) {
        ivGoodCollect.setImageResource(isCollect?R.mipmap.bg_collect_out:R.mipmap.bg_collect_in);
    }

    private void showDetails() {
        tvGoodEnglishName.setText(bean.getGoodsEnglishName());
        tvGoodName.setText(bean.getGoodsName());
        tvGoodPrice.setText(bean.getShopPrice());
        tvGoodCurrentPrice.setText(bean.getCurrencyPrice());
        salv.startPlayLoop(indicator, getAblumUrl(), getAblumCount());
        wbGoodInfo.loadDataWithBaseURL(null, bean.getGoodsBrief(), I.TEXT_HTML, I.UTF_8, null);
    }

    private int getAblumCount() {
        if (bean.getProperties() != null && bean.getProperties().length > 0) {
            return bean.getProperties()[0].getAlbums().length;
        }
        return 0;
    }

    private String[] getAblumUrl() {
        if (bean.getProperties() != null && bean.getProperties().length > 0) {
            AlbumsBean[] albums = bean.getProperties()[0].getAlbums();
            if (albums != null && albums.length > 0) {
                String[] urls = new String[albums.length];
                for (int i = 0; i < albums.length; i++) {
                    urls[i] = albums[0].getImgUrl();
                }
                return urls;
            }
        }
        return null;
    }


    @OnClick(R.id.ivBack)
    public void onClick() {
        MFGT.finish(GoodsDetailsActivity.this);
    }
}
