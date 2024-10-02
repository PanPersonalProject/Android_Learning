package pan.lib.baseandroidframework.ui.main

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import pan.lib.baseandroidframework.java_demo.dex_class_loader.DexClassLoaderUtil
import pan.lib.baseandroidframework.java_demo.dynamic_proxy.dynamicProxyExample
import pan.lib.baseandroidframework.ui.compose_views.MainScaffold
import pan.lib.baseandroidframework.ui.main.MainActivity.NavRoutes
import pan.lib.baseandroidframework.ui.main.compose_demo.ComposeRecompositionDemo
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


    object NavRoutes {
        const val HOME = "home"
        const val GRAPHICS = "graphics"
        const val COMPOSE = "compose"
        const val COMPOSE_RECOMPOSITION_DEMO = "compose_recomposition_demo"
    }

    @Preview(
        uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL,
        backgroundColor = 0xFFFFEAEE,
        showBackground = true
    )
    @Composable
    private fun MainScreen() {
        val navController = rememberNavController()
        var needShowArrowBack by remember { mutableStateOf(false) }

        LaunchedEffect(navController) {
            navController.addOnDestinationChangedListener { _, _, _ ->
                needShowArrowBack = navController.previousBackStackEntry != null
            }
        }

        AndroidLearningTheme {
            MainScaffold(
                title = "AndroidLearning",
                needShowArrowBack = needShowArrowBack,
                onNavigateBack = { navController.popBackStack() }
            ) {
                NavHost(
                    navController = navController, startDestination = NavRoutes.HOME,
                    enterTransition = {
                        fadeIn(
                            animationSpec = tween(
                                300, easing = LinearEasing
                            )
                        ) + slideIntoContainer(
                            animationSpec = tween(200, easing = EaseIn),
                            towards = AnimatedContentTransitionScope.SlideDirection.Start
                        )
                    },
                    exitTransition = {
                        fadeOut(
                            animationSpec = tween(
                                300, easing = LinearEasing
                            )
                        ) + slideOutOfContainer(
                            animationSpec = tween(200, easing = EaseOut),
                            towards = AnimatedContentTransitionScope.SlideDirection.End
                        )
                    }
                ) {
                    composable(NavRoutes.HOME) { HomeMenu(navController) }
                    composable(NavRoutes.GRAPHICS) { GraphicsMenu() }
                    composable(NavRoutes.COMPOSE) { ComposeMenu(navController) }
                    composable(NavRoutes.COMPOSE_RECOMPOSITION_DEMO) { ComposeRecompositionDemo() }
                }
            }
        }
    }

}

@Composable
fun HomeMenu(navController: NavHostController) {
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

        Button(onClick = { navController.navigate(NavRoutes.COMPOSE) }) {
            Text(text = "ComposeDemo")
        }
        Button(onClick = { navController.navigate(NavRoutes.GRAPHICS) }) {
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
fun GraphicsMenu() {
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
    }
}

@Composable
fun ComposeMenu(navController: NavHostController) {
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

        Button(onClick = {
            navController.navigate(NavRoutes.COMPOSE_RECOMPOSITION_DEMO)
        }) {
            Text(text = "ComposeRecompositionDemo")
        }
    }
}