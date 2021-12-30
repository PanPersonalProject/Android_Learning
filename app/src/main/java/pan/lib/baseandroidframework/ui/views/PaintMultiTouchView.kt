package pan.lib.baseandroidframework.ui.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import pan.lib.common_lib.utils.ext.dp2px

/**
 * AUTHOR Pan Created on 2021/12/30
 */
class PaintMultiTouchView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val pathMap = mutableMapOf<Int, Path>()

    init {
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 4.dp2px
        paint.strokeCap = Paint.Cap.ROUND
        paint.strokeJoin = Paint.Join.ROUND

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                val path = getPath(event.getPointerId(0))
                path.moveTo(event.x, event.y)
                invalidate()
            }

            MotionEvent.ACTION_MOVE -> {
                for (index in 0 until event.pointerCount) {
                    val path = getPath(event.getPointerId(index))
                    path.lineTo(event.getX(index), event.getY(index))

                }

                invalidate()
            }


            MotionEvent.ACTION_POINTER_DOWN -> {
                val path = getPath(event.getPointerId(event.actionIndex))
                path.moveTo(event.getX(event.actionIndex), event.getY(event.actionIndex))
                invalidate()

            }

        }
        return true

    }

    private fun getPath(pointerId: Int): Path {
        var path = pathMap[pointerId]
        if (path == null) {
            path = Path()
            pathMap[pointerId] = path
        }
        return path
    }

    override fun onDraw(canvas: Canvas) {
        pathMap.forEach {
            canvas.drawPath(it.value, paint)
        }

    }


}