package pan.lib.baseandroidframework.java_demo.dex_class_loader;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

/**
 * AUTHOR Pan Created on 2022/1/23
 */
public class DexClassLoaderUtil {
    public static void loadApk(Context context) {
        File apk = new File(context.getCacheDir() + "/26_pluginnable_plugin-debug.apk");
        try {
            InputStream is = context.getAssets().open("apk/26_pluginnable_plugin-debug.apk");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            FileOutputStream fos = new FileOutputStream(apk);
            fos.write(buffer);
            fos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            DexClassLoader dexClassLoader = new DexClassLoader(apk.getPath(), context.getCacheDir().getPath(), null, null);
            Class<?> utilClass = dexClassLoader.loadClass("com.hencoder.a26_pluginnable_plugin.Utils");
            Constructor constructor = utilClass.getConstructors()[0];
            Object instance = constructor.newInstance();
            Method method = utilClass.getDeclaredMethod("shout");
            method.invoke(instance);
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
