# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/Sire/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
#个推
-dontwarn com.igexin.**
-keep class com.igexin.** { *; }
-keep class org.json.** { *; }
#bugly
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}
-dontwarn com.google.errorprone.annotations.**
#retrofit
-dontwarn okio.**
-dontwarn javax.annotation.**
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
#mob share
-keep class cn.smssdk.**{*;}
-keep class com.mob.**{*;}
-dontwarn com.mob.**
-dontwarn cn.smssdk.**

#glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.AppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# for DexGuard only
#-keepresourcexmlelements manifest/application/meta-data@value=GlideModule
#ucrop
-dontwarn com.yalantis.ucrop**
-keep class com.yalantis.ucrop** { *; }
-keep interface com.yalantis.ucrop** { *; }

#dagger
-keep class dagger.** { *; }
-keepclassmembers,allowobfuscation class * {
    @javax.inject.* *;
    @dagger.* *;
    <init>();
}
-keep class **$$ModuleAdapter
-keep class **$$InjectAdapter
-keep class **$$StaticInjection
-keep class javax.inject.** { *; }
-dontwarn dagger.internal.codegen.**
-keep class android.databinding.** { *; }
#eventbus
-keepclassmembers, includedescriptorclasses class ** {
    public void onEvent*(**);
}
-keepclassmembers, includedescriptorclasses class ** {
    public void onEventMainThread*(**);
}

-keepattributes Exceptions, InnerClasses

#databinding

-dontwarn android.databinding.**
-keep class android.databinding.** { *; }

#baidu sdk
-keep class com.baidu.** {*;}
-keep class vi.com.** {*;}
-dontwarn com.baidu.**

#webView and js
-keepattributes *Annotation*
-keepattributes *JavascriptInterface*
#-keep public class org.mq.study.webview.DemoJavaScriptInterface{
#   public <methods>;
#}
#假如是内部类，混淆如下：
-keepattributes *JavascriptInterface*
#-keep public class org.mq.study.webview.webview.DemoJavaScriptInterface$InnerClass{
#    public <methods>;
#}
#其他
-dontwarn com.fasterxml.jackson.**
-keep class com.fasterxml.jackson
-dontwarn com.google.errorprone.**
-dontwarn android.arch.**
-dontwarn android.databinding.**

-keep class com.sire.corelibrary.**
-keep class com.sire.*.ViewModel.**

#==============================一般设置
# 混淆时不使用大小写混合类名
-dontusemixedcaseclassnames
# 不跳过library中的非public的类
-dontskipnonpubliclibraryclasses
# 打印混淆的详细信息
-verbose
# Optimization is turned off by default. Dex does not like code run
# through the ProGuard optimize and preverify steps (and performs some
# of these optimizations on its own).
# 关闭优化（原因见上边的原英文注释）
-dontoptimize
# 不混淆包含native方法的类的类名以及native方法名
-keepclasseswithmembernames class * {
    native <methods>;
}
# 不混淆如下两个谷歌服务类
-keep public class com.google.vending.licensing.ILicensingService
-keep public class com.android.vending.licensing.ILicensingService
# 不做预校验
-dontpreverify

### 忽略警告
#-ignorewarning

#如果引用了v4或者v7包
-dontwarn android.support.**

-keepattributes EnclosingMethod

## 注解支持
-keepclassmembers class *{
   void *(android.view.View);
}

# 不混淆使用了注解的类及类成员
-keep @android.support.annotation.Keep class * {*;}
# 如果类中有使用了注解的方法，则不混淆类和类成员
-keepclasseswithmembers class * {
    @android.support.annotation.Keep <methods>;
}
# 如果类中有使用了注解的字段，则不混淆类和类成员
-keepclasseswithmembers class * {
    @android.support.annotation.Keep <fields>;
}
# 如果类中有使用了注解的构造函数，则不混淆类和类成员
-keepclasseswithmembers class * {
    @android.support.annotation.Keep <init>(...);
}
# 不混淆Fragment的子类类名以及onCreate()、onCreateView()方法名
-keep public class * extends android.support.v4.app.Fragment {
    public void onCreate(android.os.Bundle);
    public android.view.View onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle);
}
# com.othershe.test.model代表数据bean所在的全包名目录
-keep class com.othershe.test.model.** { *; }

# Understand the @Keep support annotation.
# 不混淆Keep类
-keep class android.support.annotation.Keep
#保护注解
-keepattributes *Annotation*

# If you keep the line number information, uncomment this to
# hide the original source file name.
-renamesourcefileattribute SourceFile

-keepattributes Exceptions, Signature, InnerClasses

# Keep - Library. Keep all public and protected classes, fields, and methods.
-keep public class * {
    public protected <fields>;
    public protected <methods>;
}

# Also keep - Enumerations. Keep the special static methods that are required in
# enumeration classes.
-keepclassmembers enum  * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Keep names - Native method names. Keep all native class/method names.
-keepclasseswithmembers,allowshrinking class * {
    native <methods>;
}

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}
-keep public class android.net.http.SslError
-keep public class android.webkit.WebViewClient

-dontwarn android.webkit.WebView
-dontwarn android.net.http.SslError
-dontwarn android.webkit.WebViewClient

# Uncomment this to preserve the line number information for
# debugging stack traces.
-keepattributes SourceFile,LineNumberTable


-optimizationpasses 5
#包明不混合大小写
-dontusemixedcaseclassnames
#不去忽略非公共的库类
-dontskipnonpubliclibraryclasses
 #优化  不优化输入的类文件
-dontoptimize

 # 混淆时所采用的算法
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*


#保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}


#保持自定义控件类不被混淆
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}


#保持 Parcelable 不被混淆
-keep class * implements android.os.Parcelable {
}
 -keep class **.R$* { *; }
#避免混淆泛型 如果混淆报错建议关掉

-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

#lambda
-dontwarn java.lang.invoke.*
-keep -keep java.lang.invoke.* {*;}

# We want to keep methods in Activity that could be used in the XML attribute onClick
# 不混淆Activity中参数是View的方法，例如，一个控件通过android:onClick="clickMethodName"绑定点击事件，混淆后会导致点击事件失效
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}
# 不混淆枚举类中的values()和valueOf()方法
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
#指定压缩级别
-optimizationpasses 5

#不跳过非公共的库的类成员
-dontskipnonpubliclibraryclassmembers
# 不混淆Parcelable实现类中的CREATOR字段，以保证Parcelable机制正常工作
-keepclassmembers class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator CREATOR;
}
# 不混淆R文件中的所有静态字段，以保证正确找到每个资源的id
-keepclassmembers class **.R$* {
    public static <fields>;
}
# keep setters in Views so that animations can still work.
# see http://proguard.sourceforge.net/manual/examples.html#beans
# 不混淆View中的setXxx()和getXxx()方法，以保证属性动画正常工作
-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}



#把混淆类中的方法名也混淆了
-useuniqueclassmembernames

#优化时允许访问并修改有修饰符的类和类的成员
-allowaccessmodification

#将文件来源重命名为“SourceFile”字符串
-renamesourcefileattribute SourceFile

#保留行号
-keepattributes SourceFile,LineNumberTable

#保持泛型
-keepattributes Signature

#保持所有实现 Serializable 接口的类成员
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

#Fragment不需要在AndroidManifest.xml中注册，需要额外保护下
-keep public class * extends android.support.v4.app.Fragment
-keep public class * extends android.app.Fragment

-keepclassmembers class * {
    private static synthetic java.lang.Object $deserializeLambda$(java.lang.invoke.SerializedLambda);
}

-keepclassmembernames class * {
    private static synthetic *** lambda$*(...);
}
#====================特殊序列化bean，反射出错
# com.othershe.test.model代表数据bean所在的全包名目录
-keep class com.sire.bbsmodule.DB.Entry.** { *; }
-keep class com.sire.feedmodule.DB.Entry.** { *; }
-keep class com.sire.usermodule.DB.Entry.** { *; }
-keep class com.sire.bbsmodule.Pojo.** { *; }
-keep class com.sire.feedmodule.Pojo.** { *; }
-keep class com.sire.upgrademodule.Pojo.** { *; }
-keep class com.sire.usermodule.Pojo.** { *; }


