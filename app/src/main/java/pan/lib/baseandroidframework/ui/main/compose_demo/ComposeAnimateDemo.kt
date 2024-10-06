package pan.lib.baseandroidframework.ui.main.compose_demo

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.animation.core.repeatable
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
        Text(text = "Reverse动画")
        ReverseDemo()
        Text(text = "惯性动画")
        AnimateDecayDemo()
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
- LinearEasing：匀速运行

block: 监听动画执行的每一帧
*/

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
            ),
            block = {
                //监听动画执行的每一帧
                value //当前动画值
            }
        )
    }
    Box(
        Modifier
            .padding(bottom = 20.dp)
            .size(animatable.value)
            .background(Color.Blue)
            .clickable(onClick = {
                big = !big
            })
    )
}


@Composable
fun ReverseDemo() {
    var big by remember { mutableStateOf(false) }
    val animatable = remember { Animatable(100.dp, Dp.VectorConverter) }

    LaunchedEffect(big) {
        animatable.animateTo(
            targetValue = if (big) 200.dp else 100.dp,
            animationSpec = repeatable(
                iterations = 3, // 重复次数，动画将重复3次
                animation = tween(), // 使用补间动画，可以指定动画的持续时间、延迟和速率曲线等
                repeatMode = RepeatMode.Reverse, // 反向重复模式，每次重复时，动画将反向播放
//                initialStartOffset = StartOffset(500, StartOffsetType.FastForward) // 动画开始前的延迟时间，500毫秒后快速前进到动画的开始位置
            )

            //infiniteRepeatable 无限重复
//            animationSpec = infiniteRepeatable(
//                animation = tween(), // 使用补间动画，可以指定动画的持续时间、延迟和速率曲线等
//                repeatMode = RepeatMode.Reverse, // 反向重复模式，每次重复时，动画将反向播放
//            )
//
        )
    }
    Box(
        Modifier
            .padding(bottom = 20.dp)
            .size(animatable.value)
            .background(Color.Blue)
            .clickable(onClick = {
                big = !big
            })
    )
}

/*
animateDecay：惯性衰减
最终值和初始速度相关，比如滑动列表的初始速度
和animateTo的区别？
animateTo是先确定移动距离，animateDecay不确定移动距离，根据速度决定最后位置和结束时间
*/
@Composable
fun AnimateDecayDemo() {
    var move by remember { mutableStateOf(false) }
    val animatable = remember { Animatable(0.dp, Dp.VectorConverter) }

//    样条曲线（spline）来计算衰减动画
//    面向像素，会根据屏幕密度转换为px，是强制修正
//    val decay=rememberSplineBasedDecay<Int>()

    //指数衰减算法来计算动画
    //不会根据屏幕密度，把dp转换px. Dp本身就会根据屏幕密度转换，所以不需要再转换
    val decay = remember {
        exponentialDecay<Dp>(
            //衰减摩擦系数
            frictionMultiplier = 1f,
            //最低速度，低于此速度时动画将被视为完成。必须大于 `0`
            absVelocityThreshold = 0.1f
        )
    }
    LaunchedEffect(move) {
        if (move) {
            animatable.animateDecay(
                initialVelocity = 1000.dp,
                animationSpec = decay
            )
        } else {
            animatable.stop()
            animatable.snapTo(0.dp)
        }
    }
    Box(
        Modifier
            .padding(start = animatable.value)
            .size(100.dp)
            .background(Color.Blue)
            .clickable(onClick = {
                move = !move
            })
    )
}
