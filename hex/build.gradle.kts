plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.4.0"
	id("io.spring.dependency-management") version "1.1.6"
}

group = "sg.snserver"
version = "0.0.1-SNAPSHOT"

subprojects {
	apply(plugin = "org.jetbrains.kotlin.jvm")

	repositories {
		mavenCentral()
	}

	// Apply plugins conditionally
	if (name != "domain") {
		apply(plugin = "org.jetbrains.kotlin.plugin.spring")
		apply(plugin = "org.springframework.boot")
		apply(plugin = "io.spring.dependency-management")

		// Add dependencies only for non-domain modules after plugins are applied
		dependencies {
			"implementation"("org.springframework.boot:spring-boot-starter")
			"implementation"("org.springframework.boot:spring-boot-starter-web")
			// "implementation"("org.springframework.boot:spring-boot-starter-webflux")
			"implementation"("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
		}
	}

	dependencies {
		"implementation"("org.jetbrains.kotlin:kotlin-reflect")
	}

	tasks.withType<Test> {
		useJUnitPlatform()
	}

	kotlin {
		jvmToolchain(21)
	}
}

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
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
