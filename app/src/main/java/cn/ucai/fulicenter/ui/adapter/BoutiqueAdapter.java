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
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.BoutiqueBean;
import cn.ucai.fulicenter.model.utils.ImageLoader;

/**
 * Created by Administrator on 2017/3/15.
 */

public class BoutiqueAdapter extends RecyclerView.Adapter {
    Context mContext;
    List<BoutiqueBean> mList;
    @BindView(R.id.ivAvatar)
    ImageView ivAvatar;
    @BindView(R.id.tvGoodsName)
    TextView tvGoodsName;
    @BindView(R.id.tvGoodsPrice)
    TextView tvGoodsPrice;


    boolean isMore;

    public boolean isMore() {
        return isMore;
    }

    public void setIsMore(boolean more) {
        isMore = more;
    }


    public BoutiqueAdapter(Context mContext, List<BoutiqueBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
        isMore = true;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case I.TYPE_FOOTER:
                return new FooterHolder(View.inflate(mContext, R.layout.footer, null));
            case I.TYPE_ITEM:
                return new BoutiqueHolder(View.inflate(mContext, R.layout.item_boutique, null));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder parentHolder, int position) {
        if (getItemViewType(position) == I.TYPE_FOOTER) {
            FooterHolder holder = (FooterHolder) parentHolder;
            holder.tvFooter.setText(getFooterString());
            return;
        }
        BoutiqueBean goods = mList.get(position);
        BoutiqueHolder holder = (BoutiqueHolder) parentHolder;

        ImageLoader.downloadImg(mContext,holder.ivBoutiqueImg,goods.getImageurl());
        holder.tvBoutiqueTitle.setText(goods.getTitle());
        holder.tvBoutiqueName.setText(goods.getName());
        holder.tvBoutiqueDescription.setText(goods.getDescription());
    }

    private int getFooterString() {
        return isMore ? R.string.load_more : R.string.no_more;
    }

    @Override
    public int getItemCount() {
        return mList == null ? 1 : mList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return I.TYPE_FOOTER;
        }
        return I.TYPE_ITEM;
    }

    public void addGoods(List<BoutiqueBean> list) {
        this.mList.addAll(list);
        notifyDataSetChanged();
    }

    public void initGoods(ArrayList<BoutiqueBean> list) {
        if (mList != null) {
            this.mList.clear();
        }
        this.mList.addAll(list);
        notifyDataSetChanged();
    }

    static class FooterHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvFooter)
        TextView tvFooter;

        FooterHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    class BoutiqueHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.ivBoutiqueImg)
        ImageView ivBoutiqueImg;
        @BindView(R.id.tvBoutiqueTitle)
        TextView tvBoutiqueTitle;
        @BindView(R.id.tvBoutiqueName)
        TextView tvBoutiqueName;
        @BindView(R.id.tvBoutiqueDescription)
        TextView tvBoutiqueDescription;

        BoutiqueHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
