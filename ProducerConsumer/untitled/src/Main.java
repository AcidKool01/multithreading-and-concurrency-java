import java.util.ArrayList;
import java.util.List;

class Processor {
    private List<Integer> list;
    private static final int UPPER_LIMIT = 5;
    private static final int LOWER_LIMIT = 0;
    private final Object lock;
    private int value;

    Processor() {
        list = new ArrayList<>();
        lock = new Object();
        value = 1;
    }

    public void consumer() throws InterruptedException {
        synchronized(lock) {
            while (true) {
                if(list.size() == LOWER_LIMIT) {
                    System.out.println("Waiting for Adding of items..");
                    value = 1;
                    lock.wait();
                } else {
                    System.out.println("Removing: " + list.remove(list.size()-1));
                    lock.notify();
                }
                Thread.sleep(500);
            }
        }
    }

    public void producer() throws InterruptedException {
        synchronized(lock) {
            while (true) {
                if(list.size() == UPPER_LIMIT) {
                    System.out.println("Waiting for Removal of items..");
                    lock.wait();
                } else {
                    System.out.println("Adding: " + value);
                    list.add(value++);
                    lock.notify();
                }
                Thread.sleep(500);
            }
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Processor process = new Processor();

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    process.producer();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    process.consumer();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        t1.start();
        t2.start();
    }
}