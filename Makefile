pull:
	docker compose -f ./docker/docker-compose.yml pull

deploy-postgres:
	docker compose -f ./docker/docker-compose.yml up postgres -d