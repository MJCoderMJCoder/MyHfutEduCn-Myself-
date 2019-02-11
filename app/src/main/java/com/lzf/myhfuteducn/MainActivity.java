package com.lzf.myhfuteducn;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lzf.myhfuteducn.fragment.CourseFragment;
import com.lzf.myhfuteducn.fragment.SignFragment;
import com.lzf.myhfuteducn.util.OkHttpUtil;
import com.lzf.myhfuteducn.util.SharedPreferencesUtil;
import com.lzf.myhfuteducn.util.UrlUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Menu menu;
    private TextView toolbarTitle;
    private FragmentManager fragmentManager;
    private CourseFragment courseFragment;

    public static JSONObject semesterWeekList; //所有学期以及按照学期分的周相关信息
    public static int semesterorder; //学期列表序号
    public static String semestercode; //学期代码
    public static String semestername; //学期名称
    public static String weekIndx; //第几周

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbarTitle = toolbar.findViewById(R.id.toolbarTitle);
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

        fragmentManager = getFragmentManager();
        menu = navigationView.getMenu();

        if (SharedPreferencesUtil.contains(this, "projectId0")) {
            getSemesterWeekList();
        } else {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("userKey", SharedPreferencesUtil.get(MainActivity.this, "userKey", "") + "");
                    map.put("identity", 0 + "");
                    final String response = OkHttpUtil.submit(UrlUtil.GET_PROJECT_INFO, map);
                /*
                 {
                 "code": 200,
                 "msg": "查询成功！",
                 "salt": null,
                 "token": null,
                 "obj": {
                 "business_data": [
                 {
                 "id": "2",
                 "name": "本科"
                 }
                 ],
                 "err_code": "00000",
                 "err_msg": ""
                 },
                 "error": null
                 }
                 */
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject responseJson = new JSONObject(response);
                                if (responseJson.getInt("code") == 200) {
                                    Toast.makeText(MainActivity.this, responseJson.getString("msg"), Toast.LENGTH_SHORT).show();
                                    JSONObject objJson = responseJson.getJSONObject("obj");
                                    if (objJson != null) {
                                        JSONArray businessDataJson = objJson.getJSONArray("business_data");
                                        if (businessDataJson != null && businessDataJson.length() > 0) {
                                            for (int i = 0; i < businessDataJson.length(); i++) {
                                                SharedPreferencesUtil.put(MainActivity.this, "projectId" + i, businessDataJson.getJSONObject(i).getString("id"));
                                                SharedPreferencesUtil.put(MainActivity.this, "projectName" + i, businessDataJson.getJSONObject(i).getString("name"));
                                            }
                                            getSemesterWeekList();
                                        } else {
                                            Toast.makeText(MainActivity.this, objJson.getString("err_msg"), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                } else {
                                    Toast.makeText(MainActivity.this, responseJson.getString("error"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }.start();
        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (semesterWeekList != null) {
            menu.clear();
            addMenu(menu);
        } else {
            getMenuInflater().inflate(R.menu.main, menu);
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (semesterWeekList != null && menu.size() < 2) {
            menu.clear();
            addMenu(menu);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        //        if (id == R.id.action_settings) {
        //            return true;
        //        }
        semesterorder = item.getOrder();
        semestercode = item.getItemId() + "";
        semestername = item.getTitle() + "";
        try {
            if (courseFragment.isVisible()) {
                JSONArray semesters = semesterWeekList.getJSONArray("semesters");
                weekIndx = semesters.getJSONObject(semesterorder).getString("week_start_at");
                menu.performIdentifierAction(R.id.nav_course, 0);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        FragmentTransaction fTransaction = fragmentManager.beginTransaction();
        hideAllFragment(fTransaction);
        int id = item.getItemId();
        if (id == R.id.nav_course) {
            toolbarTitle.setText("课程表");
            courseFragment = new CourseFragment();
            fTransaction.replace(R.id.centerContent, courseFragment);
        } else if (id == R.id.nav_sign) {
            toolbarTitle.setText("课堂签到");
            fTransaction.replace(R.id.centerContent, new SignFragment());
        } else if (id == R.id.nav_community) {
            toolbarTitle.setText("社区");
            fTransaction.replace(R.id.centerContent, new SignFragment());
        } else if (id == R.id.nav_achievement) {
            toolbarTitle.setText("查成绩");
            fTransaction.replace(R.id.centerContent, new SignFragment());
        } else if (id == R.id.nav_infoset) {
            toolbarTitle.setText("信息设置");
            fTransaction.replace(R.id.centerContent, new SignFragment());
        } else if (id == R.id.nav_skin) {
            toolbarTitle.setText("切换账号");
            fTransaction.replace(R.id.centerContent, new SignFragment());
        }
        fTransaction.commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * 隐藏所有Fragment
     *
     * @param fragmentTransaction
     */
    private void hideAllFragment(FragmentTransaction fragmentTransaction) {
        if (courseFragment != null)
            fragmentTransaction.hide(courseFragment);
        //        if (fNotification != null)
        //            fragmentTransaction.hide(fNotification);
        //        if (fMyself != null)
        //            fragmentTransaction.hide(fMyself);
        // if (fRepairs != null)
        // fragmentTransaction.hide(fRepairs);
    }

    /**
     * 查询所有学期以及按照学期分的周相关信息
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getSemesterWeekList() {
        new Thread() {
            @Override
            public void run() {
                super.run();

                Map<String, String> map = new HashMap<String, String>();
                map.put("userKey", SharedPreferencesUtil.get(MainActivity.this, "userKey", "") + "");
                map.put("projectId", SharedPreferencesUtil.get(MainActivity.this, "projectId0", "") + "");
                map.put("identity", 0 + "");
                final String response = OkHttpUtil.submit(UrlUtil.GET_SEMESTER_WEEK_LIST, map);
                /*
                 {
                 "code": 200,
                 "msg": "查询成功！",
                 "salt": null,
                 "token": null,
                 "obj": {
                 "business_data": {
                 "cur_semester_code": "034",
                 "cur_week_index": 1,
                 "semesters": [
                 {
                 "code": "034",
                 "name": "2018-2019学年第二学期",
                 "season": "春",
                 "week_start_at": 1,
                 "weeks": [
                 {
                 "begin_on": "2019-02-25",
                 "end_on": "2019-03-03",
                 "index": 1
                 },
                 {
                 "begin_on": "2019-03-04",
                 "end_on": "2019-03-10",
                 "index": 2
                 },
                 {
                 "begin_on": "2019-03-11",
                 "end_on": "2019-03-17",
                 "index": 3
                 },
                 {
                 "begin_on": "2019-03-18",
                 "end_on": "2019-03-24",
                 "index": 4
                 },
                 {
                 "begin_on": "2019-03-25",
                 "end_on": "2019-03-31",
                 "index": 5
                 },
                 {
                 "begin_on": "2019-04-01",
                 "end_on": "2019-04-07",
                 "index": 6
                 },
                 {
                 "begin_on": "2019-04-08",
                 "end_on": "2019-04-14",
                 "index": 7
                 },
                 {
                 "begin_on": "2019-04-15",
                 "end_on": "2019-04-21",
                 "index": 8
                 },
                 {
                 "begin_on": "2019-04-22",
                 "end_on": "2019-04-28",
                 "index": 9
                 },
                 {
                 "begin_on": "2019-04-29",
                 "end_on": "2019-05-05",
                 "index": 10
                 },
                 {
                 "begin_on": "2019-05-06",
                 "end_on": "2019-05-12",
                 "index": 11
                 },
                 {
                 "begin_on": "2019-05-13",
                 "end_on": "2019-05-19",
                 "index": 12
                 },
                 {
                 "begin_on": "2019-05-20",
                 "end_on": "2019-05-26",
                 "index": 13
                 },
                 {
                 "begin_on": "2019-05-27",
                 "end_on": "2019-06-02",
                 "index": 14
                 },
                 {
                 "begin_on": "2019-06-03",
                 "end_on": "2019-06-09",
                 "index": 15
                 },
                 {
                 "begin_on": "2019-06-10",
                 "end_on": "2019-06-16",
                 "index": 16
                 },
                 {
                 "begin_on": "2019-06-17",
                 "end_on": "2019-06-23",
                 "index": 17
                 },
                 {
                 "begin_on": "2019-06-24",
                 "end_on": "2019-06-30",
                 "index": 18
                 },
                 {
                 "begin_on": "2019-07-01",
                 "end_on": "2019-07-07",
                 "index": 19
                 },
                 {
                 "begin_on": "2019-07-08",
                 "end_on": "2019-07-14",
                 "index": 20
                 }
                 ],
                 "year": "2018-2019"
                 },
                 {
                 "code": "033",
                 "name": "2018-2019学年第一学期",
                 "season": "秋",
                 "week_start_at": 1,
                 "weeks": [
                 {
                 "begin_on": "2018-09-03",
                 "end_on": "2018-09-09",
                 "index": 1
                 },
                 {
                 "begin_on": "2018-09-10",
                 "end_on": "2018-09-16",
                 "index": 2
                 },
                 {
                 "begin_on": "2018-09-17",
                 "end_on": "2018-09-23",
                 "index": 3
                 },
                 {
                 "begin_on": "2018-09-24",
                 "end_on": "2018-09-30",
                 "index": 4
                 },
                 {
                 "begin_on": "2018-10-01",
                 "end_on": "2018-10-07",
                 "index": 5
                 },
                 {
                 "begin_on": "2018-10-08",
                 "end_on": "2018-10-14",
                 "index": 6
                 },
                 {
                 "begin_on": "2018-10-15",
                 "end_on": "2018-10-21",
                 "index": 7
                 },
                 {
                 "begin_on": "2018-10-22",
                 "end_on": "2018-10-28",
                 "index": 8
                 },
                 {
                 "begin_on": "2018-10-29",
                 "end_on": "2018-11-04",
                 "index": 9
                 },
                 {
                 "begin_on": "2018-11-05",
                 "end_on": "2018-11-11",
                 "index": 10
                 },
                 {
                 "begin_on": "2018-11-12",
                 "end_on": "2018-11-18",
                 "index": 11
                 },
                 {
                 "begin_on": "2018-11-19",
                 "end_on": "2018-11-25",
                 "index": 12
                 },
                 {
                 "begin_on": "2018-11-26",
                 "end_on": "2018-12-02",
                 "index": 13
                 },
                 {
                 "begin_on": "2018-12-03",
                 "end_on": "2018-12-09",
                 "index": 14
                 },
                 {
                 "begin_on": "2018-12-10",
                 "end_on": "2018-12-16",
                 "index": 15
                 },
                 {
                 "begin_on": "2018-12-17",
                 "end_on": "2018-12-23",
                 "index": 16
                 },
                 {
                 "begin_on": "2018-12-24",
                 "end_on": "2018-12-30",
                 "index": 17
                 },
                 {
                 "begin_on": "2018-12-31",
                 "end_on": "2019-01-06",
                 "index": 18
                 },
                 {
                 "begin_on": "2019-01-07",
                 "end_on": "2019-01-13",
                 "index": 19
                 },
                 {
                 "begin_on": "2019-01-14",
                 "end_on": "2019-01-20",
                 "index": 20
                 }
                 ],
                 "year": "2018-2019"
                 },
                 {
                 "code": "032",
                 "name": "2017-2018学年第二学期",
                 "season": "春",
                 "week_start_at": 1,
                 "weeks": [
                 {
                 "begin_on": "2018-02-26",
                 "end_on": "2018-03-04",
                 "index": 1
                 },
                 {
                 "begin_on": "2018-03-05",
                 "end_on": "2018-03-11",
                 "index": 2
                 },
                 {
                 "begin_on": "2018-03-12",
                 "end_on": "2018-03-18",
                 "index": 3
                 },
                 {
                 "begin_on": "2018-03-19",
                 "end_on": "2018-03-25",
                 "index": 4
                 },
                 {
                 "begin_on": "2018-03-26",
                 "end_on": "2018-04-01",
                 "index": 5
                 },
                 {
                 "begin_on": "2018-04-02",
                 "end_on": "2018-04-08",
                 "index": 6
                 },
                 {
                 "begin_on": "2018-04-09",
                 "end_on": "2018-04-15",
                 "index": 7
                 },
                 {
                 "begin_on": "2018-04-16",
                 "end_on": "2018-04-22",
                 "index": 8
                 },
                 {
                 "begin_on": "2018-04-23",
                 "end_on": "2018-04-29",
                 "index": 9
                 },
                 {
                 "begin_on": "2018-04-30",
                 "end_on": "2018-05-06",
                 "index": 10
                 },
                 {
                 "begin_on": "2018-05-07",
                 "end_on": "2018-05-13",
                 "index": 11
                 },
                 {
                 "begin_on": "2018-05-14",
                 "end_on": "2018-05-20",
                 "index": 12
                 },
                 {
                 "begin_on": "2018-05-21",
                 "end_on": "2018-05-27",
                 "index": 13
                 },
                 {
                 "begin_on": "2018-05-28",
                 "end_on": "2018-06-03",
                 "index": 14
                 },
                 {
                 "begin_on": "2018-06-04",
                 "end_on": "2018-06-10",
                 "index": 15
                 },
                 {
                 "begin_on": "2018-06-11",
                 "end_on": "2018-06-17",
                 "index": 16
                 },
                 {
                 "begin_on": "2018-06-18",
                 "end_on": "2018-06-24",
                 "index": 17
                 },
                 {
                 "begin_on": "2018-06-25",
                 "end_on": "2018-07-01",
                 "index": 18
                 },
                 {
                 "begin_on": "2018-07-02",
                 "end_on": "2018-07-08",
                 "index": 19
                 },
                 {
                 "begin_on": "2018-07-09",
                 "end_on": "2018-07-15",
                 "index": 20
                 }
                 ],
                 "year": "2017-2018"
                 },
                 {
                 "code": "031",
                 "name": "2017-2018学年第一学期",
                 "season": "秋",
                 "week_start_at": 1,
                 "weeks": [
                 {
                 "begin_on": "2017-09-04",
                 "end_on": "2017-09-10",
                 "index": 1
                 },
                 {
                 "begin_on": "2017-09-11",
                 "end_on": "2017-09-17",
                 "index": 2
                 },
                 {
                 "begin_on": "2017-09-18",
                 "end_on": "2017-09-24",
                 "index": 3
                 },
                 {
                 "begin_on": "2017-09-25",
                 "end_on": "2017-10-01",
                 "index": 4
                 },
                 {
                 "begin_on": "2017-10-02",
                 "end_on": "2017-10-08",
                 "index": 5
                 },
                 {
                 "begin_on": "2017-10-09",
                 "end_on": "2017-10-15",
                 "index": 6
                 },
                 {
                 "begin_on": "2017-10-16",
                 "end_on": "2017-10-22",
                 "index": 7
                 },
                 {
                 "begin_on": "2017-10-23",
                 "end_on": "2017-10-29",
                 "index": 8
                 },
                 {
                 "begin_on": "2017-10-30",
                 "end_on": "2017-11-05",
                 "index": 9
                 },
                 {
                 "begin_on": "2017-11-06",
                 "end_on": "2017-11-12",
                 "index": 10
                 },
                 {
                 "begin_on": "2017-11-13",
                 "end_on": "2017-11-19",
                 "index": 11
                 },
                 {
                 "begin_on": "2017-11-20",
                 "end_on": "2017-11-26",
                 "index": 12
                 },
                 {
                 "begin_on": "2017-11-27",
                 "end_on": "2017-12-03",
                 "index": 13
                 },
                 {
                 "begin_on": "2017-12-04",
                 "end_on": "2017-12-10",
                 "index": 14
                 },
                 {
                 "begin_on": "2017-12-11",
                 "end_on": "2017-12-17",
                 "index": 15
                 },
                 {
                 "begin_on": "2017-12-18",
                 "end_on": "2017-12-24",
                 "index": 16
                 },
                 {
                 "begin_on": "2017-12-25",
                 "end_on": "2017-12-31",
                 "index": 17
                 },
                 {
                 "begin_on": "2018-01-01",
                 "end_on": "2018-01-07",
                 "index": 18
                 },
                 {
                 "begin_on": "2018-01-08",
                 "end_on": "2018-01-14",
                 "index": 19
                 },
                 {
                 "begin_on": "2018-01-15",
                 "end_on": "2018-01-21",
                 "index": 20
                 }
                 ],
                 "year": "2017-2018"
                 },
                 {
                 "code": "030",
                 "name": "2016-2017学年第二学期",
                 "season": "春",
                 "week_start_at": 1,
                 "weeks": [
                 {
                 "begin_on": "2017-02-20",
                 "end_on": "2017-02-26",
                 "index": 1
                 },
                 {
                 "begin_on": "2017-02-27",
                 "end_on": "2017-03-05",
                 "index": 2
                 },
                 {
                 "begin_on": "2017-03-06",
                 "end_on": "2017-03-12",
                 "index": 3
                 },
                 {
                 "begin_on": "2017-03-13",
                 "end_on": "2017-03-19",
                 "index": 4
                 },
                 {
                 "begin_on": "2017-03-20",
                 "end_on": "2017-03-26",
                 "index": 5
                 },
                 {
                 "begin_on": "2017-03-27",
                 "end_on": "2017-04-02",
                 "index": 6
                 },
                 {
                 "begin_on": "2017-04-03",
                 "end_on": "2017-04-09",
                 "index": 7
                 },
                 {
                 "begin_on": "2017-04-10",
                 "end_on": "2017-04-16",
                 "index": 8
                 },
                 {
                 "begin_on": "2017-04-17",
                 "end_on": "2017-04-23",
                 "index": 9
                 },
                 {
                 "begin_on": "2017-04-24",
                 "end_on": "2017-04-30",
                 "index": 10
                 },
                 {
                 "begin_on": "2017-05-01",
                 "end_on": "2017-05-07",
                 "index": 11
                 },
                 {
                 "begin_on": "2017-05-08",
                 "end_on": "2017-05-14",
                 "index": 12
                 },
                 {
                 "begin_on": "2017-05-15",
                 "end_on": "2017-05-21",
                 "index": 13
                 },
                 {
                 "begin_on": "2017-05-22",
                 "end_on": "2017-05-28",
                 "index": 14
                 },
                 {
                 "begin_on": "2017-05-29",
                 "end_on": "2017-06-04",
                 "index": 15
                 },
                 {
                 "begin_on": "2017-06-05",
                 "end_on": "2017-06-11",
                 "index": 16
                 },
                 {
                 "begin_on": "2017-06-12",
                 "end_on": "2017-06-18",
                 "index": 17
                 },
                 {
                 "begin_on": "2017-06-19",
                 "end_on": "2017-06-25",
                 "index": 18
                 },
                 {
                 "begin_on": "2017-06-26",
                 "end_on": "2017-07-02",
                 "index": 19
                 },
                 {
                 "begin_on": "2017-07-03",
                 "end_on": "2017-07-09",
                 "index": 20
                 }
                 ],
                 "year": "2016-2017"
                 },
                 {
                 "code": "029",
                 "name": "2016-2017学年第一学期",
                 "season": "秋",
                 "week_start_at": 1,
                 "weeks": [
                 {
                 "begin_on": "2016-08-29",
                 "end_on": "2016-09-04",
                 "index": 1
                 },
                 {
                 "begin_on": "2016-09-05",
                 "end_on": "2016-09-11",
                 "index": 2
                 },
                 {
                 "begin_on": "2016-09-12",
                 "end_on": "2016-09-18",
                 "index": 3
                 },
                 {
                 "begin_on": "2016-09-19",
                 "end_on": "2016-09-25",
                 "index": 4
                 },
                 {
                 "begin_on": "2016-09-26",
                 "end_on": "2016-10-02",
                 "index": 5
                 },
                 {
                 "begin_on": "2016-10-03",
                 "end_on": "2016-10-09",
                 "index": 6
                 },
                 {
                 "begin_on": "2016-10-10",
                 "end_on": "2016-10-16",
                 "index": 7
                 },
                 {
                 "begin_on": "2016-10-17",
                 "end_on": "2016-10-23",
                 "index": 8
                 },
                 {
                 "begin_on": "2016-10-24",
                 "end_on": "2016-10-30",
                 "index": 9
                 },
                 {
                 "begin_on": "2016-10-31",
                 "end_on": "2016-11-06",
                 "index": 10
                 },
                 {
                 "begin_on": "2016-11-07",
                 "end_on": "2016-11-13",
                 "index": 11
                 },
                 {
                 "begin_on": "2016-11-14",
                 "end_on": "2016-11-20",
                 "index": 12
                 },
                 {
                 "begin_on": "2016-11-21",
                 "end_on": "2016-11-27",
                 "index": 13
                 },
                 {
                 "begin_on": "2016-11-28",
                 "end_on": "2016-12-04",
                 "index": 14
                 },
                 {
                 "begin_on": "2016-12-05",
                 "end_on": "2016-12-11",
                 "index": 15
                 },
                 {
                 "begin_on": "2016-12-12",
                 "end_on": "2016-12-18",
                 "index": 16
                 },
                 {
                 "begin_on": "2016-12-19",
                 "end_on": "2016-12-25",
                 "index": 17
                 },
                 {
                 "begin_on": "2016-12-26",
                 "end_on": "2017-01-01",
                 "index": 18
                 },
                 {
                 "begin_on": "2017-01-02",
                 "end_on": "2017-01-08",
                 "index": 19
                 },
                 {
                 "begin_on": "2017-01-09",
                 "end_on": "2017-01-15",
                 "index": 20
                 }
                 ],
                 "year": "2016-2017"
                 },
                 {
                 "code": "028",
                 "name": "2015-2016学年第二学期",
                 "season": "春",
                 "week_start_at": 1,
                 "weeks": [
                 {
                 "begin_on": "2016-02-22",
                 "end_on": "2016-02-28",
                 "index": 1
                 },
                 {
                 "begin_on": "2016-02-29",
                 "end_on": "2016-03-06",
                 "index": 2
                 },
                 {
                 "begin_on": "2016-03-07",
                 "end_on": "2016-03-13",
                 "index": 3
                 },
                 {
                 "begin_on": "2016-03-14",
                 "end_on": "2016-03-20",
                 "index": 4
                 },
                 {
                 "begin_on": "2016-03-21",
                 "end_on": "2016-03-27",
                 "index": 5
                 },
                 {
                 "begin_on": "2016-03-28",
                 "end_on": "2016-04-03",
                 "index": 6
                 },
                 {
                 "begin_on": "2016-04-04",
                 "end_on": "2016-04-10",
                 "index": 7
                 },
                 {
                 "begin_on": "2016-04-11",
                 "end_on": "2016-04-17",
                 "index": 8
                 },
                 {
                 "begin_on": "2016-04-18",
                 "end_on": "2016-04-24",
                 "index": 9
                 },
                 {
                 "begin_on": "2016-04-25",
                 "end_on": "2016-05-01",
                 "index": 10
                 },
                 {
                 "begin_on": "2016-05-02",
                 "end_on": "2016-05-08",
                 "index": 11
                 },
                 {
                 "begin_on": "2016-05-09",
                 "end_on": "2016-05-15",
                 "index": 12
                 },
                 {
                 "begin_on": "2016-05-16",
                 "end_on": "2016-05-22",
                 "index": 13
                 },
                 {
                 "begin_on": "2016-05-23",
                 "end_on": "2016-05-29",
                 "index": 14
                 },
                 {
                 "begin_on": "2016-05-30",
                 "end_on": "2016-06-05",
                 "index": 15
                 },
                 {
                 "begin_on": "2016-06-06",
                 "end_on": "2016-06-12",
                 "index": 16
                 },
                 {
                 "begin_on": "2016-06-13",
                 "end_on": "2016-06-19",
                 "index": 17
                 },
                 {
                 "begin_on": "2016-06-20",
                 "end_on": "2016-06-26",
                 "index": 18
                 },
                 {
                 "begin_on": "2016-06-27",
                 "end_on": "2016-07-03",
                 "index": 19
                 },
                 {
                 "begin_on": "2016-07-04",
                 "end_on": "2016-07-10",
                 "index": 20
                 }
                 ],
                 "year": "2015-2016"
                 },
                 {
                 "code": "027",
                 "name": "2015-2016学年第一学期",
                 "season": "秋",
                 "week_start_at": 1,
                 "weeks": [
                 {
                 "begin_on": "2015-09-14",
                 "end_on": "2015-09-20",
                 "index": 1
                 },
                 {
                 "begin_on": "2015-09-21",
                 "end_on": "2015-09-27",
                 "index": 2
                 },
                 {
                 "begin_on": "2015-09-28",
                 "end_on": "2015-10-04",
                 "index": 3
                 },
                 {
                 "begin_on": "2015-10-05",
                 "end_on": "2015-10-11",
                 "index": 4
                 },
                 {
                 "begin_on": "2015-10-12",
                 "end_on": "2015-10-18",
                 "index": 5
                 },
                 {
                 "begin_on": "2015-10-19",
                 "end_on": "2015-10-25",
                 "index": 6
                 },
                 {
                 "begin_on": "2015-10-26",
                 "end_on": "2015-11-01",
                 "index": 7
                 },
                 {
                 "begin_on": "2015-11-02",
                 "end_on": "2015-11-08",
                 "index": 8
                 },
                 {
                 "begin_on": "2015-11-09",
                 "end_on": "2015-11-15",
                 "index": 9
                 },
                 {
                 "begin_on": "2015-11-16",
                 "end_on": "2015-11-22",
                 "index": 10
                 },
                 {
                 "begin_on": "2015-11-23",
                 "end_on": "2015-11-29",
                 "index": 11
                 },
                 {
                 "begin_on": "2015-11-30",
                 "end_on": "2015-12-06",
                 "index": 12
                 },
                 {
                 "begin_on": "2015-12-07",
                 "end_on": "2015-12-13",
                 "index": 13
                 },
                 {
                 "begin_on": "2015-12-14",
                 "end_on": "2015-12-20",
                 "index": 14
                 },
                 {
                 "begin_on": "2015-12-21",
                 "end_on": "2015-12-27",
                 "index": 15
                 },
                 {
                 "begin_on": "2015-12-28",
                 "end_on": "2016-01-03",
                 "index": 16
                 },
                 {
                 "begin_on": "2016-01-04",
                 "end_on": "2016-01-10",
                 "index": 17
                 },
                 {
                 "begin_on": "2016-01-11",
                 "end_on": "2016-01-17",
                 "index": 18
                 }
                 ],
                 "year": "2015-2016"
                 },
                 {
                 "code": "026",
                 "name": "2014-2015学年第二学期",
                 "season": "春",
                 "week_start_at": 1,
                 "weeks": [
                 {
                 "begin_on": "2015-03-02",
                 "end_on": "2015-03-08",
                 "index": 1
                 },
                 {
                 "begin_on": "2015-03-09",
                 "end_on": "2015-03-15",
                 "index": 2
                 },
                 {
                 "begin_on": "2015-03-16",
                 "end_on": "2015-03-22",
                 "index": 3
                 },
                 {
                 "begin_on": "2015-03-23",
                 "end_on": "2015-03-29",
                 "index": 4
                 },
                 {
                 "begin_on": "2015-03-30",
                 "end_on": "2015-04-05",
                 "index": 5
                 },
                 {
                 "begin_on": "2015-04-06",
                 "end_on": "2015-04-12",
                 "index": 6
                 },
                 {
                 "begin_on": "2015-04-13",
                 "end_on": "2015-04-19",
                 "index": 7
                 },
                 {
                 "begin_on": "2015-04-20",
                 "end_on": "2015-04-26",
                 "index": 8
                 },
                 {
                 "begin_on": "2015-04-27",
                 "end_on": "2015-05-03",
                 "index": 9
                 },
                 {
                 "begin_on": "2015-05-04",
                 "end_on": "2015-05-10",
                 "index": 10
                 },
                 {
                 "begin_on": "2015-05-11",
                 "end_on": "2015-05-17",
                 "index": 11
                 },
                 {
                 "begin_on": "2015-05-18",
                 "end_on": "2015-05-24",
                 "index": 12
                 },
                 {
                 "begin_on": "2015-05-25",
                 "end_on": "2015-05-31",
                 "index": 13
                 },
                 {
                 "begin_on": "2015-06-01",
                 "end_on": "2015-06-07",
                 "index": 14
                 },
                 {
                 "begin_on": "2015-06-08",
                 "end_on": "2015-06-14",
                 "index": 15
                 },
                 {
                 "begin_on": "2015-06-15",
                 "end_on": "2015-06-21",
                 "index": 16
                 },
                 {
                 "begin_on": "2015-06-22",
                 "end_on": "2015-06-28",
                 "index": 17
                 },
                 {
                 "begin_on": "2015-06-29",
                 "end_on": "2015-07-05",
                 "index": 18
                 },
                 {
                 "begin_on": "2015-07-06",
                 "end_on": "2015-07-12",
                 "index": 19
                 },
                 {
                 "begin_on": "2015-07-13",
                 "end_on": "2015-07-19",
                 "index": 20
                 },
                 {
                 "begin_on": "2015-07-20",
                 "end_on": "2015-07-26",
                 "index": 21
                 },
                 {
                 "begin_on": "2015-07-27",
                 "end_on": "2015-08-02",
                 "index": 22
                 }
                 ],
                 "year": "2014-2015"
                 },
                 {
                 "code": "025",
                 "name": "2014-2015学年第一学期",
                 "season": "秋",
                 "week_start_at": 1,
                 "weeks": [
                 {
                 "begin_on": "2014-09-15",
                 "end_on": "2014-09-21",
                 "index": 1
                 },
                 {
                 "begin_on": "2014-09-22",
                 "end_on": "2014-09-28",
                 "index": 2
                 },
                 {
                 "begin_on": "2014-09-29",
                 "end_on": "2014-10-05",
                 "index": 3
                 },
                 {
                 "begin_on": "2014-10-06",
                 "end_on": "2014-10-12",
                 "index": 4
                 },
                 {
                 "begin_on": "2014-10-13",
                 "end_on": "2014-10-19",
                 "index": 5
                 },
                 {
                 "begin_on": "2014-10-20",
                 "end_on": "2014-10-26",
                 "index": 6
                 },
                 {
                 "begin_on": "2014-10-27",
                 "end_on": "2014-11-02",
                 "index": 7
                 },
                 {
                 "begin_on": "2014-11-03",
                 "end_on": "2014-11-09",
                 "index": 8
                 },
                 {
                 "begin_on": "2014-11-10",
                 "end_on": "2014-11-16",
                 "index": 9
                 },
                 {
                 "begin_on": "2014-11-17",
                 "end_on": "2014-11-23",
                 "index": 10
                 },
                 {
                 "begin_on": "2014-11-24",
                 "end_on": "2014-11-30",
                 "index": 11
                 },
                 {
                 "begin_on": "2014-12-01",
                 "end_on": "2014-12-07",
                 "index": 12
                 },
                 {
                 "begin_on": "2014-12-08",
                 "end_on": "2014-12-14",
                 "index": 13
                 },
                 {
                 "begin_on": "2014-12-15",
                 "end_on": "2014-12-21",
                 "index": 14
                 },
                 {
                 "begin_on": "2014-12-22",
                 "end_on": "2014-12-28",
                 "index": 15
                 },
                 {
                 "begin_on": "2014-12-29",
                 "end_on": "2015-01-04",
                 "index": 16
                 },
                 {
                 "begin_on": "2015-01-05",
                 "end_on": "2015-01-11",
                 "index": 17
                 },
                 {
                 "begin_on": "2015-01-12",
                 "end_on": "2015-01-18",
                 "index": 18
                 }
                 ],
                 "year": "2014-2015"
                 },
                 {
                 "code": "024",
                 "name": "2013-2014学年第二学期",
                 "season": "春",
                 "week_start_at": 1,
                 "weeks": [
                 {
                 "begin_on": "2014-02-17",
                 "end_on": "2014-02-23",
                 "index": 1
                 },
                 {
                 "begin_on": "2014-02-24",
                 "end_on": "2014-03-02",
                 "index": 2
                 },
                 {
                 "begin_on": "2014-03-03",
                 "end_on": "2014-03-09",
                 "index": 3
                 },
                 {
                 "begin_on": "2014-03-10",
                 "end_on": "2014-03-16",
                 "index": 4
                 },
                 {
                 "begin_on": "2014-03-17",
                 "end_on": "2014-03-23",
                 "index": 5
                 },
                 {
                 "begin_on": "2014-03-24",
                 "end_on": "2014-03-30",
                 "index": 6
                 },
                 {
                 "begin_on": "2014-03-31",
                 "end_on": "2014-04-06",
                 "index": 7
                 },
                 {
                 "begin_on": "2014-04-07",
                 "end_on": "2014-04-13",
                 "index": 8
                 },
                 {
                 "begin_on": "2014-04-14",
                 "end_on": "2014-04-20",
                 "index": 9
                 },
                 {
                 "begin_on": "2014-04-21",
                 "end_on": "2014-04-27",
                 "index": 10
                 },
                 {
                 "begin_on": "2014-04-28",
                 "end_on": "2014-05-04",
                 "index": 11
                 },
                 {
                 "begin_on": "2014-05-05",
                 "end_on": "2014-05-11",
                 "index": 12
                 },
                 {
                 "begin_on": "2014-05-12",
                 "end_on": "2014-05-18",
                 "index": 13
                 },
                 {
                 "begin_on": "2014-05-19",
                 "end_on": "2014-05-25",
                 "index": 14
                 },
                 {
                 "begin_on": "2014-05-26",
                 "end_on": "2014-06-01",
                 "index": 15
                 },
                 {
                 "begin_on": "2014-06-02",
                 "end_on": "2014-06-08",
                 "index": 16
                 },
                 {
                 "begin_on": "2014-06-09",
                 "end_on": "2014-06-15",
                 "index": 17
                 },
                 {
                 "begin_on": "2014-06-16",
                 "end_on": "2014-06-22",
                 "index": 18
                 },
                 {
                 "begin_on": "2014-06-23",
                 "end_on": "2014-06-29",
                 "index": 19
                 },
                 {
                 "begin_on": "2014-06-30",
                 "end_on": "2014-07-06",
                 "index": 20
                 },
                 {
                 "begin_on": "2014-07-07",
                 "end_on": "2014-07-13",
                 "index": 21
                 },
                 {
                 "begin_on": "2014-07-14",
                 "end_on": "2014-07-20",
                 "index": 22
                 }
                 ],
                 "year": "2013-2014"
                 },
                 {
                 "code": "023",
                 "name": "2013-2014学年第一学期",
                 "season": "秋",
                 "week_start_at": 1,
                 "weeks": [
                 {
                 "begin_on": "2013-09-09",
                 "end_on": "2013-09-15",
                 "index": 1
                 },
                 {
                 "begin_on": "2013-09-16",
                 "end_on": "2013-09-22",
                 "index": 2
                 },
                 {
                 "begin_on": "2013-09-23",
                 "end_on": "2013-09-29",
                 "index": 3
                 },
                 {
                 "begin_on": "2013-09-30",
                 "end_on": "2013-10-06",
                 "index": 4
                 },
                 {
                 "begin_on": "2013-10-07",
                 "end_on": "2013-10-13",
                 "index": 5
                 },
                 {
                 "begin_on": "2013-10-14",
                 "end_on": "2013-10-20",
                 "index": 6
                 },
                 {
                 "begin_on": "2013-10-21",
                 "end_on": "2013-10-27",
                 "index": 7
                 },
                 {
                 "begin_on": "2013-10-28",
                 "end_on": "2013-11-03",
                 "index": 8
                 },
                 {
                 "begin_on": "2013-11-04",
                 "end_on": "2013-11-10",
                 "index": 9
                 },
                 {
                 "begin_on": "2013-11-11",
                 "end_on": "2013-11-17",
                 "index": 10
                 },
                 {
                 "begin_on": "2013-11-18",
                 "end_on": "2013-11-24",
                 "index": 11
                 },
                 {
                 "begin_on": "2013-11-25",
                 "end_on": "2013-12-01",
                 "index": 12
                 },
                 {
                 "begin_on": "2013-12-02",
                 "end_on": "2013-12-08",
                 "index": 13
                 },
                 {
                 "begin_on": "2013-12-09",
                 "end_on": "2013-12-15",
                 "index": 14
                 },
                 {
                 "begin_on": "2013-12-16",
                 "end_on": "2013-12-22",
                 "index": 15
                 },
                 {
                 "begin_on": "2013-12-23",
                 "end_on": "2013-12-29",
                 "index": 16
                 },
                 {
                 "begin_on": "2013-12-30",
                 "end_on": "2014-01-05",
                 "index": 17
                 },
                 {
                 "begin_on": "2014-01-06",
                 "end_on": "2014-01-12",
                 "index": 18
                 }
                 ],
                 "year": "2013-2014"
                 },
                 {
                 "code": "022",
                 "name": "2012-2013学年第二学期",
                 "season": "春",
                 "week_start_at": 1,
                 "weeks": [
                 {
                 "begin_on": "2013-02-25",
                 "end_on": "2013-03-03",
                 "index": 1
                 },
                 {
                 "begin_on": "2013-03-04",
                 "end_on": "2013-03-10",
                 "index": 2
                 },
                 {
                 "begin_on": "2013-03-11",
                 "end_on": "2013-03-17",
                 "index": 3
                 },
                 {
                 "begin_on": "2013-03-18",
                 "end_on": "2013-03-24",
                 "index": 4
                 },
                 {
                 "begin_on": "2013-03-25",
                 "end_on": "2013-03-31",
                 "index": 5
                 },
                 {
                 "begin_on": "2013-04-01",
                 "end_on": "2013-04-07",
                 "index": 6
                 },
                 {
                 "begin_on": "2013-04-08",
                 "end_on": "2013-04-14",
                 "index": 7
                 },
                 {
                 "begin_on": "2013-04-15",
                 "end_on": "2013-04-21",
                 "index": 8
                 },
                 {
                 "begin_on": "2013-04-22",
                 "end_on": "2013-04-28",
                 "index": 9
                 },
                 {
                 "begin_on": "2013-04-29",
                 "end_on": "2013-05-05",
                 "index": 10
                 },
                 {
                 "begin_on": "2013-05-06",
                 "end_on": "2013-05-12",
                 "index": 11
                 },
                 {
                 "begin_on": "2013-05-13",
                 "end_on": "2013-05-19",
                 "index": 12
                 },
                 {
                 "begin_on": "2013-05-20",
                 "end_on": "2013-05-26",
                 "index": 13
                 },
                 {
                 "begin_on": "2013-05-27",
                 "end_on": "2013-06-02",
                 "index": 14
                 },
                 {
                 "begin_on": "2013-06-03",
                 "end_on": "2013-06-09",
                 "index": 15
                 },
                 {
                 "begin_on": "2013-06-10",
                 "end_on": "2013-06-16",
                 "index": 16
                 },
                 {
                 "begin_on": "2013-06-17",
                 "end_on": "2013-06-23",
                 "index": 17
                 },
                 {
                 "begin_on": "2013-06-24",
                 "end_on": "2013-06-30",
                 "index": 18
                 },
                 {
                 "begin_on": "2013-07-01",
                 "end_on": "2013-07-07",
                 "index": 19
                 },
                 {
                 "begin_on": "2013-07-08",
                 "end_on": "2013-07-14",
                 "index": 20
                 },
                 {
                 "begin_on": "2013-07-15",
                 "end_on": "2013-07-21",
                 "index": 21
                 },
                 {
                 "begin_on": "2013-07-22",
                 "end_on": "2013-07-28",
                 "index": 22
                 }
                 ],
                 "year": "2012-2013"
                 },
                 {
                 "code": "021",
                 "name": "2012-2013学年第一学期",
                 "season": "秋",
                 "week_start_at": 1,
                 "weeks": [
                 {
                 "begin_on": "2012-09-10",
                 "end_on": "2012-09-16",
                 "index": 1
                 },
                 {
                 "begin_on": "2012-09-17",
                 "end_on": "2012-09-23",
                 "index": 2
                 },
                 {
                 "begin_on": "2012-09-24",
                 "end_on": "2012-09-30",
                 "index": 3
                 },
                 {
                 "begin_on": "2012-10-01",
                 "end_on": "2012-10-07",
                 "index": 4
                 },
                 {
                 "begin_on": "2012-10-08",
                 "end_on": "2012-10-14",
                 "index": 5
                 },
                 {
                 "begin_on": "2012-10-15",
                 "end_on": "2012-10-21",
                 "index": 6
                 },
                 {
                 "begin_on": "2012-10-22",
                 "end_on": "2012-10-28",
                 "index": 7
                 },
                 {
                 "begin_on": "2012-10-29",
                 "end_on": "2012-11-04",
                 "index": 8
                 },
                 {
                 "begin_on": "2012-11-05",
                 "end_on": "2012-11-11",
                 "index": 9
                 },
                 {
                 "begin_on": "2012-11-12",
                 "end_on": "2012-11-18",
                 "index": 10
                 },
                 {
                 "begin_on": "2012-11-19",
                 "end_on": "2012-11-25",
                 "index": 11
                 },
                 {
                 "begin_on": "2012-11-26",
                 "end_on": "2012-12-02",
                 "index": 12
                 },
                 {
                 "begin_on": "2012-12-03",
                 "end_on": "2012-12-09",
                 "index": 13
                 },
                 {
                 "begin_on": "2012-12-10",
                 "end_on": "2012-12-16",
                 "index": 14
                 },
                 {
                 "begin_on": "2012-12-17",
                 "end_on": "2012-12-23",
                 "index": 15
                 },
                 {
                 "begin_on": "2012-12-24",
                 "end_on": "2012-12-30",
                 "index": 16
                 },
                 {
                 "begin_on": "2012-12-31",
                 "end_on": "2013-01-06",
                 "index": 17
                 },
                 {
                 "begin_on": "2013-01-07",
                 "end_on": "2013-01-13",
                 "index": 18
                 }
                 ],
                 "year": "2012-2013"
                 },
                 {
                 "code": "020",
                 "name": "2011-2012学年第二学期",
                 "season": "春",
                 "week_start_at": 1,
                 "weeks": [
                 {
                 "begin_on": "2012-02-13",
                 "end_on": "2012-02-19",
                 "index": 1
                 },
                 {
                 "begin_on": "2012-02-20",
                 "end_on": "2012-02-26",
                 "index": 2
                 },
                 {
                 "begin_on": "2012-02-27",
                 "end_on": "2012-03-04",
                 "index": 3
                 },
                 {
                 "begin_on": "2012-03-05",
                 "end_on": "2012-03-11",
                 "index": 4
                 },
                 {
                 "begin_on": "2012-03-12",
                 "end_on": "2012-03-18",
                 "index": 5
                 },
                 {
                 "begin_on": "2012-03-19",
                 "end_on": "2012-03-25",
                 "index": 6
                 },
                 {
                 "begin_on": "2012-03-26",
                 "end_on": "2012-04-01",
                 "index": 7
                 },
                 {
                 "begin_on": "2012-04-02",
                 "end_on": "2012-04-08",
                 "index": 8
                 },
                 {
                 "begin_on": "2012-04-09",
                 "end_on": "2012-04-15",
                 "index": 9
                 },
                 {
                 "begin_on": "2012-04-16",
                 "end_on": "2012-04-22",
                 "index": 10
                 },
                 {
                 "begin_on": "2012-04-23",
                 "end_on": "2012-04-29",
                 "index": 11
                 },
                 {
                 "begin_on": "2012-04-30",
                 "end_on": "2012-05-06",
                 "index": 12
                 },
                 {
                 "begin_on": "2012-05-07",
                 "end_on": "2012-05-13",
                 "index": 13
                 },
                 {
                 "begin_on": "2012-05-14",
                 "end_on": "2012-05-20",
                 "index": 14
                 },
                 {
                 "begin_on": "2012-05-21",
                 "end_on": "2012-05-27",
                 "index": 15
                 },
                 {
                 "begin_on": "2012-05-28",
                 "end_on": "2012-06-03",
                 "index": 16
                 },
                 {
                 "begin_on": "2012-06-04",
                 "end_on": "2012-06-10",
                 "index": 17
                 },
                 {
                 "begin_on": "2012-06-11",
                 "end_on": "2012-06-17",
                 "index": 18
                 },
                 {
                 "begin_on": "2012-06-18",
                 "end_on": "2012-06-24",
                 "index": 19
                 },
                 {
                 "begin_on": "2012-06-25",
                 "end_on": "2012-07-01",
                 "index": 20
                 },
                 {
                 "begin_on": "2012-07-02",
                 "end_on": "2012-07-08",
                 "index": 21
                 },
                 {
                 "begin_on": "2012-07-09",
                 "end_on": "2012-07-15",
                 "index": 22
                 }
                 ],
                 "year": "2011-2012"
                 },
                 {
                 "code": "019",
                 "name": "2011-2012学年第一学期",
                 "season": "秋",
                 "week_start_at": 1,
                 "weeks": [
                 {
                 "begin_on": "2011-09-05",
                 "end_on": "2011-09-11",
                 "index": 1
                 },
                 {
                 "begin_on": "2011-09-12",
                 "end_on": "2011-09-18",
                 "index": 2
                 },
                 {
                 "begin_on": "2011-09-19",
                 "end_on": "2011-09-25",
                 "index": 3
                 },
                 {
                 "begin_on": "2011-09-26",
                 "end_on": "2011-10-02",
                 "index": 4
                 },
                 {
                 "begin_on": "2011-10-03",
                 "end_on": "2011-10-09",
                 "index": 5
                 },
                 {
                 "begin_on": "2011-10-10",
                 "end_on": "2011-10-16",
                 "index": 6
                 },
                 {
                 "begin_on": "2011-10-17",
                 "end_on": "2011-10-23",
                 "index": 7
                 },
                 {
                 "begin_on": "2011-10-24",
                 "end_on": "2011-10-30",
                 "index": 8
                 },
                 {
                 "begin_on": "2011-10-31",
                 "end_on": "2011-11-06",
                 "index": 9
                 },
                 {
                 "begin_on": "2011-11-07",
                 "end_on": "2011-11-13",
                 "index": 10
                 },
                 {
                 "begin_on": "2011-11-14",
                 "end_on": "2011-11-20",
                 "index": 11
                 },
                 {
                 "begin_on": "2011-11-21",
                 "end_on": "2011-11-27",
                 "index": 12
                 },
                 {
                 "begin_on": "2011-11-28",
                 "end_on": "2011-12-04",
                 "index": 13
                 },
                 {
                 "begin_on": "2011-12-05",
                 "end_on": "2011-12-11",
                 "index": 14
                 },
                 {
                 "begin_on": "2011-12-12",
                 "end_on": "2011-12-18",
                 "index": 15
                 },
                 {
                 "begin_on": "2011-12-19",
                 "end_on": "2011-12-25",
                 "index": 16
                 },
                 {
                 "begin_on": "2011-12-26",
                 "end_on": "2012-01-01",
                 "index": 17
                 },
                 {
                 "begin_on": "2012-01-02",
                 "end_on": "2012-01-08",
                 "index": 18
                 }
                 ],
                 "year": "2011-2012"
                 },
                 {
                 "code": "018",
                 "name": "2010-2011学年第二学期",
                 "season": "春",
                 "week_start_at": 1,
                 "weeks": [
                 {
                 "begin_on": "2011-02-21",
                 "end_on": "2011-02-27",
                 "index": 1
                 },
                 {
                 "begin_on": "2011-02-28",
                 "end_on": "2011-03-06",
                 "index": 2
                 },
                 {
                 "begin_on": "2011-03-07",
                 "end_on": "2011-03-13",
                 "index": 3
                 },
                 {
                 "begin_on": "2011-03-14",
                 "end_on": "2011-03-20",
                 "index": 4
                 },
                 {
                 "begin_on": "2011-03-21",
                 "end_on": "2011-03-27",
                 "index": 5
                 },
                 {
                 "begin_on": "2011-03-28",
                 "end_on": "2011-04-03",
                 "index": 6
                 },
                 {
                 "begin_on": "2011-04-04",
                 "end_on": "2011-04-10",
                 "index": 7
                 },
                 {
                 "begin_on": "2011-04-11",
                 "end_on": "2011-04-17",
                 "index": 8
                 },
                 {
                 "begin_on": "2011-04-18",
                 "end_on": "2011-04-24",
                 "index": 9
                 },
                 {
                 "begin_on": "2011-04-25",
                 "end_on": "2011-05-01",
                 "index": 10
                 },
                 {
                 "begin_on": "2011-05-02",
                 "end_on": "2011-05-08",
                 "index": 11
                 },
                 {
                 "begin_on": "2011-05-09",
                 "end_on": "2011-05-15",
                 "index": 12
                 },
                 {
                 "begin_on": "2011-05-16",
                 "end_on": "2011-05-22",
                 "index": 13
                 },
                 {
                 "begin_on": "2011-05-23",
                 "end_on": "2011-05-29",
                 "index": 14
                 },
                 {
                 "begin_on": "2011-05-30",
                 "end_on": "2011-06-05",
                 "index": 15
                 },
                 {
                 "begin_on": "2011-06-06",
                 "end_on": "2011-06-12",
                 "index": 16
                 },
                 {
                 "begin_on": "2011-06-13",
                 "end_on": "2011-06-19",
                 "index": 17
                 },
                 {
                 "begin_on": "2011-06-20",
                 "end_on": "2011-06-26",
                 "index": 18
                 },
                 {
                 "begin_on": "2011-06-27",
                 "end_on": "2011-07-03",
                 "index": 19
                 },
                 {
                 "begin_on": "2011-07-04",
                 "end_on": "2011-07-10",
                 "index": 20
                 }
                 ],
                 "year": "2010-2011"
                 },
                 {
                 "code": "017",
                 "name": "2010-2011学年第一学期",
                 "season": "秋",
                 "week_start_at": 1,
                 "weeks": [
                 {
                 "begin_on": "2010-08-30",
                 "end_on": "2010-09-05",
                 "index": 1
                 },
                 {
                 "begin_on": "2010-09-06",
                 "end_on": "2010-09-12",
                 "index": 2
                 },
                 {
                 "begin_on": "2010-09-13",
                 "end_on": "2010-09-19",
                 "index": 3
                 },
                 {
                 "begin_on": "2010-09-20",
                 "end_on": "2010-09-26",
                 "index": 4
                 },
                 {
                 "begin_on": "2010-09-27",
                 "end_on": "2010-10-03",
                 "index": 5
                 },
                 {
                 "begin_on": "2010-10-04",
                 "end_on": "2010-10-10",
                 "index": 6
                 },
                 {
                 "begin_on": "2010-10-11",
                 "end_on": "2010-10-17",
                 "index": 7
                 },
                 {
                 "begin_on": "2010-10-18",
                 "end_on": "2010-10-24",
                 "index": 8
                 },
                 {
                 "begin_on": "2010-10-25",
                 "end_on": "2010-10-31",
                 "index": 9
                 },
                 {
                 "begin_on": "2010-11-01",
                 "end_on": "2010-11-07",
                 "index": 10
                 },
                 {
                 "begin_on": "2010-11-08",
                 "end_on": "2010-11-14",
                 "index": 11
                 },
                 {
                 "begin_on": "2010-11-15",
                 "end_on": "2010-11-21",
                 "index": 12
                 },
                 {
                 "begin_on": "2010-11-22",
                 "end_on": "2010-11-28",
                 "index": 13
                 },
                 {
                 "begin_on": "2010-11-29",
                 "end_on": "2010-12-05",
                 "index": 14
                 },
                 {
                 "begin_on": "2010-12-06",
                 "end_on": "2010-12-12",
                 "index": 15
                 },
                 {
                 "begin_on": "2010-12-13",
                 "end_on": "2010-12-19",
                 "index": 16
                 },
                 {
                 "begin_on": "2010-12-20",
                 "end_on": "2010-12-26",
                 "index": 17
                 },
                 {
                 "begin_on": "2010-12-27",
                 "end_on": "2011-01-02",
                 "index": 18
                 },
                 {
                 "begin_on": "2011-01-03",
                 "end_on": "2011-01-09",
                 "index": 19
                 },
                 {
                 "begin_on": "2011-01-10",
                 "end_on": "2011-01-16",
                 "index": 20
                 },
                 {
                 "begin_on": "2011-01-17",
                 "end_on": "2011-01-23",
                 "index": 21
                 },
                 {
                 "begin_on": "2011-01-24",
                 "end_on": "2011-01-30",
                 "index": 22
                 }
                 ],
                 "year": "2010-2011"
                 },
                 {
                 "code": "016",
                 "name": "2009-2010学年第二学期",
                 "season": "春",
                 "week_start_at": 1,
                 "weeks": [
                 {
                 "begin_on": "2010-03-01",
                 "end_on": "2010-03-07",
                 "index": 1
                 },
                 {
                 "begin_on": "2010-03-08",
                 "end_on": "2010-03-14",
                 "index": 2
                 },
                 {
                 "begin_on": "2010-03-15",
                 "end_on": "2010-03-21",
                 "index": 3
                 },
                 {
                 "begin_on": "2010-03-22",
                 "end_on": "2010-03-28",
                 "index": 4
                 },
                 {
                 "begin_on": "2010-03-29",
                 "end_on": "2010-04-04",
                 "index": 5
                 },
                 {
                 "begin_on": "2010-04-05",
                 "end_on": "2010-04-11",
                 "index": 6
                 },
                 {
                 "begin_on": "2010-04-12",
                 "end_on": "2010-04-18",
                 "index": 7
                 },
                 {
                 "begin_on": "2010-04-19",
                 "end_on": "2010-04-25",
                 "index": 8
                 },
                 {
                 "begin_on": "2010-04-26",
                 "end_on": "2010-05-02",
                 "index": 9
                 },
                 {
                 "begin_on": "2010-05-03",
                 "end_on": "2010-05-09",
                 "index": 10
                 },
                 {
                 "begin_on": "2010-05-10",
                 "end_on": "2010-05-16",
                 "index": 11
                 },
                 {
                 "begin_on": "2010-05-17",
                 "end_on": "2010-05-23",
                 "index": 12
                 },
                 {
                 "begin_on": "2010-05-24",
                 "end_on": "2010-05-30",
                 "index": 13
                 },
                 {
                 "begin_on": "2010-05-31",
                 "end_on": "2010-06-06",
                 "index": 14
                 },
                 {
                 "begin_on": "2010-06-07",
                 "end_on": "2010-06-13",
                 "index": 15
                 },
                 {
                 "begin_on": "2010-06-14",
                 "end_on": "2010-06-20",
                 "index": 16
                 },
                 {
                 "begin_on": "2010-06-21",
                 "end_on": "2010-06-27",
                 "index": 17
                 },
                 {
                 "begin_on": "2010-06-28",
                 "end_on": "2010-07-04",
                 "index": 18
                 },
                 {
                 "begin_on": "2010-07-05",
                 "end_on": "2010-07-11",
                 "index": 19
                 },
                 {
                 "begin_on": "2010-07-12",
                 "end_on": "2010-07-18",
                 "index": 20
                 }
                 ],
                 "year": "2009-2010"
                 },
                 {
                 "code": "015",
                 "name": "2009-2010学年第一学期",
                 "season": "秋",
                 "week_start_at": 1,
                 "weeks": [
                 {
                 "begin_on": "2009-08-31",
                 "end_on": "2009-09-06",
                 "index": 1
                 },
                 {
                 "begin_on": "2009-09-07",
                 "end_on": "2009-09-13",
                 "index": 2
                 },
                 {
                 "begin_on": "2009-09-14",
                 "end_on": "2009-09-20",
                 "index": 3
                 },
                 {
                 "begin_on": "2009-09-21",
                 "end_on": "2009-09-27",
                 "index": 4
                 },
                 {
                 "begin_on": "2009-09-28",
                 "end_on": "2009-10-04",
                 "index": 5
                 },
                 {
                 "begin_on": "2009-10-05",
                 "end_on": "2009-10-11",
                 "index": 6
                 },
                 {
                 "begin_on": "2009-10-12",
                 "end_on": "2009-10-18",
                 "index": 7
                 },
                 {
                 "begin_on": "2009-10-19",
                 "end_on": "2009-10-25",
                 "index": 8
                 },
                 {
                 "begin_on": "2009-10-26",
                 "end_on": "2009-11-01",
                 "index": 9
                 },
                 {
                 "begin_on": "2009-11-02",
                 "end_on": "2009-11-08",
                 "index": 10
                 },
                 {
                 "begin_on": "2009-11-09",
                 "end_on": "2009-11-15",
                 "index": 11
                 },
                 {
                 "begin_on": "2009-11-16",
                 "end_on": "2009-11-22",
                 "index": 12
                 },
                 {
                 "begin_on": "2009-11-23",
                 "end_on": "2009-11-29",
                 "index": 13
                 },
                 {
                 "begin_on": "2009-11-30",
                 "end_on": "2009-12-06",
                 "index": 14
                 },
                 {
                 "begin_on": "2009-12-07",
                 "end_on": "2009-12-13",
                 "index": 15
                 },
                 {
                 "begin_on": "2009-12-14",
                 "end_on": "2009-12-20",
                 "index": 16
                 },
                 {
                 "begin_on": "2009-12-21",
                 "end_on": "2009-12-27",
                 "index": 17
                 },
                 {
                 "begin_on": "2009-12-28",
                 "end_on": "2010-01-03",
                 "index": 18
                 },
                 {
                 "begin_on": "2010-01-04",
                 "end_on": "2010-01-10",
                 "index": 19
                 },
                 {
                 "begin_on": "2010-01-11",
                 "end_on": "2010-01-17",
                 "index": 20
                 },
                 {
                 "begin_on": "2010-01-18",
                 "end_on": "2010-01-24",
                 "index": 21
                 },
                 {
                 "begin_on": "2010-01-25",
                 "end_on": "2010-01-31",
                 "index": 22
                 }
                 ],
                 "year": "2009-2010"
                 },
                 {
                 "code": "014",
                 "name": "2008-2009学年第二学期",
                 "season": "春",
                 "week_start_at": 1,
                 "weeks": [
                 {
                 "begin_on": "2009-02-09",
                 "end_on": "2009-02-15",
                 "index": 1
                 },
                 {
                 "begin_on": "2009-02-16",
                 "end_on": "2009-02-22",
                 "index": 2
                 },
                 {
                 "begin_on": "2009-02-23",
                 "end_on": "2009-03-01",
                 "index": 3
                 },
                 {
                 "begin_on": "2009-03-02",
                 "end_on": "2009-03-08",
                 "index": 4
                 },
                 {
                 "begin_on": "2009-03-09",
                 "end_on": "2009-03-15",
                 "index": 5
                 },
                 {
                 "begin_on": "2009-03-16",
                 "end_on": "2009-03-22",
                 "index": 6
                 },
                 {
                 "begin_on": "2009-03-23",
                 "end_on": "2009-03-29",
                 "index": 7
                 },
                 {
                 "begin_on": "2009-03-30",
                 "end_on": "2009-04-05",
                 "index": 8
                 },
                 {
                 "begin_on": "2009-04-06",
                 "end_on": "2009-04-12",
                 "index": 9
                 },
                 {
                 "begin_on": "2009-04-13",
                 "end_on": "2009-04-19",
                 "index": 10
                 },
                 {
                 "begin_on": "2009-04-20",
                 "end_on": "2009-04-26",
                 "index": 11
                 },
                 {
                 "begin_on": "2009-04-27",
                 "end_on": "2009-05-03",
                 "index": 12
                 },
                 {
                 "begin_on": "2009-05-04",
                 "end_on": "2009-05-10",
                 "index": 13
                 },
                 {
                 "begin_on": "2009-05-11",
                 "end_on": "2009-05-17",
                 "index": 14
                 },
                 {
                 "begin_on": "2009-05-18",
                 "end_on": "2009-05-24",
                 "index": 15
                 },
                 {
                 "begin_on": "2009-05-25",
                 "end_on": "2009-05-31",
                 "index": 16
                 },
                 {
                 "begin_on": "2009-06-01",
                 "end_on": "2009-06-07",
                 "index": 17
                 },
                 {
                 "begin_on": "2009-06-08",
                 "end_on": "2009-06-14",
                 "index": 18
                 },
                 {
                 "begin_on": "2009-06-15",
                 "end_on": "2009-06-21",
                 "index": 19
                 },
                 {
                 "begin_on": "2009-06-22",
                 "end_on": "2009-06-28",
                 "index": 20
                 },
                 {
                 "begin_on": "2009-06-29",
                 "end_on": "2009-07-05",
                 "index": 21
                 },
                 {
                 "begin_on": "2009-07-06",
                 "end_on": "2009-07-12",
                 "index": 22
                 }
                 ],
                 "year": "2008-2009"
                 },
                 {
                 "code": "013",
                 "name": "2008-2009学年第一学期",
                 "season": "秋",
                 "week_start_at": 1,
                 "weeks": [
                 {
                 "begin_on": "2008-09-01",
                 "end_on": "2008-09-07",
                 "index": 1
                 },
                 {
                 "begin_on": "2008-09-08",
                 "end_on": "2008-09-14",
                 "index": 2
                 },
                 {
                 "begin_on": "2008-09-15",
                 "end_on": "2008-09-21",
                 "index": 3
                 },
                 {
                 "begin_on": "2008-09-22",
                 "end_on": "2008-09-28",
                 "index": 4
                 },
                 {
                 "begin_on": "2008-09-29",
                 "end_on": "2008-10-05",
                 "index": 5
                 },
                 {
                 "begin_on": "2008-10-06",
                 "end_on": "2008-10-12",
                 "index": 6
                 },
                 {
                 "begin_on": "2008-10-13",
                 "end_on": "2008-10-19",
                 "index": 7
                 },
                 {
                 "begin_on": "2008-10-20",
                 "end_on": "2008-10-26",
                 "index": 8
                 },
                 {
                 "begin_on": "2008-10-27",
                 "end_on": "2008-11-02",
                 "index": 9
                 },
                 {
                 "begin_on": "2008-11-03",
                 "end_on": "2008-11-09",
                 "index": 10
                 },
                 {
                 "begin_on": "2008-11-10",
                 "end_on": "2008-11-16",
                 "index": 11
                 },
                 {
                 "begin_on": "2008-11-17",
                 "end_on": "2008-11-23",
                 "index": 12
                 },
                 {
                 "begin_on": "2008-11-24",
                 "end_on": "2008-11-30",
                 "index": 13
                 },
                 {
                 "begin_on": "2008-12-01",
                 "end_on": "2008-12-07",
                 "index": 14
                 },
                 {
                 "begin_on": "2008-12-08",
                 "end_on": "2008-12-14",
                 "index": 15
                 },
                 {
                 "begin_on": "2008-12-15",
                 "end_on": "2008-12-21",
                 "index": 16
                 },
                 {
                 "begin_on": "2008-12-22",
                 "end_on": "2008-12-28",
                 "index": 17
                 },
                 {
                 "begin_on": "2008-12-29",
                 "end_on": "2009-01-04",
                 "index": 18
                 },
                 {
                 "begin_on": "2009-01-05",
                 "end_on": "2009-01-11",
                 "index": 19
                 }
                 ],
                 "year": "2008-2009"
                 },
                 {
                 "code": "012",
                 "name": "2007-2008学年第二学期",
                 "season": "春",
                 "week_start_at": 1,
                 "weeks": [
                 {
                 "begin_on": "2008-02-25",
                 "end_on": "2008-03-02",
                 "index": 1
                 },
                 {
                 "begin_on": "2008-03-03",
                 "end_on": "2008-03-09",
                 "index": 2
                 },
                 {
                 "begin_on": "2008-03-10",
                 "end_on": "2008-03-16",
                 "index": 3
                 },
                 {
                 "begin_on": "2008-03-17",
                 "end_on": "2008-03-23",
                 "index": 4
                 },
                 {
                 "begin_on": "2008-03-24",
                 "end_on": "2008-03-30",
                 "index": 5
                 },
                 {
                 "begin_on": "2008-03-31",
                 "end_on": "2008-04-06",
                 "index": 6
                 },
                 {
                 "begin_on": "2008-04-07",
                 "end_on": "2008-04-13",
                 "index": 7
                 },
                 {
                 "begin_on": "2008-04-14",
                 "end_on": "2008-04-20",
                 "index": 8
                 },
                 {
                 "begin_on": "2008-04-21",
                 "end_on": "2008-04-27",
                 "index": 9
                 },
                 {
                 "begin_on": "2008-04-28",
                 "end_on": "2008-05-04",
                 "index": 10
                 },
                 {
                 "begin_on": "2008-05-05",
                 "end_on": "2008-05-11",
                 "index": 11
                 },
                 {
                 "begin_on": "2008-05-12",
                 "end_on": "2008-05-18",
                 "index": 12
                 },
                 {
                 "begin_on": "2008-05-19",
                 "end_on": "2008-05-25",
                 "index": 13
                 },
                 {
                 "begin_on": "2008-05-26",
                 "end_on": "2008-06-01",
                 "index": 14
                 },
                 {
                 "begin_on": "2008-06-02",
                 "end_on": "2008-06-08",
                 "index": 15
                 },
                 {
                 "begin_on": "2008-06-09",
                 "end_on": "2008-06-15",
                 "index": 16
                 },
                 {
                 "begin_on": "2008-06-16",
                 "end_on": "2008-06-22",
                 "index": 17
                 },
                 {
                 "begin_on": "2008-06-23",
                 "end_on": "2008-06-29",
                 "index": 18
                 },
                 {
                 "begin_on": "2008-06-30",
                 "end_on": "2008-07-06",
                 "index": 19
                 },
                 {
                 "begin_on": "2008-07-07",
                 "end_on": "2008-07-13",
                 "index": 20
                 }
                 ],
                 "year": "2007-2008"
                 },
                 {
                 "code": "011",
                 "name": "2007-2008学年第一学期",
                 "season": "秋",
                 "week_start_at": 1,
                 "weeks": [
                 {
                 "begin_on": "2007-09-03",
                 "end_on": "2007-09-09",
                 "index": 1
                 },
                 {
                 "begin_on": "2007-09-10",
                 "end_on": "2007-09-16",
                 "index": 2
                 },
                 {
                 "begin_on": "2007-09-17",
                 "end_on": "2007-09-23",
                 "index": 3
                 },
                 {
                 "begin_on": "2007-09-24",
                 "end_on": "2007-09-30",
                 "index": 4
                 },
                 {
                 "begin_on": "2007-10-01",
                 "end_on": "2007-10-07",
                 "index": 5
                 },
                 {
                 "begin_on": "2007-10-08",
                 "end_on": "2007-10-14",
                 "index": 6
                 },
                 {
                 "begin_on": "2007-10-15",
                 "end_on": "2007-10-21",
                 "index": 7
                 },
                 {
                 "begin_on": "2007-10-22",
                 "end_on": "2007-10-28",
                 "index": 8
                 },
                 {
                 "begin_on": "2007-10-29",
                 "end_on": "2007-11-04",
                 "index": 9
                 },
                 {
                 "begin_on": "2007-11-05",
                 "end_on": "2007-11-11",
                 "index": 10
                 },
                 {
                 "begin_on": "2007-11-12",
                 "end_on": "2007-11-18",
                 "index": 11
                 },
                 {
                 "begin_on": "2007-11-19",
                 "end_on": "2007-11-25",
                 "index": 12
                 },
                 {
                 "begin_on": "2007-11-26",
                 "end_on": "2007-12-02",
                 "index": 13
                 },
                 {
                 "begin_on": "2007-12-03",
                 "end_on": "2007-12-09",
                 "index": 14
                 },
                 {
                 "begin_on": "2007-12-10",
                 "end_on": "2007-12-16",
                 "index": 15
                 },
                 {
                 "begin_on": "2007-12-17",
                 "end_on": "2007-12-23",
                 "index": 16
                 },
                 {
                 "begin_on": "2007-12-24",
                 "end_on": "2007-12-30",
                 "index": 17
                 },
                 {
                 "begin_on": "2007-12-31",
                 "end_on": "2008-01-06",
                 "index": 18
                 },
                 {
                 "begin_on": "2008-01-07",
                 "end_on": "2008-01-13",
                 "index": 19
                 },
                 {
                 "begin_on": "2008-01-14",
                 "end_on": "2008-01-20",
                 "index": 20
                 }
                 ],
                 "year": "2007-2008"
                 },
                 {
                 "code": "010",
                 "name": "2006-2007学年第二学期",
                 "season": "春",
                 "week_start_at": 1,
                 "weeks": [
                 {
                 "begin_on": "2007-01-29",
                 "end_on": "2007-02-04",
                 "index": 1
                 },
                 {
                 "begin_on": "2007-02-05",
                 "end_on": "2007-02-11",
                 "index": 2
                 },
                 {
                 "begin_on": "2007-02-12",
                 "end_on": "2007-02-18",
                 "index": 3
                 },
                 {
                 "begin_on": "2007-02-19",
                 "end_on": "2007-02-25",
                 "index": 4
                 },
                 {
                 "begin_on": "2007-02-26",
                 "end_on": "2007-03-04",
                 "index": 5
                 },
                 {
                 "begin_on": "2007-03-05",
                 "end_on": "2007-03-11",
                 "index": 6
                 },
                 {
                 "begin_on": "2007-03-12",
                 "end_on": "2007-03-18",
                 "index": 7
                 },
                 {
                 "begin_on": "2007-03-19",
                 "end_on": "2007-03-25",
                 "index": 8
                 },
                 {
                 "begin_on": "2007-03-26",
                 "end_on": "2007-04-01",
                 "index": 9
                 },
                 {
                 "begin_on": "2007-04-02",
                 "end_on": "2007-04-08",
                 "index": 10
                 },
                 {
                 "begin_on": "2007-04-09",
                 "end_on": "2007-04-15",
                 "index": 11
                 },
                 {
                 "begin_on": "2007-04-16",
                 "end_on": "2007-04-22",
                 "index": 12
                 },
                 {
                 "begin_on": "2007-04-23",
                 "end_on": "2007-04-29",
                 "index": 13
                 },
                 {
                 "begin_on": "2007-04-30",
                 "end_on": "2007-05-06",
                 "index": 14
                 },
                 {
                 "begin_on": "2007-05-07",
                 "end_on": "2007-05-13",
                 "index": 15
                 },
                 {
                 "begin_on": "2007-05-14",
                 "end_on": "2007-05-20",
                 "index": 16
                 },
                 {
                 "begin_on": "2007-05-21",
                 "end_on": "2007-05-27",
                 "index": 17
                 },
                 {
                 "begin_on": "2007-05-28",
                 "end_on": "2007-06-03",
                 "index": 18
                 },
                 {
                 "begin_on": "2007-06-04",
                 "end_on": "2007-06-10",
                 "index": 19
                 },
                 {
                 "begin_on": "2007-06-11",
                 "end_on": "2007-06-17",
                 "index": 20
                 },
                 {
                 "begin_on": "2007-06-18",
                 "end_on": "2007-06-24",
                 "index": 21
                 },
                 {
                 "begin_on": "2007-06-25",
                 "end_on": "2007-07-01",
                 "index": 22
                 },
                 {
                 "begin_on": "2007-07-02",
                 "end_on": "2007-07-08",
                 "index": 23
                 }
                 ],
                 "year": "2006-2007"
                 },
                 {
                 "code": "009",
                 "name": "2006-2007学年第一学期",
                 "season": "秋",
                 "week_start_at": 1,
                 "weeks": [
                 {
                 "begin_on": "2006-08-28",
                 "end_on": "2006-09-03",
                 "index": 1
                 },
                 {
                 "begin_on": "2006-09-04",
                 "end_on": "2006-09-10",
                 "index": 2
                 },
                 {
                 "begin_on": "2006-09-11",
                 "end_on": "2006-09-17",
                 "index": 3
                 },
                 {
                 "begin_on": "2006-09-18",
                 "end_on": "2006-09-24",
                 "index": 4
                 },
                 {
                 "begin_on": "2006-09-25",
                 "end_on": "2006-10-01",
                 "index": 5
                 },
                 {
                 "begin_on": "2006-10-02",
                 "end_on": "2006-10-08",
                 "index": 6
                 },
                 {
                 "begin_on": "2006-10-09",
                 "end_on": "2006-10-15",
                 "index": 7
                 },
                 {
                 "begin_on": "2006-10-16",
                 "end_on": "2006-10-22",
                 "index": 8
                 },
                 {
                 "begin_on": "2006-10-23",
                 "end_on": "2006-10-29",
                 "index": 9
                 },
                 {
                 "begin_on": "2006-10-30",
                 "end_on": "2006-11-05",
                 "index": 10
                 },
                 {
                 "begin_on": "2006-11-06",
                 "end_on": "2006-11-12",
                 "index": 11
                 },
                 {
                 "begin_on": "2006-11-13",
                 "end_on": "2006-11-19",
                 "index": 12
                 },
                 {
                 "begin_on": "2006-11-20",
                 "end_on": "2006-11-26",
                 "index": 13
                 },
                 {
                 "begin_on": "2006-11-27",
                 "end_on": "2006-12-03",
                 "index": 14
                 },
                 {
                 "begin_on": "2006-12-04",
                 "end_on": "2006-12-10",
                 "index": 15
                 },
                 {
                 "begin_on": "2006-12-11",
                 "end_on": "2006-12-17",
                 "index": 16
                 },
                 {
                 "begin_on": "2006-12-18",
                 "end_on": "2006-12-24",
                 "index": 17
                 },
                 {
                 "begin_on": "2006-12-25",
                 "end_on": "2006-12-31",
                 "index": 18
                 },
                 {
                 "begin_on": "2007-01-01",
                 "end_on": "2007-01-07",
                 "index": 19
                 },
                 {
                 "begin_on": "2007-01-08",
                 "end_on": "2007-01-14",
                 "index": 20
                 },
                 {
                 "begin_on": "2007-01-15",
                 "end_on": "2007-01-21",
                 "index": 21
                 },
                 {
                 "begin_on": "2007-01-22",
                 "end_on": "2007-01-28",
                 "index": 22
                 }
                 ],
                 "year": "2006-2007"
                 },
                 {
                 "code": "008",
                 "name": "2005-2006学年第二学期",
                 "season": "春",
                 "week_start_at": 1,
                 "weeks": [
                 {
                 "begin_on": "2006-02-13",
                 "end_on": "2006-02-19",
                 "index": 1
                 },
                 {
                 "begin_on": "2006-02-20",
                 "end_on": "2006-02-26",
                 "index": 2
                 },
                 {
                 "begin_on": "2006-02-27",
                 "end_on": "2006-03-05",
                 "index": 3
                 },
                 {
                 "begin_on": "2006-03-06",
                 "end_on": "2006-03-12",
                 "index": 4
                 },
                 {
                 "begin_on": "2006-03-13",
                 "end_on": "2006-03-19",
                 "index": 5
                 },
                 {
                 "begin_on": "2006-03-20",
                 "end_on": "2006-03-26",
                 "index": 6
                 },
                 {
                 "begin_on": "2006-03-27",
                 "end_on": "2006-04-02",
                 "index": 7
                 },
                 {
                 "begin_on": "2006-04-03",
                 "end_on": "2006-04-09",
                 "index": 8
                 },
                 {
                 "begin_on": "2006-04-10",
                 "end_on": "2006-04-16",
                 "index": 9
                 },
                 {
                 "begin_on": "2006-04-17",
                 "end_on": "2006-04-23",
                 "index": 10
                 },
                 {
                 "begin_on": "2006-04-24",
                 "end_on": "2006-04-30",
                 "index": 11
                 },
                 {
                 "begin_on": "2006-05-01",
                 "end_on": "2006-05-07",
                 "index": 12
                 },
                 {
                 "begin_on": "2006-05-08",
                 "end_on": "2006-05-14",
                 "index": 13
                 },
                 {
                 "begin_on": "2006-05-15",
                 "end_on": "2006-05-21",
                 "index": 14
                 },
                 {
                 "begin_on": "2006-05-22",
                 "end_on": "2006-05-28",
                 "index": 15
                 },
                 {
                 "begin_on": "2006-05-29",
                 "end_on": "2006-06-04",
                 "index": 16
                 },
                 {
                 "begin_on": "2006-06-05",
                 "end_on": "2006-06-11",
                 "index": 17
                 },
                 {
                 "begin_on": "2006-06-12",
                 "end_on": "2006-06-18",
                 "index": 18
                 },
                 {
                 "begin_on": "2006-06-19",
                 "end_on": "2006-06-25",
                 "index": 19
                 },
                 {
                 "begin_on": "2006-06-26",
                 "end_on": "2006-07-02",
                 "index": 20
                 },
                 {
                 "begin_on": "2006-07-03",
                 "end_on": "2006-07-09",
                 "index": 21
                 }
                 ],
                 "year": "2005-2006"
                 },
                 {
                 "code": "007",
                 "name": "2005-2006学年第一学期",
                 "season": "秋",
                 "week_start_at": 1,
                 "weeks": [
                 {
                 "begin_on": "2005-08-29",
                 "end_on": "2005-09-04",
                 "index": 1
                 },
                 {
                 "begin_on": "2005-09-05",
                 "end_on": "2005-09-11",
                 "index": 2
                 },
                 {
                 "begin_on": "2005-09-12",
                 "end_on": "2005-09-18",
                 "index": 3
                 },
                 {
                 "begin_on": "2005-09-19",
                 "end_on": "2005-09-25",
                 "index": 4
                 },
                 {
                 "begin_on": "2005-09-26",
                 "end_on": "2005-10-02",
                 "index": 5
                 },
                 {
                 "begin_on": "2005-10-03",
                 "end_on": "2005-10-09",
                 "index": 6
                 },
                 {
                 "begin_on": "2005-10-10",
                 "end_on": "2005-10-16",
                 "index": 7
                 },
                 {
                 "begin_on": "2005-10-17",
                 "end_on": "2005-10-23",
                 "index": 8
                 },
                 {
                 "begin_on": "2005-10-24",
                 "end_on": "2005-10-30",
                 "index": 9
                 },
                 {
                 "begin_on": "2005-10-31",
                 "end_on": "2005-11-06",
                 "index": 10
                 },
                 {
                 "begin_on": "2005-11-07",
                 "end_on": "2005-11-13",
                 "index": 11
                 },
                 {
                 "begin_on": "2005-11-14",
                 "end_on": "2005-11-20",
                 "index": 12
                 },
                 {
                 "begin_on": "2005-11-21",
                 "end_on": "2005-11-27",
                 "index": 13
                 },
                 {
                 "begin_on": "2005-11-28",
                 "end_on": "2005-12-04",
                 "index": 14
                 },
                 {
                 "begin_on": "2005-12-05",
                 "end_on": "2005-12-11",
                 "index": 15
                 },
                 {
                 "begin_on": "2005-12-12",
                 "end_on": "2005-12-18",
                 "index": 16
                 },
                 {
                 "begin_on": "2005-12-19",
                 "end_on": "2005-12-25",
                 "index": 17
                 },
                 {
                 "begin_on": "2005-12-26",
                 "end_on": "2006-01-01",
                 "index": 18
                 },
                 {
                 "begin_on": "2006-01-02",
                 "end_on": "2006-01-08",
                 "index": 19
                 },
                 {
                 "begin_on": "2006-01-09",
                 "end_on": "2006-01-15",
                 "index": 20
                 }
                 ],
                 "year": "2005-2006"
                 },
                 {
                 "code": "006",
                 "name": "2004-2005学年第二学期",
                 "season": "春",
                 "week_start_at": 1,
                 "weeks": [
                 {
                 "begin_on": "2005-02-21",
                 "end_on": "2005-02-27",
                 "index": 1
                 },
                 {
                 "begin_on": "2005-02-28",
                 "end_on": "2005-03-06",
                 "index": 2
                 },
                 {
                 "begin_on": "2005-03-07",
                 "end_on": "2005-03-13",
                 "index": 3
                 },
                 {
                 "begin_on": "2005-03-14",
                 "end_on": "2005-03-20",
                 "index": 4
                 },
                 {
                 "begin_on": "2005-03-21",
                 "end_on": "2005-03-27",
                 "index": 5
                 },
                 {
                 "begin_on": "2005-03-28",
                 "end_on": "2005-04-03",
                 "index": 6
                 },
                 {
                 "begin_on": "2005-04-04",
                 "end_on": "2005-04-10",
                 "index": 7
                 },
                 {
                 "begin_on": "2005-04-11",
                 "end_on": "2005-04-17",
                 "index": 8
                 },
                 {
                 "begin_on": "2005-04-18",
                 "end_on": "2005-04-24",
                 "index": 9
                 },
                 {
                 "begin_on": "2005-04-25",
                 "end_on": "2005-05-01",
                 "index": 10
                 },
                 {
                 "begin_on": "2005-05-02",
                 "end_on": "2005-05-08",
                 "index": 11
                 },
                 {
                 "begin_on": "2005-05-09",
                 "end_on": "2005-05-15",
                 "index": 12
                 },
                 {
                 "begin_on": "2005-05-16",
                 "end_on": "2005-05-22",
                 "index": 13
                 },
                 {
                 "begin_on": "2005-05-23",
                 "end_on": "2005-05-29",
                 "index": 14
                 },
                 {
                 "begin_on": "2005-05-30",
                 "end_on": "2005-06-05",
                 "index": 15
                 },
                 {
                 "begin_on": "2005-06-06",
                 "end_on": "2005-06-12",
                 "index": 16
                 },
                 {
                 "begin_on": "2005-06-13",
                 "end_on": "2005-06-19",
                 "index": 17
                 },
                 {
                 "begin_on": "2005-06-20",
                 "end_on": "2005-06-26",
                 "index": 18
                 },
                 {
                 "begin_on": "2005-06-27",
                 "end_on": "2005-07-03",
                 "index": 19
                 },
                 {
                 "begin_on": "2005-07-04",
                 "end_on": "2005-07-10",
                 "index": 20
                 }
                 ],
                 "year": "2004-2005"
                 },
                 {
                 "code": "005",
                 "name": "2004-2005学年第一学期",
                 "season": "秋",
                 "week_start_at": 1,
                 "weeks": [
                 {
                 "begin_on": "2004-08-30",
                 "end_on": "2004-09-05",
                 "index": 1
                 },
                 {
                 "begin_on": "2004-09-06",
                 "end_on": "2004-09-12",
                 "index": 2
                 },
                 {
                 "begin_on": "2004-09-13",
                 "end_on": "2004-09-19",
                 "index": 3
                 },
                 {
                 "begin_on": "2004-09-20",
                 "end_on": "2004-09-26",
                 "index": 4
                 },
                 {
                 "begin_on": "2004-09-27",
                 "end_on": "2004-10-03",
                 "index": 5
                 },
                 {
                 "begin_on": "2004-10-04",
                 "end_on": "2004-10-10",
                 "index": 6
                 },
                 {
                 "begin_on": "2004-10-11",
                 "end_on": "2004-10-17",
                 "index": 7
                 },
                 {
                 "begin_on": "2004-10-18",
                 "end_on": "2004-10-24",
                 "index": 8
                 },
                 {
                 "begin_on": "2004-10-25",
                 "end_on": "2004-10-31",
                 "index": 9
                 },
                 {
                 "begin_on": "2004-11-01",
                 "end_on": "2004-11-07",
                 "index": 10
                 },
                 {
                 "begin_on": "2004-11-08",
                 "end_on": "2004-11-14",
                 "index": 11
                 },
                 {
                 "begin_on": "2004-11-15",
                 "end_on": "2004-11-21",
                 "index": 12
                 },
                 {
                 "begin_on": "2004-11-22",
                 "end_on": "2004-11-28",
                 "index": 13
                 },
                 {
                 "begin_on": "2004-11-29",
                 "end_on": "2004-12-05",
                 "index": 14
                 },
                 {
                 "begin_on": "2004-12-06",
                 "end_on": "2004-12-12",
                 "index": 15
                 },
                 {
                 "begin_on": "2004-12-13",
                 "end_on": "2004-12-19",
                 "index": 16
                 },
                 {
                 "begin_on": "2004-12-20",
                 "end_on": "2004-12-26",
                 "index": 17
                 },
                 {
                 "begin_on": "2004-12-27",
                 "end_on": "2005-01-02",
                 "index": 18
                 },
                 {
                 "begin_on": "2005-01-03",
                 "end_on": "2005-01-09",
                 "index": 19
                 },
                 {
                 "begin_on": "2005-01-10",
                 "end_on": "2005-01-16",
                 "index": 20
                 },
                 {
                 "begin_on": "2005-01-17",
                 "end_on": "2005-01-23",
                 "index": 21
                 }
                 ],
                 "year": "2004-2005"
                 },
                 {
                 "code": "004",
                 "name": "2003-2004学年第二学期",
                 "season": "春",
                 "week_start_at": 1,
                 "weeks": [
                 {
                 "begin_on": "2004-03-08",
                 "end_on": "2004-03-14",
                 "index": 1
                 },
                 {
                 "begin_on": "2004-03-15",
                 "end_on": "2004-03-21",
                 "index": 2
                 },
                 {
                 "begin_on": "2004-03-22",
                 "end_on": "2004-03-28",
                 "index": 3
                 },
                 {
                 "begin_on": "2004-03-29",
                 "end_on": "2004-04-04",
                 "index": 4
                 },
                 {
                 "begin_on": "2004-04-05",
                 "end_on": "2004-04-11",
                 "index": 5
                 },
                 {
                 "begin_on": "2004-04-12",
                 "end_on": "2004-04-18",
                 "index": 6
                 },
                 {
                 "begin_on": "2004-04-19",
                 "end_on": "2004-04-25",
                 "index": 7
                 },
                 {
                 "begin_on": "2004-04-26",
                 "end_on": "2004-05-02",
                 "index": 8
                 },
                 {
                 "begin_on": "2004-05-03",
                 "end_on": "2004-05-09",
                 "index": 9
                 },
                 {
                 "begin_on": "2004-05-10",
                 "end_on": "2004-05-16",
                 "index": 10
                 },
                 {
                 "begin_on": "2004-05-17",
                 "end_on": "2004-05-23",
                 "index": 11
                 },
                 {
                 "begin_on": "2004-05-24",
                 "end_on": "2004-05-30",
                 "index": 12
                 },
                 {
                 "begin_on": "2004-05-31",
                 "end_on": "2004-06-06",
                 "index": 13
                 },
                 {
                 "begin_on": "2004-06-07",
                 "end_on": "2004-06-13",
                 "index": 14
                 },
                 {
                 "begin_on": "2004-06-14",
                 "end_on": "2004-06-20",
                 "index": 15
                 },
                 {
                 "begin_on": "2004-06-21",
                 "end_on": "2004-06-27",
                 "index": 16
                 },
                 {
                 "begin_on": "2004-06-28",
                 "end_on": "2004-07-04",
                 "index": 17
                 },
                 {
                 "begin_on": "2004-07-05",
                 "end_on": "2004-07-11",
                 "index": 18
                 },
                 {
                 "begin_on": "2004-07-12",
                 "end_on": "2004-07-18",
                 "index": 19
                 },
                 {
                 "begin_on": "2004-07-19",
                 "end_on": "2004-07-25",
                 "index": 20
                 },
                 {
                 "begin_on": "2004-07-26",
                 "end_on": "2004-08-01",
                 "index": 21
                 },
                 {
                 "begin_on": "2004-08-02",
                 "end_on": "2004-08-08",
                 "index": 22
                 },
                 {
                 "begin_on": "2004-08-09",
                 "end_on": "2004-08-15",
                 "index": 23
                 },
                 {
                 "begin_on": "2004-08-16",
                 "end_on": "2004-08-22",
                 "index": 24
                 },
                 {
                 "begin_on": "2004-08-23",
                 "end_on": "2004-08-29",
                 "index": 25
                 }
                 ],
                 "year": "2003-2004"
                 },
                 {
                 "code": "003",
                 "name": "2003-2004学年第一学期",
                 "season": "秋",
                 "week_start_at": 1,
                 "weeks": [
                 {
                 "begin_on": "2003-09-01",
                 "end_on": "2003-09-07",
                 "index": 1
                 },
                 {
                 "begin_on": "2003-09-08",
                 "end_on": "2003-09-14",
                 "index": 2
                 },
                 {
                 "begin_on": "2003-09-15",
                 "end_on": "2003-09-21",
                 "index": 3
                 },
                 {
                 "begin_on": "2003-09-22",
                 "end_on": "2003-09-28",
                 "index": 4
                 },
                 {
                 "begin_on": "2003-09-29",
                 "end_on": "2003-10-05",
                 "index": 5
                 },
                 {
                 "begin_on": "2003-10-06",
                 "end_on": "2003-10-12",
                 "index": 6
                 },
                 {
                 "begin_on": "2003-10-13",
                 "end_on": "2003-10-19",
                 "index": 7
                 },
                 {
                 "begin_on": "2003-10-20",
                 "end_on": "2003-10-26",
                 "index": 8
                 },
                 {
                 "begin_on": "2003-10-27",
                 "end_on": "2003-11-02",
                 "index": 9
                 },
                 {
                 "begin_on": "2003-11-03",
                 "end_on": "2003-11-09",
                 "index": 10
                 },
                 {
                 "begin_on": "2003-11-10",
                 "end_on": "2003-11-16",
                 "index": 11
                 },
                 {
                 "begin_on": "2003-11-17",
                 "end_on": "2003-11-23",
                 "index": 12
                 },
                 {
                 "begin_on": "2003-11-24",
                 "end_on": "2003-11-30",
                 "index": 13
                 },
                 {
                 "begin_on": "2003-12-01",
                 "end_on": "2003-12-07",
                 "index": 14
                 },
                 {
                 "begin_on": "2003-12-08",
                 "end_on": "2003-12-14",
                 "index": 15
                 },
                 {
                 "begin_on": "2003-12-15",
                 "end_on": "2003-12-21",
                 "index": 16
                 },
                 {
                 "begin_on": "2003-12-22",
                 "end_on": "2003-12-28",
                 "index": 17
                 },
                 {
                 "begin_on": "2003-12-29",
                 "end_on": "2004-01-04",
                 "index": 18
                 }
                 ],
                 "year": "2003-2004"
                 },
                 {
                 "code": "002",
                 "name": "2002-2003学年第二学期",
                 "season": "春",
                 "week_start_at": 1,
                 "weeks": [
                 {
                 "begin_on": "2003-02-24",
                 "end_on": "2003-03-02",
                 "index": 1
                 },
                 {
                 "begin_on": "2003-03-03",
                 "end_on": "2003-03-09",
                 "index": 2
                 },
                 {
                 "begin_on": "2003-03-10",
                 "end_on": "2003-03-16",
                 "index": 3
                 },
                 {
                 "begin_on": "2003-03-17",
                 "end_on": "2003-03-23",
                 "index": 4
                 },
                 {
                 "begin_on": "2003-03-24",
                 "end_on": "2003-03-30",
                 "index": 5
                 },
                 {
                 "begin_on": "2003-03-31",
                 "end_on": "2003-04-06",
                 "index": 6
                 },
                 {
                 "begin_on": "2003-04-07",
                 "end_on": "2003-04-13",
                 "index": 7
                 },
                 {
                 "begin_on": "2003-04-14",
                 "end_on": "2003-04-20",
                 "index": 8
                 },
                 {
                 "begin_on": "2003-04-21",
                 "end_on": "2003-04-27",
                 "index": 9
                 },
                 {
                 "begin_on": "2003-04-28",
                 "end_on": "2003-05-04",
                 "index": 10
                 },
                 {
                 "begin_on": "2003-05-05",
                 "end_on": "2003-05-11",
                 "index": 11
                 },
                 {
                 "begin_on": "2003-05-12",
                 "end_on": "2003-05-18",
                 "index": 12
                 },
                 {
                 "begin_on": "2003-05-19",
                 "end_on": "2003-05-25",
                 "index": 13
                 },
                 {
                 "begin_on": "2003-05-26",
                 "end_on": "2003-06-01",
                 "index": 14
                 },
                 {
                 "begin_on": "2003-06-02",
                 "end_on": "2003-06-08",
                 "index": 15
                 },
                 {
                 "begin_on": "2003-06-09",
                 "end_on": "2003-06-15",
                 "index": 16
                 },
                 {
                 "begin_on": "2003-06-16",
                 "end_on": "2003-06-22",
                 "index": 17
                 },
                 {
                 "begin_on": "2003-06-23",
                 "end_on": "2003-06-29",
                 "index": 18
                 },
                 {
                 "begin_on": "2003-06-30",
                 "end_on": "2003-07-06",
                 "index": 19
                 },
                 {
                 "begin_on": "2003-07-07",
                 "end_on": "2003-07-13",
                 "index": 20
                 }
                 ],
                 "year": "2002-2003"
                 },
                 {
                 "code": "001",
                 "name": "2002-2003学年第一学期",
                 "season": "秋",
                 "week_start_at": 1,
                 "weeks": [
                 {
                 "begin_on": "2002-09-02",
                 "end_on": "2002-09-08",
                 "index": 1
                 },
                 {
                 "begin_on": "2002-09-09",
                 "end_on": "2002-09-15",
                 "index": 2
                 },
                 {
                 "begin_on": "2002-09-16",
                 "end_on": "2002-09-22",
                 "index": 3
                 },
                 {
                 "begin_on": "2002-09-23",
                 "end_on": "2002-09-29",
                 "index": 4
                 },
                 {
                 "begin_on": "2002-09-30",
                 "end_on": "2002-10-06",
                 "index": 5
                 },
                 {
                 "begin_on": "2002-10-07",
                 "end_on": "2002-10-13",
                 "index": 6
                 },
                 {
                 "begin_on": "2002-10-14",
                 "end_on": "2002-10-20",
                 "index": 7
                 },
                 {
                 "begin_on": "2002-10-21",
                 "end_on": "2002-10-27",
                 "index": 8
                 },
                 {
                 "begin_on": "2002-10-28",
                 "end_on": "2002-11-03",
                 "index": 9
                 },
                 {
                 "begin_on": "2002-11-04",
                 "end_on": "2002-11-10",
                 "index": 10
                 },
                 {
                 "begin_on": "2002-11-11",
                 "end_on": "2002-11-17",
                 "index": 11
                 },
                 {
                 "begin_on": "2002-11-18",
                 "end_on": "2002-11-24",
                 "index": 12
                 },
                 {
                 "begin_on": "2002-11-25",
                 "end_on": "2002-12-01",
                 "index": 13
                 },
                 {
                 "begin_on": "2002-12-02",
                 "end_on": "2002-12-08",
                 "index": 14
                 },
                 {
                 "begin_on": "2002-12-09",
                 "end_on": "2002-12-15",
                 "index": 15
                 },
                 {
                 "begin_on": "2002-12-16",
                 "end_on": "2002-12-22",
                 "index": 16
                 },
                 {
                 "begin_on": "2002-12-23",
                 "end_on": "2002-12-29",
                 "index": 17
                 },
                 {
                 "begin_on": "2002-12-30",
                 "end_on": "2003-01-05",
                 "index": 18
                 },
                 {
                 "begin_on": "2003-01-06",
                 "end_on": "2003-01-12",
                 "index": 19
                 },
                 {
                 "begin_on": "2003-01-13",
                 "end_on": "2003-01-19",
                 "index": 20
                 }
                 ],
                 "year": "2002-2003"
                 }
                 ]
                 },
                 "err_code": "00000",
                 "err_msg": ""
                 },
                 "error": null
                 }
                 */
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject responseJson = new JSONObject(response);
                            if (responseJson.getInt("code") == 200) {
                                Toast.makeText(MainActivity.this, responseJson.getString("msg"), Toast.LENGTH_SHORT).show();
                                JSONObject objJson = responseJson.getJSONObject("obj");
                                if (objJson != null) {
                                    semesterWeekList = objJson.getJSONObject("business_data");
                                    if (semesterWeekList != null) {
                                        semestercode = semesterWeekList.getString("cur_semester_code");
                                        weekIndx = semesterWeekList.getString("cur_week_index");
                                        menu.performIdentifierAction(R.id.nav_course, 0);
                                    } else {
                                        Toast.makeText(MainActivity.this, objJson.getString("err_msg"), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } else {
                                Toast.makeText(MainActivity.this, responseJson.getString("error"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                });
            }
        }.start();
    }

    /**
     * 动态添加菜单
     *
     * @param menu
     */
    private void addMenu(Menu menu) {
        try {
            JSONArray semesters = semesterWeekList.getJSONArray("semesters");
            for (int i = 0; i < semesters.length(); i++) {
                menu.add(0, semesters.getJSONObject(i).getInt("code"), i, semesters.getJSONObject(i).getString("name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
