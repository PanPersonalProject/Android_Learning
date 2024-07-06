package pan.lib.baseandroidframework.ui.main


import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pan.lib.baseandroidframework.ui.compose_views.MainScaffold
import pan.lib.baseandroidframework.ui.theme.AndroidLearningTheme

class CustomerViewDemoListActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainScreen()
        }

    }

    @Preview(
        uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL,
        backgroundColor = 0xFFFFEAEE,
        showBackground = true
    )
    @Composable
    private fun MainScreen() {
        AndroidLearningTheme {
            MainScaffold(title = "AndroidLearning") {
                DemoListScreen { type ->
                    val intent = Intent(this, CustomerViewDemoActivity::class.java)
                    intent.putExtra("TYPE", type.ordinal)
                    startActivity(intent)
                }
            }
        }
    }

    @Composable
    fun DemoListScreen(onButtonClick: (CustomerViewDemoActivity.Type) -> Unit) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CustomerViewDemoActivity.Type.entries.forEach { type ->
                Button(
                    onClick = { onButtonClick(type) },
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Text(text = type.displayName)
                }
            }
        }
    }
}