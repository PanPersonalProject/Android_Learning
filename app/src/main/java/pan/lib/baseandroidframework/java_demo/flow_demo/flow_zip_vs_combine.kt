package pan.lib.baseandroidframework.java_demo.flow_demo

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*


fun main() = runBlocking {
    zip()
    combine()
}

/*
打印结果:
1 -> one at 452 ms from start
2 -> one at 651 ms from start
2 -> two at 854 ms from start
3 -> two at 952 ms from start
3 -> three at 1256 ms from start
*/
private suspend fun combine() {
    val nums = (1..3).asFlow().onEach { delay(300) } // 发射数字 1..3，间隔 300 毫秒
    val strs = flowOf("one", "two", "three").onEach { delay(400) } // 每 400 毫秒发射一次字符串
    val startTime = System.currentTimeMillis() // 记录开始的时间
    nums.combine(strs) { a, b -> "$a -> $b" } // 使用“combine”组合单个字符串
        .collect { value -> // 收集并打印
            println("$value at ${System.currentTimeMillis() - startTime} ms from start")
        }
}


/*
打印结果:
1 -> one
2 -> two
3 -> three
*/
private suspend fun zip() {
    val nums = (1..3).asFlow() // 数字 1..3
    val strs = flowOf("one", "two", "three") // 字符串

    nums.zip(strs) { a, b -> "$a -> $b" } // 组合单个字符串
        .collect { println(it) } // 收集并打印
}