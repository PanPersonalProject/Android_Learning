package pan.lib.baseandroidframework.ui.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.graphics.drawable.toBitmap
import pan.lib.common_lib.utils.ext.dp2px

/**
 * AUTHOR Pan Created on 2021/12/3
 */
class CircleImageView(context: Context, attrs: AttributeSet?) : AppCompatImageView(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val rectF = RectF()

    init {
        paint.style = Paint.Style.FILL

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        rectF.left = 0f
        rectF.right = width.toFloat()
        rectF.top = 0f
        rectF.bottom = height.toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawBitmap(drawable.toBitmap(width, height), 0f, 0f, paint)
    }
}