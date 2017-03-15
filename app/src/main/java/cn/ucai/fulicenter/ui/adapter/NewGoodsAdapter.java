package cn.ucai.fulicenter.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.model.bean.NewGoodsBean;
import cn.ucai.fulicenter.model.utils.ImageLoader;

/**
 * Created by Administrator on 2017/3/15.
 */

public class NewGoodsAdapter extends RecyclerView.Adapter {
    private static final int TYPE_FOOTER = 1;
    private static final int TYPE_GOODS = 2;
    Context mContext;
    List<NewGoodsBean> mList;
    @BindView(R.id.ivAvatar)
    ImageView ivAvatar;
    @BindView(R.id.tvGoodsName)
    TextView tvGoodsName;
    @BindView(R.id.tvGoodsPrice)
    TextView tvGoodsPrice;

    String footerText = "加载更多";
    boolean isMore;

    public boolean isMore() {
        return isMore;
    }

    public void setIsMore(boolean more) {
        isMore = more;
    }

    public void setFooterText(String footerText) {
        this.footerText = footerText;
        notifyDataSetChanged();
    }

    public NewGoodsAdapter(Context mContext, List<NewGoodsBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_FOOTER:
                return new FooterHolder(View.inflate(mContext, R.layout.footer, null));
            case TYPE_GOODS:
                return new NewGoodsHolder(View.inflate(mContext, R.layout.new_goods_info, null));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder parentHolder, int position) {
        if (getItemViewType(position) == TYPE_FOOTER){
            FooterHolder holder = (FooterHolder) parentHolder;
            holder.tvFooter.setText(footerText);
            return;
        }
        NewGoodsBean goods = mList.get(position);
        NewGoodsHolder holder = (NewGoodsHolder) parentHolder;
        holder.tvGoodsName.setText(goods.getGoodsName());
        holder.tvGoodsPrice.setText(goods.getCurrencyPrice());

        ImageLoader.downloadImg(mContext, holder.ivAvatar, goods.getGoodsThumb());
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return TYPE_FOOTER;
        }
        return TYPE_GOODS;
    }

    public void addGoods(List<NewGoodsBean> list) {
        this.mList.addAll(list);
        notifyDataSetChanged();
    }

    public void initGoods(ArrayList<NewGoodsBean> list) {
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

    static class FooterHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tvFooter)
        TextView tvFooter;

        FooterHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
