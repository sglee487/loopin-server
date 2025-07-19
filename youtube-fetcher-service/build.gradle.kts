import org.springframework.boot.gradle.tasks.bundling.BootBuildImage

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
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.springframework.cloud:spring-cloud-starter-kubernetes-client-config:3.3.0")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.projectreactor:reactor-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	// google api (youtube)
	implementation("com.google.api-client:google-api-client:2.6.0")
	implementation("com.google.oauth-client:google-oauth-client:1.36.0")
	implementation("com.google.apis:google-api-services-youtube:v3-rev20240514-2.0.0")
	implementation("com.google.http-client:google-http-client-jackson2:1.44.2")
	implementation("com.google.api.client:google-api-client-extensions:1.4.1-beta")
	implementation("com.google.api-client:google-api-client-java6:2.1.4")
	implementation("com.google.oauth-client:google-oauth-client-jetty:1.36.0")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

tasks.named<BootBuildImage>("bootBuildImage") {
	imageName.set(project.name)

	docker.publishRegistry {
		username.set(project.findProperty("registryUsername")?.toString())
		password.set(project.findProperty("registryToken")?.toString())
		url.set(project.findProperty("registryUrl")?.toString())
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
