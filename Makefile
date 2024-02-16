VERSION=1.0.0

pull:
	docker compose -f ./docker/docker-compose.yml pull

build-jar:
	mvn clean install -DskipTests

build-image:
	docker build --no-cache -t ee/lhv:$(VERSION) .

deploy-postgres:
	docker compose -f ./docker/docker-compose.yml up postgres -d

deploy-lhv:
	docker compose -f ./docker/docker-compose.yml up lhv -d