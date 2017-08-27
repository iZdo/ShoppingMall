package com.izdo.shoppingmall;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.izdo.shoppingmall.widget.ClearEditText;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.mob.MobSDK;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by iZdo on 2017/8/25.
 */

public class RegActivity extends BaseActivity {

    @ViewInject(R.id.get)
    private Button get;
    @ViewInject(R.id.submit)
    private Button submit;
    @ViewInject(R.id.edittxt_phone)
    private ClearEditText edittxt_phone;
    @ViewInject(R.id.edittxt_pwd)
    private ClearEditText edittxt_pwd;

    private EventHandler eh;
    public int smsFlage = 0;//0:设置为初始化值 1：请求获取验证码 2：提交用户输入的验证码判断是否正确

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        // 通过代码注册你的AppKey和AppSecret
        MobSDK.init(this, "2066f38a8efe1", "6f6f1cbf690b52137912d8e6e1d630ec");

        ViewUtils.inject(this);

        get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("aaa", "onClick: get");
                getSmsCode("86", edittxt_phone.getText().toString());
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("aaa", "onClick: submit");
                submitCode("86", edittxt_phone.getText().toString(), edittxt_pwd.getText().toString());
            }
        });

        this.eh = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    //回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        //提交验证码成功
                        myHandler.sendEmptyMessage(1);
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        //获取验证码成功
                        myHandler.sendEmptyMessage(2);
                    } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                        //返回支持发送验证码的国家列表
                    }
                } else {
                    ((Throwable) data).printStackTrace();
                    //此语句代表接口返回失败
                    //获取验证码失败。短信验证码验证失败（用flage标记来判断）
                    if (smsFlage == 1) {
                        myHandler.sendEmptyMessage(3);
                    } else if (smsFlage == 2) {
                        myHandler.sendEmptyMessage(4);
                    }
                }
            }
        };
        SMSSDK.registerEventHandler(eh);//注册短信回调
    }

    //2 获取短信验证码 请求获取短信验证码，在监听中返回
    private void getSmsCode(String country, String phone) {
        SMSSDK.getVerificationCode(country, phone);//请求获取短信验证码，在监听中返回
    }

    //3 提交验证码
    private void submitCode(String country, String phone, String code) {
        SMSSDK.submitVerificationCode(country, phone, code);//提交短信验证码，在监听中返回
    }

    //4 注销回调接口 registerEventHandler必须和unregisterEventHandler配套使用，否则可能造成内存泄漏。
    private void unregisterHandler() {
        SMSSDK.unregisterEventHandler(eh);
        Log.d("aaa", "注销回调接口");
    }

    Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:             //验证码验证成功
                    //执行bmob注册
                    //registerUser();
                    break;
                case 2:             //获取验证码成功,注意查看
                    Log.d("aaa", "获取验证码成功,注意查看");
                    break;
                case 3:             //获取验证码失败,请填写正确的手机号码
                    Log.d("aaa", "获取验证码失败,请填写正确的手机号码");
                    break;
                case 4:             //验证码验证错误
                    Log.d("aaa", "验证码错误");
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterHandler();
    }
}

