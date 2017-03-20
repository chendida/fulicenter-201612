package cn.ucai.fulicenter.ui.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.model.bean.CategoryChildBean;
import cn.ucai.fulicenter.model.utils.CommonUtils;
import cn.ucai.fulicenter.model.utils.ImageLoader;
import cn.ucai.fulicenter.model.utils.L;


/**
 * Created by Administrator on 2017/3/17.
 */

public class CatFilterCategoryButton extends Button {
    private static final String TAG = "CatFilterCategoryButton";
    boolean isExpand = false;
    Context mContext;
    PopupWindow mPopupWindow;
    GridView gv;
    CatFilterAdapter mAdapter;
    ArrayList<CategoryChildBean> list;
    public CatFilterCategoryButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setCatFilterOnClickListener();
    }

    private void setCatFilterOnClickListener() {
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isExpand) {//点击时判断PopupWindow的状态是否展开，是展开的话就隐藏，否则展开
                    L.e(TAG, "setOnClickListener" + isExpand + "1111");
                    if (mPopupWindow != null && mPopupWindow.isShowing()) {
                        mPopupWindow.dismiss();
                    }
                } else {
                    initPop();
                }
                showArrow();
                L.e(TAG, "setOnClickListenerOver" + isExpand);
            }
        });
    }

    private void initPop() {
        if (mPopupWindow == null) {
            mPopupWindow = new PopupWindow(mContext);
            //设置宽度和高度
            mPopupWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
            mPopupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
            //设置背景颜色
            mPopupWindow.setBackgroundDrawable(new ColorDrawable(0xbb000000));
            mPopupWindow.setContentView(gv);
        }
        mPopupWindow.showAsDropDown(this);
    }

    private void showArrow() {
        L.e(TAG, "setOnClickListener" + isExpand);
        Drawable end = getResources().getDrawable(isExpand ? R.drawable.arrow2_up
                : R.drawable.arrow2_down);
        setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, end, null);
        isExpand = !isExpand;
    }

    public void initView(String groupName, ArrayList<CategoryChildBean> list) {
        if (groupName == null || list == null){
            CommonUtils.showShortToast("获取数据显示异常");
            return;
        }
        gv = new GridView(mContext);
        this.setText(groupName);
        mAdapter = new CatFilterAdapter(mContext,list);
        gv.setAdapter(mAdapter);
    }

    class CatFilterAdapter extends BaseAdapter {
        Context context;
        ArrayList<CategoryChildBean> list;

        public CatFilterAdapter(Context context, ArrayList<CategoryChildBean> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list != null ? list.size() : 0;
        }

        @Override
        public CategoryChildBean getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CatFilterHolder holder = null;
            if (convertView==null) {
                convertView = View.inflate(context, R.layout.cat_filter, null);
                holder = new CatFilterHolder(convertView);
                convertView.setTag(holder);
            }else {
                holder = (CatFilterHolder) convertView.getTag();
            }
            holder.bind(position);
            return convertView;
        }

        class CatFilterHolder {
            @BindView(R.id.ivCategoryChildThumb)
            ImageView ivCategoryChildThumb;
            @BindView(R.id.tvCategoryChildName)
            TextView tvCategoryChildName;

            CatFilterHolder(View view) {
                ButterKnife.bind(this, view);
            }

            public void bind(int position) {
                CategoryChildBean bean = list.get(position);
                tvCategoryChildName.setText(bean.getName());
                ImageLoader.downloadImg(context,ivCategoryChildThumb,bean.getImageUrl());
            }
        }
    }
}
