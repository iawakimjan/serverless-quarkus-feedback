package nl.p4c.feedback.api.service;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import nl.p4c.feedback.api.model.Answer;
import nl.p4c.feedback.api.repository.AnswerRepository;

@ApplicationScoped
public class AnswerService {

    @Inject
    AnswerRepository answerRepository;

    public Answer storeAnswer(String inviteCode, int questionId, String text) {
        Answer answer = Answer.builder()
            .inviteCode(inviteCode)
            .questionId(questionId)
            .text(text)
            .build();

        return answerRepository.putAnswer(answer);
    }
}
