import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm") version "2.1.0"
	kotlin("plugin.serialization") version "2.1.0"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.4.2"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.ntu.sc2006"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(22)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-json:3.4.2")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
	implementation("io.jsonwebtoken:jjwt-api:0.12.6")
	implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.2")
	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")
	implementation("org.reactivestreams:reactive-streams:1.0.4")
	implementation("io.projectreactor:reactor-core:3.7.3")
	runtimeOnly("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.10.1")

	val ktorVersion = "3.1.0"

	implementation("io.ktor:ktor-client-apache5:$ktorVersion")

	val supabaseVersion = "3.1.1"

	implementation("io.github.jan-tennert.supabase:auth-kt:$supabaseVersion")
	implementation("io.github.jan-tennert.supabase:postgrest-kt:$supabaseVersion")
	implementation("io.github.jan-tennert.supabase:realtime-kt:$supabaseVersion")
	implementation("io.github.jan-tennert.supabase:storage-kt:$supabaseVersion")

	implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.2")

	developmentOnly("org.springframework.boot:spring-boot-devtools")
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testImplementation("org.springframework.security:spring-security-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

tasks.withType<KotlinCompile> {
	compilerOptions {
        freeCompilerArgs.addAll(listOf("-opt-in=kotlin.uuid.ExperimentalUuidApi"))
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
