package org.perlee.declarative_playlists.playlist;

import java.util.List;

import javax.validation.Valid;

import org.perlee.declarative_playlists.commands.CreatePlaylistCommand;
import org.perlee.declarative_playlists.model.Playlist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@CrossOrigin(origins = "http://localhost:3000")
@CrossOrigin
@RequestMapping("/api/playlists")
public class PlaylistController {

    private final PlaylistService service;

    @Autowired
    public PlaylistController(final PlaylistService service) {
        this.service = service;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Valid CreatePlaylistCommand command) {
        service.generatePlaylist(command.getName(), command.getArtist(), command.getArtists(), command.getToken());
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Playlist> findAll() {
        return service.findAll();
    }


}
