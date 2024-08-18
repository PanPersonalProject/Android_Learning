package pan.lib.baseandroidframework.ui.main.graphics

import jp.co.cyberagent.android.gpuimage.filter.*

data class FilterConfig(
    // 滤镜对象
    val filter: GPUImageFilter,
    // 滤镜名称
    val name: String,
    // 是否支持滑动条
    val supportsSlider: Boolean,
    // 默认值
    val defaultValue: Float,
    // 滑动条取值范围
    val valueRange: ClosedFloatingPointRange<Float>
)

// 滤镜配置列表
val filterList = listOf(
    FilterConfig(GPUImageHueFilter(90.0f), "色调滤镜", true, 90.0f / 360.0f, 0.0f..1.0f),
    FilterConfig(GPUImageGrayscaleFilter(), "灰度滤镜", false, 1.0f, 0.0f..2.0f),
    FilterConfig(GPUImageContrastFilter(2.0f), "对比度滤镜", true, 2.0f, 0.0f..4.0f),
    FilterConfig(GPUImageBrightnessFilter(0.1f), "亮度滤镜", true, 0.1f, -1.0f..1.0f),
    FilterConfig(GPUImageSaturationFilter(1.5f), "饱和度滤镜", true, 1.5f, 0.0f..2.0f),
    FilterConfig(GPUImageGammaFilter(1.0f), "伽马滤镜", true, 1.0f, 0.0f..3.0f),
    FilterConfig(GPUImageSharpenFilter(2.0f), "锐化滤镜", true, 2.0f, -4.0f..4.0f),
    FilterConfig(GPUImageEmbossFilter(), "浮雕滤镜", true, 1.0f, 0.0f..2.0f),
    FilterConfig(GPUImageVignetteFilter(), "暗角滤镜", true, 1.0f, 0.0f..1.0f),
    FilterConfig(GPUImageExposureFilter(0.5f), "曝光滤镜", true, 0.5f, -10.0f..10.0f),
    FilterConfig(GPUImageHighlightShadowFilter(0.0f, 1.0f), "高光阴影滤镜", true, 0.5f, 0.0f..1.0f),
    FilterConfig(GPUImageOpacityFilter(1.0f), "不透明度滤镜", true, 1.0f, 0.0f..1.0f),
    FilterConfig(GPUImageRGBFilter(1.0f, 1.0f, 1.0f), "RGB滤镜", true, 1.0f, 0.0f..2.0f)
)