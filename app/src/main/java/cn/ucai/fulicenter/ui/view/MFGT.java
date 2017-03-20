package cn.ucai.fulicenter.ui.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.BoutiqueBean;
import cn.ucai.fulicenter.model.bean.CategoryChildBean;
import cn.ucai.fulicenter.ui.activity.BoutiqueChildActivity;
import cn.ucai.fulicenter.ui.activity.CategoryChildPageActivity;
import cn.ucai.fulicenter.ui.activity.GoodsDetailsActivity;
import cn.ucai.fulicenter.ui.activity.MainActivity;
import cn.ucai.fulicenter.ui.activity.SplashActivity;

/**
 * Created by Administrator on 2017/3/16.
 */

public class MFGT {
    public static void startActivity(Activity activity, Class cls){
        activity.startActivity(new Intent(activity,cls));
        activity.overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
    }
    public static void finish(Activity activity){
        activity.finish();
        activity.overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }
    public static void startActivity(Activity activity,Intent intent){
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
    }

    public static void gotoMain(Activity activity) {
        startActivity(activity, MainActivity.class);
    }

    public static void gotoBoutiqueChild(Context activity, BoutiqueBean goods) {
        startActivity((Activity) activity,new Intent(activity, BoutiqueChildActivity.class)
        .putExtra(I.NewAndBoutiqueGoods.CAT_ID,goods.getId())
        .putExtra(I.Boutique.TITLE,goods.getTitle()));
    }

    public static void gotoDetails(Context activity, int goodsId) {
        startActivity((Activity)activity,new Intent(activity,GoodsDetailsActivity.class)
        .putExtra(I.Goods.KEY_GOODS_ID,goodsId));
    }


    public static void gotoCategoryChild(Context context, int id, String groupName,
                                         ArrayList<CategoryChildBean>list) {
        startActivity((Activity) context,new Intent(context,CategoryChildPageActivity.class)
                .putExtra(I.NewAndBoutiqueGoods.CAT_ID,id)
                .putExtra(I.CategoryGroup.NAME,groupName)
                .putExtra(I.CategoryChild.DATA,list));
    }
}
