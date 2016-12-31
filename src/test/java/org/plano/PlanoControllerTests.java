package org.plano;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.plano.data.PlanoRequest;
import org.plano.data.PlanoResponse;
import org.plano.data.PlanoStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class PlanoControllerTests {

    @Mock
    PlanoProcessor mockedPlanoProcessor;

    PlanoController planoController;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        planoController = new PlanoController();
        planoController.setPlanoProcessor(mockedPlanoProcessor);
    }

    @Test
    public void testGetRequestSuccess() {
        PlanoResponse planoResponse = new PlanoResponse();
        planoResponse.setPlanoStatus(PlanoStatus.SUCCESS);

        Mockito.when(mockedPlanoProcessor.getRequest(Mockito.anyString()))
                .thenReturn(planoResponse);

        ResponseEntity<PlanoResponse> responseEntity =  planoController.getRequest(Mockito.anyString());

        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testGetRequestNotFound() {
        PlanoResponse planoResponse = new PlanoResponse();
        planoResponse.setPlanoStatus(PlanoStatus.NOT_FOUND);

        Mockito.when(mockedPlanoProcessor.getRequest(Mockito.anyString()))
                .thenReturn(planoResponse);

        ResponseEntity<PlanoResponse> responseEntity =  planoController.getRequest(Mockito.anyString());

        Assert.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testAddRequestSuccess() {
        PlanoResponse planoResponse = new PlanoResponse();
        planoResponse.setPlanoStatus(PlanoStatus.CREATED);

        Mockito.when(mockedPlanoProcessor.addRequest(Mockito.any(PlanoRequest.class)))
                .thenReturn(planoResponse);

        ResponseEntity<PlanoResponse> responseEntity =  planoController.addRequest(Mockito.any(PlanoRequest.class));

        Assert.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    public void testAddRequestInvalidInput() {
        PlanoResponse planoResponse = new PlanoResponse();
        planoResponse.setPlanoStatus(PlanoStatus.INVALID_INPUT);

        Mockito.when(mockedPlanoProcessor.addRequest(Mockito.any(PlanoRequest.class)))
                .thenReturn(planoResponse);

        ResponseEntity<PlanoResponse> responseEntity =  planoController.addRequest(Mockito.any(PlanoRequest.class));

        Assert.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testUpdateRequestSuccess() {
        PlanoResponse planoResponse = new PlanoResponse();
        planoResponse.setPlanoStatus(PlanoStatus.SUCCESS);

        Mockito.when(mockedPlanoProcessor.updateRequest(Mockito.anyString(), Mockito.any(PlanoRequest.class)))
                .thenReturn(planoResponse);

        ResponseEntity<PlanoResponse> responseEntity =  planoController.updateRequest(Mockito.anyString(),
                Mockito.any(PlanoRequest.class));

        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testUpdateRequestInvalidInput() {
        PlanoResponse planoResponse = new PlanoResponse();
        planoResponse.setPlanoStatus(PlanoStatus.INVALID_INPUT);

        Mockito.when(mockedPlanoProcessor.updateRequest(Mockito.anyString(), Mockito.any(PlanoRequest.class)))
                .thenReturn(planoResponse);

        ResponseEntity<PlanoResponse> responseEntity =  planoController.updateRequest(Mockito.anyString(),
                Mockito.any(PlanoRequest.class));

        Assert.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testRemoveRequestSuccess() {
        PlanoResponse planoResponse = new PlanoResponse();
        planoResponse.setPlanoStatus(PlanoStatus.SUCCESS);

        Mockito.when(mockedPlanoProcessor.removeRequest(Mockito.anyString()))
                .thenReturn(planoResponse);

        ResponseEntity<PlanoResponse> responseEntity =  planoController.removeRequest(Mockito.anyString());

        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testRemoveRequestInvalidInput() {
        PlanoResponse planoResponse = new PlanoResponse();
        planoResponse.setPlanoStatus(PlanoStatus.INVALID_INPUT);

        Mockito.when(mockedPlanoProcessor.removeRequest(Mockito.anyString()))
                .thenReturn(planoResponse);

        ResponseEntity<PlanoResponse> responseEntity =  planoController.removeRequest(Mockito.anyString());

        Assert.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @After
    public void after() {
        Mockito.reset(mockedPlanoProcessor);
    }
}
