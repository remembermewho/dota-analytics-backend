package com.dota2.dota2dataperser.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OpenDotaRateLimiter {

    private final Object lock = new Object();

    private long nextAllowedRequestTimeMs = 0L;

    @Value("${opendota.rate-limit.requests-per-minute:50}")
    private int requestsPerMinute;

    public void acquire() {
        synchronized (lock) {
            int safeRequestsPerMinute = Math.max(1, requestsPerMinute);

            long intervalMs = 60_000L / safeRequestsPerMinute;
            long now = System.currentTimeMillis();

            if (now < nextAllowedRequestTimeMs) {
                long sleepTimeMs = nextAllowedRequestTimeMs - now;

                try {
                    Thread.sleep(sleepTimeMs);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new IllegalStateException("OpenDota rate limiter interrupted", e);
                }
            }

            long currentTimeAfterSleep = System.currentTimeMillis();
            nextAllowedRequestTimeMs = currentTimeAfterSleep + intervalMs;
        }
    }
}