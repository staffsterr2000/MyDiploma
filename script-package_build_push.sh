mvn clean package
docker build . -t staffsterr2000/clinic-app:1.0.0
docker push staffsterr2000/clinic-app:1.0.0
docker-compose -f docker-compose.yml --env-file docker.env up