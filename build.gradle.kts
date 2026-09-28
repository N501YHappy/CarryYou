plugins {
    java
    id("com.gradleup.shadow") version "9.3.0"
    id("xyz.jpenilla.run-paper") version "3.0.2"
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.21" apply false
}
group = "xyz.n501yhappy"
version = "2.5"

repositories {
    maven("https://mirrors.huaweicloud.com/repository/maven")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.codemc.io/repository/maven-public/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    mavenCentral()
    maven("https://oss.sonatype.org/content/groups/public/")

    maven("https://maven.enginehub.org/repo/") //Worldguard
}


dependencies {
    compileOnly("org.spigotmc:spigot-api:1.16.5-R0.1-SNAPSHOT")
    compileOnly("com.sk89q.worldguard:worldguard-bukkit:7.0.0")
    compileOnly(fileTree("libs"))
    compileOnly("cn.lunadeer:DominionAPI:4.7.3")

    implementation(project(":adapts:shared"))
    implementation(project(":adapts:folia"))
    implementation(project(":adapts:bukkit"))

    implementation(project(":plugin-api"))

    File("nms").listFiles()?.forEach { file ->
        if (File(file, "build.gradle.kts").exists()) {
            implementation(project(":nms:${file.name}"))
        }
    }
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}
// paperweight 要求 Gradle 守护进程用 Java 21+ 运行,那只影响构建环境;
// 插件能否在 1.17.1(Java 17)服务器上运行取决于字节码版本,所以所有模块默认 release 17。
// 需要更高版本的模块(如 nms)用自己的 setupJava() 覆盖。
subprojects {
    tasks.withType<JavaCompile>().configureEach {
        options.release = 17
    }
}
tasks {
    runServer {
        minecraftVersion("1.21.11")
    }
    shadowJar {
        relocate("adapts.impl", "xyz.n501yhappy.carryyou.adapts")
        relocate("carryyou", "xyz.n501yhappy.carryyou"){
            skipStringConstants = true
        }
    }

    processResources {
        filesMatching("plugin.yml") {
            expand(project.properties)
        }
    }
}
