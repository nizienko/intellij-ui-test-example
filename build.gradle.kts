import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.10"
    id("org.jetbrains.intellij") version "0.5.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

val remoteRobotVersion = "0.10.0"

// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
    version = "2020.2.3"
//    localPath = "/home/def/idea-IC-211.90/"
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

repositories {
    mavenCentral()
    jcenter()
    maven("https://jetbrains.bintray.com/intellij-third-party-dependencies")
}

tasks.getByName<org.jetbrains.intellij.tasks.DownloadRobotServerPluginTask>("downloadRobotServerPlugin") {
    version = remoteRobotVersion
}

tasks.getByName<org.jetbrains.intellij.tasks.PatchPluginXmlTask>("patchPluginXml") {
    changeNotes("""
      Add change notes here.<br>
      <em>most HTML tags may be used</em>""")
}

tasks.getByName<org.jetbrains.intellij.tasks.RunIdeForUiTestTask>("runIdeForUiTests") {
    systemProperty("robot-server.port", "8082")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
dependencies {
    testImplementation("com.intellij.remoterobot:remote-robot:$remoteRobotVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.3.1")
    testRuntimeOnly ("org.junit.jupiter:junit-jupiter-engine:5.3.1")
}