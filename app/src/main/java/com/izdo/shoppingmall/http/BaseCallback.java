package com.izdo.shoppingmall.http;

import com.google.gson.internal.$Gson$Types;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by iZdo on 2017/8/12.
 */

public abstract class BaseCallback<T> {

    public Type mType;

    static Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
    }


    public BaseCallback() {
        mType = getSuperclassTypeParameter(getClass());
    }


    public abstract void onRequestBefore(Request request);

    public abstract void onFailure(Request request, IOException e);

    public abstract void onResponse(Response response);

    public abstract void onSuccess(Response response, T t);

    public abstract void onError(Response response, int code, Exception e);
}
