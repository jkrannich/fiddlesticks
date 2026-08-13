package core.http;

import tools.jackson.core.type.TypeReference;

import java.net.URI;

public interface RiotHttp {
    <T> ApiResponse<T> get(URI uri, Class<T> type);

    <T> ApiResponse<T> get(URI uri, TypeReference<T> type);
}
