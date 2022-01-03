package pan.lib.baseandroidframework.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.widget.FrameLayout
import androidx.core.view.ViewCompat
import androidx.customview.widget.ViewDragHelper
import kotlinx.android.synthetic.main.activity_customer_view_demo2_drag_up_down.view.*
import kotlin.math.abs

/**
 * AUTHOR Pan Created on 2022/1/3
 */
class DragUpDownLayout(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {
    var dragListener: ViewDragHelper.Callback = DragListener()
    var dragHelper: ViewDragHelper = ViewDragHelper.create(this, dragListener)
    val viewConfiguration: ViewConfiguration = ViewConfiguration.get(context)


    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return dragHelper.shouldInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        dragHelper.processTouchEvent(event)
        return true
    }

    override fun computeScroll() {
        if (dragHelper.continueSettling(true)) { //自动xy赋值
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

    internal inner class DragListener : ViewDragHelper.Callback() {
        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
            return child === view
        }

        override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
            return top
        }

        override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
            //默认0 0代表无法横向移动
            return 0
        }

        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            if (abs(yvel) > viewConfiguration.scaledMinimumFlingVelocity) {
                if (yvel > 0) {
                    dragHelper.settleCapturedViewAt(0, height - releasedChild.height)
                } else {
                    dragHelper.settleCapturedViewAt(0, 0)
                }
            } else {
                if (releasedChild.top < height - releasedChild.bottom) {
                    dragHelper.settleCapturedViewAt(0, 0)
                } else {
                    dragHelper.settleCapturedViewAt(0, height - releasedChild.height)
                }
            }
            postInvalidateOnAnimation()
        }
    }

}