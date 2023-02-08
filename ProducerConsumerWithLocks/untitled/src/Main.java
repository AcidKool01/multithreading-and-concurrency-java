import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Worker {
    private static Lock lock;
    private Condition condition;

    Worker() {
        lock = new ReentrantLock();
        condition = lock.newCondition();
    }

    public void produce() throws InterruptedException {
        lock.lock();
        System.out.println("Produce Method...");
        condition.await();
        System.out.println("Again Produce Method...");
    }

    public void consume() throws InterruptedException {
        Thread.sleep(2000);
        lock.lock();
        System.out.println("Consumer Method...");
        Thread.sleep(3000);
        condition.signal();
    }
}

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Worker worker = new Worker();

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    worker.produce();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    worker.consume();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();
    }
}