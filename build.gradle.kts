plugins {
    java
    id("io.freefair.lombok") version "8.10.2"
    id("com.gradleup.shadow") version "8.3.3"
}

repositories {
    mavenCentral()
    maven("https://repo.opencollab.dev/main/")
}

dependencies {
    implementation("org.spongepowered", "configurate-yaml", "4.2.0-GeyserMC-SNAPSHOT")

    compileOnly("org.geysermc.geyser", "core", "2.4.3-SNAPSHOT")
    compileOnly("org.projectlombok", "lombok", "1.18.34")
    annotationProcessor("org.projectlombok", "lombok", "1.18.34")

    testCompileOnly("org.projectlombok", "lombok", "1.18.34")
    testAnnotationProcessor("org.projectlombok", "lombok", "1.18.34")
}

tasks {
    build {
        dependsOn(shadowJar)
    }

    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(17)
    }

    shadowJar {
        archiveBaseName.set(project.name)
        archiveClassifier.set(null as String?)

        relocate("org.spongepowered", "com.triassic.geyserdebuginfo.lib.org.spongepowered")
    }

    processResources {
        filesMatching("extension.yml") {
            expand(
                "name" to project.name,
                "version" to project.version
            )
        }
    }
}

java.toolchain.languageVersion = JavaLanguageVersion.of(17)
