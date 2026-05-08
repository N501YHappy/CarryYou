rootProject.name = "CarryYou"
plugins{
    id("org.gradle.toolchains.foojay-resolver-convention") version("1.0.0")
}
include(":nms")
File("nms").listFiles()?.forEach { file ->
    if (File(file, "build.gradle.kts").exists()) {
        include(":nms:${file.name}")
    }
}