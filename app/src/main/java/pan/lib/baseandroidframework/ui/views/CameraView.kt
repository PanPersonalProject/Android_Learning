package pan.lib.baseandroidframework.ui.views

import android.content.Context
import android.graphics.Camera
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import androidx.annotation.Keep
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.graphics.drawable.toBitmap
import pan.lib.common_lib.utils.WindowUtil
import pan.lib.common_lib.utils.printSimpleLog

/**
 * AUTHOR Pan Created on 2021/12/8
 */
@Keep
class CameraView(context: Context, attrs: AttributeSet?) : AppCompatImageView(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val camera = Camera()
    private var flipRotation = 0f
        set(value) {
            field = value
            invalidate()
        }
    private var bottomFlip = 0f
        set(value) {
            printSimpleLog("bottomFlip=$bottomFlip")
            field = value
            invalidate()
        }

    private var topFlip = 0f
        set(value) {
            field = value
            invalidate()
        }


    init {
        camera.setLocation(0f, 0f, WindowUtil.getZForCamera()) //更具屏幕分辨率设定z轴英尺
        paint.style = Paint.Style.FILL
    }

    override fun onDraw(canvas: Canvas) {
        if (drawable == null) return
        val imageWidth = (width.toFloat() / 1.4).toFloat()
        val picWidthHalf = (imageWidth / 2)

        //图片下半部分
        canvas.save()
        canvas.translate(picWidthHalf, picWidthHalf)
        canvas.rotate(-flipRotation)
        camera.save()
        camera.rotateX(bottomFlip)
        camera.applyToCanvas(canvas)
        camera.restore()
        canvas.clipRect(
            -imageWidth,
            0f,
            imageWidth,
            imageWidth
        )
        canvas.rotate(flipRotation)
        canvas.translate(-picWidthHalf, -picWidthHalf)   //移动到图片到坐标系原点再转动X轴,在Camera中操作是倒叙的
        canvas.drawBitmap(
            drawable.toBitmap(imageWidth.toInt(), imageWidth.toInt()), 0f, 0f, paint
        )
        canvas.restore()

        //图片上半部分
        canvas.save()
        canvas.translate(picWidthHalf, picWidthHalf)
        canvas.rotate(-flipRotation)
        camera.save()
        camera.rotateX(topFlip)
        camera.applyToCanvas(canvas)
        camera.restore()

        canvas.clipRect(-imageWidth, -imageWidth, imageWidth, 0f)
        canvas.rotate(flipRotation)
        canvas.translate(-picWidthHalf, -picWidthHalf)   //移动到图片到坐标系原点再转动X轴,在Camera中操作是倒叙的
        canvas.drawBitmap(
            drawable.toBitmap(imageWidth.toInt(), imageWidth.toInt()), 0f, 0f, paint
        )
        canvas.restore()

    }

}