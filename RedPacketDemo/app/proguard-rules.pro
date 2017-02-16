# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/Max/Library/Android/sdk/tools/proguard/proguard-android.txt
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
-optimizationpasses 5 
-dontusemixedcaseclassnames
 -dontskipnonpubliclibraryclasses 
-dontpreverify 
-verbose
 -optimizations !code/simplification/arithmetic,!field/*,!cla
#支付宝 
-keep class com.alipay.**{*;}
 -dontwarn com.alipay.** -keep class com.ta.utdid2.**{*;} 
-keep class com.ut.device.**{*;} 
-keep class org.json.alipay.**{*;}
 -keepattributes EnclosingMethod 

-dontwarn com.tencent.** 
-keep class org.w3c.dom.**{*;}
 -dontwarn org.w3c.dom.** 
-keep class java.nio.file.**{*;}
 -dontwarn java.nio.file.** 
-keep class java.beans.**{*;} 
-dontwarn java.beans.** 
-keep class java.lang.invoke.**{*;}
 -dontwarn java.lang.invoke.** 
-keep class org.codehaus.mojo.animal_sniffer.**{*;}
 -dontwarn org.codehaus.mojo.animal_sniffer.**
# OkHttp3
-dontwarn okhttp3.logging.**
-keep class okhttp3.internal.**{*;}
-dontwarn okio.**
# Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }