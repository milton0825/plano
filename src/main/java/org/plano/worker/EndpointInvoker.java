package org.plano.worker;

/**
 * This interface defines the contract plano is going to use to invoke
 * endpoint.
 * @param <T> request type.
 */
public interface EndpointInvoker<T> {

    /**
     * Calls the endpoint with the the request.
     * @param input request.
     * @return true if success, false if fail.
     */
    boolean invoke(T input);
}
