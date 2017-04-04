package com.example.com.zhuangbi.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.com.zhuangbi.R;


/**
 * 基类
 */
public class BaseActivity extends AppCompatActivity {

    public static final String TAG="chenzb";

    public Context context=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        context=this;
    }
}
