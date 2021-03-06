package com.lzf.myhfuteducn.fragment;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzf.myhfuteducn.MainActivity;
import com.lzf.myhfuteducn.R;
import com.lzf.myhfuteducn.bean.Lesson;
import com.lzf.myhfuteducn.util.OkHttpUtil;
import com.lzf.myhfuteducn.util.ReusableAdapter;
import com.lzf.myhfuteducn.util.SharedPreferencesUtil;
import com.lzf.myhfuteducn.util.UrlUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


/**
 * 查成绩界面的UI控制层
 *
 * @author MJCoder
 * @see android.support.v4.app.Fragment
 */
public class AchievementFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    //    private static final String ARG_PARAM1 = "param1";
    //    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    //    private String mParam1;
    //    private String mParam2;

    //    private OnFragmentInteractionListener mListener;

    /**
     * 环境/上下文
     */
    private Context context;
    /**
     * 该Fragment返回的View组件
     */
    private View view;
    /**
     * 该Fragment的宿主Activity
     */
    private FragmentActivity fragmentActivity;

    /**
     * 查成绩界面的UI控制层的无参构造方法
     */
    public AchievementFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AchievementFragment.
     */
    // TODO: Rename and change types and number of parameters
    //    public static AchievementFragment newInstance(String param1, String param2) {
    //        AchievementFragment fragment = new AchievementFragment();
    //        Bundle args = new Bundle();
    //        args.putString(ARG_PARAM1, param1);
    //        args.putString(ARG_PARAM2, param2);
    //        fragment.setArguments(args);
    //        return fragment;
    //    }

    /**
     * 创建Fragment时回调，只会回调一次。
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
     * @return 返回查成绩界面的UI视图
     * @see LayoutInflater
     * @see ViewGroup
     * @see Bundle
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_achievement, container, false);
        fragmentActivity = getActivity();
        TextView semesternameTV = view.findViewById(R.id.semester_credits_gp);
        semesternameTV.setText(MainActivity.semestername);
        getSemesterScore(MainActivity.semestercode + "");
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
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
     * 根据学期的代码编号semestercode（由教务系统返回）获取对应学期的成绩信息并刷新界面
     *
     * @param semestercode 学期的代码编号semestercode（由教务系统返回）
     */
    private void getSemesterScore(final String semestercode) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                String url = UrlUtil.GET_SEMESTER_SCORE + "?projectId=" + SharedPreferencesUtil.get(context, "projectId0", "")
                        + "&userKey=" + SharedPreferencesUtil.get(context, "userKey", "")
                        + "&identity=0&semestercode=" + (semestercode.length() < 3 ? (0 + semestercode) : semestercode);
                final String response = OkHttpUtil.getData(url);
                if (fragmentActivity == null) {
                    fragmentActivity = getActivity();
                }
                fragmentActivity.runOnUiThread(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void run() {
                        try {
                            JSONObject responseJson = new JSONObject(response);
                            if (responseJson.getInt("code") == 200) {
                                //                                Toast.makeText(context, responseJson.getString("msg"), Toast.LENGTH_SHORT).show();
                                JSONObject objJson = responseJson.getJSONObject("obj");
                                if (objJson != null) {
                                    JSONObject businessDataJson = objJson.getJSONObject("business_data");
                                    if (businessDataJson != null) {
                                        JSONArray semester_lessons = businessDataJson.getJSONArray("semester_lessons");
                                        if (semester_lessons != null && semester_lessons.length() > 0) {
                                            JSONObject semester_lesson = semester_lessons.getJSONObject(0);
                                            TextView semester_credits_gp = view.findViewById(R.id.semester_credits_gp);
                                            semester_credits_gp.setText(MainActivity.semestername + "（总学分：" + semester_lesson.getString("semester_credits") + "；总绩点：" + semester_lesson.getString("semester_gp") + "）");
                                            JSONArray lessons = semester_lesson.getJSONArray("lessons");
                                            ListView acvmtListView = view.findViewById(R.id.acvmtListView);
                                            List<Lesson> lessonList = new Gson().fromJson(lessons.toString(), new TypeToken<List<Lesson>>() {
                                            }.getType());
                                            acvmtListView.setAdapter(new ReusableAdapter<Lesson>(lessonList, R.layout.item_score) {
                                                /**
                                                 * 定义一个抽象方法，完成ViewHolder与相关数据集的绑定
                                                 * <p>
                                                 * 我们创建新的BaseAdapter的时候，实现这个方法就好，另外，别忘了把我们自定义 的BaseAdapter改成abstact抽象的！
                                                 *
                                                 * @param holder
                                                 * @param obj
                                                 */
                                                @Override
                                                public void bindView(ViewHolder holder, Lesson obj) {
                                                    if (obj.isPassed()) {
                                                        holder.setBackgroundTint(R.id.score_card, Color.parseColor("#FF83CC39")); //绿
                                                    } else {
                                                        holder.setBackgroundTint(R.id.score_card, Color.parseColor("#ffff5722")); //红
                                                    }
                                                    holder.setText(R.id.course_name, obj.getCourse_name() + "：" + obj.getScore_text());
                                                    //                                                    holder.setText(R.id.score, "分数：" + obj.getScore());
                                                    holder.setText(R.id.course_credit, "学分：" + obj.getCourse_credit());
                                                    holder.setText(R.id.course_gp, "绩点：" + obj.getCourse_gp());
                                                    holder.dynamicAddExamgrade(R.id.exam_grades, obj.getExam_grades());
                                                }
                                            });
                                        } else {
                                            Toast.makeText(context, "该学期成绩暂无", Toast.LENGTH_SHORT).show();
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
}
