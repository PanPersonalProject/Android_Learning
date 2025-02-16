package pan.lib.baseandroidframework.ui.main.compose_demo

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun CustomViewDemo() {
    Text(text = "自定义垂直布局")
    CustomVerticalLayout {
        Box(
            Modifier
                .background(androidx.compose.ui.graphics.Color.Red)
                .size(100.dp)
        )
        Box(
            Modifier
                .background(androidx.compose.ui.graphics.Color.Green)
                .size(100.dp)
        )
        Box(
            Modifier
                .background(androidx.compose.ui.graphics.Color.Blue)
                .size(100.dp)
        )
    }
}

@Composable
fun CustomVerticalLayout(modifier: Modifier = Modifier, content: @Composable () -> Unit) {

    Layout(content, modifier) { measurables, constraints ->
        val placeables = measurables.map { measurable ->
            measurable.measure(constraints)
        }

        val height = placeables.sumOf { it.height } //计算高度
        val width = placeables.maxOf { it.width }// 宽度取最大值

        layout(width, height) {
            var yPosition = 0
            placeables.forEach { placeable ->
                //用于在布局中放置子元素。它将子元素放置在相对于父布局的 (0, yPosition) 位置，其中 x 坐标为 0，y 坐标为 yPosition。
                placeable.placeRelative(0, yPosition)
                yPosition += placeable.height
            }
        }
    }
}


/**
 * =================================================================================================
 * 正常流程 (默认)                          |  SubcomposeLayout 流程 (延迟组合)
 * =================================================================================================
 * 1. 组合阶段(Composition):                |  1. 初始组合阶段(Initial Composition):
 *    - 描述: 组合所有子组件，无论是否需要显示。 |     - 描述: 只组合 SubcomposeLayout 自身，子组件延迟组合。
 * -----------------------------------------|------------------------------------------------------
 * 2. 布局阶段:                             |  2. 布局阶段(Layout):
 *    - 描述: 测量和布局所有子组件，无论是否可见。|     - 描述: 动态组合和测量需要的子组件。
 * -----------------------------------------|------------------------------------------------------
 * 3. 绘制阶段:                             |  3. 绘制阶段(Drawing):
 *    - 描述: 只绘制可见组件，但组合和布局已完成 |     - 描述: 只绘制可见组件，且组合和布局仅处理必要组件。
 *            所有工作。                     |
 * =================================================================================================
 * 问题:                                    |  优点:
 * - 性能浪费: 不必要的组合和测量操作。       |  - 性能优化: 避免不必要的组合和测量。
 * - 不适合动态场景: 无法根据条件动态决定是否  |  - 动态性: 根据条件或布局约束动态决定是否组合某些组件。
 *   组合某些组件。                          |
 * =================================================================================================
 * 适用场景:                                 |  适用场景:
 * - 列表或网格: 只组合和测量当前可见的项。   |  - 列表或网格: 只组合和测量当前可见的项。
 * - 条件渲染: 根据条件动态决定是否组合某些组件。|  - 条件渲染: 根据条件动态决定是否组合某些组件。
 * - 复杂布局: 根据布局约束动态调整子组件。    |  - 复杂布局: 根据布局约束动态调整子组件。
 * =================================================================================================
 */


@Composable
fun CustomLazyColumn(
    items: List<Any>,
    itemContent: @Composable (Any) -> Unit
) {

    SubcomposeLayout { constraints ->
        // 用于存储每个项目的高度
        val itemHeights = mutableListOf<Int>()
        // 用于存储每个项目的测量结果
        val placeables = mutableListOf<Placeable>()

        // 计算每个项目的高度
        items.forEach { item ->
            //使用subcompose 函数，只对 可见范围内的 item 进行组合。 这一点Layout无法实现，layout会对所有的item进行组合
            val placeable = subcompose(item) {
                itemContent(item)
            }.first().measure(Constraints())
            itemHeights.add(placeable.height)
            placeables.add(placeable)
        }

        // 计算总高度
        val totalHeight = itemHeights.sum()

        // 设置布局的宽高
        layout(constraints.maxWidth, totalHeight) {
            var yOffset = 0

            placeables.forEachIndexed { index, placeable ->
                // 仅布局在屏幕内的项目
                if (yOffset + placeable.height <= constraints.maxHeight) {
                    placeable.placeRelative(0, yOffset)
                }
                yOffset += placeable.height
            }
        }
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL,
    backgroundColor = 0xFFFFEAEE,
    showBackground = true
)
@Composable
fun ExampleUsage() {
    val items = List(100) { "Item $it" }

    CustomLazyColumn(items = items) { item ->
        Box(modifier = Modifier.fillMaxSize()) {
            // 这里可以自定义每个项目的内容
            Text(text = item.toString())
        }
    }
}