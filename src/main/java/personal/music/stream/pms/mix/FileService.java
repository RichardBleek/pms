package personal.music.stream.pms.mix;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class FileService {

    private ResourceLoader resourceLoader;

    public FileService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public Mono<Resource> streamFile(String fileName) {
        System.out.println("streaming file: " + fileName);
        Resource result = resourceLoader.getResource("mixes/" + fileName);
        return Mono.just(result);
    }
}
