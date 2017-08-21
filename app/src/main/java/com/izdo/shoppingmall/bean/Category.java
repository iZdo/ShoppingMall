package com.izdo.shoppingmall.bean;

/**
 * Created by iZdo on 2017/8/10.
 */

public class Category extends BaseBean {

    private String name;


    public Category() {

    }

    public Category(String name) {
        this.name = name;
    }

    public Category(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
