package nl.p4c.feedback.api.repository;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import nl.p4c.feedback.api.model.Question;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

@ApplicationScoped
public class QuestionRepository extends AbstractRepository {

    @Inject
    DynamoDbClient dynamoDB;

    public List<Question> getQuestions(String inviteCode) {
        var queryRequest = this.getQuestionsQueryRequest(inviteCode);
        var response = this.dynamoDB.query(queryRequest);

        var items = response.items();

        return items.stream()
            .map(this::mapToQuestion)
            .collect(toList());
    }

    public Optional<Question> getQuestion(String inviteCode, int questionId) {
        var itemRequest = this.getItemRequest(INVITE_PREFIX + inviteCode, QUESTION_PREFIX + questionId);

        var item = this.dynamoDB.getItem(itemRequest).item();

        if (item == null || item.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(mapToQuestion(item));
    }

    private Question mapToQuestion(Map<String, AttributeValue> item) {
        int id = Integer.parseInt(item.get(SK_COL).s().split("#")[1]);

        return Question.builder()
            .inviteCode(item.get(PK_COL).s().split("#")[1])
            .id(id)
            .title(item.get("Title").s())
            .text(item.get("Text").s())
            .type(item.get("Type").s())
            .previous(Math.max(0, id - 1))
            .next(id + 1)
            .build();
    }
}
