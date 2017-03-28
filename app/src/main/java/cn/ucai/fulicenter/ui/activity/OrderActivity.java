package cn.ucai.fulicenter.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pingplusplus.android.PingppLog;
import com.pingplusplus.libone.PaymentHandler;
import com.pingplusplus.libone.PingppOne;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.ui.view.MFGT;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

public class OrderActivity extends AppCompatActivity {
    private static String URL = "http://218.244.151.190/demo/charge";
    int orderPrice = 0;
    @BindView(R.id.tvTitleName)
    TextView tvTitleName;
    @BindView(R.id.ed_order_name)
    EditText edOrderName;
    @BindView(R.id.ed_order_phone)
    EditText edOrderPhone;
    @BindView(R.id.tv_order_province)
    TextView tvOrderProvince;
    @BindView(R.id.spin_order_province)
    Spinner spinOrderProvince;
    @BindView(R.id.ed_order_street)
    EditText edOrderStreet;
    @BindView(R.id.tv_order_price)
    TextView tvOrderPrice;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);
        orderPrice = getIntent().getIntExtra(I.Cart.PAY_PRICE, 0);
        initView();
        //设置需要使用的支付方式
        PingppOne.enableChannels(new String[] { "wx", "alipay", "upacp", "bfb", "jdpay_wap" });

        // 提交数据的格式，默认格式为json
        // PingppOne.CONTENT_TYPE = "application/x-www-form-urlencoded";
        PingppOne.CONTENT_TYPE = "application/json";

        PingppLog.DEBUG = true;
    }

    private void initView() {
        tvTitleName.setText("填写收货地址");
    }

    @OnClick({R.id.ivBack, R.id.tv_order_buy})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                MFGT.finish(OrderActivity.this);
                break;
            case R.id.tv_order_buy:
                inputCheck();
                break;
        }
    }

    private void inputCheck() {
        user = FuLiCenterApplication.getUser();
        if (user != null) {
            String receiveName = edOrderName.getText().toString();
            if (TextUtils.isEmpty(receiveName)) {
                edOrderName.setError("收货人姓名不能为空");
                edOrderName.requestFocus();
                return ;
            }
            String mobile = edOrderPhone.getText().toString();
            if (TextUtils.isEmpty(mobile)) {
                edOrderPhone.setError("手机号码不能为空");
                edOrderPhone.requestFocus();
                return ;
            }
            if (!mobile.matches("[\\d]{11}")) {
                edOrderPhone.setError("手机号码格式错误");
                edOrderPhone.requestFocus();
                return ;
            }
            String area = spinOrderProvince.getSelectedItem().toString();
            if (TextUtils.isEmpty(area)) {
                Toast.makeText(OrderActivity.this, "收货地区不能为空", Toast.LENGTH_SHORT).show();
                return ;
            }
            String address = edOrderStreet.getText().toString();
            if (TextUtils.isEmpty(address)) {
                edOrderStreet.setError("街道地址不能为空");
                edOrderStreet.requestFocus();
                return ;
            }
            pay();
        }
    }

    private void pay() {
        // 产生个订单号
        String orderNo = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());

        // 计算总金额（以分为单位）
        int amount = orderPrice * 100;
        /*JSONArray billList = new JSONArray();
        for (Good good : mList) {
            amount += good.getPrice() * good.getCount() * 100;
            billList.put(good.getName() + " x " + good.getCount());
        }*/
        // 构建账单json对象
        JSONObject bill = new JSONObject();

        // 自定义的额外信息 选填
        JSONObject extras = new JSONObject();
        try {
            extras.put("extra1", "extra1");
            extras.put("extra2", "extra2");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            bill.put("order_no", orderNo);
            bill.put("amount", amount);
            bill.put("extras", extras);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //壹收款: 创建支付通道的对话框
        PingppOne.showPaymentChannels(this, bill.toString(), URL, new PaymentHandler() {
            @Override public void handlePaymentResult(Intent data) {
                if (data != null) {
                    /**
                     * code：支付结果码  -2:服务端错误、 -1：失败、 0：取消、1：成功
                     * error_msg：支付结果信息
                     */
                    int code = data.getExtras().getInt("code");
                    String result = data.getExtras().getString("result");
                }
            }
        });

    }
}
