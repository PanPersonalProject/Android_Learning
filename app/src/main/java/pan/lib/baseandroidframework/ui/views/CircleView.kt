package pan.lib.baseandroidframework.ui.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import pan.lib.common_lib.utils.ext.dp2px

class CircleView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    // 圆形的半径，默认为视图宽度的一半
    private var radius = 0f

    // 画笔配置
    private val paint = Paint().apply {
        color = Color.BLUE // 设置颜色为蓝色
        style = Paint.Style.FILL // 设置填充模式为实心
    }


    /**
     * 重写onMeasure方法，以适应不同的布局需求（wrap_content, match_parent, 指定大小）
     *
     * @param widthMeasureSpec 宽度测量规格
     * @param heightMeasureSpec 高度测量规格
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // 设置默认直径大小为100dp，这样可以保证在未指定具体大小时，有一个基础的显示尺寸
        val defaultDiameter =100.dp2px

        // 使用resolveSize方法处理宽度和高度的测量
        // resolveSize会根据测量模式（EXACTLY, AT_MOST, UNSPECIFIED）返回合适的尺寸：
        // - 对于EXACTLY（如具体数值或match_parent），返回给定的尺寸。
        // - 对于AT_MOST（如wrap_content），确保尺寸不超过最大值，尽量接近期望尺寸。
        // - 对于UNSPECIFIED，通常返回期望尺寸，但这种模式较少见。
        val resolvedWidth = resolveSize(defaultDiameter.toInt(), widthMeasureSpec)
        val resolvedHeight = resolveSize(defaultDiameter.toInt(), heightMeasureSpec)

        // 确保视图的宽度和高度一致，以形成正圆
        val size = maxOf(resolvedWidth, resolvedHeight)
        setMeasuredDimension(size, size)

        // 根据最终确定的尺寸计算圆的半径
        radius = size.toFloat() / 2
    }

    /**
     * 重写onDraw方法，用于绘制圆形
     *
     * @param canvas 画布对象
     */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // 在视图中心绘制圆形
        canvas.drawCircle(radius, radius, radius, paint)
    }
}
