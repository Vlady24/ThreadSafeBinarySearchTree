import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadSafeBSTTest {

    public static void main(String[] args) throws InterruptedException {
        final ThreadSafeBinarySearchTreeSolution_1 bst1 = new ThreadSafeBinarySearchTreeSolution_1();
        final ThreadSafeBinarySearchTreeSolution_2 bst2 = new ThreadSafeBinarySearchTreeSolution_2();
        Random random = new Random();
        int numThreads = random.nextInt(41) + 10; // 10 to 50
        Thread[] threads = new Thread[numThreads];
        
        // Add counters to track concurrent operations
        final AtomicInteger activeThreads = new AtomicInteger(0); // to track active threads
        final CountDownLatch startSignal = new CountDownLatch(1); // to start all threads at the same time
        final CountDownLatch doneSignal = new CountDownLatch(numThreads); // to wait for all threads to finish

        System.out.println("Starting test with " + numThreads + " threads");

        for (int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(() -> {
                try {
                    startSignal.await(); // Wait for all threads to be ready
                    activeThreads.incrementAndGet();
                    
                    for (int j = 0; j < 100; j++) {
                        byte[] key = new byte[5];
                        byte[] value = new byte[5];
                        random.nextBytes(key);
                        random.nextBytes(value);

                        if (random.nextBoolean()) {
                            bst1.put(key, value);
                            bst2.put(key, value);
                        } else {
                            bst1.get(key);
                            bst2.get(key);
                        }
                        
                        // Occasionally check and print the number of active threads
                        if (j % 20 == 0) {
                            System.out.println("Active threads: " + activeThreads.get());
                        }
                    }
                } catch (InterruptedException e) {
                    System.err.println("Thread interrupted: " + e.getMessage());
                } finally {
                    activeThreads.decrementAndGet();
                    doneSignal.countDown();
                }
            });
            threads[i].start();
        }

        // Start all threads simultaneously
        startSignal.countDown();

        try {
            // Wait for all threads to complete
            doneSignal.await();
            System.out.println("All threads completed successfully");
            System.out.println("Final active threads count: " + activeThreads.get());

            if (activeThreads.get() != 0) {
                System.err.println("Warning: Some threads did not complete properly");
            }
        } catch (InterruptedException e) {
            System.err.println("Main thread interrupted while waiting for completion: " + e.getMessage());
        }
    }
}
