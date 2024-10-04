package pan.lib.baseandroidframework.ui.main.compose_demo

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun AnimateDemos() {
    Column {
        Text(text = "animateDpAsState")
        AnimateDpAsStateDemo()
        Text(text = "Animatable:带动画初始值")
        AnimatableDemo()
    }
}

@Composable
fun AnimateDpAsStateDemo() {
    var big by remember { mutableStateOf(false) }
    //animateDpAsState是Status，不是 MutableState，不能直接set value
    //当big改变时，size会开启一个协程，从当前值到目标值进行计算
    val size by animateDpAsState(
        targetValue = if (big) 200.dp else 100.dp,
//        animationSpec = spring(Spring.DampingRatioMediumBouncy)//弹簧效果，会回弹
        animationSpec = spring(
            dampingRatio = 0.2f, // 阻尼比，控制弹簧的弹性
            stiffness = Spring.StiffnessHigh, // 弹簧的刚度。刚度越高，弹簧越不容易拉伸，动画会更快地稳定下来。
            visibilityThreshold = 5.dp // 可见性阈值，决定触发动画的最小变化值
        )
    )
    Box(
        Modifier
            .padding(bottom = 20.dp)
            .size(size)
            .background(Color.Green)
            .clickable(onClick = {
                big = !big
            })
    )
}

/*
Animatable是一个可动画的值，可以用来实现更复杂的动画效果
Easing:
- FastOutSlowInEasing：先加速后减速，对应属性动画插值器的 FastOutSlowInInterpolator
- LinearOutSlowInEasing：先匀速后减速，对应属性动画插值器的 LinearOutSlowInInterpolator
- FastOutLinearInEasing：先加速后匀速，对应属性动画插值器的 FastOutLinearInInterpolator
- LinearEasing：匀速运行*/
@Composable
fun AnimatableDemo() {
    var big by remember { mutableStateOf(false) }
    //animateDpAsState是Status，不是 MutableState，不能直接set value
    //当big改变时，size会开启一个协程，从当前值到目标值进行计算
    val animatable = remember { Animatable(100.dp, Dp.VectorConverter) }
    LaunchedEffect(big) {//当big改变时，开启一个协程
        animatable.snapTo(if (big) 400.dp else 0.dp) //snapTo是立即到达目标值，相当于动画初始值
        animatable.animateTo(
            targetValue = if (big) 200.dp else 100.dp,
            animationSpec = tween(
                durationMillis = 1000,
                easing = FastOutLinearInEasing //动画运行速率曲线
            )

        )
    }
    Box(
        Modifier
            .size(animatable.value)
            .background(Color.Blue)
            .clickable(onClick = {
                big = !big
            })
    )
}