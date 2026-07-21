package com.kaushik.quiz_service.service;

import com.kaushik.quiz_service.model.Option;
import com.kaushik.quiz_service.model.Question;
import com.kaushik.quiz_service.repository.OptionRepo;
import com.kaushik.quiz_service.repository.QuestionRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OptionService {

    private final OptionRepo optionRepo;
    private final QuestionRepo questionRepo;

    public OptionService(OptionRepo optionRepo, QuestionRepo questionRepo) {
        this.optionRepo = optionRepo;
        this.questionRepo = questionRepo;
    }

    public ResponseEntity<Option> createOption(Long questionId, Option option) {
        Optional<Question> questionOpt = questionRepo.findById(questionId);
        if (questionOpt.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        option.setId(null);
        option.setQuestion(questionOpt.get());

        if (option.getOptionOrder() == null) {
            List<Option> existing = optionRepo.findByQuestionId(questionId);
            option.setOptionOrder(existing.size() + 1);
        }

        Option saved = optionRepo.save(option);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }
}
