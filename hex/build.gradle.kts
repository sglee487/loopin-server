plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.2.4"
	id("io.spring.dependency-management") version "1.1.4"
}

group = "sg.snserver"
version = "0.0.2-SNAPSHOT" // 버전 업!

allprojects {
	repositories {
		mavenCentral()
	}

	tasks.withType<JavaCompile> {
		options.isFork = true
		options.forkOptions.javaHome = file(System.getProperty("java.home"))
	}
}

java {
	toolchain {
		languageVersion.set(JavaLanguageVersion.of(21))
	}
}

repositories {
	mavenCentral()
}

subprojects {
	apply(plugin = "org.jetbrains.kotlin.jvm")

	repositories {
		mavenCentral()
	}

	if (name != "domain") {
		apply(plugin = "org.jetbrains.kotlin.plugin.spring")
		apply(plugin = "org.springframework.boot")
		apply(plugin = "io.spring.dependency-management")

		dependencies {
			"implementation"("org.springframework.boot:spring-boot-starter")
			"implementation"("org.springframework.boot:spring-boot-starter-web")
			"implementation"("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
		}
	}

	dependencies {
		"implementation"("org.jetbrains.kotlin:kotlin-reflect")
	}

	if (name != "cmd" && tasks.findByName("bootJar") != null) {
		tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
			enabled = false
		}
	}

	tasks.withType<Test> {
		useJUnitPlatform()
	}

	tasks.withType<Test>().configureEach {
		useJUnitPlatform()
		reports {
			junitXml.required.set(true)
			html.required.set(true)
		}
	}

	kotlin {
		jvmToolchain(21)
	}
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.jetbrains.kotlin:kotlin-reflect")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test>().configureEach {
	useJUnitPlatform()
	reports {
		junitXml.required.set(true)
		html.required.set(true)
	}
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
	kotlinOptions {
		freeCompilerArgs = freeCompilerArgs + "-Xjsr305=strict"
	}
}
