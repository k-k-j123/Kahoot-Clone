package com.kaushik.quiz_service.controller;

import com.kaushik.quiz_service.service.OptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/option")
public class OptionController {

    private final OptionService optionService;

    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @PostMapping("/question/{questionId}")
    public ResponseEntity<?> createOption(@PathVariable long questionId, @RequestBody com.kaushik.quiz_service.model.Option option) {
        return optionService.createOption(questionId, option);
    }
}
