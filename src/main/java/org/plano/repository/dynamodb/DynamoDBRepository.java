package org.plano.repository.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.amazonaws.services.dynamodbv2.datamodeling.marshallers.DateToStringMarshaller;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import org.plano.data.PlanoRequest;
import org.plano.exception.InvalidRequestException;
import org.plano.exception.PlanoException;
import org.plano.exception.ResourceNotFoundException;
import org.plano.repository.Repository;
import org.plano.repository.dynamodb.model.DynamoDBPlanoRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * This class provides plano methods to interact with repository backed
 * by DynamoDB.
 */
@Component
public class DynamoDBRepository implements Repository<PlanoRequest> {
    private static final Logger LOG = LoggerFactory.getLogger(DynamoDBRepository.class);

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    @Value("${dynamodb.lock.duration.ms}")
    private Integer lockDurationMs;

    /**
     * Get {@link PlanoRequest} with RequestID.
     * @param requestID {@link String} RequestID.
     * @return {@link PlanoRequest}
     * @throws ResourceNotFoundException if resource not found in repository.
     */
    @Override
    public PlanoRequest getRequest(String requestID) throws ResourceNotFoundException {
        DynamoDBPlanoRequest dynamoDBPlanoRequest = dynamoDBMapper.load(DynamoDBPlanoRequest.class, requestID);
        if (dynamoDBPlanoRequest == null) {
            throw new ResourceNotFoundException("Can not find PlanoRequest with RequestID: " + requestID);
        }
        PlanoRequest planoRequest = DynamoDBUtils.createPlanoRequest(dynamoDBPlanoRequest);

        return planoRequest;
    }

    /**
     * Save {@link PlanoRequest} to DynamoDBRepository
     * @param planoRequest {@link PlanoRequest}
     * @return {@link String}
     * @throws InvalidRequestException if {@link PlanoRequest} is invalid.
     */
    @Override
    public String addRequest(PlanoRequest planoRequest) throws InvalidRequestException {
        String requestID;
        try {
            DynamoDBPlanoRequest dynamoDBPlanoRequest = DynamoDBUtils.createDynamoDBPlanoRequest(planoRequest);
            dynamoDBPlanoRequest.setLockExpireTime(new Date());
            dynamoDBMapper.save(dynamoDBPlanoRequest);
            requestID = dynamoDBPlanoRequest.getRequestID();
        } catch (IllegalArgumentException e) {
            throw new InvalidRequestException(e);
        }

        return requestID;
    }

    /**
     * Update the {@link PlanoRequest} to {@link DynamoDBRepository}.
     * @param planoRequest {@link PlanoRequest}
     * @throws InvalidRequestException if {@link PlanoRequest} is invalid.
     */
    @Override
    public void updateRequest(PlanoRequest planoRequest) throws InvalidRequestException {
        try {
            DynamoDBPlanoRequest dynamoDBPlanoRequest = DynamoDBUtils.createDynamoDBPlanoRequest(planoRequest);
            dynamoDBPlanoRequest.setLockExpireTime(new Date());
            dynamoDBMapper.save(dynamoDBPlanoRequest);
        } catch (IllegalArgumentException e) {
            throw new InvalidRequestException(e);
        }
    }

    /**
     * Remove the {@link PlanoRequest} from {@link DynamoDBRepository}.
     * @param requestID T Request.
     * @throws InvalidRequestException if {@link PlanoRequest} does not exist.
     */
    @Override
    public void removeRequest(String requestID) throws ResourceNotFoundException {
        DynamoDBPlanoRequest dynamoDBPlanoRequest = dynamoDBMapper.load(DynamoDBPlanoRequest.class, requestID);
        if (dynamoDBPlanoRequest == null) {
            String message = String.format("PlanoRequest with RequestID: {} does not exist", requestID);
            throw new ResourceNotFoundException(message);
        }
        dynamoDBMapper.delete(dynamoDBPlanoRequest);
    }

    /**
     * Find the next request to execute and lock.
     * @return {@link PlanoRequest}, null if can not find request.
     */
    @Override
    public PlanoRequest findNextRequestAndLock() {
        PlanoRequest planoRequest = null;
        try {
            DynamoDBPlanoRequest dynamoDBPlanoRequest = findNextRequest();
            lockRequest(dynamoDBPlanoRequest);
            planoRequest = DynamoDBUtils.createPlanoRequest(dynamoDBPlanoRequest);
        } catch (PlanoException e) {
            LOG.debug(e.getMessage());
        }

        return planoRequest;
    }

    /**
     * Update the {@link PlanoRequest} to {@link DynamoDBRepository} and unlock.
     * @param planoRequest {@link PlanoRequest}
     * @throws InvalidRequestException if the {@link PlanoRequest} is invalid.
     */
    @Override
    public void updateRequestAndUnlock(PlanoRequest planoRequest) throws InvalidRequestException {
        updateRequest(planoRequest);
    }

    public void setLockDurationMs(Integer lockDurationMs) {
        this.lockDurationMs = lockDurationMs;
    }

    public void setDynamoDBMapper(DynamoDBMapper dynamoDBMapper) {
        this.dynamoDBMapper = dynamoDBMapper;
    }

    private DynamoDBPlanoRequest findNextRequest() throws PlanoException {
        AttributeValue currentTime = DateToStringMarshaller.instance().marshall(new Date());
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("ExecutionTime < :currentTime and LockExpireTime < :currentTime")
                .addExpressionAttributeValuesEntry(":currentTime", currentTime)
                .withConsistentRead(true)
                .withLimit(1);

        List<DynamoDBPlanoRequest> dynamoDBPlanoRequests =
                dynamoDBMapper.scan(DynamoDBPlanoRequest.class, scanExpression);

        if (dynamoDBPlanoRequests == null || dynamoDBPlanoRequests.isEmpty()) {
            throw new PlanoException("Can not find next PlanoRequest to execute.");
        }

        DynamoDBPlanoRequest dynamoDBPlanoRequest = dynamoDBPlanoRequests.get(0);

        return dynamoDBPlanoRequest;
    }

    private void lockRequest(DynamoDBPlanoRequest dynamoDBPlanoRequest) {
        AttributeValue previousLockExpireTime = DateToStringMarshaller.instance().marshall(
                dynamoDBPlanoRequest.getLockExpireTime());

        Date lockExpireTime = new Date(System.currentTimeMillis() + lockDurationMs);
        dynamoDBPlanoRequest.setLockExpireTime(lockExpireTime);

        DynamoDBSaveExpression saveExpression = new DynamoDBSaveExpression()
                .withExpectedEntry("LockExpireTime",
                        new ExpectedAttributeValue()
                                .withComparisonOperator(ComparisonOperator.EQ)
                                .withValue(previousLockExpireTime));

        dynamoDBMapper.save(dynamoDBPlanoRequest, saveExpression);
    }
}
