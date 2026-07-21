package com.kaushik.quiz_service.dto;

import java.util.List;

public record QuizPlayDTO(
        Long id,
        String title,
        String description,
        List<QuestionPlayDTO> questions
) {
    public record QuestionPlayDTO(
            Long id,
            String questionText,
            Integer questionOrder,
            Integer timeLimitSeconds,
            Integer points,
            List<OptionPlayDTO> options
    ) {
    }

    public record OptionPlayDTO(
            Long id,
            String optionText,
            Integer optionOrder
    ) {
    }
}
