package pan.lib.baseandroidframework.ui.main

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity
import pan.lib.baseandroidframework.R
import pan.lib.baseandroidframework.java_demo.dex_class_loader.DexClassLoaderUtil
import pan.lib.common_lib.base.BaseActivity

class MainActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle("首页")

        btCustomView.setOnClickListener {
            startActivity<CustomerViewDemoActivity>()
        }

        btDexClassLoader.setOnClickListener {
            DexClassLoaderUtil.loadApk(this)
        }

    }

    override fun getLayoutId() = R.layout.activity_main
}
