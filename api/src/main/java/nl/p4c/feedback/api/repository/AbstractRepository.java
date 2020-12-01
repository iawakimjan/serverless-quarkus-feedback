package nl.p4c.feedback.api.repository;

import java.util.HashMap;
import java.util.Map;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;

public abstract class AbstractRepository {
    public final static String PK_COL = "PK";
    public final static String SK_COL = "SK";

    final static String INVITE_PREFIX = "INVITE#";
    final static String QUESTION_PREFIX = "QUESTION#";
    final static String ANSWER_PREFIX = "ANSWER#";

    public String getTableName() {
        return "Questionnaires";
    }

    protected GetItemRequest getItemRequest(String primaryKey, String sortKey) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put(PK_COL, AttributeValue.builder().s(primaryKey).build());
        key.put(SK_COL, AttributeValue.builder().s(sortKey).build());

        return GetItemRequest.builder()
            .tableName(getTableName())
            .key(key)
            .build();
    }

    protected QueryRequest getQueryRequest(String expression, String primaryKey, String sortKey) {
        Map<String, String> expressionAttributeNames = new HashMap<>();
        expressionAttributeNames.put("#pk", PK_COL);
        expressionAttributeNames.put("#sk", SK_COL);

        Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
        expressionAttributeValues.put(":pkValue", AttributeValue.builder().s(primaryKey).build());
        expressionAttributeValues.put(":skValue", AttributeValue.builder().s(sortKey).build());

        return QueryRequest.builder()
            .tableName(getTableName())
            .keyConditionExpression(expression)
            .expressionAttributeNames(expressionAttributeNames)
            .expressionAttributeValues(expressionAttributeValues)
            .build();
    }

    protected QueryRequest getQuestionsQueryRequest(String inviteCode) {
        String primaryKey = INVITE_PREFIX + inviteCode;
        return this.getQueryRequest("#pk = :pkValue and #sk BEGINS_WITH :skValue", primaryKey, QUESTION_PREFIX);
    }
}
