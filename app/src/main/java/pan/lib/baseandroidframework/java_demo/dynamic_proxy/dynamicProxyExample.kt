package pan.lib.baseandroidframework.java_demo.dynamic_proxy

import java.lang.reflect.Proxy

/**
 * @author pan qi
 * @since 2024/6/11
 */
fun dynamicProxyExample() {
    val normalService = NormalServiceImpl()

    val testInvocationHandler = TestInvocationHandler(normalService)
    val newProxyInstance = Proxy.newProxyInstance(
        normalService.javaClass.classLoader,
        normalService.javaClass.interfaces,
        testInvocationHandler
    ) as NormalService

    newProxyInstance.test()
    newProxyInstance.test2()
}