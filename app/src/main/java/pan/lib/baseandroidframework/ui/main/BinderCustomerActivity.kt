package pan.lib.baseandroidframework.ui.main

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_binder_customer.*
import pan.lib.baseandroidframework.IBookManager
import pan.lib.baseandroidframework.R
import pan.lib.common_lib.base.BaseActivity
import android.content.ComponentName
import android.content.Intent

import android.os.IBinder

import android.content.ServiceConnection
import android.os.IBinder.DeathRecipient
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pan.lib.baseandroidframework.Book
import pan.lib.baseandroidframework.IOnNewBookArrivedListener
import pan.lib.baseandroidframework.services.BookManagerService


class BinderCustomerActivity : BaseActivity() {
    var iBookManager: IBookManager? = null

    private val mOnNewBookArrivedListener = object : IOnNewBookArrivedListener.Stub() {
        override fun onNewBookArrived(newBook: Book?) {
            lifecycleScope.launch(Dispatchers.Main) {
                new_book.text = "new book arrival $newBook"
            }
        }
    }


    val deathRecipient: DeathRecipient = object : DeathRecipient {
        override fun binderDied() {
            iBookManager?.asBinder()?.unlinkToDeath(this, 0)
        }
    }

    private val serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, binder: IBinder) {
            iBookManager = IBookManager.Stub.asInterface(binder)
            binder.linkToDeath(deathRecipient, 0)
        }

        override fun onServiceDisconnected(name: ComponentName) {
            iBookManager = null
        }
    }

    override fun getLayoutId(): Int = R.layout.activity_binder_customer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle("Binder")
        btRequestBinder.setOnClickListener {
            bindBookManagerService()
        }

        btAddBook.setOnClickListener {
            iBookManager?.addBook(Book(2, "book_2"))
        }

        btGetBook.setOnClickListener {

            lifecycleScope.launch {
                val booksText = withContext(Dispatchers.IO) {
                    //binder请求会挂起线程 所以耗时操作不能在UI线程执行
                    iBookManager?.bookList
                }.toString()
                text.text = booksText
            }

        }

        btRegister.setOnClickListener {
            iBookManager?.registerListener(mOnNewBookArrivedListener)
        }

        btRemove.setOnClickListener {
            iBookManager?.unregisterListener(mOnNewBookArrivedListener)
        }

    }


    private fun bindBookManagerService() {
        val intent = Intent(this, BookManagerService::class.java)
        bindService(intent, serviceConnection, BIND_AUTO_CREATE)
    }


    override fun onDestroy() {
        unbindService(serviceConnection)
        super.onDestroy()
    }
}