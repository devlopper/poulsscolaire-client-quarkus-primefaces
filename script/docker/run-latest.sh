docker-compose down 
docker rm -f mic-identity-api-dev
docker rmi 10.3.4.18:5000/mic-identity-api
docker-compose up -d
