package com.lzf.myhfuteducn.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
        View view = inflater.inflate(R.layout.fragment_course, container, false);
        TextView semesternameTV = view.findViewById(R.id.semesternameTV);
        semesternameTV.setText(MainActivity.semestername);
        Spinner weeknameS = view.findViewById(R.id.weeknameS);
        List<Week> weekData = new ArrayList<Week>();
        try {
            JSONArray semesters = MainActivity.semesterWeekList.getJSONArray("semesters");
            JSONArray weeks = semesters.getJSONObject(MainActivity.semesterorder).getJSONArray("weeks");
            for (int i = 0; i < weeks.length(); i++) {
                JSONObject week = weeks.getJSONObject(i);
                weekData.add(new Week(week.getString("begin_on"), week.getString("end_on"), week.getString("index")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
                holder.setText(R.id.weeknameTV, "第" + obj.getIndex() + "周");
            }
        });
        weeknameS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView weeknameTV = (TextView) view.findViewById(R.id.weeknameTV);
                Log.v("view", "您选择的是~：" + weeknameTV.getText().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        getWeekSchedule(MainActivity.semestercode, MainActivity.weekIndx);
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
     * 获取一周的课程表
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
                map.put("semestercode", semestercode);
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
                    @Override
                    public void run() {
                        try {
                            JSONObject responseJson = new JSONObject(response);
                            if (responseJson.getInt("code") == 200) {
                                Toast.makeText(context, responseJson.getString("msg"), Toast.LENGTH_SHORT).show();
                                JSONObject objJson = responseJson.getJSONObject("obj");
                                if (objJson != null) {
                                    JSONArray businessDataJson = objJson.getJSONArray("business_data");
                                    if (businessDataJson != null && businessDataJson.length() > 0) {

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

}
