package pan.lib.baseandroidframework.ui.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.OverScroller
import pan.lib.baseandroidframework.R
import pan.lib.common_lib.utils.ext.dp2px
import pan.lib.common_lib.utils.makeBitmap
import pan.lib.common_lib.utils.printSimpleLog

/**
 * AUTHOR Pan Created on 2021/12/27
 */
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
    private val gestureDetector = GestureDetector(context, ScalableImageViewGestureListener())
    private var scroller = OverScroller(context)


    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(event)
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
    }

    override fun onDraw(canvas: Canvas) {

        canvas.translate(transX, transY)
        if (isBig) {
            canvas.scale(bigScale, bigScale, width / 2f, height / 2f)
        } else {
            canvas.scale(smallScale, smallScale, width / 2f, height / 2f)
        }
        canvas.drawBitmap(bitmap, offsetX, offsetY, paint)
    }


    private inner class ScalableImageViewGestureListener :
        GestureDetector.SimpleOnGestureListener(), Runnable {
        override fun onDown(e: MotionEvent?): Boolean {
            return true // 每次 ACTION_DOWN 事件出现的时候都会被调⽤，在这⾥返回 true 可以保证必然消费掉,确保后续MOVE UP操作
        }

        override fun onScroll(
            e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float
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
                printSimpleLog("transX=$transX,transY=$transY")
                invalidate()
            }
            return super.onScroll(e1, e2, distanceX, distanceY)
        }

        override fun onFling(
            e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float
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
                printSimpleLog("e1 ${e1.x}  ${e1.y}  e2 ${e2.x}  ${e2.y}")

                postOnAnimation(this)

            }

            return super.onFling(e1, e2, velocityX, velocityY)
        }

        override fun onDoubleTap(e: MotionEvent?): Boolean {
            isBig = !isBig
            if (!isBig) {
                transY = 0f
                transX = 0f
            }
            invalidate()
            return super.onDoubleTap(e)
        }

        override fun run() {
            if (scroller.computeScrollOffset()) {
                transX = scroller.currX.toFloat()
                transY = scroller.currY.toFloat()
                invalidate()
                postOnAnimation(this)
            }

        }

    }
}