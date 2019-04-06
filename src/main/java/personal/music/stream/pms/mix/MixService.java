package personal.music.stream.pms.mix;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Service
public class MixService {

    Map<String, Mix> repo = new HashMap<>();

    MixService() {
        Mix.getDummy().forEach(mix -> repo.put(mix.getName(), mix));
    }

    public Flux<Mix> mixStream() {
        return Flux.fromStream(repo.values().stream());
    }

    public Mono<Mix> mixMono(String id) {
        return Mono.just(repo.get(id));
    }
}
