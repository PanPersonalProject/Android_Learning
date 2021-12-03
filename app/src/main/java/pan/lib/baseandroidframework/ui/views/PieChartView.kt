package pan.lib.baseandroidframework.ui.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import org.jetbrains.anko.collections.forEachWithIndex
import pan.lib.common_lib.utils.ext.dp2px
import kotlin.math.cos
import kotlin.math.sin

/**
 * AUTHOR Pan Created on 2021/12/2
 */
class PieChartView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val rectF = RectF()
    private val angleList = listOf(
        PieBean(50f, Color.RED),
        PieBean(90f, Color.GREEN),
        PieBean(100f, Color.BLUE),
        PieBean(120F, Color.LTGRAY)
    )

    init {
        paint.style = Paint.Style.FILL
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        //仪表盘大小
        rectF.left = 10.dp2px
        rectF.right = width.toFloat() - 10.dp2px
        rectF.top = 10.dp2px
        rectF.bottom = height.toFloat() - 10.dp2px
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        var currentAngle = 0f
        angleList.forEachWithIndex { index, pie ->
            paint.color = pie.color
            if (index == 2) {
                canvas.save()
                val middleAngle = ((currentAngle + currentAngle + pie.angle) / 2).toDouble()
                val tranX = cos(Math.toRadians(middleAngle)) * 10.dp2px
                val tranY = sin(Math.toRadians(middleAngle)) * 10.dp2px
                canvas.translate(tranX.toFloat(), tranY.toFloat())  //平移画布达到坐标便宜效果
                canvas.drawArc(rectF, currentAngle, pie.angle, true, paint)
                canvas.restore()
            } else {
                canvas.drawArc(rectF, currentAngle, pie.angle, true, paint)
            }
            currentAngle += pie.angle
        }

    }

    data class PieBean(val angle: Float, @ColorInt val color: Int)
}