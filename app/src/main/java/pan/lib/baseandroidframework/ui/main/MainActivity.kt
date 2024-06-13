package pan.lib.baseandroidframework.ui.main

import android.os.Bundle
import android.os.Looper
import kotlinx.android.synthetic.main.activity_main.btCustomView
import kotlinx.android.synthetic.main.activity_main.btDexClassLoader
import kotlinx.android.synthetic.main.activity_main.btDynamicProxy
import kotlinx.android.synthetic.main.activity_main.btLeak
import org.jetbrains.anko.startActivity
import pan.lib.baseandroidframework.R
import pan.lib.baseandroidframework.java_demo.dex_class_loader.DexClassLoaderUtil
import pan.lib.baseandroidframework.java_demo.dynamic_proxy.dynamicProxyExample
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

        Looper.getMainLooper().queue.addIdleHandler {
            return@addIdleHandler false
        }

        btLeak.setOnClickListener {
            startActivity<LeakActivity>()
        }

        btDynamicProxy.setOnClickListener {
            dynamicProxyExample()

        }
    }

    override fun getLayoutId() = R.layout.activity_main
}
