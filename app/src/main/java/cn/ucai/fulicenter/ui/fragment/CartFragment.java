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
import butterknife.OnClick;
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
    boolean isChecked = false;

    int sumPrice;
    int rankPrice;

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
        购物车中点击更新更新商品
         */
        mAdapter.setAddListener(updateListener);
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

    private void updateCart(final int position, final int count) {
        CartBean cart = mList.get(position);
        GoodsDetailsBean goods = cart.getGoods();
        int action = cart.getCount() + count == 0 ? I.ACTION_CART_DEL : I.ACTION_CART_UPDATA;
        if (cart != null && goods != null) {
            model.cartAction(getActivity(), action, String.valueOf(cart.getId())
                    , String.valueOf(cart.getGoods().getGoodsId()), user.getMuserName()
                    , (cart.getCount() + count), new OnCompleteListener<MessageBean>() {
                        @Override
                        public void onSuccess(MessageBean result) {
                            if (result != null && result.isSuccess()) {
                                updateCartListView(position, count);
                            } else {
                                CommonUtils.showShortToast(R.string.cart_update_fail);
                            }
                        }

                        @Override
                        public void onError(String error) {
                            CommonUtils.showShortToast(R.string.cart_update_fail);
                            Log.e(TAG, "onError,error = " + error);
                        }
                    });
        }
    }

    private void updateCartListView(int position, int count) {
        if (mList.get(position).getCount() + count == 0) {
            mList.remove(position);
        } else {
            mList.get(position).setCount(mList.get(position).getCount() + count);
        }
        CommonUtils.showShortToast(R.string.cart_update_success);
        setCartListShow(!mList.isEmpty());
        mAdapter.notifyDataSetChanged();
        setPriceText();
    }

    View.OnClickListener updateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (util.check()) return;//防止连续点击
            if (user == null) {//没有用户登录
                MFGT.gotoLoginActivity(getActivity(), 0);//跳转到登录界面
            } else {
                int position = (int) v.getTag();
                int count = 0;
                Log.e(TAG, "updateAddListener,position = " + position);
                if (v.getTag(R.id.action_add_cart) != null) {
                    count = (int) v.getTag(R.id.action_add_cart);
                } else if (v.getTag(R.id.action_del_ccart) != null) {
                    count = (int) v.getTag(R.id.action_del_ccart);
                }
                updateCart(position, count);
            }
        }
    };
    CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean checked) {
            isChecked = checked;
            Log.e(TAG, "onCheckedChanged,isChecked = " + isChecked);
            int position = (int) buttonView.getTag();
            Log.e(TAG, "onCheckedChanged,position = " + position);
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
        layoutCart.setVisibility(isShow ? View.VISIBLE : View.GONE);
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
                mList.clear();
                setPriceText();//刷新选中状态时,同时更新价钱的显示
                setCartListShow(true);
                if (result != null) {
                    if (result.length > 0) {
                        ArrayList<CartBean> list = ConvertUtils.array2List(result);
                        if (list != null && list.size() > 0) {
                            mList.addAll(list);
                            mAdapter.notifyDataSetChanged();
                        }
                    } else {
                        setCartListShow(false);
                        setPriceText();
                    }
                }
            }

            @Override
            public void onError(String error) {
                Log.e(TAG, "onError,error = " + error);
                setRefresh(false);
                setPriceText();
            }
        });
    }

    /*
    计算购物车价钱
     */
    private void setPriceText() {
        sumPrice = 0;
        rankPrice = 0;
        for (CartBean cart : mList) {
            if (cart.isChecked()) {
                GoodsDetailsBean goods = cart.getGoods();
                if (goods != null) {
                    sumPrice += getPrice(goods.getCurrencyPrice()) * cart.getCount();
                    rankPrice += getPrice(goods.getRankPrice()) * cart.getCount();
                }
            }
        }
        tvCartSumPrice.setText("合计：￥" + sumPrice);
        tvCartSavePrice.setText("节省：￥" + (sumPrice - rankPrice));
    }

    private int getPrice(String p) {
        String price = p.substring(p.indexOf("￥") + 1);
        return Integer.valueOf(price);
    }
    /*
    结算按钮点击事件处理
     */
    @OnClick(R.id.tv_cart_buy)
    public void orderList() {
        if (sumPrice > 0){
            MFGT.gotoOrderActivity(getActivity(),sumPrice);
        }else {
            CommonUtils.showShortToast(R.string.order_nothing);
        }
    }
}
