package org.plano.data;

import org.springframework.http.HttpMethod;

import javax.validation.constraints.NotNull;

/**
 * Created by ctsai on 11/6/16.
 */
public class PlanoRequest {

    private HttpRequest httpRequest;

    private SchedulingRule schedulingRule;
}
