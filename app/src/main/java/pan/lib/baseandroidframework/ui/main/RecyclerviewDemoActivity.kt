package pan.lib.baseandroidframework.ui.main

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pan.lib.baseandroidframework.databinding.ActivityDemoRecyclerviewBinding
import pan.lib.baseandroidframework.ui.adapter.DemoRvAdapter
import pan.lib.baseandroidframework.ui.adapter.User
import pan.lib.common_lib.utils.printLog

class RecyclerviewDemoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDemoRecyclerviewBinding
    private val oldList = listOf(
        User(1, "Alice", 30),
        User(2, "Bob", 30),
        User(3, "Charlie", 30),
    )

    private val newList = listOf(
        User(1, "Alice", 30),
        User(3, "Charlie", 30),
        User(4, "pan", 29),
        User(2, "Bob", 29),

    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDemoRecyclerviewBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = DemoRvAdapter {
            printLog("点击内容$it")
        }

        /**
         * 设置RecyclerView具有固定大小。
         *
         * 当RecyclerView的高度或宽度在布局文件中被明确指定为固定尺寸（例如精确像素值）
         * 或匹配父容器大小（`match_parent`），应当启用此选项。
         *
         * 启用`setHasFixedSize(true)`的优势在于，它能防止由于Item内容的微小变化
         * 而触发不必要的布局重新计算（`requestLayout`调用），从而有效减少资源消耗，
         * 提升界面渲染性能，尤其是在包含大量或复杂Item的场景下更为显著。
         */
        binding.recyclerView.setHasFixedSize(true)


        /**
         * 当一个viewpager里面有多个recyclerview的时候，
         * 可以多个复用recyclerview复用一个RecycledViewPool，避免重复创建
         */
        val recyclerViewPool = RecyclerView.RecycledViewPool()
        binding.recyclerView.setRecycledViewPool(recyclerViewPool)


        val dividerDecoration = DividerDecoration(16)
        binding.recyclerView.addItemDecoration(dividerDecoration)
        binding.recyclerView.adapter = adapter


        mockData(adapter)
    }

    private fun mockData(adapter: DemoRvAdapter) {
        adapter.submitList(lifecycleScope, oldList, false)

        // 3秒后更新数据,通过DiffUtil计算
        binding.recyclerView.postDelayed({
            adapter.submitList(lifecycleScope, newList, true)
        }, 500)

    }

    class DividerDecoration(private val dividerHeight: Int) : RecyclerView.ItemDecoration() {

        //onDraw是在item view绘制之前，onDrawOver是在item view绘制之后。
        override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
            val left = parent.paddingLeft
            val right = parent.width - parent.paddingRight

            val childCount = parent.childCount
            for (i in 0 until childCount) {
                val child = parent.getChildAt(i)
                val params = child.layoutParams as RecyclerView.LayoutParams

                val top = child.bottom + params.bottomMargin
                val bottom = top + dividerHeight

                c.drawRect(
                    left.toFloat(),
                    top.toFloat(),
                    right.toFloat(),
                    bottom.toFloat(),
                    Paint().apply {
                        color = Color.BLUE // 分隔线颜色
                        style = Paint.Style.FILL
                    })
            }
        }
    }
}
