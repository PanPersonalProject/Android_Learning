package pan.lib.baseandroidframework.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import pan.lib.baseandroidframework.R

class CustomerViewDemoActivity2 : AppCompatActivity() {
    enum class Type {
        DEMO2, SCALE_IMAGE, MULTI_TOUCH, PAINT_MULTI_TOUCH, TWO_PAGE, DRAG_LISTENER, DRAG_HELPER
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        when (Type.DRAG_HELPER) {
            Type.DEMO2 -> setContentView(R.layout.activity_customer_view_demo2)
            Type.SCALE_IMAGE -> setContentView(R.layout.activity_customer_view_demo2_scaleable_image)
            Type.MULTI_TOUCH -> setContentView(R.layout.activity_customer_view_demo2_multi_touch)
            Type.PAINT_MULTI_TOUCH -> setContentView(R.layout.activity_customer_view_demo2_paint_multi_touch)
            Type.TWO_PAGE -> setContentView(R.layout.activity_customer_view_demo2_two_pager)
            Type.DRAG_LISTENER -> setContentView(R.layout.activity_customer_view_demo2_drag_listener)
            Type.DRAG_HELPER -> setContentView(R.layout.activity_customer_view_demo2_drag_up_down)
        }
    }

}