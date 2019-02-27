package com.lzf.myhfuteducn.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.lzf.myhfuteducn.R;
import com.lzf.myhfuteducn.view.CircleProgressBar;

/**
 * 清除缓存界面的UI控制层
 *
 * @author MJCoder
 * @see android.support.v4.app.Fragment
 */
public class ClearFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    //    private static final String ARG_PARAM1 = "param1";
    //    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    //    private String mParam1;
    //    private String mParam2;

    //    private OnFragmentInteractionListener mListener;

    /**
     * 该Fragment返回的View组件
     */
    private View view;

    /**
     * 清除缓存界面的UI控制层的无参构造方法
     */
    public ClearFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ClearFragment.
     */
    // TODO: Rename and change types and number of parameters
    //    public static ClearFragment newInstance(String param1, String param2) {
    //        ClearFragment fragment = new ClearFragment();
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
     * @return 返回清除缓存界面的UI视图
     * @see LayoutInflater
     * @see ViewGroup
     * @see Bundle
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_clear, container, false);
        final CircleProgressBar circleProgressBar = view.findViewById(R.id.circleProgressBar);
        final Button clear = view.findViewById(R.id.clear);
        final ProgressBar progressBar = view.findViewById(R.id.progressBar);
        circleProgressBar.setOnProgressEndListener(new CircleProgressBar.CircleProgressEndListener() {
            @Override
            public void onProgressEndListener() {
                clear.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                if (clear.getAlpha() != 1.0f) {
                    clear.setEnabled(true);
                    clear.setAlpha(1.0f);
                } else {
                    clear.setEnabled(false);
                    clear.setAlpha(0.1f);
                }
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear.setEnabled(false);
                clear.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                circleProgressBar.clearCache(true);
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

    /**
     * 当该Fragment被添加到Activity中会回调，只会被调用一次
     *
     * @param context 环境/上下文
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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
}
