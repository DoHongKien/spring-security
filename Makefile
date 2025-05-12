all: 
	build

build: 
	docker-compose -f Docker-compose.yml build

run: build
	docker-compose -f Docker-compose.yml up -d

stop: 
	docker-compose -f Docker-compose.yml down

clean: stop
	docker-compose -f Docker-compose.yml down --rmi all --volumes

.PHONY: all build run stop clean