REM Package application
call mvn clean package -f ../../pom.xml

REM stop running containers
REM call docker-compose -f docker-compose.yml down

REM remove old images
call docker rmi cykcorp/poulsscolaire:202408051926

REM build new image
call docker build -f ../../src/main/docker/Dockerfile.jvm --tag "cykcorp/poulsscolaire:202408051517" ../../
call docker tag poulsscolaire cykcorp/poulsscolaire:202408051517
call docker login -u "kycdev@gmail.com" -p "P@sSw0rd@2O18" docker.io
call docker push cykcorp/poulsscolaire:202408051926

call docker build -f ../../src/main/docker/Dockerfile.jvm --tag "europe-west1-docker.pkg.dev/fair-splice-361010/vie-ecole-docker-repo/poulsscolaire:202408051517" ../../
call docker tag poulsscolaire europe-west1-docker.pkg.dev/fair-splice-361010/vie-ecole-docker-repo/poulsscolaire:202408051517
call docker push europe-west1-docker.pkg.dev/fair-splice-361010/vie-ecole-docker-repo/poulsscolaire:202408051926