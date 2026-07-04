package com.kaushik.quiz_service.repository;

import com.kaushik.quiz_service.model.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OptionRepo extends JpaRepository<Option, Long> {

    List<Option> findByQuestionId(Long questionId);

    Optional<Option> findByQuestionIdAndIsCorrect(Long questionId, Boolean isCorrect);
}
