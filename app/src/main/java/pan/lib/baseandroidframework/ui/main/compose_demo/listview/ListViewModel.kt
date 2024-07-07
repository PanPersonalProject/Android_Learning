package pan.lib.baseandroidframework.ui.main.compose_demo.listview

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class Message(val author: String, val body: String)

class ListViewModel : ViewModel() {

    val messages = mutableStateListOf<Message>()
    var isRefreshing =mutableStateOf(false)

    fun requestMockMessages() {
        viewModelScope.launch {
            val generatedMessages = withContext(Dispatchers.Default) {
                isRefreshing.value=true
                delay(2000) // 模拟网络请求，延迟2秒
                List(20) { index ->
                    Message(
                        author = "用户${index + 1}",
                        body = "这是一条示例消息${index + 1}，内容会很长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长",
                    )
                }
            }
            isRefreshing.value=false
            messages.clear()
            messages.addAll(generatedMessages)
        }
    }
}
