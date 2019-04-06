package personal.music.stream.pms.mix;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class FileService {

    public Mono<Resource> streamFile(String fileName) {
        return null;
    }
}
