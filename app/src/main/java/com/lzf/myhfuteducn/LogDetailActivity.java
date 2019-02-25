package com.lzf.myhfuteducn;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lzf.myhfuteducn.bean.Comment;
import com.lzf.myhfuteducn.bean.Log;
import com.lzf.myhfuteducn.util.OkHttpUtil;
import com.lzf.myhfuteducn.util.ScreenUtils;
import com.lzf.myhfuteducn.util.SharedPreferencesUtil;
import com.lzf.myhfuteducn.util.UrlUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LogDetailActivity extends AppCompatActivity {

    private EditText commentET;
    private String commentETValue;
    private CheckBox checkBox;
    private Button commentSend;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM--dd HH:mm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        if (intent != null) {
            final com.lzf.myhfuteducn.bean.Log log = (com.lzf.myhfuteducn.bean.Log) intent.getSerializableExtra("log");
            if (log != null) {
                Glide.with(this)
                        .load(UrlUtil.MY_HOST + log.getLogImg())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)//图片缓存策略,这个一般必须有
                        .error(R.mipmap.ic_launcher)// 加载图片失败的时候显示的默认图
                        .dontAnimate().placeholder(R.mipmap.atlantis) //解决Glide 图片闪烁问题
                        .into((ImageView) findViewById(R.id.imageView));
                final TextView logPraise = findViewById(R.id.logPraise);
                logPraise.setText("赞（" + log.getLogPraise() + "）");
                logPraise.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (SharedPreferencesUtil.contains(LogDetailActivity.this, SharedPreferencesUtil.get(LogDetailActivity.this, "user_code", "") + "" + log.getLogId())) {
                            Toast.makeText(LogDetailActivity.this, "该日志你已经点过赞了！", Toast.LENGTH_SHORT).show();
                        } else {
                            SharedPreferencesUtil.put(LogDetailActivity.this, SharedPreferencesUtil.get(LogDetailActivity.this, "user_code", "") + "" + log.getLogId(), log.getLogTime());
                            new Thread() {
                                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                @Override
                                public void run() {
                                    super.run();
                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put("logId", log.getLogId() + "");
                                    final String response = OkHttpUtil.submit(UrlUtil.LOG_PRAISE, params);
                                    LogDetailActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                JSONObject responseJsonObject = new JSONObject(response);
                                                if (responseJsonObject.getBoolean("success")) {
                                                    logPraise.setText("赞（" + (log.getLogPraise() + 1) + "）");
                                                }
                                            } catch (JSONException e) {
                                                Toast.makeText(LogDetailActivity.this, response, Toast.LENGTH_SHORT).show();
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                }
                            }.start();
                        }
                    }
                });
                TextView logUserName = findViewById(R.id.logUserName);
                logUserName.setText(log.isLogIsAnonymity() ? ("匿名用户（" + log.getLogUserGender() + "）") : (log.getLogUserName() + "（" + log.getLogUserGender() + "）"));
                if (!log.isLogIsAnonymity()) {
                    infoDetailDialog(logUserName, log);
                }
                ((TextView) findViewById(R.id.logTime)).setText("发表于 " + simpleDateFormat.format(log.getLogTime()));
                ((TextView) findViewById(R.id.logTxt)).setText(log.getLogTxt());
                dynamicAddComment(((LinearLayout) findViewById(R.id.commentLL)), log.getComments());
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
                                    params.put("commentUserName", SharedPreferencesUtil.get(LogDetailActivity.this, "user_name", "") + "");
                                    params.put("commentUserPhone", SharedPreferencesUtil.get(LogDetailActivity.this, "mobile_phone", "") + "");
                                    params.put("commentUserEmail", SharedPreferencesUtil.get(LogDetailActivity.this, "account_email", "") + "");
                                    params.put("commentUserGender", SharedPreferencesUtil.get(LogDetailActivity.this, "gender", "") + "");
                                    params.put("commentUserBirthday", SharedPreferencesUtil.get(LogDetailActivity.this, "birthday", "") + "");
                                    params.put("commentUserDepart", SharedPreferencesUtil.get(LogDetailActivity.this, "depart_name", "") + "");
                                    params.put("commentUserMajor", SharedPreferencesUtil.get(LogDetailActivity.this, "major_name", "") + "");
                                    params.put("commentUserClass", SharedPreferencesUtil.get(LogDetailActivity.this, "adminclass_name", "") + "");
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
                                    LogDetailActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                JSONObject responseJsonObject = new JSONObject(response);
                                                if (responseJsonObject.getBoolean("success")) {
                                                    //关闭软键盘
                                                    InputMethodManager manager = (InputMethodManager) LogDetailActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                                                    manager.hideSoftInputFromWindow(commentET.getWindowToken(), 0);
                                                    log.getComments().add(new Comment(0, System.currentTimeMillis(), commentETValue, log.getLogId(),
                                                            SharedPreferencesUtil.get(LogDetailActivity.this, "user_name", "") + "",
                                                            SharedPreferencesUtil.get(LogDetailActivity.this, "mobile_phone", "") + "",
                                                            SharedPreferencesUtil.get(LogDetailActivity.this, "account_email", "") + "",
                                                            SharedPreferencesUtil.get(LogDetailActivity.this, "gender", "") + "",
                                                            SharedPreferencesUtil.get(LogDetailActivity.this, "birthday", "") + "",
                                                            SharedPreferencesUtil.get(LogDetailActivity.this, "depart_name", "") + "",
                                                            SharedPreferencesUtil.get(LogDetailActivity.this, "major_name", "") + "",
                                                            SharedPreferencesUtil.get(LogDetailActivity.this, "adminclass_name", "") + "",
                                                            checkBox.isChecked(), "", "", "", "", "", "", "", ""));
                                                    dynamicAddComment(((LinearLayout) findViewById(R.id.commentLL)), log.getComments());
                                                } else {
                                                    Toast.makeText(LogDetailActivity.this, responseJsonObject.getString("describe"), Toast.LENGTH_SHORT).show();
                                                }
                                            } catch (JSONException e) {
                                                Toast.makeText(LogDetailActivity.this, response, Toast.LENGTH_SHORT).show();
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                }
                            }.start();
                        }
                    }
                });
            }
        }
    }

    public void doClick(View view) {
        switch (view.getId()) {
            case R.id.backBtn:
                finish();
                break;
            default:
                break;
        }
    }


    private void dynamicAddComment(LinearLayout commentLL, List<Comment> commentList) {
        try {
            commentLL.removeAllViews();
            for (Comment comment : commentList) {
                LinearLayout verticalLinearLayout = new LinearLayout(this);
                verticalLinearLayout.setOrientation(LinearLayout.VERTICAL);
                verticalLinearLayout.setPadding(ScreenUtils.dp2px(this, 20), ScreenUtils.dp2px(this, 10), ScreenUtils.dp2px(this, 20), ScreenUtils.dp2px(this, 10));
                LinearLayout horizontalLinearLayout = new LinearLayout(this);
                horizontalLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
                horizontalLinearLayout.setWeightSum(2.0f);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.weight = 1;
                TextView commentUserName = new TextView(this);
                commentUserName.setText(comment.isCommentIsAnonymity() ? ("匿名用户（" + comment.getCommentUserGender() + "）") : (comment.getCommentUserName() + "（" + comment.getCommentUserGender() + "）"));
                commentUserName.setTextColor(Color.parseColor("#3F51B5"));
                commentUserName.setLayoutParams(layoutParams);
                if (!comment.isCommentIsAnonymity()) {
                    infoDetailDialog(commentUserName, comment);
                }
                horizontalLinearLayout.addView(commentUserName);
                TextView commentTime = new TextView(this);
                commentTime.setText(simpleDateFormat.format(comment.getCommentTime()));
                commentTime.setGravity(Gravity.RIGHT);
                commentTime.setLayoutParams(layoutParams);
                horizontalLinearLayout.addView(commentTime);
                verticalLinearLayout.addView(horizontalLinearLayout);
                TextView commentTxt = new TextView(this);
                commentTxt.setText("\t" + comment.getCommentTxt());
                verticalLinearLayout.addView(commentTxt);
                commentLL.addView(verticalLinearLayout);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 日志发布前的前端检测
     *
     * @return
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

    private void infoDetailDialog(TextView trigger, Object object) {
        //初始化Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //加载自定义的那个View,同时设置下
        final LayoutInflater inflater = this.getLayoutInflater();
        View info_detail_dialog = inflater.inflate(R.layout.info_detail_dialog, null, false);
        builder.setView(info_detail_dialog);
        builder.setCancelable(true);
        final AlertDialog alert = builder.create();
        if (object instanceof Log) {
            Log log = (Log) object;
            ((TextView) info_detail_dialog.findViewById(R.id.user_name)).setText("姓名：" + log.getLogUserName());
            ((TextView) info_detail_dialog.findViewById(R.id.birthday)).setText("出生日期：" + log.getLogUserBirthday());
            ((TextView) info_detail_dialog.findViewById(R.id.depart_name_adminclass_name)).setText(log.getLogUserDepart() + "-" + log.getLogUserClass());
            ((TextView) info_detail_dialog.findViewById(R.id.major_name)).setText("专业：" + log.getLogUserMajor());
            ((TextView) info_detail_dialog.findViewById(R.id.gender)).setText("性别：" + log.getLogUserGender());
            ((TextView) info_detail_dialog.findViewById(R.id.account_email)).setText("邮箱：" + log.getLogUserEmail());
            ((TextView) info_detail_dialog.findViewById(R.id.mobile_phone)).setText("手机：" + log.getLogUserPhone());
        } else if (object instanceof Comment) {
            Comment comment = (Comment) object;
            ((TextView) info_detail_dialog.findViewById(R.id.user_name)).setText("姓名：" + comment.getCommentUserName());
            ((TextView) info_detail_dialog.findViewById(R.id.birthday)).setText("出生日期：" + comment.getCommentUserBirthday());
            ((TextView) info_detail_dialog.findViewById(R.id.depart_name_adminclass_name)).setText(comment.getCommentUserDepart() + "-" + comment.getCommentUserClass());
            ((TextView) info_detail_dialog.findViewById(R.id.major_name)).setText("专业：" + comment.getCommentUserMajor());
            ((TextView) info_detail_dialog.findViewById(R.id.gender)).setText("性别：" + comment.getCommentUserGender());
            ((TextView) info_detail_dialog.findViewById(R.id.account_email)).setText("邮箱：" + comment.getCommentUserEmail());
            ((TextView) info_detail_dialog.findViewById(R.id.mobile_phone)).setText("手机：" + comment.getCommentUserPhone());
        }
        info_detail_dialog.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
        trigger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.show();
            }
        });
    }
}
