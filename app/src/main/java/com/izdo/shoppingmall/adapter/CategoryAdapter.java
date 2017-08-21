package com.izdo.shoppingmall.adapter;

import android.content.Context;

import com.izdo.shoppingmall.R;
import com.izdo.shoppingmall.bean.Category;

import java.util.List;

/**
 * Created by iZdo on 2017/8/15.
 */

public class CategoryAdapter extends SimpleAdapter<Category> {



    public CategoryAdapter(Context context, List<Category> datas) {
        super(context, R.layout.template_single_text, datas);
    }

    @Override
    protected void convert(BaseViewHolder viewHoder, Category item) {


        viewHoder.getTextView(R.id.textView).setText(item.getName());

    }
}
