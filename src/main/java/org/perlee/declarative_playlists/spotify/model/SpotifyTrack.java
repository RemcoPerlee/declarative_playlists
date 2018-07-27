package org.perlee.declarative_playlists.spotify.model;

import lombok.Data;

@Data
public class SpotifyTrack {
    String href;
    String id;
    String name;
    int popularity;
    String preview_url;
    int track_number;
    String type;
    String uri;
}
