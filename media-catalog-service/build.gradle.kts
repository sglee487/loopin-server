plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.5.0"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.loopin"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation ("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
	implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
	implementation("org.flywaydb:flyway-core")
	implementation("org.flywaydb:flyway-database-postgresql")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
	implementation("org.springframework:spring-jdbc")

//	// Bucket4j – reactive + Redis(lettuce) back-end
////	implementation("org.springframework.boot:spring-boot-starter-cache")
////	implementation("org.springframework.boot:spring-boot-starter-data-redis")
////
//	implementation("com.giffing.bucket4j.spring.boot.starter:bucket4j-spring-boot-starter:0.12.10")
////	implementation("com.bucket4j:bucket4j-redis:8.10.1")
//
////	implementation("io.lettuce:lettuce-core:6.7.0.RELEASE")
////	implementation("org.apache.commons:commons-pool2:2.12.0")
//
//
//	implementation("com.bucket4j:bucket4j_jdk17-core:8.14.0")
//	implementation("com.bucket4j:bucket4j_jdk17-redis-common:8.14.0")
////	implementation("io.lettuce:lettuce-core:6.7.0.RELEASE")
//	implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive")

	// 이미 있던 것
	implementation("com.giffing.bucket4j.spring.boot.starter:bucket4j-spring-boot-starter:0.12.10")

	// 🔑 Redis-Lettuce 전용 어댑터
	implementation("com.bucket4j:bucket4j-redis:8.10.1")

	// Lettuce & 커넥션 풀 (버전은 부트가 관리해도 무방)
	implementation("io.lettuce:lettuce-core")          // 생략하면 spring-boot-starter-data-redis가 대신 가져옴
	implementation("org.apache.commons:commons-pool2") // pooled connection 필수

	// Spring Cache 를 Redis 로 쓰려면 (아래 “옵션 A”)
//	implementation("org.springframework.boot:spring-boot-starter-data-redis")
	implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive")

	// 불필요해진 중복·실험적 모듈 제거
	// implementation("com.bucket4j:bucket4j_jdk17-redis-common:8.14.0") // <- bucket4j-redis가 포함하므로 삭제
	// implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive") // 필요 없으면 삭제

	implementation("com.giffing.bucket4j.spring.boot.starter:bucket4j-spring-boot-starter-context:0.12.10")

	implementation("io.micrometer:context-propagation")

	runtimeOnly("org.postgresql:postgresql")
	runtimeOnly("org.postgresql:r2dbc-postgresql")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.boot:spring-boot-testcontainers")
	testImplementation("io.projectreactor:reactor-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
	testImplementation("org.testcontainers:junit-jupiter")
	testImplementation("org.testcontainers:postgresql")
	testImplementation("org.testcontainers:r2dbc")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}