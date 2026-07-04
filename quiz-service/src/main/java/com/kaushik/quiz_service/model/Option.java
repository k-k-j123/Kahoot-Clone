package com.kaushik.quiz_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "`option`")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(name = "option_text", nullable = false, length = 500)
    private String optionText;

    @Column(name = "is_correct", nullable = false)
    private Boolean isCorrect;

    @Column(name = "option_order", nullable = false)
    private Integer optionOrder;
}
