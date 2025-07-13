import xyz.jpenilla.runpaper.task.RunServer
import java.util.Properties

plugins {
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
    id("com.gradleup.shadow") version "9.0.0-beta12"
    id("xyz.jpenilla.run-paper") version "2.3.1"
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

    implementation("com.github.Drownek.light-platform:bukkit:2.0.2")

    implementation("eu.okaeri:okaeri-configs-json-simple:5.0.9")
    implementation("eu.okaeri:okaeri-persistence-jdbc:3.0.1-beta.3")
    implementation("org.mariadb.jdbc:mariadb-java-client:2.7.3")

    /* lombok */
    val lombok = "1.18.32"
    compileOnly("org.projectlombok:lombok:$lombok")
    annotationProcessor("org.projectlombok:lombok:$lombok")
}

tasks.shadowJar {
    archiveFileName.set("StaffActivityMonitor-Bukkit-${project.version}.jar")

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
        "net.kyori",
        "me.drownek.util",
    ).forEach { pack ->
        relocate(pack, "$prefix.$pack")
    }

    /* Fail as it wont work on server versions with plugin remapping */
    duplicatesStrategy = DuplicatesStrategy.FAIL
}

val randomPort = false
val port = 25566

val runVersions = mapOf(
    "1.8.8" to 16,
    "1.16.5" to 16,
    "1.18.2" to 17,
    "1.19.4" to 19,
    "1.20.6" to 21,
    "1.21.5" to 21,
    "1.21.6" to 21,
    "1.21.7" to 21,
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
        languageVersion.set(JavaLanguageVersion.of(16))
    }
}