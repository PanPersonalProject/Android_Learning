package pan.lib.baseandroidframework.ui.main


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import pan.lib.baseandroidframework.R

class CustomerViewDemoListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_view_demo_list)

        // 为每个Type设置按钮点击事件
        setButtonTypeListeners()
    }

    private fun setButtonTypeListeners() {
        with(CustomerViewDemoActivity.Type.values()) {
            forEachIndexed { _, type ->
                val buttonId = resources.getIdentifier("button_${type.name.lowercase()}", "id", packageName)
                val button = findViewById<Button>(buttonId)

                button?.setOnClickListener {
                    val intent = Intent(this@CustomerViewDemoListActivity, CustomerViewDemoActivity::class.java)
                    intent.putExtra("TYPE", type.ordinal)
                    startActivity(intent)
                }
            }
        }
    }
}