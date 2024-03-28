call docker-compose down 
call docker rm -f mic-identity-api-dev
call docker rmi 10.3.4.18:5000/mic-identity-api
call docker-compose up -d
