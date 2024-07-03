package pan.lib.baseandroidframework.ui.views

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.OverScroller
import androidx.annotation.Keep
import pan.lib.baseandroidframework.R
import pan.lib.common_lib.utils.ext.dp2px
import pan.lib.common_lib.utils.makeBitmap
import pan.lib.common_lib.utils.printSimpleLog

/**
 * AUTHOR Pan Created on 2021/12/27
 */
@Keep
class ScalableImageView(context: Context, attrs: AttributeSet?) : View(context, attrs) {


    private var bitmap: Bitmap = makeBitmap(R.mipmap.bj, 300.dp2px.toInt())

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var offsetX = 0f //原始偏移
    private var offsetY = 0f
    private var transX = 0f //滑动的距离
    private var transY = 0f
    private var isBig = false
    private val overScale = 1.5
    private var smallScale = 0f
    private var bigScale = 0f
    private var currentScale = 0f

    private val scaleAnimator by lazy { ObjectAnimator.ofFloat(this, "scaleProgress", 0f, 1f) }

    private var scaleProgress = 0f
        set(value) {
            field = value
            currentScale = smallScale + (bigScale - smallScale) * value
            invalidate()
        }
    private val scaleGestureListener = ScalableImageViewGestureListener()
    private val gestureDetector = GestureDetector(context, scaleGestureListener)
    private val scaleDetector = ScaleGestureDetector(context, scaleGestureListener)
    private var scroller = OverScroller(context)


    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        var result = scaleDetector.onTouchEvent(event)
        if (!scaleDetector.isInProgress) {
            result = gestureDetector.onTouchEvent(event)
        }
        return result
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        offsetX = (width - bitmap.width) / 2f
        offsetY = (height - bitmap.height) / 2f

        if ((bitmap.width / bitmap.height.toFloat()) > width / height.toFloat()) {
            smallScale = width / bitmap.width.toFloat()
            bigScale = ((height / bitmap.height.toFloat()) * overScale).toFloat()
        } else {
            smallScale = height / bitmap.height.toFloat()
            bigScale = ((width / bitmap.width.toFloat()) * overScale).toFloat()
        }
        currentScale = smallScale
    }

    override fun onDraw(canvas: Canvas) {
        transX *= scaleProgress
        transY *= scaleProgress
        canvas.translate(transX, transY)
        canvas.scale(currentScale, currentScale, width / 2f, height / 2f)
        canvas.drawBitmap(bitmap, offsetX, offsetY, paint)
    }


    private inner class ScalableImageViewGestureListener :
        GestureDetector.SimpleOnGestureListener(), ScaleGestureDetector.OnScaleGestureListener,
        Runnable {
        var originScale = 0f
        override fun onDown(e: MotionEvent): Boolean {
            return true // 每次 ACTION_DOWN 事件出现的时候都会被调⽤，在这⾥返回 true 可以保证必然消费掉,确保后续MOVE UP操作
        }

        override fun onScroll(
            e1: MotionEvent?,
            e2: MotionEvent,
            distanceX: Float,
            distanceY: Float
        ): Boolean {

            if (isBig) {
                transX -= distanceX
                transY -= distanceY
                val bigWidth = bitmap.width * bigScale
                val bigHeight = bitmap.height * bigScale
                transX = transX.coerceAtMost((bigWidth - width) / 2)
                transX = transX.coerceAtLeast(-(bigWidth - width) / 2)
                transY = transY.coerceAtMost((bigHeight - height) / 2)
                transY = transY.coerceAtLeast(-(bigHeight - height) / 2)
                invalidate()
            }
            return super.onScroll(e1, e2, distanceX, distanceY)
        }

        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            //第⼀个事件是⽤户按下时的 ACTION_DOWN 事件，第⼆个事件是当前事件
            if (isBig) {
                val bigWidth = (bitmap.width * bigScale).toInt()
                val bigHeight = (bitmap.height * bigScale).toInt()
                scroller.fling(
                    transX.toInt(),
                    transY.toInt(),
                    velocityX.toInt(),
                    velocityY.toInt(),
                    -(bigWidth - width) / 2,
                    (bigWidth - width) / 2,
                    -(bigHeight - height) / 2,
                    (bigHeight - height) / 2
                )
                printSimpleLog("onFling transX=$transX,transY=$transY")
                printSimpleLog("e1 ${e1?.x}  ${e1?.y}  e2 ${e2.x}  ${e2.y}")

                postOnAnimation(this)

            }

            return super.onFling(e1, e2, velocityX, velocityY)
        }

        override fun onDoubleTap(e: MotionEvent): Boolean {
            isBig = !isBig
            scaleAnimator.cancel()
            if (isBig) {
                scaleAnimator.start()
            } else {
                scaleAnimator.reverse()
            }
            return super.onDoubleTap(e)
        }

        override fun run() {
            if (scroller.computeScrollOffset()) {
                transX = scroller.currX.toFloat()
                transY = scroller.currY.toFloat()
                invalidate()
                postOnAnimation(this)//post是立即去主线程执行,postOnAnimations是下一帧调用run方法
            }

        }

        override fun onScale(detector: ScaleGestureDetector): Boolean {
            currentScale = originScale * detector.scaleFactor
            invalidate()
            return false
        }

        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
            originScale = currentScale
            return true
        }

        override fun onScaleEnd(detector: ScaleGestureDetector) {
            originScale = 0f
        }

    }
}