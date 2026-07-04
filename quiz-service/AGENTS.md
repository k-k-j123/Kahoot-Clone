# quiz-service

## Stack

- Spring Boot 4.0.7 / Java 21 / Maven wrapper (`./mvnw`)
- MySQL + JPA, Eureka client, Springdoc OpenAPI, Lombok
- `spring.jpa.hibernate.ddl-auto=create` — schema drops on every restart

## Build & test

```sh
./mvnw compile          # compile
./mvnw test             # run tests (single test: QuizServiceApplicationTests.contextLoads)
./mvnw test -Dtest=FooTest
```

## Architecture

- **No controllers exist** — API endpoints are implemented only as service methods returning `ResponseEntity`. Controllers must be created to expose them.
- Services return `ResponseEntity` from the service layer (existing convention, not ideal but consistent).
- All IDs are `Long`. Repos extend `JpaRepository<Entity, Long>`.

## Project layout

```
model/     Quiz, Question, Option — JPA entities, Lombok @Data/@NoArgsConstructor/@AllArgsConstructor
repository QuizRepo, QuestionRepo, OptionRepo — Spring Data JPA, add query methods here
service/   QuizService, QuestionService, OptionService — business logic returning ResponseEntity
dto/       QuizPlayDTO — no Lombok, explicit getters/setters, nested DTOs for play endpoint
```

## Key domain

```
Quiz (1) → (N) Question (1) → (N) Option
```

- All `@ManyToOne` are LAZY. No inverse `@OneToMany` mapped sides — query via `findByQuizId` / `findByQuestionId`.
- `Option` table is backtick-quoted: `@Table(name = "\`option\`")` because `option` is a reserved SQL word.

## Endpoints (service methods)

| Method | Service method | Notes |
|---|---|---|
| `POST /quizzes/{quizId}/questions` | `QuestionService.createQuestion(quizId, question)` | Auto-assigns questionOrder |
| `DELETE /questions/{id}` | `QuestionService.deleteQuestion(id)` | |
| `POST /questions/{questionId}/options` | `OptionService.createOption(questionId, option)` | Auto-assigns optionOrder |
| `GET /quizzes/{id}/play` | `QuizService.playQuiz(id)` | Returns QuizPlayDTO (no correct answers exposed) |
| `GET /questions/{id}/correct-answer` | `QuestionService.getCorrectAnswer(id)` | Returns the Option where isCorrect=true |

## Conventions

- Entities use Lombok (`@Data`, `@NoArgsConstructor`, `@AllArgsConstructor`). DTOs use explicit getters/setters (no Lombok).
- `ddl-auto=create` — any schema changes in entities are applied on restart, but existing data is lost.

## Notes

- `application.properties` has hardcoded MySQL credentials (`root`/`kkj123`) pointed at `localhost:3306/quiz_db`.
- MySQL must be running locally for the app to start.
- Eureka client is on classpath but no config — app expects a Eureka server unless `eureka.client.enabled=false`.
