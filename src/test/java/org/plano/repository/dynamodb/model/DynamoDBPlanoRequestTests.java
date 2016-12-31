package org.plano.repository.dynamodb.model;

import org.junit.Assert;
import org.junit.Test;
import org.plano.data.HttpRequest;
import org.plano.data.PlanoRequest;
import org.plano.data.SchedulePolicy;
import org.plano.repository.dynamodb.model.DynamoDBHttpRequest;
import org.plano.repository.dynamodb.model.DynamoDBPlanoRequest;
import org.plano.repository.dynamodb.model.DynamoDBSchedulePolicy;

public class DynamoDBPlanoRequestTests {
    @Test
    public void testDynamoDBPlanoRequestShouldMatchPlanoRequest() {
        Assert.assertEquals(PlanoRequest.class.getDeclaredFields().length + 1,
                DynamoDBPlanoRequest.class.getDeclaredFields().length);
    }

    @Test
    public void testDynamoDBHttpRequestShouldMatchHttpRequest() {
        Assert.assertEquals(DynamoDBHttpRequest.class.getFields().length,
                HttpRequest.class.getFields().length);
    }

    @Test
    public void testDynamoDBSchedulePolicyShouldMatchSchedulePolicy() {
        Assert.assertEquals(DynamoDBSchedulePolicy.class.getFields().length,
                SchedulePolicy.class.getFields().length);
    }
}
