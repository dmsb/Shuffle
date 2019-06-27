package com.example.shuffle.models.spotify.query_result;

import com.example.shuffle.models.spotify.Album;
import com.example.shuffle.models.spotify.Artist;
import com.example.shuffle.models.spotify.Playlist;
import com.example.shuffle.models.spotify.Track;

public class GenericTypedItemResult {

    private TypedItemResult<Track> tracks;
    private TypedItemResult<Artist> artists;
    private TypedItemResult<Album> albums;
    private TypedItemResult<Playlist> playlists;

    public TypedItemResult<Track> getTracks() {
        return tracks;
    }

    public void setTracks(TypedItemResult<Track> tracks) {
        this.tracks = tracks;
    }

    public TypedItemResult<Artist> getArtists() {
        return artists;
    }

    public void setArtists(TypedItemResult<Artist> artists) {
        this.artists = artists;
    }

    public TypedItemResult<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(TypedItemResult<Album> albums) {
        this.albums = albums;
    }

    public TypedItemResult<Playlist> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(TypedItemResult<Playlist> playlists) {
        this.playlists = playlists;
    }
}
