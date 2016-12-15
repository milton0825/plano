package org.plano.master;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.plano.data.HttpRequest;
import org.plano.data.PlanoRequest;
import org.plano.repository.Repository;
import org.plano.worker.EndpointInvoker;
import org.plano.worker.HttpEndpointInvoker;
import org.plano.worker.PlanoWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;


import javax.annotation.PostConstruct;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * PlanoMaster is responsible for managing a pool of {@link PlanoWorker}.
 */
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
