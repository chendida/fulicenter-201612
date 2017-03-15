package cn.ucai.fulicenter.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.NewGoodsBean;
import cn.ucai.fulicenter.model.net.INewGoodsModel;
import cn.ucai.fulicenter.model.net.NewGoodsModel;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.utils.CommonUtils;
import cn.ucai.fulicenter.model.utils.ConvertUtils;
import cn.ucai.fulicenter.model.utils.L;
import cn.ucai.fulicenter.ui.adapter.NewGoodsAdapter;
import cn.ucai.fulicenter.ui.view.SpaceItemDecoration;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewGoodsFragment extends Fragment {
    private static final String TAG = NewGoodsFragment.class.getSimpleName();
    INewGoodsModel model;
    @BindView(R.id.rv_NewGoods)
    RecyclerView rvNewGoods;
    Unbinder bind;
    int pageId = 1;
    GridLayoutManager gm;
    NewGoodsAdapter mAdapter;
    List<NewGoodsBean> mList = new ArrayList<>();
    @BindView(R.id.tvFresh)
    TextView tvFresh;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_goods, container, false);
        bind = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        model = new NewGoodsModel();
        initView();
        initData(I.ACTION_DOWNLOAD);
        setListener();
    }

    private void setListener() {
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //ImageLoader.release();
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

    private void initView() {
        srl.setColorSchemeColors(
                getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_green),
                getResources().getColor(R.color.google_red),
                getResources().getColor(R.color.google_yellow));
        gm = new GridLayoutManager(getContext(), I.COLUM_NUM);
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

        mAdapter = new NewGoodsAdapter(getContext(), mList);
        rvNewGoods.setAdapter(mAdapter);

        rvNewGoods.addItemDecoration(new SpaceItemDecoration(24));
    }

    private void initData(final int action) {
        model.loadData(getContext(), pageId, new OnCompleteListener<NewGoodsBean[]>() {
            @Override
            public void onSuccess(NewGoodsBean[] result) {
                setRefresh(false);
                mAdapter.setIsMore(true);
                L.e(TAG, "initData,result = " + result);
                if (result != null && result.length > 0) {
                    ArrayList<NewGoodsBean> list = ConvertUtils.array2List(result);
                    if (action == I.ACTION_PULL_DOWN || action == I.ACTION_DOWNLOAD) {
                        mList.clear();
                    }
                    mList.addAll(list);
                    if (list.size() < I.PAGE_SIZE_DEFAULT){
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

    private void setRefresh(boolean refresh) {
        srl.setRefreshing(refresh);
        tvFresh.setVisibility(refresh?View.VISIBLE:View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (bind != null) {
            bind.unbind();
        }
    }
}
