package personal.music.stream.pms.mix;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class MixService {

    private Logger log = LoggerFactory.getLogger(MixService.class);

    @Value("${pms.files-folder}")
    String filesFolder;

    ResourceLoader resourceLoader;

    private Map<String, Mix> repo = new HashMap<>();

    MixService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    private void fillRepo() {
        Resource resource = resourceLoader.getResource("file:" + filesFolder + "mixes.txt");
        try {
            BufferedReader reader = new BufferedReader(new FileReader(resource.getFile()));
            reader.lines().forEach(s -> repo.put(
                    s, new Mix(s, s, "http://rbleek.com/jaydee/file/"+s+".jpg",
                            "http://rbleek.com/jaydee/file/"+s+".m4a")));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public Flux<Mix> mixStream() {
        fillRepo();
        return Flux.fromStream(repo.values().stream());
    }

    public Mono<Mix> mixMono(String id) {
        fillRepo();
        return Mono.just(repo.get(id));
    }
}
