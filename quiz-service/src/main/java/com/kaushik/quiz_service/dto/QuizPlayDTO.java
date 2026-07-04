package com.kaushik.quiz_service.dto;

import java.util.ArrayList;
import java.util.List;

public class QuizPlayDTO {

    private Long id;
    private String title;
    private String description;
    private List<QuestionPlayDTO> questions = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<QuestionPlayDTO> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionPlayDTO> questions) {
        this.questions = questions;
    }

    public static class QuestionPlayDTO {

        private Long id;
        private String questionText;
        private Integer questionOrder;
        private Integer timeLimitSeconds;
        private Integer points;
        private List<OptionPlayDTO> options = new ArrayList<>();

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getQuestionText() {
            return questionText;
        }

        public void setQuestionText(String questionText) {
            this.questionText = questionText;
        }

        public Integer getQuestionOrder() {
            return questionOrder;
        }

        public void setQuestionOrder(Integer questionOrder) {
            this.questionOrder = questionOrder;
        }

        public Integer getTimeLimitSeconds() {
            return timeLimitSeconds;
        }

        public void setTimeLimitSeconds(Integer timeLimitSeconds) {
            this.timeLimitSeconds = timeLimitSeconds;
        }

        public Integer getPoints() {
            return points;
        }

        public void setPoints(Integer points) {
            this.points = points;
        }

        public List<OptionPlayDTO> getOptions() {
            return options;
        }

        public void setOptions(List<OptionPlayDTO> options) {
            this.options = options;
        }
    }

    public static class OptionPlayDTO {

        private Long id;
        private String optionText;
        private Integer optionOrder;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getOptionText() {
            return optionText;
        }

        public void setOptionText(String optionText) {
            this.optionText = optionText;
        }

        public Integer getOptionOrder() {
            return optionOrder;
        }

        public void setOptionOrder(Integer optionOrder) {
            this.optionOrder = optionOrder;
        }
    }
}
