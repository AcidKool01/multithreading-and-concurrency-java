class H2O {
    Semaphore s1;
    Semaphore s2;

    public H2O() {
        s1 = new Semaphore(2);
        s2 = new Semaphore(0);
    }

    public void hydrogen(Runnable releaseHydrogen) throws InterruptedException {
		s1.acquire();
        // releaseHydrogen.run() outputs "H". Do not change or remove this line.
        releaseHydrogen.run();
        s2.release();
    }

    public void oxygen(Runnable releaseOxygen) throws InterruptedException {
        s2.acquire(2);
        // releaseOxygen.run() outputs "H". Do not change or remove this line.
		releaseOxygen.run();
        s1.release(2);
    }
}


class H2OTwo {
    private Semaphore semO;
    private Semaphore semH;
    private Phaser phaser;
    public H2O() {
        semO = new Semaphore(1);
        semH = new Semaphore(2);
        phaser = new Phaser(3);
    }

    public void hydrogen(Runnable releaseHydrogen) throws InterruptedException {
		
        semH.acquire();
        
        releaseHydrogen.run();
        phaser.arriveAndAwaitAdvance();
        phaser.arriveAndAwaitAdvance();
        
        semH.release();
        
    }

    public void oxygen(Runnable releaseOxygen) throws InterruptedException {
        semO.acquire();
        
        phaser.arriveAndAwaitAdvance();
        releaseOxygen.run();
        phaser.arriveAndAwaitAdvance();
		
        semO.release();
    }
}