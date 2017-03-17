package cn.ucai.fulicenter.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.NewGoodsBean;
import cn.ucai.fulicenter.model.utils.ImageLoader;
import cn.ucai.fulicenter.ui.view.FooterHolder;
import cn.ucai.fulicenter.ui.view.MFGT;

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


    int softBy =  I.SORT_BY_ADDTIME_DESC;
    public void setSoftBy(int softBy) {
        this.softBy = softBy;
        softBy();
    }
    /*
    排序方法
     */
    private void softBy(){
        Collections.sort(mList, new Comparator<NewGoodsBean>() {
            @Override
            public int compare(NewGoodsBean left, NewGoodsBean right) {
                int result = 0;
                switch (softBy){
                    case I.SORT_BY_ADDTIME_DESC:
                        result = (int) (left.getAddTime() - right.getAddTime());
                        break;
                    case I.SORT_BY_ADDTIME_ASC:
                        result = (int) (right.getAddTime() - left.getAddTime());
                        break;
                    case I.SORT_BY_PRICE_DESC:
                        result = getPrice(left.getCurrencyPrice()) - getPrice(right.getCurrencyPrice());
                        break;
                    case I.SORT_BY_PRICE_ASC:
                        result = getPrice(right.getCurrencyPrice()) - getPrice(left.getCurrencyPrice());
                        break;
                }
                return result;
            }
        });
        notifyDataSetChanged();
    }
    private int getPrice(String p){
        String price = p.substring(p.indexOf("￥") + 1);
        return Integer.valueOf(price);
    }


    boolean isMore;

    public boolean isMore() {
        return isMore;
    }

    public void setIsMore(boolean more) {
        isMore = more;
    }


    public NewGoodsAdapter(Context mContext, List<NewGoodsBean> mList) {
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
                return new NewGoodsHolder(View.inflate(mContext, R.layout.new_goods_info, null));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder parentHolder, int position) {
        if (getItemViewType(position) == I.TYPE_FOOTER){
            FooterHolder holder = (FooterHolder) parentHolder;
            holder.setFooter(getFooterString());
            return;
        }
        final NewGoodsBean goods = mList.get(position);
        NewGoodsHolder holder = (NewGoodsHolder) parentHolder;
        holder.tvGoodsName.setText(goods.getGoodsName());
        holder.tvGoodsPrice.setText(goods.getCurrencyPrice());

        ImageLoader.downloadImg(mContext, holder.ivAvatar, goods.getGoodsThumb());

        //点击跳转到商品详情界面
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MFGT.gotoDetails(mContext,goods.getGoodsId());
            }
        });
    }

    private int getFooterString() {
        return isMore?R.string.load_more:R.string.no_more;
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

    class NewGoodsHolder extends RecyclerView.ViewHolder {
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
