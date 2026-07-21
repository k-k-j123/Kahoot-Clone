package com.kaushik.quiz_service.controller;

import com.kaushik.quiz_service.service.QuestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/question")
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService){
        this.questionService = questionService;
    }

    @PostMapping("/quiz/{id}")
    public ResponseEntity<?> createQuestion(@PathVariable long id, @RequestBody com.kaushik.quiz_service.model.Question question){
        return questionService.createQuestion(id, question);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteQuestion(@PathVariable long id) {
        return questionService.deleteQuestion(id);
    }

    @GetMapping("/ans/{id}")
    public ResponseEntity<?> getCorrectAnswer(@PathVariable long id) {
        return questionService.getCorrectAnswer(id);
    }

}
