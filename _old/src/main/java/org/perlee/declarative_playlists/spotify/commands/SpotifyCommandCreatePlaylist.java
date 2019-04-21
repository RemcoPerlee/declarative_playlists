package org.perlee.declarative_playlists.spotify.commands;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SpotifyCommandCreatePlaylist {
    String name;
    @JsonProperty("public") boolean playlist_public;
}
