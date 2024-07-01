package pan.lib.baseandroidframework.java_demo.coroutines_test

import kotlinx.coroutines.*

/**
 * 协程结构化取消演示
 *
 * 当父 CoroutineScope 被取消时，将会递归地取消所有其下属的子 CoroutineScope
 * 及它们所启动的任务。
 *
 * 输出结果：
 * Parent coroutine started
 * Child coroutine is working
 * Child coroutine is working
 * Cancelling parent coroutine
 * Parent coroutine is cancelled
 * Child coroutine is cancelled
 */

fun main() = runBlocking {
    val parentScope = launch {
        println("Parent coroutine started")

        val childScope = launch {
            try {
                repeat(10) {
                    println("Child coroutine is working")
                    delay(500L)
                }
            } catch (e: CancellationException) {
                println("Child coroutine is cancelled")
            }
        }

        delay(2000L) // 让子协程运行一段时间
        println("Parent coroutine is cancelling")
    }

    delay(1000L) // 让父协程运行一段时间
    println("Cancelling parent coroutine")
    parentScope.cancelAndJoin() // 取消父协程并等待它完成
    println("Parent coroutine is cancelled")
}
