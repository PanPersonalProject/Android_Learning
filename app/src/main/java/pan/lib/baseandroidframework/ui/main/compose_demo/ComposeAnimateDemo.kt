package pan.lib.baseandroidframework.ui.main.compose_demo

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationEndReason
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.coroutines.cancellation.CancellationException


@Composable
fun AnimateDemos() {
    Column(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState())
    ) {
        Text(text = "animateDpAsState")
        AnimateDpAsStateDemo()
        Text(text = "Animatable:带动画初始值")
        AnimatableDemo()
        Text(text = "Reverse动画")
        ReverseDemo()
        Text(text = "惯性动画")
        AnimateDecayDemo()
        Text(text = "动画打断")
        AnimateInterruptDemo()
        Text(text = "边界动画")
        AnimateBoundDemo()
        Text(text = "Transaction动画")
        TransactionDemo()
        Text(text = "AnimatedVisibility动画")
        AnimatedVisibilityDemo()
    }
}

/*animateDpAsState状态切换是从A状态到B状态，不需要动画的初始值
animateDpAsState是Animatable的渐变场景的api，抛弃了一些功能。*/
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

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun AnimateBoundDemo() {
    var startAnimation by remember { mutableStateOf(false) }

    BoxWithConstraints {
        val anim = remember { Animatable(0.dp, Dp.VectorConverter) }
        val animY = remember { Animatable(0.dp, Dp.VectorConverter) }
        val decay = remember { exponentialDecay<Dp>() }

        if (startAnimation) {
            LaunchedEffect(Unit) {
                var result = anim.animateDecay(4000.dp, decay)
                while (result.endReason == AnimationEndReason.BoundReached) {
                    //动画到达边界，回弹回去的动画
                    result = anim.animateDecay(-result.endState.velocity, decay)
                }
            }
            //y轴惯性动画
            LaunchedEffect(Unit) {
                animY.animateDecay(2000.dp, decay)
            }
            //边界设置
            anim.updateBounds(0.dp, upperBound = maxWidth - 100.dp)
            animY.updateBounds(upperBound = maxHeight - 100.dp)
        }

        Box(
            Modifier
                .padding(anim.value, animY.value, 0.dp, 0.dp)
                .size(100.dp)
                .background(Color.Green)
                .clickable { startAnimation = true }
        )
    }
}


/*动画停止可分为异常停止和正常停止，其中打断和主动停止动画属于异常停止，
动画运行完成或达到边界后停止属于正常停止。*/
@Composable
fun AnimateInterruptDemo() {
    val anim = remember { Animatable(0.dp, Dp.VectorConverter) }
    val decay = remember { exponentialDecay<Dp>() }
    LaunchedEffect(Unit) {
        delay(1000)
        try {
            anim.animateDecay(2000.dp, decay)
        } catch (e: CancellationException) {
            Log.e("AnimateInterruptDemo", "动画被打断了", e)
        }
    }

    LaunchedEffect(Unit) {
        delay(1300)
        anim.animateDecay((-1000).dp, decay)//打断上一个动画
    }

    Box(
        Modifier
            .padding(anim.value, 0.dp, 0.dp, 0.dp)
            .size(100.dp)
            .background(Color.Green)
    )
//    LaunchedEffect(Unit) {
//        delay(1000)
//        anim.animateDecay(2000.dp, decay)
//    }
//    LaunchedEffect(Unit) {
//        delay(1300)
//        anim.stop()//停止动画
//    }
}

/*
多属性的状态切换，使用Transaction
Transition相比多个animateDpAsState，有两个优势：
1.方便管理，animation preview界面变量展示也能更清晰
2.性能更好，只会在一个协程更新*/
@Preview
@Composable
fun TransactionDemo() {
    var big by remember { mutableStateOf(false) }
    val bigTransition = updateTransition(targetState = big)
//    val bigTransition = rememberTransition(MutableTransitionState(big).apply { targetState = true })
    val size by bigTransition.animateDp(
        label = "size", //preview 动画标签
        transitionSpec = {
            if (false isTransitioningTo true) { //正在从false到true的过程中
                spring()
            } else {
                tween()
            }
        }
    ) { if (it) 200.dp else 100.dp }

    val corner by bigTransition.animateDp(label = "corner") { if (it) 0.dp else 20.dp }
    Box(
        Modifier
            .padding(bottom = 20.dp)
            .clip(RoundedCornerShape(corner))
            .size(size)
            .background(Color.Green)
            .clickable(onClick = {
                big = !big
            })
    )

}

@Composable
fun AnimatedVisibilityDemo() {
    var show by remember { mutableStateOf(false) }
    Row(
        Modifier.horizontalScroll(rememberScrollState())
    ) {
        AnimatedVisibility(show, enter = fadeIn(tween(2000), initialAlpha = 0.3f)) {
            Box(
                Modifier
                    .size(100.dp)
                    .background(Color.Green)

            )
        }

        AnimatedVisibility(show, enter = slideIn { IntOffset(-it.width, -it.height) }) {
            Box(
                Modifier
                    .padding(start = 20.dp)
                    .size(100.dp)
                    .background(Color.Green)

            )
        }

        AnimatedVisibility(show, enter = slideInHorizontally()) {
            Box(
                Modifier
                    .padding(start = 20.dp)
                    .size(100.dp)
                    .background(Color.Green)

            )
        }

        AnimatedVisibility(
            show,
            enter = expandIn(expandFrom = Alignment.TopStart, initialSize = {
                IntSize(it.width / 2, it.height / 2)
            })
        ) {
            Box(
                Modifier
                    .padding(start = 20.dp)
                    .size(100.dp)
                    .background(Color.Green)

            )
        }

    }

    Button(
        onClick = { show = !show },
        Modifier.padding(bottom = 20.dp)
    ) {
        Text(text = "Toggle")
    }

}