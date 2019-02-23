package com.lzf.myhfuteducn.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
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
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzf.myhfuteducn.MainActivity;
import com.lzf.myhfuteducn.R;
import com.lzf.myhfuteducn.bean.Week;
import com.lzf.myhfuteducn.util.OkHttpUtil;
import com.lzf.myhfuteducn.util.ReusableAdapter;
import com.lzf.myhfuteducn.util.SharedPreferencesUtil;
import com.lzf.myhfuteducn.util.UrlUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CourseFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CourseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@SuppressLint("ValidFragment")
public class CourseFragment extends Fragment {
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

    public CourseFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CourseFragment.
     */
    // TODO: Rename and change types and number of parameters
    //    public static CourseFragment newInstance(String param1, String param2) {
    //        CourseFragment fragment = new CourseFragment();
    //        Bundle args = new Bundle();
    //        args.putString(ARG_PARAM1, param1);
    //        args.putString(ARG_PARAM2, param2);
    //        fragment.setArguments(args);
    //        return fragment;
    //    }
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
        // Inflate the item_week_layout for this fragment
        try {
            view = inflater.inflate(R.layout.fragment_course, container, false);
            TextView semesternameTV = view.findViewById(R.id.semesternameTV);
            semesternameTV.setText(MainActivity.semestername);

            final Spinner weeknameS = view.findViewById(R.id.weeknameS);
            view.findViewById(R.id.weeksRL).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    weeknameS.performClick();
                }
            });
            JSONArray semesters = MainActivity.semesterWeekList.getJSONArray("semesters");
            JSONArray weeks = semesters.getJSONObject(MainActivity.semesterorder).getJSONArray("weeks");
            final List<Week> weekData = new Gson().fromJson(weeks.toString(), new TypeToken<List<Week>>() {
            }.getType());
            weeknameS.setAdapter(new ReusableAdapter<Week>(weekData, R.layout.item_week) {
                /**
                 * 定义一个抽象方法，完成ViewHolder与相关数据集的绑定
                 * <p>
                 * 我们创建新的BaseAdapter的时候，实现这个方法就好，另外，别忘了把我们自定义 的BaseAdapter改成abstact抽象的！
                 *
                 * @param holder
                 * @param obj
                 */
                @Override
                public void bindView(ViewHolder holder, Week obj) {
                    holder.setText(R.id.weeknameTV, "第  " + obj.getIndex() + "  周");
                    holder.setText(R.id.begin_end, obj.getBegin_on() + " 至 " + obj.getEnd_on());
                }
            });
            weeknameS.setSelection(MainActivity.weekIndx - 1, true);
            weeknameS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Week week = weekData.get(position);
                    if (MainActivity.weekIndx != week.getIndex()) {
                        getWeekSchedule(MainActivity.semestercode + "", week.getIndex() + "");
                        MainActivity.weekIndx = week.getIndex();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            getWeekSchedule(MainActivity.semestercode + "", MainActivity.weekIndx + "");
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
                getActivity().runOnUiThread(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void run() {
                        try {
                            int weekdayCache = 0;
                            int unitCache = 1;
                            View courseCardCache = null;
                            JSONObject responseJson = new JSONObject(response);
                            if (responseJson.getInt("code") == 200) {
                                Toast.makeText(context, responseJson.getString("msg"), Toast.LENGTH_SHORT).show();
                                JSONObject objJson = responseJson.getJSONObject("obj");
                                if (objJson != null) {
                                    JSONArray businessDataJson = objJson.getJSONArray("business_data");
                                    if (businessDataJson != null) {
                                        LinearLayout mondayL = view.findViewById(R.id.monday);
                                        mondayL.removeAllViews();
                                        LinearLayout tuesdayL = view.findViewById(R.id.tuesday);
                                        tuesdayL.removeAllViews();
                                        LinearLayout wednesdayL = view.findViewById(R.id.wednesday);
                                        wednesdayL.removeAllViews();
                                        LinearLayout thursdayL = view.findViewById(R.id.thursday);
                                        thursdayL.removeAllViews();
                                        LinearLayout fridayL = view.findViewById(R.id.friday);
                                        fridayL.removeAllViews();
                                        LinearLayout saturdayL = view.findViewById(R.id.saturday);
                                        saturdayL.removeAllViews();
                                        LinearLayout weekdayL = view.findViewById(R.id.weekday);
                                        wednesdayL.removeAllViews();
                                        List<JSONObject> jsonObjectList = new ArrayList<JSONObject>();
                                        for (int i = 0; i < businessDataJson.length(); i++) {
                                            JSONObject jsonObject = businessDataJson.getJSONObject(i);
                                            int weekday = jsonObject.getInt("weekday");
                                            LinearLayout day = null;
                                            switch (weekday) {
                                                case 1:
                                                    day = mondayL;
                                                    break;
                                                case 2:
                                                    day = tuesdayL;
                                                    break;
                                                case 3:
                                                    day = wednesdayL;
                                                    break;
                                                case 4:
                                                    day = thursdayL;
                                                    break;
                                                case 5:
                                                    day = fridayL;
                                                    break;
                                                case 6:
                                                    day = saturdayL;
                                                    break;
                                                case 7:
                                                    day = weekdayL;
                                                    break;
                                                default:
                                                    break;
                                            }
                                            if (day != null) {
                                                int start_unit = jsonObject.getInt("start_unit");
                                                if (weekdayCache != weekday) {
                                                    unitCache = 1;
                                                    weekdayCache = weekday;
                                                }
                                                float weight = start_unit - unitCache;
                                                if (weight < 0) {
                                                    if (courseCardCache != null) {
                                                        jsonObjectList.add(jsonObject);
                                                        courseDetailDialog(courseCardCache, jsonObjectList);
                                                        ((TextView) courseCardCache.findViewById(R.id.course_name)).setText("多课程时间冲突");
                                                    }
                                                } else {
                                                    jsonObjectList.clear();
                                                    //占位视图
                                                    View course_card = LayoutInflater.from(context).inflate(R.layout.course_card, null); //加载单个课程布局
                                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                                                            (ViewGroup.LayoutParams.MATCH_PARENT, 0, weight); //设置布局高度,即跨多少节课
                                                    course_card.setLayoutParams(params);
                                                    TextView textView = course_card.findViewById(R.id.course_name);
                                                    textView.setText(""); //显示课程名
                                                    textView.getBackground().setTint(Color.WHITE);
                                                    day.addView(course_card);

                                                    //课程卡片视图
                                                    int end_unit = jsonObject.getInt("end_unit");
                                                    unitCache = end_unit + 1;
                                                    weight = end_unit - start_unit + 1;
                                                    course_card = LayoutInflater.from(context).inflate(R.layout.course_card, null); //加载单个课程布局
                                                    params = new LinearLayout.LayoutParams
                                                            (ViewGroup.LayoutParams.MATCH_PARENT, 0, weight); //设置布局高度,即跨多少节课
                                                    course_card.setLayoutParams(params);
                                                    textView = course_card.findViewById(R.id.course_name);
                                                    textView.setText(jsonObject.getString("course_name")); //显示课程名
                                                    if (weekday % 2 == 0) {
                                                        if (i % 2 == 0) {
                                                            textView.getBackground().setTint(Color.parseColor("#9983CC39")); //绿
                                                        } else {
                                                            textView.getBackground().setTint(Color.parseColor("#ffff5722")); //红
                                                        }
                                                    } else {
                                                        if (i % 2 == 0) {
                                                            textView.getBackground().setTint(Color.parseColor("#ff00bcd4")); //蓝
                                                        } else {
                                                            textView.getBackground().setTint(Color.parseColor("#ffff9800")); //黄
                                                        }
                                                    }
                                                    courseCardCache = course_card;
                                                    jsonObjectList.add(jsonObject);
                                                    courseDetailDialog(course_card, jsonObjectList);
                                                    day.addView(course_card);
                                                }
                                            }

                                        }
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
                    }
                });
            }
        }.start();
    }

    /**
     * 课程详情对话框
     *
     * @param course_card
     * @param jsonObjectList
     * @throws JSONException
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void courseDetailDialog(View course_card, List<JSONObject> jsonObjectList) throws JSONException {
        //初始化Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //加载自定义的那个View,同时设置下
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        View course_detail_dialog = inflater.inflate(R.layout.course_detail_dialog, null, false);
        builder.setView(course_detail_dialog);
        builder.setCancelable(true);
        final AlertDialog alert = builder.create();
        if (jsonObjectList.size() > 1) {
            ((TextView) course_detail_dialog.findViewById(R.id.title)).setText("多课程时间冲突");
        } else {
            JSONObject jsonObject = jsonObjectList.get(0);
            ((TextView) course_detail_dialog.findViewById(R.id.title)).setText(jsonObject.getString("course_name"));
        }
        LinearLayout course_detail_L = course_detail_dialog.findViewById(R.id.course_detail_L);
        for (JSONObject jsonObject : jsonObjectList) {
            LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_login));
            linearLayout.getBackground().setTint(Color.parseColor("#88F5FFFA")); //淡灰
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams
                    (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 20, 0, 0);
            linearLayout.setLayoutParams(layoutParams); //设置布局高度,即跨多少节课);
            linearLayout.setPadding(30, 30, 30, 30);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            TextView course_name = new TextView(context);
            course_name.setText("课程名称：" + jsonObject.getString("course_name"));
            linearLayout.addView(course_name);
            TextView activity_weekstate = new TextView(context);
            activity_weekstate.setPadding(250, 0, 0, 0);
            activity_weekstate.setText(jsonObject.getString("activity_weekstate"));
            linearLayout.addView(activity_weekstate);
            TextView time = new TextView(context);
            time.setPadding(250, 0, 0, 0);
            time.setText(jsonObject.getString("start_time") + " - " + jsonObject.getString("end_time"));
            linearLayout.addView(time);
            JSONArray rooms = jsonObject.getJSONArray("rooms");
            for (int i = 0; i < rooms.length(); i++) {
                JSONObject room = rooms.getJSONObject(i);
                TextView campus_name = new TextView(context);
                campus_name.setText("校区：" + room.getString("campus_name") + "（" + room.getString("floor_name") + "）");
                linearLayout.addView(campus_name);
                TextView room_name = new TextView(context);
                room_name.setPadding(160, 0, 0, 0);
                room_name.setText(room.getString("name"));
                linearLayout.addView(room_name);
            }
            JSONArray teachers = jsonObject.getJSONArray("teachers");
            for (int i = 0; i < teachers.length(); i++) {
                TextView teacher_name = new TextView(context);
                teacher_name.setText("教师：" + teachers.getJSONObject(i).getString("name"));
                linearLayout.addView(teacher_name);
            }
            course_detail_L.addView(linearLayout);
        }

        course_detail_dialog.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
        course_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.show();
            }
        });
    }
}
