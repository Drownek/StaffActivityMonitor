plugins {
    id("com.gradleup.shadow") version "9.0.0-beta12"
    id("xyz.jpenilla.run-waterfall") version "2.3.1"
    id("de.eldoria.plugin-yml.bungee") version "0.8.0"
}

dependencies {
    implementation(project(":core"))

    compileOnly("net.md-5:bungeecord-api:1.21-R0.3")

    implementation("com.github.Drownek:platform-bungee:2.3.0-beta2")

    implementation("org.bstats:bstats-bungeecord:3.0.2")

    /* lombok */
    val lombok = "1.18.32"
    compileOnly("org.projectlombok:lombok:$lombok")
    annotationProcessor("org.projectlombok:lombok:$lombok")
}

bungee {
    main = "me.drownek.staffactivity.StaffActivityPlugin"
    name = "StaffActivityMonitor"
    author = "Drownek"
}

tasks.shadowJar {
    archiveFileName.set("StaffActivityMonitor-Bungee-${project.version}.jar")

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
        "org.bstats",
    ).forEach { pack ->
        relocate(pack, "$prefix.$pack")
    }
}

tasks {
    runWaterfall {
        waterfallVersion("1.21")
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}