package pan.lib.baseandroidframework.ui.views

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import pan.lib.baseandroidframework.R
import pan.lib.common_lib.utils.ext.dp2px
import pan.lib.common_lib.utils.makeBitmap

/**
 * @author pan qi
 * @since 2024/6/15
 * Canvas平移旋转顺序测试
 */
class CanvasCoordinateSystemTestView(context: Context, attrs: AttributeSet) :
    View(context, attrs) {

    private var bitmap: Bitmap = makeBitmap(R.mipmap.bj, 120.dp2px.toInt())
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onDraw(canvas: Canvas) {
        canvas.save()
        //变换操作的顺序会显著影响最终的绘制结果
        canvas.translate(100.dp2px, 100.dp2px)
        canvas.rotate(45f)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        canvas.restore()
    }
}