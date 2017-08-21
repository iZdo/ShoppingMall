package com.izdo.shoppingmall.adapter;

import android.content.Context;

import java.util.List;

/**
 * Created by iZdo on 2017/8/14.
 */

public abstract class SimpleAdapter<T> extends BaseAdapter<T, BaseViewHolder> {

    public SimpleAdapter(Context context, int layoutResId) {
        super(context, layoutResId);
    }

    public SimpleAdapter(Context context, int layoutResId, List<T> datas) {
        super(context, layoutResId, datas);
    }

}
