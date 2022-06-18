package org.onepiece.redis.component;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class Receiver {
    private final AtomicInteger counter = new AtomicInteger();

    public void receiveMessage(String message) {
        log.info("Received <- {} ->", message);
        counter.incrementAndGet();
    }

    public int getCount() {
        return counter.get();
    }
}
