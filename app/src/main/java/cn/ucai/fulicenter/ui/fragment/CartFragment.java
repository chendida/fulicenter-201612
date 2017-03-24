package cn.ucai.fulicenter.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.model.bean.BoutiqueBean;
import cn.ucai.fulicenter.model.bean.CartBean;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.net.CartModel;
import cn.ucai.fulicenter.model.net.ICartModel;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.utils.CommonUtils;
import cn.ucai.fulicenter.model.utils.ConvertUtils;
import cn.ucai.fulicenter.ui.adapter.BoutiqueAdapter;
import cn.ucai.fulicenter.ui.adapter.CartAdapter;
import cn.ucai.fulicenter.ui.view.SpaceItemDecoration;

/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends Fragment {
    private static final String TAG = CartFragment.class.getSimpleName();
    ICartModel model;
    LinearLayoutManager gm;
    CartAdapter mAdapter;
    List<CartBean> mList = new ArrayList<>();
    @BindView(R.id.tv_cart_sum_price)
    TextView tvCartSumPrice;
    @BindView(R.id.tv_cart_save_price)
    TextView tvCartSavePrice;
    @BindView(R.id.tvFresh)
    TextView tvFresh;
    @BindView(R.id.tv_nothing)
    TextView tvNothing;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;
    User user;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        Log.e(TAG, "onCreateView");
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e(TAG, "onActivityCreated");
        model = new CartModel();
        initView();
        initData();
    }

    private void initView() {
        srl.setColorSchemeColors(
                getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_green),
                getResources().getColor(R.color.google_red),
                getResources().getColor(R.color.google_yellow));
        gm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(gm);
        rv.setHasFixedSize(true);

        mAdapter = new CartAdapter(getContext(), mList);
        rv.setAdapter(mAdapter);
        rv.addItemDecoration(new SpaceItemDecoration(24));
    }


    private void initData() {
        user = FuLiCenterApplication.getUser();
        if (user != null) {
            showCartList();
        }
    }

    private void showCartList() {
        model.loadCastData(getActivity(), user.getMuserName(), new OnCompleteListener<CartBean[]>() {
            @Override
            public void onSuccess(CartBean[] result) {
                if (result != null){
                    if (result.length > 0){
                        ArrayList<CartBean> list = ConvertUtils.array2List(result);
                        mList.addAll(list);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }
}
