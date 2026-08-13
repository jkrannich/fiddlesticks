import org.gradle.api.publish.maven.tasks.PublishToMavenRepository

plugins {
    id("java-library")
    id("maven-publish")
    signing
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(26)
    }
    withSourcesJar()
    withJavadocJar()
}

group = "io.github.jkrannich"
version = providers.gradleProperty("releaseVersion").orElse("0.1.0-SNAPSHOT").get()
description = "A synchronous Java client for the Riot Games League of Legends APIs."

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            artifactId = "leagueapi"

            pom {
                name.set("Fiddlesticks League API")
                description.set(project.description)
                url.set("https://github.com/jkrannich/fiddlesticks")

                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/license/mit/")
                        distribution.set("repo")
                    }
                }

                developers {
                    developer {
                        id.set("jkrannich")
                        name.set("jkrannich")
                        url.set("https://github.com/jkrannich")
                    }
                }

                scm {
                    connection.set("scm:git:git://github.com/jkrannich/fiddlesticks.git")
                    developerConnection.set("scm:git:ssh://git@github.com/jkrannich/fiddlesticks.git")
                    url.set("https://github.com/jkrannich/fiddlesticks")
                }
            }
        }
    }
}

val centralPortalUsername = providers.gradleProperty("centralPortalUsername")
    .orElse(providers.environmentVariable("CENTRAL_PORTAL_USERNAME"))
    .orNull
val centralPortalPassword = providers.gradleProperty("centralPortalPassword")
    .orElse(providers.environmentVariable("CENTRAL_PORTAL_PASSWORD"))
    .orNull
val signingKey = providers.gradleProperty("signingKey").orNull
val signingPassword = providers.gradleProperty("signingPassword").orNull
val signingKeyId = providers.gradleProperty("signingKeyId").orNull
val isReleaseVersion = !version.toString().endsWith("-SNAPSHOT")

signing {
    setRequired {
        isReleaseVersion && gradle.taskGraph.allTasks.any { task ->
            task.name.startsWith("publish") || task.name.contains("CentralPortal")
        }
    }

    if (!signingKey.isNullOrBlank() && !signingPassword.isNullOrBlank()) {
        if (!signingKeyId.isNullOrBlank()) {
            useInMemoryPgpKeys(signingKeyId, signingKey, signingPassword)
        } else {
            useInMemoryPgpKeys(signingKey, signingPassword)
        }
    }

    sign(publishing.publications["mavenJava"])
}

tasks.register("verifyReleaseConfiguration") {
    group = "publishing"
    description = "Verifies the release version and signing configuration."

    doLast {
        val releaseVersion = version.toString()
        require(releaseVersion.matches(Regex("\\d+\\.\\d+\\.\\d+"))) {
            "Release version must be plain SemVer (for example 0.1.0), but was '$releaseVersion'."
        }
        require(!centralPortalUsername.isNullOrBlank()) {
            "Missing Central Portal username. Provide CENTRAL_PORTAL_USERNAME or ORG_GRADLE_PROJECT_centralPortalUsername."
        }
        require(!centralPortalPassword.isNullOrBlank()) {
            "Missing Central Portal password. Provide CENTRAL_PORTAL_PASSWORD or ORG_GRADLE_PROJECT_centralPortalPassword."
        }
        require(!signingKey.isNullOrBlank()) {
            "Missing signingKey. Provide it through ORG_GRADLE_PROJECT_signingKey or -PsigningKey."
        }
        require(!signingPassword.isNullOrBlank()) {
            "Missing signingPassword. Provide it through ORG_GRADLE_PROJECT_signingPassword or -PsigningPassword."
        }
        require(!signingKeyId.isNullOrBlank()) {
            "Missing signingKeyId. Provide it through ORG_GRADLE_PROJECT_signingKeyId or -PsigningKeyId."
        }
    }
}

tasks.withType<PublishToMavenRepository>().configureEach {
    dependsOn("verifyReleaseConfiguration")
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.mockito:mockito-core:5.20.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.8.0")
    testImplementation("org.assertj:assertj-core:3.27.7")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    compileOnly("org.projectlombok:lombok:1.18.46")
    annotationProcessor("org.projectlombok:lombok:1.18.46")
    testCompileOnly("org.projectlombok:lombok:1.18.46")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.46")

    implementation("tools.jackson.core:jackson-databind:3.2.1")
    testImplementation("io.github.cdimascio:dotenv-java:3.0.0")
}

tasks.test {
    useJUnitPlatform {
        if (!project.hasProperty("includeIntegrationTests")) {
            excludeTags("integration")
        }
    }
}
