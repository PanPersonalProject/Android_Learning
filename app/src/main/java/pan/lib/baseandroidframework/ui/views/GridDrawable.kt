package pan.lib.baseandroidframework.ui.views

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import pan.lib.common_lib.utils.ext.dp2px

/**
 * @author pan qi
 * @since 2024/6/15
 * 网格线Drawable
 */
class GridDrawable(
    private val horizontalSpacing: Float = 20.dp2px,
    private val verticalSpacing: Float = 20.dp2px,
    private val lineColor: Int = Color.GRAY,
    private val lineWidth: Float = 2.dp2px
) : Drawable() {

    private val gridPaint = Paint().apply {
        color = lineColor
        strokeWidth = lineWidth
        style = Paint.Style.STROKE
    }

    private var viewWidth = 0
    private var viewHeight = 0


    override fun draw(canvas: Canvas) {
        if (viewWidth == 0 || viewHeight == 0) return // 防止宽度或高度未被设置

        // 计算行数和列数，确保包括起始位置的线条
        val columns = (viewWidth / horizontalSpacing).toInt() + 1
        val rows = (viewHeight / verticalSpacing).toInt() + 1

        // 绘制水平网格线，从0开始以包含顶部的线
        for (i in 0 until rows) {
            val y = i * verticalSpacing
            canvas.drawLine(0f, y, viewWidth.toFloat(), y, gridPaint)
        }

        // 绘制垂直网格线，同样从0开始以包含左侧的线
        for (j in 0 until columns) {
            val x = j * horizontalSpacing
            canvas.drawLine(x, 0f, x, viewHeight.toFloat(), gridPaint)
        }
    }


    override fun setBounds(left: Int, top: Int, right: Int, bottom: Int) {
        super.setBounds(left, top, right, bottom)
        viewWidth = right - left
        viewHeight = bottom - top
    }

    override fun setAlpha(alpha: Int) {
        gridPaint.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        gridPaint.colorFilter = colorFilter
    }

    @Deprecated("Deprecated in Java")
    override fun getOpacity(): Int {
        return when (gridPaint.alpha) {
            0 -> PixelFormat.TRANSPARENT
            255 -> PixelFormat.OPAQUE
            else -> PixelFormat.TRANSLUCENT
        }
    }


}
