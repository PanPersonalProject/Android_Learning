package pan.lib.baseandroidframework.ui.main.compose_demo.listview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow


class ListViewModel : ViewModel() {
    val messages: Flow<PagingData<Message>> = MessageRepository.getMessagePagingData().cachedIn(viewModelScope)
}
