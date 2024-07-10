package pan.lib.baseandroidframework.ui.main

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import org.jetbrains.anko.startActivity
import pan.lib.baseandroidframework.java_demo.dex_class_loader.DexClassLoaderUtil
import pan.lib.baseandroidframework.java_demo.dynamic_proxy.dynamicProxyExample
import pan.lib.baseandroidframework.ui.compose_views.MainScaffold
import pan.lib.baseandroidframework.ui.main.compose_demo.listview.ComposeListViewDemoActivity
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


    @Composable
    fun MenuList() {
        val context = LocalContext.current

        Column(
            modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = { startActivity<CustomerViewDemoListActivity>() }) {
                Text(text = "自定义View集合")
            }
            Button(onClick = { startActivity<RecyclerviewDemoActivity>() }) {
                Text(text = "RecyclerViewDiffUtil")
            }

            Button(onClick = { startActivity<ComposeListViewDemoActivity>() }) {
                Text(text = "ComposeListDemo")
            }

            Button(onClick = {
                DexClassLoaderUtil.loadApk(context)
            }) {
                Text(text = "DexClassLoader")
            }
            Button(onClick = { startActivity<LeakActivity>() }) {
                Text(text = "内存泄漏测试")
            }
            Button(onClick = { dynamicProxyExample() }) {
                Text(text = "动态代理测试")
            }

        }
    }


}
