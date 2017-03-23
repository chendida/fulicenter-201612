package cn.ucai.fulicenter.ui.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import java.util.ArrayList;

import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.BoutiqueBean;
import cn.ucai.fulicenter.model.bean.CategoryChildBean;
import cn.ucai.fulicenter.ui.activity.BoutiqueChildActivity;
import cn.ucai.fulicenter.ui.activity.CategoryChildPageActivity;
import cn.ucai.fulicenter.ui.activity.CollectDetailsActivity;
import cn.ucai.fulicenter.ui.activity.GoodsDetailsActivity;
import cn.ucai.fulicenter.ui.activity.LoginActivity;
import cn.ucai.fulicenter.ui.activity.MainActivity;
import cn.ucai.fulicenter.ui.activity.RegisterActivity;
import cn.ucai.fulicenter.ui.activity.SettingsActivity;
import cn.ucai.fulicenter.ui.activity.SplashActivity;
import cn.ucai.fulicenter.ui.activity.UpdateNickActivity;
import cn.ucai.fulicenter.ui.fragment.MimeFragment;

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
        startActivity((Activity) context, new Intent(context, CategoryChildPageActivity.class)
                .putExtra(I.NewAndBoutiqueGoods.CAT_ID, id)
                .putExtra(I.CategoryGroup.NAME, groupName)
                .putExtra(I.CategoryChild.DATA, list));
    }

    public static void gotoLoginActivity(Activity activity,int requestCode) {
        startActivityForResult(activity,new Intent(activity,LoginActivity.class), requestCode);
    }


    public static void gotoRegisterActivity(LoginActivity activity) {
        startActivityForResult(activity,new Intent(activity,RegisterActivity.class),
                I.REQUEST_CODE_REGISTER);
    }
    public static void startActivityForResult(Activity activity,Intent intent,int requestCode){
        activity.startActivityForResult(intent,requestCode);
        activity.overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
    }

    public static void gotoSettings(Activity activity) {
        startActivity(activity,SettingsActivity.class);
    }

    public static void gotoUpdateNickActivity(Activity activity) {
        startActivity(activity,UpdateNickActivity.class);
    }

    public static void gotoCollectDetailsActivity(Activity activity) {
        startActivity(activity,CollectDetailsActivity.class);
    }
}
