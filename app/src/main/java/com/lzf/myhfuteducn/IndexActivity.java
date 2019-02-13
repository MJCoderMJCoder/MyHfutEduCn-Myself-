package com.lzf.myhfuteducn;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.lzf.myhfuteducn.util.SharedPreferencesUtil;

public class IndexActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        //设置当前窗体为全屏显示
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (SharedPreferencesUtil.contains(this, "userKey")) {
            startActivity(new Intent(IndexActivity.this, MainActivity.class));
        } else {
            startActivity(new Intent(IndexActivity.this, LoginActivity.class));
        }
        finish();
    }
}
