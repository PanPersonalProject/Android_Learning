package pan.lib.baseandroidframework.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import pan.lib.baseandroidframework.Book
import pan.lib.baseandroidframework.IBookManager
import pan.lib.baseandroidframework.IOnNewBookArrivedListener
import java.util.concurrent.CopyOnWriteArrayList

class BookManagerService : Service() {

    val books = CopyOnWriteArrayList<Book>()
    private val booksListener = CopyOnWriteArrayList<IOnNewBookArrivedListener>()

    override fun onCreate() {
        books.add(Book(0, "book_0"))
        books.add(Book(1, "book_1"))
      flow {
            var counter = 2

            while (true) {
                val book = Book(counter, "book_$counter")
                counter++
                delay(500) // Suspends the coroutine for some time
                emit(book)
            }
        }.collect {

      }
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

        override fun registerListener(listener: IOnNewBookArrivedListener?) {
            if (!booksListener.contains(listener))
                booksListener.add(listener)
        }

        override fun unregisterListener(listener: IOnNewBookArrivedListener?) {
            if (booksListener.contains(listener))
                booksListener.remove(listener)
        }

    }

    override fun onBind(intent: Intent): IBinder = serviceBinder
}