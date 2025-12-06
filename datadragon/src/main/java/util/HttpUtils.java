package util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Map;

public final class HttpUtils {

    private static final HttpClient HTTP = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .findAndRegisterModules();

    private HttpUtils() {}

    public static <T> T read(String url, Class<T> type) {
        String body = get(URI.create(url));
        try {
            return MAPPER.readValue(body, type);
        } catch (IOException e) {
            throw new RuntimeException("Could not parse JSON from " + url, e);
        }
    }

    public static <T> List<T> readArray(String url, Class<T[]> type) {
        String body = get(URI.create(url));
        try {
            return List.of(MAPPER.readValue(body, type));
        } catch (IOException e) {
            throw new RuntimeException("Failed reading array from " + url, e);
        }
    }

    public static Map<String, Object> readMap(String url) {
        String body = get(URI.create(url));
        try {
            return MAPPER.readValue(body, new TypeReference<>() {
            });
        } catch (IOException e) {
            throw new RuntimeException("Failed reading map from " + url, e);
        }
    }

    public static String get(URI uri) {
        try {
            HttpRequest request = HttpRequest.newBuilder(uri)
                    .timeout(Duration.ofSeconds(10))
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = HTTP.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new RuntimeException("HTPP " + response.statusCode() + " for " + uri);
            }

            return response.body();
        } catch (Exception e) {
            throw new RuntimeException("Failed GET " + uri, e);
        }
    }

    public static List<Object> readList(String url) {
        String body = get(URI.create(url));
        try {
            return MAPPER.readValue(body, new TypeReference<>() {
            });
        } catch (IOException e) {
            throw new RuntimeException("Failed reading list from " + url, e);
        }
    }
}
