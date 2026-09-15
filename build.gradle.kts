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

configurations.create("shadowLink")

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.16.5-R0.1-SNAPSHOT")
    compileOnly("com.sk89q.worldguard:worldguard-bukkit:7.0.0")
    compileOnly(fileTree("libs"))
    compileOnly("cn.lunadeer:DominionAPI:4.7.3")
    implementation(project(":adapts:shared"))
    compileOnly(project(":plugin-api"))
    add("shadowLink", project(":adapts:folia"))
    add("shadowLink", project(":adapts:bukkit"))
    add("shadowLink", project(":plugin-api"))
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
        downloadPlugins.modrinth("WorldGuard", "7.0.17")
        downloadPlugins.modrinth("Luckperms", "v5.5.71-bukkit")
        downloadPlugins.url("https://cdn.modrinth.com/data/1u6JkXh5/versions/F5ea2ov3/worldedit-bukkit-7.4.5.jar?mr_download_reason=standalone&mr_game_version=1.21.11&mr_loader=paper")
    }
    shadowJar {
        configurations.add(project.configurations.getByName("shadowLink"))
        // Shadow 9.x 需要手动指定 runtimeClasspath
        configurations.add(project.configurations.runtimeClasspath.get())
        // 重命名包避免和别的插件冲突
        relocate("adapts.impl", "xyz.n501yhappy.carryyou.adapts")
    }

    processResources {
        filesMatching("plugin.yml") {
            expand(project.properties)
        }
    }
}
