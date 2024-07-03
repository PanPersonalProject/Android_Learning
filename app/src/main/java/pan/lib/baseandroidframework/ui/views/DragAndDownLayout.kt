package pan.lib.baseandroidframework.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.DragEvent
import android.view.View
import android.view.View.OnDragListener
import android.view.View.OnLongClickListener
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import pan.lib.baseandroidframework.databinding.ActivityCustomerViewDemo2DragListenerBinding
import pan.lib.common_lib.utils.printSimpleLog

/**
 * AUTHOR Pan Created on 2022/1/2
 */
class DragAndDownLayout(context: Context, attrs: AttributeSet?) :
    ConstraintLayout(context, attrs) {
    private lateinit var binding: ActivityCustomerViewDemo2DragListenerBinding
    private val onLongClickListener = OnLongClickListener {
        /**
        1.ClipData 用于传跨进程数据,比较重,而且只有DragEvent.ACTION_DROP的时候会传递
        2.DragShadowBuilder 拖拽时的图像
        3.myLocalState 比较轻不能跨进程,任何DragEvent都会传递
        4.flags 拖拽的类型*/
        ViewCompat.startDragAndDrop(it, null, DragShadowBuilder(it), it, 0)
    }

    private val dragListener = OnDragListener { v, event ->
        val dragView = event.localState as View

        when (event.action) {
            DragEvent.ACTION_DRAG_STARTED -> {
                printSimpleLog("ACTION_DRAG_STARTED" + v.javaClass.simpleName)
                dragView.visibility = View.INVISIBLE
            }

            DragEvent.ACTION_DRAG_ENTERED -> {
                printSimpleLog("ACTION_DRAG_ENTERED" + v.javaClass.simpleName)
            }

            DragEvent.ACTION_DROP -> {
                printSimpleLog("ACTION_DROP" + v.javaClass.simpleName)
                binding.contentLayout.addView(TextView(context).apply {
                    text = dragView.contentDescription
                })

            }

            DragEvent.ACTION_DRAG_ENDED -> {
                dragView.visibility = View.VISIBLE
            }

        }
        true
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        binding = ActivityCustomerViewDemo2DragListenerBinding.bind(this) // 初始化ViewBinding
        binding.iv1.setOnLongClickListener(onLongClickListener)
        binding.iv2.setOnLongClickListener(onLongClickListener)
        binding.contentLayout.setOnDragListener(dragListener)
    }
}
