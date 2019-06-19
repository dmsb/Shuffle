package com.example.shuffle.models.spotify.query_result;

import com.example.shuffle.models.spotify.Album;
import com.example.shuffle.models.spotify.Artist;
import com.example.shuffle.models.spotify.GenericRepresentation;
import com.example.shuffle.models.spotify.Track;

public class ItemResult <T extends GenericRepresentation> {

    private TypedItemResult<Artist> artists;
    private TypedItemResult<Track> tracks;
    private TypedItemResult<Album> albums;
    //private TypedItem<Playlist> playlists;

    public TypedItemResult<Artist> getArtists() {
        return artists;
    }

    public void setArtists(TypedItemResult<Artist> artists) {
        this.artists = artists;
    }

    public TypedItemResult<Track> getTracks() {
        return tracks;
    }

    public void setTracks(TypedItemResult<Track> tracks) {
        this.tracks = tracks;
    }

    public TypedItemResult<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(TypedItemResult<Album> albums) {
        this.albums = albums;
    }
}
