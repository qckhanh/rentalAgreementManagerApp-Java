package org.rmit.Helper;

import javafx.application.Platform;
import javafx.concurrent.Task;

import java.util.List;
import java.util.concurrent.*;

public class TaskUtils {
    private static final ExecutorService executor = Executors.newCachedThreadPool();

    public static <T> Task<T> createTask(Callable<T> callable) {
        return new Task<>() {
            @Override
            protected T call() throws Exception {
                return callable.call(); // Execute the passed method and return the result
            }
        };
    }

    public static <T> void run(Task<T> tTask){
        Thread backgroundThread = new Thread(tTask);
        backgroundThread.setDaemon(true);
        backgroundThread.start();
    }


    // Shutdown the executor service
    public static void shutdown() {
        executor.shutdown();
    }


    public static <T> void countDown(CountDownLatch latch, Callable<T> callable) {
        new Thread(() -> {
            try {
                latch.await();  // Wait for the latch to reach zero
                callable.call(); // Execute the passed callable
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

}
