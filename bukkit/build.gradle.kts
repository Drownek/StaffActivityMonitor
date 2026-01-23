import xyz.jpenilla.runpaper.task.RunServer
import java.util.Properties

plugins {
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
    id("com.gradleup.shadow") version "9.0.0-beta12"
    id("xyz.jpenilla.run-paper") version "2.3.1"
    id("me.drownek.paper-e2e") version "1.0.2"
}

e2e {
    minecraftVersion.set("1.19.4")
    autoDownloadServer.set(true)
    acceptEula.set(true)
    testsDir.set(file("src/test/e2e"))
}

bukkit {
    main = "me.drownek.staffactivity.StaffActivityPlugin"
    apiVersion = "1.13"
    name = "StaffActivityMonitor"
    author = "Drownek"
    version = "${project.version}"
}

dependencies {
    implementation(project(":core"))

    compileOnly("org.spigotmc:spigot-api:1.16.5-R0.1-SNAPSHOT")

    implementation("io.github.drownek:platform-bukkit:2.3.2-SNAPSHOT")

    implementation("org.bstats:bstats-bukkit:3.0.2")

    /* lombok */
    val lombok = "1.18.32"
    compileOnly("org.projectlombok:lombok:$lombok")
    annotationProcessor("org.projectlombok:lombok:$lombok")
}

tasks.shadowJar {
    archiveFileName.set("StaffActivityMonitor-Bukkit-${project.version}.jar")

    mergeServiceFiles()

    val prefix = "me.drownek.staffactivity.libs"
    listOf(
        "eu.okaeri",
        "dev.rollczi.litecommands",
        "com.cryptomorin",
        "dev.triumphteam",
        "panda",
        "net.jodah",
        "net.kyori",
        "me.drownek.util",
        "org.bstats",
    ).forEach { pack ->
        relocate(pack, "$prefix.$pack")
    }

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

val randomPort = false
val port = 25566

val runVersions = mapOf(
    "1.8.8" to 17,
    "1.12.2" to 17,
    "1.13.2" to 17,
    "1.16.5" to 17,
    "1.17.1" to 17,
    "1.18.2" to 17,
    "1.19.4" to 19,
    "1.21.11" to 21
)

tasks {
    runVersions.forEach { key, value ->
        val n = key.replace(".", "_")
        register("run$n", RunServer::class) {
            minecraftVersion(key)

            /* Automatically accept EULA */
            jvmArgs("-Dcom.mojang.eula.agree=true")

            val runDir = layout.projectDirectory.dir("run$n")
            runDirectory.set(runDir)
            pluginJars.from(shadowJar.flatMap { it.archiveFile })

            /* Start server with specified Java version */
            val toolchains = project.extensions.getByType<JavaToolchainService>()
            javaLauncher.set(toolchains.launcherFor {
                languageVersion.set(JavaLanguageVersion.of(value))
            })

            /* Assign random or specified port for multiple instances at the same time */
            doFirst {
                val runDirFile = runDir.asFile
                if (!runDirFile.exists()) {
                    runDirFile.mkdirs()
                }

                val serverPropertiesFile = runDirFile.resolve("server.properties")
                if (!serverPropertiesFile.exists()) {
                    serverPropertiesFile.createNewFile()
                }

                val props = Properties().apply {
                    serverPropertiesFile.inputStream().use { load(it) }
                }

                val port = if (randomPort) (20000..40000).random() else port
                props["server-port"] = port.toString()

                serverPropertiesFile.outputStream().use { props.store(it, null) }

                println(">> Starting server $key on port $port")
            }
        }
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}