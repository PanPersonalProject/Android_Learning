package pan.lib.baseandroidframework.java_demo.pxjava_demo

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.Function
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.internal.wait
import java.util.concurrent.TimeUnit

/**
 * AUTHOR Pan Created on 2022/1/8
 */
class RxJavaTest {
    fun singleSimple() {
        Single.just(2)
            .map(Function {
                return@Function "value is $it" //int转string
            })
            .delay(2, TimeUnit.SECONDS)
            .subscribe(object : SingleObserver<String> {
                override fun onSubscribe(d: Disposable) {
                    println("isDisposed=" + d.isDisposed)
                }

                override fun onSuccess(t: String) {
                    println(t)
                }

                override fun onError(e: Throwable) {
                }
            })

    }

    fun observableSimple() {
//        Observable.just(2,3,4) 和Observable.create<Int> { subscriber ->} 一样
        synchronized(this) {

            Observable.create<Int> { subscriber ->
                println("Observable.create currentThread=" + Thread.currentThread().name)
                subscriber.onNext(2)
                subscriber.onNext(3)
                subscriber.onNext(4)
                subscriber.onComplete()
            }
                .map(Function {
                    return@Function "value is $it" //int转string
                })
                .subscribeOn(Schedulers.computation()) //  subscribeOn()主要改变的是订阅的线程，即call()执行的线程;
                .observeOn(Schedulers.io()) //  observeOn()主要改变的是发送的线程，即onNext()执行的线程。
                .subscribe(object : Observer<String> {
                    override fun onSubscribe(d: Disposable) {
                        println("isDisposed=" + d.isDisposed + "  Observable.create currentThread=" + Thread.currentThread().name)
                    }

                    override fun onNext(t: String) {
                        println(t + "  Observable.create currentThread=" + Thread.currentThread().name)
                    }

                    override fun onError(e: Throwable) {
                    }

                    override fun onComplete() {
                        println("onComplete")
                    }

                })
            wait()
        }

    }
}

fun main() {
    val rxJavaTest = RxJavaTest()
//    rxJavaTest.singleSimple()
    rxJavaTest.observableSimple()

}