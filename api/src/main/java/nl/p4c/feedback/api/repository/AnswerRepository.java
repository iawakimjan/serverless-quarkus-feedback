package nl.p4c.feedback.api.repository;

import java.util.HashMap;
import java.util.Map;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import nl.p4c.feedback.api.model.Answer;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

@ApplicationScoped
public class AnswerRepository extends AbstractRepository {

    @Inject
    DynamoDbClient dynamoDB;

    protected PutItemRequest putRequest(Answer answer) {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put(PK_COL, AttributeValue.builder().s(INVITE_PREFIX + answer.getInviteCode()).build());
        item.put(SK_COL, AttributeValue.builder().s(ANSWER_PREFIX + QUESTION_PREFIX + answer.getQuestionId()).build());
        item.put("Text", AttributeValue.builder().s(answer.getText()).build());

        return PutItemRequest.builder()
            .tableName(getTableName())
            .item(item)
            .build();
    }

    public Answer putAnswer(Answer answer) {
        var putItemRequest = this.putRequest(answer);

        this.dynamoDB.putItem(putItemRequest);

        return answer;
    }
}
