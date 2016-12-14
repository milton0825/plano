package org.plano.worker;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.plano.data.HttpRequest;
import org.plano.data.PlanoRequest;
import org.plano.exception.InvalidRequestException;
import org.plano.repository.Repository;

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
    public void testWhenRepositoryReturnNullThenSleep() throws InterruptedException, InvalidRequestException {
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
    public void testWhenInvokeSuccessThenRemoveRequest() throws InvalidRequestException, InterruptedException {
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
    public void testWhenInvokeFailureThenUpdateRequestAndUnlock() throws InvalidRequestException, InterruptedException {
        PlanoRequest mockedPlanoRequest = Mockito.mock(PlanoRequest.class);
        Mockito.when(repository.findNextRequestAndLock()).thenReturn(mockedPlanoRequest);
        Mockito.when(httpEndpointInvoker.invoke(Mockito.any(HttpRequest.class))).thenReturn(false);

        Thread thread = new Thread(planoWorker);
        thread.start();

        Thread.sleep(500L);
        thread.interrupt();

        Mockito.verify(repository, Mockito.never()).removeRequest(Mockito.anyString());
        Mockito.verify(repository, Mockito.atLeastOnce()).updateRequestAndUnlock(Mockito.any(PlanoRequest.class));
    }
}
