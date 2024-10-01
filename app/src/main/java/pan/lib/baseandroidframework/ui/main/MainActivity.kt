package pan.lib.baseandroidframework.ui.main

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import pan.lib.baseandroidframework.java_demo.dex_class_loader.DexClassLoaderUtil
import pan.lib.baseandroidframework.java_demo.dynamic_proxy.dynamicProxyExample
import pan.lib.baseandroidframework.ui.compose_views.MainScaffold
import pan.lib.baseandroidframework.ui.main.compose_demo.listview.ComposeListViewDemoActivity
import pan.lib.baseandroidframework.ui.main.graphics.GpuImageActivity
import pan.lib.baseandroidframework.ui.main.graphics.OpenGLDemoActivity
import pan.lib.baseandroidframework.ui.theme.AndroidLearningTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainScreen()
        }
    }

    @Preview(
        uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL,
        backgroundColor = 0xFFFFEAEE,
        showBackground = true
    )
    @Composable
    private fun MainScreen() {
        AndroidLearningTheme {
            MainScaffold(
                title = "AndroidLearning",
                needShowArrowBack = false,
            ) {
                MenuList()
            }
        }
    }
}

enum class MenuState {
    HOME,
    GRAPHICS,
    COMPOSE
}


@Composable
fun MenuList() {
    val menuState = remember { mutableStateOf(MenuState.HOME) }

    Column(
        modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (menuState.value) {
            MenuState.HOME -> HomeMenu(menuState)
            MenuState.GRAPHICS -> GraphicsMenu(menuState)
            MenuState.COMPOSE -> ComposeMenu(menuState)
        }
    }
}

@Composable
fun HomeMenu(menuState: MutableState<MenuState>) {
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            context.startActivity(Intent(context, CustomerViewDemoListActivity::class.java))
        }) {
            Text(text = "自定义View集合")
        }
        Button(onClick = {
            context.startActivity(Intent(context, RecyclerviewDemoActivity::class.java))
        }) {
            Text(text = "RecyclerViewDiffUtil")
        }

        Button(onClick = { menuState.value = MenuState.COMPOSE }) {
            Text(text = "ComposeDemo")
        }
        Button(onClick = { menuState.value = MenuState.GRAPHICS }) {
            Text(text = "图形学Demo")
        }
        Button(onClick = {
            DexClassLoaderUtil.loadApk(context)
        }) {
            Text(text = "DexClassLoader")
        }
        Button(onClick = {
            context.startActivity(Intent(context, LeakActivity::class.java))
        }) {
            Text(text = "内存泄漏测试")
        }
        Button(onClick = { dynamicProxyExample() }) {
            Text(text = "动态代理测试")
        }
    }
}

@Composable
fun GraphicsMenu(menuState: MutableState<MenuState>) {
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            context.startActivity(Intent(context, OpenGLDemoActivity::class.java))
        }) {
            Text(text = "OpenGLDemo")
        }
        Button(onClick = {
            context.startActivity(Intent(context, GpuImageActivity::class.java))
        }) {
            Text(text = "GPUImageDemo")
        }
        Button(onClick = { menuState.value = MenuState.HOME }) {
            Text(text = "返回")
        }
    }
}

@Composable
fun ComposeMenu(menuState: MutableState<MenuState>) {
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            context.startActivity(Intent(context, ComposeListViewDemoActivity::class.java))
        }) {
            Text(text = "ComposeListDemo")
        }
        Button(onClick = { menuState.value = MenuState.HOME }) {
            Text(text = "返回")
        }
    }
}