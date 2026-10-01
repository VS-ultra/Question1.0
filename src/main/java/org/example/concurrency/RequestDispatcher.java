package org.example.concurrency;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.ProductRequest;
import org.example.service.ApplicationProcessingService;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class RequestDispatcher {

    private final List<RequestWorker> workers = new ArrayList<>();
    private final List<Thread> threads = new ArrayList<>();
    private final AtomicInteger nextWorker = new AtomicInteger();

    private volatile boolean accepting = true;

    public RequestDispatcher(int workerCount, int queueCapacity, ApplicationProcessingService processingService) {
        if (workerCount <= 0 || queueCapacity <= 0) {
            throw new IllegalArgumentException("workerCount and queueCapacity must be > 0");
        }
        for (int i = 1; i <= workerCount; i++) {
            RequestWorker worker = new RequestWorker(
                    "request-worker-" + i,
                    new LinkedBlockingQueue<>(queueCapacity),
                    processingService);
            workers.add(worker);
        }
    }

    public void start() {
        for (RequestWorker worker : workers) {
            Thread thread = new Thread(worker, worker.getName());
            threads.add(thread);
            thread.start();
        }
        log.info("Dispatcher started with {} workers", workers.size());
    }

    public void submit(ProductRequest request) throws InterruptedException {
        if (!accepting) {
            throw new IllegalStateException("Dispatcher is stopped");
        }
        int index = Math.floorMod(nextWorker.getAndIncrement(), workers.size());
        workers.get(index).enqueue(request);
    }
    public void shutdown() throws InterruptedException {
        accepting = false;
        for (RequestWorker worker : workers) {
            worker.stop();
        }
        for (Thread thread : threads) {
            thread.join();
        }
        log.info("Dispatcher stopped. Processed by worker: {}", getProcessedByWorker());
    }
    public Map<String, Integer> getProcessedByWorker() {
        Map<String, Integer> result = new LinkedHashMap<>();
        for (RequestWorker worker : workers) {
            result.put(worker.getName(), worker.getProcessedCount() + worker.getFailedCount());
        }
        return result;
    }
}