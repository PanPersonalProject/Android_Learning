package pan.lib.baseandroidframework.ui.main.compose_demo

import android.content.res.Configuration
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollDispatcher
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt


@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL,
    backgroundColor = 0xFFFFEAEE,
    showBackground = true
)
@Composable
fun NestedScrollSample() {
    var offsetY by remember { mutableFloatStateOf(0f) }
    val dispatcher = remember { NestedScrollDispatcher() }
    val connection = remember {
        object : NestedScrollConnection {
            //onPostScroll 函数的作用是在子组件已经消耗了滚动距离之后，通知父组件剩余的未消耗的滚动距离，并允许父组件进一步消耗这些剩余的滚动距离。
            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource,
            ): Offset {
                offsetY += available.y
                return available
            }
        }
    }
    Column(
        Modifier
            .offset { IntOffset(0, offsetY.roundToInt()) }
            .draggable(rememberDraggableState {
                //在拖动之前，dispatchPreScroll咨询父级是否需要滚动，并返回消耗的滚动距离
                val consumed =
                    dispatcher.dispatchPreScroll(Offset(0f, it), NestedScrollSource.UserInput)
                //计算剩余的滚动距离
                offsetY += it - consumed.y
                //在拖动之后，dispatchPostScroll通知父级已经滚动了多少距离，以及剩余可以滚动的距离
                dispatcher.dispatchPostScroll(
                    Offset(0f, it),
                    Offset(0f, 0f),
                    NestedScrollSource.UserInput
                )
            }, Orientation.Vertical)
            .nestedScroll(connection, dispatcher)
    ) {
        for (i in 1..10) {
            Text("第 $i 项")
        }
        LazyColumn(Modifier.height(50.dp)) {
            items(10) {
                Text("内部 List - 第 $it 项")
            }
        }
    }
}

