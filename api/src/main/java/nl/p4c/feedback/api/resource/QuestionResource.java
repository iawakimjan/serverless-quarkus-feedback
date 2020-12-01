package nl.p4c.feedback.api.resource;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import java.util.Optional;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import nl.p4c.feedback.api.model.Question;
import nl.p4c.feedback.api.service.QuestionService;

@Path("/invites/{inviteCode}/questions")
public class QuestionResource {

    @Inject
    Template questionTemplate;

    @Inject
    QuestionService questionService;

    @GET
    @Path("/{questionId}")
    public TemplateInstance getQuestion(@PathParam("inviteCode") String inviteCode, @PathParam("questionId") int questionId) {
        Optional<Question> question = questionService.findQuestion(inviteCode, questionId);

        if (question.isEmpty()) {
            throw new RuntimeException();
        }

        return questionTemplate.data("question", question.get());
    }
}
