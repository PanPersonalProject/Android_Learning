package pan.lib.baseandroidframework.ui.main.compose_demo

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.StateObject
import androidx.compose.runtime.staticCompositionLocalOf
import kotlin.collections.component1
import kotlin.collections.component2


/**
 *展示字符串长度demo，说明remember的用法
 * remember(msg) 表示当 msg 发生变化时，重新计算并记住其值。
 * 如果写成remember{ msg.length }，当ShowStrLength重组时，length不会重新计算。
 */
@Composable
fun ShowStrLength(msg: String) {
    val length = remember(msg) { msg.length }
    Text(text = "Length: $length")

    //也可以使用 rememberUpdatedState 确保 state 在 msg 变化时更新
    val lengthState = rememberUpdatedState(newValue = msg.length)
    Text(text = "Length: ${lengthState.value}")
}

/**
 *状态提升和单一信息源
 *原则1：状态能不往上提就不往上提
 * 假设把所有状态都提到最顶层，会导致以下问题：
 * 1. 代码复杂度增加：所有状态都集中在顶层组件，导致顶层组件变得复杂且难以维护。
 * 2. 状态管理困难：当状态过多时，管理和追踪状态的变化会变得困难。
 * 3. 性能问题：顶层组件的重组会影响其所有子组件，可能导致不必要的性能开销。
 * 4. 可读性降低：状态提升过多会使代码的可读性和理解难度增加。
 *
 * 因此，状态提升应根据实际需求进行，尽量保持状态的局部性。
 *
 * 原则2：单一信息源原则：ui当有多个数据源的时候，应该设计链式传递数据，遵守单一信息源原则。
 */
@Composable
fun ParentComposable() {
    var name by remember { mutableStateOf("pan") }
    Column {
        StateHoistingDemo(name)
    }

    /*  ui当有多个数据源的时候，应该遵守单一信息源原则，compose的官方ui都是遵守这个原则的
        比如TextField有2个数据源：用户输入和TextField的value。
        这2个数据源是单一链式传递的，不然在用户输入的时候，TextField的value也会发生变化*/
    TextField(value = name, onValueChange = { name = it })
}

//状态提升
@Composable
fun StateHoistingDemo(name: String) {
    Text(name)
}

//mutableStateOf无法监听集合的变化，因为集合的引用没有变化需要使用mutableStateListOf
@Composable
fun MutableStateCollectionsDemo() {
    val numbs = remember { mutableStateListOf(1, 2, 3) }
    val map = remember { mutableStateMapOf("key1" to "value1", "key2" to "value2") }

    Column {
        numbs.forEach {
            Text(text = it.toString())
        }
        Button(onClick = { numbs.add(numbs.size + 1) }) {
            Text("Add Number")
        }

        map.forEach { (key, value) ->
            Text(text = "$key: $value")
        }
        Button(onClick = { map["key${map.size + 1}"] = "value${map.size + 1}" }) {
            Text("Add Key-Value Pair")
        }
    }
}


@Composable
fun DerivedStateOfDemo() {
    var nameList = remember {
        mutableStateListOf("张三")
    }
    UpdateNameList(nameList = nameList) {
        nameList.add("李四")
    }
}


@Composable
fun UpdateNameList(nameList: List<String>, onClick: () -> Unit) {
    /*   nameList是StateObject
         remember观察nameList的引用的变化
         derivedStateOf观察StateObject nameList内部元素的变化*/
    val personList by remember(nameList) {
        derivedStateOf {
            nameList.map {
                "my name is $it"
            }
        }
    }
    Column {
        Button(onClick = {
            onClick.invoke()
        }) {
            Text(text = "add new name")
        }
        Text(text = "nameList is StateObject: ${nameList is StateObject}")

        for (person in personList) {
            Text(text = person)
        }
    }

}

/*
CompositionLocal 具有穿透函数功能的局部变量
把它构建成一个：不怕影响到更大范围的对象，也就是提供绝对正确的值 ——>共识
适用于：context 环境 theme
*/
val LocaleVersion = compositionLocalOf<String> {
    error("No version provided")
}

/*
与 compositionLocalOf 不同，Compose 不会跟踪 staticCompositionLocalOf 的读取。节省了跟踪的性能开销。
更改该值会导致提供 CompositionLocal 的整个 content lambda 被重组，而不仅仅是在组合中读取 current 值的位置。
如果为 CompositionLocal 提供的值发生更改的可能性微乎其微或永远不会更改，使用 staticCompositionLocalOf 可提高性能。
*/
val LocaleVersion2 = staticCompositionLocalOf<String> {
    error("No version provided")
}

@Composable
fun CompositionLocalDemo() {
    CompositionLocalProvider(LocaleVersion provides "1.0") {
        CompositionLocalChildDemo() // LocaleVersion: 1.0
        CompositionLocalProvider(LocaleVersion provides "1.1") {
            CompositionLocalChildDemo() // LocaleVersion: 1.1
        }
    }
}

@Composable
fun CompositionLocalChildDemo() {
    Text(text = "LocaleVersion: ${LocaleVersion.current}")
}