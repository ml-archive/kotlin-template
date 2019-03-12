# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/joso/sdk/sdk/tools/proguard/proguard-android.txt

# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:
-keepattributes *Annotation*,Exceptions, Signature, InnerClasses, EnclosingMethod

#Hockey
-keep public class javax.net.ssl.**
-keepclassmembers public class javax.net.ssl.** { *; }
-keep public class org.apache.http.**
-keepclassmembers public class org.apache.http.** { *; }
-keepclassmembers class net.hockeyapp.android.UpdateFragment { *; }
-keep public class net.hockeyapp.android.**
-keepclassmembers public class net.hockeyapp.android.** { *; }


# OkHttp
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**


# google
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.android.gms.**
-dontwarn android.support.v7.**
-keep class android.support.v7.** { *; }
-keep interface android.support.v7.** { *; }
-dontwarn android.support.design.**
-keep class android.support.design.** { *; }
-keep interface android.support.design.** { *; }
-keep public class android.support.design.R$* { *; }


## Nodes
-keep class dk.eboks.app.domain.models.** { *; }
-keep interface dk.eboks.app.domain.models.** { *; }
-keep class dk.nodes.nstack.** { *; }
-keep interface dk.nodes.nstack.** { *; }
-dontwarn org.apache.**
-dontwarn dk.nodes.**

## Translation

-keepclasseswithmembernames class * {
     dk.eboks.app.domain.models.Translation.* <method>;
 }
-keepclasseswithmembernames class * {
    @translate.* <fields>;
}
-keepclasseswithmembernames class * {
    @translate.* <methods>;
}


# Gson specific classes
##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic status information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { *; }

##---------------End: proguard configuration for Gson  ----------


# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-dontwarn dk.eboks.app.presentation.ui.**
