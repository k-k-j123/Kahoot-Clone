#!/usr/bin/env bash
set -euo pipefail

BASE_URL="http://localhost:8080/quiz-service/api"

post() {
  curl -s -X POST -H "Content-Type: application/json" -d "$2" "$1"
}

echo "=== Seeding Quiz Data ==="

# ── Wait for quiz-service to be reachable via the gateway ──
# quiz-service registers with Eureka after startup; the gateway's registry can
# lag behind, causing 503 "Unable to find instance for quiz-service". Poll until
# the gateway route resolves before seeding, so extracted ids are never null.
echo "Waiting for quiz-service to be reachable via gateway..."
ready=0
for i in $(seq 1 60); do
  code=$(curl -s -o /dev/null -w "%{http_code}" "$BASE_URL/quiz/getAll")
  if [ "$code" = "200" ]; then
    ready=1
    break
  fi
  sleep 1
done
if [ "$ready" -ne 1 ]; then
  echo "ERROR: quiz-service never became reachable via the gateway (last HTTP $code)." >&2
  echo "Is service-registry up and quiz-service registered with Eureka?" >&2
  exit 1
fi

# ── Quiz 1: General Knowledge Trivia ──
echo "[1/5] Creating quiz: General Knowledge Trivia"
QUIZ1=$(post "$BASE_URL/quiz" '{"title":"General Knowledge Trivia","description":"Test your knowledge across science, history, geography, and pop culture."}')
QID1=$(echo "$QUIZ1" | jq -r '.id')
echo "  quiz id=$QID1"

echo "  adding questions..."
Q1_1=$(post "$BASE_URL/question/quiz/$QID1" '{"questionText":"What is the chemical symbol for gold?","timeLimitSeconds":20,"points":1000}')
Q1_1_ID=$(echo "$Q1_1" | jq -r '.id')
post "$BASE_URL/option/question/$Q1_1_ID" '{"optionText":"Go","isCorrect":false}'
post "$BASE_URL/option/question/$Q1_1_ID" '{"optionText":"Au","isCorrect":true}'
post "$BASE_URL/option/question/$Q1_1_ID" '{"optionText":"Gd","isCorrect":false}'
post "$BASE_URL/option/question/$Q1_1_ID" '{"optionText":"Ag","isCorrect":false}'

Q1_2=$(post "$BASE_URL/question/quiz/$QID1" '{"questionText":"In which year did the Titanic sink?","timeLimitSeconds":20,"points":1000}')
Q1_2_ID=$(echo "$Q1_2" | jq -r '.id')
post "$BASE_URL/option/question/$Q1_2_ID" '{"optionText":"1905","isCorrect":false}'
post "$BASE_URL/option/question/$Q1_2_ID" '{"optionText":"1912","isCorrect":true}'
post "$BASE_URL/option/question/$Q1_2_ID" '{"optionText":"1920","isCorrect":false}'
post "$BASE_URL/option/question/$Q1_2_ID" '{"optionText":"1898","isCorrect":false}'

Q1_3=$(post "$BASE_URL/question/quiz/$QID1" '{"questionText":"What is the largest ocean on Earth?","timeLimitSeconds":15,"points":800}')
Q1_3_ID=$(echo "$Q1_3" | jq -r '.id')
post "$BASE_URL/option/question/$Q1_3_ID" '{"optionText":"Atlantic Ocean","isCorrect":false}'
post "$BASE_URL/option/question/$Q1_3_ID" '{"optionText":"Indian Ocean","isCorrect":false}'
post "$BASE_URL/option/question/$Q1_3_ID" '{"optionText":"Pacific Ocean","isCorrect":true}'
post "$BASE_URL/option/question/$Q1_3_ID" '{"optionText":"Arctic Ocean","isCorrect":false}'

Q1_4=$(post "$BASE_URL/question/quiz/$QID1" '{"questionText":"Which planet is known as the Red Planet?","timeLimitSeconds":15,"points":800}')
Q1_4_ID=$(echo "$Q1_4" | jq -r '.id')
post "$BASE_URL/option/question/$Q1_4_ID" '{"optionText":"Venus","isCorrect":false}'
post "$BASE_URL/option/question/$Q1_4_ID" '{"optionText":"Jupiter","isCorrect":false}'
post "$BASE_URL/option/question/$Q1_4_ID" '{"optionText":"Mars","isCorrect":true}'
post "$BASE_URL/option/question/$Q1_4_ID" '{"optionText":"Saturn","isCorrect":false}'

# ── Quiz 2: JavaScript Fundamentals ──
echo "[2/5] Creating quiz: JavaScript Fundamentals"
QUIZ2=$(post "$BASE_URL/quiz" '{"title":"JavaScript Fundamentals","description":"How well do you know the core concepts of JavaScript?"}')
QID2=$(echo "$QUIZ2" | jq -r '.id')
echo "  quiz id=$QID2"

echo "  adding questions..."
Q2_1=$(post "$BASE_URL/question/quiz/$QID2" '{"questionText":"What does the typeof null operator return in JavaScript?","timeLimitSeconds":20,"points":1200}')
Q2_1_ID=$(echo "$Q2_1" | jq -r '.id')
post "$BASE_URL/option/question/$Q2_1_ID" '{"optionText":"\"null\"","isCorrect":false}'
post "$BASE_URL/option/question/$Q2_1_ID" '{"optionText":"\"undefined\"","isCorrect":false}'
post "$BASE_URL/option/question/$Q2_1_ID" '{"optionText":"\"object\"","isCorrect":true}'
post "$BASE_URL/option/question/$Q2_1_ID" '{"optionText":"\"boolean\"","isCorrect":false}'

Q2_2=$(post "$BASE_URL/question/quiz/$QID2" '{"questionText":"Which keyword creates a variable that cannot be reassigned?","timeLimitSeconds":15,"points":1000}')
Q2_2_ID=$(echo "$Q2_2" | jq -r '.id')
post "$BASE_URL/option/question/$Q2_2_ID" '{"optionText":"var","isCorrect":false}'
post "$BASE_URL/option/question/$Q2_2_ID" '{"optionText":"let","isCorrect":false}'
post "$BASE_URL/option/question/$Q2_2_ID" '{"optionText":"const","isCorrect":true}'
post "$BASE_URL/option/question/$Q2_2_ID" '{"optionText":"static","isCorrect":false}'

Q2_3=$(post "$BASE_URL/question/quiz/$QID2" '{"questionText":"What is a closure in JavaScript?","timeLimitSeconds":30,"points":1500}')
Q2_3_ID=$(echo "$Q2_3" | jq -r '.id')
post "$BASE_URL/option/question/$Q2_3_ID" '{"optionText":"A way to close browser windows programmatically","isCorrect":false}'
post "$BASE_URL/option/question/$Q2_3_ID" '{"optionText":"A function that has access to its outer scope variables even after the outer function has returned","isCorrect":true}'
post "$BASE_URL/option/question/$Q2_3_ID" '{"optionText":"A method to end a for loop","isCorrect":false}'
post "$BASE_URL/option/question/$Q2_3_ID" '{"optionText":"A built-in method to destroy objects","isCorrect":false}'

Q2_4=$(post "$BASE_URL/question/quiz/$QID2" '{"questionText":"Which array method executes a function on each element and returns a new array?","timeLimitSeconds":20,"points":1000}')
Q2_4_ID=$(echo "$Q2_4" | jq -r '.id')
post "$BASE_URL/option/question/$Q2_4_ID" '{"optionText":"forEach()","isCorrect":false}'
post "$BASE_URL/option/question/$Q2_4_ID" '{"optionText":"map()","isCorrect":true}'
post "$BASE_URL/option/question/$Q2_4_ID" '{"optionText":"reduce()","isCorrect":false}'
post "$BASE_URL/option/question/$Q2_4_ID" '{"optionText":"filter()","isCorrect":false}'

# ── Quiz 3: Space Exploration ──
echo "[3/5] Creating quiz: Space Exploration"
QUIZ3=$(post "$BASE_URL/quiz" '{"title":"Space Exploration","description":"From the Moon landing to Mars rovers — how much do you know about space?"}')
QID3=$(echo "$QUIZ3" | jq -r '.id')
echo "  quiz id=$QID3"

echo "  adding questions..."
Q3_1=$(post "$BASE_URL/question/quiz/$QID3" '{"questionText":"Who was the first human to walk on the Moon?","timeLimitSeconds":15,"points":1000}')
Q3_1_ID=$(echo "$Q3_1" | jq -r '.id')
post "$BASE_URL/option/question/$Q3_1_ID" '{"optionText":"Buzz Aldrin","isCorrect":false}'
post "$BASE_URL/option/question/$Q3_1_ID" '{"optionText":"Neil Armstrong","isCorrect":true}'
post "$BASE_URL/option/question/$Q3_1_ID" '{"optionText":"Yuri Gagarin","isCorrect":false}'
post "$BASE_URL/option/question/$Q3_1_ID" '{"optionText":"Michael Collins","isCorrect":false}'

Q3_2=$(post "$BASE_URL/question/quiz/$QID3" '{"questionText":"What is the name of the space telescope launched in 1990 that is still operational?","timeLimitSeconds":20,"points":1200}')
Q3_2_ID=$(echo "$Q3_2" | jq -r '.id')
post "$BASE_URL/option/question/$Q3_2_ID" '{"optionText":"Kepler","isCorrect":false}'
post "$BASE_URL/option/question/$Q3_2_ID" '{"optionText":"Chandra","isCorrect":false}'
post "$BASE_URL/option/question/$Q3_2_ID" '{"optionText":"James Webb","isCorrect":false}'
post "$BASE_URL/option/question/$Q3_2_ID" '{"optionText":"Hubble","isCorrect":true}'

Q3_3=$(post "$BASE_URL/question/quiz/$QID3" '{"questionText":"Which planet has the most moons in our solar system?","timeLimitSeconds":25,"points":1200}')
Q3_3_ID=$(echo "$Q3_3" | jq -r '.id')
post "$BASE_URL/option/question/$Q3_3_ID" '{"optionText":"Jupiter","isCorrect":false}'
post "$BASE_URL/option/question/$Q3_3_ID" '{"optionText":"Saturn","isCorrect":true}'
post "$BASE_URL/option/question/$Q3_3_ID" '{"optionText":"Uranus","isCorrect":false}'
post "$BASE_URL/option/question/$Q3_3_ID" '{"optionText":"Neptune","isCorrect":false}'

Q3_4=$(post "$BASE_URL/question/quiz/$QID3" '{"questionText":"What does NASA stand for?","timeLimitSeconds":15,"points":800}')
Q3_4_ID=$(echo "$Q3_4" | jq -r '.id')
post "$BASE_URL/option/question/$Q3_4_ID" '{"optionText":"National Aeronautics and Space Administration","isCorrect":true}'
post "$BASE_URL/option/question/$Q3_4_ID" '{"optionText":"National Air and Space Agency","isCorrect":false}'
post "$BASE_URL/option/question/$Q3_4_ID" '{"optionText":"North American Space Association","isCorrect":false}'
post "$BASE_URL/option/question/$Q3_4_ID" '{"optionText":"National Astronomy and Science Authority","isCorrect":false}'

# ── Quiz 4: World Geography ──
echo "[4/5] Creating quiz: World Geography"
QUIZ4=$(post "$BASE_URL/quiz" '{"title":"World Geography","description":"Rivers, mountains, capitals, and borders — do you know your way around the globe?"}')
QID4=$(echo "$QUIZ4" | jq -r '.id')
echo "  quiz id=$QID4"

echo "  adding questions..."
Q4_1=$(post "$BASE_URL/question/quiz/$QID4" '{"questionText":"What is the longest river in the world?","timeLimitSeconds":20,"points":1000}')
Q4_1_ID=$(echo "$Q4_1" | jq -r '.id')
post "$BASE_URL/option/question/$Q4_1_ID" '{"optionText":"Amazon River","isCorrect":false}'
post "$BASE_URL/option/question/$Q4_1_ID" '{"optionText":"Yangtze River","isCorrect":false}'
post "$BASE_URL/option/question/$Q4_1_ID" '{"optionText":"Nile River","isCorrect":true}'
post "$BASE_URL/option/question/$Q4_1_ID" '{"optionText":"Mississippi River","isCorrect":false}'

Q4_2=$(post "$BASE_URL/question/quiz/$QID4" '{"questionText":"Which country has the most natural lakes?","timeLimitSeconds":25,"points":1200}')
Q4_2_ID=$(echo "$Q4_2" | jq -r '.id')
post "$BASE_URL/option/question/$Q4_2_ID" '{"optionText":"United States","isCorrect":false}'
post "$BASE_URL/option/question/$Q4_2_ID" '{"optionText":"Russia","isCorrect":false}'
post "$BASE_URL/option/question/$Q4_2_ID" '{"optionText":"Canada","isCorrect":true}'
post "$BASE_URL/option/question/$Q4_2_ID" '{"optionText":"Finland","isCorrect":false}'

Q4_3=$(post "$BASE_URL/question/quiz/$QID4" '{"questionText":"What is the smallest country in the world by area?","timeLimitSeconds":15,"points":1000}')
Q4_3_ID=$(echo "$Q4_3" | jq -r '.id')
post "$BASE_URL/option/question/$Q4_3_ID" '{"optionText":"Monaco","isCorrect":false}'
post "$BASE_URL/option/question/$Q4_3_ID" '{"optionText":"Vatican City","isCorrect":true}'
post "$BASE_URL/option/question/$Q4_3_ID" '{"optionText":"San Marino","isCorrect":false}'
post "$BASE_URL/option/question/$Q4_3_ID" '{"optionText":"Liechtenstein","isCorrect":false}'

# ── Quiz 5: Movies & Cinema ──
echo "[5/5] Creating quiz: Movies & Cinema"
QUIZ5=$(post "$BASE_URL/quiz" '{"title":"Movies & Cinema","description":"Classic films, legendary directors, and Oscar trivia."}')
QID5=$(echo "$QUIZ5" | jq -r '.id')
echo "  quiz id=$QID5"

echo "  adding questions..."
Q5_1=$(post "$BASE_URL/question/quiz/$QID5" '{"questionText":"Who directed the movie Schindler List?","timeLimitSeconds":20,"points":1000}')
Q5_1_ID=$(echo "$Q5_1" | jq -r '.id')
post "$BASE_URL/option/question/$Q5_1_ID" '{"optionText":"Martin Scorsese","isCorrect":false}'
post "$BASE_URL/option/question/$Q5_1_ID" '{"optionText":"Steven Spielberg","isCorrect":true}'
post "$BASE_URL/option/question/$Q5_1_ID" '{"optionText":"Francis Ford Coppola","isCorrect":false}'
post "$BASE_URL/option/question/$Q5_1_ID" '{"optionText":"Ridley Scott","isCorrect":false}'

Q5_2=$(post "$BASE_URL/question/quiz/$QID5" '{"questionText":"Which movie won the Academy Award for Best Picture in 1994?","timeLimitSeconds":25,"points":1200}')
Q5_2_ID=$(echo "$Q5_2" | jq -r '.id')
post "$BASE_URL/option/question/$Q5_2_ID" '{"optionText":"Pulp Fiction","isCorrect":false}'
post "$BASE_URL/option/question/$Q5_2_ID" '{"optionText":"Forrest Gump","isCorrect":true}'
post "$BASE_URL/option/question/$Q5_2_ID" '{"optionText":"The Shawshank Redemption","isCorrect":false}'
post "$BASE_URL/option/question/$Q5_2_ID" '{"optionText":"Four Weddings and a Funeral","isCorrect":false}'

Q5_3=$(post "$BASE_URL/question/quiz/$QID5" '{"questionText":"In which film does the character say: Here is looking at you, kid?","timeLimitSeconds":20,"points":1000}')
Q5_3_ID=$(echo "$Q5_3" | jq -r '.id')
post "$BASE_URL/option/question/$Q5_3_ID" '{"optionText":"Casablanca","isCorrect":true}'
post "$BASE_URL/option/question/$Q5_3_ID" '{"optionText":"Gone with the Wind","isCorrect":false}'
post "$BASE_URL/option/question/$Q5_3_ID" '{"optionText":"The Maltese Falcon","isCorrect":false}'
post "$BASE_URL/option/question/$Q5_3_ID" '{"optionText":"Citizen Kane","isCorrect":false}'

echo ""
echo "=== Done! Seeded 5 quizzes with 18 questions (72 options) ==="
echo "Try: curl http://localhost:8080/quiz-service/api/quiz/getAll | jq"
