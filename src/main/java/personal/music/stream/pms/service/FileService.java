package personal.music.stream.pms.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class FileService {

    private Logger log = LoggerFactory.getLogger(FileService.class);

    @Value("${pms.files-folder}")
    String filesFolder;

    private ResourceLoader resourceLoader;

    public FileService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public Mono<Resource> streamFile(String fileName) {
        log.info("streaming file: {}", fileName);
        Resource result = resourceLoader.getResource("file:" + filesFolder + fileName);
        return Mono.just(result);
    }
}
