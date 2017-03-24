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
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.CartBean;
import cn.ucai.fulicenter.model.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.model.bean.MessageBean;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.net.CartModel;
import cn.ucai.fulicenter.model.net.ICartModel;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.utils.CommonUtils;
import cn.ucai.fulicenter.model.utils.ConvertUtils;
import cn.ucai.fulicenter.ui.activity.GoodsDetailsActivity;
import cn.ucai.fulicenter.ui.adapter.CartAdapter;
import cn.ucai.fulicenter.ui.view.MFGT;
import cn.ucai.fulicenter.ui.view.SpaceItemDecoration;
import cn.ucai.fulicenter.ui.view.utils.AntiShake;

/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends Fragment {
    private static final String TAG = CartFragment.class.getSimpleName();
    AntiShake util = new AntiShake();
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
    @BindView(R.id.layout_cart)
    RelativeLayout layoutCart;

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
    }

    @Override
    public void onResume() {
        super.onResume();
        model = new CartModel();
        initView();
        initData();
        setListener();
    }

    private void setListener() {
        mAdapter.setListener(listener);
        /*
        购物车中点击更新添加商品
         */
        mAdapter.setAddListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                Log.e(TAG,"onCheckedChanged,goodsId = " +position);
                if (util.check()) return;//防止连续点击
                if (user == null) {//没有用户登录
                    MFGT.gotoLoginActivity(getActivity(), 0);//跳转到登录界面
                }else {
                    updateCart(position);
                }
            }
        });

        /*
        购物车中点击更新减少商品
         */
        mAdapter.setDelListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                Log.e(TAG,"onCheckedChanged,goodsId = " +position);
                if (util.check()) return;//防止连续点击
                if (user == null) {//没有用户登录
                    MFGT.gotoLoginActivity(getActivity(), 0);//跳转到登录界面
                }else {
                    update_del_Cart(position);
                }
            }
        });
        /*
        下拉刷新
         */
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setRefresh(true);
                initData();
            }
        });
    }

    private void update_del_Cart(final int position) {
        CartBean cart = mList.get(position);
        final int count = cart.getCount();
        if (count <= 1){
            mList.remove(position);
            mAdapter.notifyDataSetChanged();
            return;
        }
        Log.e(TAG,"count = " + count);
        model.cartAction(getActivity(), I.ACTION_CART_UPDATA, String.valueOf(cart.getId())
                , String.valueOf(cart.getGoods().getGoodsId()), user.getMuserName()
                , (count - 1), new OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean result) {
                        if (result != null && result.isSuccess()){
                            CommonUtils.showShortToast(R.string.cart_update_success);
                            mList.get(position).setCount(count - 1);
                            mAdapter.notifyDataSetChanged();
                        }else {
                            CommonUtils.showShortToast(R.string.cart_update_fail);
                        }
                    }

                    @Override
                    public void onError(String error) {
                        CommonUtils.showShortToast(R.string.cart_update_fail);
                        Log.e(TAG,"onError,error = " +error);
                    }
                });
    }

    private void updateCart(final int position) {
        CartBean cart = mList.get(position);
        model.cartAction(getActivity(), I.ACTION_CART_UPDATA, String.valueOf(cart.getId())
                , String.valueOf(cart.getGoods().getGoodsId()), user.getMuserName()
                , (cart.getCount() + 1), new OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean result) {
                        if (result != null && result.isSuccess()){
                            CommonUtils.showShortToast(R.string.cart_update_success);
                            mList.get(position).setCount(mList.get(position).getCount() + 1);
                            mAdapter.notifyDataSetChanged();
                        }else {
                            CommonUtils.showShortToast(R.string.cart_update_fail);
                        }
                    }

                    @Override
                    public void onError(String error) {
                        CommonUtils.showShortToast(R.string.cart_update_fail);
                        Log.e(TAG,"onError,error = " +error);
                    }
                });
    }


    CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            Log.e(TAG,"onCheckedChanged,isChecked = " + isChecked);
            int position = (int) buttonView.getTag();
            Log.e(TAG,"onCheckedChanged,position = " + position);
            mList.get(position).setChecked(isChecked);
            setPriceText();
        }
    };

    private void setRefresh(boolean refresh) {
        srl.setRefreshing(refresh);
        tvFresh.setVisibility(refresh ? View.VISIBLE : View.GONE);
    }

    private void setCartListShow(boolean isShow) {
        tvNothing.setVisibility(isShow ? View.GONE : View.VISIBLE);
        layoutCart.setVisibility(isShow?View.VISIBLE:View.GONE);
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
        setCartListShow(false);
        setPriceText();
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
                setRefresh(false);
                setCartListShow(true);
                if (result != null) {
                    mList.clear();
                    if (result.length > 0) {
                        ArrayList<CartBean> list = ConvertUtils.array2List(result);
                        mList.addAll(list);
                        mAdapter.notifyDataSetChanged();
                    }else {
                        setCartListShow(false);
                    }
                }
            }

            @Override
            public void onError(String error) {
                Log.e(TAG, "onError,error = " + error);
                setRefresh(false);
            }
        });
    }
    /*
    计算购物车价钱
     */
    private void setPriceText(){
        int sumPrice = 0;
        int rankPrice = 0;
        for (CartBean cart:mList) {
            if (cart.isChecked()){
                GoodsDetailsBean goods = cart.getGoods();
                if (goods != null){
                    sumPrice += getPrice(goods.getCurrencyPrice())*cart.getCount();
                    rankPrice += getPrice(goods.getRankPrice()) * cart.getCount();
                }
            }
        }
        tvCartSumPrice.setText("合计：￥"+sumPrice);
        tvCartSavePrice.setText("节省：￥" + (sumPrice - rankPrice));
    }

    private int getPrice(String p){
        String price = p.substring(p.indexOf("￥") + 1);
        return Integer.valueOf(price);
    }
}
