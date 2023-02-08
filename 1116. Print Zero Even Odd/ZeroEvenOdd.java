class ZeroEvenOdd {
    private int n;
    private AtomicInteger k;
    
    public ZeroEvenOdd(int n) {
        this.n = n; 
        this.k = new AtomicInteger(1);
    }

    // printNumber.accept(x) outputs "x", where x is an integer.
    public void zero(IntConsumer printNumber) throws InterruptedException {
        synchronized(this) {
            while(k.get() <= 2*n-1) {
                while(k.get()%4 != 1 && k.get()%4 != 3) {
                    wait();
                }

                printNumber.accept(0);
                k.incrementAndGet();
                notifyAll();
            }
        }
    }

    public void even(IntConsumer printNumber) throws InterruptedException {
        synchronized(this) {
            while(k.get() <= 2*n-3) {
                while(k.get()%4 != 0) {
                    wait();
                }

                printNumber.accept((int)(k.get()/2));
                k.incrementAndGet();
                notifyAll();
            }
        }
    }

    public void odd(IntConsumer printNumber) throws InterruptedException {
        synchronized(this) {
            while(k.get() <= 2*n) {
                while(k.get()%4 != 2) {
                    wait();
                }
                
                printNumber.accept((int)(k.get()/2));
                k.incrementAndGet();
                notifyAll();
            }
        }
    }
}

class ZeroEvenOdd2 {

    private int n;
    private Semaphore zeroSem, oddSem, evenSem;
    
    public ZeroEvenOdd2(int n) {
        this.n = n;
        zeroSem = new Semaphore(1);
        oddSem = new Semaphore(0);
        evenSem = new Semaphore(0);
    }

    // printNumber.accept(x) outputs "x", where x is an integer.
    public void zero(IntConsumer printNumber) throws InterruptedException {
        for (int i = 0; i < n; ++i) {
            zeroSem.acquire();
            printNumber.accept(0);
            (i % 2 == 0 ? oddSem : evenSem).release(); // Alternately release odd() and even().
        }
    }

    public void even(IntConsumer printNumber) throws InterruptedException {
        for (int i = 2; i <= n; i += 2) {
            evenSem.acquire();
            printNumber.accept(i);
            zeroSem.release();
        }
    }

    public void odd(IntConsumer printNumber) throws InterruptedException {
        for (int i = 1; i <= n; i += 2) {
            oddSem.acquire();
            printNumber.accept(i);
            zeroSem.release();
        }
    }
	
}