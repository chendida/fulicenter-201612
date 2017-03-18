package cn.ucai.fulicenter.ui.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.nfc.Tag;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.model.utils.L;


/**
 * Created by Administrator on 2017/3/17.
 */

public class CatFilterCategoryButton extends Button{
    private static final String TAG  = "CatFilterCategoryButton";
    boolean isExpand = false;
    Context mContext;
    PopupWindow mPopupWindow;
    public CatFilterCategoryButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setCatFilterOnClickListener();
    }

    private void setCatFilterOnClickListener() {
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isExpand){//点击时判断PopupWindow的状态是否展开，是展开的话就隐藏，否则展开
                    L.e(TAG,"setOnClickListener" + isExpand+"1111");
                    if (mPopupWindow != null && mPopupWindow.isShowing()){
                        mPopupWindow.dismiss();
                    }
                }else {
                    initPop();
                }
                showArrow();
                L.e(TAG,"setOnClickListenerOver" + isExpand);
            }
        });
    }

    private void initPop() {
        if (mPopupWindow == null){
            mPopupWindow = new PopupWindow(mContext);
            //设置宽度和高度
            mPopupWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
            mPopupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
            //设置背景颜色
            mPopupWindow.setBackgroundDrawable(new ColorDrawable(0xbb000000));
            TextView tv = new TextView(mContext);
            tv.setTextColor(getResources().getColor(R.color.red));
            tv.setTextSize(30);
            tv.setText("CatFilterCategoryButton");
            mPopupWindow.setContentView(tv);
        }
        mPopupWindow.showAsDropDown(this);
    }

    private void showArrow() {
        L.e(TAG,"setOnClickListener" + isExpand);
        Drawable end = getResources().getDrawable(isExpand? R.drawable.arrow2_up
                :R.drawable.arrow2_down);
        setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,end,null);
        isExpand = !isExpand;
    }
}
