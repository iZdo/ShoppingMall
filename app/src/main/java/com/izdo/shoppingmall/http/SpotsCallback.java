package com.izdo.shoppingmall.http;

import android.content.Context;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import dmax.dialog.SpotsDialog;

/**
 * Created by iZdo on 2017/8/13.
 */

public abstract class SpotsCallback<T> extends BaseCallback<T> {

    private  Context mContext;

    SpotsDialog mDialog;

    public SpotsCallback(Context context) {
        mContext = context;

        initSpotsDialog();
    }

    private  void initSpotsDialog(){

        mDialog = new SpotsDialog(mContext,"拼命加载中...");

    }

    public void showDialog() {

        mDialog.show();
    }

    public  void dismissDialog(){
        mDialog.dismiss();
    }

    public void setLoadMessage(int resId){
        mDialog.setMessage(mContext.getString(resId));
    }

    @Override
    public void onRequestBefore(Request request) {
        showDialog();
    }

    @Override
    public void onFailure(Request request, IOException e) {
        dismissDialog();
    }

    @Override
    public void onResponse(Response response) {
        dismissDialog();
    }
}
