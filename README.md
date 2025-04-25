### How to deploy

1. build image
```shell
make build-docker
```

2. (optional) test built image before push

```shell
docker run \
  --rm \
  --name api-app \
  --network sn-network \
  -p 8080:8080 \
  --env-file .env.docker \
  $(DOCKER_REPO)/sn-api-server:latest
```

3. push docker image
```shell
make push-docker
```

4. (optional) clean built images
```shell
make clean-docker
```
