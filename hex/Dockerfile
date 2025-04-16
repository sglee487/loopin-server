# ========================
# Build stage
# ========================
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

# gradle wrapper 전체 복사
COPY gradlew /app/gradlew
COPY gradle/wrapper /app/gradle/wrapper

# 설정 파일 복사
COPY build.gradle.kts settings.gradle.kts /app/

# 헥사고날 아키텍처 모듈 복사
COPY adapter_inbound /app/adapter_inbound
COPY adapter_outbound /app/adapter_outbound
COPY application /app/application
COPY cmd /app/cmd
COPY domain /app/domain
COPY infrastructure /app/infrastructure

RUN ls -alh gradle/wrapper && cat gradle/wrapper/gradle-wrapper.properties


# 실행 권한 부여 후 빌드
RUN chmod +x ./gradlew && ./gradlew :cmd:bootJar --no-daemon

# ========================
# Runtime stage
# ========================
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# 빌드 결과 복사
COPY --from=build /app/cmd/build/libs/app.jar ./app.jar

# 실행 명령
ENTRYPOINT ["java", "-jar", "app.jar"]
