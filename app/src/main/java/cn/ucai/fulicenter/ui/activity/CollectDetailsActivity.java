package cn.ucai.fulicenter.ui.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.CollectBean;
import cn.ucai.fulicenter.model.bean.NewGoodsBean;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.net.IUserRegister;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.net.UserRegister;
import cn.ucai.fulicenter.model.utils.CommonUtils;
import cn.ucai.fulicenter.model.utils.ConvertUtils;
import cn.ucai.fulicenter.model.utils.L;
import cn.ucai.fulicenter.ui.adapter.CollectGoodsAdapter;
import cn.ucai.fulicenter.ui.adapter.NewGoodsAdapter;
import cn.ucai.fulicenter.ui.fragment.NewGoodsFragment;
import cn.ucai.fulicenter.ui.view.MFGT;
import cn.ucai.fulicenter.ui.view.SpaceItemDecoration;

public class CollectDetailsActivity extends AppCompatActivity {
    private static final String TAG = CollectDetailsActivity.class.getSimpleName();
    @BindView(R.id.rv_NewGoods)
    RecyclerView rvNewGoods;
    GridLayoutManager gm;
    CollectGoodsAdapter mAdapter;
    List<CollectBean> mList = new ArrayList<>();
    @BindView(R.id.tvFresh)
    TextView tvFresh;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;
    @BindView(R.id.tvTitleName)
    TextView tvTitleName;
    IUserRegister userModel;
    User user;
    int pageId = 1;
    Unbinder bind;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_details);
        bind = ButterKnife.bind(this);
        /*userModel = new UserRegister();
        initView();
        initData(I.ACTION_DOWNLOAD);
        setListener();
        tvTitleName.setText("收藏宝贝");*/
    }
    private void setListener() {
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setRefresh(true);
                pageId = 1;
                initData(I.ACTION_PULL_DOWN);
            }
        });

        rvNewGoods.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastPosition = gm.findLastVisibleItemPosition();
                if (mAdapter.isMore() && RecyclerView.SCROLL_STATE_IDLE == newState
                        && lastPosition == mAdapter.getItemCount() - 1) {
                    pageId++;
                    initData(I.ACTION_PULL_UP);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        userModel = new UserRegister();
        initView();
        initData(I.ACTION_DOWNLOAD);
        setListener();
        tvTitleName.setText("收藏宝贝");
    }

    private void initView() {
        user = FuLiCenterApplication.getUser();
        srl.setColorSchemeColors(
                getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_green),
                getResources().getColor(R.color.google_red),
                getResources().getColor(R.color.google_yellow));
        gm = new GridLayoutManager(CollectDetailsActivity.this, I.COLUM_NUM);
        gm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == mList.size()) {
                    return I.COLUM_NUM;
                }
                return 1;
            }
        });
        rvNewGoods.setLayoutManager(gm);
        rvNewGoods.setHasFixedSize(true);

        mAdapter = new CollectGoodsAdapter(CollectDetailsActivity.this, mList,user);
        rvNewGoods.setAdapter(mAdapter);

        rvNewGoods.addItemDecoration(new SpaceItemDecoration(24));
    }


    private void setRefresh(boolean refresh) {
        //防止数据加载失败出现空指针而加的保护判断
        if (srl != null) {
            srl.setRefreshing(refresh);
        }
        if (tvFresh != null) {
            tvFresh.setVisibility(refresh ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (bind != null) {
            bind.unbind();
        }
    }
    private void initData(final int action) {
        if (user == null){
            MFGT.finish(CollectDetailsActivity.this);
            return;
        }
        userModel.loadCollectList(CollectDetailsActivity.this, user.getMuserName(), pageId
                , I.PAGE_SIZE_DEFAULT, new OnCompleteListener<CollectBean[]>() {
                    @Override
                    public void onSuccess(CollectBean[] result) {
                        setRefresh(false);
                        mAdapter.setIsMore(true);
                        L.e(TAG, "initData,result = " + result);
                        if (result != null && result.length > 0) {
                            ArrayList<CollectBean> list = ConvertUtils.array2List(result);
                            if (action == I.ACTION_PULL_DOWN || action == I.ACTION_DOWNLOAD) {
                                mList.clear();
                            }
                            mList.addAll(list);
                            if (list.size() < I.PAGE_SIZE_DEFAULT) {
                                mAdapter.setIsMore(false);
                            }
                            mAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(String error) {
                        L.e(TAG, "initData,error = " + error);
                        CommonUtils.showShortToast(error);
                        setRefresh(false);
                    }
                });
    }

    @OnClick(R.id.ivBack)
    public void onClick() {
        MFGT.finish(CollectDetailsActivity.this);
    }
}
