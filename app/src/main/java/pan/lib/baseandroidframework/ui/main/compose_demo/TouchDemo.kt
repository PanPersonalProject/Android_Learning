package pan.lib.baseandroidframework.ui.main.compose_demo

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import pan.lib.baseandroidframework.R
import kotlin.math.roundToInt

@Composable
fun TouchDemo() {
    Column(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Text(text = "DraggableSample")
        DraggableSample()
        Text("ScrollableSample")
        ScrollableSample()
        Text("二维滑动监测")
        DragWithPointerInput()
        Text("触摸事件底层实现")
        TouchClickEventDemo()
        Text("触摸事件传递")
        PointerEventPassDemo()
        Text("多指手势")
        GestureImage()


    }
}

@Composable
fun TouchClickEventDemo() {
    Text(
        text = "点击触发Click事件",
        modifier = Modifier
            //  detectTapGestures {  } 也可以实现点击事件，会更简单
            .myClick {
                Log.e("TouchClickEventDemo", "点击了")
            }
            .background(Color.Blue)
            .height(48.dp)
            .fillMaxWidth()
            .padding(8.dp),
        color = Color.White
    )
}

@Composable
private fun Modifier.myClick(onClick: () -> Unit) = pointerInput(Unit) {

    awaitEachGesture {
        awaitFirstDown()
        while (true) {
            val event = awaitPointerEvent()
            when (event.type) {
                // 按下事件
                PointerEventType.Move -> {
                    val pos = event.changes[0].position
                    if (pos.x < 0 || pos.x > size.width || pos.y < 0 || pos.y > size.height) {
                        break //break代表一次触摸事件流程结束，后面的Release事件不会再触发
                    }

                }

                PointerEventType.Release -> {
                    //最后一根手指抬起，才触发点击事件
                    if (event.changes.size == 1) {
                        onClick()
                    }
                    break
                }

            }
            waitForUpOrCancellation()
        }
    }
}

/**指针事件在这三个组合项中流动三次，分别在三个“阶段”中：

Modifier.pointerInput(Unit) {
awaitPointerEventScope {
val eventOnInitialPass = awaitPointerEvent(PointerEventPass.Initial)
val eventOnMainPass = awaitPointerEvent(PointerEventPass.Main) // default
val eventOnFinalPass = awaitPointerEvent(PointerEventPass.Final)
}
}
1.Initial 阶段：

在 Initial 阶段，事件从 UI 树的顶部流向底部。这个阶段允许父组件在子组件消费事件之前拦截事件。例如，工具提示需要拦截长按事件，而不是将其传递给子组件。在我们的示例中，ListItem 会在 Button 之前接收到事件。

2.Main 阶段：

在 Main 阶段，事件从 UI 树的叶节点流向 UI 树的根节点。这个阶段是你通常消费手势的地方，也是监听事件的默认阶段。在这个阶段处理手势意味着叶节点优先于它们的父节点，这对于大多数手势来说是最合乎逻辑的行为。在我们的示例中，Button 会在 ListItem 之前接收到事件。

3.Final 阶段：

在 Final 阶段，事件再次从 UI 树的顶部流向叶节点。这个阶段允许堆栈中更高的元素响应其父组件对事件的消费。例如，当按钮的按压变成其可滚动父组件的拖动时，按钮会移除其波纹效果。*/
@Composable
fun PointerEventPassDemo() {
    // 控制父组件是否拦截事件的状态
    var parentShouldIntercept by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(Color.LightGray)
            // 父组件的 pointerInput 修饰符
            .pointerInput(parentShouldIntercept) {
                awaitEachGesture {
                    while (true) {
                        // 在 Initial 阶段处理事件，父组件根据条件决定是否拦截事件
                        val event = awaitPointerEvent(PointerEventPass.Initial)
                        if (parentShouldIntercept && event.changes.any { it.pressed }) {
                            // 父组件拦截并消耗事件
                            event.changes.forEach { it.consume() }
                            println("父组件拦截并消耗事件")
                        }
                    }
                }
            }
    ) {
        // 切换父组件是否拦截事件的按钮
        Button(onClick = { parentShouldIntercept = !parentShouldIntercept }) {
            Text(text = if (parentShouldIntercept) "父组件拦截事件中" else "父组件未拦截事件")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .size(100.dp)
                .background(Color.Blue)
                // 子组件的 pointerInput 修饰符
                .pointerInput(Unit) {
                    awaitEachGesture {
                        while (true) {
                            // 在 Main 阶段处理事件，子组件尝试消费未被消耗的事件
                            val event = awaitPointerEvent(PointerEventPass.Main)
                            if (event.changes.any { it.pressed && !it.isConsumed }) {
                                // 子组件处理未被消耗的事件
                                println("子组件消费事件")
                            }
                        }
                    }
                }
        )
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

@Composable
fun GestureImage() {
    val scale = remember { mutableFloatStateOf(1f) }
    val offsetX = remember { mutableFloatStateOf(0f) }
    val offsetY = remember { mutableFloatStateOf(0f) }
    val rotation = remember { mutableFloatStateOf(0f) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray)
            .pointerInput(Unit) {
                // 检测变换手势，包括缩放、平移和旋转
                detectTransformGestures { _, pan, zoom, rotationChange ->
                    // 更新缩放比例
                    scale.floatValue *= zoom
                    // 更新水平偏移量
                    offsetX.floatValue += pan.x
                    // 更新垂直偏移量
                    offsetY.floatValue += pan.y
                    // 更新旋转角度
                    rotation.floatValue += rotationChange
                }
            }
    ) {
        Image(
            painter = painterResource(id = R.mipmap.bj),
            contentDescription = null,
            modifier = Modifier
                .size(150.dp)
                .graphicsLayer(
                    scaleX = scale.floatValue, // 应用水平缩放
                    scaleY = scale.floatValue, // 应用垂直缩放
                    translationX = offsetX.floatValue, // 应用水平平移
                    translationY = offsetY.floatValue, // 应用垂直平移
                    rotationZ = rotation.floatValue // 应用旋转
                ),
            contentScale = ContentScale.Fit
        )
    }
}

