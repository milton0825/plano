package org.plano.worker;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.plano.data.HttpRequest;
import org.plano.data.PlanoRequest;
import org.plano.exception.InvalidRequestException;
import org.plano.exception.ResourceNotFoundException;
import org.plano.repository.Repository;
import org.plano.worker.EndpointInvoker;
import org.plano.worker.PlanoWorker;
import utils.DataTestUtils;

public class PlanoWorkerTests {
    @Mock
    private Repository<PlanoRequest> repository;

    @Mock
    private EndpointInvoker<HttpRequest> httpEndpointInvoker;

    private PlanoWorker planoWorker;

    private static final Long PLANO_WORKER_SLEEP_TIME_MS = 100L;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        planoWorker = new PlanoWorker(repository, httpEndpointInvoker);
        planoWorker.setSleepTimeMs(PLANO_WORKER_SLEEP_TIME_MS);
    }

    @Test
    public void testWhenInterruptedThenReturn() throws InterruptedException {
        Mockito.when(repository.findNextRequestAndLock()).thenReturn(null);

        Thread thread = new Thread(planoWorker);
        thread.start();

        Thread.sleep(500L);

        Mockito.verify(repository, Mockito.atLeastOnce()).findNextRequestAndLock();

        thread.interrupt();

        Mockito.reset(repository);

        Thread.sleep(500L);

        Mockito.verify(repository, Mockito.never()).findNextRequestAndLock();
    }

    @Test
    public void testWhenRepositoryReturnNullThenSleep() throws InterruptedException,
            InvalidRequestException, ResourceNotFoundException {
        Mockito.when(repository.findNextRequestAndLock()).thenReturn(null);

        Thread thread = new Thread(planoWorker);
        thread.start();

        Thread.sleep(1000L);
        thread.interrupt();

        Mockito.verify(repository, Mockito.atLeastOnce()).findNextRequestAndLock();
        Mockito.verify(httpEndpointInvoker, Mockito.never()).invoke(Mockito.any(HttpRequest.class));
        Mockito.verify(repository, Mockito.never()).removeRequest(Mockito.anyString());
        Mockito.verify(repository, Mockito.never()).updateRequestAndUnlock(Mockito.any(PlanoRequest.class));
    }

    @Test
    public void testWhenInvokeSuccessThenRemoveRequest() throws InvalidRequestException,
            InterruptedException, ResourceNotFoundException {
        PlanoRequest mockedPlanoRequest = Mockito.mock(PlanoRequest.class);
        Mockito.when(repository.findNextRequestAndLock()).thenReturn(mockedPlanoRequest);
        Mockito.when(httpEndpointInvoker.invoke(Mockito.any(HttpRequest.class))).thenReturn(true);

        Thread thread = new Thread(planoWorker);
        thread.start();

        Thread.sleep(500L);
        thread.interrupt();

        Mockito.verify(repository, Mockito.atLeastOnce()).removeRequest(Mockito.anyString());
        Mockito.verify(repository, Mockito.never()).updateRequestAndUnlock(Mockito.any(PlanoRequest.class));
    }

    @Test
    public void testWhenInvokeFailureThenUpdateRequestAndUnlock() throws InvalidRequestException,
            InterruptedException, ResourceNotFoundException {
        Integer numberOfExecutions = 5;
        PlanoRequest planoRequest = DataTestUtils.createPlanoRequest();
        planoRequest.getSchedulePolicy().setMultiplier(1);
        planoRequest.getSchedulePolicy().setNumberOfExecutions(numberOfExecutions);
        planoRequest.getSchedulePolicy().setExecutionIntervalMs(100L);

        PlanoRequest mockedPlanoRequest = Mockito.spy(planoRequest);
        Mockito.when(repository.findNextRequestAndLock()).thenReturn(mockedPlanoRequest);
        Mockito.when(httpEndpointInvoker.invoke(Mockito.any(HttpRequest.class))).thenReturn(false);

        Thread thread = new Thread(planoWorker);
        thread.start();

        Thread.sleep(500L);
        thread.interrupt();

        Integer numberOfUpdates = numberOfExecutions-1;
        Mockito.verify(repository, Mockito.times(numberOfUpdates)).updateRequestAndUnlock(Mockito.any(PlanoRequest.class));
    }
}
