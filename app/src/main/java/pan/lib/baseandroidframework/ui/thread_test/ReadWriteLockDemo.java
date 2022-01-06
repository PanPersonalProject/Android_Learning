package pan.lib.baseandroidframework.ui.thread_test;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * AUTHOR Pan Created on 2022/1/5
 * ReentrantReadWriteLock
 * 线程进入读锁的前提条件  没有其他线程的写锁
 * 线程进入写锁的前提条件 没有其他线程的读锁
 */
public class ReadWriteLockDemo {
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
    private final ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();

    private int x = 0;

    public void readX() {
        readLock.lock();
        try {
            System.out.println("x=" + x);
        } finally {
            readLock.unlock();

        }

    }

    public void writeX() {
        writeLock.lock();
        try {
            x++;
        } finally {
            writeLock.unlock();

        }

    }

    public void test() {
        new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < 1_000_000_000; i++) {
                    writeX();
                }
            }
        }.start();
        new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < 1_000_000_000; i++) {
                    readX();
                }
            }
        }.start();

        new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < 1_000_000_000; i++) {
                    readX();
                }
            }
        }.start();

    }
}
