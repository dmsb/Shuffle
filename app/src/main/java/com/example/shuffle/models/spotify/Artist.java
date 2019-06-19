package com.example.shuffle.models.spotify;

import java.util.List;

public class Artist extends GenericRepresentation {

    private Follower followers;
    private List<String> geners;
    private List<Image> images;
    private Integer popularity;

    public Follower getFollowers() {
        return followers;
    }

    public void setFollowers(Follower followers) {
        this.followers = followers;
    }

    public List<String> getGeners() {
        return geners;
    }

    public void setGeners(List<String> geners) {
        this.geners = geners;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public Integer getPopularity() {
        return popularity;
    }

    public void setPopularity(Integer popularity) {
        this.popularity = popularity;
    }
}
