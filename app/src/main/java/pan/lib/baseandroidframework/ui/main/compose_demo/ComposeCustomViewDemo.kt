package pan.lib.baseandroidframework.ui.main.compose_demo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview
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