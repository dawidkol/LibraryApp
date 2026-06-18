TAG=$(git rev-parse HEAD)
docker build -t dkcodepro/libraryapp:$TAG .
docker push dkcodepro/libraryapp:$TAG
docker push dkcodepro/libraryapp:latest