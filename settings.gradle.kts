pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        repositories {
            maven { url  = uri("https://devrepo.kakao.com/nexus/repository/kakaomap-releases/") }
        }
    }
}

rootProject.name = "Capstone"
include(":app")
