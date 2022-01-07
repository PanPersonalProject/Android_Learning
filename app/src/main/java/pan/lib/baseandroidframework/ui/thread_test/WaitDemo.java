package pan.lib.baseandroidframework.ui.thread_test;

public class WaitDemo {
    private String sharedString;

    private synchronized void initString() {
        sharedString = "wait success msg";
        notifyAll();
    }

    private synchronized void printString() {
        while (sharedString == null) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("String: " + sharedString);
    }

    public void runTest() {
        final Thread thread1 = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                printString();
            }
        };
        thread1.start();
        Thread thread2 = new Thread() {
            @Override
            public void run() {

                initString();
            }
        };
        thread2.start();
    }
}