package pan.lib.baseandroidframework.ui.main.compose_demo

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun EffectDemo() {
    Column(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState())
    ) {
        Text(text = "SideEffect对比DisposeEffect")
        SideEffectCompareDisposeEffect()


    }
}


@Preview
@Composable
fun SideEffectCompareDisposeEffect() {
    var showText by remember { mutableStateOf(true) }

    SideEffect {
        //SideEffect里的代码在每次重组后都会执行
        Log.d("EffectDemo", "SideEffect 调用了")
    }


    DisposableEffect(showText) {
        // DisposableEffect里的代码在 showText 变化时执行
        Log.d("EffectDemo", "DisposableEffect 调用了")

        // onDispose 在 DisposableEffect 被移除时执行,当 showText 变化时，会先执行 onDispose，再执行 DisposableEffect
        onDispose {
            Log.d("EffectDemo", "onDispose 调用了")
        }
    }

    // 切换 showText 的值
    Button(onClick = { showText = !showText }) {
        Text("Click")
        if (showText) {
            Text(" showing")
        }
    }
}