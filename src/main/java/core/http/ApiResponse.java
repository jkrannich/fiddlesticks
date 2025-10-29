package core.http;

import java.util.List;
import java.util.Map;

public record ApiResponse<T>(
        int status,
        Map<String, List<String>> headers,
        T body
) {}
