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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

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
            String videoId = extractVideoId(url);
            if (videoId == null) {
                log.warn("Could not extract video ID from URL: {}", url);
                return;
            }
            
            String filesFolder = configuration.getFilesFolder();
            
            // Download video with audio included (highest available quality)
            log.info("Downloading video with audio...");
            downloadVideo(url, simulate);
            
            // Find the downloaded video file
            String videoFile = findVideoFile(filesFolder);
            if (videoFile != null) {
                log.info("Successfully downloaded: {}", videoFile);
                
                // Extract thumbnail from 5th second
                extractThumbnailFromFrame(videoFile);
                
                // Create info.json
                createInfoJson(videoId, url, videoFile);
                
                log.info("Download complete for: {}", videoFile);
            } else {
                log.warn("No video file found after download");
            }
        } catch (Exception e) {
            log.error("YouTube downloader failed: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void downloadVideo(String url, boolean simulate) throws IOException, InterruptedException {
        log.info("Downloading video with audio (hd1080 or fallback)...");
        final List<String> executable = configuration.getVideoExecutableBase("hd1080", simulate);
        executable.add(url);
        
        final ProcessBuilder builder = new ProcessBuilder(executable).redirectErrorStream(true);
        final Process process = builder.start();
        logOutput(process);
        waitForFinish(process);
        
        log.info("Video download completed");
    }

    private String findVideoFile(String filesFolder) throws IOException {
        Path folder = Paths.get(filesFolder);
        if (!Files.exists(folder)) {
            return null;
        }
        
        try (Stream<Path> paths = Files.list(folder)) {
            return paths.filter(path -> {
                String name = path.getFileName().toString();
                return name.endsWith(".mp4") && !name.endsWith(".info.json");
            }).findFirst()
            .map(Path::toString)
            .orElse(null);
        }
    }

    private void extractThumbnailFromFrame(String videoFile) throws IOException, InterruptedException {
        String jpgFile = videoFile.replace(".mp4", ".jpg");
        
        ProcessBuilder pb = new ProcessBuilder(
            "ffmpeg", "-y", "-i", videoFile, 
            "-ss", "00:00:05", "-frames:v", "1", 
            "-q:v", "2", jpgFile
        );
        pb.redirectErrorStream(true);
        Process p = pb.start();
        p.waitFor();
        log.info("Extracted thumbnail from 5th second: {}", jpgFile);
    }

    private void createInfoJson(String videoId, String url, String videoFile) throws IOException, InterruptedException {
        String infoJsonFile = videoFile.replace(".mp4", ".info.json");
        
        ProcessBuilder pb = new ProcessBuilder(
            "youtubedr", "info", url
        );
        pb.redirectErrorStream(true);
        Process p = pb.start();
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        
        String title = null;
        String channel = null;
        
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("Title:")) {
                title = line.substring(6).trim();
            } else if (line.startsWith("Author:")) {
                channel = line.substring(7).trim();
            }
        }
        p.waitFor();
        
        if (title != null && channel != null) {
            String jsonContent = String.format(
                "{\n  \"id\": \"%s\",\n  \"title\": \"%s\",\n  \"channel\": \"%s\",\n  \"upload_date\": \"20250101\"\n}",
                videoId.replaceAll("[^a-zA-Z0-9-]", "-"),
                title.replace("\"", "\\\""),
                channel.replace("\"", "\\\"")
            );
            java.nio.file.Files.writeString(java.nio.file.Paths.get(infoJsonFile), jsonContent);
            log.info("Created info.json: {}", infoJsonFile);
        }
    }

    private String extractVideoId(String url) {
        if (url.contains("v=")) {
            String[] parts = url.split("v=");
            if (parts.length > 1) {
                String id = parts[1].split("[&?]")[0];
                return id.isEmpty() ? null : id;
            }
        }
        return null;
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
