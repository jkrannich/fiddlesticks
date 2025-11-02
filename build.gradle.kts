plugins {
    id("java")
}

group = "io.github.jkrannich"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.1")
    implementation("com.fasterxml.jackson.core:jackson-core:2.17.1")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.17.1")
    implementation("io.github.cdimascio:dotenv-java:3.0.0")
}

tasks.test {
    useJUnitPlatform()
}