package pan.lib.baseandroidframework.ui.main

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.IBinder.DeathRecipient
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pan.lib.baseandroidframework.Book
import pan.lib.baseandroidframework.IBookManager
import pan.lib.baseandroidframework.IOnNewBookArrivedListener
import pan.lib.baseandroidframework.databinding.ActivityBinderCustomerBinding
import pan.lib.baseandroidframework.services.BookManagerService
import pan.lib.common_lib.base.BaseActivity


class BinderCustomerActivity : BaseActivity() {
    private lateinit var binding: ActivityBinderCustomerBinding
    var iBookManager: IBookManager? = null
    var isBind = false

    private val mOnNewBookArrivedListener = object : IOnNewBookArrivedListener.Stub() {
        override fun onNewBookArrived(newBook: Book?) {
            lifecycleScope.launch(Dispatchers.Main) {
                binding.newBook.text = "new book arrival $newBook"
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
            isBind = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
            iBookManager = null
            isBind = false
        }
    }


    override fun getLayout(layoutInflater: LayoutInflater): View {
        binding = ActivityBinderCustomerBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle("Binder")
        binding.btRequestBinder.setOnClickListener {
            bindBookManagerService()
        }

        binding.btAddBook.setOnClickListener {
            iBookManager?.addBook(Book(2, "book_2"))
        }

        binding.btGetBook.setOnClickListener {

            lifecycleScope.launch {
                val booksText = withContext(Dispatchers.IO) {
                    //binder请求会挂起线程 所以耗时操作不能在UI线程执行
                    iBookManager?.bookList
                }.toString()
                binding.text.text = booksText
            }

        }

        binding.btRegister.setOnClickListener {
            iBookManager?.registerListener(mOnNewBookArrivedListener)
        }

        binding.btRemove.setOnClickListener {
            iBookManager?.unregisterListener(mOnNewBookArrivedListener)
        }

    }


    private fun bindBookManagerService() {
        val intent = Intent(this, BookManagerService::class.java)
        bindService(intent, serviceConnection, BIND_AUTO_CREATE)
    }


    override fun onDestroy() {
        if (isBind) {
            unbindService(serviceConnection)
        }
        super.onDestroy()
    }
}