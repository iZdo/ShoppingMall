package com.izdo.shoppingmall.http;

import android.content.Context;
import android.content.Intent;

import com.izdo.shoppingmall.LoginActivity;
import com.izdo.shoppingmall.MyApplication;
import com.izdo.shoppingmall.R;
import com.izdo.shoppingmall.utils.ToastUtils;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

/**
 * Created by iZdo on 2017/8/24.
 */

public abstract class SimpleCallback<T> extends BaseCallback<T> {

    protected Context mContext;

    public SimpleCallback(Context context){

        mContext = context;

    }

    @Override
    public void onBeforeRequest(Request request) {

    }

    @Override
    public void onFailure(Request request, Exception e) {

    }

    @Override
    public void onResponse(Response response) {

    }

    @Override
    public void onTokenError(Response response, int code) {
        ToastUtils.show(mContext, mContext.getString(R.string.token_error));

        Intent intent = new Intent();
        intent.setClass(mContext, LoginActivity.class);
        mContext.startActivity(intent);

        MyApplication.getInstance().clearUser();

    }
}
