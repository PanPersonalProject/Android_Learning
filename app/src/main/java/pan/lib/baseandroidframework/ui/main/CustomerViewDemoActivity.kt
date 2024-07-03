package pan.lib.baseandroidframework.ui.main


import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

import pan.lib.baseandroidframework.R
import pan.lib.baseandroidframework.ui.adapter.TopArticleAdapter
import pan.lib.baseandroidframework.ui.views.CameraView

class CustomerViewDemoActivity : AppCompatActivity() {
    enum class Type {
        DEMO1, DEMO2, SCALE_IMAGE, MULTI_TOUCH, PAINT_MULTI_TOUCH, TWO_PAGE, DRAG_LISTENER, DRAG_HELPER,
        SCROLL, CANVAS_COORDINATE_SYSTEM
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 获取传递进来的Type枚举值
        val typeOrdinal = intent.getIntExtra("TYPE", Type.DEMO1.ordinal)
        val selectedType = Type.values()[typeOrdinal]

        when (selectedType) {
            Type.DEMO1 -> {
                setContentView(R.layout.activity_customer_view_demo)
                picAnimator()
            }
            Type.DEMO2 -> setContentView(R.layout.activity_customer_view_demo2)
            Type.SCALE_IMAGE -> setContentView(R.layout.activity_customer_view_demo2_scaleable_image)
            Type.MULTI_TOUCH -> setContentView(R.layout.activity_customer_view_demo2_multi_touch)
            Type.PAINT_MULTI_TOUCH -> setContentView(R.layout.activity_customer_view_demo2_paint_multi_touch)
            Type.TWO_PAGE -> setContentView(R.layout.activity_customer_view_demo2_two_pager)
            Type.DRAG_LISTENER -> setContentView(R.layout.activity_customer_view_demo2_drag_listener)
            Type.DRAG_HELPER -> setContentView(R.layout.activity_customer_view_demo2_drag_up_down)
            Type.SCROLL -> {
                setContentView(R.layout.activity_customer_view_demo2_scroll)
                nestedScroll()
            }
            Type.CANVAS_COORDINATE_SYSTEM -> setContentView(R.layout.activity_canvas_coordinate_system)
        }
    }

    //DEMO1
    private fun picAnimator() {
        val cameraView=findViewById<CameraView>(R.id.cameraView)
        val bottomFlipAnimator = ObjectAnimator.ofFloat(cameraView, "bottomFlip", 45f)
        bottomFlipAnimator.duration = 1500

        val flipRotationAnimator = ObjectAnimator.ofFloat(cameraView, "flipRotation", 270f)
        flipRotationAnimator.duration = 1500

        val topFlipAnimator = ObjectAnimator.ofFloat(cameraView, "bottomFlip", 45f, 0f)
        topFlipAnimator.duration = 1500
        val animatorSet = AnimatorSet()
        animatorSet.playSequentially(bottomFlipAnimator, flipRotationAnimator, topFlipAnimator)
        animatorSet.startDelay = 1000
        animatorSet.start()
    }

    /**
    NestedScrollView嵌套RecyclerView,如果RecyclerView先限制高度,RecyclerView会将自己高度测量为所有item的高度,导致一次性加载所以item
    所以NestedScrollView嵌套RecyclerView 应该限制recyclerview高度*/
    private fun nestedScroll() {
        val topArticleAdapter = TopArticleAdapter().apply {
            val list = mutableListOf<String>()
            for (index in 0..20) {
                list.add("index $index")
            }
            setNewInstance(list)
        }
        val scrollRecyclerView = findViewById<RecyclerView>(R.id.scrollRecyclerView)
        scrollRecyclerView.adapter = topArticleAdapter

    }

}