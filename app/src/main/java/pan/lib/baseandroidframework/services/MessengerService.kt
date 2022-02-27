package pan.lib.baseandroidframework.services

import android.app.Service
import android.content.Intent
import android.os.*
import pan.lib.baseandroidframework.Book
import pan.lib.common_lib.utils.ext.toObject
import java.util.concurrent.CopyOnWriteArrayList
import android.os.Messenger

/**
 * AUTHOR Pan Created on 2022/2/27
 */
class MessengerService : Service() {

    companion object {
        const val ADD_BOOK = 1
        const val ADD_BOOK_REPLY = 2
        const val ADD_BOOK_KEY = "ADD_BOOK"
        const val ADD_BOOK_REPLY_KEY = "ADD_BOOK_REPLY"
    }

    private val books = CopyOnWriteArrayList<Book>()
    private val messenger = Messenger(MessengerHandler(books))

    private class MessengerHandler(val books: CopyOnWriteArrayList<Book>) :
        Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                ADD_BOOK -> {
                    Thread.sleep(1000)
                    val book = msg.data.getString(ADD_BOOK_KEY)?.toObject<Book>()
                    books.add(book)
                    replyClient(msg)

                }
            }
        }

        private fun replyClient(msg: Message) {
            val client = msg.replyTo
            val replyMessage: Message? = Message.obtain(null, ADD_BOOK_REPLY)
            val bundle = Bundle()
            bundle.putString(ADD_BOOK_REPLY_KEY, "已添加 现在一共有$books")
            replyMessage?.data = bundle
            try {
                client.send(replyMessage);
            } catch (e: RemoteException) {
                e.printStackTrace()
            }
        }
    }

    override fun onCreate() {
        books.add(Book(0, "book_0"))
        books.add(Book(1, "book_1"))
    }

    override fun onBind(intent: Intent?): IBinder? {
        return messenger.binder
    }
}