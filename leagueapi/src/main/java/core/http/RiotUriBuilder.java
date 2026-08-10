package core.http;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/** Small URI helper that correctly encodes Riot IDs and query parameters. */
public final class RiotUriBuilder {
    private RiotUriBuilder() {
    }

    public static URI path(final String baseUrl, final String... segments) {
        Objects.requireNonNull(baseUrl, "baseUrl");
        final String path = List.of(segments).stream()
                .map(RiotUriBuilder::encode)
                .collect(Collectors.joining("/"));
        return URI.create(baseUrl + (baseUrl.endsWith("/") ? "" : "/") + path);
    }

    public static URI pathAndQuery(
            final String baseUrl,
            final List<String> segments,
            final Map<String, String> queryParameters
    ) {
        final URI path = path(baseUrl, segments.toArray(String[]::new));
        if (queryParameters.isEmpty()) {
            return path;
        }

        final String query = queryParameters.entrySet().stream()
                .map(entry -> encode(entry.getKey()) + "=" + encode(entry.getValue()))
                .collect(Collectors.joining("&"));
        return URI.create(path + "?" + query);
    }

    private static String encode(final String value) {
        return URLEncoder.encode(Objects.requireNonNull(value, "URI value"), StandardCharsets.UTF_8)
                .replace("+", "%20");
    }
}
