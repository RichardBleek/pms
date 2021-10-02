package personal.music.stream.pms.mix;

import java.util.Date;

public class Mix implements Comparable<Mix> {

    private String id;
    private String name;
    private String author;
    private String imageUrl;
    private String fullUrl;
    private String imageFile;
    private String audioFile;
    private Date publishedDate;

    public Mix(String id, String name, String author, String imageUrl, String fullUrl, String imageFile, String audioFile,
               Date publishedDate) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.imageUrl = imageUrl;
        this.fullUrl = fullUrl;
        this.imageFile = imageFile;
        this.audioFile = audioFile;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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

    public String getImageFile() {
        return imageFile;
    }

    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
    }

    public String getAudioFile() {
        return audioFile;
    }

    public void setAudioFile(String audioFile) {
        this.audioFile = audioFile;
    }

    public Date getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }

    @Override
    public int compareTo(Mix o) {
        return o.getPublishedDate().compareTo(publishedDate);
    }
}
