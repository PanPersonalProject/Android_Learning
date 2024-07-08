package pan.lib.baseandroidframework.java_demo.flow_demo

import kotlinx.coroutines.runBlocking

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

import kotlin.system.*

fun simple(): Flow<Int> = flow {
    for (i in 1..3) {
        delay(100) // 假装我们异步等待了 100 毫秒
        emit(i) // 发射下一个值
    }
}

fun main() = runBlocking {
    val time = measureTimeMillis {
        simple()
            //使用buffer在流上使用 buffer 操作符来并发运行这个 simple 流中发射元素的代码以及收集的代码， 而不是顺序运行它们：
            .buffer()
            .collect { value ->
                delay(300) // 假装我们花费 300 毫秒来处理它
                println(value)
            }
    }
    println("Collected in $time ms")
}