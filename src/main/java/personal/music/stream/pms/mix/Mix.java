package personal.music.stream.pms.mix;

import reactor.core.publisher.Flux;

import java.util.List;

public class Mix {

    String name;
    String imageUrl;
    String fullUrl;

    public Mix(String name, String imageUrl, String fullUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.fullUrl = fullUrl;
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
        return List.of(new Mix("a", "http://localhost:8080/mixes/a.mp3", "http://localhost:8080/mixes/a.jpg"),
                new Mix("b", "http://localhost:8080/mixes/b.mp3", "http://localhost:8080/mixes/b.jpg"),
                new Mix("c", "http://localhost:8080/mixes/c.mp3", "http://localhost:8080/mixes/c.jpg"),
                new Mix("d", "http://localhost:8080/mixes/d.mp3", "http://localhost:8080/mixes/d.jpg"));
    }
}
