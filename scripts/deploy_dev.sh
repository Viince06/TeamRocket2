#!/bin/bash
if [ "${PWD##*/}" = "scripts" ]
then
  cd ..
elif [ ! -f ./docker-compose.yml ]
then
  echo "Le script doit être lancé depuis le dossier le contenant ou à la racine du projet."
  exit 1
fi

# 1/ Supprimer les images docker potentiellement lancées + prune
{
  docker-compose down && echo "Arrêt des containers lancés"
} || echo "Aucun container lancé détecté"

docker system prune -f || {
  echo "Docker n'est pas lancé."
  exit 1
}

# 2/ mvn clean install
mvn clean install -DskipTests -q -Dsurefire.printSummary=false || {
  echo "Echec de l'installation maven du projet. Veuillez vérifier que tous les tests passent correctement."
  exit 1
}

# 3/ build des images
{
  docker build -t 7wonders/server:0.1 Server && docker build -t 7wonders/stats:0.1 Stats && docker build -t 7wonders/player:0.1 Player
} || {
  echo "Echec de la création des images docker."
  exit 1
}

# 4/ docker-compose up
docker-compose up -d server stats
sleep 10
docker-compose up --scale player=3 -d player
echo "Les images docker sont parfaitement lancées."
