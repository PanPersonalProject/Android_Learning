package pan.lib.baseandroidframework.ui.compose_views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * @author pan qi
 * @since 2024/7/5
 *Scaffold界面框架控件:可以为最常见的顶级 Material 组件（如 TopAppBar、BottomAppBar、FloatingActionButton 和 Drawer）提供槽位
 */

@Composable
fun MainScaffold(
    title: String = "",
    needShowArrowBack: Boolean = true,
    onNavigateBack: () -> Unit = {},
    content: @Composable (innerPadding: Modifier) -> Unit,
) {
    Scaffold(
        topBar = {
            if (title.isNotEmpty()) {
                TopBar(title, needShowArrowBack, onNavigateBack)
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.TopStart
        ) {
            content(Modifier.padding(innerPadding))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(title: String, needShowArrowBack: Boolean, onNavigateBack: () -> Unit) {
    CenterAlignedTopAppBar(
        title = { Text(title) },
        navigationIcon = {
            if (needShowArrowBack) {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
                }
            }

        },
    )
}
