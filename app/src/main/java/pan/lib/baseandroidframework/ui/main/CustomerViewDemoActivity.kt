package pan.lib.baseandroidframework.ui.main


import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_customer_view_demo.*
import pan.lib.baseandroidframework.R
import pan.lib.common_lib.base.BaseActivity

class CustomerViewDemoActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        picAnimator()
    }

    private fun picAnimator() {
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

    override fun getLayoutId() = R.layout.activity_customer_view_demo
}