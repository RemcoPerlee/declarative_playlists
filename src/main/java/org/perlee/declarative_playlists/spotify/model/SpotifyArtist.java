package org.perlee.declarative_playlists.spotify.model;

import java.util.List;

import lombok.Data;

@Data
public class SpotifyArtist {
    private String id;
    private String href;
    private String name;
    private int popularity;
    private String uri;
    private List<String> genres;
}
