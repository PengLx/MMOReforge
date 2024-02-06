plugins {
    `java-library`
    `maven-publish`
    id("io.izzel.taboolib") version "1.42"
    id("org.jetbrains.kotlin.jvm") version "1.5.10"
}

taboolib {
    description {
        contributors {
            name("Peng_Lx")
        }
        dependencies {
            name("MMOItems")
            name("Vault").loadafter(true)
            name("MythicLib")
        }
    }
    install("common")
    install("common-5")
    install("module-ui")
    install("module-configuration")
    install("module-lang")
    install("platform-bukkit")
    install("expansion-command-helper")
    classifier = null
    version = "6.0.9-114"
}

repositories {
    mavenCentral()
    maven("https://nexus.phoenixdvpt.fr/repository/maven-public/")
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("ink.ptms:nms-all:1.0.0")
    compileOnly("ink.ptms.core:v11902:11902-minimize:mapped")
    compileOnly("ink.ptms.core:v11902:11902-minimize:universal")
    compileOnly("net.Indyuce:MMOItems-API:6.7.5-SNAPSHOT")
    compileOnly("io.lumine:MythicLib-dist:1.3.4-SNAPSHOT")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7.1")
    compileOnly(kotlin("stdlib"))
    compileOnly(fileTree("libs"))
    implementation("org.apache.commons:commons-lang3:3.12.0")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf("-Xjvm-default=all")
    }
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

publishing {
    repositories {
        maven {
            url = uri("https://repo.tabooproject.org/repository/releases")
            credentials {
                username = project.findProperty("taboolibUsername").toString()
                password = project.findProperty("taboolibPassword").toString()
            }
            authentication {
                create<BasicAuthentication>("basic")
            }
        }
    }
    publications {
        create<MavenPublication>("library") {
            from(components["java"])
            groupId = project.group.toString()
        }
    }
}