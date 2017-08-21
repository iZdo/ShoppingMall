package com.izdo.shoppingmall.bean;

import java.io.Serializable;

/**
 * Created by iZdo on 2017/8/10.
 */

public class BaseBean implements Serializable {

    protected long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
