# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#Webview 中 JavaScript 的调用方法不能混淆
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
#-keepclassmembers class * extends android.webkit.WebViewClient {
#    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
#    public boolean *(android.webkit.WebView, java.lang.String);
#}
#-keepclassmembers class * extends android.webkit.WebViewClient {
#    public void *(android.webkit.WebView, java.lang.String);
#}
#JS 调用Java 方法：Webview 引用的是哪个包名下的。
#-keepattributes *JavascriptInterface*

# Uncomment this to preserve the line number information for
# debugging stack traces.
# 抛出异常时保留代码行号
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

##---------------Begin: proguard configuration for MJCoder  ----------
#指定代码的压缩级别（代码混淆压缩比，在0~7之间，默认为5，一般不做修改）
#-optimizationpasses 5
#包明不混合大小写（混合时不使用大小写混合，混合后的类名为小写）
-dontusemixedcaseclassnames
#不去忽略非公共的库类（混淆第三方jar）
-dontskipnonpubliclibraryclasses
# 指定不去忽略非公共库的类成员
-dontskipnonpubliclibraryclassmembers
#优化  不优化输入的类文件
#-dontoptimize
#不做预校验（preverify是proguard的四个步骤之一，Android不需要preverify，去掉这一步能够加快混淆速度。）
-dontpreverify
#混淆时是否记录日志（能够使我们的项目混淆后产生映射文件：包含有类名->混淆后类名的映射关系）
-verbose
# 混淆时所采用的算法（后面的参数是一个过滤器，这个过滤器是谷歌推荐的算法，一般不做更改）
# -optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
#忽略警告
#-ignorewarning
# 设置是否允许改变作用域
# -allowaccessmodification
# 把混淆类中的方法名也混淆了
-useuniqueclassmembernames
##---------------Begin: 记录生成的日志数据,gradle build时在本项目根目录输出  ----------
#apk 包内所有 class 的内部结构
-dump proguard/class_files.txt
#未混淆的类和成员
-printseeds proguard/seeds.txt
#列出从 apk 中删除的代码
-printusage proguard/unused.txt
#混淆前后的映射
-printmapping proguard/mapping.txt
##---------------End: 记录生成的日志数据，gradle build时 在本项目根目录输出  ----------
#如果引用了v4或者v7包：忽略某个包的警告
-dontwarn android.support.**
# -keep关键字
# keep：包留类和类中的成员，防止他们被混淆（防止被移除或者被重命名）
# keepnames:保留类和类中的成员防止被混淆，但成员如果没有被引用将被删除
# keepclassmembers :只保留类中的成员，防止被混淆和移除。
# keepclassmembernames:只保留类中的成员，但如果成员没有被引用将被删除。
# keepclasseswithmembers:如果当前类中包含指定的方法，则保留类和类成员，否则将被混淆。（防止被移除或者被重命名）
# keepclasseswithmembernames:如果当前类中包含指定的方法，则保留类和类成员，如果类成员没有被引用，则会被移除。
#保护注解
#-keepattributes *Annotation*
#保留Annotation不混淆 这在JSON实体映射时非常重要，比如fastJson
# -keepattributes *Annotation*,InnerClasses
# 保留sdk系统自带的一些内容 【例如：-keepattributes *Annotation* 会保留Activity的被@override注释的onCreate、onDestroy方法等】
# -keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod
# 保持哪些类不被混淆：反射用到的类不混淆(否则反射可能出现问题)；对第三方库中的类不进行混淆
# 反射中使用的元素，需要保证类名、方法名、属性名不变，否则反射会有问题。要把反射调用的类，变量，方法也设置成入口节点。只需要加上-keep就可以了。
# 最好不让一些bean 类混淆：与服务端交互时，使用GSON、fastjson等框架解析服务端数据时，所写的JSON对象类不混淆，否则无法将JSON解析成对应的对象；
# 四大组件不能混淆，四大组件必须在 manifest 中注册声明，而混淆后类名会发生更改，这样不符合四大组件的注册机制。）
#AndroidMainfest中的类不混淆，所以四大组件和Application的子类和Framework层下所有的类默认不会进行混淆，自定义的View默认也不会被混淆。所以像网上贴的很多排除自定义View，或四大组件被混淆的规则在Android Studio中是无需加入的。
# -keep public class * extends android.app.Fragment
# -keep public class * extends android.app.Activity
# -keep public class * extends android.app.Application
# -keep public class * extends android.app.Service
# -keep public class * extends android.content.BroadcastReceiver
# -keep public class * extends android.content.ContentProvider
# -keep public class * extends android.app.backup.BackupAgentHelper
# -keep public class * extends android.preference.Preference
# -keep public class com.android.vending.licensing.ILicensingService
# 保留support下的所有类及其内部类
# -keep class android.support.** {*;}
# 保留继承的
# ? 	匹配单个字符
# *	    匹配类名中的任何部分，但不包含额外的包名（一颗星：表示只是保持该包下的类名，而子包下的类名还是会被混淆；）
# **	匹配类名中的任何部分，并且可以包含额外的包名（两颗星：表示把本包和所含子包下的类名都保持；用以上方法保持类后，你会发现类名虽然未混淆，但里面的具体方法和变量命名还是变了。）
# %	    匹配任何基础类型的类型名
# ...	匹配任意数量、任意类型的参数
# -keep public class * extends android.support.v4.**
# -keep public class * extends android.support.v7.**
# -keep public class * extends android.support.annotation.**
# -keep public class * extends android.support.** #如果有引用v4或者v7包，需添加
#保持自定义控件类不被混淆
#-keepclasseswithmembers class * {
#    public <init>(android.content.Context, android.util.AttributeSet);
#}
#-keepclasseswithmembers class * {
#    public <init>(android.content.Context, android.util.AttributeSet, int);
#}
# 保留我们自定义控件（继承自View）不被混淆
#-keep public class * extends android.view.View {
#    public <init>(android.content.Context);
#    public <init>(android.content.Context, android.util.AttributeSet);
#    public <init>(android.content.Context, android.util.AttributeSet, int);
#    public void set*(...);
#    public * get*();
#}
#保留在Activity中的方法参数是view的方法，这样在layout中写的onClick就不会被影响
#-keepclassmembers class * extends android.app.Activity {
#   public void *(android.view.View);
#}
#-keepclassmembers class * {
#    public void *ButtonClicked(android.view.View);
#}
# 对于带有回调函数的onXXEvent、**On*Listener的，不能被混淆
# -keepclassmembers class * {
#     void *(**On*Event);
#     void *(**On*Listener);
# }
#保持 native 方法不被混淆：JNI方法不可混淆，因为这个方法需要和native方法保持一致；
#JNI 调用 Java 方法，需要通过类名和方法名构成的地址形成。
#Java 使用 Native 方法，Native 是C/C++编写的，方法是无法一同混淆的。
#-keepclasseswithmembernames class * {
#   native <methods>;
#}
#保持 Parcelable 不被混淆
#Parcelable 的子类和 Creator 的静态成员变量不混淆，否则会出现 android.os.BadParcelableExeception 异常。
#如果要保留一个类中的内部类不被混淆则需要用$符号（$	指内部类）
#-keep class * implements android.os.Parcelable {
#  public static final android.os.Parcelable$Creator *;
#}
#保持 Serializable 不被混淆：Serializable 接口类反序列化：
-keep class * implements java.io.Serializable {
    public *;
}
-keepnames class * implements java.io.Serializable
#保持 Serializable 不被混淆并且enum 类也不被混淆
#<init>;     //匹配所有构造器
#<fields>;   //匹配所有域（字段）
#<methods>;  //匹配所有方法方法
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
#保持枚举 enum 类不被混淆
#不能混淆枚举中的value和valueOf方法，因为这两个方法是静态添加到代码中进行，也会被反射使用，所以无法混淆这两种方法。
#应用使用枚举将添加很多方法，增加了包中的方法数，将增加 dex 的大小。
# -keepclassmembers enum * {
#   public static **[] values();
#   public static ** valueOf(java.lang.String);
# }
#不混淆资源类：不混淆R文件中的所有静态字段，我们都知道R文件是通过字段来记录每个资源的id的，字段名要是被混淆了，id也就找不着了。
#-keep class **.R$* {*;}
#-keepclassmembers class **.R$* {
#    public static <fields>;
#}
#避免混淆泛型 如果混淆报错建议关掉
#-keepattributes Signature
#移除Log类打印各个等级日志的代码，打正式包的时候可以做为禁log使用，这里可以作为禁止log打印的功能使用，另外的一种实现方案是通过BuildConfig.DEBUG的变量来控制
#-assumenosideeffects class android.util.Log {
#    public static *** v(...);
#    public static *** i(...);
#    public static *** d(...);
#    public static *** w(...);
#    public static *** e(...);
#}
#如果在当前的application module或者依赖的library module中使用了第三方的库，并不需要显式添加规则
#-libraryjars xxx
#添加了反而有可能在打包的时候遭遇同一个jar多次被指定的错误，一般只需要添加忽略警告和保持某些class不被混淆的声明。
#以libaray的形式引用了开源项目,如果不想混淆 keep 掉，在引入的module的build.gradle中设置minifyEnabled=false
##---------------End: proguard configuration for MJCoder  ----------

##---------------Begin: proguard configuration for Glide  ----------
#ProGuard：Depending on your ProGuard (DexGuard) config and usage, you may need to include the following lines in your proguard.cfg (see the Download and Setup docs page for more details):
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
# for DexGuard only
# -keepresourcexmlelements manifest/application/meta-data@value=GlideModule
##---------------End: proguard configuration for Glide  ----------

##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature
# For using GSON @Expose annotation
-keepattributes *Annotation*
# Gson specific classes
-dontwarn sun.misc.**
#-keep class com.google.gson.stream.** { *; }
# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { *; }
# Prevent proguard from stripping interface information from TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer
##---------------End: proguard configuration for Gson  ----------

##---------------Begin: proguard configuration for OKIO  ----------
# Animal Sniffer compileOnly dependency to ensure APIs are compatible with older versions of Java.
-dontwarn org.codehaus.mojo.animal_sniffer.*
##---------------End: proguard configuration for OKIO  ----------
##---------------Begin: proguard configuration for OKHTTP  ----------
# JSR 305 annotations are for embedding nullability information.
-dontwarn javax.annotation.**
# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase
# Animal Sniffer compileOnly dependency to ensure APIs are compatible with older versions of Java.
-dontwarn org.codehaus.mojo.animal_sniffer.*
# OkHttp platform used only on JVM and when Conscrypt dependency is available.
-dontwarn okhttp3.internal.platform.ConscryptPlatform
##---------------End: proguard configuration for OKHTTP  ----------