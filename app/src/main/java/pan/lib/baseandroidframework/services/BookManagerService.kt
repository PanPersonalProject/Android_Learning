package pan.lib.baseandroidframework.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import pan.lib.baseandroidframework.Book
import pan.lib.baseandroidframework.IBookManager
import java.util.concurrent.CopyOnWriteArrayList

class BookManagerService : Service() {

    val books = CopyOnWriteArrayList<Book>()

    override fun onCreate() {
        books.add(Book(0, "book_0"))
        books.add(Book(1, "book_1"))
    }

    private val serviceBinder = object : IBookManager.Stub() {
        override fun getBookList(): MutableList<Book> {
            //这里可以模拟耗时操作,阻塞client线程,所以耗时操作不应该
            Thread.sleep(3000)
            return books
        }

        override fun addBook(book: Book?) {
            book?.let { books.add(it) }
        }

    }

    override fun onBind(intent: Intent): IBinder = serviceBinder
}