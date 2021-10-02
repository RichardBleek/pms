package personal.music.stream.pms.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "pms.yt.playlist")
public class YouTubePlaylistConfiguration {
    private List<String> urls = new ArrayList<>();

    public List<String> getUrls() {
        return urls;
    }
}
