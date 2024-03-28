REM Package application
call mvn clean package -f ../../pom.xml

call remove-latest.bat

REM build new image
call docker build -f ../../src/main/docker/Dockerfile.jvm --tag mic-form:%1 ../../
call docker tag mic-form 10.3.4.18:5000/mic-form:%1