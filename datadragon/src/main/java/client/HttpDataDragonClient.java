package client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.ChampionDetailsDto;
import dto.ChampionDto;
import dto.ChampionsIndexDto;
import dto.ItemsIndexDto;
import dto.RealmDto;
import dto.RuneTreeDto;
import dto.SummonerSpellsDto;
import util.HttpUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class HttpDataDragonClient implements DataDragonClient {

    private static final String BASE = "https://ddragon.leagueoflegends.com";

    private final DataDragonTransport transport;
    private final ObjectMapper objectMapper;

    public HttpDataDragonClient() {
        this(HttpUtils::getBytes);
    }

    /** Creates a client with a custom transport, useful for tests and advanced integrations. */
    public HttpDataDragonClient(final DataDragonTransport transport) {
        this.transport = Objects.requireNonNull(transport, "transport");
        this.objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .findAndRegisterModules();
    }

    @Override
    public List<String> versions() {
        return readArray(path("api", "versions.json"), String[].class);
    }

    @Override
    public String latestVersion() {
        final List<String> versions = versions();
        return versions.isEmpty() ? null : versions.getFirst();
    }

    @Override
    public List<String> languages() {
        return readArray(path("cdn", "languages.json"), String[].class);
    }

    @Override
    public RealmDto realm(final String platform) {
        return read(path("realms", platform + ".json"), RealmDto.class);
    }

    @Override
    public URI patchArchive(final String version) {
        return path("cdn", "dragontail-" + version + ".tgz");
    }

    @Override
    public byte[] downloadPatchArchive(final String version) {
        return transport.get(patchArchive(version));
    }

    @Override
    public ChampionsIndexDto champions(final String version, final String locale) {
        return read(
                path("cdn", version, "data", locale, "champion.json"),
                ChampionsIndexDto.class
        );
    }

    @Override
    public ChampionDetailsDto championDetails(
            final String version,
            final String locale,
            final String championId
    ) {
        return read(
                path("cdn", version, "data", locale, "champion", championId + ".json"),
                ChampionDetailsDto.class
        );
    }

    @Override
    public ChampionDto champion(
            final String version,
            final String locale,
            final String championId
    ) {
        final ChampionDetailsDto details = championDetails(version, locale, championId);
        final ChampionDto champion = details.data().get(championId);
        if (champion != null) {
            return champion;
        }
        if (details.data().size() == 1) {
            return details.data().values().iterator().next();
        }
        throw new IllegalArgumentException(
                "Champion response did not contain '" + championId + "'"
        );
    }

    @Override
    public URI championSquare(final String version, final String championId) {
        return versionedAsset(version, "champion", championId + ".png");
    }

    @Override
    public URI championLoading(final String version, final String championId) {
        return unversionedAsset("img/champion/loading/" + championId + "_0.jpg");
    }

    @Override
    public URI championSplash(final String championId, final int skinNumber) {
        return unversionedAsset("img/champion/splash/" + championId + "_" + skinNumber + ".jpg");
    }

    @Override
    public URI championPassive(final String version, final String imageFile) {
        return versionedAsset(version, "passive", imageFile);
    }

    @Override
    public URI championSpell(final String version, final String imageFile) {
        return versionedAsset(version, "spell", imageFile);
    }

    @Override
    public URI sprite(final String version, final String spriteFile) {
        return versionedAsset(version, "sprite", spriteFile);
    }

    @Override
    public URI versionedAsset(
            final String version,
            final String directory,
            final String fileName
    ) {
        return path("cdn", version, "img", directory, fileName);
    }

    @Override
    public ItemsIndexDto items(final String version, final String locale) {
        return read(
                path("cdn", version, "data", locale, "item.json"),
                ItemsIndexDto.class
        );
    }

    @Override
    public URI itemIcon(final String version, final String itemId) {
        return versionedAsset(version, "item", itemId + ".png");
    }

    @Override
    public SummonerSpellsDto summonerSpells(final String version, final String locale) {
        return read(
                path("cdn", version, "data", locale, "summoner.json"),
                SummonerSpellsDto.class
        );
    }

    @Override
    public URI summonerSpellIcon(final String version, final String spellId) {
        return versionedAsset(version, "spell", spellId + ".png");
    }

    @Override
    public List<RuneTreeDto> runes(final String version, final String locale) {
        return read(
                path("cdn", version, "data", locale, "runesReforged.json"),
                new TypeReference<>() {
                }
        );
    }

    @Override
    public URI runeIcon(final String iconPath) {
        return unversionedAsset("img/" + iconPath);
    }

    @Override
    public URI profileIcon(final String version, final int iconId) {
        return versionedAsset(version, "profileicon", iconId + ".png");
    }

    private URI unversionedAsset(final String relativePath) {
        final String normalized = relativePath.startsWith("/")
                ? relativePath.substring(1)
                : relativePath;
        return URI.create(BASE + "/cdn/" + encodePath(normalized));
    }

    private URI path(final String... segments) {
        final String relativePath = Arrays.stream(segments)
                .map(HttpDataDragonClient::encodeSegment)
                .collect(Collectors.joining("/"));
        return URI.create(BASE + "/" + relativePath);
    }

    private <T> T read(final URI uri, final Class<T> type) {
        try {
            return objectMapper.readValue(transport.get(uri), type);
        } catch (final IOException e) {
            throw new RuntimeException("Could not parse JSON from " + uri, e);
        }
    }

    private <T> T read(final URI uri, final TypeReference<T> type) {
        try {
            return objectMapper.readValue(transport.get(uri), type);
        } catch (final IOException e) {
            throw new RuntimeException("Could not parse JSON from " + uri, e);
        }
    }

    private <T> List<T> readArray(final URI uri, final Class<T[]> type) {
        final T[] values = read(uri, type);
        return List.copyOf(Arrays.asList(values));
    }

    private static String encodeSegment(final String value) {
        return URLEncoder.encode(
                        Objects.requireNonNull(value, "Data Dragon path segment"),
                        StandardCharsets.UTF_8
                )
                .replace("+", "%20");
    }

    private static String encodePath(final String value) {
        return Arrays.stream(Objects.requireNonNull(value, "Data Dragon asset path").split("/"))
                .map(HttpDataDragonClient::encodeSegment)
                .collect(Collectors.joining("/"));
    }
}
