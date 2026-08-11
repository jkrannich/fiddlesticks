# League API wrapper

The library is synchronous by design. It does not retry requests, wait on rate limits, or run requests concurrently; those policies stay with the application using it.

## Add the dependency

After the first Central release:

```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("io.github.jkrannich:leagueapi:0.1.0")
}
```

For local development, publish the current snapshot with `gradlew :leagueapi:publishToMavenLocal` and add `mavenLocal()` to the consuming project.

## Create a client

```java
RiotApi api = RiotApi.builder()
        .apiKey(System.getenv("RIOT_API_KEY"))
        .defaultPlatformRegion(Regions.PlatformRegion.EUW1)
        .defaultRegionalRoute(Regions.RegionalRoute.EUROPE)
        .build();
```

Defaults are `EUW1` and `EUROPE`, so a minimal client only needs an API key.

## Identity and match history

```java
AccountDto account = api.regional()
        .accounts()
        .byRiotId("GameName", "TAG");

List<String> matchIds = api.regional()
        .matches()
        .getMatchIdsByPuuid(
                Puuid.of(account.puuid()),
                MatchHistoryQuery.firstPage(20)
        );
```

Account and Match endpoints use a regional route. Summoner, ranked, mastery, spectator, status, challenges, champion rotation, and Clash endpoints use a platform region.

## Endpoint map

| Client | Routing | Main paths |
| --- | --- | --- |
| `accounts()` | Regional | `/riot/account/v1/accounts/by-riot-id/{gameName}/{tagLine}`, `/riot/account/v1/accounts/by-puuid/{puuid}` |
| `summoners()` | Platform | `/lol/summoner/v4/summoners/by-puuid/{puuid}`, `/lol/summoner/v4/summoners/{summonerId}` |
| `matches()` | Regional | `/lol/match/v5/matches/by-puuid/{puuid}/ids`, `/lol/match/v5/matches/{matchId}`, `/timeline` |
| `clash()` | Platform | `/lol/clash/v1/players/by-puuid/{puuid}`, `/teams/{teamId}`, `/tournaments`, `/tournaments/by-team/{teamId}`, `/tournaments/{tournamentId}` |
| `league()` | Platform | `/lol/league/v4/entries/by-puuid/{puuid}`, league and tier lookups |
| `championMastery()` | Platform | `/lol/champion-mastery/v4/...` |
| `spectator()` | Platform | `/lol/spectator/v5/active-games/by-summoner/{puuid}` |
| `status()` | Platform | `/lol/status/v4/platform-data` |

## Clash endpoints

```java
List<PlayerDto> players = api.platform()
        .clash()
        .getPlayersByPuuid(account.puuid());

List<TournamentDto> tournaments = api.platform()
        .clash()
        .getAllActiveOrUpcomingTournaments();
```

Available Clash operations are player lookup, team lookup, active/upcoming tournament lookup, tournament-by-team lookup, and tournament-by-ID lookup.

## Testing

Normal tests never call Riot. Live tests are tagged `integration` and can be enabled explicitly:

```text
gradlew :leagueapi:test -PincludeIntegrationTests
```

Set `RIOT_API_KEY` in the environment or `.env` before enabling them.

See [`RELEASING.md`](../RELEASING.md) for Central Portal publishing and signing setup.
