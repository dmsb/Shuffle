package com.example.shuffle.models.spotify.query_result;

import com.example.shuffle.models.spotify.Adder;
import com.example.shuffle.models.spotify.Track;
import com.example.shuffle.models.spotify.VideoThumbnail;

public class TrackItem {

    private Track track;
    private String added_at;
    private Adder added_by;
    private Boolean is_local;
    private String primary_color;
    private VideoThumbnail video_thumbnail;

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }

    public String getAdded_at() {
        return added_at;
    }

    public void setAdded_at(String added_at) {
        this.added_at = added_at;
    }

    public Adder getAdded_by() {
        return added_by;
    }

    public void setAdded_by(Adder added_by) {
        this.added_by = added_by;
    }

    public Boolean getIs_local() {
        return is_local;
    }

    public void setIs_local(Boolean is_local) {
        this.is_local = is_local;
    }

    public String getPrimary_color() {
        return primary_color;
    }

    public void setPrimary_color(String primary_color) {
        this.primary_color = primary_color;
    }

    public VideoThumbnail getVideo_thumbnail() {
        return video_thumbnail;
    }

    public void setVideo_thumbnail(VideoThumbnail video_thumbnail) {
        this.video_thumbnail = video_thumbnail;
    }
}
