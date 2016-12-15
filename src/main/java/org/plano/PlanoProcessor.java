package org.plano;

import org.plano.data.PlanoRequest;
import org.plano.data.PlanoResponse;
import org.plano.data.PlanoStatus;
import org.plano.exception.InvalidRequestException;
import org.plano.exception.ResourceNotFoundException;
import org.plano.repository.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * {@link PlanoProcessor} is responsible to validate the request, call {@link Repository}
 * and compose the {@link PlanoResponse}.
 */
@Component
public class PlanoProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(PlanoProcessor.class);

    private Repository<PlanoRequest> repositoryWrapper;

    /**
     * Get {@link PlanoRequest} from {@link Repository}.
     * @param requestID RequestID.
     * @return {@link PlanoResponse}.
     */
    public PlanoResponse getRequest(String requestID) {
        PlanoResponse planoResponse = new PlanoResponse();
        try {
            PlanoRequest planoRequest = repositoryWrapper.getRequest(requestID);

            planoResponse.setPlanoRequest(planoRequest);
            planoResponse.setPlanoStatus(PlanoStatus.SUCCESS);
        } catch (ResourceNotFoundException e) {
            planoResponse.setPlanoStatus(PlanoStatus.NOT_FOUND);
            planoResponse.setErrorMessage(e.getMessage());
        }

        return planoResponse;
    }

    /**
     * Add {@link PlanoRequest} to {@link Repository}.
     * @param planoRequest {@link PlanoRequest}.
     * @return {@link PlanoResponse}.
     */
    public PlanoResponse addRequest(PlanoRequest planoRequest) {
        PlanoResponse planoResponse = new PlanoResponse();
        try {
            if (!planoRequest.isValid()) {
                String message = String.format("Invalid PlanoRequest: {}", planoRequest);
                throw new InvalidRequestException(message);
            }

            String requestID = repositoryWrapper.addRequest(planoRequest);

            planoResponse.setRequestID(requestID);
            planoResponse.setPlanoStatus(PlanoStatus.CREATED);
        } catch (InvalidRequestException e) {
            planoResponse.setPlanoStatus(PlanoStatus.INVALID_INPUT);
            planoResponse.setErrorMessage(e.getMessage());
        }

        return planoResponse;
    }

    /**
     * Update {@link PlanoRequest} in {@link Repository}.
     * @param requestID RequestID.
     * @param planoRequest {@link PlanoRequest}.
     * @return {@link PlanoResponse}.
     */
    public PlanoResponse updateRequest(String requestID, PlanoRequest planoRequest) {
        PlanoResponse planoResponse = new PlanoResponse();
        try {
            if (!planoRequest.isValid()) {
                String message = String.format("Invalid PlanoRequest: {}", planoRequest);
                throw new InvalidRequestException(message);
            }

            planoRequest.setRequestID(requestID);

            repositoryWrapper.updateRequest(planoRequest);

            planoResponse.setRequestID(requestID);
            planoResponse.setPlanoStatus(PlanoStatus.SUCCESS);
        } catch (InvalidRequestException e) {
            planoResponse.setPlanoStatus(PlanoStatus.INVALID_INPUT);
            planoResponse.setErrorMessage(e.getMessage());
        }

        return planoResponse;
    }

    /**
     * Removes {@link PlanoRequest} from {@link Repository} with RequestID.
     * @param requestID RequestID.
     * @return {@link PlanoResponse}.
     */
    public PlanoResponse removeRequest(String requestID) {
        PlanoResponse planoResponse = new PlanoResponse();
        try {
            repositoryWrapper.removeRequest(requestID);

            planoResponse.setRequestID(requestID);
            planoResponse.setPlanoStatus(PlanoStatus.SUCCESS);
        } catch (InvalidRequestException e) {
            planoResponse.setPlanoStatus(PlanoStatus.INVALID_INPUT);
            planoResponse.setErrorMessage(e.getMessage());
        }

        return planoResponse;
    }

    public void setRepositoryWrapper(Repository<PlanoRequest> repositoryWrapper) {
        this.repositoryWrapper = repositoryWrapper;
    }
}
