# SQLCipher loads native code and uses JNI callbacks by name.
-keep class net.zetetic.database.** { *; }
-keep class net.zetetic.** { *; }

# Keep line numbers for local, user-shared crash logs.
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile
