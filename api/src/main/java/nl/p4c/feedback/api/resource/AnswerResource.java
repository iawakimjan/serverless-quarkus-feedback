package nl.p4c.feedback.api.resource;


import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import nl.p4c.feedback.api.form.AnswerForm;
import nl.p4c.feedback.api.model.Question;
import nl.p4c.feedback.api.service.AnswerService;
import nl.p4c.feedback.api.service.QuestionService;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

@Path("/invites/{inviteCode}/questions")
public class AnswerResource {

    @Inject
    QuestionService questionService;

    @Inject
    AnswerService answerService;

    @POST
    @Path("{questionId}/answer")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response answerQuestion(@PathParam("inviteCode") String inviteCode, @PathParam("questionId") int questionId,
                                   @MultipartForm AnswerForm answerForm) {
        Optional<Question> question = questionService.findQuestion(inviteCode, questionId);

        if (question.isEmpty()) {
            throw new RuntimeException();
        }

        String answer = "";
        switch (question.get().getType()) {
            case "TEXT":
                answer = answerForm.answerText;
                break;
            case "SCORE":
                answer = answerForm.score;
                break;
        }

        answerService.storeAnswer(inviteCode, questionId, answer);

        int nextQuestionId = questionId + 1;
        Optional<Question> nextQuestion = questionService.findQuestion(inviteCode, nextQuestionId);

        try {
            if (nextQuestion.isEmpty()) {
                return Response.seeOther(new URI("/invites/" + inviteCode + "/finish"))
                    .build();
            } else {
                return Response.seeOther(new URI("/invites/" + inviteCode + "/questions/" + nextQuestionId))
                    .build();
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException();
        }
    }
}
