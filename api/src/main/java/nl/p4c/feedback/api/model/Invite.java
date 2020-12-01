package nl.p4c.feedback.api.model;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@RegisterForReflection
public class Invite {
    private String code;
    private String name;
    private String email;
    private String introduction;
    private String status;
}
