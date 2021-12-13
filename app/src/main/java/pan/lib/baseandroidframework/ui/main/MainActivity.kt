package pan.lib.baseandroidframework.ui.main

import android.os.Bundle
import android.widget.SeekBar
import com.example.demo.ui.top.TopArticleActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity
import pan.lib.baseandroidframework.R
import pan.lib.common_lib.base.BaseActivity

class MainActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle("首页")
        btNet.setOnClickListener {
            startActivity<TopArticleActivity>()
        }

        btCustomView.setOnClickListener {
            startActivity<CustomerViewDemoActivity>()
        }


    }

    override fun getLayoutId() = R.layout.activity_main
}
