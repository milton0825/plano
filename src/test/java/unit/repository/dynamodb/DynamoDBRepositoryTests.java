package unit.repository.dynamodb;

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
import org.plano.repository.dynamodb.DynamoDBRepository;
import org.plano.repository.dynamodb.model.DynamoDBPlanoRequest;
import utils.DataTestUtils;

import java.util.Date;


public class DynamoDBRepositoryTests {
    private static final String TABLE_NAME = "PlanoRequests";
    private static final Integer LOCK_DURATION_MS = 50000;
    private static DynamoDBRepository dynamoDBRepository;
    private static AmazonDynamoDB dynamoDB;
    private static DynamoDBMapper mapper;

    @BeforeClass
    public static void beforeClass() {
        dynamoDB = DynamoDBEmbedded.create().amazonDynamoDB();
        DynamoDBRepository.setAmazonDynamoDB(dynamoDB);
        dynamoDBRepository = new DynamoDBRepository();
        dynamoDBRepository.setLockDurationMs(LOCK_DURATION_MS);
        dynamoDBRepository.init();
        mapper = new DynamoDBMapper(dynamoDB);
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
        dynamoDBRepository.addRequest(planoRequest);
        long numberOfItems = dynamoDB.describeTable(TABLE_NAME).getTable().getItemCount();

        Assert.assertEquals(1L, numberOfItems);
    }

    @Test(expected = InvalidRequestException.class)
    public void testAddRequestInputIsNull() throws InvalidRequestException {
        dynamoDBRepository.addRequest(null);
    }

    @Test
    public void testGetRequest() throws PlanoException {
        PlanoRequest planoRequest = DataTestUtils.createPlanoRequest();
        dynamoDBRepository.addRequest(planoRequest);
        PlanoRequest retrievedRequest = dynamoDBRepository.getRequest(planoRequest.getRequestID());

        Assert.assertEquals(retrievedRequest, planoRequest);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testGetRequestNotExist() throws ResourceNotFoundException {
        dynamoDBRepository.getRequest("123");
    }

    @Test
    public void testUpdateRequest() throws PlanoException {
        PlanoRequest planoRequest = DataTestUtils.createPlanoRequest();
        dynamoDBRepository.addRequest(planoRequest);
        planoRequest.setExecutionTime(new Date());
        dynamoDBRepository.updateRequest(planoRequest);
        PlanoRequest retrievedRequest = dynamoDBRepository.getRequest(planoRequest.getRequestID());

        Assert.assertEquals(planoRequest, retrievedRequest);
    }

    @Test(expected = InvalidRequestException.class)
    public void testUpdateRequestWithInvalidRequest() throws PlanoException {
        dynamoDBRepository.updateRequest(null);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testRemoveRequest() throws PlanoException {
        PlanoRequest planoRequest = DataTestUtils.createPlanoRequest();
        dynamoDBRepository.addRequest(planoRequest);
        dynamoDBRepository.removeRequest(planoRequest.getRequestID());
        dynamoDBRepository.getRequest(planoRequest.getRequestID());

        Assert.fail("PlanoRequest is not deleted successfully.");
    }

    @Test(expected = InvalidRequestException.class)
    public void testRemoveRequestNotExist() throws PlanoException {
        dynamoDBRepository.removeRequest("123");
    }

    @Test
    public void testFindNextRequestAndLockReturnCorrectRequest() throws PlanoException {
        PlanoRequest planoRequest = DataTestUtils.createPlanoRequest();
        dynamoDBRepository.addRequest(planoRequest);
        PlanoRequest retrievedRequest = dynamoDBRepository.findNextRequestAndLock();

        Assert.assertEquals(planoRequest, retrievedRequest);
    }

    @Test
    public void testFindNextRequestAndLockNotReturnFutureRequest() throws PlanoException {
        PlanoRequest planoRequest = DataTestUtils.createPlanoRequest();
        Date futureDate = new Date(System.currentTimeMillis() + 100000L);
        planoRequest.setExecutionTime(futureDate);
        dynamoDBRepository.addRequest(planoRequest);
        PlanoRequest nextPlanoRequest = dynamoDBRepository.findNextRequestAndLock();

        Assert.assertNull(nextPlanoRequest);
    }

    @Test
    public void testFindNextRequestAndLockNotReturnLockedRequest() throws PlanoException {
        PlanoRequest planoRequest = DataTestUtils.createPlanoRequest();
        dynamoDBRepository.addRequest(planoRequest);
        dynamoDBRepository.findNextRequestAndLock();
        PlanoRequest retrievedRequest = dynamoDBRepository.findNextRequestAndLock();

        Assert.assertNull(retrievedRequest);
    }

    @Test
    public void testUpdateRequestAndUnlock() throws PlanoException {
        PlanoRequest planoRequest = DataTestUtils.createPlanoRequest();
        dynamoDBRepository.addRequest(planoRequest);
        dynamoDBRepository.findNextRequestAndLock();
        dynamoDBRepository.updateRequestAndUnlock(planoRequest);
        dynamoDBRepository.findNextRequestAndLock();
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
}
