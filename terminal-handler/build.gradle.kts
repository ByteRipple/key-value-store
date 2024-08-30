plugins {
    kotlin("jvm") version "1.9.23"
    application
    java
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.10.3")
    implementation(project(":core-lib"))
    testImplementation(kotlin("test"))
}

application {
    mainClass.set("org.Main")
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "org.Main"
    }
    destinationDirectory.set(file("${rootProject.projectDir}/output-jars"))

    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}