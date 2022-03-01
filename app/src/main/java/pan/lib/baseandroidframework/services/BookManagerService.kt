package pan.lib.baseandroidframework.services

import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.IBinder
import android.os.Parcel
import android.os.RemoteCallbackList
import android.os.RemoteException
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import pan.lib.baseandroidframework.Book
import pan.lib.baseandroidframework.IBookManager
import pan.lib.baseandroidframework.IOnNewBookArrivedListener
import java.util.concurrent.CopyOnWriteArrayList

class BookManagerService : Service() {

    val books = CopyOnWriteArrayList<Book>()

    //跨进程会生成新的对象,但binder对象不变,所以使用RemoteCallbackList,内部数据结构ArrayMap<IBinder, Callback> mCallbacks
    private val booksListeners = RemoteCallbackList<IOnNewBookArrivedListener>()
    private var job = Job()

    override fun onCreate() {
        GlobalScope.launch(Dispatchers.Main + job) {
            flow {
                var counter = 0

                while (true) {
                    val book = Book(counter, "book_$counter")
                    counter++
                    delay(3000)
                    emit(book)
                }
            }.collect {
                books.add(it)
                notifyDataChanged(it)

            }
        }

    }

    private fun notifyDataChanged(book: Book) {
        for (i in 0 until booksListeners.beginBroadcast()) {
            try {
                booksListeners.getBroadcastItem(i).onNewBookArrived(book)
            } catch (remoteException: RemoteException) {
                remoteException.printStackTrace()
            }
        }
        booksListeners.finishBroadcast()
    }


    private val serviceBinder = object : IBookManager.Stub() {

        override fun onTransact(code: Int, data: Parcel, reply: Parcel?, flags: Int): Boolean {
            //如果包名不是pan.lib开头,禁止IPC
            val packagesForUid = packageManager.getPackagesForUid(getCallingUid())
            if (!packagesForUid.isNullOrEmpty()) {
                if (!packagesForUid[0].startsWith("pan.lib")) {
                    return false
                }

            }
            return super.onTransact(code, data, reply, flags)
        }

        override fun getBookList(): MutableList<Book> {
            //这里可以模拟耗时操作,阻塞client线程,所以耗时操作不应该
            Thread.sleep(3000)
            return books
        }

        override fun addBook(book: Book?) {
            book?.let { books.add(it) }
        }

        override fun registerListener(listener: IOnNewBookArrivedListener?) {
            booksListeners.register(listener)
        }

        override fun unregisterListener(listener: IOnNewBookArrivedListener?) {
            booksListeners.unregister(listener)
        }


    }

    override fun onBind(intent: Intent): IBinder? {

        val check =
            checkCallingOrSelfPermission("pan.lib.baseandroidframework.ACCESS_BOOK_SERVICE")

        //如果AndroidManifest没有申请ACCESS_BOOK_SERVICE权限则无法绑定
        if (check == PackageManager.PERMISSION_DENIED) {
            return null;
        }
        return serviceBinder

    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}