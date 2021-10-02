package personal.music.stream.pms.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "pms.yt.downloader")
public class YouTubeDownloaderConfiguration {
    private String executable;
    private List<String> extraOptions = new ArrayList<>();

    public String getExecutable() {
        return executable;
    }

    public void setExecutable(String executable) {
        this.executable = executable;
    }

    public List<String> getExtraOptions() {
        return extraOptions;
    }
}
