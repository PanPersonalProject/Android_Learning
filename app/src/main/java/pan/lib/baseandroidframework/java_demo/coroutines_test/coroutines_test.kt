package pan.lib.baseandroidframework.java_demo.coroutines_test

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * AUTHOR Pan Created on 2022/1/31
 * result:MAIN into
   coroutines_into 14
   coroutines_IO
   coroutines_into2 16
 */

fun main() {
    GlobalScope.launch {
        println("coroutines_into "+Thread.currentThread().id)
        //线程的代码在到达suspend函数的时候被掐断,再回来的时候不一定还是原来的线程
        withContext(Dispatchers.IO){
            Thread.sleep(2000)
            println("coroutines_IO")
        }
        println("coroutines_into2 "+Thread.currentThread().id)
    }
    println("MAIN into "+Thread.currentThread().id)
    Thread.sleep(3000)
}