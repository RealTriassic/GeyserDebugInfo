plugins {
    java
    id("net.kyori.blossom") version "2.1.0"
    id("net.kyori.indra.git") version "3.1.3"
    id("io.freefair.lombok") version "8.10.2"
    id("com.gradleup.shadow") version "8.3.3"
    id("org.jetbrains.gradle.plugin.idea-ext") version "1.1.9"
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

    sourceSets {
        main {
            blossom {
                javaSources {
                    property("version", project.version.toString())
                    property("gitBranch", indraGit.branchName())
                    property("gitCommit", indraGit.commit()?.name)
                }
            }
        }
    }
}

java.toolchain.languageVersion = JavaLanguageVersion.of(17)
