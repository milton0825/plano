package org.plano.repository.dynamodb;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.local.embedded.DynamoDBEmbedded;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.CreateTableResult;
import com.amazonaws.services.dynamodbv2.model.DeleteTableRequest;
import com.amazonaws.services.dynamodbv2.model.DeleteTableResult;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.plano.data.PlanoRequest;
import org.plano.exception.InvalidRequestException;
import org.plano.exception.PlanoException;
import org.plano.exception.ResourceNotFoundException;
import org.plano.repository.dynamodb.model.DynamoDBPlanoRequest;
import utils.DataTestUtils;

import java.util.Date;

public class DynamoDBRepositoryTests {
    private static final String TABLE_NAME = "PlanoRequests";
    private static final Integer LOCK_DURATION_MS = 50000;
    private static DynamoDBRepository sDynamoDBRepository;
    private static AmazonDynamoDB sDynamoDB;
    private static DynamoDBMapper sDynamoDBMapper;

    @BeforeClass
    public static void beforeClass() {
        sDynamoDB = DynamoDBEmbedded.create().amazonDynamoDB();
        sDynamoDBMapper = new DynamoDBMapper(sDynamoDB);
        sDynamoDBRepository = new DynamoDBRepository();
        sDynamoDBRepository.setDynamoDBMapper(sDynamoDBMapper);
        sDynamoDBRepository.setLockDurationMs(LOCK_DURATION_MS);
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
    public void testAddRequest() throws InvalidRequestException {
        PlanoRequest planoRequest = DataTestUtils.createPlanoRequest();
        sDynamoDBRepository.addRequest(planoRequest);
        long numberOfItems = sDynamoDB.describeTable(TABLE_NAME).getTable().getItemCount();

        Assert.assertEquals(1L, numberOfItems);
    }

    @Test(expected = InvalidRequestException.class)
    public void testAddRequestInputIsNull() throws InvalidRequestException {
        sDynamoDBRepository.addRequest(null);
    }

    @Test
    public void testGetRequest() throws PlanoException {
        PlanoRequest planoRequest = DataTestUtils.createPlanoRequest();
        sDynamoDBRepository.addRequest(planoRequest);
        PlanoRequest retrievedRequest = sDynamoDBRepository.getRequest(planoRequest.getRequestID());

        Assert.assertEquals(retrievedRequest, planoRequest);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testGetRequestNotExist() throws ResourceNotFoundException {
        sDynamoDBRepository.getRequest("123");
    }

    @Test
    public void testUpdateRequest() throws PlanoException {
        PlanoRequest planoRequest = DataTestUtils.createPlanoRequest();
        sDynamoDBRepository.addRequest(planoRequest);
        planoRequest.setExecutionTime(new Date());
        sDynamoDBRepository.updateRequest(planoRequest);
        PlanoRequest retrievedRequest = sDynamoDBRepository.getRequest(planoRequest.getRequestID());

        Assert.assertEquals(planoRequest, retrievedRequest);
    }

    @Test(expected = InvalidRequestException.class)
    public void testUpdateRequestWithInvalidRequest() throws PlanoException {
        sDynamoDBRepository.updateRequest(null);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testRemoveRequest() throws PlanoException {
        PlanoRequest planoRequest = DataTestUtils.createPlanoRequest();
        sDynamoDBRepository.addRequest(planoRequest);
        sDynamoDBRepository.removeRequest(planoRequest.getRequestID());
        sDynamoDBRepository.getRequest(planoRequest.getRequestID());

        Assert.fail("PlanoRequest is not deleted successfully.");
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testRemoveRequestNotExist() throws PlanoException {
        sDynamoDBRepository.removeRequest("123");
    }

    @Test
    public void testFindNextRequestAndLockReturnCorrectRequest() throws PlanoException {
        PlanoRequest planoRequest = DataTestUtils.createPlanoRequest();
        sDynamoDBRepository.addRequest(planoRequest);
        PlanoRequest retrievedRequest = sDynamoDBRepository.findNextRequestAndLock();

        Assert.assertEquals(planoRequest, retrievedRequest);
    }

    @Test
    public void testFindNextRequestAndLockNotReturnFutureRequest() throws PlanoException {
        PlanoRequest planoRequest = DataTestUtils.createPlanoRequest();
        Date futureDate = new Date(System.currentTimeMillis() + 100000L);
        planoRequest.setExecutionTime(futureDate);
        sDynamoDBRepository.addRequest(planoRequest);
        PlanoRequest nextPlanoRequest = sDynamoDBRepository.findNextRequestAndLock();

        Assert.assertNull(nextPlanoRequest);
    }

    @Test
    public void testFindNextRequestAndLockNotReturnLockedRequest() throws PlanoException {
        PlanoRequest planoRequest = DataTestUtils.createPlanoRequest();
        sDynamoDBRepository.addRequest(planoRequest);
        sDynamoDBRepository.findNextRequestAndLock();
        PlanoRequest retrievedRequest = sDynamoDBRepository.findNextRequestAndLock();

        Assert.assertNull(retrievedRequest);
    }

    @Test
    public void testUpdateRequestAndUnlock() throws PlanoException {
        PlanoRequest planoRequest = DataTestUtils.createPlanoRequest();
        sDynamoDBRepository.addRequest(planoRequest);
        sDynamoDBRepository.findNextRequestAndLock();
        sDynamoDBRepository.updateRequestAndUnlock(planoRequest);
        sDynamoDBRepository.findNextRequestAndLock();
    }

    private CreateTableResult createPlanoRequestsTable() {
        CreateTableRequest request = sDynamoDBMapper.generateCreateTableRequest(DynamoDBPlanoRequest.class)
                .withProvisionedThroughput(new ProvisionedThroughput()
                        .withReadCapacityUnits(5L)
                        .withWriteCapacityUnits(6L));

        CreateTableResult createTableResult = sDynamoDB.createTable(request);

        return createTableResult;
    }

    private DeleteTableResult deletePlanoRequestsTable() {
        DeleteTableRequest request = sDynamoDBMapper.generateDeleteTableRequest(DynamoDBPlanoRequest.class);
        DeleteTableResult deleteTableResult = sDynamoDB.deleteTable(request);

        return deleteTableResult;
    }
}
