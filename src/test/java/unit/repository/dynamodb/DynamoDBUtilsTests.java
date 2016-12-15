package unit.repository.dynamodb;

import org.junit.Assert;
import org.junit.Test;
import org.plano.data.PlanoRequest;
import org.plano.repository.dynamodb.DynamoDBUtils;
import org.plano.repository.dynamodb.model.DynamoDBPlanoRequest;
import utils.DynamoDBTestUtils;

public class DynamoDBUtilsTests {

    @Test(expected = IllegalArgumentException.class)
    public void testCreatePlanoRequestWithNull() {
        DynamoDBUtils.createPlanoRequest(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateDynamoDBPlanoRequestWithNull() {
        DynamoDBUtils.createDynamoDBPlanoRequest(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateHttpRequestWithNull() {
        DynamoDBUtils.createHttpRequest(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateDynamoDBHttpRequestWithNull() {
        DynamoDBUtils.createDynamoDBHttpRequest(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateSchedulePolicyWithNull() {
        DynamoDBUtils.createSchedulePolicy(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateDynamoDBSchedulePolicyWithNull() {
        DynamoDBUtils.createDynamoDBSchedulePolicy(null);
    }

    @Test
    public void testCreateDynamoDBPlanoRequestSuccess() {
        DynamoDBPlanoRequest dynamoDBPlanoRequest = DynamoDBTestUtils.createDynamoDBPlanoRequest();
        PlanoRequest planoRequest = DynamoDBUtils.createPlanoRequest(dynamoDBPlanoRequest);

        Assert.assertEquals(dynamoDBPlanoRequest, DynamoDBUtils.createDynamoDBPlanoRequest(planoRequest));
    }
}
