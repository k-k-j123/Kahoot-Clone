package com.kaushik.quiz_service.controller;

import com.kaushik.quiz_service.service.QuizService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quiz")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService){
        this.quizService=quizService;
    }

    @PostMapping
    public ResponseEntity<?> createQuiz(@RequestBody com.kaushik.quiz_service.model.Quiz quiz){
        return quizService.createQuiz(quiz);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getQuizDetails(@PathVariable long id){
        return quizService.getQuizDetails(id);
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllQuiz(){
        return quizService.getAllQuizes();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteQuiz(@PathVariable long id){
        return quizService.deleteQuiz(id);
    }

    @GetMapping("/play/{id}")
    public ResponseEntity<?> playQuiz(@PathVariable long id){
        return quizService.playQuiz(id);
    }

}
