package pan.lib.baseandroidframework.ui.main.compose_demo.listview

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pan.lib.baseandroidframework.R
import pan.lib.baseandroidframework.ui.compose_views.MainScaffold
import pan.lib.baseandroidframework.ui.theme.AndroidLearningTheme

class ComposeListViewDemoActivity : ComponentActivity() {

    private val viewModel by viewModels<ListViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainScreen(viewModel.messages)
        }
        viewModel.requestMockMessages()
    }


    @Composable
    private fun MainScreen(messages: SnapshotStateList<Message>) {
        AndroidLearningTheme {
            MainScaffold(title = "消息列表", onNavigateBack = { finish() }) {
                if (messages.isEmpty()) {
                    //如果数据为空，则显示加载中
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            verticalArrangement = Arrangement.Center, // 使内容垂直居中
                            horizontalAlignment = Alignment.CenterHorizontally, // 使内容水平居中
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(64.dp),
                                color = MaterialTheme.colorScheme.secondary,
                                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "正在加载消息，请稍候...",
                                style = MaterialTheme.typography.bodyLarge,
                            )
                        }
                    }

                } else {
                    //如果数据不为空，则显示消息
                    Conversation(messages)
                }
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

    @Composable
    fun Conversation(messages: List<Message>) {
        //增加item垂直间距
        LazyColumn(verticalArrangement = Arrangement.spacedBy(5.dp)) {
            items(messages) {
                MessageCard(it)
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
        MainScreen(messages)
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

