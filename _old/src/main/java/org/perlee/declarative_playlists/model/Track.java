package org.perlee.declarative_playlists.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Track {
    String id;
    String spotifyUri;
}
