package unit;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.plano.PlanoProcessor;
import org.plano.data.PlanoRequest;
import org.plano.data.PlanoResponse;
import org.plano.data.PlanoStatus;
import org.plano.exception.InvalidRequestException;
import org.plano.exception.ResourceNotFoundException;
import org.plano.repository.Repository;
import utils.DataTestUtils;

public class PlanoProcessorTests {
    @Mock
    private Repository<PlanoRequest> mockedRepositoryWrapper;
    private PlanoProcessor planoProcessor;
    private static final String REQUEST_ID = "43c8e50c-2e5b-4e36-a3d6-4e144f6c6d5d";

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        planoProcessor = new PlanoProcessor();
        planoProcessor.setRepositoryWrapper(mockedRepositoryWrapper);
    }

    @Test
    public void testGetRequestSuccess() throws ResourceNotFoundException {
        PlanoRequest planoRequest = DataTestUtils.createPlanoRequest();

        Mockito.when(mockedRepositoryWrapper.getRequest(REQUEST_ID))
                .thenReturn(planoRequest);

        PlanoResponse planoResponse = planoProcessor.getRequest(REQUEST_ID);

        Assert.assertEquals(PlanoStatus.SUCCESS, planoResponse.getPlanoStatus());
        Assert.assertEquals(planoRequest, planoResponse.getPlanoRequest());
    }

    @Test
    public void testGetRequestNotFound() throws ResourceNotFoundException {
        Mockito.when(mockedRepositoryWrapper.getRequest(Mockito.anyString()))
                .thenThrow(new ResourceNotFoundException("Resource not found."));

        PlanoResponse planoResponse = planoProcessor.getRequest(REQUEST_ID);

        Assert.assertEquals(PlanoStatus.NOT_FOUND, planoResponse.getPlanoStatus());
        Assert.assertNotNull(planoResponse.getErrorMessage());
    }

    @Test
    public void testAddRequestCreated() throws InvalidRequestException {
        PlanoRequest mockedPlanoRequest = Mockito.mock(PlanoRequest.class);

        Mockito.when(mockedPlanoRequest.isValid()).thenReturn(true);

        Mockito.when(mockedRepositoryWrapper.addRequest(mockedPlanoRequest))
                .thenReturn(REQUEST_ID);

        PlanoResponse planoResponse = planoProcessor.addRequest(mockedPlanoRequest);

        Assert.assertEquals(PlanoStatus.CREATED, planoResponse.getPlanoStatus());
        Assert.assertEquals(REQUEST_ID, planoResponse.getRequestID());
    }

    @Test
    public void testAddRequestInvalidInput() {
        PlanoRequest mockedPlanoRequest = Mockito.mock(PlanoRequest.class);

        Mockito.when(mockedPlanoRequest.isValid()).thenReturn(false);

        PlanoResponse planoResponse = planoProcessor.addRequest(mockedPlanoRequest);

        Assert.assertEquals(PlanoStatus.INVALID_INPUT, planoResponse.getPlanoStatus());
        Assert.assertNotNull(planoResponse.getErrorMessage());
    }

    @Test
    public void testUpdateRequestSuccess() {
        PlanoRequest mockedPlanoRequest = Mockito.mock(PlanoRequest.class);

        Mockito.when(mockedPlanoRequest.isValid()).thenReturn(true);

        PlanoResponse planoResponse = planoProcessor.updateRequest(REQUEST_ID, mockedPlanoRequest);

        Assert.assertEquals(PlanoStatus.SUCCESS, planoResponse.getPlanoStatus());
        Assert.assertEquals(REQUEST_ID, planoResponse.getRequestID());
    }

    @Test
    public void testUpdateRequestInvalidInput() {
        PlanoRequest mockedPlanoRequest = Mockito.mock(PlanoRequest.class);

        Mockito.when(mockedPlanoRequest.isValid()).thenReturn(false);

        PlanoResponse planoResponse = planoProcessor.updateRequest(REQUEST_ID, mockedPlanoRequest);

        Assert.assertEquals(PlanoStatus.INVALID_INPUT, planoResponse.getPlanoStatus());
        Assert.assertNotNull(planoResponse.getErrorMessage());
    }

    @Test
    public void testRemoveRequestSuccess() {
        PlanoResponse planoResponse = planoProcessor.removeRequest(REQUEST_ID);

        Assert.assertEquals(PlanoStatus.SUCCESS, planoResponse.getPlanoStatus());
        Assert.assertEquals(REQUEST_ID, planoResponse.getRequestID());
    }

    @Test
    public void testRemoveRequestInvalidInput() throws InvalidRequestException {
        Mockito.doThrow(new InvalidRequestException("Invalid request."))
                .when(mockedRepositoryWrapper).removeRequest(REQUEST_ID);

        PlanoResponse planoResponse = planoProcessor.removeRequest(REQUEST_ID);

        Assert.assertEquals(PlanoStatus.INVALID_INPUT, planoResponse.getPlanoStatus());
    }

    @After
    public void after() {
        Mockito.reset(mockedRepositoryWrapper);
    }
}
