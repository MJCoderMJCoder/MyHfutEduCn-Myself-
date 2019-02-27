package com.lzf.myhfuteducn;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.os.storage.StorageManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lzf.myhfuteducn.util.OkHttpUtil;
import com.lzf.myhfuteducn.util.SharedPreferencesUtil;
import com.lzf.myhfuteducn.util.UrlUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 发表日志界面的UI控制层
 *
 * @author MJCoder
 * @see AppCompatActivity
 */
public class LogActivity extends AppCompatActivity {

    /**
     * 图片选择控件：包含用相机拍照或从相册选择的弹出框
     */
    private LinearLayout cameraChoose;
    /**
     * 发表日志的图片预览视图控件
     */
    private ImageView logImg;
    /**
     * 发表日志的文本编辑框
     */
    private EditText logTxt;
    /**
     * 是否匿名发表的选择框
     */
    private CheckBox checkBox;
    /**
     * 请求权限的编号代码
     */
    public final int REQUEST_PERMISSION = 6002;
    /**
     * 选择本地图片的编号代码
     */
    public final int LOCAL_PHOTOS = 6003;
    /**
     * 用相机拍照的编号代码
     */
    public final int PHOTOGRAPH = 6004;
    /**
     * 所请求的一系列权限
     */
    private final String[] PERMISSIONS = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS, Manifest.permission.MEDIA_CONTENT_CONTROL, Manifest.permission.MANAGE_DOCUMENTS, Manifest.permission.INTERNET, Manifest.permission.CAMERA};
    /**
     * 发表日志的图片的缓存文件
     */
    private File currentImageFile = null;
    /**
     * 声明一个集合，在后面的代码中用来存储用户拒绝授权的一系列权限
     */
    private List<String> permissionList = new ArrayList<String>();
    /**
     * 发表日志的文本编辑框的文本内容
     */
    private String logTxtValue = "";

    /**
     * Activity首次被创建时会调用该方法
     *
     * @param savedInstanceState
     * @see Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        cameraChoose = findViewById(R.id.cameraChoose);
        logImg = findViewById(R.id.logImg);
        logTxt = findViewById(R.id.logTxt);
        checkBox = findViewById(R.id.checkBox);
    }

    /**
     * 用户单击界面上的对应视图控件时进行的响应操作
     *
     * @param view 用户单击界面上的对应视图控件
     */
    public void doClick(View view) {
        //请求存储权限
        permissionIsGranted();
        switch (view.getId()) {
            case R.id.backBtn:
                finish();
                break;
            case R.id.logImg:
                cameraChoose.setVisibility(View.VISIBLE);
                break;
            case R.id.photograph:
                imageOperation(PHOTOGRAPH);
                break;
            case R.id.localPhotos:
                imageOperation(LOCAL_PHOTOS);
                break;
            case R.id.publish:
                if (publishCheck()) {
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            Map<String, File> files = new HashMap<String, File>();
                            files.put("logImg", currentImageFile);
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("logTxt", logTxtValue);
                            params.put("logUserName", SharedPreferencesUtil.get(LogActivity.this, "user_name", "") + "");
                            params.put("logUserPhone", SharedPreferencesUtil.get(LogActivity.this, "mobile_phone", "") + "");
                            params.put("logUserEmail", SharedPreferencesUtil.get(LogActivity.this, "account_email", "") + "");
                            params.put("logUserGender", SharedPreferencesUtil.get(LogActivity.this, "gender", "") + "");
                            params.put("logUserBirthday", SharedPreferencesUtil.get(LogActivity.this, "birthday", "") + "");
                            params.put("logUserDepart", SharedPreferencesUtil.get(LogActivity.this, "depart_name", "") + "");
                            params.put("logUserMajor", SharedPreferencesUtil.get(LogActivity.this, "major_name", "") + "");
                            params.put("logUserClass", SharedPreferencesUtil.get(LogActivity.this, "adminclass_name", "") + "");
                            params.put("logIsAnonymity", checkBox.isChecked() + "");
                            final String response = OkHttpUtil.uploadFiles(UrlUtil.LOG_INSERT, params, files);
                            /**
                             *   {"success":true,"describe":"日志发布成功","data":{"logId":3,"logTime":1550289089189,"logImg":"upload/9c987c68-ded2-4cc2-b794-f918b8f32e18_cameraCache.jpg","logTxt":"\uD83C\uDF0E这种，\uD83C\uDFDF️\uD83C\uDFD6️\uD83D\uDDFA️\uD83C\uDF0F\uD83C\uDF0B\uD83D\uDDFB\uD83C\uDFD7️\uD83C\uDF7B\uD83E\uDD43\uD83E\uDD42","logPraise":0,"logUserName":"赵文翔","logUserPhone":"17355305937","logUserEmail":"zwx19980125@qq.com","logUserGender":"男","logUserBirthday":"1998-01-25","logUserDepart":"机械工程学院","logUserMajor":"工业工程","logUserClass":"工业工16-2班","logIsAnonymity":false,"comments":null}}
                             */
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        JSONObject responseJB = new JSONObject(response);
                                        Toast.makeText(LogActivity.this, responseJB.getString("describe"), Toast.LENGTH_SHORT).show();
                                        if (responseJB.getBoolean("success")) {
                                            finish();
                                        }
                                    } catch (JSONException e) {
                                        Toast.makeText(LogActivity.this, response, Toast.LENGTH_SHORT).show();
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    }.start();
                }
                break;
            case R.id.myCommunity:
                startActivity(new Intent(this, MyCommunityActivity.class));
                break;
            default:
                break;
        }
    }

    /**
     * 日志发布前的前端检查：确保所发布的内容真实有效。
     *
     * @return 检查后的结果（true：所发布的内容真实有效可以提交；false：内容缺失或是不合法，需重新编辑）
     */
    private boolean publishCheck() {
        boolean valid = true;
        logTxtValue = logTxt.getText().toString();
        if (logTxtValue == null || logTxtValue.trim().equals("")) {
            valid = false;
            logTxt.requestFocus(); //学号输入框获取焦点
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(logTxt, 0); //弹出软键盘
        } else if (currentImageFile == null) {
            valid = false;
            cameraChoose.setVisibility(View.VISIBLE);
            Toast.makeText(LogActivity.this, "请先选择一张图片", Toast.LENGTH_SHORT).show();
        }
        return valid;
    }

    /**
     * 判断哪些权限未授予以便在必要的时候重新申请
     * 判断存储未授予权限的集合permissionList是否为空：未授予的权限为空，表示都授予了
     */
    private void permissionIsGranted() {
        permissionList.clear();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : PERMISSIONS) {
                if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) { //该权限已经授予
                    //判断是否需要 向用户解释，为什么要申请该权限
                    ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
                    permissionList.add(permission);
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            permission)) {
                        Toast.makeText(this, "MyHfutEduCn获取权限失败，请在“设置”-“应用权限”-打开所需权限", Toast.LENGTH_LONG).show();
                    }
                }
            }
            if (!permissionList.isEmpty()) {
                String[] permissions = new String[permissionList.size()];
                //请求权限
                ActivityCompat.requestPermissions(this, permissionList.toArray(permissions), REQUEST_PERMISSION);
            }
        }
    }

    /**
     * 发表日志图片时的控件选择：包含用相机拍照或从相册选择。
     *
     * @param chooseType 用相机拍照（PHOTOGRAPH）还是从相册选择（LOCAL_PHOTOS）
     */
    private void imageOperation(int chooseType) {
        // 置入一个不设防的VmPolicy：Android 7.0 FileUriExposedException
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
        switch (chooseType) {
            case PHOTOGRAPH:
                try {
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        File dir = new File(Environment.getExternalStorageDirectory(), "MyHfutEduCn/.camera"); //MyHfutEduCn/.camera
                        if (!dir.exists()) {
                            dir.mkdirs();
                        }
                        File[] files = dir.listFiles();//获取文件列表
                        for (int i = 0; i < files.length; i++) {
                            files[i].delete();//删除该文档下的所有文件
                        }
                        currentImageFile = new File(dir, "cameraCache.jpg");
                        //                currentImageFile = new File(dir, System.currentTimeMillis() + ".jpg");
                        if (!currentImageFile.exists()) {
                            try {
                                currentImageFile.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        it.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(currentImageFile));
                        startActivityForResult(it, PHOTOGRAPH);
                    } else {
                        String dirTemp = null;
                        StorageManager storageManager = (StorageManager) getSystemService(Context.STORAGE_SERVICE);
                        Class<?>[] paramClasses = {};
                        Method getVolumePathsMethod;
                        try {
                            getVolumePathsMethod = StorageManager.class.getMethod("getVolumePaths", paramClasses);
                            // 在反射调用之前将此对象的 accessible 标志设置为 true，以此来提升反射速度。
                            getVolumePathsMethod.setAccessible(true);
                            Object[] params = {};
                            Object invoke = getVolumePathsMethod.invoke(storageManager, params);
                            for (int i = 0; i < ((String[]) invoke).length; i++) {
                                if (!((String[]) invoke)[i].equals(Environment.getExternalStorageDirectory().toString())) {
                                    dirTemp = ((String[]) invoke)[i];
                                }
                            }
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        } catch (SecurityException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                        File dir = new File(dirTemp, "MyHfutEduCn/.camera"); //MyHfutEduCn/.camera
                        if (!dir.exists()) {
                            dir.mkdirs();
                        }
                        File[] files = dir.listFiles();//获取文件列表
                        for (int i = 0; i < files.length; i++) {
                            files[i].delete();//删除该文档下的所有文件
                        }
                        currentImageFile = new File(dir, "cameraCache.jpg");
                        //                currentImageFile = new File(dir, System.currentTimeMillis() + ".jpg");
                        if (!currentImageFile.exists()) {
                            try {
                                currentImageFile.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        it.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(currentImageFile));
                        startActivityForResult(it, PHOTOGRAPH);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //hyx
                //        Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //        startActivityForResult(intentFromCapture, GlobalConsts.CAMERA_REQUEST_CODE);
                break;
            case LOCAL_PHOTOS:
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                if ((intent.resolveActivity(getPackageManager()) != null)) {
                    startActivityForResult(intent, LOCAL_PHOTOS);
                } else {
                    Toast.makeText(LogActivity.this, "本地暂无图片", Toast.LENGTH_SHORT).show();
                }
                //        Intent intentFromGallery = new Intent();
                //        intentFromGallery.setType("image/*"); // 设置文件类型
                //        intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
                //        startActivityForResult(intentFromGallery, IMAGE_REQUEST_CODE);
                break;
            default:
                break;
        }
    }

    /**
     * 获取小米手机上的图片路径（解决小米手机上获取图片路径为null的情况）
     *
     * @param intent Intent中包含有小米手机上用户选择图片后返回的相关信息（它可以将结果数据返回给调用者；各种数据可以附加到“extra”中)。
     * @return 小米手机上用户选择的对应的图片路径
     * @see Intent
     */
    private Uri getXiaoMiUri(Intent intent) {
        Uri uri = intent.getData();
        String type = intent.getType();
        if (uri.getScheme().equals("file") && (type.contains("image/"))) {
            String path = uri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);
                ContentResolver cr = this.getContentResolver();
                StringBuffer buff = new StringBuffer();
                buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=")
                        .append("'" + path + "'").append(")");
                Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        new String[]{MediaStore.Images.ImageColumns._ID},
                        buff.toString(), null, null);
                int index = 0;
                for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                    // set _id value
                    index = cur.getInt(index);
                }
                if (index == 0) {
                    // do nothing
                } else {
                    Uri uri_temp = Uri
                            .parse("content://media/external/images/media/"
                                    + index);
                    if (uri_temp != null) {
                        uri = uri_temp;
                    }
                }
            }
        }
        return uri;
    }

    /**
     * 当LogActivity启动的活动退出时调用，为LogActivity提供启动它的requestCode，返回的resultCode以及来自它的任何其他数据。
     * 如果活动显式返回，没有返回任何结果，或者在操作期间崩溃，则 resultCode 将为{@link #RESULT_CANCELED}。
     *
     * @param requestCode 最初提供给startActivityForResult（）的整数请求代码，允许您识别此结果的来源。（用相机拍照（PHOTOGRAPH）还是从相册选择（LOCAL_PHOTOS））
     * @param resultCode  LogActivity调用启动的活动退出时返回的标准活动结果（整数结果代码）。
     * @param data        Intent data中包含有小米手机上用户选择图片后返回的相关信息（它可以将结果数据返回给调用者；各种数据可以附加到“extra”中)。
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //        Log.v("requestCode", "" + requestCode);
        //        Log.v("resultCode", "" + resultCode);
        //        Log.v("data", "" + data);
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == Activity.RESULT_OK) {
                Log.v("currentImageFile", currentImageFile + "");
                switch (requestCode) {
                    case PHOTOGRAPH:
                        if (currentImageFile == null) { //部分机型（vivo v3）返回时会清除currentImageFile所占的内存空间。（重新走MyApplication导致）
                            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                                File dir = new File(Environment.getExternalStorageDirectory(), "MyHfutEduCn/.camera"); //MyHfutEduCn/.camera
                                if (!dir.exists()) {
                                    dir.mkdirs();
                                }
                                currentImageFile = new File(dir, "cameraCache.jpg");
                                if (!currentImageFile.exists()) {
                                    try {
                                        currentImageFile.createNewFile();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                String dirTemp = null;
                                StorageManager storageManager = (StorageManager) getSystemService(Context.STORAGE_SERVICE);
                                Class<?>[] paramClasses = {};
                                Method getVolumePathsMethod;
                                try {
                                    getVolumePathsMethod = StorageManager.class.getMethod("getVolumePaths", paramClasses);
                                    // 在反射调用之前将此对象的 accessible 标志设置为 true，以此来提升反射速度。
                                    getVolumePathsMethod.setAccessible(true);
                                    Object[] params = {};
                                    Object invoke = getVolumePathsMethod.invoke(storageManager, params);
                                    for (int i = 0; i < ((String[]) invoke).length; i++) {
                                        if (!((String[]) invoke)[i].equals(Environment.getExternalStorageDirectory().toString())) {
                                            dirTemp = ((String[]) invoke)[i];
                                        }
                                    }
                                } catch (NoSuchMethodException e) {
                                    e.printStackTrace();
                                } catch (SecurityException e) {
                                    e.printStackTrace();
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                } catch (IllegalArgumentException e) {
                                    e.printStackTrace();
                                } catch (InvocationTargetException e) {
                                    e.printStackTrace();
                                }
                                File dir = new File(dirTemp, "MyHfutEduCn/.camera"); //MyHfutEduCn/.camera
                                if (!dir.exists()) {
                                    dir.mkdirs();
                                }
                                currentImageFile = new File(dir, "cameraCache.jpg");
                                if (!currentImageFile.exists()) {
                                    try {
                                        currentImageFile.createNewFile();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                        break;
                    case LOCAL_PHOTOS:
                        Uri selectedImage = data.getData(); // 获取系统返回的照片的Uri
                        String[] filePathColumn = new String[]{MediaStore.Images.Media.DATA};
                        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);// 从系统表中查询指定Uri对应的照片
                        if (cursor == null) {
                            selectedImage = getXiaoMiUri(data);//解决方案( 解决小米手机上获取图片路径为null的情况)
                            filePathColumn = new String[]{MediaStore.Images.Media.DATA};
                            cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);// 从系统表中查询指定Uri对应的照片
                        }
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        File tempFile = new File(cursor.getString(columnIndex)); // 获取照片路径
                        cursor.close();
                        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                            File dir = new File(Environment.getExternalStorageDirectory(), "MyHfutEduCn/.camera"); //MyHfutEduCn/.camera
                            if (!dir.exists()) {
                                dir.mkdirs();
                            }
                            File[] files = dir.listFiles();//获取文件列表
                            for (int i = 0; i < files.length; i++) {
                                files[i].delete();//删除该文档下的所有文件
                            }
                            currentImageFile = new File(dir, "cameraCache.jpg");
                            //                currentImageFile = new File(dir, System.currentTimeMillis() + ".jpg");
                            if (!currentImageFile.exists()) {
                                try {
                                    currentImageFile.createNewFile();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            String dirTemp = null;
                            StorageManager storageManager = (StorageManager) getSystemService(Context.STORAGE_SERVICE);
                            Class<?>[] paramClasses = {};
                            Method getVolumePathsMethod;
                            try {
                                getVolumePathsMethod = StorageManager.class.getMethod("getVolumePaths", paramClasses);
                                // 在反射调用之前将此对象的 accessible 标志设置为 true，以此来提升反射速度。
                                getVolumePathsMethod.setAccessible(true);
                                Object[] params = {};
                                Object invoke = getVolumePathsMethod.invoke(storageManager, params);
                                for (int i = 0; i < ((String[]) invoke).length; i++) {
                                    if (!((String[]) invoke)[i].equals(Environment.getExternalStorageDirectory().toString())) {
                                        dirTemp = ((String[]) invoke)[i];
                                    }
                                }
                            } catch (NoSuchMethodException e) {
                                e.printStackTrace();
                            } catch (SecurityException e) {
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (IllegalArgumentException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                            File dir = new File(dirTemp, "MyHfutEduCn/.camera"); //MyHfutEduCn/.camera
                            if (!dir.exists()) {
                                dir.mkdirs();
                            }
                            File[] files = dir.listFiles();//获取文件列表
                            for (int i = 0; i < files.length; i++) {
                                files[i].delete();//删除该文档下的所有文件
                            }
                            currentImageFile = new File(dir, "cameraCache.jpg");
                            //                currentImageFile = new File(dir, System.currentTimeMillis() + ".jpg");
                            if (!currentImageFile.exists()) {
                                try {
                                    currentImageFile.createNewFile();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        FileInputStream fis = null;
                        FileOutputStream fos = null;
                        try {
                            //文件复制到sd卡中
                            fis = new FileInputStream(tempFile);
                            fos = new FileOutputStream(currentImageFile);
                            int len = 0;
                            byte[] buffer = new byte[2048];
                            while (-1 != (len = fis.read(buffer))) {
                                fos.write(buffer, 0, len);
                            }
                            fos.flush();
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            //关闭数据流
                            try {
                                if (fos != null)
                                    fos.close();
                                if (fis != null)
                                    fis.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    default:
                        break;
                }
                Log.v("currentImageFile", currentImageFile + "");
                Glide.with(this)
                        .load(currentImageFile.getAbsolutePath())
                        .skipMemoryCache(true) //禁止内存缓存
                        .diskCacheStrategy(DiskCacheStrategy.NONE)//图片缓存策略,这个一般必须有
                        .error(R.mipmap.ic_launcher)// 加载图片失败的时候显示的默认图
                        .into(logImg);
                cameraChoose.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
