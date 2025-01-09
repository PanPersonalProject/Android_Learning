package pan.lib.baseandroidframework.ui.main.compose_demo

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@Composable
fun EffectDemo() {
    Column(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState())
    ) {
        Text(text = "Effect对比")
        EffectCompare()
        Text("rememberUpdatedState")
        RememberUpdatedStateDemo()
        Text("CoroutineScope")
        CoroutineScopeDemo()
        Text("ProduceState")
        ProduceStateDemo()
        Text("snapshotFlow")
        SnapshotFlowDemo()

    }
}


@Preview
@Composable
fun EffectCompare() {
    var showText by remember { mutableStateOf(true) }

    SideEffect {
        //SideEffect里的代码在每次重组后都会执行
        Log.d("EffectDemo", "SideEffect 调用了")
    }

    DisposableEffect(showText) {
        // DisposableEffect里的代码在 showText 变化时执行
        Log.d("EffectDemo", "DisposableEffect 调用了")

        // onDispose 在 DisposableEffect 被移除时执行,当 showText 变化时，会先执行 onDispose，再执行 DisposableEffect
        onDispose {
            Log.d("EffectDemo", "onDispose 调用了")
        }
    }

    LaunchedEffect(showText) {
        //当 key 参数发生变化时，LaunchedEffect 会取消当前协程并启动一个新的协程
        Log.d("EffectDemo", "LaunchedEffect 调用了")

    }

    // 切换 showText 的值
    Button(onClick = { showText = !showText }) {
        Text("Click")
        if (showText) {
            Text(" showing")
        }
    }
}

@Composable
fun RememberUpdatedStateDemo() {
    var welcome by remember { mutableStateOf("欢迎光临！") }
    CustomLaunchedEffect(welcome)
    Button(onClick = { welcome = "不欢迎" }) {
        Text(welcome)
    }
}

/**
 *rememberUpdatedState相当于:
 *
 *val rememberedWelcome by remember { mutableStateOf(welcome) }
 *
 *rememberedWelcome=welcome
 */
@Composable
private fun CustomLaunchedEffect(welcome: String) {
    //如果这里不用rememberUpdatedState，再LaunchedEffect直接用welcome，且假设不希望重启LaunchedEffect
    //那么welcome的值在LaunchedEffect里是不会变化的
    val rememberedWelcome by rememberUpdatedState(welcome)

    LaunchedEffect(Unit) {
        delay(3000)
        Log.d("EffectDemo", "rememberedWelcome = $rememberedWelcome")
    }
}

/**
rememberCoroutineScope 和 LaunchedEffect 都是 Jetpack Compose 中用于处理协程的 API，但它们有不同的使用场景：

rememberCoroutineScope:
用于在组合函数中创建一个 CoroutineScope，该作用域的生命周期与组合函数的生命周期一致。
适用于在Composable作用域外面启动协程，并且希望手动控制协程的启动和取消的场景。

LaunchedEffect:
用于在组合函数中启动协程。
当 key 参数发生变化时，LaunchedEffect 会取消当前协程并启动一个新的协程。
适用于需要在状态变化时自动启动和取消协程的场景。
 */
@Composable
fun CoroutineScopeDemo() {
    val scope = rememberCoroutineScope()
    var showText by remember { mutableStateOf(true) }

    // 使用 rememberCoroutineScope 启动协程
    Button(onClick = {
        scope.launch {
            Log.d("CoroutineScopeDemo", "rememberCoroutineScope 启动的协程")
        }
    }) {
        Text("Click to launch coroutine")
    }

    // 使用 LaunchedEffect 启动协程
    LaunchedEffect(showText) {
        Log.d("CoroutineScopeDemo", "LaunchedEffect 启动的协程")
    }

    // 切换 showText 的值
    Button(onClick = { showText = !showText }) {
        Text("Toggle showText")
    }
}

private val dataFlow = MutableStateFlow("初始数据")

/**
 * 它用于将非 Compose 的异步或监听驱动的状态转换成 Compose 可以理解和响应的State
 *
 * ProduceStateDemo等同于下面写法的简化版：
 *
 *     val result = remember { mutableStateOf("加载中...") }
 *
 *     LaunchedEffect(Unit) {
 *         dataFlow.collect { result.value = it }
 *     }
 *
 *     Text(text = result.value)
 *
 * flow也有更便捷的写法：
 *
 *     val data by dataFlow.collectAsState(initial = "加载中...")
 * */
@Composable
fun ProduceStateDemo() {
    val data by produceState(initialValue = "加载中...") {
        dataFlow.collect { value = it }
    }

    Text(text = data)
}

/**
 * SnapshotFlowDemo 演示了如何使用 snapshotFlow 将多个 Compose 状态转换为 Flow
 */
@Composable
fun SnapshotFlowDemo() {
    var count by remember { mutableIntStateOf(0) }
    var text by remember { mutableStateOf("初始文本") }

    // 使用 snapshotFlow 将多个 Compose 状态转换为 Flow
    val combinedFlow = snapshotFlow { count to text }

    // 使用 LaunchedEffect 收集 Flow 的数据
    LaunchedEffect(Unit) {
        combinedFlow.collect { (countValue, textValue) ->
            Log.d("SnapshotFlowDemo", "收集到的 count: $countValue, text: $textValue")
        }
    }

    // UI 用于更新状态
    Column {
        Button(onClick = { count++ }) {
            Text("增加 count: $count")
        }
        Button(onClick = { text = "更新后的文本" }) {
            Text("更新文本: $text")
        }
    }
}