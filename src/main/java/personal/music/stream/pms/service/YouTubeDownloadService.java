package personal.music.stream.pms.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import personal.music.stream.pms.config.YouTubeDownloaderConfiguration;
import personal.music.stream.pms.config.YouTubePlaylistConfiguration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class YouTubeDownloadService {

    private static final int TIMEOUT = 15;

    private final Logger log = LoggerFactory.getLogger(YouTubeDownloadService.class);
    private final YouTubePlaylistConfiguration playLists;
    private final YouTubeDownloaderConfiguration configuration;

    @Autowired
    public YouTubeDownloadService(final YouTubePlaylistConfiguration playLists, final YouTubeDownloaderConfiguration configuration) {
        this.playLists = playLists;
        this.configuration = configuration;
    }

    public void downloadPlaylists() {
        this.downloadPlaylists(false);
    }

    public void downloadPlaylists(boolean simulate) {
        playLists.getUrls().forEach(url -> this.download(url, simulate));
    }

    private void download(final String url, boolean simulate) {
        try {
            final List<String> executable = configuration.getExecutableBase(simulate);
            executable.add(url);
            final ProcessBuilder builder = new ProcessBuilder(executable).redirectErrorStream(true);
            final Process process = builder.start();
            logOutput(process);
            waitForFinish(process);
        } catch (IOException | InterruptedException e) {
            log.error("YouTube downloader failed: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void waitForFinish(final Process process) throws InterruptedException {
        boolean hasProgramExitedSuccesfully = process.waitFor(TIMEOUT, TimeUnit.SECONDS);
        if (hasProgramExitedSuccesfully) {
            log.info("Playlists succesvol gedownload");
        } else {
            process.destroy();
            log.warn("Applicatie deed er langer over dan {} seconden om af te ronden", TIMEOUT);
        }
    }

    private void logOutput(final Process process) throws IOException {
        try (final BufferedReader lineReader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            lineReader.lines().forEach(log::info);
        }
    }
}
