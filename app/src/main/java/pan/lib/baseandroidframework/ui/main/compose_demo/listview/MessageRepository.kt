package pan.lib.baseandroidframework.ui.main.compose_demo.listview

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

/**
 * @author pan qi
 * @since 2024/7/7
 */
object MessageRepository {
    private const val PAGE_SIZE = 20

    fun getMessagePagingData(): Flow<PagingData<Message>> {
        return Pager(
            config = PagingConfig(PAGE_SIZE, initialLoadSize = PAGE_SIZE),
            pagingSourceFactory = { MessageDataSource() }
        ).flow
    }


}