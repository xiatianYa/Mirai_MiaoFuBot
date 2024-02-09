plugins {
    val kotlinVersion = "1.8.10"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion

    id("net.mamoe.mirai-console") version "2.16.0"
}

group = "com.mao"
version = "0.1.0"

repositories {
    maven("https://maven.aliyun.com/repository/public")
    mavenCentral()
}
dependencies{
    // Baidu API
    implementation("com.baidu.aip:java-sdk:4.0.0")
    // FastJSON
    implementation("com.alibaba:fastjson:1.2.46")
    implementation("com.squareup.okhttp3:okhttp:3.8.1")
    implementation("org.json:json:20210307")
    implementation("org.yaml:snakeyaml:1.29")
}
mirai {
    jvmTarget = JavaVersion.VERSION_1_8
}