package com.izdo.shoppingmall.bean;

import java.io.Serializable;

/**
 * Created by iZdo on 2017/8/16.
 */

public class ShoppingCart extends Wares implements Serializable {

    private int count;
    private boolean isChecked = true;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean checked) {
        isChecked = checked;
    }
}
