package pan.lib.baseandroidframework.ui.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.graphics.drawable.toBitmap
import pan.lib.common_lib.utils.ext.dp2px

/**
 * AUTHOR Pan Created on 2021/12/8
 */
class CameraView(context: Context, attrs: AttributeSet?) : AppCompatImageView(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val rectF = RectF()
    private val camera = Camera()

    init {
        camera.rotateX(0f)
        camera.setLocation(0f, 0f, -6 * resources.displayMetrics.density) //更具屏幕分辨率设定z轴英尺
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
        val picWidthHalf = (100.dp2px.toInt() / 2).toFloat() + 15.dp2px

        //图片下半部分
        canvas.save()
        canvas.rotate(-20f)
        canvas.clipRect(
            15.dp2px,
            15.dp2px + 100.dp2px / 2,
            15.dp2px + 100.dp2px,
            15.dp2px + 100.dp2px
        )
        canvas.rotate(20f)
        canvas.drawBitmap(
            drawable.toBitmap(
                100.dp2px.toInt(),
                100.dp2px.toInt()
            ), 15.dp2px, 15.dp2px, paint
        )
        canvas.restore()

        //图片上半部分
        canvas.save()
        canvas.translate(picWidthHalf, picWidthHalf)
        canvas.rotate(-20f)
        camera.applyToCanvas(canvas)
        canvas.rotate(20f)

        canvas.translate(-picWidthHalf, -picWidthHalf)   //移动到图片到坐标系原点再转动X轴,在Camera中操作是倒叙的
        canvas.clipRect(
            15.dp2px,
            15.dp2px ,
            15.dp2px + 100.dp2px,
            15.dp2px + 50.dp2px
        )
        canvas.drawBitmap(
            drawable.toBitmap(
                100.dp2px.toInt(),
                100.dp2px.toInt()
            ), 15.dp2px, 15.dp2px, paint
        )
        canvas.restore()

    }

    fun rotateX3d(angle: Int) {
        camera.rotateX(angle.toFloat())
        invalidate()
    }
}