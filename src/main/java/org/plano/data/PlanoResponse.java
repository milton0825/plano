package org.plano.data;


import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * PlanoResponse POJO.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlanoResponse {
    private PlanoRequest planoRequest;
    private String errorMessage;
    private String requestID;
    private PlanoStatus planoStatus;

    public PlanoRequest getPlanoRequest() {
        return planoRequest;
    }

    public void setPlanoRequest(PlanoRequest planoRequest) {
        this.planoRequest = planoRequest;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getRequestID() {
        return requestID;
    }

    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }

    public PlanoStatus getPlanoStatus() {
        return planoStatus;
    }

    public void setPlanoStatus(PlanoStatus planoStatus) {
        this.planoStatus = planoStatus;
    }
}
