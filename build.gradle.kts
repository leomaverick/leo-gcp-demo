plugins {
	java
	id("org.springframework.boot") version "3.5.6-SNAPSHOT"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.leo.example"
version = "0.0.1-SNAPSHOT"
description = "Demo project for Spring Boot"

java {
	toolchain { languageVersion = JavaLanguageVersion.of(21) }
}

repositories {
	mavenCentral()
	maven { url = uri("https://repo.spring.io/snapshot") }
}

dependencies {
	// App
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.13")
	implementation("com.google.cloud.sql:postgres-socket-factory:1.16.0")

	implementation(platform("com.google.cloud:libraries-bom:26.49.0"))
	implementation("com.google.cloud:google-cloud-pubsub")

	developmentOnly("org.springframework.boot:spring-boot-devtools")
	runtimeOnly("org.postgresql:postgresql")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.mockito:mockito-inline:5.2.0")

	testImplementation("org.testcontainers:testcontainers:1.20.3")
	testImplementation("org.testcontainers:junit-jupiter:1.20.3")
	testImplementation("org.testcontainers:postgresql:1.20.3")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
