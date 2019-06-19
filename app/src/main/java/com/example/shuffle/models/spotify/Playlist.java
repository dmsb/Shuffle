package com.example.shuffle.models.spotify;

import com.google.gson.annotations.SerializedName;

public class Playlist extends GenericRepresentation {

    private Boolean collaborative;
    private String primary_color;

    @SerializedName("public")
    private Boolean isPublic;

    private String snapshot_id;
    private TracksLink tracks;

    private class TracksLink {

        private String href;
        private Integer total;

        public String getHref() {
            return href;
        }

        public void setHref(String href) {
            this.href = href;
        }

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }
    }

    public Boolean getCollaborative() {
        return collaborative;
    }

    public void setCollaborative(Boolean collaborative) {
        this.collaborative = collaborative;
    }

    public String getPrimary_color() {
        return primary_color;
    }

    public void setPrimary_color(String primary_color) {
        this.primary_color = primary_color;
    }

    public Boolean getPublic() {
        return isPublic;
    }

    public void setPublic(Boolean aPublic) {
        isPublic = aPublic;
    }

    public String getSnapshot_id() {
        return snapshot_id;
    }

    public void setSnapshot_id(String snapshot_id) {
        this.snapshot_id = snapshot_id;
    }

    public TracksLink getTracks() {
        return tracks;
    }

    public void setTracks(TracksLink tracks) {
        this.tracks = tracks;
    }
}
