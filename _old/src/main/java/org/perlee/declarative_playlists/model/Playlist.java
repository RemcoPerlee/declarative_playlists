package org.perlee.declarative_playlists.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Playlist {
    String id;
    String name;
    String uri;
}
