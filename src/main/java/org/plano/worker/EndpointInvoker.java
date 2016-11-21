package org.plano.worker;

/**
 * Created by ctsai on 11/19/16.
 */
public interface EndpointInvoker<T> {

    boolean invoke(T input);
}
