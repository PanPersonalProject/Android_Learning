package pan.lib.baseandroidframework.ui.main

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.os.Messenger
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView

import pan.lib.baseandroidframework.Book
import pan.lib.baseandroidframework.databinding.ActivityMessengerCustomerBinding
import pan.lib.baseandroidframework.services.MessengerService
import pan.lib.common_lib.base.BaseActivity
import pan.lib.common_lib.utils.ext.toJson
import java.lang.ref.WeakReference


class MessengerCustomerActivity : BaseActivity() {
    private lateinit var binding: ActivityMessengerCustomerBinding
    private var messenger: Messenger? = null   //messenger一次处理一个请求 不会阻塞线程 通过handler机制通知
    private lateinit var replyMessenger: Messenger //用户服务端向客户端回复

    private val serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, binder: IBinder) {
            messenger = Messenger(binder)
        }

        override fun onServiceDisconnected(name: ComponentName) {
            messenger = null
        }
    }


    override fun getLayout(layoutInflater: LayoutInflater): View {
        binding = ActivityMessengerCustomerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle("Messenger")

        replyMessenger = Messenger(MessengerHandler(WeakReference(binding.text)))

        binding.btRequestBinder.setOnClickListener {
            bindMessengerService()
        }

        binding.btAddBook.setOnClickListener {
            val message = Message.obtain(null, MessengerService.ADD_BOOK)
            val bundle = Bundle()
            //只有是系统提供的实现了Parcelable接口的对象才能通过bundle传输
            bundle.putString(MessengerService.ADD_BOOK_KEY, Book(2, "book_2").toJson())
            message.data = bundle
            message.replyTo = replyMessenger
            messenger?.send(message)
        }

    }


    private class MessengerHandler(val textViewRef: WeakReference<TextView>) :
        Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MessengerService.ADD_BOOK_REPLY -> {
                    val reply = msg.data.getString(MessengerService.ADD_BOOK_REPLY_KEY)
                    textViewRef.get()?.text = reply
                }
            }
        }
    }


    private fun bindMessengerService() {
        val intent = Intent(this, MessengerService::class.java)
        bindService(intent, serviceConnection, BIND_AUTO_CREATE)
    }


    override fun onDestroy() {
        unbindService(serviceConnection)
        super.onDestroy()
    }
}