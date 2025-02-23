package pan.lib.baseandroidframework.ui.main.compose_demo

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun TouchDemo() {
    Column(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState())
    ) {
        Text(text = "DraggableSample")
        DraggableSample()
        Text("ScrollableSample")
        ScrollableSample()
        Text("二维滑动监测")
        DragWithPointerInput()
    }
}

@Composable
fun DraggableSample() {
    Column {
        // 创建一个 MutableInteractionSource 用于跟踪交互状态
        val interactionSource = remember { MutableInteractionSource() }

        // 记住 offsetX 状态，用于记录水平偏移量
        var offsetX by remember { mutableFloatStateOf(0f) }

        // 记住 text 状态，用于显示文本
        var text by remember { mutableStateOf("拖动我") }

        // 显示文本并设置可拖动的修饰符
        Text(
            text,
            Modifier
                .offset { IntOffset(offsetX.roundToInt(), 0) } // 根据 offsetX 设置水平偏移量
                .draggable(
                    state = rememberDraggableState { delta ->
                        println("又移动了 $delta 个像素")
                        offsetX += delta // 更新 offsetX
                    },
                    orientation = Orientation.Horizontal, // 设置拖动方向为水平
                    interactionSource = interactionSource // 传递 interactionSource
                )
        )

        // 根据是否正在拖动更新文本
        val isDragged by interactionSource.collectIsDraggedAsState()
        text = if (isDragged) "正在拖动" else "停止拖动"
    }
}

@Composable
fun ScrollableSample() {
    Column {
        // 记住 offsetX 状态，用于记录水平偏移量
        var offsetX by remember { mutableFloatStateOf(0f) }

        // 记住 text 状态，用于显示文本
        var text by remember { mutableStateOf("滚动我") }

        // 显示文本并设置可滚动的修饰符
        Text(
            text,
            Modifier
                .offset { IntOffset(offsetX.roundToInt(), 0) } // 根据 offsetX 设置水平偏移量
                .scrollable(
                    state = rememberScrollableState { delta ->
                        println("又滚动了 $delta 个像素")
                        offsetX += delta // 更新 offsetX
                        delta // 返回消耗的滚动量
                    },
                    orientation = Orientation.Horizontal // 设置滚动方向为水平
                )
        )

        // 根据 offsetX 更新文本
        text = if (offsetX != 0f) "正在滚动" else "停止滚动"
    }
}


@Composable
fun DragWithPointerInput() {
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }

    Box(
        modifier = Modifier
            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()// 消费事件，防止继续传播
                    offsetX += dragAmount.x
                    offsetY += dragAmount.y
                }
            }
            .size(100.dp)
            .background(Color.Blue)
    ) {
        Text(
            text = "当前位置: x=${offsetX.toInt()}, y=${offsetY.toInt()}",
            color = Color.White
        )
    }
}