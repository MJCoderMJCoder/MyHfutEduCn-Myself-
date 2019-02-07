package com.lzf.myhfuteducn;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.lzf.myhfuteducn.util.SharedPreferencesUtil;

import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbarTitle=   toolbar.findViewById(R.id.toolbarTitle);
        Log.v("dsfa", toolbarTitle + "");
        toolbarTitle.setText("课程表");
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView user_name = headerView.findViewById(R.id.user_name);
        user_name.setText("姓名：" + SharedPreferencesUtil.get(this, "user_name", ""));
        TextView user_code = headerView.findViewById(R.id.user_code);
        user_code.setText("学号：" + SharedPreferencesUtil.get(this, "user_code", ""));
        TextView account_email = headerView.findViewById(R.id.account_email);
        account_email.setText("邮箱：" + SharedPreferencesUtil.get(this, "account_email", ""));
        TextView mobile_phone = headerView.findViewById(R.id.mobile_phone);
        mobile_phone.setText("手机：" + SharedPreferencesUtil.get(this, "mobile_phone", ""));
        TextView depart_name_adminclass_name = headerView.findViewById(R.id.depart_name_adminclass_name);
        depart_name_adminclass_name.setText(SharedPreferencesUtil.get(this, "depart_name", "") + "-" + SharedPreferencesUtil.get(this, "adminclass_name", ""));
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_course) {
            toolbarTitle.setText("课程表");
        } else if (id == R.id.nav_sign) {
            toolbarTitle.setText("课堂签到");
        } else if (id == R.id.nav_community) {
            toolbarTitle.setText("社区");
        } else if (id == R.id.nav_achievement) {
            toolbarTitle.setText("查成绩");
        } else if (id == R.id.nav_infoset) {
            toolbarTitle.setText("信息设置");
        } else if (id == R.id.nav_skin) {
            toolbarTitle.setText("皮肤切换");
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
