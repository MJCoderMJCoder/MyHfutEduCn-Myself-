package com.lzf.myhfuteducn.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lzf.myhfuteducn.MainActivity;
import com.lzf.myhfuteducn.R;
import com.lzf.myhfuteducn.util.OkHttpUtil;
import com.lzf.myhfuteducn.util.SharedPreferencesUtil;
import com.lzf.myhfuteducn.util.UrlUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InfosetFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InfosetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InfosetFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    //    private static final String ARG_PARAM1 = "param1";
    //    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    //    private String mParam1;
    //    private String mParam2;

    //    private OnFragmentInteractionListener mListener;

    private Context context;
    private View view;
    private EditText account_email;
    private EditText mobile_phone;
    private String accountEmail;
    private String mobilePhone;

    public InfosetFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InfosetFragment.
     */
    // TODO: Rename and change types and number of parameters
    //    public static InfosetFragment newInstance(String param1, String param2) {
    //        InfosetFragment fragment = new InfosetFragment();
    //        Bundle args = new Bundle();
    //        args.putString(ARG_PARAM1, param1);
    //        args.putString(ARG_PARAM2, param2);
    //        fragment.setArguments(args);
    //        return fragment;
    //    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //        if (getArguments() != null) {
        //            mParam1 = getArguments().getString(ARG_PARAM1);
        //            mParam2 = getArguments().getString(ARG_PARAM2);
        //        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_infoset, container, false);
        TextView user_name = view.findViewById(R.id.user_name);
        user_name.setText("姓名：" + SharedPreferencesUtil.get(context, "user_name", ""));
        TextView user_code = view.findViewById(R.id.user_code);
        user_code.setText("学号：" + SharedPreferencesUtil.get(context, "user_code", ""));
        TextView depart_name_adminclass_name = view.findViewById(R.id.depart_name_adminclass_name);
        depart_name_adminclass_name.setText(SharedPreferencesUtil.get(context, "depart_name", "") + "-" + SharedPreferencesUtil.get(context, "adminclass_name", ""));
        TextView major_name = view.findViewById(R.id.major_name);
        major_name.setText("专业：" + SharedPreferencesUtil.get(context, "major_name", ""));
        TextView projectName = view.findViewById(R.id.projectName);
        projectName.setText("学历：" + SharedPreferencesUtil.get(context, "projectName0", ""));
        TextView birthday = view.findViewById(R.id.birthday);
        birthday.setText("出生日期：" + SharedPreferencesUtil.get(context, "birthday", ""));
        TextView gender = view.findViewById(R.id.gender);
        gender.setText("性别：" + SharedPreferencesUtil.get(context, "gender", ""));
        account_email = view.findViewById(R.id.account_email);
        account_email.setText("" + SharedPreferencesUtil.get(context, "account_email", ""));
        mobile_phone = view.findViewById(R.id.mobile_phone);
        mobile_phone.setText("" + SharedPreferencesUtil.get(context, "mobile_phone", ""));
        final Button submitPhoneEmail = view.findViewById(R.id.submitPhoneEmail);
        submitPhoneEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (submitCheck()) { //开始登陆
                    submitPhoneEmail.setEnabled(false);
                    new Thread() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void run() {
                            super.run();
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("email", accountEmail);
                            map.put("phone", mobilePhone);
                            map.put("userKey", SharedPreferencesUtil.get(context, "userKey", "") + "");
                            map.put("identity", 0 + "");
                            final String response = OkHttpUtil.submit(UrlUtil.EDIT_PHONE_EMAIL, map);
                            /*
                             {
                             "code": 200,
                             "msg": "查询成功！",
                             "salt": null,
                             "token": null,
                             "obj": {
                             "business_data": {
                             "success": true,
                             "user_code": "2016210855"
                             },
                             "err_code": "00000",
                             "err_msg": ""
                             },
                             "error": null
                             }
                             */
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        JSONObject responseJson = new JSONObject(response);
                                        if (responseJson.getInt("code") == 200) {
                                            Toast.makeText(context, responseJson.getString("msg"), Toast.LENGTH_SHORT).show();
                                            JSONObject objJson = responseJson.getJSONObject("obj");
                                            if (objJson != null) {
                                                JSONObject businessDataJson = objJson.getJSONObject("business_data");
                                                if (businessDataJson != null) {
                                                    SharedPreferencesUtil.put(context, "user_code", businessDataJson.getString("user_code"));
                                                    SharedPreferencesUtil.put(context, "account_email", accountEmail);
                                                    SharedPreferencesUtil.put(context, "mobile_phone", mobilePhone);
                                                } else {
                                                    Toast.makeText(context, objJson.getString("err_msg"), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        } else {
                                            Toast.makeText(context, responseJson.getString("error"), Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                                        e.printStackTrace();
                                    }
                                    submitPhoneEmail.setEnabled(true);
                                }
                            });
                        }
                    }.start();
                }
            }
        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        //        if (mListener != null) {
        //            mListener.onFragmentInteraction(uri);
        //        }
    }

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
    private boolean submitCheck() {
        boolean valid = true;
        accountEmail = account_email.getText().toString();
        mobilePhone = mobile_phone.getText().toString();
        if (accountEmail == null || accountEmail.trim().equals("") || !isEmail(accountEmail)) {
            valid = false;
            account_email.requestFocus(); //学号输入框获取焦点
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(account_email, 0); //弹出软键盘
            Toast.makeText(context, "请输入正确的邮箱", Toast.LENGTH_SHORT).show();
        } else if (mobilePhone == null || mobilePhone.trim().equals("")) {
            valid = false;
            mobile_phone.requestFocus(); //密码输入框获取焦点
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(mobile_phone, 0); //弹出软键盘
            Toast.makeText(context, "请输入手机号", Toast.LENGTH_SHORT).show();
        }
        return valid;
    }


    /**
     * 判断格式是否为email
     */
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }
}