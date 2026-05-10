import org.gradle.kotlin.dsl.register

plugins {
    java
    id("com.gradleup.shadow") version "9.3.0"
}

group = "xyz.n501yhappy"
version = "2.0"
configurations.create("shadowLink")
repositories {
    maven("https://mirrors.huaweicloud.com/repository/maven")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.rosewooddev.io/repository/public/")
    maven("https://repo.codemc.io/repository/maven-public/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    mavenCentral()
    maven("https://oss.sonatype.org/content/groups/public/")

    maven("https://maven.enginehub.org/repo/") //Worldguard
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.21-R0.1-SNAPSHOT")
    compileOnly ("com.sk89q.worldguard:worldguard-bukkit:7.0.0") //W! O! R! L! D! G! R! O! U! N! D!
    compileOnly (files("./libs/Residence6.0.1.6.jar"))
    for (nms in project.project(":nms").subprojects) {
        if (nms.name == "shared") implementation(nms)
        add("shadowLink", nms)
    }
}
val targetJavaVersion = 8
java {
    val javaVersion = JavaVersion.toVersion(targetJavaVersion)
    if (JavaVersion.current() < javaVersion) {
        val lang = JavaLanguageVersion.of(targetJavaVersion)
        toolchain.languageVersion.set(lang)
    }
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
}
tasks {
    shadowJar {
        // 添加 shadowLink 配置到打包任务，不在代码进行依赖引用，单纯打包 NMS 实现进去，即可杂交编译目标
        configurations.add(project.configurations.getByName("shadowLink"))
        relocate("nms.impl", "xyz.n501yhappy.carryyou.nms")
    }
    val copyTask = create<Copy>("copyBuildArtifact") {
        dependsOn(shadowJar)
        from(shadowJar.get().outputs)
        rename { "${project.name}-$version.jar" }
        into(rootProject.file("Nya_Build"))
    }
    build {
        dependsOn(copyTask)
    }
    withType<JavaCompile>().configureEach {
        options?.encoding = "UTF-8"

        if (targetJavaVersion >= 10 || JavaVersion.current().isJava10Compatible()) {
            options?.release?.set(targetJavaVersion)
        }
    }

    processResources {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
        from(sourceSets.main.get().resources.srcDirs) {
            expand(mapOf("version" to project.version))
            include("plugin.yml")
        }
    }
}