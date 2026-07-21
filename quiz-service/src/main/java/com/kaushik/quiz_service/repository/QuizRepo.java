package com.kaushik.quiz_service.repository;

import com.kaushik.quiz_service.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuizRepo extends JpaRepository<Quiz, Long> {
}
