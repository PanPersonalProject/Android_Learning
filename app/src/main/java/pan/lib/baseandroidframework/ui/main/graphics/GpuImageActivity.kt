package pan.lib.baseandroidframework.ui.main.graphics

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import jp.co.cyberagent.android.gpuimage.GPUImage
import jp.co.cyberagent.android.gpuimage.GPUImageView
import jp.co.cyberagent.android.gpuimage.filter.*
import pan.lib.baseandroidframework.R
import pan.lib.baseandroidframework.ui.theme.AndroidLearningTheme

class GpuImageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidLearningTheme {
                GpuImageScreen()
            }
        }
    }

    // 更新 GpuImageActivity.kt 中的 GpuImageScreen 函数
    @Composable
    fun GpuImageScreen(viewModel: GpuImageViewModel = viewModel()) {
        // 当前滤镜索引
        val currentFilterIndex by viewModel.currentFilterIndex.collectAsState()
        // 当前滤镜值
        val currentFilterValue by viewModel.currentFilterValue.collectAsState()
        // 当前滤镜配置
        val currentFilterConfig = filterList[currentFilterIndex]

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AndroidView(
                modifier = Modifier.weight(1f),
                factory = { context ->
                    GPUImageView(context).apply {
                        val bitmap = BitmapFactory.decodeResource(context.resources, R.mipmap.bj)
                        setScaleType(GPUImage.ScaleType.CENTER_INSIDE)
                        filter = currentFilterConfig.filter
                        setImage(bitmap)
                    }
                },
                update = { view ->
                    when (val filter = currentFilterConfig.filter) {
                        is GPUImageContrastFilter -> filter.setContrast(currentFilterValue)
                        is GPUImageBrightnessFilter -> filter.setBrightness(currentFilterValue)
                        is GPUImageSaturationFilter -> filter.setSaturation(currentFilterValue)
                        is GPUImageHueFilter -> filter.setHue(currentFilterValue * 360) // 角度
                        is GPUImageGammaFilter -> filter.setGamma(currentFilterValue)
                        is GPUImageSharpenFilter -> filter.setSharpness(currentFilterValue)
                        is GPUImageExposureFilter -> filter.setExposure(currentFilterValue)
                        is GPUImageHighlightShadowFilter -> filter.setShadows(currentFilterValue)
                        is GPUImageMonochromeFilter -> filter.setIntensity(currentFilterValue)
                        is GPUImageEmbossFilter -> filter.intensity = currentFilterValue
                        is GPUImageVignetteFilter -> filter.setVignetteStart(currentFilterValue)
                        is GPUImageOpacityFilter -> filter.setOpacity(currentFilterValue)
                        is GPUImageRGBFilter -> filter.setRed(currentFilterValue)
                        // 添加其他filter
                    }
                    view.filter = currentFilterConfig.filter
                }
            )
            Text(text = currentFilterConfig.name, modifier = Modifier.padding(16.dp))
            if (currentFilterConfig.supportsSlider) {
                Slider(
                    value = currentFilterValue,
                    onValueChange = { viewModel.updateFilterValue(it) },
                    valueRange = currentFilterConfig.valueRange,
                    modifier = Modifier.padding(16.dp)
                )
            }
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(onClick = { viewModel.previousFilter(filterList.size) }) {
                    Text("上一滤镜")
                }
                Button(onClick = { viewModel.nextFilter(filterList.size) }) {
                    Text("下一滤镜")
                }
            }
        }
    }
}