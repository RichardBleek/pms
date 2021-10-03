package personal.music.stream.pms.service;

import org.slf4j.*;
import org.springframework.scheduling.annotation.*;
import org.springframework.stereotype.*;

@Service
public class SceduleService {

    private Logger log = LoggerFactory.getLogger(MixService.class);

    private final YouTubeDownloadService youTubeDownloadService;

    public SceduleService(YouTubeDownloadService youTubeDownloadService) {
        this.youTubeDownloadService = youTubeDownloadService;
    }

    @Scheduled(fixedDelay = 60000)
    public void triggerDownload() {
        log.info("Starting download");

        youTubeDownloadService.downloadPlaylists();
    }
}
