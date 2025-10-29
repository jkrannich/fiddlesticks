package core.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import core.config.RiotApiConfig;
import core.error.RiotException;

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
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String apiKey;

    public JavaNetRiotHttp(RiotApiConfig config) {
        this.httpClient = HttpClient.newBuilder().connectTimeout(config.timeout()).build();
        this.apiKey = config.apiKey();
    }

    @Override
    public <T> ApiResponse<T> get(URI uri, Class<T> type) {
        try {
            HttpRequest request = HttpRequest.newBuilder(uri)
                    .timeout(Duration.ofSeconds(10))
                    .header("X-Riot-Token", apiKey)
                    .header("Accept", "application/json")
                    .GET().build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            int s = response.statusCode();
            Map<String, List<String>> headers = response.headers().map();

            if (s == 200) {
                T body = objectMapper.readValue(response.body(), type);
                return new ApiResponse<>(s, headers, body);
            }
        } catch (Exception e) {
            throw new RiotException("Http error calling" + uri, e);
        }
    }
}
