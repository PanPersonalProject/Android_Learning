package pan.lib.baseandroidframework.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.View.MeasureSpec
import android.view.ViewGroup
import pan.lib.common_lib.utils.printLog

/**
 * AUTHOR Pan Created on 2021/12/18
 */
class TagLayout(context: Context?, attrs: AttributeSet?) : ViewGroup(context, attrs) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val specMode = MeasureSpec.getMode(widthMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        printLog(
            "width=$width," +
                    "height=${MeasureSpec.getSize(heightMeasureSpec)}"
        )

    }


    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        printLog("l=$l,t=$t,r=$r,b=$b")
    }


}