package pan.lib.baseandroidframework.ui.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.graphics.drawable.toBitmap
import pan.lib.common_lib.utils.ext.dp2px

/**
 * AUTHOR Pan Created on 2021/12/3
 *
 * https://developer.android.com/reference/android/graphics/PorterDuff.Mode
 */
class CircleImageView(context: Context, attrs: AttributeSet?) : AppCompatImageView(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val rectF = RectF()
    private val porterDuff = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)// 图形处理中用于控制两个图像在叠加时像素混合方式的一个类
    private val boardWidth = 2.dp2px

    init {
        paint.style = Paint.Style.FILL
        paint.color = Color.BLUE

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        rectF.left = 0f
        rectF.right = width.toFloat()
        rectF.top = 0f
        rectF.bottom = height.toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawCircle(
            (width / 2).toFloat(),
            (width / 2).toFloat(),
            (width / 2).toFloat(),
            paint
        ) //外边框

        //使用离屏缓冲，saveLayer会创建一个透明的图层,方便porterDuff进行图形混合
        val saveCount = canvas.saveLayer(rectF, paint)
        canvas.drawCircle(
            (width / 2).toFloat(),
            (width / 2).toFloat(),
            (width / 2).toFloat() - boardWidth,
            paint
        )
        paint.xfermode = porterDuff
        canvas.drawBitmap(
            drawable.toBitmap(
                (width - boardWidth).toInt(),
                (height - boardWidth).toInt()
            ), 0f, 0f, paint
        )
        paint.xfermode = null
        canvas.restoreToCount(saveCount)

    }

    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)
    }
}