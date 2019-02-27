package com.lzf.myhfuteducn;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.ComponentCallbacks;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.view.Display;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.PhantomReference;
import java.lang.ref.WeakReference;
import java.util.HashMap;


/**
 * Android系统在每个程序运行的时候都会仅且只创建一个Application对象，所以Application 是单例(singleton)模式的一个类，而且Application对象的生命周期是整个程序中最长的，
 * Application对象的生命周期等于这个程序的生命周期。
 *
 * @author MJCoder
 * @see android.app.Application
 */
public class LzfApplication extends Application {

    /**
     * Application环境/上下文实例
     *
     * @see Context
     */
    private static Context context;

    /**
     * 获取环境/上下文实例
     *
     * @return 环境/上下文
     * @see Context
     */
    public static Context getContext() {
        return context;
    }

    /**
     * 无参构造方法
     */
    public LzfApplication() {
        super();
    }

    /**
     * Returns a hash code value for the object. This method is
     * supported for the benefit of hash tables such as those provided by
     * {@link HashMap}.
     * <p>
     * The general contract of {@code hashCode} is:
     * <ul>
     * <li>Whenever it is invoked on the same object more than once during
     * an execution of a Java application, the {@code hashCode} method
     * must consistently return the same integer, provided no information
     * used in {@code equals} comparisons on the object is modified.
     * This integer need not remain consistent from one execution of an
     * application to another execution of the same application.
     * <li>If two objects are equal according to the {@code equals(Object)}
     * method, then calling the {@code hashCode} method on each of
     * the two objects must produce the same integer result.
     * <li>It is <em>not</em> required that if two objects are unequal
     * according to the {@link Object#equals(Object)}
     * method, then calling the {@code hashCode} method on each of the
     * two objects must produce distinct integer results.  However, the
     * programmer should be aware that producing distinct integer results
     * for unequal objects may improve the performance of hash tables.
     * </ul>
     * <p>
     * As much as is reasonably practical, the hashCode method defined by
     * class {@code Object} does return distinct integers for distinct
     * objects. (This is typically implemented by converting the internal
     * address of the object into an integer, but this implementation
     * technique is not required by the
     * Java&trade; programming language.)
     *
     * @return a hash code value for this object.
     * @see Object#equals(Object)
     * @see System#identityHashCode
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * <p>
     * The {@code equals} method implements an equivalence relation
     * on non-null object references:
     * <ul>
     * <li>It is <i>reflexive</i>: for any non-null reference value
     * {@code x}, {@code x.equals(x)} should return
     * {@code true}.
     * <li>It is <i>symmetric</i>: for any non-null reference values
     * {@code x} and {@code y}, {@code x.equals(y)}
     * should return {@code true} if and only if
     * {@code y.equals(x)} returns {@code true}.
     * <li>It is <i>transitive</i>: for any non-null reference values
     * {@code x}, {@code y}, and {@code z}, if
     * {@code x.equals(y)} returns {@code true} and
     * {@code y.equals(z)} returns {@code true}, then
     * {@code x.equals(z)} should return {@code true}.
     * <li>It is <i>consistent</i>: for any non-null reference values
     * {@code x} and {@code y}, multiple invocations of
     * {@code x.equals(y)} consistently return {@code true}
     * or consistently return {@code false}, provided no
     * information used in {@code equals} comparisons on the
     * objects is modified.
     * <li>For any non-null reference value {@code x},
     * {@code x.equals(null)} should return {@code false}.
     * </ul>
     * <p>
     * The {@code equals} method for class {@code Object} implements
     * the most discriminating possible equivalence relation on objects;
     * that is, for any non-null reference values {@code x} and
     * {@code y}, this method returns {@code true} if and only
     * if {@code x} and {@code y} refer to the same object
     * ({@code x == y} has the value {@code true}).
     * <p>
     * Note that it is generally necessary to override the {@code hashCode}
     * method whenever this method is overridden, so as to maintain the
     * general contract for the {@code hashCode} method, which states
     * that equal objects must have equal hash codes.
     *
     * @param obj the reference object with which to compare.
     * @return {@code true} if this object is the same as the obj
     * argument; {@code false} otherwise.
     * @see #hashCode()
     * @see HashMap
     */
    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    /**
     * Creates and returns a copy of this object.  The precise meaning
     * of "copy" may depend on the class of the object. The general
     * intent is that, for any object {@code x}, the expression:
     * <blockquote>
     * <pre>
     * x.clone() != x</pre></blockquote>
     * will be true, and that the expression:
     * <blockquote>
     * <pre>
     * x.clone().getClass() == x.getClass()</pre></blockquote>
     * will be {@code true}, but these are not absolute requirements.
     * While it is typically the case that:
     * <blockquote>
     * <pre>
     * x.clone().equals(x)</pre></blockquote>
     * will be {@code true}, this is not an absolute requirement.
     *
     * By convention, the returned object should be obtained by calling
     * {@code super.clone}.  If a class and all of its superclasses (except
     * {@code Object}) obey this convention, it will be the case that
     * {@code x.clone().getClass() == x.getClass()}.
     *
     * By convention, the object returned by this method should be independent
     * of this object (which is being cloned).  To achieve this independence,
     * it may be necessary to modify one or more fields of the object returned
     * by {@code super.clone} before returning it.  Typically, this means
     * copying any mutable objects that comprise the internal "deep structure"
     * of the object being cloned and replacing the references to these
     * objects with references to the copies.  If a class contains only
     * primitive fields or references to immutable objects, then it is usually
     * the case that no fields in the object returned by {@code super.clone}
     * need to be modified.
     *
     * The method {@code clone} for class {@code Object} performs a
     * specific cloning operation. First, if the class of this object does
     * not implement the interface {@code Cloneable}, then a
     * {@code CloneNotSupportedException} is thrown. Note that all arrays
     * are considered to implement the interface {@code Cloneable} and that
     * the return type of the {@code clone} method of an array type {@code T[]}
     * is {@code T[]} where T is any reference or primitive type.
     * Otherwise, this method creates a new instance of the class of this
     * object and initializes all its fields with exactly the contents of
     * the corresponding fields of this object, as if by assignment; the
     * contents of the fields are not themselves cloned. Thus, this method
     * performs a "shallow copy" of this object, not a "deep copy" operation.
     *
     * The class {@code Object} does not itself implement the interface
     * {@code Cloneable}, so calling the {@code clone} method on an object
     * whose class is {@code Object} will result in throwing an
     * exception at run time.
     *
     * @return a clone of this instance.
     * @throws CloneNotSupportedException if the object's class does not
     *                                    support the {@code Cloneable} interface. Subclasses
     *                                    that override the {@code clone} method can also
     *                                    throw this exception to indicate that an instance cannot
     *                                    be cloned.
     * @see Cloneable
     */
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /**
     * Returns a string representation of the object. In general, the
     * {@code toString} method returns a string that
     * "textually represents" this object. The result should
     * be a concise but informative representation that is easy for a
     * person to read.
     * It is recommended that all subclasses override this method.
     * <p>
     * The {@code toString} method for class {@code Object}
     * returns a string consisting of the name of the class of which the
     * object is an instance, the at-sign character `{@code @}', and
     * the unsigned hexadecimal representation of the hash code of the
     * object. In other words, this method returns a string equal to the
     * value of:
     * <blockquote>
     * <pre>
     * getClass().getName() + '@' + Integer.toHexString(hashCode())
     * </pre></blockquote>
     *
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return super.toString();
    }

    /**
     * Called by the garbage collector on an object when garbage collection
     * determines that there are no more references to the object.
     * A subclass overrides the {@code finalize} method to dispose of
     * system resources or to perform other cleanup.
     * <p>
     * The general contract of {@code finalize} is that it is invoked
     * if and when the Java&trade; virtual
     * machine has determined that there is no longer any
     * means by which this object can be accessed by any thread that has
     * not yet died, except as a result of an action taken by the
     * finalization of some other object or class which is ready to be
     * finalized. The {@code finalize} method may take any action, including
     * making this object available again to other threads; the usual purpose
     * of {@code finalize}, however, is to perform cleanup actions before
     * the object is irrevocably discarded. For example, the finalize method
     * for an object that represents an input/output connection might perform
     * explicit I/O transactions to break the connection before the object is
     * permanently discarded.
     * <p>
     * The {@code finalize} method of class {@code Object} performs no
     * special action; it simply returns normally. Subclasses of
     * {@code Object} may override this definition.
     * <p>
     * The Java programming language does not guarantee which thread will
     * invoke the {@code finalize} method for any given object. It is
     * guaranteed, however, that the thread that invokes finalize will not
     * be holding any user-visible synchronization locks when finalize is
     * invoked. If an uncaught exception is thrown by the finalize method,
     * the exception is ignored and finalization of that object terminates.
     * <p>
     * After the {@code finalize} method has been invoked for an object, no
     * further action is taken until the Java virtual machine has again
     * determined that there is no longer any means by which this object can
     * be accessed by any thread that has not yet died, including possible
     * actions by other objects or classes which are ready to be finalized,
     * at which point the object may be discarded.
     * <p>
     * The {@code finalize} method is never invoked more than once by a Java
     * virtual machine for any given object.
     * <p>
     * Any exception thrown by the {@code finalize} method causes
     * the finalization of this object to be halted, but is otherwise
     * ignored.
     *
     * @throws Throwable the {@code Exception} raised by this method
     * @jls 12.6 Finalization of Class Instances
     * @see WeakReference
     * @see PhantomReference
     */
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    /**
     * Called when the application is starting, before any activity, service,
     * or receiver objects (excluding content providers) have been created.
     * Implementations should be as quick as possible (for example using
     * lazy initialization of state) since the time spent in this function
     * directly impacts the performance of starting the first activity,
     * service, or receiver in a process.
     * If you override this method, be sure to call super.onCreate().
     */
    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * This method is for use in emulated process environments.  It will
     * never be called on a production Android device, where processes are
     * removed by simply killing them; no user code (including this callback)
     * is executed when doing so.
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    /**
     * 系统的配置信息发生改变时，系统会调用此方法
     *
     * @param newConfig 新的设备配置信息
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    /**
     * 该方法主要用于当前系统可用内存比较低的时候回调使用。
     * OnLowMemory被回调时，已经没有后台进程；
     */
    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    /**
     * 系统会根据不同的内存状态来回调。根据不同的内存状态，来响应不同的内存释放策略。\
     * onTrimMemory被回调时，还有后台进程。
     *
     * @param level 代表不同的内存状态
     *              TRIM_MEMORY_COMPLETE：内存不足，并且该进程在后台进程列表最后一个，马上就要被清理
     *              TRIM_MEMORY_MODERATE：内存不足，并且该进程在后台进程列表的中部。
     *              TRIM_MEMORY_BACKGROUND：内存不足，并且该进程是后台进程。
     *              TRIM_MEMORY_UI_HIDDEN：内存不足，并且该进程的UI已经不可见了。
     *              TRIM_MEMORY_RUNNING_CRITICAL：内存不足(后台进程不足3个)，并且该进程优先级比较高，需要清理内存
     *              TRIM_MEMORY_RUNNING_LOW：内存不足(后台进程不足5个)，并且该进程优先级比较高，需要清理内存
     *              TRIM_MEMORY_RUNNING_MODERATE：内存不足(后台进程超过5个)，并且该进程优先级比较高，需要清理内存
     */
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    /**
     * 注册组件回调
     *
     * @param callback 所有应用程序组件通用的回调API集
     */
    @Override
    public void registerComponentCallbacks(ComponentCallbacks callback) {
        super.registerComponentCallbacks(callback);
    }

    /**
     * 删除组件回调
     *
     * @param callback 所有应用程序组件通用的回调API集
     */
    @Override
    public void unregisterComponentCallbacks(ComponentCallbacks callback) {
        super.unregisterComponentCallbacks(callback);
    }

    /**
     * 注册之后应用里的所有activity的生命周期都会被监控起来。
     *
     * @param callback 用于监听应用中所有Activity的运行情况
     */
    @Override
    public void registerActivityLifecycleCallbacks(ActivityLifecycleCallbacks callback) {
        super.registerActivityLifecycleCallbacks(callback);
    }

    /**
     * 取消注册监控所有activity的生命周期回调。
     *
     * @param callback 用于监听应用中所有Activity的运行情况
     */
    @Override
    public void unregisterActivityLifecycleCallbacks(ActivityLifecycleCallbacks callback) {
        super.unregisterActivityLifecycleCallbacks(callback);
    }

    /**
     * 注册请求辅助监听
     * 当用户请求辅助时，调用此方法，以使用当前应用程序的所有上下文构建完整的ACTION_ASSIST Intent。
     *
     * @param callback 当用户请求辅助时回调
     */
    @Override
    public void registerOnProvideAssistDataListener(OnProvideAssistDataListener callback) {
        super.registerOnProvideAssistDataListener(callback);
    }

    /**
     * 删除请求辅助监听
     *
     * @param callback 当用户删除请求辅助时回调
     */
    @Override
    public void unregisterOnProvideAssistDataListener(OnProvideAssistDataListener callback) {
        super.unregisterOnProvideAssistDataListener(callback);
    }

    /**
     * Set the base context for this ContextWrapper.  All calls will then be
     * delegated to the base context.  Throws
     * IllegalStateException if a base context has already been set.
     *
     * @param base The new base context for this wrapper.
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    /**
     * @return the base context as set by the constructor or setBaseContext
     */
    @Override
    public Context getBaseContext() {
        return super.getBaseContext();
    }

    /**
     * assets文件夹里面的文件都是保持原始的文件格式，需要用AssetManager以字节流的形式读取文件。
     * 获取AssetManager引用。
     *
     * @return AssetManager引用。
     */
    @Override
    public AssetManager getAssets() {
        return super.getAssets();
    }

    /**
     * 读取系统资源
     *
     * @return 系统资源
     */
    @Override
    public Resources getResources() {
        return super.getResources();
    }

    /**
     * 获得一个PackageManger对象：用于检索与当前安装在设备上的应用程序包相关的各种信息的类。
     *
     * @return PackageManger对象：用于检索与当前安装在设备上的应用程序包相关的各种信息的类。
     */
    @Override
    public PackageManager getPackageManager() {
        return super.getPackageManager();
    }


    /**
     * 获取ContentResolver对象：此类为应用程序提供对内容模型的访问权限。
     *
     * @return ContentResolver对象：此类为应用程序提供对内容模型的访问权限。
     */
    @Override
    public ContentResolver getContentResolver() {
        return super.getContentResolver();
    }

    /**
     * 获取Looper对象：用于为线程运行消息循环的类。
     *
     * @return Looper对象：用于为线程运行消息循环的类。
     */
    @Override
    public Looper getMainLooper() {
        return super.getMainLooper();
    }

    /**
     * 获取应用的环境/上下文
     *
     * @return 应用的环境/上下文
     */
    @Override
    public Context getApplicationContext() {
        return super.getApplicationContext();
    }

    /**
     * 设置主题
     *
     * @param resid 主题的资源ID
     */
    @Override
    public void setTheme(int resid) {
        super.setTheme(resid);
    }

    /**
     * 获取主题Resources.Theme对象
     *
     * @return Resources.Theme对象：此类包含特定主题的当前属性值。
     */
    @Override
    public Resources.Theme getTheme() {
        return super.getTheme();
    }

    /**
     * 获取用于启动主应用程序的类装入器。
     *
     * @return 返回用于启动主应用程序的类装入器。
     */
    @Override
    public ClassLoader getClassLoader() {
        return super.getClassLoader();
    }

    /**
     * 获取包名
     *
     * @return 包名
     */
    @Override
    public String getPackageName() {
        return super.getPackageName();
    }

    /**
     * 获取ApplicationInfo对象：可以检索有关特定应用程序的信息。
     *
     * @return ApplicationInfo对象：可以检索有关特定应用程序的信息。
     */
    @Override
    public ApplicationInfo getApplicationInfo() {
        return super.getApplicationInfo();
    }

    /**
     * 获取该程序的安装包资源路径
     *
     * @return 获取该程序的安装包资源路径
     */
    @Override
    public String getPackageResourcePath() {
        return super.getPackageResourcePath();
    }

    /**
     * 获取该程序的安装包代码路径
     *
     * @return 该程序的安装包代码路径
     */
    @Override
    public String getPackageCodePath() {
        return super.getPackageCodePath();
    }

    /**
     * 获取SharedPreferences对象
     *
     * @param name 配置文件名(自定义)，当这个文件不存在时，直接创建，如果已经存在，则直接使用。
     *             （如果想要与本应用程序的其他组件共享此配置文件，可以用这个名字来检索到这个配置文件，在这里要特别注意，因为在Android中已经确定了SharedPreferences是以xml形式保存，所以，在填写文件名参数时，不要给定“.xml”后缀，android会自动添加。只要直接写上文件名即可。它会直接被保存在/data/data/<package name>/shared_prefs路径下，它是采用键值对的形式保存参数。当你需要获得某个参数值时，按照参数的键索引即可）。
     * @param mode 操作模式，默认的模式为MODE_PRIVATE（代表该文件是私有数据，只能被应用本身访问，写入的内容会覆盖原文件的内容）。
     *             还可以使用MODE_APPEND（文件存在，直接在后面追加内容；不存在则新建）、MODE_WORLD_READABLE（当前文件可以被其他应用读取）和MODE_WORLD_WRITEABLE（当前文件可以被其他应用写入）
     * @return SharedPreferences对象
     */
    @Override
    public SharedPreferences getSharedPreferences(String name, int mode) {
        return super.getSharedPreferences(name, mode);
    }

    /**
     * 移动SharedPreferences从sourceContext到当前Context
     *
     * @param sourceContext 原文环境/上下文
     * @param name          SharedPreferences配置文件名(自定义)
     * @return 是否移动成功（true：移动成功；否则：false）
     */
    @Override
    public boolean moveSharedPreferencesFrom(Context sourceContext, String name) {
        return super.moveSharedPreferencesFrom(sourceContext, name);
    }

    /**
     * 删除配置文件名(自定义)为name的SharedPreferences
     *
     * @param name 配置文件名(自定义)
     * @return 是否删除成功（true：删除成功；否则：false）
     */
    @Override
    public boolean deleteSharedPreferences(String name) {
        return super.deleteSharedPreferences(name);
    }

    /**
     * 打开指定文件名称的输入流
     *
     * @param name 指定文件的名称
     * @return 文件输入流
     * @throws FileNotFoundException 文件未发现异常
     */
    @Override
    public FileInputStream openFileInput(String name) throws FileNotFoundException {
        return super.openFileInput(name);
    }

    /**
     * 打开指定文件名称的输出流
     *
     * @param name 指定文件的名称
     * @param mode 操作模式，默认的模式为MODE_PRIVATE（代表该文件是私有数据，只能被应用本身访问，写入的内容会覆盖原文件的内容）。
     *             还可以使用MODE_APPEND（文件存在，直接在后面追加内容；不存在则新建）、MODE_WORLD_READABLE（当前文件可以被其他应用读取）和MODE_WORLD_WRITEABLE（当前文件可以被其他应用写入）
     * @return 文件输出流
     * @throws FileNotFoundException 文件未发现异常
     */
    @Override
    public FileOutputStream openFileOutput(String name, int mode) throws FileNotFoundException {
        return super.openFileOutput(name, mode);
    }

    /**
     * 删除指定名称的文件
     *
     * @param name 指定文件的名称
     * @return 是否删除成功（true：删除成功过；否则：false）
     */
    @Override
    public boolean deleteFile(String name) {
        return super.deleteFile(name);
    }

    /**
     * 获取文件流路径
     *
     * @param name 要获取文件流路径的文件的名称。
     * @return 给定文件：包含文件流路径
     */
    @Override
    public File getFileStreamPath(String name) {
        return super.getFileStreamPath(name);
    }

    /**
     * 文件列表
     *
     * @return 一系列文件列表
     */
    @Override
    public String[] fileList() {
        return super.fileList();
    }

    /**
     * 获取数据的目录
     *
     * @return 数据的目录
     */
    @Override
    public File getDataDir() {
        return super.getDataDir();
    }

    /**
     * 获取内部存储文件的目录
     *
     * @return 文件的目录
     */
    @Override
    public File getFilesDir() {
        return super.getFilesDir();
    }

    /**
     * 获取内部存储未备份的文件目录
     *
     * @return 未备份的文件目录
     */
    @Override
    public File getNoBackupFilesDir() {
        return super.getNoBackupFilesDir();
    }

    /**
     * 获取外部存储文件目录
     *
     * @param type 类型
     *             {@link android.os.Environment#DIRECTORY_MUSIC},
     *             {@link android.os.Environment#DIRECTORY_PODCASTS},
     *             {@link android.os.Environment#DIRECTORY_RINGTONES},
     *             {@link android.os.Environment#DIRECTORY_ALARMS},
     *             {@link android.os.Environment#DIRECTORY_NOTIFICATIONS},
     *             {@link android.os.Environment#DIRECTORY_PICTURES}, or
     *             {@link android.os.Environment#DIRECTORY_MOVIES}.
     * @return 外部存储文件目录
     */
    @Override
    public File getExternalFilesDir(String type) {
        return super.getExternalFilesDir(type);
    }

    /**
     * 获取一系列外部存储文件目录
     *
     * @param type 类型
     *             {@link android.os.Environment#DIRECTORY_MUSIC},
     *             {@link android.os.Environment#DIRECTORY_PODCASTS},
     *             {@link android.os.Environment#DIRECTORY_RINGTONES},
     *             {@link android.os.Environment#DIRECTORY_ALARMS},
     *             {@link android.os.Environment#DIRECTORY_NOTIFICATIONS},
     *             {@link android.os.Environment#DIRECTORY_PICTURES}, or
     *             {@link android.os.Environment#DIRECTORY_MOVIES}.
     * @return 一系列外部存储文件目录
     */
    @Override
    public File[] getExternalFilesDirs(String type) {
        return super.getExternalFilesDirs(type);
    }

    /**
     * 返回所有共享/外部存储设备上特定于应用程序的目录的绝对路径，其中可以找到应用程序的OBB文件（如果有）。
     * 请注意，如果应用程序没有任何OBB文件，则这些目录可能不存在。
     *
     * @return 特定于应用程序的目录的绝对路径。
     */
    @Override
    public File getObbDir() {
        return super.getObbDir();
    }

    /**
     * 返回所有共享/外部存储设备上特定于应用程序的目录的绝对路径，其中可以找到应用程序的OBB文件（如果有）。
     * 请注意，如果应用程序没有任何OBB文件，则这些目录可能不存在。
     *
     * @return 特定于应用程序的一系列目录的绝对路径。
     */
    @Override
    public File[] getObbDirs() {
        return super.getObbDirs();
    }

    /**
     * 获取保存应用程序缓存文件的目录的路径。
     *
     * @return 保存应用程序缓存文件的目录的路径。
     */
    @Override
    public File getCacheDir() {
        return super.getCacheDir();
    }

    /**
     * 获取保存应用程序代码缓存文件的目录的路径。
     *
     * @return 保存应用程序代码缓存文件的目录的路径。
     */
    @Override
    public File getCodeCacheDir() {
        return super.getCodeCacheDir();
    }

    /**
     * 获取应用程序特定目录的绝对路径。外部存储
     *
     * @return 应用程序特定目录的绝对路径。外部存储
     */
    @Override
    public File getExternalCacheDir() {
        return super.getExternalCacheDir();
    }

    /**
     * 获取应用程序一系列特定目录的绝对路径。外部存储
     *
     * @return 应用程序一系列特定目录的绝对路径。外部存储
     */
    @Override
    public File[] getExternalCacheDirs() {
        return super.getExternalCacheDirs();
    }

    /**
     * 获取应用程序一系列特定目录的绝对路径。外部存储
     *
     * @return 应用程序一系列特定目录的绝对路径。外部存储
     */
    @Override
    public File[] getExternalMediaDirs() {
        return super.getExternalMediaDirs();
    }

    /**
     * 获取请求目录的{@link File}对象。 如果目录尚不存在，则将创建该目录。
     *
     * @param name 请求目录的名称
     * @param mode 操作模式，默认的模式为MODE_PRIVATE（代表该文件是私有数据，只能被应用本身访问，写入的内容会覆盖原文件的内容）。
     *             还可以使用MODE_APPEND（文件存在，直接在后面追加内容；不存在则新建）、MODE_WORLD_READABLE（当前文件可以被其他应用读取）和MODE_WORLD_WRITEABLE（当前文件可以被其他应用写入）
     * @return 请求目录的{@link File}对象。 如果目录尚不存在，则将创建该目录。
     */
    @Override
    public File getDir(String name, int mode) {
        return super.getDir(name, mode);
    }

    /**
     * 打开与此Context的应用程序包关联的新私有SQLiteDatabase。 如果数据库文件不存在，创建该文件。
     *
     * @param name    数据库的名称(在应用程序包中是惟一的)。
     * @param mode    操作模式，默认的模式为MODE_PRIVATE（代表该文件是私有数据，只能被应用本身访问，写入的内容会覆盖原文件的内容）。
     *                还可以使用MODE_APPEND（文件存在，直接在后面追加内容；不存在则新建）、MODE_WORLD_READABLE（当前文件可以被其他应用读取）和MODE_WORLD_WRITEABLE（当前文件可以被其他应用写入）
     * @param factory 一个可选的工厂类，在查询被调用时被调用来实例化游标。
     * @return 具有给定名称的新创建数据库的内容
     */
    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory) {
        return super.openOrCreateDatabase(name, mode, factory);
    }

    /**
     * 打开与此Context的应用程序包关联的新私有SQLiteDatabase。 如果数据库文件不存在，创建该文件。
     *
     * @param name         数据库的名称(在应用程序包中是惟一的)。
     * @param mode         操作模式，默认的模式为MODE_PRIVATE（代表该文件是私有数据，只能被应用本身访问，写入的内容会覆盖原文件的内容）。
     *                     还可以使用MODE_APPEND（文件存在，直接在后面追加内容；不存在则新建）、MODE_WORLD_READABLE（当前文件可以被其他应用读取）和MODE_WORLD_WRITEABLE（当前文件可以被其他应用写入）
     * @param factory      一个可选的工厂类，在查询被调用时被调用来实例化游标。
     * @param errorHandler 当sqlite报告数据库损坏时要使用的{@link DatabaseErrorHandler}。 如果为null，则假定为{@link android.database.DefaultDatabaseErrorHandler}。
     * @return 具有给定名称的新创建数据库的内容
     */
    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory, DatabaseErrorHandler errorHandler) {
        return super.openOrCreateDatabase(name, mode, factory, errorHandler);
    }

    /**
     * 将现有数据库文件从给定的源存储上下文移动到此上下文。 这通常用于在升级后在存储位置之间迁移数据，例如迁移到受设备保护的存储。
     * 数据库必须在移动前关闭。
     *
     * @param sourceContext 包含要移动的现有数据库的源上下文。
     * @param name          数据库文件名
     * @return 如果移动成功或者源数据库中不存在数据库：true；否则false。
     */
    @Override
    public boolean moveDatabaseFrom(Context sourceContext, String name) {
        return super.moveDatabaseFrom(sourceContext, name);
    }

    /**
     * 删除数据库
     *
     * @param name 数据库文件名
     * @return 如果删除成功：true；否则false。
     */
    @Override
    public boolean deleteDatabase(String name) {
        return super.deleteDatabase(name);
    }

    /**
     * 获取数据库的路径
     *
     * @param name 数据库文件名
     * @return 包含有数据库路径的对应文件
     */
    @Override
    public File getDatabasePath(String name) {
        return super.getDatabasePath(name);
    }

    /**
     * 一系列数据库
     *
     * @return
     */
    @Override
    public String[] databaseList() {
        return super.databaseList();
    }

    /**
     * 获取壁纸
     *
     * @return Drawable（可绘制的、图片）对象
     */
    @Override
    public Drawable getWallpaper() {
        return super.getWallpaper();
    }

    /**
     * 检索当前系统壁纸; 如果没有设置壁纸，则返回空指针。 这将作为抽象Drawable返回，您可以在View中安装它以显示用户当前设置的任何壁纸。
     *
     * @return Drawable（可绘制的、图片）对象
     */
    @Override
    public Drawable peekWallpaper() {
        return super.peekWallpaper();
    }

    /**
     * 获取壁纸所需的最小宽度
     *
     * @return 壁纸所需的最小宽度
     */
    @Override
    public int getWallpaperDesiredMinimumWidth() {
        return super.getWallpaperDesiredMinimumWidth();
    }

    /**
     * 获取壁纸所需的最小高度
     *
     * @return 壁纸所需的最小高度
     */
    @Override
    public int getWallpaperDesiredMinimumHeight() {
        return super.getWallpaperDesiredMinimumHeight();
    }

    /**
     * 设置壁纸
     *
     * @param bitmap 将用作新系统墙纸的bitmap位图。
     * @throws IOException 如果尝试将壁纸设置为提供的图像时发生错误。
     */
    @Override
    public void setWallpaper(Bitmap bitmap) throws IOException {
        super.setWallpaper(bitmap);
    }

    /**
     * 设置壁纸
     *
     * @param data 包含要作为墙纸安装的原始数据的流。
     * @throws IOException 如果尝试将壁纸设置为提供的图像时发生错误。
     */
    @Override
    public void setWallpaper(InputStream data) throws IOException {
        super.setWallpaper(data);
    }

    /**
     * 删除任何当前设置的系统壁纸，恢复到系统的内置壁纸。 成功时，广播意图{@link Intent＃ACTION_WALLPAPER_CHANGED}。
     *
     * @throws IOException 输入输出异常
     */
    @Override
    public void clearWallpaper() throws IOException {
        super.clearWallpaper();
    }

    /**
     * 开启一个新的Activity
     *
     * @param intent 要启动的Activity的描述
     */
    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
    }

    /**
     * 开启一个新的Activity
     *
     * @param intent  要启动的Activity的描述
     * @param options 有关如何启动活动的其他选项。
     */
    @Override
    public void startActivity(Intent intent, Bundle options) {
        super.startActivity(intent, options);
    }

    /**
     * 开启一系列新的Activity
     *
     * @param intents 要启动的Intent数组
     */
    @Override
    public void startActivities(Intent[] intents) {
        super.startActivities(intents);
    }

    /**
     * 开启一系列新的Activity
     *
     * @param intents 要启动的Intent数组
     * @param options 有关如何启动Activity的其他选项。
     */
    @Override
    public void startActivities(Intent[] intents, Bundle options) {
        super.startActivities(intents, options);
    }

    /**
     * 像{@link #startActivity（Intent，Bundle）}一样，但是要开始使用IntentSender。
     * 如果IntentSender用于某个活动，那么该活动将好比在此处调用常规{@link #startActivity（Intent）}一样启动; 否则，将执行其相关操作（例如发送广播），就像好比已在其上调用{@link IntentSender #sendIntent IntentSender.sendIntent}一样。
     *
     * @param intent       要启动的IntentSender。
     * @param fillInIntent 如果为非null，则将其作为{@link IntentSender #sendIntent}的intent参数提供。
     * @param flagsMask    您将要更改的原始IntentSender中的Intent标志。
     * @param flagsValues  flagsMask 中设置的任何位的所需值
     * @param extraFlags   始终设为0。
     * @throws IntentSender.SendIntentException 发送Intent时的异常
     */
    @Override
    public void startIntentSender(IntentSender intent, Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags) throws IntentSender.SendIntentException {
        super.startIntentSender(intent, fillInIntent, flagsMask, flagsValues, extraFlags);
    }

    /**
     * 像{@link #startActivity（Intent，Bundle）}一样，但是要开始使用IntentSender。
     * 如果IntentSender用于某个活动，那么该活动将好比在此处调用常规{@link #startActivity（Intent）}一样启动; 否则，将执行其相关操作（例如发送广播），就像好比已在其上调用{@link IntentSender #sendIntent IntentSender.sendIntent}一样。
     *
     * @param intent       要启动的IntentSender。
     * @param fillInIntent 如果为非null，则将其作为{@link IntentSender #sendIntent}的intent参数提供。
     * @param flagsMask    您将要更改的原始IntentSender中的Intent标志。
     * @param flagsValues  flagsMask 中设置的任何位的所需值
     * @param extraFlags   始终设为0。
     * @param options      有关如何启动Activity的其他选项。
     * @throws IntentSender.SendIntentException 发送Intent时的异常
     */
    @Override
    public void startIntentSender(IntentSender intent, Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags, Bundle options) throws IntentSender.SendIntentException {
        super.startIntentSender(intent, fillInIntent, flagsMask, flagsValues, extraFlags, options);
    }

    /**
     * 将给定意图广播给所有感兴趣的BroadcastReceivers，允许强制执行可选的必需权限。 这个调用是异步的; 它会立即返回，并且您将在接收器运行时继续执行。没有结果从接收器传播，接收器不能中止广播。
     * 如果要允许接收方传播结果或中止广播，则必须使用{@link #sendOrderedBroadcast（Intent，String）}发送有序广播。
     *
     * @param intent 广播的Intent; 匹配此Intent的所有接收器都将接收广播。
     * @see BroadcastReceiver
     */
    @Override
    public void sendBroadcast(Intent intent) {
        super.sendBroadcast(intent);
    }

    /**
     * 将给定意图广播给所有感兴趣的BroadcastReceivers，允许强制执行可选的必需权限。 这个调用是异步的; 它会立即返回，并且您将在接收器运行时继续执行。没有结果从接收器传播，接收器不能中止广播。
     * 如果要允许接收方传播结果或中止广播，则必须使用{@link #sendOrderedBroadcast（Intent，String）}发送有序广播。
     *
     * @param intent             广播的Intent; 匹配此Intent的所有接收器都将接收广播。
     * @param receiverPermission （可选）字符串命名接收方必须保留的权限才能接收您的广播。 如果为null，则不需要任何权限。
     * @see BroadcastReceiver
     */
    @Override
    public void sendBroadcast(Intent intent, String receiverPermission) {
        super.sendBroadcast(intent, receiverPermission);
    }

    /**
     * 允许您从广播中接收数据。 这是通过在调用时提供自己的BroadcastReceiver来实现的，在广播结束时将其视为最终接收者 - 将调用其{@link BroadcastReceiver＃onReceive}方法，并从其他接收者收集结果值。
     * 广播将以与调用{@link #sendOrderedBroadcast（Intent，String）}相同的方式进行序列化。
     * 与{@link #sendBroadcast（Intent）}一样，此方法是异步的; 它将在调用resultReceiver.onReceive（）之前返回。
     *
     * @param intent             广播的Intent; 匹配此Intent的所有接收器都将接收广播。
     * @param receiverPermission 字符串命名接收者必须持有的权限才能接收您的广播。 如果为null，则不需要任何权限。
     * @see BroadcastReceiver
     */
    @Override
    public void sendOrderedBroadcast(Intent intent, String receiverPermission) {
        super.sendOrderedBroadcast(intent, receiverPermission);
    }

    /**
     * 允许您从广播中接收数据。 这是通过在调用时提供自己的BroadcastReceiver来实现的，在广播结束时将其视为最终接收者 - 将调用其{@link BroadcastReceiver＃onReceive}方法，并从其他接收者收集结果值。
     * 广播将以与调用{@link #sendOrderedBroadcast（Intent，String）}相同的方式进行序列化。
     * 与{@link #sendBroadcast（Intent）}一样，此方法是异步的; 它将在调用resultReceiver.onReceive（）之前返回。
     *
     * @param intent             广播的Intent; 匹配此Intent的所有接收器都将接收广播。
     * @param receiverPermission 字符串命名接收者必须持有的权限才能接收您的广播。 如果为null，则不需要任何权限。
     * @param resultReceiver     你自己的BroadcastReceiver被视为广播的最终接收者。
     * @param scheduler          用于调度resultReceiver回调的自定义处理程序; 如果为null，它将在Context的主线程中进行调度。
     * @param initialCode        结果代码的初始值。 通常是Activity.RESULT_OK。
     * @param initialData        结果数据的初始值。 通常为空。
     * @param initialExtras      结果额外的初始值。 通常为空。
     * @see BroadcastReceiver
     */
    @Override
    public void sendOrderedBroadcast(Intent intent, String receiverPermission, BroadcastReceiver resultReceiver, Handler scheduler, int initialCode, String initialData, Bundle initialExtras) {
        super.sendOrderedBroadcast(intent, receiverPermission, resultReceiver, scheduler, initialCode, initialData, initialExtras);
    }

    /**
     * 允许您指定广播将被发送到的用户。 这不适用于未预先安装在系统映像上的应用程序。
     *
     * @param intent 广播的Intent; 匹配此Intent的所有接收器都将接收广播。
     * @param user   UserHandle发送Intent。
     * @see BroadcastReceiver
     */
    @Override
    public void sendBroadcastAsUser(Intent intent, UserHandle user) {
        super.sendBroadcastAsUser(intent, user);
    }

    /**
     * 允许您指定广播将被发送到的用户。 这不适用于未预先安装在系统映像上的应用程序。
     *
     * @param intent             广播的Intent; 匹配此Intent的所有接收器都将接收广播。
     * @param user               UserHandle发送Intent。
     * @param receiverPermission 字符串命名接收者必须持有的权限才能接收您的广播。 如果为null，则不需要任何权限。
     * @see BroadcastReceiver
     */
    @Override
    public void sendBroadcastAsUser(Intent intent, UserHandle user, String receiverPermission) {
        super.sendBroadcastAsUser(intent, user, receiverPermission);
    }

    /**
     * 允许您指定广播将被发送到的用户。 这不适用于未预先安装在系统映像上的应用程序。
     *
     * @param intent             广播的Intent; 匹配此Intent的所有接收器都将接收广播。
     * @param user               UserHandle发送Intent。
     * @param receiverPermission 字符串命名接收者必须持有的权限才能接收您的广播。 如果为null，则不需要任何权限。
     * @param resultReceiver     你自己的BroadcastReceiver被视为广播的最终接收者。
     * @param scheduler          用于调度resultReceiver回调的自定义处理程序; 如果为null，它将在Context的主线程中进行调度。
     * @param initialCode        结果代码的初始值。 通常是Activity.RESULT_OK。
     * @param initialData        结果数据的初始值。 通常为空。
     * @param initialExtras      结果额外的初始值。 通常为空。
     * @see BroadcastReceiver
     */
    @Override
    public void sendOrderedBroadcastAsUser(Intent intent, UserHandle user, String receiverPermission, BroadcastReceiver resultReceiver, Handler scheduler, int initialCode, String initialData, Bundle initialExtras) {
        super.sendOrderedBroadcastAsUser(intent, user, receiverPermission, resultReceiver, scheduler, initialCode, initialData, initialExtras);
    }

    /**
     * 执行“粘性”的{@link #sendBroadcast（Intent）}，这意味着您发送的意图在广播完成后保持不变，以便其他人可以通过 {@link #registerReceiver(BroadcastReceiver, IntentFilter)}的返回值快速检索该数据。 在所有其他方面，这与{@link #sendBroadcast（Intent）}的行为相同。
     *
     * @param intent 广播Intent; 匹配此Intent的所有接收器将接收广播，并且Intent将被保留以重新广播给未来的接收器。
     * @see BroadcastReceiver
     */
    @Override
    public void sendStickyBroadcast(Intent intent) {
        super.sendStickyBroadcast(intent);
    }

    /**
     * 允许您从广播中接收数据。 这是通过在调用时提供自己的BroadcastReceiver来实现的，在广播结束时将其视为最终接收者 - 将调用其{@link BroadcastReceiver＃onReceive}方法，并从其他接收者收集结果值。
     * 广播将以与调用{@link #sendOrderedBroadcast（Intent，String）}相同的方式进行序列化。
     * 与{@link #sendBroadcast（Intent）}一样，此方法是异步的; 它将在调用resultReceiver.onReceive（）之前返回。 请注意，存储的粘性数据仅是您最初提供给广播的数据，而不是接收方所做的任何更改的结果。
     *
     * @param intent         广播的Intent; 匹配此Intent的所有接收器都将接收广播。
     * @param resultReceiver 你自己的BroadcastReceiver被视为广播的最终接收者。
     * @param scheduler      用于调度resultReceiver回调的自定义处理程序; 如果为null，它将在Context的主线程中进行调度。
     * @param initialCode    结果代码的初始值。 通常是Activity.RESULT_OK。
     * @param initialData    结果数据的初始值。 通常为空。
     * @param initialExtras  结果额外的初始值。 通常为空。
     * @see BroadcastReceiver
     */
    @Override
    public void sendStickyOrderedBroadcast(Intent intent, BroadcastReceiver resultReceiver, Handler scheduler, int initialCode, String initialData, Bundle initialExtras) {
        super.sendStickyOrderedBroadcast(intent, resultReceiver, scheduler, initialCode, initialData, initialExtras);
    }

    /**
     * 删除先前使用{@link #sendStickyBroadcast}发送的数据，这样就好像粘性广播从未发生过一样。
     *
     * @param intent 先前广播的意图。
     * @see BroadcastReceiver
     */
    @Override
    public void removeStickyBroadcast(Intent intent) {
        super.removeStickyBroadcast(intent);
    }

    /**
     * 允许您指定广播将被发送到的用户。 这不适用于未预先安装在系统映像上的应用程序。
     *
     * @param intent 广播的Intent; 匹配此Intent的所有接收器都将接收广播。
     * @param user   UserHandle发送Intent。
     */
    @Override
    public void sendStickyBroadcastAsUser(Intent intent, UserHandle user) {
        super.sendStickyBroadcastAsUser(intent, user);
    }

    /**
     * 允许您指定广播将被发送到的用户。 这不适用于未预先安装在系统映像上的应用程序。
     *
     * @param intent         广播的Intent; 匹配此Intent的所有接收器都将接收广播。
     * @param user           UserHandle发送Intent。
     * @param resultReceiver 你自己的BroadcastReceiver被视为广播的最终接收者。
     * @param scheduler      用于调度resultReceiver回调的自定义处理程序; 如果为null，它将在Context的主线程中进行调度。
     * @param initialCode    结果代码的初始值。 通常是Activity.RESULT_OK。
     * @param initialData    结果数据的初始值。 通常为空。
     * @param initialExtras  结果额外的初始值。 通常为空。
     * @see BroadcastReceiver
     */
    @Override
    public void sendStickyOrderedBroadcastAsUser(Intent intent, UserHandle user, BroadcastReceiver resultReceiver, Handler scheduler, int initialCode, String initialData, Bundle initialExtras) {
        super.sendStickyOrderedBroadcastAsUser(intent, user, resultReceiver, scheduler, initialCode, initialData, initialExtras);
    }

    /**
     * 允许您移除指定广播将被发送到的用户。 这不适用于未预先安装在系统映像上的应用程序。
     * 您必须持有{@link android.Manifest.permission＃BROADCAST_STICKY}权限才能使用此API。 如果您未持有该权限，则会抛出{@link SecurityException}。
     *
     * @param intent 先前广播的Intent。
     * @param user   UserHandle删除粘性广播。
     */
    @Override
    public void removeStickyBroadcastAsUser(Intent intent, UserHandle user) {
        super.removeStickyBroadcastAsUser(intent, user);
    }

    /**
     * 注册Intent广播的接收器，在scheduler的上下文中运行。 有关更多信息，请参阅{@link #registerReceiver（BroadcastReceiver，IntentFilter）}。
     * 这允许您指定谁可以接收哪些广播意图或让接收者在与主应用程序线程不同的线程中运行。
     *
     * @param receiver 广播接收器来处理广播。
     * @param filter   选择要接收的 Intent广播。
     * @return 找到匹配 filter 的第一个粘性意图，如果没有，则返回null。
     */
    @Override
    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        return super.registerReceiver(receiver, filter);
    }

    /**
     * 注册Intent广播的接收器，在scheduler的上下文中运行。 有关更多信息，请参阅{@link #registerReceiver（BroadcastReceiver，IntentFilter）}。
     * 这允许您指定谁可以接收哪些广播意图或让接收者在与主应用程序线程不同的线程中运行。
     *
     * @param receiver 广播接收器来处理广播。
     * @param filter   选择要接收的 Intent广播。
     * @param flags    接收器的附加选项。 可能是0或{@link #RECEIVER_VISIBLE_TO_INSTANT_APPS}。
     * @return 找到匹配 filter 的第一个粘性意图，如果没有，则返回null。
     */
    @Override
    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter, int flags) {
        return super.registerReceiver(receiver, filter, flags);
    }

    /**
     * 注册Intent广播的接收器，在scheduler的上下文中运行。 有关更多信息，请参阅{@link #registerReceiver（BroadcastReceiver，IntentFilter）}。
     * 这允许您指定谁可以接收哪些广播意图或让接收者在与主应用程序线程不同的线程中运行。
     *
     * @param receiver            广播接收器来处理广播。
     * @param filter              选择要接收的 Intent广播。
     * @param broadcastPermission 字符串命名广播必须拥有的权限才能向您发送意图。 如果为null，则不需要任何权限。
     * @param scheduler           处理程序标识将接收Intent的线程。 如果为null，则将使用该进程的主线程。
     * @return 找到匹配 filter 的第一个粘性意图，如果没有，则返回null。
     */
    @Override
    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter, String broadcastPermission, Handler scheduler) {
        return super.registerReceiver(receiver, filter, broadcastPermission, scheduler);
    }

    /**
     * 注册Intent广播的接收器，在scheduler的上下文中运行。 有关更多信息，请参阅{@link #registerReceiver（BroadcastReceiver，IntentFilter）}。
     * 这允许您指定谁可以接收哪些广播意图或让接收者在与主应用程序线程不同的线程中运行。
     *
     * @param receiver            广播接收器来处理广播。
     * @param filter              选择要接收的 Intent广播。
     * @param broadcastPermission 字符串命名广播必须拥有的权限才能向您发送意图。 如果为null，则不需要任何权限。
     * @param scheduler           处理程序标识将接收Intent的线程。 如果为null，则将使用该进程的主线程。
     * @param flags               接收器的附加选项。 可能是0或{@link #RECEIVER_VISIBLE_TO_INSTANT_APPS}。
     * @return 找到匹配 filter 的第一个粘性意图，如果没有，则返回null。
     */
    @Override
    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter, String broadcastPermission, Handler scheduler, int flags) {
        return super.registerReceiver(receiver, filter, broadcastPermission, scheduler, flags);
    }

    /**
     * 取消注册以前注册的BroadcastReceiver。 将删除已为此BroadcastReceiver注册的所有过滤器。
     *
     * @param receiver 要取消注册的广播接收器。
     */
    @Override
    public void unregisterReceiver(BroadcastReceiver receiver) {
        super.unregisterReceiver(receiver);
    }

    /**
     * 请求启动给定的应用程序服务。 Intent应该包含要启动的特定服务实现的完整类名，或者要包含的特定包名。如果指定的Intent较少，则会记录有关此内容的警告。在这种情况下，可以使用任何多个匹配服务。如果此服务尚未运行，它将被实例化并启动（如果需要，为其创建一个进程）;如果它正在运行，那么它仍然在运行。
     * 对此方法的每次调用都将导致对目标服务的{@link android.app.Service＃onStartCommand}方法的相应调用。这提供了一种将作业提交到服务的便捷方式，而无需绑定和调用其接口。
     * 使用startService（）会覆盖由{@link #bindService}管理的默认服务生命周期：它要求服务在调用{@link #stopService}之前保持运行，无论是否有任何客户端连接到它。请注意，对startService（）的调用不会嵌套：无论您调用startService（）多少次，对{@link #stopService}的单次调用都会将其停止。
     * 系统尝试尽可能地保持运行服务。它们应该被停止的唯一时间是当前前台应用程序使用如此多的资源以使服务需要被杀死。如果服务进程中发生任何错误，它将自动重新启动。
     * 如果您没有启动给定服务的权限，此函数将抛出{@link SecurityException}。
     * 每次调用startService（）都会导致系统完成大量工作，以管理围绕意图处理的服务生命周期，这可能需要几毫秒的CPU时间。由于此成本，startService（）不应用于频繁向service传递intent，仅用于安排重要工作。使用{@link #bindService bound services}进行高频呼叫。
     *
     * @param service 标识要启动的服务。 Intent必须完全显式（提供组件名称）。 Intent extras中可能包含其他值以提供参数以及此特定的启动调用。
     * @return 如果服务正在启动或已在运行，则返回已启动的实际服务的{@link ComponentName}; 否则，如果服务不存在，则返回null。
     */
    @Override
    public ComponentName startService(Intent service) {
        return super.startService(service);
    }

    /**
     * 类似于{@link #startService（Intent）}，但隐含的承诺是服务将调用{@link android.app.Service＃startForeground（int，android.app.Notification）startForeground（int，android.app.Notification） ）}一旦它开始运行。
     * 为此提供服务的时间与ANR(Application Not responding)间隔相当，否则系统将自动停止服务并声明应用程序ANR(Application Not responding)。
     * 与普通的{@link #startService（Intent）}不同，此方法可以随时使用，无论托管服务的应用程序是否处于前台状态。
     *
     * @param service 标识要启动的服务。 Intent必须完全显式（提供组件名称）。 Intent extras中可能包含其他值以提供参数以及此特定的启动调用。
     * @return
     */
    @Override
    public ComponentName startForegroundService(Intent service) {
        return super.startForegroundService(service);
    }

    /**
     * 请求停止给定的应用程序服务。 如果服务没有运行，则没有任何反应。 否则它会停止。 请注意，不计算对startService（）的调用 - 无论启动多少次，都会停止服务。
     * 请注意，如果已停止的服务仍然绑定了{@link ServiceConnection}对象且设置了{@link #BIND_AUTO_CREATE}，则在删除所有这些绑定之前不会销毁它。 有关服务生命周期的更多详细信息，请参阅{@link android.app.Service}文档。
     * 如果您没有权限停止给定服务，则此函数将抛出{@link SecurityException}。
     *
     * @param name 要停止的服务的描述。 Intent必须完全显式（提供组件名称）或指定它所针对的特定包名称。
     * @return 如果有一个服务匹配已经运行的给定Intent，那么它将被停止并返回{@code true}; 否则返回{@code false}。
     */
    @Override
    public boolean stopService(Intent name) {
        return super.stopService(name);
    }

    /**
     * 连接到应用程序服务，根据需要创建它。这定义了应用程序和服务之间的依赖关系。给定的conn将在创建时接收服务对象，并告知它是否死亡并重新启动。只要存在调用上下文，系统就会认为该服务是必需的。例如，如果此Context是一个已停止的Activity，则在恢复Activity之前，不需要该服务继续运行。
     * 如果您没有绑定到给定服务的权限，则此函数将抛出{@link SecurityException}。
     * 注意：无法从{@link BroadcastReceiver}组件调用此方法 。
     * 可用于从BroadcastReceiver与服务通信的模式是使用包含要发送的命令的参数调用{@link #startService}，服务调用其{@link android.app.Service #stopSelf（int）完成执行该命令时的方法。有关此内容的说明，但是，从已经注册{@link #registerReceiver}的BroadcastReceiver中使用此方法是可以的，因为此BroadcastReceiver的生命周期与另一个对象（注册它的对象）相关联。
     *
     * @param service 标识要连接的服务。 Intent必须指定显式组件名称。
     * @param conn    服务启动和停止时接收信息。 这必须是有效的ServiceConnection对象; 它不能为空。
     * @param flags   绑定的操作选项。可以是0，{@link #BIND_AUTO_CREATE}，{@link #BIND_DEBUG_UNBIND}，{@link #BIND_NOT_FOREGROUND}，{@link #BIND_ABOVE_CLIENT}，{@link #BIND_ALLOW_OOM_MANAGEMENT}或{@link #BIND_WAIVE_PRIORITY}。
     * @return 如果您已成功绑定到该服务，则返回{@code true}; 如果未建立连接，则返回{@code false}，因此您将不会收到服务对象。 但是，您仍应调用{@link #unbindService}来释放连接。
     */
    @Override
    public boolean bindService(Intent service, ServiceConnection conn, int flags) {
        return super.bindService(service, conn, flags);
    }

    /**
     * 断开与应用程序服务的连接。 重新启动服务后，您将不再接听回调，现在可以随时停止服务。
     *
     * @param conn 先前提供给bindService（）的连接接口。 此参数不能为null。
     */
    @Override
    public void unbindService(ServiceConnection conn) {
        super.unbindService(conn);
    }

    /**
     * 开始执行{@link android.app.Instrumentation}类。 给定的Instrumentation组件将通过终止其目标应用程序（如果当前正在运行），启动目标进程，实例化检测组件，然后让它驱动应用程序来运行。
     * 此函数不是同步的 - 它在仪器启动时和运行时立即返回。
     * 通常只允许对一个未签名的包或使用签名包签名的包运行检测（确保目标信任检测）。
     *
     * @param className   要运行的检测组件的名称。
     * @param profileFile 在检测运行时写入分析数据的可选路径，如果没有分析，则为null。
     * @param arguments   传递给检测的其他可选参数，或null。
     * @return {@code true}如果检测成功启动，否则{@code false}。
     */
    @Override
    public boolean startInstrumentation(ComponentName className, String profileFile, Bundle arguments) {
        return super.startInstrumentation(className, profileFile, arguments);
    }

    /**
     * 按名称将句柄返回到系统级服务。返回对象的类因请求的名称而异。目前可用的名称是：
     * {@link #WINDOW_SERVICE}（“窗口”）
     * 顶级窗口管理器，您可以在其中放置自定义窗口。返回的对象是{@link android.view.WindowManager}。
     * {@link #LAYOUT_INFLATER_SERVICE}（“layout_inflater”）
     * 用于在此上下文中展开布局资源的{@link android.view.LayoutInflater}。
     * {@link #ACTIVITY_SERVICE}（“活动”）
     * 一个{@link android.app.ActivityManager}，用于与系统的全局活动状态进行交互。
     * {@link #POWER_SERVICE}（“权力”）
     * 用于控制电源管理的{@link android.os.PowerManager}。
     * {@link #ALARM_SERVICE}（“闹钟”）
     * 一个{@link android.app.AlarmManager}，用于在您选择时接收意图。
     * {@link #NOTIFICATION_SERVICE}（“通知”）
     * 用于通知用户后台事件的{@link android.app.NotificationManager}。
     * {@link #KEYGUARD_SERVICE}（“keyguard”）
     * 用于控制键盘锁的{@link android.app.KeyguardManager}。
     * {@link #LOCATION_SERVICE}（“位置”）
     *  用于控制位置（例如，GPS）更新的{@link android.location.LocationManager}。
     * {@link #SEARCH_SERVICE}（“搜索”）
     * 用于处理搜索的{@link android.app.SearchManager}。
     * {@link #VIBRATOR_SERVICE}（“振动器”）
     * 用于与振动器硬件交互的{@link android.os.Vibrator}。
     * {@link #CONNECTIVITY_SERVICE}（“连接”）
     * 用于处理网络连接管理的{@link android.net.ConnectivityManager ConnectivityManager}。
     * {@link #WIFI_SERVICE}（“wifi”）
     * 用于管理Wi-Fi连接的{@link android.net.wifi.WifiManager WifiManager}。在NYC之前的版本中，它应该只从应用程序上下文中获取，而不是从任何其他派生上下文中获取，以避免调用进程中的内存泄漏。
     * {@link #WIFI_AWARE_SERVICE}（“wifiaware”）
     * 用于管理Wi-Fi Aware发现和连接的{@link android.net.wifi.aware.WifiAwareManager WifiAwareManager}。
     * {@link＃WIFI_P2P_SERVICE}（“wifip2p”）
     * 用于管理Wi-Fi Direct连接的{@link android.net.wifi.p2p.WifiP2pManager WifiP2pManager}。
     * {@link #INPUT_METHOD_SERVICE}（“input_method”）
     * 用于管理输入方法的{@link android.view.inputmethod.InputMethodManager InputMethodManager}。
     * {@link #UI_MODE_SERVICE}（“uimode”）
     * 用于控制UI模式的{@link android.app.UiModeManager}。
     * {@link #DOWNLOAD_SERVICE}（“下载”）
     * 用于请求HTTP下载的{@link android.app.DownloadManager}
     * {@link #BATTERY_SERVICE}（“batterymanager”）
     * 用于管理电池状态的{@link android.os.BatteryManager}
     * {@link #JOB_SCHEDULER_SERVICE}（“taskmanager”）
     * 用于管理计划任务的{@link android.app.job.JobScheduler}
     * {@link #NETWORK_STATS_SERVICE}（“netstats”）
     * 用于查询网络使用情况统计信息的{@link android.app.usage.NetworkStatsManager NetworkStatsManager}。
     * {@link #HARDWARE_PROPERTIES_SERVICE}（“hardware_properties”）
     * 用于访问硬件属性的{@link android.os.HardwarePropertiesManager}。
     *
     * @param name 所需服务的名称。
     * @return 如果名称不存在，则为服务或null。
     * @see #WINDOW_SERVICE
     * @see android.view.WindowManager
     * @see #LAYOUT_INFLATER_SERVICE
     * @see android.view.LayoutInflater
     * @see #ACTIVITY_SERVICE
     * @see android.app.ActivityManager
     * @see #POWER_SERVICE
     * @see android.os.PowerManager
     * @see #ALARM_SERVICE
     * @see android.app.AlarmManager
     * @see #NOTIFICATION_SERVICE
     * @see android.app.NotificationManager
     * @see #KEYGUARD_SERVICE
     * @see android.app.KeyguardManager
     * @see #LOCATION_SERVICE
     * @see android.location.LocationManager
     * @see #SEARCH_SERVICE
     * @see android.app.SearchManager
     * @see #SENSOR_SERVICE
     * @see android.hardware.SensorManager
     * @see #STORAGE_SERVICE
     * @see android.os.storage.StorageManager
     * @see #VIBRATOR_SERVICE
     * @see android.os.Vibrator
     * @see #CONNECTIVITY_SERVICE
     * @see android.net.ConnectivityManager
     * @see #WIFI_SERVICE
     * @see android.net.wifi.WifiManager
     * @see #AUDIO_SERVICE
     * @see android.media.AudioManager
     * @see #MEDIA_ROUTER_SERVICE
     * @see android.media.MediaRouter
     * @see #TELEPHONY_SERVICE
     * @see android.telephony.TelephonyManager
     * @see #TELEPHONY_SUBSCRIPTION_SERVICE
     * @see android.telephony.SubscriptionManager
     * @see #CARRIER_CONFIG_SERVICE
     * @see android.telephony.CarrierConfigManager
     * @see #INPUT_METHOD_SERVICE
     * @see android.view.inputmethod.InputMethodManager
     * @see #UI_MODE_SERVICE
     * @see android.app.UiModeManager
     * @see #DOWNLOAD_SERVICE
     * @see android.app.DownloadManager
     * @see #BATTERY_SERVICE
     * @see android.os.BatteryManager
     * @see #JOB_SCHEDULER_SERVICE
     * @see android.app.job.JobScheduler
     * @see #NETWORK_STATS_SERVICE
     * @see android.app.usage.NetworkStatsManager
     * @see android.os.HardwarePropertiesManager
     * @see #HARDWARE_PROPERTIES_SERVICE
     */
    @Override
    public Object getSystemService(String name) {
        return super.getSystemService(name);
    }

    /**
     * 获取由指定类表示的系统级服务的名称。
     *
     * @param serviceClass 所需服务的类。
     * @return 如果类不是受支持的系统服务，则服务名称或null。
     */
    @Override
    public String getSystemServiceName(Class<?> serviceClass) {
        return super.getSystemServiceName(serviceClass);
    }

    /**
     * 检查特定的包是否获得了特定的权限。
     *
     * @param permission 正在检查的权限的名称。
     * @param pid        正在对进程ID进行检查。一定是> 0。
     * @param uid        要检查的用户ID。 uid为0是root用户，它将通过每个权限检查。
     * @return {@link PackageManager＃PERMISSION_GRANTED}如果给定的pid / uid允许该权限，或{@link PackageManager＃PERMISSION_DENIED}如果不允许。
     */
    @Override
    public int checkPermission(String permission, int pid, int uid) {
        return super.checkPermission(permission, pid, uid);
    }

    /**
     * 确定您正在处理的IPC的调用进程是否已被授予特定权限。 这与使用{@link android.os.Binder＃getCallingPid}和{@link android.os.Binder #getCallingUid}返回的pid和uid调用{@link #checkPermission（String，int，int）}基本相同。
     * 一个重要的区别是，如果您当前没有处理IPC（Inter-Process Communication：跨进程通信），此功能将始终失败。 这样做是为了防止意外泄露权限; 您可以使用{@link #checkCallingOrSelfPermission}来避免这种保护。
     *
     * @param permission 正在检查的权限的名称。
     * @return {@link PackageManager＃PERMISSION_GRANTED}如果允许调用pid / uid该权限，或{@link PackageManager＃PERMISSION_DENIED}如果不允许。
     */
    @Override
    public int checkCallingPermission(String permission) {
        return super.checkCallingPermission(permission);
    }

    /**
     * 确定IPC（Inter-Process Communication：跨进程通信）或您的调用进程是否已被授予特定权限。 这与{@link #checkCallingPermission}相同，但如果您当前没有处理IPC（Inter-Process Communication：跨进程通信），它会授予您自己的权限。 小心使用！
     *
     * @param permission 正在检查的权限的名称。
     * @return {@link PackageManager＃PERMISSION_GRANTED}如果允许调用pid / uid该权限，或{@link PackageManager＃PERMISSION_DENIED}如果不允许。
     */
    @Override
    public int checkCallingOrSelfPermission(String permission) {
        return super.checkCallingOrSelfPermission(permission);
    }

    /**
     * 确定是否获得了特定的权限。
     *
     * @param permission 正在检查的权限的名称。
     * @return {@link PackageManager＃PERMISSION_GRANTED}如果允许调用pid / uid该权限，或{@link PackageManager＃PERMISSION_DENIED}如果不允许。
     */
    @Override
    public int checkSelfPermission(String permission) {
        return super.checkSelfPermission(permission);
    }

    /**
     * 如果对系统中运行的特定进程和用户标识不允许给定权限，则抛出{@link SecurityException}。
     *
     * @param permission 正在检查的权限的名称。
     * @param pid        正在对进程ID进行检查。一定是> 0。
     * @param uid        要检查的用户ID。 uid为0是root用户，它将通过每个权限检查。
     * @param message    如果抛出异常，则在异常中包含一条消息。
     */
    @Override
    public void enforcePermission(String permission, int pid, int uid, String message) {
        super.enforcePermission(permission, pid, uid, message);
    }

    /**
     * 如果您正在处理的IPC的调用进程未被授予特定权限，请抛出{@link SecurityException}。
     * 这与使用{@link android.os.Binder #getCallingPid}和{@link android.os.Binder＃返回的pid和uid调用{@link #enforcePermission（String，int，int，String）}基本相同getCallingUid}。
     * 一个重要的区别是，如果您当前没有处理IPC，则此函数将始终抛出SecurityException。 这样做是为了防止意外泄露权限; 您可以使用{@link #enforceCallingOrSelfPermission}来避免这种保护。
     *
     * @param permission 正在检查的权限的名称。
     * @param message    如果抛出异常，则在异常中包含一条消息。
     */
    @Override
    public void enforceCallingPermission(String permission, String message) {
        super.enforceCallingPermission(permission, message);
    }

    /**
     * 如果您和您正在处理的IPC的调用进程都未被授予特定权限，请抛出{@link SecurityException}。 这与{@link #enforceCallingPermission}相同，但如果您当前没有处理IPC，它会授予您自己的权限。 小心使用！
     *
     * @param permission 正在检查的权限的名称。
     * @param message    如果抛出异常，则在异常中包含一条消息。
     */
    @Override
    public void enforceCallingOrSelfPermission(String permission, String message) {
        super.enforceCallingOrSelfPermission(permission, message);
    }

    /**
     * 授予访问特定Uri到另一个包的权限，无论该包是否具有访问Uri内容提供者的一般权限。这可以用于授予特定的临时权限，通常是为了响应用户交互（例如用户打开您希望其他人显示的附件）。
     * 通常，您应该使用{@link Intent #FLAG_GRANT_READ_URI_PERMISSION Intent.FLAG_GRANT_READ_URI_PERMISSION}或{@link Intent #FLAG_GRANT_WRITE_URI_PERMISSION Intent.FLAG_GRANT_WRITE_URI_PERMISSION}，并使用Intent直接启动活动而不是此功能。如果直接使用此功能，则应该确保在不再允许目标访问它时调用{@link #revokeUriPermission}。
     * 要成功，拥有Uri的内容提供商必须在其清单中设置@link android.R.styleable#AndroidManifestProvider_grantUriPermissions <grantUriPermissions/>} 属性或标签。
     *
     * @param toPackage 您希望允许访问Uri的包。
     * @param uri       要授予访问权的Uri。
     * @param modeFlags 所需的访问模式。
     */
    @Override
    public void grantUriPermission(String toPackage, Uri uri, int modeFlags) {
        super.grantUriPermission(toPackage, uri, modeFlags);
    }

    /**
     * 删除以前使用{@link #grantUriPermission}或任何其他机制添加的特定内容提供商Uri的所有权限。给定的Uri将匹配所有先前授予的Uris，这些Uris是相同的或者是给定Uri的子路径。也就是说，它不会删除任何更高级别的前缀授予。
     * 在{@link android.os.Build.VERSION_CODES＃LOLLIPOP}之前，如果您没有对Uri的常规权限访问权限，但是通过特定的Uri权限授予获得了访问权限，则无法使用此功能撤销该授权并抛出{@link SecurityException}。
     * 从{@link android.os.Build.VERSION_CODES＃LOLLIPOP}开始，此函数不会抛出安全异常，但会删除向应用程序授予Uri的任何权限（或者没有）。
     * 与{@link #revokeUriPermission（String，Uri，int）}不同，此方法通过任何已发生的机制影响与给定Uri匹配的所有权限授予（例如：间接通过剪贴板，activity launch, service start, 等）。 这意味着这可能具有潜在的危险性，因为它可以撤销其他应用程序可能强烈期望坚持的拨款。
     *
     * @param uri       您想要撤销访问的Uri。
     * @param modeFlags 要撤销的访问模式。
     */
    @Override
    public void revokeUriPermission(Uri uri, int modeFlags) {
        super.revokeUriPermission(uri, modeFlags);
    }

    /**
     * 删除访问特定内容提供程序Uri的权限，该特定内容提供程序Uri之前已添加{@link #grantUriPermission}以用于特定目标程序包。 给定的Uri将匹配所有先前授予的Uris，这些Uris是相同的或者是给定Uri的子路径。 也就是说，它不会删除任何更高级别的前缀授予。
     * 与{@link #revokeUriPermission（Uri，int）}不同，此方法仅撤销已通过{@link #grantUriPermission}明确授予的权限，且仅用于指定的包。 通过其他机制（剪贴板，activity launch, service start, 等）发生的任何匹配授权都不会被删除。
     *
     * @param targetPackage 先前授予访问权限的包。
     * @param uri           您想要撤销访问的Uri。
     * @param modeFlags     要撤销的访问模式。
     */
    @Override
    public void revokeUriPermission(String targetPackage, Uri uri, int modeFlags) {
        super.revokeUriPermission(targetPackage, uri, modeFlags);
    }

    /**
     * 确定是否已授予特定进程和用户标识访问特定URI的权限。 这仅检查已明确授予的权限 - 如果给定的进程/ uid具有对URI的内容提供者的更一般访问权限，则此检查将始终失败。
     *
     * @param uri       正在检查的uri。
     * @param pid       正在对进程ID进行检查。一定是> 0。
     * @param uid       要检查的用户ID。 uid为0是root用户，它将通过每个权限检查。
     * @param modeFlags 要检查的访问模式。
     * @return {@link PackageManager＃PERMISSION_GRANTED}如果允许调用pid / uid该权限，或{@link PackageManager＃PERMISSION_DENIED}如果不允许。
     */
    @Override
    public int checkUriPermission(Uri uri, int pid, int uid, int modeFlags) {
        return super.checkUriPermission(uri, pid, uid, modeFlags);
    }

    /**
     * 确定是否已授予调用进程和用户标识访问特定URI的权限。 这与使用{@link android.os.Binder＃getCallingPid}和{@link android.os.Binder＃返回的pid和uid调用{@link #checkUriPermission（Uri，int，int，int）}基本相同getCallingUid}。
     * 一个重要的区别是，如果您当前没有处理IPC，此功能将始终失败。
     *
     * @param uri       正在检查的uri。
     * @param modeFlags 检查的访问模式。
     * @return {@link PackageManager＃PERMISSION_GRANTED}如果允许调用pid / uid该权限，或{@link PackageManager＃PERMISSION_DENIED}如果不允许。
     */
    @Override
    public int checkCallingUriPermission(Uri uri, int modeFlags) {
        return super.checkCallingUriPermission(uri, modeFlags);
    }

    /**
     * 确定IPC 或 调用进程是否已被授予访问特定URI的权限。 这与{@link #checkCallingUriPermission}相同，但如果您当前没有处理IPC，它会授予您自己的权限。 小心使用！
     *
     * @param uri       正在检查的uri。
     * @param modeFlags 检查的访问模式。
     * @return {@link PackageManager＃PERMISSION_GRANTED}如果允许调用pid / uid该权限，或{@link PackageManager＃PERMISSION_DENIED}如果不允许。
     */
    @Override
    public int checkCallingOrSelfUriPermission(Uri uri, int modeFlags) {
        return super.checkCallingOrSelfUriPermission(uri, modeFlags);
    }

    /**
     * 检查Uri和正常权限。 这允许您在一次调用中同时执行{@link #checkPermission}和{@link #checkUriPermission}。
     *
     * @param uri             要检查其权限的Uri，或为不执行此检查的null。
     * @param readPermission  提供整体读访问权限的权限，或者为不执行此检查的null。
     * @param writePermission 提供整体写访问权限的权限，或者为不执行此检查的null。
     * @param pid             正在对进程ID进行检查。一定是> 0。
     * @param uid             uid为0是root用户，它将通过每个权限检查。
     * @param modeFlags       检查的访问模式。
     * @return {@link PackageManager＃PERMISSION_GRANTED}如果允许调用者访问该URI或拥有其中一个给定权限，或者{@link PackageManager＃PERMISSION_DENIED}如果不允许。
     */
    @Override
    public int checkUriPermission(Uri uri, String readPermission, String writePermission, int pid, int uid, int modeFlags) {
        return super.checkUriPermission(uri, readPermission, writePermission, pid, uid, modeFlags);
    }

    /**
     * 如果未授予特定进程和用户标识访问特定URI的权限，请抛出{@link SecurityException}。 这仅检查已明确授予的权限 - 如果给定的进程/ uid具有对URI的内容提供者的更一般访问权限，则此检查将始终失败。
     *
     * @param uri       正在检查的uri。
     * @param pid       正在对进程ID进行检查。一定是> 0。
     * @param uid       uid为0是root用户，它将通过每个权限检查。
     * @param modeFlags 要强制执行的访问模式。
     * @param message   如果抛出则包含在异常中的消息。
     */
    @Override
    public void enforceUriPermission(Uri uri, int pid, int uid, int modeFlags, String message) {
        super.enforceUriPermission(uri, pid, uid, modeFlags, message);
    }

    /**
     * 如果未授予调用进程和用户标识访问特定URI的权限，请抛出{@link SecurityException}。
     * 这与使用{@link android.os.Binder #getCallingPid}和{@link android.os.Binder#getCallingUid}返回的pid和uid调用{@link #enforceUriPermission（Uri，int，int，int，String）}基本相同。活页夹＃getCallingUid}。
     * 一个重要的区别是，如果您当前没有处理IPC，此函数将始终抛出SecurityException。
     *
     * @param uri       正在检查的uri。
     * @param modeFlags 要强制执行的访问模式。
     * @param message   如果抛出则包含在异常中的消息。
     */
    @Override
    public void enforceCallingUriPermission(Uri uri, int modeFlags, String message) {
        super.enforceCallingUriPermission(uri, modeFlags, message);
    }

    /**
     * 如果IPC  或调用进程未被授予访问特定URI的权限，请抛出{@link SecurityException}。 这与{@link #enforceCallingUriPermission}相同，但如果您当前没有处理IPC，它会授予您自己的权限。 小心使用！
     *
     * @param uri       正在检查的uri。
     * @param modeFlags 要强制执行的访问模式。
     * @param message   如果抛出则包含在异常中的消息。
     */
    @Override
    public void enforceCallingOrSelfUriPermission(Uri uri, int modeFlags, String message) {
        super.enforceCallingOrSelfUriPermission(uri, modeFlags, message);
    }

    /**
     * 强制执行Uri和正常权限。 这允许您在一次调用中同时执行{@link #enforcePermission}和{@link #enforceUriPermission}。
     *
     * @param uri             要检查其权限的Uri，或为不执行此检查的null。
     * @param readPermission  提供整体读访问权限的权限，或者为不执行此检查的null。
     * @param writePermission 提供整体写访问权限的权限，或者为不执行此检查的null。
     * @param pid             正在对进程ID进行检查。一定是> 0。
     * @param uid             uid为0是root用户，它将通过每个权限检查。
     * @param modeFlags       要强制执行的访问模式。
     * @param message         如果抛出则包含在异常中的消息。
     */
    @Override
    public void enforceUriPermission(Uri uri, String readPermission, String writePermission, int pid, int uid, int modeFlags, String message) {
        super.enforceUriPermission(uri, readPermission, writePermission, pid, uid, modeFlags, message);
    }

    /**
     * 返回给定应用程序名称的新Context对象。 此Context与命名应用程序在启动时获得的内容相同，包含相同的资源和类加载器。
     * 每次调用此方法都会返回Context对象的新实例; Context对象不是共享的，但它们共享公共状态（Resources，ClassLoader等），因此Context实例本身相当轻量级。
     *
     * @param packageName 应用程序包的名称。
     * @param flags       选择标记。
     * @return 应用程序的{{@link Context}}
     * @throws PackageManager.NameNotFoundException 如果没有具有给定包名称的应用程序。
     */
    @Override
    public Context createPackageContext(String packageName, int flags) throws PackageManager.NameNotFoundException {
        return super.createPackageContext(packageName, flags);
    }

    /**
     * 为当前Context返回一个新的Context对象，但调整其资源以匹配给定的Configuration。
     * 每次调用此方法都会返回Context对象的新实例; 上下文对象不是共享的，但是常见的状态（ClassLoader，相同配置的其他资源）可能因此Context本身可以相当轻量级。
     *
     * @param overrideConfiguration {@link Configuration}指定在原始Context资源的基本配置中要修改的值。 如果基本配置发生更改（例如由于方向更改），则此上下文的资源也将更改，除非已在此处显式覆盖了值。
     * @return 具有给定配置覆盖的{{@link Context}}。
     */
    @Override
    public Context createConfigurationContext(Configuration overrideConfiguration) {
        return super.createConfigurationContext(overrideConfiguration);
    }

    /**
     * 为当前Context返回一个新的Context对象，但调整其资源以匹配给定Display的度量。
     * 每次调用此方法都会返回Context对象的新实例; 上下文对象不是共享的，但是常见的状态（ClassLoader，相同配置的其他资源）可能因此Context本身可以相当轻量级。
     * 返回的显示上下文提供 {@link android.view.WindowManager}（请参阅{@link #getSystemService（String）}），其配置为在给定显示器上显示窗口。 WindowManager的{@link android.view.WindowManager＃getDefaultDisplay}方法可用于从返回的Context中检索Display。
     *
     * @param display 一个{@link Display}对象，指定显示其指标的显示应该定制Context的资源以及应该显示哪些新窗口。
     * @return 显示{{@link Context}}。
     */
    @Override
    public Context createDisplayContext(Display display) {
        return super.createDisplayContext(display);
    }

    /**
     * 表示这种情况下是否受到限制。
     *
     * @return {@code true}如果此上下文受限制，{@code false}否则。
     */
    @Override
    public boolean isRestricted() {
        return super.isRestricted();
    }

    /**
     * 返回当前Context的新Context对象，但其存储API由受设备保护的存储支持。
     * 在具有直接引导的设备上，存储在此位置的数据使用绑定到物理设备的密钥进行加密，并且可以在设备成功引导后立即访问，在用户使用其凭据进行身份验证之前和之后（例如锁定模式或PIN）。
     * 由于设备保护的数据在没有用户身份验证的情况下可用，因此您应该使用此Context仔细限制存储的数据。例如，强烈建议不要在设备保护区域中存储敏感的身份验证令牌或密码。
     * 如果底层设备无法使用不同的密钥存储受设备保护和凭据保护的数据，则两个存储区域将同时可用。它们仍然是磁盘上的两个不同存储位置，只有可用性窗口会发生变化。
     * 每次调用此方法都会返回Context对象的新实例;上下文对象不是共享的，但是常见的状态（ClassLoader，相同配置的其他资源）可能因此Context本身可以相当轻量级。
     *
     * @return 返回当前Context的新Context对象
     */
    @Override
    public Context createDeviceProtectedStorageContext() {
        return super.createDeviceProtectedStorageContext();
    }

    /**
     * 指示此上下文的存储API是否由受设备保护的存储支持。
     *
     * @return
     */
    @Override
    public boolean isDeviceProtectedStorage() {
        return super.isDeviceProtectedStorage();
    }

    /**
     * 返回给定拆分名称的新Context对象。 新的Context有一个ClassLoader和Resources对象，可以访问split及其所有依赖项的代码/资源。
     * 每次调用此方法都会返回Context对象的新实例; 上下文对象不是共享的，但是常见的状态（ClassLoader，同一拆分的其他资源）可能因此Context本身可以相当轻量级。
     *
     * @param splitName 要包含的拆分的名称，如split的 AndroidManifest.xml 中所声明的。
     * @return 一个{{@link Context}}，加载了给定的分割代码和/或资源。
     * @throws PackageManager.NameNotFoundException
     */
    @Override
    public Context createContextForSplit(String splitName) throws PackageManager.NameNotFoundException {
        return super.createContextForSplit(splitName);
    }
}
