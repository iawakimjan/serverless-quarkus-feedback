package nl.p4c.feedback.api.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import nl.p4c.feedback.api.model.Invite;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeAction;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.AttributeValueUpdate;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;

@ApplicationScoped
public class InviteRepository extends AbstractRepository {

    @Inject
    DynamoDbClient dynamoDB;

    public Optional<Invite> getInvite(String inviteCode) {
        var getItemRequest = this.getItemRequest(INVITE_PREFIX + inviteCode, INVITE_PREFIX + inviteCode);
        var item = dynamoDB.getItem(getItemRequest).item();

        if (item == null || item.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(mapToInvite(item));
    }

    public void closeInvite(String inviteCode) {
        var request = this.updateInviteStatus(inviteCode, "CLOSED");
        this.dynamoDB.updateItem(request);
    }

    protected UpdateItemRequest updateInviteStatus(String inviteCode, String status) {
        Map<String, AttributeValue> keyAttributeValues = new HashMap<>();
        keyAttributeValues.put(PK_COL, AttributeValue.builder().s(INVITE_PREFIX + inviteCode).build());
        keyAttributeValues.put(SK_COL, AttributeValue.builder().s(INVITE_PREFIX + inviteCode).build());

        Map<String, AttributeValueUpdate> updates = new HashMap<>();
        updates.put("Status", AttributeValueUpdate.builder().action(AttributeAction.PUT).value(AttributeValue.builder().s(status).build()).build());

        return UpdateItemRequest.builder()
            .tableName(getTableName())
            .key(keyAttributeValues)
            .attributeUpdates(updates)
            .build();
    }

    private Invite mapToInvite(Map<String, AttributeValue> item) {
        return Invite.builder()
            .code(item.get(PK_COL).s().split("#")[1])
            .email(item.get("Email").s())
            .introduction(item.get("Introduction").s())
            .name(item.get("Name").s())
            .status(item.get("Status").s())
            .build();
    }
}
