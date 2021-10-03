package personal.music.stream.pms.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class YouTubeDownloadServiceTest {

    private final YouTubeDownloadService ytDownloadService;

    @Autowired
    public YouTubeDownloadServiceTest(final YouTubeDownloadService ytDownloadService) {
        this.ytDownloadService = ytDownloadService;
    }

    @Test
    void download() {
        ytDownloadService.downloadPlaylists(true);
    }

}