package org.cef.misc;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.LongConsumer;

/**
 * Cleaner state for native-backed resources. Holds only the native handle and a disposer callback
 * so cleanup actions never retain the owning Java object.
 */
public final class NativeCleanup implements Runnable {
    private final AtomicLong handle = new AtomicLong();
    private final AtomicBoolean cleaned = new AtomicBoolean(false);
    private final LongConsumer disposer;

    public NativeCleanup(LongConsumer disposer) {
        this.disposer = disposer;
    }

    public void setHandle(long value) {
        handle.set(value);
    }

    public void clearHandle() {
        handle.set(0);
    }

    @Override
    public void run() {
        if (cleaned.compareAndSet(false, true)) {
            long current = handle.getAndSet(0);
            if (current != 0) {
                disposer.accept(current);
            }
        }
    }

    public void markCleaned() {
        cleaned.set(true);
        handle.set(0);
    }

    public void clean() {
        run();
    }
}
