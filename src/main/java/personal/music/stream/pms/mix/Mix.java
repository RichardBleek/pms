package personal.music.stream.pms.mix;

import java.util.List;

public class Mix {

    String id;
    String name;
    String imageUrl;
    String fullUrl;

    public Mix(String id, String name, String imageUrl, String fullUrl) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.fullUrl = fullUrl;
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
}
