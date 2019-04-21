package org.perlee.declarative_playlists.playlist;

import java.util.ArrayList;
import java.util.List;

import org.perlee.declarative_playlists.Application;
import org.perlee.declarative_playlists.exceptions.ArtistNotFoundException;
import org.perlee.declarative_playlists.model.Playlist;
import org.perlee.declarative_playlists.model.Track;
import org.perlee.declarative_playlists.spotify.commands.SpotifyCommandCreatePlaylist;
import org.perlee.declarative_playlists.spotify.model.SpotifyPlaylist;
import org.perlee.declarative_playlists.spotify.model.SpotifySnapshot;
import org.perlee.declarative_playlists.spotify.model.SpotifyTrack;
import org.perlee.declarative_playlists.spotify.model.SpotifyTracks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.client.RestTemplate;

@Service
public class PlaylistService {
    private static final String artistTopTracksURL = "https://api.spotify.com/v1/artists/%s/top-tracks?country=%s&limit=%d";
    private static final String createPlaylistURL = "https://api.spotify.com/v1/users/remcoperlee/playlists";
    private static final String addTrackToPlaylistURL = "https://api.spotify.com/v1/users/remcoperlee/playlists/%s/tracks?uris=%s";

    private static final Logger log = LoggerFactory.getLogger(Application.class);
    private static final int topnumber = 5;
    private static final String country = "NL";

    public void generatePlaylist(String name, String artistURI, List<String> artistURIs, String token){
        log.info(String.format("Creating playlist for: %s %s %s", name, artistURIs, token));

        List<Track> tracks = null;

        if (artistURI.isEmpty()) {
            tracks = getArtistsTopTracks(artistURIs);
        } else {
            tracks = getArtistTopTracks(artistURI);
        }

        Playlist playlist = createPlayList(name, token);
        addTracksToPlaylist(playlist, tracks, token);

    }

    public List<Playlist> findAll(){
        List<Playlist> list = new ArrayList<Playlist>();

        list.add(Playlist.builder().id("1").name("Nummer 1").build());
        list.add(Playlist.builder().id("2").name("Nummer 2").build());
        list.add(Playlist.builder().id("3").name("Nummer 3").build());

        log.info("getall");

        return list;
    }

    private static List<Track> getArtistsTopTracks(final List<String> artistURIs) {
        List<Track> tracks = new ArrayList<Track>();

        // find each artist based on URI
        for (String artistURI : artistURIs) {
            List<Track> artistTracks = getArtistTopTracks(artistURI);

            for (Track track : artistTracks) {
                tracks.add(track);
            }
        }

        return tracks;
    }

    private static List<Track> getArtistTopTracks(final String artistURI) {
        List<Track> tracks = new ArrayList<Track>();

        try {
            String artistId = ArtistUriToId(artistURI);

            String url = String.format(artistTopTracksURL, artistId, country, topnumber);
            RestTemplate restTemplate = new RestTemplate();
            SpotifyTracks spotifyTracks = restTemplate.getForObject(url, SpotifyTracks.class);

            int i = 0;
            for (SpotifyTrack spotifyTrack : spotifyTracks.getTracks()) {
                i++;
                if (i <= topnumber) {
                    Track track = Track.builder()
                            .id(spotifyTrack.getId())
                            .spotifyUri(spotifyTrack.getUri())
                            .build();
                    tracks.add(track);
                }
            }
        } catch (ArtistNotFoundException e) {
            log.warn(String.format("The artist with URI %s cannot be translated to a spotifyID. The artist is skipped.", artistURI));
        }

        return tracks;
    }

    private static String ArtistUriToId(String Uri) throws ArtistNotFoundException {
        String[] parts = Uri.split(":");

        if (parts.length == 3) {
            return parts[2];
        } else {
            throw new ArtistNotFoundException("");
        }
    }

    private static Playlist createPlayList(String name, String token) {
        RestTemplate restTemplate = new RestTemplate();

        SpotifyCommandCreatePlaylist command = SpotifyCommandCreatePlaylist.builder()
                .name(name)
                .playlist_public(true)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);

        String url = String.format(createPlaylistURL);

        HttpEntity<?> httpEntity = new HttpEntity<Object>(command, headers);
        SpotifyPlaylist spotifyPlaylist = restTemplate.postForObject(url, httpEntity, SpotifyPlaylist.class);

        Playlist playlist = Playlist.builder()
                                .id(spotifyPlaylist.getId())
                                .name(spotifyPlaylist.getName())
                                .build();
        return playlist;
    }

    private static void addTracksToPlaylist (Playlist playlist, List<Track> tracks, String token) {
        for (Track track : tracks) {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + token);

            String url = String.format(addTrackToPlaylistURL, playlist.getId(), track.getSpotifyUri());

            HttpEntity<?> httpEntity = new HttpEntity<Object>(headers);
            SpotifySnapshot snapshot = restTemplate.postForObject(url, httpEntity, SpotifySnapshot.class);
        }
    }

}
