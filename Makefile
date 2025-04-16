include .env
export

# repo = abc
repo = $(DOCKER_REPO)
# image = ems/image
image = sn-api-server
tag = $(shell git rev-parse HEAD | cut -c1-8)

.PHONY: build-docker
build-docker:
	docker build ./hex -f ./hex/Dockerfile -t $(repo)/$(image):$(tag) --platform="linux/amd64"
	docker tag $(repo)/$(image):$(tag) $(repo)/$(image):latest

.PHONY: push-docker
push-docker:
	docker push $(repo)/$(image):$(tag)
	docker push $(repo)/$(image):latest

.PHONY: clean-docker
clean-docker:
	docker rmi $(repo)/$(image):$(tag) || true
	docker rmi $(repo)/$(image):latest || true

.PHONY: docker-login
docker-login:
	echo $(DOCKER_TOKEN) | docker login --username $(DOCKER_USERNAME) --password-stdin