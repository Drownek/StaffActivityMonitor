plugins {
    id("com.gradleup.shadow") version "9.0.0-beta12"
    id("xyz.jpenilla.run-velocity") version "2.3.1"
}

dependencies {
    implementation(project(":core"))

    compileOnly("com.velocitypowered:velocity-api:3.3.0-SNAPSHOT")
    annotationProcessor("com.velocitypowered:velocity-api:3.3.0-SNAPSHOT")

    implementation("eu.okaeri:okaeri-configs-json-simple:5.0.9")
    implementation("eu.okaeri:okaeri-persistence-jdbc:3.0.1-beta.3")
    implementation("org.mariadb.jdbc:mariadb-java-client:2.7.3")

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

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}