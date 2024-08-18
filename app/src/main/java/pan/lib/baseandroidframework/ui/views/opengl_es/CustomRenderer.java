package pan.lib.baseandroidframework.ui.views.opengl_es;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class CustomRenderer implements GLSurfaceView.Renderer {
    private final FloatBuffer mTriangle1Vertices;

    public CustomRenderer() {
        final float[] triangle1VerticesData = {
                // X, Y, Z,
                // R, G, B, A

                //这表示三角形的一个点，前三个数字代表位置（X、Y 和 Z），
                -0.5f, -0.25f, 0.0f,
                //代表颜色（红色、绿色、蓝色和 alpha（透明度））
                1.0f, 0.0f, 0.0f, 1.0f,

                0.5f, -0.25f, 0.0f,
                0.0f, 0.0f, 1.0f, 1.0f,

                0.0f, 0.559016994f, 0.0f,
                0.0f, 1.0f, 0.0f, 1.0f};


        int mBytesPerFloat = 4;//每个浮点数占4个字节
        // OpenGL ES 底层是 C 语言，所以需要使用 ByteBuffer 来存储数据
        mTriangle1Vertices = ByteBuffer.allocateDirect(triangle1VerticesData.length * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();

        //将 triangle1VerticesData 数组中的所有元素放入 mTriangle1Vertices 缓冲区。
        //.position(0): 将缓冲区的位置设置为 0，准备从缓冲区的开头开始读取数据。
        mTriangle1Vertices.put(triangle1VerticesData).position(0);
    }

    /**
     * 当 GLSurfaceView 的表面被创建时调用。
     * 通常在这里设置 OpenGL 环境的初始状态，比如清除颜色。
     */
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // 设置背景帧颜色为灰色
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 0.5f);
        // 启用了顶点属性数组。在 OpenGL 中，顶点数据（如位置、颜色等）存储在缓冲区对象中，
        // 然后通过顶点属性数组传递给着色器。这里的参数 0 是顶点属性的索引。
        GLES20.glEnableVertexAttribArray(0);

        // 创建并编译顶点着色器
        String vertexShaderCode =
                "attribute vec4 vPosition;" +
                        "void main() {" +
                        "  gl_Position = vPosition;" +
                        "}";
        int vertexShader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        GLES20.glShaderSource(vertexShader, vertexShaderCode);
        GLES20.glCompileShader(vertexShader);

        // 创建并编译片段着色器
        String fragmentShaderCode =
                "precision mediump float;" +
                        "uniform vec4 vColor;" +
                        "void main() {" +
                        "  gl_FragColor = vColor;" +
                        "}";
        int fragmentShader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
        GLES20.glShaderSource(fragmentShader, fragmentShaderCode);
        GLES20.glCompileShader(fragmentShader);

        // 创建着色器程序并链接着色器
        int shaderProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(shaderProgram, vertexShader);
        GLES20.glAttachShader(shaderProgram, fragmentShader);
        GLES20.glLinkProgram(shaderProgram);

        // 使用着色器程序
        GLES20.glUseProgram(shaderProgram);

        // 获取位置句柄
        int positionHandle = GLES20.glGetAttribLocation(shaderProgram, "vPosition");

        // 启用位置句柄
        GLES20.glEnableVertexAttribArray(positionHandle);

        // 获取颜色句柄
        int colorHandle = GLES20.glGetUniformLocation(shaderProgram, "vColor");

        // 设置绘制三角形的颜色
        GLES20.glUniform4f(colorHandle, 1.0f, 0.0f, 0.0f, 1.0f); // 红色
    }

    /**
     * 每次需要绘制一个新帧时调用，在这里执行所有的绘制操作。
     */
    @Override
    public void onDrawFrame(GL10 gl) {
        // 重新绘制背景颜色
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        // 设置顶点属性指针
        mTriangle1Vertices.position(0);
        GLES20.glVertexAttribPointer(0, 3, GLES20.GL_FLOAT, false, 7 * 4, mTriangle1Vertices);

        // 绘制三角形
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
    }

    /**
     * 当 GLSurfaceView 的表面尺寸发生变化时调用，
     * 比如当设备的屏幕方向改变时，在这里调整视口的大小。
     */
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        // 根据几何变化调整视口
        GLES20.glViewport(0, 0, width, height);
    }
}