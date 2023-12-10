plugins {
    // id("java")
    id("application")
}

apply(plugin = "java")

group = "fr.bafbi.javaproject"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("io.javalin:javalin:5.6.3")
    implementation("com.j2html:j2html:1.6.0")
    implementation("org.webjars.npm:htmx.org:1.9.2")
    implementation("org.slf4j:slf4j-simple:2.0.7")
}

tasks {
    val copyCssFile by registering(Copy::class) {
        dependsOn(":css:build")
        from("css/build/src/project.css")
        into("build/resources/css")
    }
    compileJava {
        dependsOn(copyCssFile)
    }
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("fr.bafbi.javaproject.Main")
}

