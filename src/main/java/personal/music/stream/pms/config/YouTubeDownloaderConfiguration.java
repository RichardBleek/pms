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

	private final List<String> defaultOptions = List.of("--quality", "medium");

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
		executableBase.add("download");
		executableBase.add("-d");
		executableBase.add(filesFolder);
		executableBase.addAll(defaultOptions);
		executableBase.addAll(extraOptions);
		return executableBase;
	}
}
