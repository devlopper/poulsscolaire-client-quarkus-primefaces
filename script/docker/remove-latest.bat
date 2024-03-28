REM stop running containers
call docker-compose -f docker-compose.yml down

REM remove old images
call docker rmi 10.3.4.18:5000/mic-form