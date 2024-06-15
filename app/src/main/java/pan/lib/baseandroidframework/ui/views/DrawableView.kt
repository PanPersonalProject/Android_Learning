package pan.lib.baseandroidframework.ui.views

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View

/**
 * @author pan qi
 * @since 2024/6/15
 * 测试自定义Drawable
 */
class DrawableView(context: Context, attrs: AttributeSet) :
    View(context, attrs) {

    private val gridDrawable =GridDrawable()

    override fun onDraw(canvas: Canvas) {
        gridDrawable.setBounds(0, 0, width, height)
        gridDrawable.draw(canvas)
    }
}