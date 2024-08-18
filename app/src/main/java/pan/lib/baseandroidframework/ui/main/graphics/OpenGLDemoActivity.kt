package pan.lib.baseandroidframework.ui.main.graphics

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import pan.lib.baseandroidframework.ui.theme.AndroidLearningTheme
import pan.lib.baseandroidframework.ui.views.opengl_es.CustomGLSurfaceView
import pan.lib.baseandroidframework.ui.views.opengl_es.LessonOneRenderer

class OpenGLDemoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidLearningTheme {
                OpenGLDemoScreen()
            }
        }

    }
}

@Composable
fun OpenGLDemoScreen() {
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            CustomGLSurfaceView(context).apply {
                init(LessonOneRenderer())
            }
        }
    )
}