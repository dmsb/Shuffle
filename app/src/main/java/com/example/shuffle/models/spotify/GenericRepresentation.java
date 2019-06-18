package com.example.shuffle.models.spotify;

public abstract class GenericRepresentation {

    private String href;
    private String id;
    private String name;
    private String type;
    private String uri;
    private ExternalURL externalURL;

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public ExternalURL getExternalURL() {
        return externalURL;
    }

    public void setExternalURL(ExternalURL externalURL) {
        this.externalURL = externalURL;
    }
}
