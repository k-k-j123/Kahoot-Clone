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

import java.util.ArrayList;
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
        Quiz q = new Quiz();
        try {
            quiz.setId(null);
            quizRepo.save(quiz);
            q.setTitle(quiz.getTitle());
            q.setDescription(quiz.getDescription());
        } catch (Exception e) {
            System.out.println("exception occurred in creating quiz :");
            e.printStackTrace();
        }
        return new ResponseEntity<>(q, HttpStatus.OK);
    }

    public ResponseEntity<Quiz> getQuizDetails(Long id) {
        Quiz q = new Quiz();
        try {
            Optional<Quiz> opt = quizRepo.findById(id);
            if (opt.isPresent()) {
                q = opt.get();
            }
        } catch (Exception e) {
            System.out.println("exception occurred in getting quiz details:");
            e.printStackTrace();
        }
        return new ResponseEntity<>(q, HttpStatus.OK);
    }

    public ResponseEntity<List<Quiz>> getAllQuizes() {
        List<Quiz> allquizes = new ArrayList<>();
        try {
            allquizes = quizRepo.findAll();
        } catch (Exception e) {
            System.out.println("exception occurred in getting all quiz details:");
            e.printStackTrace();
        }
        return new ResponseEntity<>(allquizes, HttpStatus.OK);
    }

    public ResponseEntity<?> deleteQuiz(Long id) {
        try {
            quizRepo.deleteById(id);
        } catch (Exception e) {
            System.out.println("exception occurred in deleting the quiz:");
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<QuizPlayDTO> playQuiz(Long id) {
        Optional<Quiz> quizOpt = quizRepo.findById(id);
        if (quizOpt.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Quiz quiz = quizOpt.get();
        QuizPlayDTO playDTO = new QuizPlayDTO();
        playDTO.setId(quiz.getId());
        playDTO.setTitle(quiz.getTitle());
        playDTO.setDescription(quiz.getDescription());

        List<Question> questions = questionRepo.findByQuizId(id);
        for (Question question : questions) {
            QuizPlayDTO.QuestionPlayDTO qDTO = new QuizPlayDTO.QuestionPlayDTO();
            qDTO.setId(question.getId());
            qDTO.setQuestionText(question.getQuestionText());
            qDTO.setQuestionOrder(question.getQuestionOrder());
            qDTO.setTimeLimitSeconds(question.getTimeLimitSeconds());
            qDTO.setPoints(question.getPoints());

            List<Option> options = optionRepo.findByQuestionId(question.getId());
            for (Option option : options) {
                QuizPlayDTO.OptionPlayDTO oDTO = new QuizPlayDTO.OptionPlayDTO();
                oDTO.setId(option.getId());
                oDTO.setOptionText(option.getOptionText());
                oDTO.setOptionOrder(option.getOptionOrder());
                qDTO.getOptions().add(oDTO);
            }

            playDTO.getQuestions().add(qDTO);
        }

        return new ResponseEntity<>(playDTO, HttpStatus.OK);
    }
}
