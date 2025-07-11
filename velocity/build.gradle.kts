plugins {
    id("com.gradleup.shadow") version "9.0.0-beta12"
    id("xyz.jpenilla.run-velocity") version "2.3.1"
}

repositories {
    maven("https://jitpack.io")
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("com.velocitypowered:velocity-api:3.3.0-SNAPSHOT")
    annotationProcessor("com.velocitypowered:velocity-api:3.3.0-SNAPSHOT")

    implementation("com.github.Drownek.light-platform:velocity:2.0.2")

    /* lombok */
    val lombok = "1.18.32"
    compileOnly("org.projectlombok:lombok:$lombok")
    annotationProcessor("org.projectlombok:lombok:$lombok")
}

tasks.shadowJar {
    archiveFileName.set("StaffActivityMonitor-Velocity-${project.version}.jar")

    exclude(
        "org/intellij/lang/annotations/**",
        "org/jetbrains/annotations/**",
        "META-INF/**",
        "javax/**"
    )

    val prefix = "me.drownek.staffactivity.libs"
    listOf(
        "eu.okaeri",
        "dev.rollczi.litecommands",
        "com.cryptomorin",
        "dev.triumphteam",
        "panda",
        "net.jodah",
        "me.drownek.util",
    ).forEach { pack ->
        relocate(pack, "$prefix.$pack")
    }
}

tasks {
    runVelocity {
        velocityVersion("3.3.0-SNAPSHOT")
    }
}