plugins{
    java
}
val source = _repositories.RoseWoodDev
val sharedPaperAPI = "1.21"
subprojects{
    apply(plugin = "java")
    repositories {
        mavenCentral()
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        maven("https://repo.papermc.io/repository/maven-public/")
        maven(source.repository)
    }
    loadJava(8)
}
val shared = project("shared")
shared.dependencies {
    add("compileOnly", "io.papermc.paper:paper-api:$sharedPaperAPI-R0.1-SNAPSHOT") //坏paper
}
subprojects {
    dependencies {
        if (name != "shared") add("compileOnly", shared)
    }
}
object repositories{
    class Sources(
        val repository: String,
        val dependency: (String) -> String
    );
    val RoseWoodDev = Sources(
        repository = "https://repo.rosewooddev.io/repository/public/",
        dependency = { zako -> "org.spigotmc:spigot:$zako" }
    )
}