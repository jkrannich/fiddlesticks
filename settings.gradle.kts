pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

plugins {
    id("com.gradleup.nmcp.settings") version "1.6.1"
}

rootProject.name = "fiddlesticks"
include("datadragon")
include("leagueapi")

nmcpSettings {
    centralPortal {
        username = providers.gradleProperty("centralPortalUsername")
            .orElse(providers.environmentVariable("CENTRAL_PORTAL_USERNAME"))
            .getOrElse("")
        password = providers.gradleProperty("centralPortalPassword")
            .orElse(providers.environmentVariable("CENTRAL_PORTAL_PASSWORD"))
            .getOrElse("")

        // Keep the first release manually releasable from the Central Portal.
        publishingType = "USER_MANAGED"
        publicationName = "fiddlesticks"
    }
}
