package pan.lib.baseandroidframework.java_demo.coroutines_test

import kotlinx.coroutines.*

/**
运行结果
Main thread: main
runBlocking: main
async: DefaultDispatcher-worker-1
Result: Hello, World!
Main thread after runBlocking: main*/

fun main() {
    println("Main thread: ${Thread.currentThread().name}")

    //runBlocking 会阻塞当前线程，并在同一个线程中运行其代码块
    val result = runBlocking {
        println(" runBlocking: ${Thread.currentThread().name}")
        val job = async(Dispatchers.Default) {
            delay(500L)
            println(" async: ${Thread.currentThread().name}")
            "Hello, World!"
        }
        job.await()
    }

    println("Result: $result")
    println("Main thread after runBlocking: ${Thread.currentThread().name}")
}
