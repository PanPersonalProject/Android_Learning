package pan.lib.baseandroidframework.ui.main.compose_demo.listview

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.delay
import pan.lib.common_lib.utils.printSimpleLog

/**
 * @author pan qi
 * @since 2024/7/7
 */
class MessageDataSource : PagingSource<Int, Message>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Message> {
        val page = params.key ?: 0 // 当前页码
        val pageSize = params.loadSize // 每页大小
        printSimpleLog("page:$page,pageSize:$pageSize")
        return try {
            val messages = generateMockMessages(page, pageSize)// 生成模拟消息列表
            LoadResult.Page(
                data = messages,
                prevKey = if (page == 0) null else page - 1, // 上一页
                nextKey = if (messages.isEmpty()) null else page + 1 // 下一页 如果没有更多数据，返回null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    private suspend fun generateMockMessages(page: Int, pageSize: Int): List<Message> {
        delay(1500) // 模拟网络延迟
        return List(pageSize) { index ->
            Message(
                author = "用户${index + 1 + page * pageSize}",
                body = "这是一条示例消息${index + 1 + page * pageSize}，内容会很长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长",
            )
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Message>): Int? = null
}
