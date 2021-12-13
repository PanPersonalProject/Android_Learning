package pan.lib.baseandroidframework.ui.main


import android.animation.ObjectAnimator
import android.os.Bundle
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_customer_view_demo.*
import pan.lib.baseandroidframework.R
import pan.lib.common_lib.base.BaseActivity
import android.animation.AnimatorSet

class CustomerViewDemoActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                cameraView.rotateX3d(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })
        val bottomFlipAnimator=ObjectAnimator.ofFloat(cameraView,"bottomFlip",45f)
        bottomFlipAnimator.duration = 1500
        val flipRotationAnimator=ObjectAnimator.ofFloat(cameraView,"flipRotation",270f)
        bottomFlipAnimator.duration = 1500
        val topFlipAnimator=ObjectAnimator.ofFloat(cameraView,"topFlip",45f)
        topFlipAnimator.duration = 1500

        val animatorSet = AnimatorSet()
        animatorSet.playSequentially(bottomFlipAnimator)
//        animatorSet.playSequentially(bottomFlipAnimator, flipRotationAnimator, topFlipAnimator)
        animatorSet.startDelay = 1000
        animatorSet.start()
    }

    override fun getLayoutId()=R.layout.activity_customer_view_demo
}