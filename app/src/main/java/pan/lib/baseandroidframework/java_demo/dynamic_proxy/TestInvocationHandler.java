package pan.lib.baseandroidframework.java_demo.dynamic_proxy;

import static pan.lib.common_lib.utils.LogUtilKt.printSimpleLog;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class TestInvocationHandler implements InvocationHandler { // 定义了一个持有目标对象的 TestInvocationHandler 类，实现 InvocationHandler 接口

    private final Object target; // 声明一个目标对象

    public TestInvocationHandler(Object target) {
        this.target = target; // 构造函数初始化目标对象
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable { // 实现了 invoke 方法，这是动态代理的核心
        printSimpleLog("Before calling method: " + method.getName()); // 在调用目标方法前打印日志
        Object result = method.invoke(target, args); // 通过反射调用目标对象的 method 方法，并传递参数
        printSimpleLog("After calling method: " + method.getName()); // 在调用目标方法后打印日志
        return result; // 返回方法调用的结果
    }
}
