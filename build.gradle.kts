plugins {
    id("java")
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "maven-publish")

    group = "me.drownek"
    version = "1.0-SNAPSHOT"

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(16))
        }
    }

    tasks.withType<JavaCompile> {
        options.compilerArgs.add("-parameters")
        options.encoding = "UTF-8"
    }

    repositories {
        mavenCentral()
        mavenLocal()
        maven("https://jitpack.io")
        maven("https://storehouse.okaeri.eu/repository/maven-public/")
        maven("https://repo.panda-lang.org/releases")
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    }
}