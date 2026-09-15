setupJava(24)
dependencies {
    paperweight.paperDevBundle("1.21.11-R0.1-SNAPSHOT")
}
tasks.assemble {
    dependsOn(tasks.reobfJar)
}