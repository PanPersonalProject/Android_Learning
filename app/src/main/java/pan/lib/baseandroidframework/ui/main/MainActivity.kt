package pan.lib.baseandroidframework.ui.main

import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View

import org.jetbrains.anko.startActivity
import pan.lib.baseandroidframework.databinding.ActivityMainBinding
import pan.lib.baseandroidframework.java_demo.dex_class_loader.DexClassLoaderUtil
import pan.lib.baseandroidframework.java_demo.dynamic_proxy.dynamicProxyExample
import pan.lib.common_lib.base.BaseActivity

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle("首页")

        binding.btCustomView.setOnClickListener {
            startActivity<CustomerViewDemoListActivity>()
        }

        binding.btDexClassLoader.setOnClickListener {
            DexClassLoaderUtil.loadApk(this)
        }

        Looper.getMainLooper().queue.addIdleHandler {
            return@addIdleHandler false
        }

        binding.btLeak.setOnClickListener {
            startActivity<LeakActivity>()
        }

        binding.btDynamicProxy.setOnClickListener {
            dynamicProxyExample()

        }
        binding.btRecyclerViewDemo.setOnClickListener {
            startActivity<RecyclerviewDemoActivity>()
        }
    }

    override fun getLayout(layoutInflater: LayoutInflater): View {
        binding = ActivityMainBinding.inflate(layoutInflater)
        return binding.root
    }


}
