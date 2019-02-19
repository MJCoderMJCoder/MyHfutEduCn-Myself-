package com.lzf.myhfuteducn.util;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lzf.myhfuteducn.R;
import com.lzf.myhfuteducn.bean.Comment;
import com.lzf.myhfuteducn.bean.Lesson;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 可复用的适配器
 */
public abstract class ReusableAdapter<T> extends BaseAdapter {
    private List<T> listData;
    private int listItemResource; // id

    public ReusableAdapter(List<T> listData, int listItemResource) {
        this.listData = listData;
        this.listItemResource = listItemResource;
    }

    public int getCount() {
        // 如果listDara不为空，则返回listData.size;否则返回0
        return (listData != null) ? listData.size() : 0;
    }

    public Object getItem(int position) {
        return listData.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    /**
     * View AnimalAdapter.getView(int position, View convertView, ViewGroup
     * parent) 其实这个convertView是系统提供给我们的可供服用的View 的缓存对象
     * <p>
     * 有多少列就会调用多少次getView(有多少个Item，那么getView方法就会被调用多少次)
     */
    @SuppressWarnings("unchecked")
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = ViewHolder.bind(parent.getContext(), convertView,
                parent, listItemResource, position);
        bindView(holder, (T) getItem(position));
        return holder.getItemView();
    }

    /**
     * 定义一个抽象方法，完成ViewHolder与相关数据集的绑定
     * <p>
     * 我们创建新的BaseAdapter的时候，实现这个方法就好，另外，别忘了把我们自定义 的BaseAdapter改成abstact抽象的！
     */
    public abstract void bindView(ViewHolder holder, T obj);

    /**
     * ViewHolder功能：
     * <p>
     * 1. findViewById，设置控件状态；
     * <p>
     * 2. 定义一个查找控件的方法，我们的思路是通过暴露公共的方法，调用方法时传递过来 控件id，以及设置的内容，比如TextView设置文本：
     * public ViewHolder setText(int id, CharSequence text){文本设置}
     * <p>
     * 3. 将convertView复用部分搬到这里，那就需要传递一个context对象了，我们把需要获取 的部分都写到构造方法中！
     * <p>
     * 4. 写一堆设置方法(public)，比如设置文字大小颜色，图片背景等！
     */
    public static class ViewHolder {
        // 存储ListView 的 item中的View
        private SparseArray<View> viewsOflistViewItem;
        private View storeConvertView; // 存放convertView
        private int position; // 位置、定位
        private Context context; // Context上下文
        private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM--dd HH:mm");

        // 构造方法，完成相关初始化
        private ViewHolder(Context context, ViewGroup parent,
                           int listItemResource) {
            // 存储ListView 的 item中的View
            viewsOflistViewItem = new SparseArray<View>();
            this.context = context;
            // View android.view.LayoutInflater.inflate(int resource, ViewGroup
            // root, boolean attachToRoot)【LayoutInflater：布局填充器】
            View convertView = LayoutInflater.from(context).inflate(
                    listItemResource, parent, false);
            convertView.setTag(this);
            storeConvertView = convertView; // 存放convertView
        }

        // 绑定ViewHolder与item
        public static ViewHolder bind(Context context, View convertView,
                                      ViewGroup parent, int listItemResource, int position) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder(context, parent, listItemResource);
            } else {
                holder = (ViewHolder) convertView.getTag();
                holder.storeConvertView = convertView;
            }
            holder.position = position;
            return holder;
        }

        // 根据id获取集合中保存的控件
        @SuppressWarnings("unchecked")
        public <T extends View> T getView(int id) {
            T t = (T) viewsOflistViewItem.get(id);
            if (t == null) {
                t = (T) storeConvertView.findViewById(id);
                viewsOflistViewItem.put(id, t);
            }
            return t;
        }

        // 接着我们再定义一堆暴露出来的方法
        // 获取当前条目
        public View getItemView() {
            return storeConvertView;
        }

        // 获取条目位置
        public int getItemPosition() {
            return position;
        }

        // 设置文字
        public ViewHolder setText(int id, CharSequence text) {
            View view = getView(id);
            if (view instanceof TextView) {
                ((TextView) view).setText(text);
            }
            return this;
        }

        // 设置TextView中的文本对齐
        public ViewHolder setTVGravity(int id, int gravity) {
            View view = getView(id);
            if (view instanceof TextView) {
                ((TextView) view).setGravity(gravity);
            }
            return this;
        }

        // 设置TextView中的文本颜色
        public ViewHolder setTextColor(int id, int color) {
            View view = getView(id);
            if (view instanceof TextView) {
                ((TextView) view).setTextColor(color);
            }
            return this;
        }

        // 设置TextView中的文本颜色
        public ViewHolder setBackgroundColor(int id, int color) {
            View view = getView(id);
            view.setBackgroundColor(color);
            return this;
        }

        // 设置图片
        public ViewHolder setImageResource(int id, int drawableRes) {
            View view = getView(id);
            if (view instanceof ImageView) {
                ((ImageView) view).setImageResource(drawableRes);
            } else {
                view.setBackgroundResource(drawableRes);
            }
            return this;
        }

        // 设置点击监听
        public ViewHolder setOnClickListener(int id, View.OnClickListener listener) {
            getView(id).setOnClickListener(listener);
            return this;
        }

        // 设置触摸监听
        public ViewHolder setOnTouchListener(int id, View.OnTouchListener listener) {
            getView(id).setOnTouchListener(listener);
            return this;
        }

        // 设置可见
        public ViewHolder setVisibility(int id, int visible) {
            getView(id).setVisibility(visible);
            return this;
        }

        // 设置标签
        public ViewHolder setTag(int id, Object obj) {
            getView(id).setTag(obj);
            return this;
        }


        //设置ListView(统计界面专用)
        public ViewHolder setListView(int id, final BaseAdapter adapter) {
            ListView view = (ListView) getView(id);
            if (view instanceof ListView) {
                view.setAdapter(adapter);
            } else {
            }
            return this;
        }

        //设置进度条
        public ViewHolder setProgressBar(int id, int max, int progress) {
            View view = getView(id);
            if (view instanceof ProgressBar) {
                ((ProgressBar) view).setMax(max);
                ((ProgressBar) view).setProgress(progress);
            } else {
            }
            return this;
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public ViewHolder setBackgroundTint(int id, int tintColor) {
            View view = getView(id);
            view.getBackground().setTint(tintColor);
            return this;
        }


        //设置网络图片
        public ViewHolder setImageByGlide(int id, String obj, Context context) {
            View view = getView(id);
            if (view instanceof ImageView) {
                Glide.with(context)
                        .load(obj) //UrlUtils.URL_Image +
                        .diskCacheStrategy(DiskCacheStrategy.ALL)//图片缓存策略,这个一般必须有
                        .error(R.mipmap.ic_launcher)// 加载图片失败的时候显示的默认图
                        .dontAnimate().placeholder(R.mipmap.ic_launcher) //解决Glide 图片闪烁问题
                        .into((ImageView) view);
            } else {
            }
            return this;
        }

        /**
         * 动态添加成绩的子视图
         *
         * @param id
         * @param examgradeList
         * @return
         */
        public ViewHolder dynamicAddExamgrade(int id, List<Lesson.Examgrade> examgradeList) {
            try {
                LinearLayout view = getView(id);
                view.removeAllViews();
                for (Lesson.Examgrade examgrade : examgradeList) {
                    LinearLayout linearLayout = new LinearLayout(context);
                    linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                    linearLayout.setGravity(Gravity.RIGHT);
                    linearLayout.setPadding(15, 0, 15, 0);
                    TextView type = new TextView(context);
                    type.setTextSize(14);
                    type.setText(examgrade.getType() + "：" + examgrade.getScore_text());
                    type.setPadding(15, 15, 40, 15);
                    type.setTextColor(Color.WHITE);
                    linearLayout.addView(type);
                    TextView type_state = new TextView(context);
                    type_state.setTextSize(12);
                    type_state.setText("状态：" + examgrade.getState());
                    type_state.setPadding(15, 15, 40, 15);
                    type_state.setTextColor(Color.WHITE);
                    linearLayout.addView(type_state);
                    TextView type_score = new TextView(context);
                    type_score.setTextSize(12);
                    type_score.setText("分数：" + examgrade.getScore());
                    type_score.setPadding(15, 15, 15, 15);
                    type_score.setTextColor(Color.WHITE);
                    linearLayout.addView(type_score);
                    view.addView(linearLayout);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return this;
        }

        /**
         * 动态添加评论的子视图
         *
         * @param id
         * @param commentList
         * @return
         */
        public ViewHolder dynamicAddComment(int id, List<Comment> commentList) {
            try {
                LinearLayout view = getView(id);
                view.removeAllViews();
                for (Comment comment : commentList) {
                    LinearLayout verticalLinearLayout = new LinearLayout(context);
                    verticalLinearLayout.setOrientation(LinearLayout.VERTICAL);
                    verticalLinearLayout.setPadding(30, 30, 30, 30);
                    LinearLayout horizontalLinearLayout = new LinearLayout(context);
                    horizontalLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
                    horizontalLinearLayout.setWeightSum(2.0f);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.weight = 1;
                    TextView commentUserName = new TextView(context);
                    commentUserName.setText(((comment.isCommentIsAnonymity()) ? "匿名用户" : comment.getCommentUserName()));
                    commentUserName.setTextColor(Color.parseColor("#3F51B5"));
                    commentUserName.setLayoutParams(layoutParams);
                    horizontalLinearLayout.addView(commentUserName);
                    TextView commentTime = new TextView(context);
                    commentTime.setText(simpleDateFormat.format(comment.getCommentTime()));
                    commentTime.setGravity(Gravity.RIGHT);
                    commentTime.setLayoutParams(layoutParams);
                    horizontalLinearLayout.addView(commentTime);
                    verticalLinearLayout.addView(horizontalLinearLayout);
                    TextView commentTxt = new TextView(context);
                    commentTxt.setText("\t" + comment.getCommentTxt());
                    verticalLinearLayout.addView(commentTxt);
                    view.addView(verticalLinearLayout);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return this;
        }
        // 其他方法可自行扩展
    }

    /**
     * 测试ListView的数据更新(why?????)
     * 删除所有数据
     */
    public void clear() {
        if (listData != null) {
            listData.clear();
        }
        notifyDataSetChanged();
    }

    public void add(T data) {
        listData.add(data);
        notifyDataSetChanged();
    }

    public void add(int position, T data) {
        listData.add(position, data);
        notifyDataSetChanged();
    }

    public void delete(T data) {
        listData.remove(data);
        notifyDataSetChanged();
    }

    public void update(int position, T data) {
        listData.set(position, data);
        notifyDataSetChanged();
    }

    public void updateAll(List<T> listData) {
        this.listData = listData;
        notifyDataSetChanged();
    }
}
