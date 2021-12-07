package pan.lib.baseandroidframework.ui.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import pan.lib.common_lib.utils.ext.dp2px

/**
 * AUTHOR Pan Created on 2021/12/7
 */
class SportsView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private var paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var boundsRect = Rect()
    private var fontMetrics = Paint.FontMetrics()
    private val ringWidth = 20.dp2px
    private val circleColor = Color.parseColor("#90A4AE")
    private val highlightColor = Color.parseColor("#FF4081")
    private val rectF = RectF()

    init {
        paint.textSize = 100f
        paint.typeface = Typeface.createFromAsset(context.assets, "Quicksand-Regular.ttf")
        paint.getFontMetrics(fontMetrics)
        paint.textAlign = Paint.Align.CENTER
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        rectF.left = ringWidth
        rectF.right = width.toFloat() - ringWidth
        rectF.top = ringWidth
        rectF.bottom = height.toFloat() - ringWidth
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // 绘制环
        paint.style = Paint.Style.STROKE
        paint.color = circleColor
        paint.strokeWidth = ringWidth
        canvas.drawCircle(
            (width / 2).toFloat(),
            (width / 2).toFloat(),
            (width / 2).toFloat() - ringWidth,
            paint
        )

        // 绘制进度条
        paint.color = highlightColor
        paint.strokeCap = Paint.Cap.ROUND
        canvas.drawArc(
            rectF,
            -90f,
            225f,
            false,
            paint
        )

        // 绘制文字
        val text = "PAN"
        paint.textSize = 100f
        paint.style = Paint.Style.FILL
        paint.textAlign = Paint.Align.CENTER
        paint.getTextBounds(text, 0, text.length, boundsRect)
//        val offset = (fontMetrics.ascent + fontMetrics.descent) / 2
        val offset = boundsRect.centerY() //centerY=(top + bottom) / 2    top为负 bottom在0附近
        canvas.drawText(text, (width / 2).toFloat(), (height / 2 - offset).toFloat(), paint)
    }


}