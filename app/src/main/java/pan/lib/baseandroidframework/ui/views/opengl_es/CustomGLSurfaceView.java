package pan.lib.baseandroidframework.ui.views.opengl_es;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;

import androidx.lifecycle.Lifecycle;

/**
 * GLSurfaceView为 OpenGL 提供了专用的渲染线程，以便主线程不会停滞。
 * <br>
 * 它支持连续或按需渲染。
 * <br>
 * 它使用EGL（OpenGL和底层窗口系统之间的接口）为您完成屏幕设置
 */
public class CustomGLSurfaceView extends GLSurfaceView {


    public CustomGLSurfaceView(Context context) {
        super(context);
    }

    public void init(GLSurfaceView.Renderer renderer) {
        // 检查设备是否支持 OpenGL ES 3.0
        if (supportsOpenGLES3(getContext())) {
            setEGLContextClientVersion(3);
        } else {
            setEGLContextClientVersion(2);
        }

        // 配置 EGL 设置
        setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        getHolder().setFormat(PixelFormat.RGBA_8888);

        setRenderer(renderer);// 设置渲染器

        /*
         * RENDERMODE_WHEN_DIRTY:
         * 描述: 渲染器仅在表面创建时或调用 requestRender() 时进行渲染。
         * 适用场景: 适用于静态场景或不需要频繁更新的应用，可以通过手动调用 requestRender() 来触发渲染，从而节省电量和提高性能。
         *
         * RENDERMODE_CONTINUOUSLY:
         * 描述: 渲染器会连续调用以重新渲染场景。
         * 适用场景: 适用于需要持续更新画面的应用，如游戏或动画，确保画面流畅。
         */
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

        /*
         * DEBUG_CHECK_GL_ERROR:
         * 描述: 在每次 GL 调用后检查 glError()，如果 glError 表示发生了错误，则抛出异常。
         * 适用场景: 用于调试，帮助追踪哪个 OpenGL ES 调用导致了错误。
         *
         * DEBUG_LOG_GL_CALLS:
         * 描述: 将 GL 调用记录到系统日志中，日志级别为“verbose”，标签为“GLSurfaceView”。
         * 适用场景: 用于调试，记录所有 GL 调用以便分析。
         */
        setDebugFlags(DEBUG_LOG_GL_CALLS);
    }

    private boolean supportsOpenGLES3(Context context) {
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        return configurationInfo.reqGlEsVersion >= 0x30000;
    }


    public void registerLifecycle(Lifecycle lifecycle) {
        lifecycle.addObserver(new CustomGLSurfaceViewLifecycleObserver(this));
    }
}
