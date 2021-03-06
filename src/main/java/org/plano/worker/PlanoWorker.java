package org.plano.worker;

import org.plano.data.HttpRequest;
import org.plano.data.PlanoRequest;
import org.plano.exception.PlanoException;
import org.plano.repository.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Plano Worker is responsible for finding requests from database and
 * calling the endpoint.
 */
@Component
public class PlanoWorker implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(PlanoWorker.class);

    @Value("${plano.worker.sleep.time.ms}")
    private Long sleepTimeMs;

    @Autowired
    @Qualifier("RepositoryWrapper")
    private Repository<PlanoRequest> repositoryWrapper;

    @Autowired
    @Qualifier("HttpEndpointInvoker")
    private EndpointInvoker<HttpRequest> httpEndpointInvoker;

    /**
     * Constructor.
     * @param repository {@link Repository} persistence data store
     * @param endpointInvoker {@link EndpointInvoker} service to call endpoint
     */
    @Autowired
    public PlanoWorker(@Qualifier("RepositoryWrapper") Repository<PlanoRequest> repository,
                       @Qualifier("HttpEndpointInvoker") EndpointInvoker<HttpRequest> endpointInvoker) {
        repositoryWrapper = repository;
        httpEndpointInvoker = endpointInvoker;
    }

    @Override
    public void run() {
        try {
            execute();
        } catch (PlanoException e) {
            LOG.debug("Exception caught when PlanoWorker is running.", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOG.debug("Thread is interrupted", e);
        }
    }

    private void execute() throws PlanoException, InterruptedException {
        PlanoRequest planoRequest;
        while (!Thread.currentThread().isInterrupted()) {
            planoRequest = repositoryWrapper.findNextRequestAndLock();
            while (planoRequest == null) {
                Thread.sleep(sleepTimeMs);
                planoRequest = repositoryWrapper.findNextRequestAndLock();
            }

            boolean isSuccess = httpEndpointInvoker.invoke(planoRequest.getHttpRequest());

            if (isSuccess) {
                repositoryWrapper.removeRequest(planoRequest.getRequestID());
            } else {
                planoRequest.updateForNextExecution();

                if (planoRequest.isValid()) {
                    repositoryWrapper.updateRequestAndUnlock(planoRequest);
                } else {
                    repositoryWrapper.removeRequest(planoRequest.getRequestID());
                }
            }
        }
    }

    /**
     * Set sleep time for thread.
     * @param sleepTimeMs thread sleep time
     */
    public void setSleepTimeMs(Long sleepTimeMs) {
        sleepTimeMs = sleepTimeMs;
    }
}
