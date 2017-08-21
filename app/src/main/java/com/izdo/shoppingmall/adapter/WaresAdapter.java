package com.izdo.shoppingmall.adapter;

import android.content.Context;
import android.net.Uri;

import com.facebook.drawee.view.SimpleDraweeView;
import com.izdo.shoppingmall.R;
import com.izdo.shoppingmall.bean.Wares;

import java.util.List;

/**
 * Created by iZdo on 2017/8/15.
 */

public class WaresAdapter extends SimpleAdapter<Wares> {

    public WaresAdapter(Context context, List<Wares> datas) {
        super(context, R.layout.template_grid_wares,datas);
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, Wares item) {

        viewHolder.getTextView(R.id.text_title).setText(item.getName());
        viewHolder.getTextView(R.id.text_price).setText("¥" + item.getPrice());
        SimpleDraweeView draweeView = (SimpleDraweeView) viewHolder.getView(R.id.drawee_view);
        draweeView.setImageURI(Uri.parse(item.getImgUrl()));
    }
}
