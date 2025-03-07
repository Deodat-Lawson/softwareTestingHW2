plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val junitJupiterVersion = "5.11.3"
val jqwikVersion = "1.9.2"

tasks.named<JavaCompile>("compileTestJava") {
    options.compilerArgs.add("-parameters")
}

tasks.test {
    useJUnitPlatform {
        includeEngines("jqwik", "junit-jupiter")
    }

    // Update your include patterns to match your actual test class names
    include("**/*Properties.class")
    include("**/*Test.class")
    include("**/*Tests.class")
    include("**/*PBT.class")  // Add this to match your AuxiliaryMethodsPBT class
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:$junitJupiterVersion"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("net.jqwik:jqwik:$jqwikVersion")
}