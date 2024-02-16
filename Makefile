VERSION=1.0.0

pull:
	docker compose -f ./docker/docker-compose.yml pull

build:
	mvn clean install -DskipTests && docker build --no-cache -t ee/lhv:$(VERSION) .

deploy-postgres:
	docker compose -f ./docker/docker-compose.yml up postgres -d

deploy-lhv:
	docker compose -f ./docker/docker-compose.yml up lhv -d