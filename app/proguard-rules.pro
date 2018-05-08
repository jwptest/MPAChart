-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-ignorewarnings
-keepattributes EnclosingMethod
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

#-printseeds seeds.txt
#-printusage unused.txt
#-printmapping mapping.txt

-keep public class * implements java.io.Serializable
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Dialog
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference

-dontwarn android.support.v4.**
-keep class android.support.v4.** { *; }

-keep public class * extends android.support.v4.**
-keep public class * extends android.app.Fragment

#butterknife
-keep class butterknife.** { *; }
-dontwarn butterknife.**
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

#Annotation
-keep class * extends java.lang.annotation.Annotation { *; }
-keep interface * extends java.lang.annotation.Annotation { *; }
-keepattributes *Annotation*

-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
-keep public class * implements java.io.Serializable {*;}

#EventBus
-keepclassmembers class ** {
    public void onEvent*(**);
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
-keep class de.greenrobot.event.**{*;}

#glide混淆
-keep class com.zimadai.cd.common.GlideConfig
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

#Bugly混淆
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}

-keepclassmembers public class * extends android.view.View {
    void set*(***);
    *** get*();
}

-keepclassmembers class * extends android.app.Activity {
    public void *(android.view.View);
}

-keep class * extends android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

-keep class **.R$* {
*;
}

-keepclassmembers class **.R$* {
    public static <fields>;
    public static final int *;
    public static final  int[] *;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context,android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context,android.util.AttributeSet,int);
}

-keepclassmembers class * extends android.app.Activity {
    public void *(android.view.View);
}

-keep class * extends android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

-keep public class * extends java.io.Serializable {
    public private protected <fields>;
    public private protected <methods>;
}


-keepclassmembers enum  *,* {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Keep names - Native method names. Keep all native class/method names.
-keepclasseswithmembers,allowshrinking class * {
    native <methods>;
}

# 序列化后必须使用下面这段
-keepnames class * implements java.io.Serializable
-keepclassmembers class * implements java.io.Serializable {
   static final long serialVersionUID;
   private static final java.io.ObjectStreamField[] serialPersistentFields;
   !static !transient <fields>;
   private void writeObject(java.io.ObjectOutputStream);
   private void readObject(java.io.ObjectInputStream);
   java.lang.Object writeReplace();
   java.lang.Object readResolve();
}

##---------------Begin: proguard configuration for Gson  ----------
-dontwarn com.google.**
-keep class com.google.gson.** {*; }
-keepattributes Signature
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.examples.android.model.** { *; }
-keep class com.baidu.image.viewimage.model.**{*;}
-keep class com.baidu.image.viewimage.model.json.**{*;}
##---------------End: proguard configuration for Gson  ----------


-dontwarn android.support.v4.**
-keep class android.support.v4.** { *; }
-keep public class * extends android.support.v4.**

##---------------Begin: proguard configuration for union  ----------
-keep class com.unionpay.** {*;}
-dontwarn  com.unionpay.**

-keep  public class com.unionpay.uppay.net.HttpConnection {
	public <methods>;
}
-keep  public class com.unionpay.uppay.net.HttpParameters {
	public <methods>;
}
-keep  public class com.unionpay.uppay.model.BankCardInfo {
	public <methods>;
}
-keep  public class com.unionpay.uppay.model.PAAInfo {
	public <methods>;
}
-keep  public class com.unionpay.uppay.model.ResponseInfo {
	public <methods>;
}
-keep  public class com.unionpay.uppay.model.PurchaseInfo {
	public <methods>;
}
-keep  public class com.unionpay.uppay.util.DeviceInfo {
	public <methods>;
}
-keep  public class java.util.HashMap {
	public <methods>;
}
-keep  public class java.lang.String {
	public <methods>;
}
-keep  public class java.util.List {
	public <methods>;
}
-keep  public class com.unionpay.uppay.util.PayEngine {
	public <methods>;
	native <methods>;
}
##---------------End: proguard configuration for union  ----------

-keepclasseswithmembernames class * {
    native <methods>;
}

#项目内部配置
-keep class com.finance.event.** {*; }
-keep class com.finance.model.ben.** {*; }

