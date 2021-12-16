package pan.lib.baseandroidframework.ui.views

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import androidx.annotation.Keep
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.doAfterTextChanged
import pan.lib.common_lib.utils.ext.dp2px
import pan.lib.common_lib.utils.ext.sp

/**
 * AUTHOR Pan Created on 2021/12/16
 */
@Keep
class MaterialEditText(context: Context, attrs: AttributeSet) : AppCompatEditText(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textRect = Rect()
    private val topSpace = 20.dp2px.toInt()
    private val progressAnimator = ObjectAnimator.ofFloat(this, "labelProgress", 0f, 1f)

    private var labelProgress = 0f //1彻底显示label 0不显示label
        set(value) {
            field = value
            invalidate()

        }

    private var isBeforeNoText = true

    init {
        setPadding(paddingLeft, paddingTop + topSpace, paddingRight, paddingBottom)
        paint.style = Paint.Style.FILL
        paint.textSize = 15.sp.toFloat()

        paint.color = hintTextColors.defaultColor

        doAfterTextChanged {
            val text = it.toString()
            if (text.isEmpty() && !isBeforeNoText) {
                isBeforeNoText = true
                progressAnimator.reverse()
            } else if (text.isNotEmpty() && isBeforeNoText) {
                isBeforeNoText = false
                progressAnimator.start()
            }
        }

    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        paint.getTextBounds(hint.toString(), 0, hint.length, textRect)
        paint.alpha = (255 * labelProgress).toInt()
        val y = textRect.height().toFloat() +
                (topSpace - textRect.height().toFloat()) * (1 - labelProgress)
        canvas.drawText(hint.toString(), 4.dp2px, y, paint)


    }

}