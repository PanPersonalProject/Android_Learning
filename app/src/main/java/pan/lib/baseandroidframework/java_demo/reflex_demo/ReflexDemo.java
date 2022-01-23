package pan.lib.baseandroidframework.java_demo.reflex_demo;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * AUTHOR Pan Created on 2022/1/23
 */
public class ReflexDemo {
    public static void main(String[] args) {
//        publicClassReflex();
        privateClassReflex();


    }

    private static void publicClassReflex() {
        try {
            Class<PackagePublicBean> reflexBeanClass = PackagePublicBean.class;
            PackagePublicBean reflexBean = reflexBeanClass.newInstance();
            reflexBean.test();
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    private static void privateClassReflex() {
        try {
            Class reflexBeanClass = Class.forName("pan.lib.baseandroidframework.java_demo.reflex_demo.reflex_bean.PackagePrivateBean");
            Constructor constructor = reflexBeanClass.getConstructors()[0];
            constructor.setAccessible(true); //使构造函数可以公开访问
            Object instance = constructor.newInstance();
            Method method = reflexBeanClass.getDeclaredMethod("test");
            method.setAccessible(true);//使函数可以公开访问
            method.invoke(instance);
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}


