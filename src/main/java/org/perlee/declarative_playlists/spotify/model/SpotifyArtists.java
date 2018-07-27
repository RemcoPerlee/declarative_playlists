package org.perlee.declarative_playlists.spotify.model;

import java.util.List;

import lombok.Data;

@Data
public class SpotifyArtists {
    List<SpotifyArtist> items;
}
