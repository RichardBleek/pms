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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class YouTubeDownloadService {

    private static final int TIMEOUT = 15;
    private List<String> defaultOptions = List.of("-k", "-x", "--audio-format", "m4a", "--write-thumbnail", "--yes-playlist", "--format", "best", "--write-info-json");

    private final YouTubePlaylistConfiguration playLists;
    private final Logger log = LoggerFactory.getLogger(YouTubeDownloadService.class);

    private final List<String> commands;

    @Autowired
    public YouTubeDownloadService(final YouTubePlaylistConfiguration playLists, final YouTubeDownloaderConfiguration configuration, final String filesFolder) {
        this.playLists = playLists;
        final String outputFolder = filesFolder + "/%(title)s.%(ext)s";
        commands = new ArrayList<>();
        commands.add(configuration.getExecutable());
        commands.addAll(List.of("-o", outputFolder));
        commands.addAll(defaultOptions);
        commands.addAll(configuration.getExtraOptions());
    }

    public void downloadPlaylists() {
        playLists.getUrls().forEach(this::download);
    }

    private void download(final String url) {
        try {
            ArrayList<String> urlCommands = new ArrayList<>(commands);
            urlCommands.add(url);
            final ProcessBuilder builder = new ProcessBuilder(urlCommands).redirectErrorStream(true);
            final Process process = builder.start();
            logOutput(process);
            waitForFinish(process);
        } catch (IOException | InterruptedException e) {
            log.error("Kan de YouTube downloader niet uitvoeren: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void waitForFinish(Process process) throws InterruptedException {
        boolean hasProgramExitedSuccesfully = process.waitFor(TIMEOUT, TimeUnit.SECONDS);
        if (hasProgramExitedSuccesfully) {
            log.info("Playlists succesvol gedownload");
        } else {
            log.warn("Applicatie deed er langer over dan {} seconden om af te ronden", TIMEOUT);
        }
    }

    private void logOutput(Process process) throws IOException {
        try (BufferedReader lineReader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            lineReader.lines().forEach(log::debug);
        }
    }

    public List<String> getPlayLists() {
        return playLists.getUrls();
    }
}
