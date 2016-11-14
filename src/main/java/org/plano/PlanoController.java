package org.plano;

/**
 * Created by ctsai on 11/5/16.
 */

import org.plano.data.PlanoResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class PlanoController {

    @GetMapping(path = "/entries/{entryID}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getEntry(@PathVariable String entryID,
            @RequestHeader String transactionGUID) {

    }

    @PostMapping(path = "/entries",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addEntry() {
        PlanoResponse planoResponse = new PlanoResponse();

        return ResponseEntity.ok().body(planoResponse);
    }

    @PutMapping(path = "/entries/{entryID}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateEntry(@PathVariable String entryID) {

    }

    @DeleteMapping(path = "/entries/{entryID}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity removeEntry(@PathVariable String entryID) {

    }

    @GetMapping(path = "/health", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity health() {
        return ResponseEntity.ok().body("healthy");
    }
}
