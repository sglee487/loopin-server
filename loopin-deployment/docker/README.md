# Docker Compose 실행 가이드

## 프로파일별 실행 명령어

### 개발용 (인프라 + 애플리케이션)
실행:
```bash
docker compose --profile infra --profile app up -d
```

중지 및 삭제:
```bash
docker compose --profile infra --profile app down
```

### 인프라만 실행 (DB, Redis, Keycloak)
실행:
```bash
docker compose --profile infra up -d
```

중지 및 삭제:
```bash
docker compose --profile infra down
```

### 애플리케이션 서비스만 실행
실행:
```bash
docker compose --profile app up -d
```

중지 및 삭제:
```bash
docker compose --profile app down
```

### 스트리밍 서비스만 실행
실행:
```bash
docker compose --profile streaming up -d
```

중지 및 삭제:
```bash
docker compose --profile streaming down
```

### 모니터링 서비스만 실행
실행:
```bash
docker compose --profile observability up -d
```

중지 및 삭제:
```bash
docker compose --profile observability down
```

### 전체 실행
실행:
```bash
docker compose --profile all up -d
```

또는:
```bash
docker compose up -d
```

중지 및 삭제:
```bash
docker compose down
```

### 조합 실행 예제
개발 환경 (인프라 + 앱 + 모니터링) 실행:
```bash
docker compose --profile infra --profile app --profile observability up -d
```

개발 환경 중지:
```bash
docker compose --profile infra --profile app --profile observability down
```

스트리밍 테스트 (인프라 + 앱 + 스트리밍) 실행:
```bash
docker compose --profile infra --profile app --profile streaming up -d
```

스트리밍 테스트 중지:
```bash
docker compose --profile infra --profile app --profile streaming down
```

### 전체 리셋 (모든 컨테이너, 볼륨, 네트워크 삭제)
```bash
docker compose down --volumes --remove-orphans
```

---

## change keycloak client password

```shell
/opt/keycloak/bin/kcadm.sh config credentials \
--server http://localhost:8080 \
--realm master \
--user user \
--password 'password'

```

```shell
/opt/keycloak/bin/kcadm.sh get clients -r loopin --fields id,clientId
```

```shell
/opt/keycloak/bin/kcadm.sh update clients/{clientId} \
-r loopin \
-s secret='keycloak-client-secret'
```