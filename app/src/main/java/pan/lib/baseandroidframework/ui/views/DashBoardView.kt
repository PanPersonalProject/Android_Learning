package pan.lib.baseandroidframework.ui.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import pan.lib.common_lib.utils.ext.dp2px
import kotlin.math.cos
import kotlin.math.sin

/**
 * AUTHOR Pan Created on 2021/12/1
 */
class  DashBoardView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val rectF = RectF()
    private lateinit var pathDashPathEffect: PathDashPathEffect
    private val dashPath = Path()
    var arcLength: Float = 0f

    init {
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 2.dp2px
        dashPath.addRect(0f, 0f, 2.dp2px, 10.dp2px, Path.Direction.CW)
requestLayout()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        //仪表盘大小
        rectF.left = paint.strokeWidth
        rectF.right = width.toFloat() - paint.strokeWidth
        rectF.top = paint.strokeWidth
        rectF.bottom = height.toFloat() - paint.strokeWidth

        //计算弧线长度
        val arcPath = Path()
        arcPath.addArc(rectF, 135f, 270f)
        arcLength = PathMeasure(arcPath, false).length

        //刻度Path效果
        pathDashPathEffect = PathDashPathEffect(
            dashPath,
            (arcLength - 2.dp2px) / 20,
            0f,
            PathDashPathEffect.Style.ROTATE
        )


    }

    override fun onDraw(canvas: Canvas) {
        //sweepAngle 弧度的角度  useCenter 扇形图or弧线
        canvas.drawArc(rectF, 135f, 270f, false, paint)

        //画刻度
        paint.pathEffect = pathDashPathEffect
        canvas.drawArc(rectF, 135f, 270f, false, paint)
        paint.pathEffect = null

        val x = rectF.centerX()
        val y = rectF.centerY()
        val pointerLength = x - 15.dp2px
        val angle = getAngle(11)
        val stopX = cos(Math.toRadians(angle)) * pointerLength
        val stopY = sin(Math.toRadians(angle)) * pointerLength
        canvas.drawLine(x, y, x + stopX.toFloat(), y + stopY.toFloat(), paint)

    }

    private fun getAngle(dash: Int): Double {
        return 135 + (dash.toDouble() / 20) * 270
    }
}