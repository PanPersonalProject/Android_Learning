package pan.lib.baseandroidframework.java_demo.coroutines_test


import kotlinx.coroutines.*
import kotlin.coroutines.*

/**
 * suspendCancellableCoroutine函数通常用于与回调式api合作，例如将回调风格的 API 转换为协程友好的挂起函数
 *一般可能是协程中调用java回调式api，想要代码风格线性处理的情况
 */

fun asyncOperation(callback: (Int?, Exception?) -> Unit) {
    GlobalScope.launch {
        try {
            delay(1000L)
            // 模拟异步操作成功返回结果
            callback(42, null)
        } catch (e: Exception) {
            // 模拟异步操作失败
            callback(null, e)
        }
    }
}

/**
 *  suspendCancellableCoroutine对比suspendCoroutine:
 * 当使用 suspendCancellableCoroutine 时，如果外部协程被取消，挂起的操作会自动取消，并调用相应的取消回调。
 */
suspend fun asyncOperationAsync(): Int = suspendCancellableCoroutine { continuation ->
    asyncOperation { result, exception ->
        if (exception != null) {
            // 异步操作失败，抛出异常
            continuation.resumeWithException(exception)
        } else if (result != null) {
            // 异步操作成功，恢复协程并返回结果
            continuation.resume(result)
        }
    }

    // 当协程被取消时执行的操作
    continuation.invokeOnCancellation {
        println("Coroutine was cancelled")
        // 在这里执行任何必要的清理操作
    }
}

fun main() = runBlocking { //runBlocking 是 Kotlin 协程库提供的一个阻塞式函数，用于启动一个新的协程并阻塞当前线程直到协程执行完毕。
    val job = launch {
        try {
            val result = asyncOperationAsync()
            println("Async operation result: $result")
        } catch (e: Exception) {
            println("Async operation failed: ${e.message}")
        }
    }

    val mockCancel = false // 模拟取消任务
    if (mockCancel) {
        // 取消任务的示例
        delay(500L)
        job.cancelAndJoin()
        println("Job was cancelled")
    }

}