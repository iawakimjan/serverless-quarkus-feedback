package nl.p4c.feedback.api.model;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@RegisterForReflection
public class Answer {
    private String inviteCode;
    private int questionId;
    private String text;
}
