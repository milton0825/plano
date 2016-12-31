package org.plano;

import org.plano.data.PlanoRequest;
import org.plano.data.PlanoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * {@link PlanoController} maps HTTP requests to corresponding handlers.
 */
@RestController
public class PlanoController {

    @Autowired
    private PlanoProcessor planoProcessor;

    /**
     * GET method to retrieve {@link PlanoRequest} with RequestID.
     * @param requestID RequestID.
     * @return {@link ResponseEntity}.
     */
    @GetMapping(path = "/requests/{requestID}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getRequest(@PathVariable String requestID) {
        PlanoResponse planoResponse = planoProcessor.getRequest(requestID);
        ResponseEntity<PlanoResponse> responseEntity = composeResponseEntity(planoResponse);

        return responseEntity;
    }

    /**
     * POST method to save {@link PlanoRequest} to persistence data store.
     * @param planoRequest {@link PlanoRequest}.
     * @return {@link ResponseEntity}.
     */
    @PostMapping(path = "/requests",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addRequest(@RequestBody PlanoRequest planoRequest) {
        PlanoResponse planoResponse = planoProcessor.addRequest(planoRequest);
        ResponseEntity<PlanoResponse> responseEntity = composeResponseEntity(planoResponse);

        return responseEntity;
    }

    /**
     * PUT method to update {@link PlanoRequest} to persistence data store.
     * @param requestID RequestID.
     * @param planoRequest {@link PlanoRequest}.
     * @return {@link ResponseEntity}.
     */
    @PutMapping(path = "/requests/{requestID}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateRequest(@PathVariable String requestID, @RequestBody PlanoRequest planoRequest) {
        PlanoResponse planoResponse = planoProcessor.updateRequest(requestID, planoRequest);
        ResponseEntity<PlanoResponse> responseEntity = composeResponseEntity(planoResponse);

        return responseEntity;
    }

    /**
     * DELETE method to remove {@link PlanoRequest} from persistence data store with RequestID.
     * @param requestID RequestID.
     * @return {@link ResponseEntity}.
     */
    @DeleteMapping(path = "/requests/{requestID}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity removeRequest(@PathVariable String requestID) {
        PlanoResponse planoResponse = planoProcessor.removeRequest(requestID);
        ResponseEntity<PlanoResponse> responseEntity = composeResponseEntity(planoResponse);

        return responseEntity;
    }

    /**
     * GET method to return system health.
     * @return {@link ResponseEntity}.
     */
    @GetMapping(path = "/health", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity health() {
        return ResponseEntity.ok().body("healthy");
    }

    public void setPlanoProcessor(PlanoProcessor planoProcessor) {
        this.planoProcessor = planoProcessor;
    }

    /**
     * Compose {@link ResponseEntity} based on {@link PlanoResponse}.
     * @param planoResponse {@link PlanoResponse}.
     * @return {@link ResponseEntity}.
     */
    private ResponseEntity<PlanoResponse> composeResponseEntity(PlanoResponse planoResponse) {
        ResponseEntity<PlanoResponse> responseEntity = null;
        switch (planoResponse.getPlanoStatus()) {
            case SUCCESS:
                responseEntity = new ResponseEntity<>(planoResponse, HttpStatus.OK);
                break;
            case CREATED:
                responseEntity = new ResponseEntity<>(planoResponse, HttpStatus.CREATED);
                break;
            case NOT_FOUND:
                responseEntity = new ResponseEntity<>(planoResponse, HttpStatus.NOT_FOUND);
                break;
            case INVALID_INPUT:
                responseEntity = new ResponseEntity<>(planoResponse, HttpStatus.BAD_REQUEST);
                break;
        }

        return responseEntity;
    }
}
