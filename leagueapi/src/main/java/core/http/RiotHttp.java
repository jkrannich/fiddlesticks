package core.http;

import java.net.URI;

public interface RiotHttp {
    <T> ApiResponse<T> get(URI uri, Class<T> type);
}
