package pan.lib.baseandroidframework.java_demo.dynamic_proxy

import pan.lib.common_lib.utils.printSimpleLog

/**
 * @author pan qi
 * @since 2024/6/11
 */
class NormalServiceImpl : NormalService {
    override fun test() {
        printSimpleLog("调用了 test")
    }

    override fun test2() {
        printSimpleLog("调用了 test 2")
    }
}