package com.example.shuffle.models.spotify;

import java.time.LocalDate;
import java.util.List;

public class Album extends GenericRepresentation {

    private String album_type;
    private List<Artist> artists;
    private List<Image> images;
    private LocalDate release_date;
    private LocalDate release_date_precision;
    private Integer totalTracks;

    public String getAlbum_type() {
        return album_type;
    }

    public void setAlbum_type(String album_type) {
        this.album_type = album_type;
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public LocalDate getRelease_date() {
        return release_date;
    }

    public void setRelease_date(LocalDate release_date) {
        this.release_date = release_date;
    }

    public LocalDate getRelease_date_precision() {
        return release_date_precision;
    }

    public void setRelease_date_precision(LocalDate release_date_precision) {
        this.release_date_precision = release_date_precision;
    }

    public Integer getTotalTracks() {
        return totalTracks;
    }

    public void setTotalTracks(Integer totalTracks) {
        this.totalTracks = totalTracks;
    }
}
