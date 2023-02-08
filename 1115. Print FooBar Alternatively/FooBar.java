class FooBar1 {
    private int n;
    private AtomicInteger k;

    public FooBar(int n) {
        this.n = n;
        this.k = new AtomicInteger(1);
    }

    public void foo(Runnable printFoo) throws InterruptedException {
        
        for (int i = 0; i < n; i++) {
        	// printFoo.run() outputs "foo". Do not change or remove this line.
            synchronized(this) {
                if(k.get()%2==0) {
                    wait();
                } 
                printFoo.run();
                k.incrementAndGet();
                notifyAll();
            }
        }
    }

    public void bar(Runnable printBar) throws InterruptedException {
        
        for (int i = 0; i < n; i++) {
            synchronized(this) {
                if(k.get()%2==1) {
                    wait();
                }
                printBar.run();
                k.incrementAndGet();
                notifyAll();
            }
        }
    }
}

class FooBar2 {
    private int n;
    Semaphore s1 = new Semaphore(0); // s1 does not have a permit
    Semaphore s2 = new Semaphore(1); // s2 has one permit

    public FooBar(int n) {
        this.n = n;
    }

    public void foo(Runnable printFoo) throws InterruptedException {
        
        for (int i = 0; i < n; i++) {
            s2.acquire(); // there is one permit so it does not have to wait
        	// printFoo.run() outputs "foo". Do not change or remove this line.
        	printFoo.run();
            s1.release();
        }
    }

    public void bar(Runnable printBar) throws InterruptedException {
        
        for (int i = 0; i < n; i++) {
            s1.acquire(); // there is no permit so it will wait for s1.release(); add one permit
            // printBar.run() outputs "bar". Do not change or remove this line.
        	printBar.run();
            s2.release();
        }
    }
}