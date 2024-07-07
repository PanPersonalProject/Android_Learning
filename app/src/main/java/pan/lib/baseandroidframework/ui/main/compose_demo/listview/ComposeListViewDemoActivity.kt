package pan.lib.baseandroidframework.ui.main.compose_demo.listview

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import pan.lib.baseandroidframework.R
import pan.lib.baseandroidframework.ui.compose_views.MainScaffold
import pan.lib.baseandroidframework.ui.theme.AndroidLearningTheme
import pan.lib.common_lib.utils.printSimpleLog

/*
 * compose列表
 * 使用paging实现分页功能
 * 使用PullToRefreshBox实现下拉刷新
 * compose paging文档：https://developer.android.com/reference/kotlin/androidx/paging/compose/package-summary
 */
class ComposeListViewDemoActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainScreen()
        }
    }


    @Composable
    private fun MainScreen(viewModel: ListViewModel = viewModel()) {
        AndroidLearningTheme {
            MainScaffold(
                title = "列表(支持下拉刷新和上拉加载)",
                onNavigateBack = { finish() }) {
                val messages = viewModel.messages.collectAsLazyPagingItems()
                when (messages.loadState.refresh) {

                    is LoadState.Error -> {
                        val message = (messages.loadState.refresh as LoadState.Error).error.message
                        Text(text = "Load Error: $message")
                    }

                    is LoadState.NotLoading, is LoadState.Loading -> {
                        Conversation()
                    }
                }
                printSimpleLog("loadState: ${messages.loadState.refresh}")

            }
        }
    }

    @Composable
    fun MessageCard(msg: Message) {
        Row(modifier = Modifier.padding(all = 8.dp)) {
            Image(
                painter = painterResource(R.mipmap.bj),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))

            var isExpanded by remember { mutableStateOf(false) }
            // surfaceColor will be updated gradually from one color to the other
            val surfaceColor by animateColorAsState(
                if (isExpanded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                label = "",
            )

            Column(modifier = Modifier.clickable { isExpanded = !isExpanded }) {
                Text(
                    text = msg.author,
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.titleSmall
                )

                Spacer(modifier = Modifier.height(4.dp))

                Surface(
                    shape = MaterialTheme.shapes.medium,
                    shadowElevation = 1.dp,
                    // surfaceColor color will be changing gradually from primary to surface
                    color = surfaceColor,
                    // animateContentSize will change the Surface size gradually
                    modifier = Modifier
                        .animateContentSize()
                        .padding(1.dp)
                ) {
                    Text(
                        text = msg.body,
                        modifier = Modifier.padding(all = 4.dp),
                        maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Conversation(viewModel: ListViewModel = viewModel()) {
        val messages = viewModel.messages.collectAsLazyPagingItems()
        var isRefreshing by remember { mutableStateOf(false) }

        LaunchedEffect(messages.loadState.refresh) {// 监听刷新状态
            isRefreshing = messages.loadState.refresh is LoadState.Loading // 刷新状态
        }

        PullToRefreshBox(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally)
                .padding(5.dp),
            isRefreshing = isRefreshing,
            onRefresh = {
                printSimpleLog("onRefresh")
                messages.refresh()
            },
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                items(messages.itemCount) { index ->
                    val item = messages[index]
                    if (item != null) {
                        MessageCard(item)
                    }
                }

                if (messages.loadState.append == LoadState.Loading) {
                    item {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentWidth(Alignment.CenterHorizontally)
                        )
                    }
                }
            }
        }
    }


    @Preview(name = "Light Mode")
    @Preview(
        uiMode = UI_MODE_NIGHT_YES,
        showBackground = true,
        name = "Dark Mode"
    )
    @Composable
    fun MainScreenPreview() {
        val messages = remember { mutableStateListOf<Message>() }
        val list = List(20) { index ->
            Message(
                author = "用户${index + 1}",
                body = "这是一条示例消息${index + 1}，内容会很长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长",
            )
        }

        messages.addAll(list)
        MainScreen()
    }

    @Preview(name = "Light Mode")
    @Preview(
        uiMode = UI_MODE_NIGHT_YES,
        showBackground = true,
        name = "Dark Mode"
    )
    @Composable
    fun MessageCardPreview() {
        AndroidLearningTheme {
            Surface {
                MessageCard(Message("Pan", "hello compose"))
            }
        }
    }
}

