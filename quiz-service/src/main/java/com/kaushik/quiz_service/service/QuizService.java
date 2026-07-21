package com.kaushik.quiz_service.service;

import com.kaushik.quiz_service.dto.QuizPlayDTO;
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
public class QuizService {

    private final QuizRepo quizRepo;
    private final QuestionRepo questionRepo;
    private final OptionRepo optionRepo;

    public QuizService(QuizRepo quizRepo, QuestionRepo questionRepo, OptionRepo optionRepo) {
        this.quizRepo = quizRepo;
        this.questionRepo = questionRepo;
        this.optionRepo = optionRepo;
    }

    public ResponseEntity<Quiz> createQuiz(Quiz quiz) {
        quiz.setId(null);
        Quiz saved = quizRepo.save(quiz);
        return new ResponseEntity<>(saved, HttpStatus.OK);
    }

    public ResponseEntity<Quiz> getQuizDetails(Long id) {
        Quiz q = quizRepo.findById(id).orElse(new Quiz());
        return new ResponseEntity<>(q, HttpStatus.OK);
    }

    public ResponseEntity<List<Quiz>> getAllQuizes() {
        List<Quiz> allquizes = quizRepo.findAll();
        return new ResponseEntity<>(allquizes, HttpStatus.OK);
    }

    public ResponseEntity<?> deleteQuiz(Long id) {
        quizRepo.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<QuizPlayDTO> playQuiz(Long id) {
        Optional<Quiz> quizOpt = quizRepo.findById(id);
        if (quizOpt.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Quiz quiz = quizOpt.get();
        List<QuizPlayDTO.QuestionPlayDTO> questions = questionRepo.findByQuizId(id).stream()
                .map(q -> {
                    List<QuizPlayDTO.OptionPlayDTO> options = optionRepo.findByQuestionId(q.getId()).stream()
                            .map(o -> new QuizPlayDTO.OptionPlayDTO(o.getId(), o.getOptionText(), o.getOptionOrder()))
                            .toList();
                    return new QuizPlayDTO.QuestionPlayDTO(
                            q.getId(), q.getQuestionText(), q.getQuestionOrder(),
                            q.getTimeLimitSeconds(), q.getPoints(), options);
                })
                .toList();

        QuizPlayDTO playDTO = new QuizPlayDTO(quiz.getId(), quiz.getTitle(), quiz.getDescription(), questions);
        return new ResponseEntity<>(playDTO, HttpStatus.OK);
    }
}
