package cn.ucai.fulicenter.ui.adapter;

import android.content.Context;
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
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.CollectBean;
import cn.ucai.fulicenter.model.bean.MessageBean;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.net.GoodsModel;
import cn.ucai.fulicenter.model.net.IGoodsModel;
import cn.ucai.fulicenter.model.net.IUserRegister;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.net.UserRegister;
import cn.ucai.fulicenter.model.utils.CommonUtils;
import cn.ucai.fulicenter.model.utils.ImageLoader;
import cn.ucai.fulicenter.ui.activity.CollectDetailsActivity;
import cn.ucai.fulicenter.ui.view.FooterHolder;
import cn.ucai.fulicenter.ui.view.MFGT;

/**
 * Created by Administrator on 2017/3/15.
 */

public class CollectGoodsAdapter extends RecyclerView.Adapter {
    private static final String TAG = CollectDetailsActivity.class.getSimpleName();
    Context mContext;
    List<CollectBean> mList;
    @BindView(R.id.ivAvatar)
    ImageView ivAvatar;
    @BindView(R.id.tvGoodsName)
    TextView tvGoodsName;
    boolean isMore;
    User user;
    IGoodsModel model;
    public boolean isMore() {
        return isMore;
    }

    public void setIsMore(boolean more) {
        isMore = more;
    }


    public CollectGoodsAdapter(Context mContext, List<CollectBean> mList, User user) {
        this.mContext = mContext;
        this.mList = mList;
        this.user = user;
        isMore = true;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case I.TYPE_FOOTER:
                return new FooterHolder(View.inflate(mContext, R.layout.footer, null));
            case I.TYPE_ITEM:
                return new NewGoodsHolder(View.inflate(mContext, R.layout.collect_goods, null));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder parentHolder, int position) {
        if (getItemViewType(position) == I.TYPE_FOOTER) {
            FooterHolder holder = (FooterHolder) parentHolder;
            holder.setFooter(getFooterString());
            return;
        }
        NewGoodsHolder holder = (NewGoodsHolder) parentHolder;
        holder.bind(position);
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

    public void addGoods(List<CollectBean> list) {
        this.mList.addAll(list);
        notifyDataSetChanged();
    }

    public void initGoods(ArrayList<CollectBean> list) {
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
        @BindView(R.id.iv_collect_del)
        ImageView ivCollectDel;

        NewGoodsHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bind(final int position) {
            final CollectBean goods = mList.get(position);
            tvGoodsName.setText(goods.getGoodsName());
            ImageLoader.downloadImg(mContext, ivAvatar, goods.getGoodsThumb());
            //点击跳转到商品详情界面
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MFGT.gotoDetails(mContext, goods.getGoodsId());
                }
            });
            ivCollectDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadCollectCount(goods,position);
                    Log.e(TAG,"delete,success"+goods.getGoodsId());
                    notifyDataSetChanged();
                    //Log.e(TAG,"delete,success");
                }
            });
        }
    }
    private void loadCollectCount(final CollectBean goods, final int position) {
        Log.e(TAG,"delete,success"+111);
        if (user != null){
            Log.e(TAG,"delete,success" + 222);
            model = new GoodsModel();
            Log.e(TAG,"delete,success");
            model.loadCollectAction(mContext, I.ACTION_DELETE_COLLECT, goods.getGoodsId(),
                    user.getMuserName()
                    , new OnCompleteListener<MessageBean>() {
                        @Override
                        public void onSuccess(MessageBean result) {
                            if (result != null && result.isSuccess()){
                                Log.e(TAG,"delete,success");
                                mList.remove(goods);
                                CommonUtils.showShortToast("删除收藏成功");
                            }else {
                                CommonUtils.showShortToast(R.string.delete_collect_fail);
                            }
                        }

                        @Override
                        public void onError(String error) {
                            CommonUtils.showShortToast(R.string.delete_collect_fail);
                        }
                    });
        }
    }
}
