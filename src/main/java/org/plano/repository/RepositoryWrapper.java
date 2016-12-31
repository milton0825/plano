package org.plano.repository;

import org.plano.data.PlanoRequest;
import org.plano.exception.InvalidRequestException;
import org.plano.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Wrapper of repository.
 */
@Component(value = "RepositoryWrapper")
public class RepositoryWrapper implements Repository<PlanoRequest> {

    private Repository<PlanoRequest> repository;

    @Autowired
    private RepositoryFactory repositoryFactory;

    @Value("${repository.type}")
    private String repositoryType;

    @PostConstruct
    public void init() {
        repository = repositoryFactory.create(RepositoryType.valueOf(repositoryType));
    }

    /**
     * Get request with requestID from repository.
     * @param requestID {@link String} RequestID.
     * @return {@link PlanoRequest} request.
     * @throws ResourceNotFoundException if resource not found.
     */
    @Override
    public PlanoRequest getRequest(String requestID) throws ResourceNotFoundException {
        return repository.getRequest(requestID);
    }

    /**
     * Save request to repository.
     * @param request {@link PlanoRequest} Request.
     * @return RequestID.
     * @throws InvalidRequestException if the request is invalid.
     */
    @Override
    public String addRequest(PlanoRequest request) throws InvalidRequestException {
        return repository.addRequest(request);
    }

    /**
     * Update the request to repository.
     * @param request {@link PlanoRequest} Request.
     * @throws InvalidRequestException if the request is invalid.
     */
    @Override
    public void updateRequest(PlanoRequest request) throws InvalidRequestException {
        repository.updateRequest(request);
    }

    /**
     * Remove the request from repository.
     * @param requestID {@link PlanoRequest} Request.
     * @throws ResourceNotFoundException if the request is not found.
     */
    @Override
    public void removeRequest(String requestID) throws ResourceNotFoundException {
        repository.removeRequest(requestID);
    }

    /**
     * Find the next request to execute and lock.
     * @return {@link PlanoRequest}, null if can not find request to execute.
     */
    @Override
    public PlanoRequest findNextRequestAndLock(){
        return repository.findNextRequestAndLock();
    }

    /**
     * Update the request to repository and unlock.
     * @param request {@link PlanoRequest} Request.
     * @throws InvalidRequestException if the request is invalid.
     */
    @Override
    public void updateRequestAndUnlock(PlanoRequest request) throws InvalidRequestException {
        repository.updateRequestAndUnlock(request);
    }

    /**
     * Set RepositoryType.
     * @param repositoryType type of repository.
     */
    protected void setRepositoryType(String repositoryType) {
        this.repositoryType = repositoryType;
    }
}
