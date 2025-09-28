rootProject.name = "StaffActivityMonitor"
plugins {
    // add toolchain resolver
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        mavenLocal()
        maven("https://jitpack.io")
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://storehouse.okaeri.eu/repository/maven-public/")
        maven("https://repo.panda-lang.org/releases")
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        maven("https://oss.sonatype.org/content/repositories/snapshots")
    }
}

include("bukkit", "velocity", "bungee", "core")