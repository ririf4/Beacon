import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	// Kotlin
	kotlin("jvm") version "2.1.0"
	kotlin("plugin.serialization") version "2.1.0"
	id("org.jetbrains.dokka") version "2.0.0"

	// Publishing
	`maven-publish`
}

group = "net.ririfa"
version = "1.5.2"

repositories {
	mavenCentral()
}

dependencies {
	dokkaHtmlPlugin("org.jetbrains.dokka:kotlin-as-java-plugin:1.9.20")

	implementation("io.github.classgraph:classgraph:4.8.179")
	implementation("org.jetbrains.kotlin:kotlin-reflect:2.1.0")
	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.1")
	implementation("org.slf4j:slf4j-api:2.1.0-alpha1")
}

java {
	withSourcesJar()
	withJavadocJar()

	sourceCompatibility = JavaVersion.VERSION_17
	targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
	jvmToolchain {
		languageVersion.set(JavaLanguageVersion.of(17))
	}
}

tasks.withType<KotlinCompile> {
	compilerOptions {
		jvmTarget.set(JvmTarget.JVM_17)
	}
}

tasks.withType<JavaCompile> {
	options.release.set(17)
}

tasks.named<Jar>("jar") {
	duplicatesStrategy = DuplicatesStrategy.EXCLUDE
	archiveClassifier.set("")
}

publishing {
	publications {
		//maven
		create<MavenPublication>("maven") {

			groupId = project.group.toString()
			artifactId = project.name
			version = project.version.toString()

			from(components["java"])

			pom {
				name.set("Beacon")
				description.set("A simple event API for Kotlin/Java")
				url.set("https://github.com/ririf4/Beacon")
				licenses {
					license {
						name.set("MIT")
						url.set("https://opensource.org/license/mit")
					}
				}
				developers {
					developer {
						id.set("ririfa")
						name.set("RiriFa")
						email.set("main@ririfa.net")
					}
				}
				scm {
					connection.set("scm:git:git://github.com/ririf4/Beacon.git")
					developerConnection.set("scm:git:ssh://github.com/ririf4/Beacon.git")
					url.set("https://github.com/ririf4/Beacon")
				}
				dependencies
			}
		}
	}
	repositories {
		maven {
			val releasesRepoUrl = uri("https://repo.ririfa.net/maven2-rel/")
			val snapshotsRepoUrl = uri("https://repo.ririfa.net/maven2-snap/")
			url = if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl

			credentials {
				username = findProperty("nxUN").toString()
				password = findProperty("nxPW").toString()
			}
		}
	}
}