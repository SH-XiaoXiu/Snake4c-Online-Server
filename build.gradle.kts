import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "cn.xiuxius.snake"
version = "1.0-SNAPSHOT"

java {

    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }

}

repositories {
    mavenCentral()
}


dependencies {
    implementation("io.netty:netty-all:4.2.1.Final")
    implementation("cn.hutool:hutool-json:5.8.38")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    compileOnly("org.projectlombok:lombok:1.18.32")
    annotationProcessor("org.projectlombok:lombok:1.18.32")
    implementation("ch.qos.logback:logback-classic:1.5.18")
    implementation("ch.qos.logback:logback-core:1.5.18")
    implementation("org.slf4j:slf4j-api:2.0.13")
}


tasks.test {
    useJUnitPlatform()
}

// ✅ 添加 shadowJar 配置
tasks.named<ShadowJar>("shadowJar") {
    archiveBaseName.set("snake-game")
    archiveClassifier.set("") // 不加 -all 后缀
    archiveVersion.set("1.0")
    // 替换为你的主类名
    manifest {
        attributes["Main-Class"] = "cn.xiuxius.snake.Application"
    }
    mergeServiceFiles() // 可选：处理 META-INF/services 冲突
}

// ✅ 设置构建默认生成 shadowJar 而非普通 jar（可选）
tasks.build {
    dependsOn(tasks.shadowJar)
}