package pan.lib.baseandroidframework.ui.main.graphics

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class GpuImageViewModel : ViewModel() {
    // 当前滤镜索引
    val currentFilterIndex: MutableStateFlow<Int> = MutableStateFlow(0)

    // 当前滤镜值
    val currentFilterValue: MutableStateFlow<Float> = MutableStateFlow(filterList[0].defaultValue)

    // 切换到下一个滤镜
    fun nextFilter(filterCount: Int) {
        currentFilterIndex.value = (currentFilterIndex.value + 1) % filterCount
        resetFilterValue()
    }

    // 切换到上一个滤镜
    fun previousFilter(filterCount: Int) {
        currentFilterIndex.value = (currentFilterIndex.value - 1 + filterCount) % filterCount
        resetFilterValue()
    }

    // 更新当前滤镜值
    fun updateFilterValue(value: Float) {
        currentFilterValue.value = value
    }

    // 重置当前滤镜值为默认值
    private fun resetFilterValue() {
        currentFilterValue.value = filterList[currentFilterIndex.value].defaultValue
    }
}