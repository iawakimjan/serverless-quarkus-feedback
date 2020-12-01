package nl.p4c.feedback.api.service;

import java.util.List;
import java.util.Optional;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import nl.p4c.feedback.api.model.Question;
import nl.p4c.feedback.api.repository.QuestionRepository;

@ApplicationScoped
public class QuestionService {

    @Inject
    QuestionRepository questionRepository;

    public Optional<Question> findQuestion(String inviteCode, int questionId) {
        return questionRepository.getQuestion(inviteCode, questionId);
    }

    public List<Question> findQuestions(String inviteCode) {
        return questionRepository.getQuestions(inviteCode);
    }
}
