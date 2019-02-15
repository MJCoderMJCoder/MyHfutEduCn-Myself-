package com.lzf.myhfuteducn.fragment;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.lzf.myhfuteducn.MainActivity;
import com.lzf.myhfuteducn.R;
import com.lzf.myhfuteducn.util.OkHttpUtil;
import com.lzf.myhfuteducn.util.SharedPreferencesUtil;
import com.lzf.myhfuteducn.util.UrlUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SignFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SignFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    //    private static final String ARG_PARAM1 = "param1";
    //    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    //    private String mParam1;
    //    private String mParam2;
    //
    //    private OnFragmentInteractionListener mListener;
    private View view;
    private Button sign;
    private Context context;
    private JSONObject course;
    private SimpleDateFormat sdfYyyyMMdd = new SimpleDateFormat("yyyyMMdd");
    private SimpleDateFormat sdfHHmm = new SimpleDateFormat("HHmm");

    public SignFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignFragment.
     */
    // TODO: Rename and change types and number of parameters
    //    public static SignFragment newInstance(String param1, String param2) {
    //        SignFragment fragment = new SignFragment();
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the item_week_layout for this fragment
        try {
            view = inflater.inflate(R.layout.fragment_sign, container, false);
            sign = view.findViewById(R.id.sign);
            int date = Integer.parseInt(sdfYyyyMMdd.format(System.currentTimeMillis()));
            if (MainActivity.semesterWeekList != null) {
                JSONArray semesters = MainActivity.semesterWeekList.getJSONArray("semesters");
                if (semesters != null) {
                    for (int j = 0; j < semesters.length(); j++) {
                        JSONObject semester = semesters.getJSONObject(j);
                        if (semester != null) {
                            JSONArray weeks = semester.getJSONArray("weeks");
                            if (weeks != null) {
                                for (int i = 0; i < weeks.length(); i++) {
                                    JSONObject week = weeks.getJSONObject(i);
                                    if (date < Integer.parseInt(week.getString("begin_on").replace("-", ""))) {
                                        break;
                                    } else if (date <= Integer.parseInt(week.getString("end_on").replace("-", ""))) {
                                        getWeekSchedule(semester.getString("code"), week.getString("index"));
                                        return view;
                                    }
                                }
                            }
                        }
                    }
                    sign.setEnabled(false);
                    sign.getBackground().setTint(Color.GRAY);
                    sign.setText("暂无课堂\n\n无需签到");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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


    /**
     * 获取一周的课程表并绘制
     *
     * @param semestercode
     * @param weekIndx
     */
    private void getWeekSchedule(final String semestercode, final String weekIndx) {
        new Thread() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {
                super.run();
                Map<String, String> map = new HashMap<String, String>();
                map.put("userKey", SharedPreferencesUtil.get(context, "userKey", "") + "");
                map.put("projectId", SharedPreferencesUtil.get(context, "projectId0", "") + "");
                map.put("identity", 0 + "");
                map.put("semestercode", (semestercode.length() < 3 ? (0 + semestercode) : semestercode));
                map.put("weekIndx", weekIndx);
                final String response = OkHttpUtil.submit(UrlUtil.GET_WEEK_SCHEDULE, map);
                /*
                 {
                 "code": 200,
                 "msg": "查询成功！",
                 "salt": null,
                 "token": null,
                 "obj": {
                 "business_data": [
                 {
                 "activity_id": "38703",
                 "activity_week": "1,2,3,6,7,8,9,10",
                 "activity_weekstate": "1-3周,6-10周",
                 "course_name": "质量管理与可靠性",
                 "end_time": "09:50",
                 "end_unit": 2,
                 "is_conflict": false,
                 "lesson_code": "0261022B--001",
                 "rooms": [
                 {
                 "campus_name": "屯溪路校区",
                 "code": "00000405",
                 "floor_name": "主楼",
                 "name": "主楼415*"
                 }
                 ],
                 "start_time": "08:00",
                 "start_unit": 1,
                 "teachclass_stdcount": 89,
                 "teachers": [
                 {
                 "code": "1999800029",
                 "name": "沈维蕾"
                 }
                 ],
                 "weekday": 1
                 },
                 {
                 "activity_id": "38683",
                 "activity_week": "1,2,3,6",
                 "activity_weekstate": "1-3周,6周",
                 "course_name": "智能工业设备",
                 "end_time": "16:20",
                 "end_unit": 6,
                 "is_conflict": false,
                 "lesson_code": "0262370X--001",
                 "rooms": [
                 {
                 "campus_name": "屯溪路校区",
                 "code": "00000405",
                 "floor_name": "主楼",
                 "name": "主楼415*"
                 }
                 ],
                 "start_time": "14:30",
                 "start_unit": 5,
                 "teachclass_stdcount": 95,
                 "teachers": [
                 {
                 "code": "2006800044",
                 "name": "葛茂根"
                 }
                 ],
                 "weekday": 1
                 },
                 {
                 "activity_id": "38707",
                 "activity_week": "1,2,3,6,7,8,9,10,12",
                 "activity_weekstate": "1-3周,6-10周,12周",
                 "course_name": "设施规划与物流分析",
                 "end_time": "09:50",
                 "end_unit": 2,
                 "is_conflict": false,
                 "lesson_code": "0262122B--001",
                 "rooms": [
                 {
                 "campus_name": "屯溪路校区",
                 "code": "00000405",
                 "floor_name": "主楼",
                 "name": "主楼415*"
                 }
                 ],
                 "start_time": "08:00",
                 "start_unit": 1,
                 "teachclass_stdcount": 89,
                 "teachers": [
                 {
                 "code": "2004800085",
                 "name": "周蓉"
                 }
                 ],
                 "weekday": 2
                 },
                 {
                 "activity_id": "38705",
                 "activity_week": "1,2,3,6,7,8,9,10,12",
                 "activity_weekstate": "1-3周,6-10周,12周",
                 "course_name": "面向对象的可视化编程",
                 "end_time": "12:00",
                 "end_unit": 4,
                 "is_conflict": false,
                 "lesson_code": "0261102B--001",
                 "rooms": [
                 {
                 "campus_name": "屯溪路校区",
                 "code": "00000405",
                 "floor_name": "主楼",
                 "name": "主楼415*"
                 }
                 ],
                 "start_time": "10:10",
                 "start_unit": 3,
                 "teachclass_stdcount": 91,
                 "teachers": [
                 {
                 "code": "2004800078",
                 "name": "王跃飞"
                 }
                 ],
                 "weekday": 2
                 },
                 {
                 "activity_id": "45211",
                 "activity_week": "1,2,3,6,7,8,9,10",
                 "activity_weekstate": "1-3周,6-10周",
                 "course_name": "制造过程智能监测与控制",
                 "end_time": "16:20",
                 "end_unit": 6,
                 "is_conflict": false,
                 "lesson_code": "0260310X--001",
                 "rooms": [
                 {
                 "campus_name": "屯溪路校区",
                 "code": "00000405",
                 "floor_name": "主楼",
                 "name": "主楼415*"
                 }
                 ],
                 "start_time": "14:30",
                 "start_unit": 5,
                 "teachclass_stdcount": 86,
                 "teachers": [
                 {
                 "code": "2006800044",
                 "name": "葛茂根"
                 }
                 ],
                 "weekday": 2
                 },
                 {
                 "activity_id": "38703",
                 "activity_week": "1,2,3,6,7,8,9,10",
                 "activity_weekstate": "1-3周,6-10周",
                 "course_name": "质量管理与可靠性",
                 "end_time": "09:50",
                 "end_unit": 2,
                 "is_conflict": false,
                 "lesson_code": "0261022B--001",
                 "rooms": [
                 {
                 "campus_name": "屯溪路校区",
                 "code": "00000405",
                 "floor_name": "主楼",
                 "name": "主楼415*"
                 }
                 ],
                 "start_time": "08:00",
                 "start_unit": 1,
                 "teachclass_stdcount": 89,
                 "teachers": [
                 {
                 "code": "1999800029",
                 "name": "沈维蕾"
                 }
                 ],
                 "weekday": 3
                 },
                 {
                 "activity_id": "38707",
                 "activity_week": "1,2,3,6,7,8,9,10,12",
                 "activity_weekstate": "1-3周,6-10周,12周",
                 "course_name": "设施规划与物流分析",
                 "end_time": "09:50",
                 "end_unit": 2,
                 "is_conflict": false,
                 "lesson_code": "0262122B--001",
                 "rooms": [
                 {
                 "campus_name": "屯溪路校区",
                 "code": "00000405",
                 "floor_name": "主楼",
                 "name": "主楼415*"
                 }
                 ],
                 "start_time": "08:00",
                 "start_unit": 1,
                 "teachclass_stdcount": 89,
                 "teachers": [
                 {
                 "code": "2004800085",
                 "name": "周蓉"
                 }
                 ],
                 "weekday": 4
                 },
                 {
                 "activity_id": "38705",
                 "activity_week": "1,2,3,6,7,8,9,10,12",
                 "activity_weekstate": "1-3周,6-10周,12周",
                 "course_name": "面向对象的可视化编程",
                 "end_time": "12:00",
                 "end_unit": 4,
                 "is_conflict": false,
                 "lesson_code": "0261102B--001",
                 "rooms": [
                 {
                 "campus_name": "屯溪路校区",
                 "code": "00000405",
                 "floor_name": "主楼",
                 "name": "主楼415*"
                 }
                 ],
                 "start_time": "10:10",
                 "start_unit": 3,
                 "teachclass_stdcount": 91,
                 "teachers": [
                 {
                 "code": "2004800078",
                 "name": "王跃飞"
                 }
                 ],
                 "weekday": 4
                 },
                 {
                 "activity_id": "38683",
                 "activity_week": "1,2,3,6",
                 "activity_weekstate": "1-3周,6周",
                 "course_name": "智能工业设备",
                 "end_time": "16:20",
                 "end_unit": 6,
                 "is_conflict": false,
                 "lesson_code": "0262370X--001",
                 "rooms": [
                 {
                 "campus_name": "屯溪路校区",
                 "code": "00000405",
                 "floor_name": "主楼",
                 "name": "主楼415*"
                 }
                 ],
                 "start_time": "14:30",
                 "start_unit": 5,
                 "teachclass_stdcount": 95,
                 "teachers": [
                 {
                 "code": "2006800044",
                 "name": "葛茂根"
                 }
                 ],
                 "weekday": 4
                 },
                 {
                 "activity_id": "45211",
                 "activity_week": "1,2,3,6,7,8,9,10",
                 "activity_weekstate": "1-3周,6-10周",
                 "course_name": "制造过程智能监测与控制",
                 "end_time": "12:00",
                 "end_unit": 4,
                 "is_conflict": false,
                 "lesson_code": "0260310X--001",
                 "rooms": [
                 {
                 "campus_name": "屯溪路校区",
                 "code": "00000405",
                 "floor_name": "主楼",
                 "name": "主楼415*"
                 }
                 ],
                 "start_time": "10:10",
                 "start_unit": 3,
                 "teachclass_stdcount": 86,
                 "teachers": [
                 {
                 "code": "2006800044",
                 "name": "葛茂根"
                 }
                 ],
                 "weekday": 5
                 }
                 ],
                 "err_code": "00000",
                 "err_msg": ""
                 },
                 "error": null
                 }
                 */
                // 获得星期几（注意（这个与Date类是不同的）：1代表星期日、2代表星期1、3代表星期二，以此类推）
                final int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
                getActivity().runOnUiThread(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void run() {
                        try {
                            JSONObject responseJson = new JSONObject(response);
                            if (responseJson.getInt("code") == 200) {
                                Toast.makeText(context, responseJson.getString("msg"), Toast.LENGTH_SHORT).show();
                                JSONObject objJson = responseJson.getJSONObject("obj");
                                if (objJson != null) {
                                    JSONArray businessDataJson = objJson.getJSONArray("business_data");
                                    if (businessDataJson != null) {
                                        for (int i = 0; i < businessDataJson.length(); i++) {
                                            course = businessDataJson.getJSONObject(i);
                                            int weekday = course.getInt("weekday");
                                            if (weekday == 7 && dayOfWeek == 1) { //星期日
                                                signDraw(semestercode, weekIndx, weekday);
                                                return;
                                            } else if (weekday == 1 && dayOfWeek == 2) { //星期一
                                                signDraw(semestercode, weekIndx, weekday);
                                                return;
                                            } else if (weekday == 2 && dayOfWeek == 3) { //星期二
                                                signDraw(semestercode, weekIndx, weekday);
                                                return;
                                            } else if (weekday == 3 && dayOfWeek == 4) { //星期三
                                                signDraw(semestercode, weekIndx, weekday);
                                                return;
                                            } else if (weekday == 4 && dayOfWeek == 5) { //星期四
                                                signDraw(semestercode, weekIndx, weekday);
                                                return;
                                            } else if (weekday == 5 && dayOfWeek == 6) { //星期五
                                                signDraw(semestercode, weekIndx, weekday);
                                                return;
                                            } else if (weekday == 6 && dayOfWeek == 7) { //星期六
                                                signDraw(semestercode, weekIndx, weekday);
                                                return;
                                            }
                                        }
                                        sign.setEnabled(false);
                                        sign.getBackground().setTint(Color.GRAY);
                                        sign.setText("暂无课堂\n\n无需签到");
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }.start();
    }

    /**
     * 签到按钮绘制
     *
     * @param semestercode
     * @param weekIndx
     * @param weekday
     * @throws JSONException
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void signDraw(final String semestercode, final String weekIndx, final int weekday) throws JSONException {
        int time = Integer.parseInt(sdfHHmm.format(System.currentTimeMillis()));
        String[] start_time = course.getString("start_time").split(":");
        int signStart = 0;
        if (Integer.parseInt(start_time[1]) > 20) {
            signStart = Integer.parseInt(start_time[0] + ((Integer.parseInt(start_time[1]) - 20) + ""));
        } else {
            signStart = Integer.parseInt(((Integer.parseInt(start_time[0]) - 1) + "") + ((60 + Integer.parseInt(start_time[1]) - 20) + ""));
        }
        int signEnd = 0;
        if (Integer.parseInt(start_time[1]) >= 30) {
            signEnd = Integer.parseInt(((Integer.parseInt(start_time[0]) + 1) + "") + ((Integer.parseInt(start_time[1]) - 30) + ""));
        } else {
            signEnd = Integer.parseInt(start_time[0] + ((Integer.parseInt(start_time[1]) + 30) + ""));
        }
        if (time >= signStart && time < signEnd) {
            if (SharedPreferencesUtil.contains(context, semestercode + "-" + weekIndx + "-" + weekday + "-" + signStart + "-" + signEnd)) {
                sign.setEnabled(false);
                sign.getBackground().setTint(Color.parseColor("#3F51B5"));
                sign.setText("已签");
            } else {
                sign.setEnabled(true);
                sign.getBackground().setTint(Color.parseColor("#FF4081"));
                sign.setText("签到");
                final int finalSignEnd = signEnd;
                final int finalSignStart = signStart;
                sign.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferencesUtil.put(context, semestercode + "-" + weekIndx + "-" + weekday + "-" + finalSignStart + "-" + finalSignEnd, true);
                        sign.setEnabled(false);
                        sign.getBackground().setTint(Color.parseColor("#3F51B5"));
                        sign.setText("已签");
                    }
                });
            }
        } else if (
                time > signEnd && time < Integer.parseInt(course.getString("start_time").replace(":", ""))) {
            sign.setEnabled(false);
            sign.getBackground().setTint(Color.GRAY);
            sign.setText("旷课");
        } else {
            sign.setEnabled(false);
            sign.getBackground().setTint(Color.GRAY);
            sign.setText("暂无课堂\n\n无需签到");
        }
    }
}
