package personal.music.stream.pms.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "pms.yt.downloader")
public class YouTubeDownloaderConfiguration {

    private final String filesFolder;

    @Autowired
    public YouTubeDownloaderConfiguration(final String filesFolder) {
        this.filesFolder = filesFolder;
    }

    private final List<String> defaultOptions = List.of("-k", "-x", "--audio-format", "m4a", "--write-thumbnail", "--yes-playlist", "--format", "best", "--write-info-json");

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

    public List<String> getExecutableBase(boolean simulate) {
        final List<String> executableBase = new ArrayList<>();
        executableBase.add(executable);
        executableBase.addAll(List.of("-o", filesFolder + "/%(title)s.%(ext)s"));
        executableBase.addAll(defaultOptions);
        if(simulate) {
            executableBase.add("--simulate");
        }
        executableBase.addAll(extraOptions);
        return executableBase;
    }
}
