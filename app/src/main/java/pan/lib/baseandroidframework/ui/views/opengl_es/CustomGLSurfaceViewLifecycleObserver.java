package pan.lib.baseandroidframework.ui.views.opengl_es;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

public class CustomGLSurfaceViewLifecycleObserver implements DefaultLifecycleObserver {
    private final CustomGLSurfaceView mGLSurfaceView;

    public CustomGLSurfaceViewLifecycleObserver(CustomGLSurfaceView glSurfaceView) {
        mGLSurfaceView = glSurfaceView;
    }

    @Override
    public void onResume(@NonNull LifecycleOwner owner) {
        // The activity must call the GL surface view's onResume() on activity onResume().
        mGLSurfaceView.onResume();
    }

    @Override
    public void onPause(@NonNull LifecycleOwner owner) {
        // The activity must call the GL surface view's onPause() on activity onPause().
        mGLSurfaceView.onPause();
    }
}