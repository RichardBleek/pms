package personal.music.stream.pms.service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("dev")
@Disabled("Integration test - requires youtubedr, network access, and ~3-5 seconds to download")
class YouTubeDownloadServiceTest {

    private final YouTubeDownloadService ytDownloadService;

    @Autowired
    public YouTubeDownloadServiceTest(final YouTubeDownloadService ytDownloadService) {
        this.ytDownloadService = ytDownloadService;
    }

    @Test
    void download() throws Exception {
        Path tempDir = Files.createTempDirectory("pms-test");
        String testVideoUrl = "https://www.youtube.com/watch?v=dQw4w9WgXcQ";
        
        List<String> cmd = List.of("youtubedr", "download", "-d", tempDir.toString(), "--quality", "medium", testVideoUrl);
        ProcessBuilder pb = new ProcessBuilder(cmd);
        Process process = pb.start();
        
        boolean completed = process.waitFor(60, java.util.concurrent.TimeUnit.SECONDS);
        
        assertTrue(completed, "Download should complete within 60 seconds");
        assertTrue(process.exitValue() == 0, "Download should succeed");
        
        Files.walk(tempDir)
            .filter(Files::isRegularFile)
            .findFirst()
            .ifPresent(path -> System.out.println("Downloaded: " + path));
        
        Files.walk(tempDir).forEach(p -> {
            try { Files.deleteIfExists(p); } catch (Exception e) {}
        });
        Files.deleteIfExists(tempDir);
    }

}