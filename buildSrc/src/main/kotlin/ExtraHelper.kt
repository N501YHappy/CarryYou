import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.kotlin.dsl.withType
object _repositories{
    class Sources(
        val repository: String,
        val dependency: (String) -> String
    );
    val RoseWoodDev = Sources(
        repository = "https://repo.rosewooddev.io/repository/public/",
        dependency = { zako -> "org.spigotmc:spigot:$zako" }
    )
}
fun DependencyHandler.loadNMS(mcversion : String){
    add("compileOnly", "org.spigotmc:spigot-api:$mcversion-R0.1-SNAPSHOT")
    add("compileOnly", _repositories.RoseWoodDev.dependency(mcversion))
}
fun Project.loadJava(javaVer: Int) {
    extensions.configure(JavaPluginExtension::class.java) {
        disableAutoTargetJvm()
        val ver = JavaVersion.toVersion(javaVer)
        if (JavaVersion.current() < ver) {
            val lang = JavaLanguageVersion.of(javaVer)
            toolchain.languageVersion.set(lang)
        }
        sourceCompatibility = ver
        targetCompatibility = ver
    }
    tasks.withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
        sourceCompatibility = javaVer.toString()
        targetCompatibility = javaVer.toString()
    }
}
