package functional;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.local.embedded.DynamoDBEmbedded;
import com.amazonaws.services.dynamodbv2.local.main.ServerRunner;
import com.amazonaws.services.dynamodbv2.local.server.DynamoDBProxyServer;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.CreateTableResult;
import com.amazonaws.services.dynamodbv2.model.DescribeTableResult;
import com.amazonaws.services.dynamodbv2.model.ListTablesResult;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.plano.repository.dynamodb.model.DynamoDBPlanoRequest;
import utils.DynamoDBTestUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ctsai on 11/22/16.
 */
public class DynamoDBLocalTests {

    static final private String TABLE_NAME = "PlanoRequests";
    private AmazonDynamoDB dynamoDB;
    private DynamoDBMapper mapper;

    @Before
    public void before() {


        dynamoDB = DynamoDBEmbedded.create().amazonDynamoDB();
        mapper = new DynamoDBMapper(dynamoDB);
    }

    @After
    public void after() {
        dynamoDB.shutdown();
    }

    @Test
    public void testCreatePlanoRequestsTable() {
        CreateTableResult result = createPlanoRequestsTable();
        Assert.assertEquals(TABLE_NAME, result.getTableDescription().getTableName());
    }

    @Test
    public void testAddPlanoRequest() {
        createPlanoRequestsTable();
        DynamoDBPlanoRequest dynamoDBPlanoRequest = DynamoDBTestUtils.createDynamoDBPlanoRequest();
        mapper.save(dynamoDBPlanoRequest);

        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":requestID", new AttributeValue().withS(dynamoDBPlanoRequest.getRequestID()));

        DynamoDBQueryExpression<DynamoDBPlanoRequest> queryExpression =
                new DynamoDBQueryExpression<DynamoDBPlanoRequest>()
                .withKeyConditionExpression("requestID = :requestID")
                .withExpressionAttributeValues(eav);
        DynamoDBPlanoRequest request = mapper.load(DynamoDBPlanoRequest.class, dynamoDBPlanoRequest.getRequestID());

        Assert.assertEquals(dynamoDBPlanoRequest, request);
    }

    private CreateTableResult createPlanoRequestsTable() {
        CreateTableRequest request = mapper.generateCreateTableRequest(DynamoDBPlanoRequest.class)
                .withProvisionedThroughput(new ProvisionedThroughput()
                        .withReadCapacityUnits(5L)
                        .withWriteCapacityUnits(6L));

        CreateTableResult createTableResult = dynamoDB.createTable(request);

        return createTableResult;
    }

    @Ignore
    @Test
    public void testDynamoDB() throws Exception {
        try {
            // Create an in-memory and in-process instance of DynamoDB Local that skips HTTP
            dynamoDB = DynamoDBEmbedded.create().amazonDynamoDB();
            // use the DynamoDB API with DynamoDBEmbedded
            listTables(dynamoDB.listTables(), "DynamoDB Embedded");
        } finally {
            // Shutdown the thread pools in DynamoDB Local / Embedded
            if(dynamoDB != null) {
                dynamoDB.shutdown();
            }
        }

        // Create an in-memory and in-process instance of DynamoDB Local that runs over HTTP
        final String[] localArgs = { "-inMemory" };
        DynamoDBProxyServer server = null;
        try {
            server = ServerRunner.createServerFromCommandLineArgs(localArgs);
            server.start();
            dynamoDB = new AmazonDynamoDBClient(new BasicAWSCredentials("access", "secret"));
            dynamoDB.setEndpoint("http://localhost:8000");

            // use the DynamoDB API over HTTP
            listTables(dynamoDB.listTables(), "DynamoDB Local over HTTP");
        } finally {
            // Stop the DynamoDB Local endpoint
            if(server != null) {
                server.stop();
            }
        }
    }

    public static void listTables(ListTablesResult result, String method) {
        System.out.println("found " + Integer.toString(result.getTableNames().size()) + " tables with " + method);
        for(String table : result.getTableNames()) {
            System.out.println(table);
        }
    }

    void getTableInformation() {

        System.out.println("Describing " + TABLE_NAME);

        DescribeTableResult tableDescription = dynamoDB.describeTable(TABLE_NAME);
        System.out.format("Name: %s:\n" + "Status: %s \n"
                        + "Provisioned Throughput (read capacity units/sec): %d \n"
                        + "Provisioned Throughput (write capacity units/sec): %d \n",
                tableDescription.getTable().getTableName(),
                tableDescription.getTable().getTableStatus(),
                tableDescription.getTable().getProvisionedThroughput().getReadCapacityUnits(),
                tableDescription.getTable().getProvisionedThroughput().getWriteCapacityUnits());
    }
}
