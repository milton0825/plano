package org.plano;

/**
 * Created by ctsai on 11/5/16.
 */

import org.plano.data.PlanoResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PlanoController {

    @GetMapping(path = "/requests/{requestID}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getRequest(@PathVariable String requestID,
            @RequestHeader String transactionGUID) {
        return null;
    }

    @PostMapping(path = "/requests",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addRequest() {
        return null;
    }

    @PutMapping(path = "/requests/{requestID}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateRequest(@PathVariable String requestID) {
        return null;
    }

    @DeleteMapping(path = "/requests/{requestID}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity removeRequest(@PathVariable String requestID) {
        return null;
    }

    @GetMapping(path = "/health", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity health() {
        return ResponseEntity.ok().body("healthy");
    }
}
