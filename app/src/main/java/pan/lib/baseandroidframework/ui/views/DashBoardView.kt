package pan.lib.baseandroidframework.ui.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import pan.lib.common_lib.utils.ext.dp2px

/**
 * AUTHOR Pan Created on 2021/12/1
 */
class DashBoardView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val rectF = RectF()

    init {
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 2.dp2px
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        rectF.left = paint.strokeWidth
        rectF.right = width.toFloat() - paint.strokeWidth
        rectF.top = paint.strokeWidth
        rectF.bottom = height.toFloat() - paint.strokeWidth
    }

    override fun onDraw(canvas: Canvas) {
        //sweepAngle 弧度的角度  useCenter 扇形图or弧线
        canvas.drawArc(rectF, 135f, 270f, false, paint)

    }
}