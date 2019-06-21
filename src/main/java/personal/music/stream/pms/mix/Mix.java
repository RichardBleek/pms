package personal.music.stream.pms.mix;

import java.util.Date;

public class Mix {

    private String id;
    private String name;
    private String imageUrl;
    private String fullUrl;
    private Date publishedDate;

    public Mix(String id, String name, String imageUrl, String fullUrl, Date publishedDate) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.fullUrl = fullUrl;
        this.publishedDate = publishedDate;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getFullUrl() {
        return fullUrl;
    }

    public void setFullUrl(String fullUrl) {
        this.fullUrl = fullUrl;
    }

    public Date getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }
}
