package nl.p4c.feedback.api.form;

import javax.ws.rs.FormParam;

public class AnswerForm {

    public @FormParam("answerText") String answerText;
    public @FormParam("score") String score;
}
