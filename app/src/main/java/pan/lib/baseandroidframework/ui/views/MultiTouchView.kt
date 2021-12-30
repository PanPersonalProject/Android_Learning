package pan.lib.baseandroidframework.ui.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import pan.lib.baseandroidframework.R
import pan.lib.common_lib.utils.ext.dp2px
import pan.lib.common_lib.utils.makeBitmap
import pan.lib.common_lib.utils.printSimpleLog

/**
 * AUTHOR Pan Created on 2021/12/27
 */
class MultiTouchView(context: Context, attrs: AttributeSet?) : View(context, attrs) {


    private var bitmap: Bitmap = makeBitmap(R.mipmap.bj, 200.dp2px.toInt())

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var offsetX = 0f
    private var offsetY = 0f
    private var lastOffsetX = 0f
    private var lastOffsetY = 0f
    private var downX = 0f
    private var downY = 0f
    private var currentPointerId = 0

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                currentPointerId = event.getPointerId(0)
                downX = event.x
                downY = event.y
                lastOffsetX = offsetX  //累积上一次的offset
                lastOffsetY = offsetY
            }

            MotionEvent.ACTION_MOVE -> {
                //上一次的offset加上本次offset
                val pointerIndex = event.findPointerIndex(currentPointerId)
                offsetX = lastOffsetX + (event.getX(pointerIndex) - downX)
                offsetY = lastOffsetY + (event.getY(pointerIndex) - downY)
                invalidate()
            }

            MotionEvent.ACTION_POINTER_UP -> {

                if (currentPointerId == event.getPointerId(event.actionIndex)) {
                    currentPointerId = if (event.actionIndex == event.pointerCount - 1) {
                        event.getPointerId(event.pointerCount - 2)
                    } else {
                        event.getPointerId(event.pointerCount - 1)

                    }

                }
                printSimpleLog("ACTION_POINTER_UP")
            }

            MotionEvent.ACTION_POINTER_DOWN -> {
                currentPointerId = event.getPointerId(event.actionIndex)
                val pointerIndex = event.findPointerIndex(currentPointerId)
                downX = event.getX(pointerIndex)
                downY = event.getY(pointerIndex)
                lastOffsetX = offsetX  //累积上一次的offset
                lastOffsetY = offsetY
                printSimpleLog(
                    "ACTION_POINTER_DOWN ${event.actionIndex} id- ${event.getPointerId(event.actionIndex)}"
                )

            }

        }
        return true
    }


    override fun onDraw(canvas: Canvas) {
        canvas.drawBitmap(bitmap, offsetX, offsetY, paint)
    }


}