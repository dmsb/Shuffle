package com.example.shuffle.models.spotify.advanced_searchs;

import com.example.shuffle.models.spotify.Seed;
import com.example.shuffle.models.spotify.Track;

import java.util.List;

public class SeededRecommendation {

    private List<Track> tracks;
    private List<Seed> seeds;

    public List<Track> getTracks() {
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }

    public List<Seed> getSeeds() {
        return seeds;
    }

    public void setSeeds(List<Seed> seeds) {
        this.seeds = seeds;
    }
}
