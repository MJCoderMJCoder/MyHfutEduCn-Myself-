package com.lzf.myhfuteducn;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzf.myhfuteducn.bean.Comment;
import com.lzf.myhfuteducn.bean.Log;
import com.lzf.myhfuteducn.util.OkHttpUtil;
import com.lzf.myhfuteducn.util.ReusableAdapter;
import com.lzf.myhfuteducn.util.SharedPreferencesUtil;
import com.lzf.myhfuteducn.util.UrlUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的社区界面的UI控制层
 *
 * @author MJCoder
 * @see AppCompatActivity
 */
public class MyCommunityActivity extends AppCompatActivity {
    /**
     * 搜索文本输入框
     */
    private EditText searchET;
    /**
     * 评论控件
     */
    private RelativeLayout commentRL;
    /**
     * 评论的文本编辑框
     */
    private EditText commentET;
    /**
     * 评论的文本编辑框的文本内容
     */
    private String commentETValue;
    /**
     * 是否匿名评论的选项框
     */
    private CheckBox checkBox;
    /**
     * 发表评论按钮
     */
    private Button commentSend;
    /**
     * 用于显示一系列日志的列表视图
     */
    private ListView logListView;
    /**
     * 从阿里云服务端返回的日志数据列表
     */
    private List<com.lzf.myhfuteducn.bean.Log> logData;
    /**
     * logListView的适配器，用于处理一系列日志的列表视图的具体显示内容
     */
    private ReusableAdapter<Log> logReusableAdapter;
    /**
     * 该变量用于格式化日期显示
     */
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM--dd HH:mm");
    /**
     * 用户点击选中的具体的某个Log日志
     */
    private com.lzf.myhfuteducn.bean.Log log;
    /**
     * 用户点击的该Log日志在列表视图中的位置
     */
    private int position;

    /**
     * Activity首次被创建时会调用该方法
     *
     * @param savedInstanceState
     * @see Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_community);
        searchET = findViewById(R.id.searchET);
        searchET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //关闭软键盘
                    InputMethodManager manager = (InputMethodManager) MyCommunityActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    manager.hideSoftInputFromWindow(searchET.getWindowToken(), 0);
                    new Thread() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void run() {
                            super.run();
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("logTxt", searchET.getText().toString());
                            params.put("logUserName", SharedPreferencesUtil.get(MyCommunityActivity.this, "user_name", "") + "");
                            params.put("logUserPhone", SharedPreferencesUtil.get(MyCommunityActivity.this, "mobile_phone", "") + "");
                            params.put("logUserEmail", SharedPreferencesUtil.get(MyCommunityActivity.this, "account_email", "") + "");
                            params.put("logUserGender", SharedPreferencesUtil.get(MyCommunityActivity.this, "gender", "") + "");
                            params.put("logUserBirthday", SharedPreferencesUtil.get(MyCommunityActivity.this, "birthday", "") + "");
                            params.put("logUserDepart", SharedPreferencesUtil.get(MyCommunityActivity.this, "depart_name", "") + "");
                            params.put("logUserMajor", SharedPreferencesUtil.get(MyCommunityActivity.this, "major_name", "") + "");
                            params.put("logUserClass", SharedPreferencesUtil.get(MyCommunityActivity.this, "adminclass_name", "") + "");
                            final String response = OkHttpUtil.submit(UrlUtil.LOG_SELECT, params);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        JSONObject responseJsonObject = new JSONObject(response);
                                        if (responseJsonObject.getBoolean("success")) {
                                            List<Log> logData = new Gson().fromJson(responseJsonObject.getJSONArray("data").toString(), new TypeToken<List<Log>>() {
                                            }.getType());
                                            logReusableAdapter.updateAll(logData);
                                        } else {
                                            Toast.makeText(MyCommunityActivity.this, responseJsonObject.getString("describe"), Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        Toast.makeText(MyCommunityActivity.this, response, Toast.LENGTH_SHORT).show();
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    }.start();
                    return true;
                }
                return false;
            }
        });
        commentRL = findViewById(R.id.commentRL);
        commentET = findViewById(R.id.commentET);
        checkBox = findViewById(R.id.checkBox);
        commentSend = findViewById(R.id.commentSend);
        commentSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (commentCheck()) {
                    new Thread() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void run() {
                            super.run();
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("commentTxt", commentETValue);
                            params.put("commentLog", log.getLogId() + "");
                            params.put("commentUserName", SharedPreferencesUtil.get(MyCommunityActivity.this, "user_name", "") + "");
                            params.put("commentUserPhone", SharedPreferencesUtil.get(MyCommunityActivity.this, "mobile_phone", "") + "");
                            params.put("commentUserEmail", SharedPreferencesUtil.get(MyCommunityActivity.this, "account_email", "") + "");
                            params.put("commentUserGender", SharedPreferencesUtil.get(MyCommunityActivity.this, "gender", "") + "");
                            params.put("commentUserBirthday", SharedPreferencesUtil.get(MyCommunityActivity.this, "birthday", "") + "");
                            params.put("commentUserDepart", SharedPreferencesUtil.get(MyCommunityActivity.this, "depart_name", "") + "");
                            params.put("commentUserMajor", SharedPreferencesUtil.get(MyCommunityActivity.this, "major_name", "") + "");
                            params.put("commentUserClass", SharedPreferencesUtil.get(MyCommunityActivity.this, "adminclass_name", "") + "");
                            params.put("commentIsAnonymity", checkBox.isChecked() + "");
                            params.put("replyUserName", "");
                            params.put("replyUserPhone", "");
                            params.put("replyUserEmail", "");
                            params.put("replyUserGender", "");
                            params.put("replyUserBirthday", "");
                            params.put("replyUserDepart", "");
                            params.put("replyUserMajor", "");
                            params.put("replyUserClass", "");
                            final String response = OkHttpUtil.submit(UrlUtil.COMMENT_INSERT, params);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        JSONObject responseJsonObject = new JSONObject(response);
                                        if (responseJsonObject.getBoolean("success")) {
                                            //关闭软键盘
                                            InputMethodManager manager = (InputMethodManager) MyCommunityActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                                            manager.hideSoftInputFromWindow(commentET.getWindowToken(), 0);
                                            commentRL.setVisibility(View.GONE);
                                            log.getComments().add(new Comment(0, System.currentTimeMillis(), commentETValue, log.getLogId(),
                                                    SharedPreferencesUtil.get(MyCommunityActivity.this, "user_name", "") + "",
                                                    SharedPreferencesUtil.get(MyCommunityActivity.this, "mobile_phone", "") + "",
                                                    SharedPreferencesUtil.get(MyCommunityActivity.this, "account_email", "") + "",
                                                    SharedPreferencesUtil.get(MyCommunityActivity.this, "gender", "") + "",
                                                    SharedPreferencesUtil.get(MyCommunityActivity.this, "birthday", "") + "",
                                                    SharedPreferencesUtil.get(MyCommunityActivity.this, "depart_name", "") + "",
                                                    SharedPreferencesUtil.get(MyCommunityActivity.this, "major_name", "") + "",
                                                    SharedPreferencesUtil.get(MyCommunityActivity.this, "adminclass_name", "") + "",
                                                    checkBox.isChecked(), "", "", "", "", "", "", "", ""));
                                            logReusableAdapter.update(position, log);
                                        } else {
                                            Toast.makeText(MyCommunityActivity.this, responseJsonObject.getString("describe"), Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        Toast.makeText(MyCommunityActivity.this, response, Toast.LENGTH_SHORT).show();
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    }.start();
                }
            }
        });
        logListView = findViewById(R.id.logListView);
    }

    /**
     * 恢复Activity时回调；确保每次进入该界面都进行刷新最新的数据信息
     */
    @Override
    public void onResume() {
        super.onResume();
        new Thread() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {
                super.run();
                Map<String, String> params = new HashMap<String, String>();
                params.put("logTxt", searchET.getText().toString());
                params.put("logUserName", SharedPreferencesUtil.get(MyCommunityActivity.this, "user_name", "") + "");
                params.put("logUserPhone", SharedPreferencesUtil.get(MyCommunityActivity.this, "mobile_phone", "") + "");
                params.put("logUserEmail", SharedPreferencesUtil.get(MyCommunityActivity.this, "account_email", "") + "");
                params.put("logUserGender", SharedPreferencesUtil.get(MyCommunityActivity.this, "gender", "") + "");
                params.put("logUserBirthday", SharedPreferencesUtil.get(MyCommunityActivity.this, "birthday", "") + "");
                params.put("logUserDepart", SharedPreferencesUtil.get(MyCommunityActivity.this, "depart_name", "") + "");
                params.put("logUserMajor", SharedPreferencesUtil.get(MyCommunityActivity.this, "major_name", "") + "");
                params.put("logUserClass", SharedPreferencesUtil.get(MyCommunityActivity.this, "adminclass_name", "") + "");
                final String response = OkHttpUtil.submit(UrlUtil.LOG_SELECT, params);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject responseJsonObject = new JSONObject(response);
                            if (responseJsonObject.getBoolean("success")) {
                                if (logData != null) {
                                    logData.clear();
                                }
                                logData = new Gson().fromJson(responseJsonObject.getJSONArray("data").toString(), new TypeToken<List<com.lzf.myhfuteducn.bean.Log>>() {
                                }.getType());
                                logReusableAdapter = new ReusableAdapter<com.lzf.myhfuteducn.bean.Log>(logData, R.layout.item_my_log) {
                                    @Override
                                    public void bindView(final ViewHolder holder, final com.lzf.myhfuteducn.bean.Log obj) {
                                        holder.setOnClickListener(R.id.delete, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                new Thread() {
                                                    @Override
                                                    public void run() {
                                                        super.run();
                                                        Map<String, String> params = new HashMap<String, String>();
                                                        params.put("logId", obj.getLogId() + "");
                                                        final String response = OkHttpUtil.submit(UrlUtil.LOG_DELETE, params);
                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                try {
                                                                    JSONObject responseJsonObject = new JSONObject(response);
                                                                    if (responseJsonObject.getBoolean("success")) {
                                                                        logReusableAdapter.delete(obj);
                                                                    }
                                                                } catch (JSONException e) {
                                                                    Toast.makeText(MyCommunityActivity.this, response, Toast.LENGTH_SHORT).show();
                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                        });
                                                    }
                                                }.start();
                                            }
                                        });
                                        holder.setText(R.id.logPraise, "赞（" + obj.getLogPraise() + "）");
                                        holder.setOnClickListener(R.id.logPraise, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if (SharedPreferencesUtil.contains(MyCommunityActivity.this, SharedPreferencesUtil.get(MyCommunityActivity.this, "user_code", "") + "" + obj.getLogId())) {
                                                    Toast.makeText(MyCommunityActivity.this, "该日志你已经点过赞了！", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    SharedPreferencesUtil.put(MyCommunityActivity.this, SharedPreferencesUtil.get(MyCommunityActivity.this, "user_code", "") + "" + obj.getLogId(), obj.getLogTime());
                                                    new Thread() {
                                                        @Override
                                                        public void run() {
                                                            super.run();
                                                            Map<String, String> params = new HashMap<String, String>();
                                                            params.put("logId", obj.getLogId() + "");
                                                            final String response = OkHttpUtil.submit(UrlUtil.LOG_PRAISE, params);
                                                            runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    try {
                                                                        JSONObject responseJsonObject = new JSONObject(response);
                                                                        if (responseJsonObject.getBoolean("success")) {
                                                                            obj.setLogPraise(obj.getLogPraise() + 1);
                                                                            notifyDataSetChanged();
                                                                        }
                                                                    } catch (JSONException e) {
                                                                        Toast.makeText(MyCommunityActivity.this, response, Toast.LENGTH_SHORT).show();
                                                                        e.printStackTrace();
                                                                    }
                                                                }
                                                            });
                                                        }
                                                    }.start();
                                                }
                                            }
                                        });
                                        holder.setText(R.id.logUserName, ((obj.isLogIsAnonymity()) ? "匿名用户" : obj.getLogUserName()));
                                        holder.setText(R.id.logTime, "发表于 " + simpleDateFormat.format(obj.getLogTime()));
                                        holder.setImageByGlide(R.id.logImg, UrlUtil.MY_HOST + obj.getLogImg(), MyCommunityActivity.this);
                                        holder.setText(R.id.logTxt, obj.getLogTxt());
                                        holder.setOnClickListener(R.id.logTxt, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                position = holder.getItemPosition();
                                                log = obj;
                                                commentRL.setVisibility(View.VISIBLE);
                                                commentET.setHint("评论");
                                                commentET.requestFocus();
                                                InputMethodManager imm = (InputMethodManager) MyCommunityActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                                                imm.showSoftInput(commentET, 0); //弹出软键盘
                                            }
                                        });
                                        holder.setOnClickListener(R.id.commentTV, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                position = holder.getItemPosition();
                                                log = obj;
                                                commentRL.setVisibility(View.VISIBLE);
                                                commentET.setHint("评论");
                                                commentET.requestFocus();
                                                InputMethodManager imm = (InputMethodManager) MyCommunityActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                                                imm.showSoftInput(commentET, 0); //弹出软键盘
                                            }
                                        });
                                        holder.setOnClickListener(R.id.lookDetail, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(MyCommunityActivity.this, LogDetailActivity.class);
                                                intent.putExtra("log", obj);
                                                startActivity(intent);
                                            }
                                        });
                                        if (obj.getComments() != null) { //&& obj.getComments().size() > 0
                                            //                                            holder.setVisibility(R.id.line, View.VISIBLE);
                                            holder.dynamicAddComment(R.id.commentLL, obj.getComments());
                                        }
                                    }
                                };
                                logListView.setAdapter(logReusableAdapter);
                            }
                        } catch (JSONException e) {
                            Toast.makeText(MyCommunityActivity.this, response, Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                });
            }
        }.start();
    }

    /**
     * 用户单击界面上的对应视图控件时进行的响应操作
     *
     * @param view 用户单击界面上的对应视图控件
     */
    public void doClick(View view) {
        switch (view.getId()) {
            case R.id.backBtn:
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 评论发布前的前端检查：确保所发布的内容真实有效。
     *
     * @return 检查后的结果（true：所发布的内容真实有效可以提交；false：内容缺失或是不合法，需重新编辑）
     */
    private boolean commentCheck() {
        boolean valid = true;
        commentETValue = commentET.getText().toString();
        if (commentETValue == null || commentETValue.trim().equals("")) {
            valid = false;
            commentET.requestFocus(); //学号输入框获取焦点
            InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(commentET, 0); //弹出软键盘
        }
        return valid;
    }

    /**
     * 用户按了返回物理按键后的事件处理。
     */
    @Override
    public void onBackPressed() {
        if (commentRL.getVisibility() == View.VISIBLE) {
            commentRL.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }
}
