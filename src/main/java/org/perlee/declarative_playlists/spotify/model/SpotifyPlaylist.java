package org.perlee.declarative_playlists.spotify.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class SpotifyPlaylist {
    boolean collaborative;
    String description;
    String href;
    String id;
    String name;
    @JsonProperty("public") boolean playlist_public;
    String snapshot_id;
    String type;
    String uri;
}
