package pan.lib.baseandroidframework.ui.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.graphics.drawable.toBitmap

/**
 * AUTHOR Pan Created on 2021/12/8
 */
class CameraView(context: Context, attrs: AttributeSet?) : AppCompatImageView(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val camera = Camera()
    private var flipRotation = 10f
        set(value) {
            field = value
            invalidate()
        }
    private var bottomFlip = 0f
        set(value) {
            Log.e("TAG_GENERAL", "angle=$value")
            field = value
            invalidate()
        }

    private var topFlip = 0f
        set(value) {
            field = value
            invalidate()
        }


    init {
        camera.rotateX(0f)
        camera.setLocation(0f, 0f, -6 * resources.displayMetrics.density) //更具屏幕分辨率设定z轴英尺
        paint.style = Paint.Style.FILL
    }

    override fun onDraw(canvas: Canvas) {
        val picWidthHalf = (width / 2).toFloat()


       /*   绘制下半部分
        canvas.save();
        canvas.translate(PADDING + IMAGE_WIDTH / 2, PADDING + IMAGE_WIDTH / 2);
        canvas.rotate(-flipRotation);
        camera.save();
        camera.rotateX(bottomFlip);
        camera.applyToCanvas(canvas);
        camera.restore();
        canvas.clipRect(- IMAGE_WIDTH, 0, IMAGE_WIDTH, IMAGE_WIDTH);
        canvas.rotate(flipRotation);
        canvas.translate(- (PADDING + IMAGE_WIDTH / 2), - (PADDING + IMAGE_WIDTH / 2));
        canvas.drawBitmap(Utils.getAvatar(getResources(), (int) IMAGE_WIDTH), PADDING, PADDING, paint);
        canvas.restore();
        * */
        //图片下半部分
        canvas.save()
        canvas.translate(picWidthHalf, picWidthHalf)
        canvas.rotate(-flipRotation)
        camera.save()
        camera.rotateX(bottomFlip)
        camera.applyToCanvas(canvas)
        camera.restore()
        canvas.clipRect(
            -picWidthHalf * 2,
            0f,
            picWidthHalf * 2,
            picWidthHalf * 2
        )
        canvas.rotate(flipRotation)
        canvas.translate(-picWidthHalf, -picWidthHalf)   //移动到图片到坐标系原点再转动X轴,在Camera中操作是倒叙的
        canvas.drawBitmap(
            drawable.toBitmap(
                width,
                width
            ), 0f, 0f, paint
        )
        canvas.restore()

        //图片上半部分
        canvas.save()
        canvas.rotate(-flipRotation)
        canvas.translate(picWidthHalf, picWidthHalf)
        camera.save()
        camera.rotateX(topFlip)
        camera.applyToCanvas(canvas)
        camera.restore()
        canvas.clipRect(
            -picWidthHalf * 2,
            -picWidthHalf * 2,
            picWidthHalf * 2,
            0f
        )
        canvas.rotate(flipRotation)
        canvas.translate(-picWidthHalf, -picWidthHalf)   //移动到图片到坐标系原点再转动X轴,在Camera中操作是倒叙的
        canvas.drawBitmap(
            drawable.toBitmap(
                width,
                width
            ), 0f, 0f, paint
        )
        canvas.restore()

        /*


             canvas.save();
        canvas.translate(PADDING + IMAGE_WIDTH / 2, PADDING + IMAGE_WIDTH / 2);
        canvas.rotate(-flipRotation);
        camera.save();
        camera.rotateX(topFlip);
        camera.applyToCanvas(canvas);
        camera.restore();
        canvas.clipRect(- IMAGE_WIDTH, - IMAGE_WIDTH, IMAGE_WIDTH, 0);
        canvas.rotate(flipRotation);
        canvas.translate(- (PADDING + IMAGE_WIDTH / 2), - (PADDING + IMAGE_WIDTH / 2));
        canvas.drawBitmap(Utils.getAvatar(getResources(), (int) IMAGE_WIDTH), PADDING, PADDING, paint);
        canvas.restore();
        *   绘制下半部分
        canvas.save();
        canvas.translate(PADDING + IMAGE_WIDTH / 2, PADDING + IMAGE_WIDTH / 2);
        canvas.rotate(-flipRotation);
        camera.save();
        camera.rotateX(bottomFlip);
        camera.applyToCanvas(canvas);
        camera.restore();
        canvas.clipRect(- IMAGE_WIDTH, 0, IMAGE_WIDTH, IMAGE_WIDTH);
        canvas.rotate(flipRotation);
        canvas.translate(- (PADDING + IMAGE_WIDTH / 2), - (PADDING + IMAGE_WIDTH / 2));
        canvas.drawBitmap(Utils.getAvatar(getResources(), (int) IMAGE_WIDTH), PADDING, PADDING, paint);
        canvas.restore();
        * */

    }

    fun rotateX3d(angle: Int) {
        bottomFlip = angle.toFloat()
        invalidate()
    }
}