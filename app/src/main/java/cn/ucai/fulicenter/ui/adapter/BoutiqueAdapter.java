package cn.ucai.fulicenter.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.BoutiqueBean;
import cn.ucai.fulicenter.model.net.BoutiqueChildModel;
import cn.ucai.fulicenter.model.net.IBoutiqueChildModel;
import cn.ucai.fulicenter.model.utils.ImageLoader;
import cn.ucai.fulicenter.ui.activity.BoutiqueChildActivity;
import cn.ucai.fulicenter.ui.view.MFGT;

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
    public BoutiqueAdapter(Context mContext, List<BoutiqueBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BoutiqueHolder(View.inflate(mContext, R.layout.item_boutique, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder parentHolder, int position) {
        final BoutiqueBean goods = mList.get(position);
        BoutiqueHolder holder = (BoutiqueHolder) parentHolder;

        ImageLoader.downloadImg(mContext, holder.ivBoutiqueImg, goods.getImageurl());
        holder.tvBoutiqueTitle.setText(goods.getTitle());
        holder.tvBoutiqueName.setText(goods.getName());
        holder.tvBoutiqueDescription.setText(goods.getDescription());

        //设置点击跳转到二级页面
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MFGT.gotoBoutiqueChild(mContext,goods);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size() ;
    }

    class BoutiqueHolder extends RecyclerView.ViewHolder {
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
