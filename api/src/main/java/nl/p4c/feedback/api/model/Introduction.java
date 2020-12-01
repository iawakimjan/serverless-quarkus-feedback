package nl.p4c.feedback.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Introduction {

    private String name;
    private String text;
}
