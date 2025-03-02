package pan.lib.baseandroidframework.ui.main

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import pan.lib.baseandroidframework.R
import pan.lib.baseandroidframework.databinding.ActivityComposeViewInAndroidBinding
import pan.lib.baseandroidframework.ui.main.compose_demo.AndroidViewInCompose

class ComposeViewInAndroidActivity : AppCompatActivity() {

    private val binding: ActivityComposeViewInAndroidBinding by lazy {
        ActivityComposeViewInAndroidBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.composeView.setContent {
            //在原生Android View中嵌入Compose，嵌入的compose view里面也可以嵌入原生Android View
            AndroidViewInCompose()
        }
    }
}