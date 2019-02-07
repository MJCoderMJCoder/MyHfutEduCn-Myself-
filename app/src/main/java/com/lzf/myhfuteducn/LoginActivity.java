package com.lzf.myhfuteducn;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lzf.myhfuteducn.util.OkHttpUtil;
import com.lzf.myhfuteducn.util.SharedPreferencesUtil;
import com.lzf.myhfuteducn.util.UrlUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private String username;
    private String password;

    private EditText studentIdET;
    private EditText passwordET;
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        studentIdET = findViewById(R.id.studentIdET);
        passwordET = findViewById(R.id.passwordET);
        loginBtn = findViewById(R.id.loginBtn);
        if (SharedPreferencesUtil.contains(this, "userKey")) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginBtn:
                if (loginCheck()) { //开始登陆
                    loginBtn.setEnabled(false);
                    new Thread() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void run() {
                            super.run();
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("username", username);
                            map.put("password", Base64.encodeToString(password.getBytes(), Base64.DEFAULT)); // MjUyMDE3===node.js.base64Encode
                            map.put("identity", 0 + "");
                            final String response = OkHttpUtil.submit(UrlUtil.login, map);
                            /*
                             {
                             "code": 200,
                             "msg": "查询成功！",
                             "salt": null,
                             "token": "%7BRSA%7D32C632BA3A782773153E674F86839F81BC69AAB079794A0163D9A84DAFA2CA56B937FB7AD6E05C05A489C4B3FACDDA098CAF444249E841B3375E6FDDCC2AE85FA6BD10B01760CB0CD6BFC8E6FB64167515AA4B7AD2BB0E95D080264C402FBD6D2B7FBFD97C46DB3A6A44095997A16AA30C29CF34F0A2F7B6A2163D14DB588AD3",
                             "obj": {
                             "business_data": {
                             "account_email": "zwx19980125@qq.com",
                             "adminclass_name": "工业工16-2班",
                             "ancestral_addr": "",
                             "birthday": "1998-01-25",
                             "depart_name": "机械工程学院",
                             "direction_name": "",
                             "gender": "男",
                             "major_name": "工业工程",
                             "mobile_phone": "17355305937",
                             "user_code": "2016210855",
                             "user_name": "赵文翔"
                             },
                             "err_code": "00000",
                             "err_msg": "",
                             "userKey": "Zw3tBoNZJr2Vq_4L9yKCCEGKpPCKVUI3jP6O95IIxzc="
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
                                            Toast.makeText(LoginActivity.this, responseJson.getString("msg"), Toast.LENGTH_SHORT).show();
                                            JSONObject objJson = responseJson.getJSONObject("obj");
                                            if (objJson != null) {
                                                JSONObject businessDataJson = objJson.getJSONObject("business_data");
                                                if (businessDataJson != null) {
                                                    SharedPreferencesUtil.put(LoginActivity.this, "salt", responseJson.getString("salt"));
                                                    SharedPreferencesUtil.put(LoginActivity.this, "token", responseJson.getString("token"));

                                                    SharedPreferencesUtil.put(LoginActivity.this, "account_email", businessDataJson.getString("account_email"));
                                                    SharedPreferencesUtil.put(LoginActivity.this, "adminclass_name", businessDataJson.getString("adminclass_name"));
                                                    SharedPreferencesUtil.put(LoginActivity.this, "ancestral_addr", businessDataJson.getString("ancestral_addr"));
                                                    SharedPreferencesUtil.put(LoginActivity.this, "birthday", businessDataJson.getString("birthday"));
                                                    SharedPreferencesUtil.put(LoginActivity.this, "depart_name", businessDataJson.getString("depart_name"));
                                                    SharedPreferencesUtil.put(LoginActivity.this, "direction_name", businessDataJson.getString("direction_name"));
                                                    SharedPreferencesUtil.put(LoginActivity.this, "gender", businessDataJson.getString("gender"));
                                                    SharedPreferencesUtil.put(LoginActivity.this, "major_name", businessDataJson.getString("major_name"));
                                                    SharedPreferencesUtil.put(LoginActivity.this, "mobile_phone", businessDataJson.getString("mobile_phone"));
                                                    SharedPreferencesUtil.put(LoginActivity.this, "user_code", businessDataJson.getString("user_code"));
                                                    SharedPreferencesUtil.put(LoginActivity.this, "user_name", businessDataJson.getString("user_name"));
                                                    SharedPreferencesUtil.put(LoginActivity.this, "err_code", objJson.getString("err_code"));
                                                    SharedPreferencesUtil.put(LoginActivity.this, "err_msg", objJson.getString("err_msg"));
                                                    SharedPreferencesUtil.put(LoginActivity.this, "userKey", objJson.getString("userKey"));

                                                    SharedPreferencesUtil.put(LoginActivity.this, "error", responseJson.getString("error"));

                                                    LoginActivity.this.startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                                    LoginActivity.this.finish();
                                                }
                                            }
                                        } else {
                                            Toast.makeText(LoginActivity.this, responseJson.getString("error"), Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        Toast.makeText(LoginActivity.this, response, Toast.LENGTH_SHORT).show();
                                        e.printStackTrace();
                                    }
                                    loginBtn.setEnabled(true);
                                }
                            });
                        }
                    }.start();
                }
                break;
            default:
                break;
        }
    }


    private boolean loginCheck() {
        boolean valid = true;
        username = studentIdET.getText().toString();
        password = passwordET.getText().toString();
        if (username == null || username.trim().equals("")) {
            valid = false;
            studentIdET.requestFocus(); //学号输入框获取焦点
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(studentIdET, 0); //弹出软键盘
        } else if (password == null || password.trim().equals("")) {
            valid = false;
            passwordET.requestFocus(); //密码输入框获取焦点
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(passwordET, 0); //弹出软键盘
        }
        return valid;
    }
}
