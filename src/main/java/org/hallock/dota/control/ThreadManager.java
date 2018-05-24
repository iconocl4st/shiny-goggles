package org.hallock.dota.control;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadManager {
    private final ExecutorService executorService;

    public ThreadManager(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public void run(Runnable runnable) {
        executorService.submit(runnable);
    }

    public static ThreadManager buildThreadPool() {
        return new ThreadManager(Executors.newSingleThreadExecutor());
    }
}
