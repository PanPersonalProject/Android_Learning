package pan.lib.baseandroidframework.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import pan.lib.baseandroidframework.R
import pan.lib.common_lib.utils.printSimpleLog

/**
 * AUTHOR Pan Created on 2021/12/21
 */
class TouchStudyView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    /**
     * TouchTarget会记住按下时的view,接下去的触摸事件会直接调用view的dispatchTouchEvent
     *  结果:
     *  TouchStudyFrameLayout onInterceptTouchEvent action=0
    onTouchEvent test2 action=0
    onTouchEvent test1 action=0
    TouchStudyView ACTION_DOWN
    TouchStudyFrameLayout onInterceptTouchEvent action=2
    onTouchEvent test1 action=2
    TouchStudyView action=2*/

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (R.id.test1 == id) {
            printSimpleLog("onTouchEvent test1 action=" + event.actionMasked)
            if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                printSimpleLog("TouchStudyView ACTION_DOWN")
                return true
            }
            printSimpleLog("TouchStudyView action=" + event.actionMasked)
            return false
        } else if (R.id.test2 == id) {
            printSimpleLog("onTouchEvent test2 action=" + event.actionMasked)
            return false
        }
        parent.requestDisallowInterceptTouchEvent(true)
        return super.onTouchEvent(event)
    }

}

class TouchStudyFrameLayout(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {
    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        printSimpleLog("TouchStudyFrameLayout onInterceptTouchEvent action=" + ev.actionMasked)
        return super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        printSimpleLog("TouchStudyFrameLayout onTouchEvent action=" + event.actionMasked)
        return true
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        return super.dispatchTouchEvent(event)
    }


}