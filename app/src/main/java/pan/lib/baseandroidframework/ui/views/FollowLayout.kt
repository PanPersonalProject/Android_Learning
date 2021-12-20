package pan.lib.baseandroidframework.ui.views

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View.MeasureSpec
import android.view.ViewGroup
import androidx.core.view.children
import pan.lib.common_lib.utils.printLog
import kotlin.math.max

/**
 * AUTHOR Pan Created on 2021/12/18
 */
class FollowLayout(context: Context?, attrs: AttributeSet?) : ViewGroup(context, attrs) {


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        var currentWidth = 0  //当前行用到的宽度位置
        var lastLineTop = 0 //最新一行的top值
        var lineMaxHeight = 0 //当前行view最高的height
        var maxwidth = 0 //所以行最宽的width

        for (position in 0 until childCount) {
            val view = getChildAt(position)
            measureChild(view, widthMeasureSpec, heightMeasureSpec)
            if (measuredWidth < currentWidth + view.measuredWidth) {
                lastLineTop += lineMaxHeight
                maxwidth = max(maxwidth, currentWidth)
                lineMaxHeight = 0
                currentWidth = 0
            }
            currentWidth += view.measuredWidth
            lineMaxHeight = max(lineMaxHeight, view.measuredHeight)
        }


        val resolveWidth = resolveSize(maxwidth, widthMeasureSpec)
        val resolveHeight = resolveSize(lastLineTop + lineMaxHeight, heightMeasureSpec)
        setMeasuredDimension(resolveWidth, resolveHeight)

    }


    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        printLog("l=$l,t=$t,r=$r,b=$b")
        var currentWidth = 0  //当前行用到的宽度位置
        var lastLineTop = 0 //最新一行的top值
        var lineMaxHeight = 0 //当前行view最高的height

        for (position in 0 until childCount) {
            val view = getChildAt(position)
            if (measuredWidth < currentWidth + view.measuredWidth) {
                lastLineTop += lineMaxHeight
                lineMaxHeight = 0
                currentWidth = 0
            }
            view.layout(
                currentWidth, //相对距离 l – Left position, relative to parent
                lastLineTop,
                currentWidth + view.measuredWidth,
                lastLineTop + view.measuredHeight
            )
            currentWidth += view.measuredWidth
            lineMaxHeight = max(lineMaxHeight, view.measuredHeight)

        }

    }


}