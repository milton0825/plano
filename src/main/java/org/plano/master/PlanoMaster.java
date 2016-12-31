package org.plano.master;

import org.plano.worker.PlanoWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * PlanoMaster is responsible for managing a pool of {@link PlanoWorker}.
 */
@Component(value = "PlanoMaster")
public class PlanoMaster implements Master {

    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;

    @Autowired
    private Runnable planoWorker;

    @Value("${plano.master.thread.pool.size}")
    private Integer threadPoolSize;

    @Value("${plano.master.schedule.initial.delay.ms}")
    private Long scheduleInitialDelayMs;

    @Value("${plano.master.schedule.period.ms}")
    private Long schedulePeriodMs;

    @PostConstruct
    public void init() {
        scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(threadPoolSize);
    }

    /**
     * Create a thread pool and start creating {@link PlanoWorker}.
     */
    @Override
    public void start() {
        scheduledThreadPoolExecutor.scheduleAtFixedRate(planoWorker,
                scheduleInitialDelayMs,
                schedulePeriodMs,
                TimeUnit.MILLISECONDS);
    }

    /**
     * Shutdown the thread pool and stop creating {@link PlanoWorker}.
     */
    @Override
    public void shutdown() {
        scheduledThreadPoolExecutor.shutdown();
    }

    @PreDestroy
    public void destroy() {
        shutdown();
    }

    public void setThreadPoolSize(Integer threadPoolSize) {
        this.threadPoolSize = threadPoolSize;
    }

    public void setScheduleInitialDelayMs(Long scheduleInitialDelayMs) {
        this.scheduleInitialDelayMs = scheduleInitialDelayMs;
    }

    public void setSchedulePeriodMs(Long schedulePeriodMs) {
        this.schedulePeriodMs = schedulePeriodMs;
    }

    public void setPlanoWorker(Runnable planoWorker) {
        this.planoWorker = planoWorker;
    }
}
