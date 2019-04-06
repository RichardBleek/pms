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

    public static List<Mix> getDummy() {
        return List.of(new Mix("a","a", "http://localhost:8080/mixes/a.mp3", "http://localhost:8080/mixes/a.jpg"),
                new Mix("b","b", "http://localhost:8080/mixes/b.mp3", "http://localhost:8080/mixes/b.jpg"),
                new Mix("c","c", "http://localhost:8080/mixes/c.mp3", "http://localhost:8080/mixes/c.jpg"),
                new Mix("d","d", "http://localhost:8080/mixes/d.mp3", "http://localhost:8080/mixes/d.jpg"));
    }
}
