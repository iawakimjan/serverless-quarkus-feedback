package nl.p4c.feedback.api.model;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@RegisterForReflection
public class Question {
    private String inviteCode;
    private int id;
    private String title;
    private String text;
    private String type;

    transient private Integer next;
    transient private Integer previous;
}
