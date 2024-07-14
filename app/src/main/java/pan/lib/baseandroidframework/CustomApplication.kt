package pan.lib.baseandroidframework

import android.R.attr
import android.util.Log
import pan.lib.baseandroidframework.util.watch_dog.ANRWatchDog
import pan.lib.common_lib.base.BaseApplication
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.ObjectOutputStream


/**
 *
 * Author:         pan qi
 * CreateDate:     2020/6/3 15:44
 */
class CustomApplication : BaseApplication() {
    private var duration = 2
    override fun onCreate() {
        super.onCreate()

        //启动一个异步线程，在 while 循环中，使用主线程的 Handler 立即发送一个消息，线程休眠指定的时间 interval，
        //线程唤醒之后，如果发送的消息还没被主线程执行，即认为主线程发生了卡顿。
        ANRWatchDog().setIgnoreDebugger(true).setANRListener { error ->
            Log.e("ANR-Watchdog-Demo", "Detected Application Not Responding!")
            // Some tools like ACRA are serializing the exception, so we must make sure the exception serializes correctly
            try {
                ObjectOutputStream(ByteArrayOutputStream()).writeObject(error)
            } catch (ex: IOException) {
                throw RuntimeException(ex)
            }

            Log.i("ANR-Watchdog-Demo", "Error was successfully serialized")
            throw error
        }.setANRInterceptor { duration ->
            val ret: Long = this.duration * 1000 - duration
            if (ret > 0) Log.w(
                "ANR-Watchdog-Demo",
                "Intercepted ANR that is too short (" + attr.duration + " ms), postponing for " + ret + " ms."
            )
            ret
        }.start()
    }
}