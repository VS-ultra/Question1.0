package org.example.concurrency;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.ProductRequest;
import org.example.exception.BusinessException;
import org.example.service.ApplicationProcessingService;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RequiredArgsConstructor
public class RequestWorker implements Runnable {

    @Getter
    private final String name;
    private final BlockingQueue<ProductRequest> queue;
    private final ApplicationProcessingService processingService;

    private final AtomicInteger processedCount = new AtomicInteger();
    private final AtomicInteger failedCount = new AtomicInteger();

    private volatile boolean running = true;

    @Override
    public void run() {
        log.info("{} started", name);
        while (running || !queue.isEmpty()) {
            try {
                ProductRequest request = queue.poll(100, TimeUnit.MILLISECONDS);
                if (request != null) {
                    process(request);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        log.info("{} stopped: processed={}, failed={}", name, processedCount.get(), failedCount.get());
    }
    public void enqueue(ProductRequest request) throws InterruptedException {
        queue.put(request);
    }

    public void stop() {
        running = false;
    }

    public int getProcessedCount() {
        return processedCount.get();
    }

    public int getFailedCount() {
        return failedCount.get();
    }

    public int getQueueSize() {
        return queue.size();
    }
    private void process(ProductRequest request) {
        try {
            processingService.processRequest(request);
            processedCount.incrementAndGet();
        } catch (BusinessException e) {
            failedCount.incrementAndGet();
            log.warn("{}: request {} rejected: {}", name, request.getId(), e.getMessage());
        } catch (RuntimeException e) {
            failedCount.incrementAndGet();
            log.error("{}: unexpected error on request {}", name, request.getId(), e);
        }
    }
}