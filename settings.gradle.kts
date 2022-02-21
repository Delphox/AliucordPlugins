rootProject.name = "AliucordPlugins"

listOf(
    "NitroSpoof"
).forEach { plugin ->
    include(":$plugin")
    project(":$plugin").projectDir = File("./plugins/$plugin")
}
