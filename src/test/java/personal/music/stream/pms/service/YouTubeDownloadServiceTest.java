package personal.music.stream.pms.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;

import java.util.stream.Stream;

@ActiveProfiles("w10")
@SpringBootTest
class YouTubeDownloadServiceTest {

    private final YouTubeDownloadService ytDownloadService;

    @Autowired
    public YouTubeDownloadServiceTest(final YouTubeDownloadService ytDownloadService) {
        this.ytDownloadService = ytDownloadService;
    }

    @Test
    void test() {
        Assertions.assertNotNull(ytDownloadService);
        Assertions.assertNotNull(ytDownloadService.getPlayLists());
        Assertions.assertEquals(2, ytDownloadService.getPlayLists().size());
        ytDownloadService.getPlayLists().forEach(System.out::println);
    }

    @Test
    void download() {
        ytDownloadService.downloadPlaylists();
    }

}