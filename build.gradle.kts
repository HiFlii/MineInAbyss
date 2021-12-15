import Com_mineinabyss_conventions_platform_gradle.Deps

val idofrontVersion: String by project
val gearyVersion: String by project
val gearyAddonsVersion: String by project
val lootyVersion: String by project
val deeperWorldVersion: String by project

plugins {
    id("com.mineinabyss.conventions.kotlin")
    id("com.mineinabyss.conventions.papermc")
    id("com.mineinabyss.conventions.copyjar")
    id("com.mineinabyss.conventions.publication")
    kotlin("plugin.serialization")
}

allprojects {
    apply(plugin = "java")

    repositories {
        mavenCentral()
        maven("https://erethon.de/repo/")
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://jitpack.io")
        maven("https://repo.codemc.io/repository/maven-snapshots/")
    }

    dependencies {
        // MineInAbyss platform
        compileOnly(Deps.kotlin.stdlib)
        compileOnly(Deps.kotlinx.serialization.json)
        compileOnly(Deps.kotlinx.serialization.kaml)
        compileOnly(Deps.kotlinx.coroutines)
        compileOnly(Deps.minecraft.skedule)

        compileOnly(Deps.exposed.core) { isTransitive = false }
        compileOnly(Deps.exposed.dao) { isTransitive = false }
        compileOnly(Deps.exposed.jdbc) { isTransitive = false }
        // TODO add to idofront platform
        implementation("org.jetbrains.exposed:exposed-java-time:0.33.1") { isTransitive = false }
        compileOnly(Deps.`sqlite-jdbc`)

        // Plugin deps
        compileOnly("com.mineinabyss:deeperworld:$deeperWorldVersion")
        compileOnly("com.mineinabyss:geary-platform-papermc:$gearyVersion")
        compileOnly("com.mineinabyss:geary-commons-papermc:$gearyAddonsVersion")
        compileOnly("com.mineinabyss:looty:$lootyVersion")
        compileOnly("com.github.MilkBowl:VaultAPI:1.7") { exclude(group = "org.bukkit") }
        compileOnly("com.mineinabyss:guiy-compose:0.1.4")
        compileOnly("nl.rutgerkok:blocklocker:1.10.2-SNAPSHOT")

        implementation("com.mineinabyss:idofront:$idofrontVersion")
        // TODO add to ido
        implementation("net.wesjd:anvilgui:1.5.3-SNAPSHOT")
    }
}

dependencies {

    // Shaded
    implementation(project(":mineinabyss-core"))
    implementation(project(":mineinabyss-systems"))
}
