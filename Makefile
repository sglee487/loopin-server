include .env
export

repo = $(DOCKER_REPO)
image = sn-api-server
tag = $(shell git rev-parse HEAD | cut -c1-8)

.PHONY: build-docker
build-docker:
	docker buildx build \
		--platform linux/amd64,linux/arm64 \
		--file ./hex/Dockerfile \
		--tag $(repo)/$(image):$(tag) \
		--tag $(repo)/$(image):latest \
		--push \
		./hex
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