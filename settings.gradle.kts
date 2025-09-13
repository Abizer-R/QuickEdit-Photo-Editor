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
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "QuickEdit"
include(":app-sample")
include(":quickedit")
include(":quickedit-core-engine")
include(":quickedit-ui")
include(":quickedit-tool-draw")
include(":quickedit-tool-text")
include(":quickedit-tool-crop")
