package pan.lib.baseandroidframework.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.widget.OverScroller
import androidx.core.view.forEachIndexed
import pan.lib.common_lib.utils.printSimpleLog
import kotlin.math.abs


/**
 * AUTHOR Pan Created on 2022/1/1
 */
class TwoPagerView(context: Context?, attrs: AttributeSet?) : ViewGroup(context, attrs) {
    private var currentPage = 0
    private var downX = 0f
    private var downY = 0f
    private var lastScroll = 0
    private val viewConfiguration = ViewConfiguration.get(context)
    private val velocityTracker = VelocityTracker.obtain()
    private var scroller = OverScroller(context)


    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        var intercept = false
        velocityTracker.addMovement(ev)
        when (ev.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                actionDown(ev)
            }
            MotionEvent.ACTION_MOVE -> {
                //scaledPagingTouchSlop 大于这个值应该开始移动拦截
                if (abs(ev.x - downX) > viewConfiguration.scaledPagingTouchSlop) {
                    intercept = true
                    //请求parent再本次时间序列中不再拦截,比如在ScrollView中,划的时候y方向给可能被parent拦截
                    parent.requestDisallowInterceptTouchEvent(true)
                }
            }

        }
        return intercept
    }


    override fun onTouchEvent(ev: MotionEvent): Boolean {
        velocityTracker.addMovement(ev)
        when (ev.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                actionDown(ev)
            }
            MotionEvent.ACTION_MOVE -> {
                scrollX = -(ev.x - downX).toInt() + lastScroll
                scrollX = scrollX.coerceAtMost(width)
                scrollX = scrollX.coerceAtLeast(0)
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                velocityTracker.computeCurrentVelocity(1000)
                currentPage =
                    if (abs(velocityTracker.xVelocity) > viewConfiguration.scaledMinimumFlingVelocity) {
                        printSimpleLog("velocityTracker.xVelocity ${velocityTracker.xVelocity}")
                        if (velocityTracker.xVelocity < 0) 1 else 0
                    } else {
                        if (width / 2 > scrollX) 0 else 1
                    }

                val tranX = if (currentPage == 1) width - scrollX else -scrollX
                scroller.startScroll(scrollX, 0, tranX, 0)
                postInvalidateOnAnimation()

            }

        }

        return true
    }

    override fun computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollX = scroller.currX
            postInvalidateOnAnimation()
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        forEachIndexed { index, view ->
            val left = width * index
            val right = left + width
            view.layout(left, t, right, b)
        }
    }

    /**
    当自view拦截时 不会走onTouchEvent的ACTION_DOWN,所以都需要同样操作
    onInterceptTouchEvent ACTION_DOWN  x 1154.5283 y 1154.5283
    onInterceptTouchEvent ACTION_MOVE abs 3.8811035
    onInterceptTouchEvent ACTION_MOVE abs 20.5354
    onInterceptTouchEvent ACTION_MOVE abs 40.39441
    onInterceptTouchEvent ACTION_MOVE abs 56.18274
    onInterceptTouchEvent ACTION_MOVE abs 67.75098
    onInterceptTouchEvent ACTION_MOVE abs 76.83325
    onInterceptTouchEvent ACTION_MOVE abs 85.00665
    onInterceptTouchEvent ACTION_MOVE abs 91.06079
    onInterceptTouchEvent ACTION_MOVE abs 96.51257
    onTouchEvent ACTION_MOVE  x 1172.6765 x 1172.6765
    onTouchEvent ACTION_MOVE  x 1172.956 x 1172.956
     */
    private fun actionDown(ev: MotionEvent) {
        velocityTracker.clear()
        downX = ev.x
        downY = ev.y
        lastScroll = scrollX
    }

}