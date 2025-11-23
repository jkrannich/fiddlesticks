package core.dto.status;

import core.enums.PublishLocation;

import java.util.List;

public record UpdateDto(
        int id,
        String author,
        boolean publish,
        List<PublishLocation> public_locations,
        List<ContentDto> translations,
        String created_at,
        String updated_at
) {
}
