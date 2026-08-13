# Fiddlesticks

Fiddlesticks is a set of synchronous Java clients for Riot Games APIs. The repository publishes the modules independently, while keeping their versions aligned for releases.

## Modules

| Module | Maven coordinate | Description |
| --- | --- | --- |
| [League API](leagueapi/README.md) | `io.github.jkrannich:leagueapi` | Riot Games League of Legends API client |
| [Data Dragon](datadragon/README.md) | `io.github.jkrannich:datadragon` | Data Dragon metadata, JSON, and asset client |

Both modules target Java 26 and use Jackson 3 for JSON mapping. Jackson's annotation module remains on its `com.fasterxml.jackson` namespace as required by Jackson 3.

## Installation

After a release is available on Maven Central, use the same version for whichever modules your application needs:

```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("io.github.jkrannich:leagueapi:<version>")
    implementation("io.github.jkrannich:datadragon:<version>")
}
```

The modules are independent; an application can depend on either one without depending on the other.

## Quick start

### League API

League API calls require a Riot API key:

```java
RiotApi api = RiotApi.builder()
        .apiKey(System.getenv("RIOT_API_KEY"))
        .defaultPlatformRegion(Regions.PlatformRegion.EUW1)
        .defaultRegionalRoute(Regions.RegionalRoute.EUROPE)
        .build();

AccountDto account = api.regional()
        .accounts()
        .byRiotId("GameName", "TAG");
```

See the [League API documentation](leagueapi/README.md) for routing, endpoint clients, Clash, match history, and integration tests.

### Data Dragon

Data Dragon is public and does not require an API key:

```java
DataDragonClient dataDragon = new HttpDataDragonClient();
String version = dataDragon.latestVersion();

ChampionsIndexDto champions = dataDragon.champions(version, "en_US");
URI ahriIcon = dataDragon.championSquare(version, "Ahri");
```

See the [Data Dragon documentation](datadragon/README.md) for champion, item, rune, summoner spell, profile icon, and patch asset endpoints.

## Build and test

Run the normal unit tests and build both modules from the repository root:

```text
./gradlew :leagueapi:test :datadragon:test
./gradlew :leagueapi:build :datadragon:build
```

League API integration tests are opt-in and require `RIOT_API_KEY`:

```text
./gradlew :leagueapi:test -PincludeIntegrationTests
```

For local consumer testing, publish both modules to Maven Local:

```text
./gradlew :leagueapi:publishToMavenLocal :datadragon:publishToMavenLocal
```

See [RELEASING.md](RELEASING.md) for Central Portal credentials, signing, tags, and the combined release workflow.
