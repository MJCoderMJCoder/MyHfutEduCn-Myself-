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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class LogActivity extends AppCompatActivity {

    private LinearLayout cameraChoose;
    private ImageView useImage;

    public final int REQUEST_PERMISSION = 6002; //请求权限
    public final int LOCAL_PHOTOS = 6003; //选择本地图片
    public final int PHOTOGRAPH = 6004; //用相机拍照
    private final String[] PERMISSIONS = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS, Manifest.permission.MEDIA_CONTENT_CONTROL, Manifest.permission.MANAGE_DOCUMENTS, Manifest.permission.INTERNET, Manifest.permission.CAMERA};
    private File currentImageFile = null; //拍照时的缓存文件
    private List<String> permissionList = new ArrayList<String>();  // 声明一个集合，在后面的代码中用来存储用户拒绝授权的权


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        cameraChoose = findViewById(R.id.cameraChoose);
        useImage = findViewById(R.id.useImage);
    }

    public void doClick(View view) {
        //请求存储权限
        permissionIsGranted();
        switch (view.getId()) {
            case R.id.backBtn:
                finish();
                break;
            case R.id.useImage:
                cameraChoose.setVisibility(View.VISIBLE);
                break;
            case R.id.photograph:
                imageOperation(PHOTOGRAPH);
                break;
            case R.id.localPhotos:
                imageOperation(LOCAL_PHOTOS);
                break;
            case R.id.publish:
                break;
            default:
                break;
        }
    }

    /**
     * 判断哪些权限未授予以便必要的时候重新申请
     * 判断存储委授予权限的集合是否为空：未授予的权限为空，表示都授予了
     *
     * @return
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
                ActivityCompat.requestPermissions(this, permissionList.toArray(permissions),
                        REQUEST_PERMISSION);
            }
        }
    }

    /**
     * 分享时的照片选择或是拍照
     *
     * @param chooseType
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
     * 解决小米手机上获取图片路径为null的情况
     *
     * @param intent
     * @return
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.v("requestCode", "" + requestCode);
        Log.v("resultCode", "" + resultCode);
        Log.v("data", "" + data);
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
                        .into(useImage);
                cameraChoose.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
