package core.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import core.config.RiotApiConfig;
import core.error.RiotException;
import core.error.RiotNotFoundException;
import core.error.RiotRateLimitException;
import core.error.RiotServerException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Map;

public final class JavaNetRiotHttp implements RiotHttp {
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private final String apiKey;

    public JavaNetRiotHttp(final RiotApiConfig config) {
        this.httpClient = HttpClient.newBuilder().connectTimeout(config.timeout()).build();
        this.apiKey = config.apiKey();
    }

    @Override
    public <T> ApiResponse<T> get(final URI uri, final Class<T> type) {
        try {
            final HttpRequest request = HttpRequest.newBuilder(uri)
                    .timeout(Duration.ofSeconds(20))
                    .header("X-Riot-Token", apiKey)
                    .header("Accept", "application/json")
                    .GET().build();

            final HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            final int s = response.statusCode();
            final Map<String, List<String>> headers = response.headers().map();

            if (s == 200) {
                final T body = objectMapper.readValue(response.body(), type);
                return new ApiResponse<>(s, headers, body);
            } else {
                switch (s) {
                    case 400 -> throw new RiotException("Bad request calling" + uri);
                    case 401 -> throw new RiotException("Unauthorized calling " + uri);
                    case 404 -> throw new RiotNotFoundException("Not found calling" + uri);
                    case 429 -> throw RiotRateLimitException.fromHeaders(headers);
                    case 500, 502, 503, 504 -> throw new RiotServerException(s, uri.toString());
                    default -> throw new RiotException("Http error calling" + uri);
                }
            }
        } catch (final RiotException e) {
            throw e;
        } catch (final Exception e) {
            throw new RiotException("Error calling" + uri, e);
        }
    }
}
