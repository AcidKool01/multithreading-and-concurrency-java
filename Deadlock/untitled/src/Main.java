import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    private Lock lock1 = new ReentrantLock(true);
    private Lock lock2 = new ReentrantLock(true);

    public void worker1() throws InterruptedException {
        lock1.lock();
        System.out.println("Worker1 acquires the lock1..");
        Thread.sleep(300);

        lock2.lock();
        System.out.println("Worker1 acquired the lock2..");

        lock1.unlock();
        lock2.unlock();
    }

    public void worker2() throws InterruptedException {
        lock2.lock();
        System.out.println("Worker2 acquires the lock2..");
        Thread.sleep(300);

        lock1.lock();
        System.out.println("Worker2 acquired the lock1..");

        lock1.unlock();
        lock2.unlock();
    }

    public static void main(String[] args) {
        Main deadlock = new Main();

        new Thread(deadlock::worker1, "worker1").start();
        new Thread(deadlock::worker2, "worker2").start();
    }
}