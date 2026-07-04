package com.kaushik.quiz_service.service;

import com.kaushik.quiz_service.model.Option;
import com.kaushik.quiz_service.model.Question;
import com.kaushik.quiz_service.model.Quiz;
import com.kaushik.quiz_service.repository.OptionRepo;
import com.kaushik.quiz_service.repository.QuestionRepo;
import com.kaushik.quiz_service.repository.QuizRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {

    private final QuestionRepo questionRepo;
    private final QuizRepo quizRepo;
    private final OptionRepo optionRepo;

    public QuestionService(QuestionRepo questionRepo, QuizRepo quizRepo, OptionRepo optionRepo) {
        this.questionRepo = questionRepo;
        this.quizRepo = quizRepo;
        this.optionRepo = optionRepo;
    }

    public ResponseEntity<Question> createQuestion(Long quizId, Question question) {
        Optional<Quiz> quizOpt = quizRepo.findById(quizId);
        if (quizOpt.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        question.setQuiz(quizOpt.get());

        if (question.getQuestionOrder() == null) {
            List<Question> existing = questionRepo.findByQuizId(quizId);
            question.setQuestionOrder(existing.size() + 1);
        }

        Question saved = questionRepo.save(question);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    public ResponseEntity<Void> deleteQuestion(Long id) {
        Optional<Question> questionOpt = questionRepo.findById(id);
        if (questionOpt.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        questionRepo.delete(questionOpt.get());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    public ResponseEntity<Option> getCorrectAnswer(Long id) {
        Optional<Question> questionOpt = questionRepo.findById(id);
        if (questionOpt.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Optional<Option> correctOpt = optionRepo.findByQuestionIdAndIsCorrect(id, true);
        if (correctOpt.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(correctOpt.get(), HttpStatus.OK);
    }
}
