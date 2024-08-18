REM Package application
call mvn clean package -f ../../pom.xml

REM stop running containers
call docker-compose -f docker-compose.yml down

REM remove old images
call docker rmi cykcorp/poulsscolaire

REM build new image
call docker build -f ../../src/main/docker/Dockerfile.jvm --tag "cykcorp/poulsscolaire:latest" ../../
call docker tag poulsscolaireapi cykcorp/poulsscolaire

call push-to-docker-hub-latest.bat