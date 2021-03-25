read -p "Which type of Run do you want it, A = Run only services (No DB or RabbitMQ), B = Run environments tools (Mongo and RabbitMQ) , C = Run 3 services with  Mongo and RabbitMQ. [ENTER]: " runTypeInput
runType=$(echo "$runTypeInput" | tr '[:lower:]' '[:upper:]')

echo "---------------------------------------------------------"

./gradlew clean build

if [[ $runType =~ [ABC] ]]; then
  if [ "$runType" = "A" ]; then
    eval "./gradlew clean build"
    eval "docker-compose -f docker-compose-services.yaml up --build"
    exit 1
  elif [ "$runType" = "B" ]; then
    eval "docker-compose -f docker-compose-env.yaml up --build"
    exit 1
  elif [ "$runType" = "C" ]; then
    eval "./gradlew clean build"
    eval "docker-compose -f docker-compose.yaml up --build"
    exit 1
  fi
else
  echo "Please, enter a valid type of run.: A = Run only services (No DB or RabbitMQ), B = Run environments tools (Mongo and RabbitMQ) , C = Run 3 services with  Mongo and RabbitMQ."
  exit 0
fi
