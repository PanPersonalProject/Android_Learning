package pan.lib.baseandroidframework.ui.main

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import pan.lib.baseandroidframework.R

class LeakActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leak)
        LeakMockUtil.context = this
    }
}


//模拟内存泄漏,用于leakcanary检测
class LeakMockUtil {
    companion object {
        @SuppressLint("StaticFieldLeak")
        var context: Context? = null

    }
}