package org.example.concurrency;
import lombok.extern.slf4j.Slf4j;
import org.example.ENUM.TaskPriority;
import org.example.concurrency.config.ThreadPoolConfig;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

@Slf4j
public class PriorityTaskExecutor implements AutoCloseable {
    private final ThreadPoolConfig config;
    private final ThreadPoolExecutor executor;

    public PriorityTaskExecutor(ThreadPoolConfig config) {
        this.config = Objects.requireNonNull(config, "config cannot be null");
        this.executor = new ThreadPoolExecutor(
                config.poolSize(),
                config.poolSize(),
                0L, TimeUnit.MILLISECONDS,
                new PriorityBlockingQueue<>(),
                namedThreadFactory(config.threadNamePrefix()),
                new ThreadPoolExecutor.AbortPolicy()
        );
        log.info("Thread pool started: size={}, prefix={}", config.poolSize(), config.threadNamePrefix());
    }
    public <T> CompletableFuture<T> submitClientTask(String name, Supplier<T> task) {
        return submit(TaskPriority.HIGH, name, task);
    }

    public <T> CompletableFuture<T> submitAnalyticsTask(String name, Supplier<T> task) {
        return submit(TaskPriority.LOW, name, task);
    }

    public <T> CompletableFuture<T> submit(TaskPriority priority, String name, Supplier<T> task) {
        Objects.requireNonNull(task, "task cannot be null");
        CompletableFuture<T> result = new CompletableFuture<>();

        PrioritizedTask prioritizedTask = new PrioritizedTask(priority, name, () -> {
            try {
                result.complete(task.get());
            } catch (Throwable e) {
                log.error("Task failed: name={}, priority={}", name, priority, e);
                result.completeExceptionally(e);
            }
        });

        try {
            executor.execute(prioritizedTask);
        } catch (RejectedExecutionException e) {
            log.error("Task rejected (pool is shut down): name={}", name);
            result.completeExceptionally(e);
        }
        return result;
    }
    public int getQueueSize() {
        return executor.getQueue().size();
    }
    public int getActiveCount() {
        return executor.getActiveCount();
    }

    public long getCompletedTaskCount() {
        return executor.getCompletedTaskCount();
    }
    public void shutdown() {
        log.info("Shutting down pool: queued={}, active={}", getQueueSize(), getActiveCount());
        executor.shutdown();
        try {
            if (!executor.awaitTermination(config.shutdownTimeoutSeconds(), TimeUnit.SECONDS)) {
                List<Runnable> dropped = executor.shutdownNow();
                log.warn("Pool did not stop in time, forced shutdown. Dropped tasks: {}", dropped.size());
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        log.info("Pool stopped. Completed tasks: {}", getCompletedTaskCount());
    }

    @Override
    public void close() {
        shutdown();
    }

    private static ThreadFactory namedThreadFactory(String prefix) {
        AtomicInteger counter = new AtomicInteger(1);
        return runnable -> {
            Thread thread = new Thread(runnable, prefix + "-" + counter.getAndIncrement());
            thread.setDaemon(false);
            thread.setUncaughtExceptionHandler((t, e) ->
                    log.error("Uncaught exception in thread {}", t.getName(), e));
            return thread;
        };
    }
}
