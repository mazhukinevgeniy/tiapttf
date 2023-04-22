plugins {
    id("java")
    kotlin("jvm") version "1.8.20"
}

tasks.withType<JavaCompile> {
    val compilerArgs = options.compilerArgs
    compilerArgs.addAll(listOf("-encoding", "UTF-8"))
}

group = "homemade"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.8.20")
    implementation(project(":game-model-v2"))
    implementation(project(":field-structure"))
    implementation(project(":game-loop"))
    implementation(project(":game-serializations"))
    implementation(project(":common-legacy"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(11)
}
