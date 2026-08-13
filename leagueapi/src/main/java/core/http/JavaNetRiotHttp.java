package core.http;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;
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
import java.util.List;
import java.util.Map;

/** Default synchronous transport. Retry and rate-limit policies intentionally live above this class. */
public final class JavaNetRiotHttp implements RiotHttp {
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper = JsonMapper.builder()
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .findAndAddModules()
            .build();
    private final String apiKey;
    private final java.time.Duration timeout;

    public JavaNetRiotHttp(final RiotApiConfig config) {
        this.httpClient = HttpClient.newBuilder().connectTimeout(config.getTimeout()).build();
        this.apiKey = config.getApiKey();
        this.timeout = config.getTimeout();
    }

    @Override
    public <T> ApiResponse<T> get(final URI uri, final Class<T> type) {
        return execute(uri, responseBody -> objectMapper.readValue(responseBody, type));
    }

    @Override
    public <T> ApiResponse<T> get(final URI uri, final TypeReference<T> type) {
        return execute(uri, responseBody -> objectMapper.readValue(responseBody, type));
    }

    private <T> ApiResponse<T> execute(final URI uri, final BodyParser<T> parser) {
        try {
            final HttpRequest request = HttpRequest.newBuilder(uri)
                    .timeout(timeout)
                    .header("X-Riot-Token", apiKey)
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            final HttpResponse<String> response = httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8)
            );
            final int status = response.statusCode();
            final Map<String, List<String>> headers = response.headers().map();

            if (status >= 200 && status < 300) {
                return new ApiResponse<>(status, headers, parser.parse(response.body()));
            }

            switch (status) {
                case 400 -> throw new RiotException("Bad request calling " + uri);
                case 401 -> throw new RiotException("Unauthorized calling " + uri);
                case 403 -> throw new RiotException("Forbidden calling " + uri);
                case 404 -> throw new RiotNotFoundException(uri.toString());
                case 429 -> throw RiotRateLimitException.fromHeaders(headers);
                case 500, 502, 503, 504 -> throw new RiotServerException(status, uri.toString());
                default -> throw new RiotException("HTTP error " + status + " calling " + uri);
            }
        } catch (final RiotException e) {
            throw e;
        } catch (final Exception e) {
            throw new RiotException("Error calling " + uri, e);
        }
    }

    @FunctionalInterface
    private interface BodyParser<T> {
        T parse(String responseBody) throws Exception;
    }
}
