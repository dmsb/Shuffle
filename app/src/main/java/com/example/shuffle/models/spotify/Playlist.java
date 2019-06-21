package com.example.shuffle.models.spotify;

import com.example.shuffle.models.spotify.query_result.TrackItem;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Playlist extends GenericRepresentation {

    private Boolean collaborative;
    private String primary_color;
    private String description;
    private Follower followers;
    private Owner owner;
    private List<Image> images;

    @SerializedName("public")
    private Boolean isPublic;

    private String snapshot_id;
    private TracksLink tracks;

    public class TracksLink {

        private String href;
        private Integer limit;
        private String next;
        private Integer offset;
        private String previous;
        private Integer total;
        private List<TrackItem> items;

        public String getHref() {
            return href;
        }

        public void setHref(String href) {
            this.href = href;
        }

        public Integer getLimit() {
            return limit;
        }

        public void setLimit(Integer limit) {
            this.limit = limit;
        }

        public String getNext() {
            return next;
        }

        public void setNext(String next) {
            this.next = next;
        }

        public Integer getOffset() {
            return offset;
        }

        public void setOffset(Integer offset) {
            this.offset = offset;
        }

        public String getPrevious() {
            return previous;
        }

        public void setPrevious(String previous) {
            this.previous = previous;
        }

        public Integer getTotal() {
            return total;
        }

        public List<TrackItem> getItems() {
            return items;
        }

        public void setItems(List<TrackItem> items) {
            this.items = items;
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

    public void setPublic(Boolean isPublic) {
        this.isPublic = isPublic;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Follower getFollowers() {
        return followers;
    }

    public void setFollowers(Follower followers) {
        this.followers = followers;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }
}
