package com.lzf.myhfuteducn.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.lzf.myhfuteducn.LogActivity;
import com.lzf.myhfuteducn.LogDetailActivity;
import com.lzf.myhfuteducn.R;
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
 * 社区界面的UI控制层
 *
 * @author MJCoder
 * @see android.support.v4.app.Fragment
 */
public class CommunityFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    //    private static final String ARG_PARAM1 = "param1";
    //    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    //    private String mParam1;
    //    private String mParam2;

    //    private OnFragmentInteractionListener mListener;

    /**
     * 该Fragment的宿主Activity
     */
    private FragmentActivity fragmentActivity;
    /**
     * 环境/上下文
     */
    private Context context;
    /**
     * 社区界面顶部的搜索框
     */
    private EditText searchET;
    /**
     * 评论控件
     */
    private RelativeLayout commentRL;
    /**
     * 评论控件中的编写框
     */
    private EditText commentET;
    /**
     * 评论的内容文本
     */
    private String commentETValue;
    /**
     * 是否匿名发表的选项框
     */
    private CheckBox checkBox;
    /**
     * 发送按钮
     */
    private Button commentSend;
    /**
     * 用于显示一系列日志的列表视图
     */
    private ListView logListView;
    /**
     * logListView的适配器，用于处理一系列日志的列表视图的具体显示内容
     */
    private ReusableAdapter<com.lzf.myhfuteducn.bean.Log> logReusableAdapter;
    /**
     * 从阿里云服务端返回的日志数据列表
     */
    private List<com.lzf.myhfuteducn.bean.Log> logData;
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
     * 社区界面的UI控制层的无参构造方法
     */
    public CommunityFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CommunityFragment.
     */
    // TODO: Rename and change types and number of parameters
    //    public static CommunityFragment newInstance(String param1, String param2) {
    //        CommunityFragment fragment = new CommunityFragment();
    //        Bundle args = new Bundle();
    //        args.putString(ARG_PARAM1, param1);
    //        args.putString(ARG_PARAM2, param2);
    //        fragment.setArguments(args);
    //        return fragment;
    //    }

    /**
     * 创建Fragment是回调，只会回调一次。
     *
     * @param savedInstanceState
     * @see Bundle
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //        if (getArguments() != null) {
        //            mParam1 = getArguments().getString(ARG_PARAM1);
        //            mParam2 = getArguments().getString(ARG_PARAM2);
        //        }
    }

    /**
     * 每次创建、绘制该Fragment的View组件时回调，会将显示的View返回
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return 返回社区界面的UI视图
     * @see LayoutInflater
     * @see ViewGroup
     * @see Bundle
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_community, container, false);
        fragmentActivity = getActivity();
        view.findViewById(R.id.publishFAB).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, LogActivity.class));
            }
        });
        searchET = view.findViewById(R.id.searchET);
        searchET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //关闭软键盘
                    InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    manager.hideSoftInputFromWindow(searchET.getWindowToken(), 0);
                    new Thread() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void run() {
                            super.run();
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("logTxt", searchET.getText().toString());
                            params.put("logUserName", searchET.getText().toString());
                            params.put("logUserPhone", searchET.getText().toString());
                            params.put("logUserEmail", searchET.getText().toString());
                            params.put("logUserGender", searchET.getText().toString());
                            params.put("logUserBirthday", searchET.getText().toString());
                            params.put("logUserDepart", searchET.getText().toString());
                            params.put("logUserMajor", searchET.getText().toString());
                            params.put("logUserClass", searchET.getText().toString());
                            final String response = OkHttpUtil.submit(UrlUtil.LOG_DIMSELECT, params);
                            if (fragmentActivity == null) {
                                fragmentActivity = getActivity();
                            }
                            fragmentActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        JSONObject responseJsonObject = new JSONObject(response);
                                        if (responseJsonObject.getBoolean("success")) {
                                            List<com.lzf.myhfuteducn.bean.Log> logData = new Gson().fromJson(responseJsonObject.getJSONArray("data").toString(), new TypeToken<List<Log>>() {
                                            }.getType());
                                            logReusableAdapter.updateAll(logData);
                                        } else {
                                            Toast.makeText(context, responseJsonObject.getString("describe"), Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
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
        commentRL = view.findViewById(R.id.commentRL);
        commentET = view.findViewById(R.id.commentET);
        checkBox = view.findViewById(R.id.checkBox);
        commentSend = view.findViewById(R.id.commentSend);
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
                            params.put("commentUserName", SharedPreferencesUtil.get(context, "user_name", "") + "");
                            params.put("commentUserPhone", SharedPreferencesUtil.get(context, "mobile_phone", "") + "");
                            params.put("commentUserEmail", SharedPreferencesUtil.get(context, "account_email", "") + "");
                            params.put("commentUserGender", SharedPreferencesUtil.get(context, "gender", "") + "");
                            params.put("commentUserBirthday", SharedPreferencesUtil.get(context, "birthday", "") + "");
                            params.put("commentUserDepart", SharedPreferencesUtil.get(context, "depart_name", "") + "");
                            params.put("commentUserMajor", SharedPreferencesUtil.get(context, "major_name", "") + "");
                            params.put("commentUserClass", SharedPreferencesUtil.get(context, "adminclass_name", "") + "");
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
                            if (fragmentActivity == null) {
                                fragmentActivity = getActivity();
                            }
                            fragmentActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        JSONObject responseJsonObject = new JSONObject(response);
                                        if (responseJsonObject.getBoolean("success")) {
                                            //关闭软键盘
                                            InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                                            manager.hideSoftInputFromWindow(commentET.getWindowToken(), 0);
                                            commentRL.setVisibility(View.GONE);
                                            log.getComments().add(new Comment(0, System.currentTimeMillis(), commentETValue, log.getLogId(),
                                                    SharedPreferencesUtil.get(context, "user_name", "") + "",
                                                    SharedPreferencesUtil.get(context, "mobile_phone", "") + "",
                                                    SharedPreferencesUtil.get(context, "account_email", "") + "",
                                                    SharedPreferencesUtil.get(context, "gender", "") + "",
                                                    SharedPreferencesUtil.get(context, "birthday", "") + "",
                                                    SharedPreferencesUtil.get(context, "depart_name", "") + "",
                                                    SharedPreferencesUtil.get(context, "major_name", "") + "",
                                                    SharedPreferencesUtil.get(context, "adminclass_name", "") + "",
                                                    checkBox.isChecked(), "", "", "", "", "", "", "", ""));
                                            logReusableAdapter.update(position, log);
                                        } else {
                                            Toast.makeText(context, responseJsonObject.getString("describe"), Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    }.start();
                }
            }
        });
        logListView = view.findViewById(R.id.logListView);
        return view;
    }

    /**
     * 恢复Fragment时回调；确保每次进入该界面都进行刷新最新的数据信息
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
                params.put("logUserName", searchET.getText().toString());
                params.put("logUserPhone", searchET.getText().toString());
                params.put("logUserEmail", searchET.getText().toString());
                params.put("logUserGender", searchET.getText().toString());
                params.put("logUserBirthday", searchET.getText().toString());
                params.put("logUserDepart", searchET.getText().toString());
                params.put("logUserMajor", searchET.getText().toString());
                params.put("logUserClass", searchET.getText().toString());
                final String response = OkHttpUtil.submit(UrlUtil.LOG_DIMSELECT, params);
                if (fragmentActivity == null) {
                    fragmentActivity = getActivity();
                }
                fragmentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject responseJsonObject = new JSONObject(response);
                            if (responseJsonObject.getBoolean("success")) {
                                if (logData != null) {
                                    logData.clear();
                                }
                                logData = new Gson().fromJson(responseJsonObject.getJSONArray("data").toString(), new TypeToken<List<Log>>() {
                                }.getType());
                                logReusableAdapter = new ReusableAdapter<com.lzf.myhfuteducn.bean.Log>(logData, R.layout.item_log) {
                                    @Override
                                    public void bindView(final ViewHolder holder, final com.lzf.myhfuteducn.bean.Log obj) {
                                        holder.setText(R.id.logPraise, "赞（" + obj.getLogPraise() + "）");
                                        holder.setText(R.id.logPraiseLink, "赞（" + obj.getLogPraise() + "）");
                                        holder.setOnClickListener(R.id.logPraise, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if (SharedPreferencesUtil.contains(context, SharedPreferencesUtil.get(context, "user_code", "") + "" + obj.getLogId())) {
                                                    Toast.makeText(context, "该日志你已经点过赞了！", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    SharedPreferencesUtil.put(context, SharedPreferencesUtil.get(context, "user_code", "") + "" + obj.getLogId(), obj.getLogTime());
                                                    new Thread() {
                                                        @Override
                                                        public void run() {
                                                            super.run();
                                                            Map<String, String> params = new HashMap<String, String>();
                                                            params.put("logId", obj.getLogId() + "");
                                                            final String response = OkHttpUtil.submit(UrlUtil.LOG_PRAISE, params);
                                                            if (fragmentActivity == null) {
                                                                fragmentActivity = getActivity();
                                                            }
                                                            fragmentActivity.runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    try {
                                                                        JSONObject responseJsonObject = new JSONObject(response);
                                                                        if (responseJsonObject.getBoolean("success")) {
                                                                            obj.setLogPraise(obj.getLogPraise() + 1);
                                                                            notifyDataSetChanged();
                                                                        }
                                                                    } catch (JSONException e) {
                                                                        Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                                                                        e.printStackTrace();
                                                                    }
                                                                }
                                                            });
                                                        }
                                                    }.start();
                                                }
                                            }
                                        });
                                        holder.setOnClickListener(R.id.logPraiseLink, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if (SharedPreferencesUtil.contains(context, SharedPreferencesUtil.get(context, "user_code", "") + "" + obj.getLogId())) {
                                                    Toast.makeText(context, "该日志你已经点过赞了！", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    SharedPreferencesUtil.put(context, SharedPreferencesUtil.get(context, "user_code", "") + "" + obj.getLogId(), obj.getLogTime());
                                                    new Thread() {
                                                        @Override
                                                        public void run() {
                                                            super.run();
                                                            Map<String, String> params = new HashMap<String, String>();
                                                            params.put("logId", obj.getLogId() + "");
                                                            final String response = OkHttpUtil.submit(UrlUtil.LOG_PRAISE, params);
                                                            if (fragmentActivity == null) {
                                                                fragmentActivity = getActivity();
                                                            }
                                                            fragmentActivity.runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    try {
                                                                        JSONObject responseJsonObject = new JSONObject(response);
                                                                        if (responseJsonObject.getBoolean("success")) {
                                                                            obj.setLogPraise(obj.getLogPraise() + 1);
                                                                            notifyDataSetChanged();
                                                                        }
                                                                    } catch (JSONException e) {
                                                                        Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
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
                                        holder.setImageByGlide(R.id.logImg, UrlUtil.MY_HOST + obj.getLogImg(), context);
                                        holder.setText(R.id.logTxt, obj.getLogTxt());
                                        holder.setOnClickListener(R.id.logTxt, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                position = holder.getItemPosition();
                                                log = obj;
                                                commentRL.setVisibility(View.VISIBLE);
                                                commentET.setHint("评论");
                                                commentET.requestFocus();
                                                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
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
                                                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                                                imm.showSoftInput(commentET, 0); //弹出软键盘
                                            }
                                        });
                                        holder.setOnClickListener(R.id.lookDetail, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(context, LogDetailActivity.class);
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
                            Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                });
            }
        }.start();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        android.util.Log.v("onButtonPressed", "" + uri);
        //        if (mListener != null) {
        //            mListener.onFragmentInteraction(uri);
        //        }
    }

    /**
     * 当该Fragment被添加到Activity中会回调，只会被调用一次
     *
     * @param context 环境/上下文
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        //        if (context instanceof OnFragmentInteractionListener) {
        //            mListener = (OnFragmentInteractionListener) context;
        //        } else {
        //            throw new RuntimeException(context.toString()
        //                    + " must implement OnFragmentInteractionListener");
        //        }
    }

    /**
     * 将该Fragment从Activity中删除/替换后回调该方法（onDestroy()方法后一定回调该方法）；且该方法只会调用一次。
     */
    @Override
    public void onDetach() {
        super.onDetach();
        //        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    //    public interface OnFragmentInteractionListener {
    //        // TODO: Update argument type and name
    //        void onFragmentInteraction(Uri uri);
    //    }

    /**
     * 评论发布前的前端检查：确保所发布的内容真实有效。
     *
     * @return 检查后的结果（true：该内容真实有效可以提交；false：内容缺失或是不合法，需重新编辑）
     */
    private boolean commentCheck() {
        boolean valid = true;
        commentETValue = commentET.getText().toString();
        if (commentETValue == null || commentETValue.trim().equals("")) {
            valid = false;
            commentET.requestFocus(); //学号输入框获取焦点
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(commentET, 0); //弹出软键盘
        }
        return valid;
    }

    /**
     * 用户按了返回物理按键后的事件处理。
     *
     * @return 是否自定义处理（true：返回事件进行自定义处理，执行自定义的代码；false：返回事件交由系统默认进行响应）
     */
    public boolean onBackPressed() {
        if (commentRL != null && commentRL.getVisibility() == View.VISIBLE) {
            commentRL.setVisibility(View.GONE);
            return true;
        }
        return false;
    }
}
