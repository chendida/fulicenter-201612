package cn.ucai.fulicenter.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.model.bean.NewGoodsBean;
import cn.ucai.fulicenter.model.utils.L;

/**
 * Created by Administrator on 2017/3/15.
 */

public class NewGoodsAdapter extends RecyclerView.Adapter {
    Context mContext;
    List<NewGoodsBean> mList;
    @BindView(R.id.ivAvatar)
    ImageView ivAvatar;
    @BindView(R.id.tvGoodsName)
    TextView tvGoodsName;
    @BindView(R.id.tvGoodsPrice)
    TextView tvGoodsPrice;

    public NewGoodsAdapter(Context mContext, List<NewGoodsBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        /*View view = View.inflate(mContext, R.layout.new_goods_info, null);
        NewGoodsHolder holder = new NewGoodsHolder(view);*/
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.new_goods_info, parent, false);
       return new NewGoodsHolder(inflate);
//        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder parentHolder, int position) {
        NewGoodsBean goods = mList.get(position);
        NewGoodsHolder holder = (NewGoodsHolder) parentHolder;
        L.e("leary", goods.toString());
        holder.tvGoodsName.setText(goods.getGoodsName());
        holder.tvGoodsPrice.setText(goods.getCurrencyPrice());
    }

    @Override
    public int getItemCount() {
       return mList ==null ? 0 : mList.size();
    }

    public void addGoods(List<NewGoodsBean> list) {
        if (mList != null) {
            this.mList.clear();
        }
        this.mList.addAll(list);
        notifyDataSetChanged();
    }

    static class NewGoodsHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivAvatar)
        ImageView ivAvatar;
        @BindView(R.id.tvGoodsName)
        TextView tvGoodsName;
        @BindView(R.id.tvGoodsPrice)
        TextView tvGoodsPrice;

        NewGoodsHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
