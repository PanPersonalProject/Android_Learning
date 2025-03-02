package pan.lib.baseandroidframework.ui.main.compose_demo

import android.content.res.Configuration
import android.widget.TextView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL,
    backgroundColor = 0xFFFFEAEE,
    showBackground = true
)
@Composable
fun AndroidViewInCompose() {
    var text by remember { mutableStateOf("这是一个 Android View") }

    // Simulate a text change after some time or based on some event
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(2000)
        text = "文本已更新"
    }

    AndroidView(
        factory = { context ->
            TextView(context).apply {
                this.text = text
            }
        },
        update = { view ->
            view.text = text
        }
    )
}