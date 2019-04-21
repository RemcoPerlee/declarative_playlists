package org.perlee.declarative_playlists.commands;

import java.util.List;

import lombok.Data;

@Data
public class CreatePlaylistCommand {
    private String name;
    private String artist;
    private List<String> artists;
    private String token;
}
