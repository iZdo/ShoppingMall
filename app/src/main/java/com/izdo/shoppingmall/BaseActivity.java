package com.izdo.shoppingmall;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.izdo.shoppingmall.bean.User;

/**
 * Created by iZdo on 2017/8/25.
 */

public class BaseActivity extends AppCompatActivity {

    public void startActivity(Intent intent, boolean isNeedLogin) {


        if (isNeedLogin) {

            User user = MyApplication.getInstance().getUser();
            if (user != null) {
                super.startActivity(intent);
            } else {

                MyApplication.getInstance().putIntent(intent);
                Intent loginIntent = new Intent(this
                        , LoginActivity.class);
                super.startActivity(intent);

            }

        } else {
            super.startActivity(intent);
        }

    }
}
