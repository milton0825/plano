package org.plano.master;

/**
 * Master is responsible to manage a pool of {@link Runnable}.
 */
public interface Master {
    /**
     * Create a thread pool and start creating {@link Runnable}.
     */
    void start();

    /**
     * Shut down the thread pool and stop creating {@link Runnable}.
     */
    void shutdown();
}
