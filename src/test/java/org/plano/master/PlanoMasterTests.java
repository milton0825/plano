package org.plano.master;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.plano.worker.PlanoWorker;

public class PlanoMasterTests {
    private PlanoMaster planoMaster;
    private static final Integer THREAD_POOL_SIZE = 1;
    private static final Long SCHEDULE_INITIAL_DELAY_MS = 100L;
    private static final Long SCHEDULE_PERIOD_MS = 100L;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);

        planoMaster = new PlanoMaster();
        planoMaster.setThreadPoolSize(THREAD_POOL_SIZE);
        planoMaster.setScheduleInitialDelayMs(SCHEDULE_INITIAL_DELAY_MS);
        planoMaster.setSchedulePeriodMs(SCHEDULE_PERIOD_MS);
        planoMaster.init();
    }

    @Test
    public void testCreateThreadPeriodicallyWhenThreadPoolIsNotFull() throws InterruptedException {
        Runnable planoWorker = Mockito.mock(PlanoWorker.class);
        planoMaster.setPlanoWorker(planoWorker);

        Mockito.doNothing().when(planoWorker).run();

        planoMaster.start();

        Thread.sleep(1000);

        planoMaster.shutdown();

        Mockito.verify(planoWorker, Mockito.atLeast(5)).run();
    }

    @Test
    public void testNotCreateThreadWhenThreadPoolIsFull() throws InterruptedException {
        Runnable planoWorker = Mockito.spy(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            while (!Thread.currentThread().isInterrupted()) {
                                Thread.sleep(100L);
                            }
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
        );

        planoMaster.setPlanoWorker(planoWorker);

        planoMaster.start();

        Thread.sleep(1000L);

        planoMaster.shutdown();

        Mockito.verify(planoWorker, Mockito.times(1)).run();
    }
}
