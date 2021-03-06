package org.plano.repository;

import org.plano.exception.InvalidRequestException;
import org.plano.exception.ResourceNotFoundException;

/**
 * This interface defines the contract for the persistence layer of plano.
 * @param <T> request
 */
public interface Repository<T> {
    /**
     * Get request with requestID from repository.
     * @param requestID {@link String} RequestID
     * @return <T> request
     * @throws ResourceNotFoundException if resource not found
     */
    T getRequest(String requestID) throws ResourceNotFoundException;

    /**
     * Save request to repository.
     * @param request <T> Request
     * @return RequestID
     * @throws InvalidRequestException if the request is invalid
     */
    String addRequest(T request) throws InvalidRequestException;

    /**
     * Update the request to repository.
     * @param request <T> Request
     * @throws InvalidRequestException if the request is invalid
     */
    void updateRequest(T request) throws InvalidRequestException;

    /**
     * Remove the request from repository.
     * @param requestID <T> Request
     * @throws ResourceNotFoundException if the request not found
     */
    void removeRequest(String requestID) throws ResourceNotFoundException;

    /**
     * Find the next request to execute and lock.
     * @return <T> Request, null if can not find request
     */
    T findNextRequestAndLock();

    /**
     * Update the request to repository and unlock.
     * @param request <T> Request
     * @throws InvalidRequestException if the request is invalid
     */
    void updateRequestAndUnlock(T request) throws InvalidRequestException;
}
