package pan.lib.baseandroidframework.ui.main.compose_demo

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
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

@Preview
@Composable
fun ComposedModifierDemo() {
    Column {
        Box(Modifier.background(Color.Blue) then Modifier.customPaddingModifier())
        Text(
            "Hello, World!",
            Modifier.background(Color.Green) then Modifier.customPaddingModifier()
        )
    }
}

/**
Modifier.composed()用于创建独立的有状态的 Modifier，
所谓「独立」就是它会生成多个 Modifier 对象，互不影响。
ComposedModifier 主要用途是：
1.封装带状态的 Modifier。
2.封装的 Modifier 需要 Compose 环境。*/
fun Modifier.customPaddingModifier(): Modifier = composed {
    var padding by remember { mutableStateOf(8.dp) }
    Modifier
        .padding(padding)
        .clickable { padding += 8.dp }
}

/*Modifier.layout() 的用处是修改对应组件的尺寸与位置偏移
它适用于给组件增加装饰效果。 所谓「装饰效果」就是不干涉组件内部测量布局，只在外部调整，例如 padding。
Compose 每一个组件最终都会生成 LayoutNode 对象，作为这个组件的代表存在内存中。
它们的测量与布局由 LayoutNode.remeasure() 和 LayoutNode.replace() 方法负责，
其实前者已经做完了所有工作，包括布局的计算，后者只是用计算的结果布局一下。*/
@Preview
@Composable
fun LayoutModifierDemo() {
    Box(
        Modifier
            .background(Color.Green)
    ) {  // 背景为绿色的容器
        Text(
            "Hello, World!",
            Modifier
                .layout { measurable, constraints ->
                    // 将 padding 设置为 10dp，并转换为像素单位
                    var padding = 10.dp.roundToPx()

                    // 测量文本组件的实际大小，同时对其进行约束调整，左右、上下留出 padding 空间
                    val placeable = measurable.measure(
                        constraints.copy(
                            maxWidth = constraints.maxWidth - padding * 2,  // 水平方向左右各留出 padding
                            maxHeight = constraints.maxHeight - padding * 2  // 垂直方向上下各留出 padding
                        )
                    )

                    // 通过 layout 函数设置组件最终的宽度和高度
                    layout(placeable.width + padding * 2, placeable.height + padding * 2) {
                        // 将测量的元素放置在指定位置，这里是 (padding, padding) 的位置
                        placeable.place(padding, padding)
                    }
                }
                .background(Color.Blue)  // 在 layout 调整后的文本上添加蓝色背景
        )
    }
}

/*
跟踪LayoutNodeLayoutDelegate.performMeasure()会发现：
    Modifier后添加的子LayoutNode会先被调用，然后再调用之前的父LayoutNode。

例如：
Box(Modifier.padding(10.dp).padding(20.dp)) //最终的padding是30dp
生成的 outerWrapper 是这样的：
ModifiedLayoutNode(
  modifier = PaddingModifier(10.dp),
  wrapped = ModifiedLayoutWrapper(
    modifier = PaddingModifier(20.dp),
    wrapped = InnerPlaceable
  )
)
*/

@Preview
@Composable
fun LayoutModifierDemo2() {
    //最终结果是50dp，父LayoutNode要求子LayoutNode的上限是50dp，子LayoutNode虽然写了100dp，但是会服从父LayoutNode
    Box(
        Modifier
            .size(50.dp)
            .size(100.dp)
            .background(Color.Green)
    )

}