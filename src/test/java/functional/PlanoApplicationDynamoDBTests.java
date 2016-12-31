package functional;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.local.embedded.DynamoDBEmbedded;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.CreateTableResult;
import com.amazonaws.services.dynamodbv2.model.DeleteTableRequest;
import com.amazonaws.services.dynamodbv2.model.DeleteTableResult;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.plano.PlanoApplication;
import org.plano.data.PlanoRequest;
import org.plano.data.PlanoResponse;
import org.plano.master.PlanoMaster;
import org.plano.repository.dynamodb.model.DynamoDBPlanoRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import utils.DataTestUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = {PlanoApplication.class,
                PlanoApplicationDynamoDBTests.ContextConfiguration.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PlanoApplicationDynamoDBTests {

    private static final String TABLE_NAME = "PlanoRequests";
    private static final String BASE_URL = "/requests";
    private static final String REQUEST_ID = "4e212f33-14b8-4933-b18d-d55dffc7f458";

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private AmazonDynamoDB dynamoDB;

    @Autowired
    private DynamoDBMapper mapper;

    @Autowired
    private PlanoMaster planoMaster;

    @MockBean
    private HttpClient mockedHttpClient;

    @PostConstruct
    public void init() {
        mapper = new DynamoDBMapper(dynamoDB);
        planoMaster.start();
    }

    @PreDestroy
    public void destroy() {
        planoMaster.shutdown();
    }

    @Before
    public void before() {
        CreateTableResult result = createPlanoRequestsTable();
        Assert.assertEquals(TABLE_NAME, result.getTableDescription().getTableName());
    }

    @After
    public void after() {
        DeleteTableResult result = deletePlanoRequestsTable();
        Assert.assertEquals(TABLE_NAME, result.getTableDescription().getTableName());
    }

    @Test
    public void contextLoads() {
    }

    @Test
    public void testAddRequestCreated() {
        String url = BASE_URL;
        PlanoRequest planoRequest = DataTestUtils.createPlanoRequestWithNullRequestID();
        HttpEntity<PlanoRequest> httpEntity = new HttpEntity<>(planoRequest);

        ResponseEntity<PlanoResponse> responseEntity = testRestTemplate.exchange(
                url,
                HttpMethod.POST,
                httpEntity,
                PlanoResponse.class);

        Assert.assertEquals(HttpStatus.CREATED.value(), responseEntity.getStatusCodeValue());
        Assert.assertNotNull(responseEntity.getBody().getRequestID());
    }

    @Test
    public void testAddRequestInvalidInput() {
        String url = BASE_URL;
        PlanoRequest planoRequest = DataTestUtils.createPlanoRequestWithNullRequestID();
        planoRequest.setHttpRequest(null);

        HttpEntity<PlanoRequest> httpEntity = new HttpEntity<>(planoRequest);

        ResponseEntity<PlanoResponse> responseEntity = testRestTemplate.exchange(
                url,
                HttpMethod.POST,
                httpEntity,
                PlanoResponse.class);

        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), responseEntity.getStatusCodeValue());
        Assert.assertNotNull(responseEntity.getBody().getErrorMessage());
    }

    @Test
    public void testGetRequestSuccess() {
        String postUrl = BASE_URL;
        PlanoRequest planoRequest = DataTestUtils.createPlanoRequestWithNullRequestID();
        HttpEntity<PlanoRequest> httpEntity = new HttpEntity<>(planoRequest);

        ResponseEntity<PlanoResponse> responseEntity = testRestTemplate.exchange(
                postUrl,
                HttpMethod.POST,
                httpEntity,
                PlanoResponse.class);

        /**
         * Request ID is generated and returned by Plano. For validation we need to
         * manually set it.
         */
        String requestID = responseEntity.getBody().getRequestID();
        planoRequest.setRequestID(requestID);

        String getUrl = BASE_URL + "/" + requestID;
        responseEntity = testRestTemplate.exchange(
                getUrl,
                HttpMethod.GET,
                null,
                PlanoResponse.class);

        Assert.assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCodeValue());
        Assert.assertEquals(planoRequest, responseEntity.getBody().getPlanoRequest());
    }

    @Test
    public void testGetRequestNotFound() {
        String getUrl = BASE_URL + "/" + REQUEST_ID;

        ResponseEntity<PlanoResponse> responseEntity = testRestTemplate.exchange(
                getUrl,
                HttpMethod.GET,
                null,
                PlanoResponse.class);

        Assert.assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCodeValue());
        Assert.assertNotNull(responseEntity.getBody().getErrorMessage());
    }

    @Test
    public void testUpdateRequestSuccess() {
        String postUrl = BASE_URL;
        PlanoRequest planoRequest = DataTestUtils.createPlanoRequestWithNullRequestID();
        HttpEntity<PlanoRequest> httpEntity = new HttpEntity<>(planoRequest);

        ResponseEntity<PlanoResponse> responseEntity = testRestTemplate.exchange(
                postUrl,
                HttpMethod.POST,
                httpEntity,
                PlanoResponse.class);

        String requestID = responseEntity.getBody().getRequestID();

        String putUrl = BASE_URL + "/" + requestID;
        planoRequest = DataTestUtils.createPlanoRequest();
        planoRequest.setRequestID(requestID);
        httpEntity = new HttpEntity<>(planoRequest);

        responseEntity = testRestTemplate.exchange(
                putUrl,
                HttpMethod.PUT,
                httpEntity,
                PlanoResponse.class);

        Assert.assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCodeValue());
        Assert.assertEquals(requestID, responseEntity.getBody().getRequestID());

        String getUrl = BASE_URL + "/" + requestID;

        responseEntity = testRestTemplate.getForEntity(getUrl, PlanoResponse.class);

        Assert.assertEquals(planoRequest, responseEntity.getBody().getPlanoRequest());
    }

    @Test
    public void testUpdateRequestInvalidInput() {
        String postUrl = BASE_URL;
        PlanoRequest planoRequest = DataTestUtils.createPlanoRequestWithNullRequestID();
        HttpEntity<PlanoRequest> httpEntity = new HttpEntity<>(planoRequest);

        ResponseEntity<PlanoResponse> responseEntity = testRestTemplate.exchange(
                postUrl,
                HttpMethod.POST,
                httpEntity,
                PlanoResponse.class);

        String requestID = responseEntity.getBody().getRequestID();

        String putUrl = BASE_URL + "/" + requestID;
        planoRequest = DataTestUtils.createPlanoRequest();
        planoRequest.setHttpRequest(null);
        httpEntity = new HttpEntity<>(planoRequest);

        responseEntity = testRestTemplate.exchange(
                putUrl,
                HttpMethod.PUT,
                httpEntity,
                PlanoResponse.class);

        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), responseEntity.getStatusCodeValue());
        Assert.assertNotNull(responseEntity.getBody().getErrorMessage());
    }

    @Test
    public void testRemoveRequestSuccess() {
        String postUrl = BASE_URL;
        PlanoRequest planoRequest = DataTestUtils.createPlanoRequestWithNullRequestID();
        HttpEntity<PlanoRequest> httpEntity = new HttpEntity<>(planoRequest);

        ResponseEntity<PlanoResponse> responseEntity = testRestTemplate.exchange(
                postUrl,
                HttpMethod.POST,
                httpEntity,
                PlanoResponse.class);

        String requestID = responseEntity.getBody().getRequestID();

        String deleteUrl = BASE_URL + "/" + requestID;

        responseEntity = testRestTemplate.exchange(
                deleteUrl,
                HttpMethod.DELETE,
                null,
                PlanoResponse.class);

        Assert.assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCodeValue());
        Assert.assertEquals(requestID, responseEntity.getBody().getRequestID());

        String getUrl = BASE_URL + "/" + requestID;

        responseEntity = testRestTemplate.exchange(
                getUrl,
                HttpMethod.GET,
                null,
                PlanoResponse.class);

        Assert.assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCodeValue());
    }

    @Test
    public void testRemoveRequestNotFound() {
        String deleteUrl = BASE_URL + "/" + REQUEST_ID;

        ResponseEntity<PlanoResponse> responseEntity = testRestTemplate.exchange(
                deleteUrl,
                HttpMethod.DELETE,
                null,
                PlanoResponse.class);

        Assert.assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCodeValue());
        Assert.assertNotNull(responseEntity.getBody().getErrorMessage());
    }

    @Test
    public void testPlanoWorkerScanAndInvokeSuccess() throws IOException, InterruptedException {
        HttpResponse httpResponse = DataTestUtils.createHttpResponse(HttpStatus.OK.value());

        Mockito.when(mockedHttpClient.execute(Mockito.any(HttpUriRequest.class)))
                .thenReturn(httpResponse);

        String postUrl = BASE_URL;
        PlanoRequest planoRequest = DataTestUtils.createPlanoRequestWithNullRequestID();
        HttpEntity<PlanoRequest> httpEntity = new HttpEntity<>(planoRequest);

        Integer numberOfRequests = 10;
        for (int i = 0; i < numberOfRequests; i++) {
            testRestTemplate.exchange(postUrl, HttpMethod.POST, httpEntity, PlanoResponse.class);
        }

        Thread.sleep(500L);

        Mockito.verify(mockedHttpClient, Mockito.times(numberOfRequests)).execute(Mockito.any(HttpUriRequest.class));
    }

    @Test
    public void testPlanoWorkerRetryRequestWhenFailedToInvokeEndpoint() throws IOException, InterruptedException {
        HttpResponse httpResponse = DataTestUtils.createHttpResponse(HttpStatus.NOT_FOUND.value());

        Mockito.when(mockedHttpClient.execute(Mockito.any(HttpUriRequest.class)))
                .thenReturn(httpResponse);

        String postUrl = BASE_URL;
        Integer numberOfExecutions = 5;
        PlanoRequest planoRequest = DataTestUtils.createPlanoRequestWithNullRequestID();
        planoRequest.getSchedulePolicy().setMultiplier(1);
        planoRequest.getSchedulePolicy().setNumberOfExecutions(numberOfExecutions);
        planoRequest.getSchedulePolicy().setExecutionIntervalMs(100L);

        HttpEntity<PlanoRequest> httpEntity = new HttpEntity<>(planoRequest);

        testRestTemplate.exchange(postUrl, HttpMethod.POST, httpEntity, PlanoResponse.class);

        Thread.sleep(1000L);

        Mockito.verify(mockedHttpClient, Mockito.times(numberOfExecutions)).execute(Mockito.any(HttpUriRequest.class));
    }

    private CreateTableResult createPlanoRequestsTable() {
        CreateTableRequest request = mapper.generateCreateTableRequest(DynamoDBPlanoRequest.class)
                .withProvisionedThroughput(new ProvisionedThroughput()
                        .withReadCapacityUnits(5L)
                        .withWriteCapacityUnits(6L));

        CreateTableResult createTableResult = dynamoDB.createTable(request);

        return createTableResult;
    }

    private DeleteTableResult deletePlanoRequestsTable() {
        DeleteTableRequest request = mapper.generateDeleteTableRequest(DynamoDBPlanoRequest.class);
        DeleteTableResult deleteTableResult = dynamoDB.deleteTable(request);

        return deleteTableResult;
    }

    @TestConfiguration
    public static class ContextConfiguration {
        @Bean
        @Primary
        public DynamoDBMapper createDynamoDBMapper(AmazonDynamoDB amazonDynamoDB) {
            return new DynamoDBMapper(amazonDynamoDB);
        }

        @Bean
        @Primary
        public AmazonDynamoDB createAmazonDynamoDBLocal() throws Exception {
            return DynamoDBEmbedded.create().amazonDynamoDB();
        }
    }
}
