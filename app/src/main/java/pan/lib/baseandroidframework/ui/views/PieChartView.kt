package pan.lib.baseandroidframework.ui.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import org.jetbrains.anko.collections.forEachWithIndex
import pan.lib.common_lib.utils.ext.dp2px

/**
 * AUTHOR Pan Created on 2021/12/2
 */
class PieChartView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val rectF = RectF()
    val angleList = listOf([50f, Color.RED], 100f, 90f, 120f)


    init {
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 2.dp2px

        listOf({50f, Color.RED})
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        //仪表盘大小
        rectF.left = paint.strokeWidth
        rectF.right = width.toFloat() - paint.strokeWidth
        rectF.top = paint.strokeWidth
        rectF.bottom = height.toFloat() - paint.strokeWidth
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        var currentAngle = 0f
        angleList.forEachWithIndex { index, fl ->  }
        for (fl in angleList) {

        }
        angleList.forEach { angle ->
            canvas.drawArc(rectF, currentAngle, angle, false, paint)
            currentAngle = angle
        }
    }
}