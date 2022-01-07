package pan.lib.baseandroidframework.ui.thread_test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * AUTHOR Pan Created on 2022/1/4
 */
public class ThreadDemo {
    /*
        保证加了 volatile 关键字的字段的操作具有原⼦性和同步性，其中原⼦性相当于实现了针对
        单⼀字段的线程间互斥访问。因此 volatile 可以看做是简化版的 synchronized。
        volatile 只对基本类型 (byte、char、short、int、long、float、double、boolean) 的赋值
        操作和对象的引⽤赋值操作有效。
        i++不是单一的赋值操作,还有i+1的计算,所以volatile不适用于计算*/
    volatile int volatileInt = 1;

    public static void main(String[] args) {
//        simpleThread();
//        threadFactory()
//        executor();
//        callable();
//        lockDemo();
        interruptDemo();
    }


    private static void simpleThread() {
        Runnable runnable = () -> System.out.print("runnable执行了\n");
        Thread thread = new Thread(runnable) {
            @Override
            public void run() {
                super.run(); //如果有runnable,runnable也会执行
                System.out.print("thread.run()执行了\n");


            }
        };
        thread.start();
        thread.interrupt();
    }

    static void threadFactory() {
        ThreadFactory factory = new ThreadFactory() {

            final AtomicInteger count = new AtomicInteger(0); //Atomic原子性

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "Thread-" + count.incrementAndGet());
            }
        };

        Runnable runnable = () -> System.out.println(Thread.currentThread().getName() + " started!");

        Thread thread = factory.newThread(runnable);
        thread.start();
        Thread thread1 = factory.newThread(runnable);
        thread1.start();
    }

    private static void executor() {
        /*
         corePoolSize 核心线程数
             线程池的基本大小，即在没有任务需要执行的时候线程池的大小，并且只有在工作队列满了的情况下才会创建超出这个数量的线程。
             这里需要注意的是：在刚刚创建ThreadPoolExecutor的时候，线程并不会立即启动，
             而是要等到有任务提交时才会启动，除非调用了prestartCoreThread/prestartAllCoreThreads事先启动核心线程。
             再考虑到keepAliveTime和allowCoreThreadTimeOut超时参数的影响，所以没有任务需要执行的时候，线程池的大小不一定是corePoolSize。
         maximumPoolSize 最大线程数
         BlockingQueue 阻塞队列
         keepAliveTime 非核心线程空闲存活时间*/

        ThreadPoolExecutor executor = new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                60L, TimeUnit.SECONDS,
                new SynchronousQueue<>());
        executor.execute(() -> {
            //线程操作
        });

//        ExecutorService executor= Executors.newCachedThreadPool();

    }

    static void callable() {
        Callable<String> callable = new Callable<String>() {
            @Override
            public String call() {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "Done!";
            }
        };

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(callable);
        try {
            long before = System.currentTimeMillis();
            String result = future.get();
            long after = System.currentTimeMillis();
            System.out.println("result: " + result + "time used=" + (after - before) / 1000f);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private static void lockDemo() {
        new ReadWriteLockDemo().test();
    }

    private static void interruptDemo() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                int i = 0;
                while (true) {

                    if (isInterrupted()) {
                        return;
                    }
                    try {
                        sleep(1000);
                        System.out.println("now i = " + ++i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        thread.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread.interrupt();
    }

}
