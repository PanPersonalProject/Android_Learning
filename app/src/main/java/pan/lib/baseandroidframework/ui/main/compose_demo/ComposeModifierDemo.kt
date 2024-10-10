package pan.lib.baseandroidframework.ui.main.compose_demo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
Modifier是实现了Modifier接口的伴生对象，适合作为函数参数的第一个带默认值参数

then接口用于合并两个Modifier对象，返回一个CombinedModifier对象：
infix fun then(other: Modifier): Modifier =
if (other === Modifier) this else CombinedModifier(this, other)
 */
@Preview
@Composable
fun Custom(
    modifier: Modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
        .alpha(0.5f)
) {

    //检查Modifier链中是否存在至少一个Element满足给定的谓词predicate
    val hasPadding = modifier.any { it.toString().contains("padding") }

    //从左到右遍历Modifier链中的每个Element，并将每个Element应用到初始值initial上
    //Element实现Modifier接口,类似于是modifier的子节点，里面不会包含子Modifier
    val result = modifier.foldIn("") { acc, element ->
        val simpleName = element::class.simpleName ?: "Unknown"
        acc + simpleName + "\n"
    }
    Box(
        modifier = if (hasPadding) {
            modifier.background(Color.Green)
        } else {
            modifier.background(Color.White)
        }
    ) {
        Text(text = result)
    }
}


/**
 * CombinedModifier提取到外部，方便注释学习
 */
class CombinedModifier(
    internal val outer: Modifier,
    internal val inner: Modifier
) : Modifier {
    //从左到右遍历 Modifier 链中的每个 Element，并将每个 Element 应用到初始值 initial 上。 先加入的先应用
    override fun <R> foldIn(initial: R, operation: (R, Modifier.Element) -> R): R =
        inner.foldIn(outer.foldIn(initial, operation), operation)

    //作用: 从右到左遍历 Modifier 链中的每个 Element，并将每个 Element 应用到初始值 initial 上。后加入的先应用
    override fun <R> foldOut(initial: R, operation: (Modifier.Element, R) -> R): R =
        outer.foldOut(inner.foldOut(initial, operation), operation)


    //作用: 检查 Modifier 链中是否存在至少一个 Element 满足给定的谓词 predicate。
    override fun any(predicate: (Modifier.Element) -> Boolean): Boolean =
        outer.any(predicate) || inner.any(predicate)

    //作用: 检查 Modifier 链中的所有 Element 是否都满足给定的谓词 predicate。
    override fun all(predicate: (Modifier.Element) -> Boolean): Boolean =
        outer.all(predicate) && inner.all(predicate)


    override fun toString() = "[" + foldIn("") { acc, element ->
        if (acc.isEmpty()) element.toString() else "$acc, $element"
    } + "]"
}