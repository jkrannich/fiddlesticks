# Fiddlesticks Data Dragon

[Back to the project README](../README.md)

The Data Dragon module is a synchronous Java client for Riot Games Data Dragon. It provides typed JSON responses and URI helpers for Data Dragon's versioned assets.

## Add the dependency

After a release is available on Maven Central:

```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("io.github.jkrannich:datadragon:<version>")
}
```

For local development, publish it with `./gradlew :datadragon:publishToMavenLocal` and add `mavenLocal()` to the consuming project.

## Create a client

Data Dragon endpoints are public and do not require a Riot API key:

```java
DataDragonClient dataDragon = new HttpDataDragonClient();

String version = dataDragon.latestVersion();
ChampionsIndexDto champions = dataDragon.champions(version, "en_US");
ChampionDetailsDto ahri = dataDragon.championDetails(version, "en_US", "Ahri");
URI ahriIcon = dataDragon.championSquare(version, "Ahri");
```

The client also supports an injected `DataDragonTransport`, which is useful for tests or applications that provide their own HTTP layer:

```java
DataDragonClient dataDragon = new HttpDataDragonClient(uri -> {
    // Return the bytes for uri from the application's HTTP stack.
    return loadBytes(uri);
});
```

## Endpoint groups

| Group | Operations |
| --- | --- |
| Metadata | Versions, latest version, languages, and platform realm |
| Champions | Index, details, square/loading/splash images, passive and spell assets |
| Items | Item index and item icons |
| Summoner spells | Spell index and spell icons |
| Runes | Rune trees and rune icons |
| Profile icons | Versioned profile icon URIs |
| Patch archives | Patch archive URI and download |

Versioned JSON methods accept a Data Dragon version and locale such as `14.24.1` and `en_US`. Asset methods return `URI` values so the application can download or cache them using its preferred HTTP client.

## Testing

Data Dragon tests use an injected transport and do not call Riot services:

```text
./gradlew :datadragon:test
```
