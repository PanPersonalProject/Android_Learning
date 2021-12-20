package pan.lib.baseandroidframework.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import kotlin.math.max

/**
 * AUTHOR Pan Created on 2021/12/18
 */
class FollowLayout(context: Context?, attrs: AttributeSet?) : ViewGroup(context, attrs) {

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    override fun generateDefaultLayoutParams(): LayoutParams {
        return MarginLayoutParams(super.generateDefaultLayoutParams())
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        var currentWidth = 0  //当前行用到的宽度位置
        var lastLineTop = 0 //最新一行的top值
        var lineMaxHeight = 0 //当前行view最高的height
        var maxwidth = 0 //所以行最宽的width

        for (position in 0 until childCount) {
            val view = getChildAt(position)
            measureChildWithMargins(
                view,
                widthMeasureSpec,
                currentWidth,
                heightMeasureSpec,
                lastLineTop
            )
            val marginLayoutParams = view.layoutParams as MarginLayoutParams

            if (measuredWidth < currentWidth + view.measuredWidth + marginLayoutParams.marginStart) {
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
        var currentWidth = 0  //当前行用到的宽度位置
        var lastLineTop = 0 //最新一行的top值
        var lineMaxHeight = 0 //当前行view最高的height

        for (position in 0 until childCount) {
            val view = getChildAt(position)
            val marginLayoutParams = view.layoutParams as MarginLayoutParams
            if (measuredWidth < currentWidth + view.measuredWidth + marginLayoutParams.marginStart) {
                lastLineTop += lineMaxHeight
                lineMaxHeight = 0
                currentWidth = 0
            }
            view.layout(
                currentWidth + marginLayoutParams.marginStart, //相对距离 l – Left position, relative to parent
                lastLineTop,
                currentWidth + view.measuredWidth,
                lastLineTop + view.measuredHeight
            )
            currentWidth += view.measuredWidth
            lineMaxHeight = max(lineMaxHeight, view.measuredHeight)

        }

    }


}