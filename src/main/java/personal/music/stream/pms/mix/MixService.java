package personal.music.stream.pms.mix;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class MixService {

    public Flux<Mix> mixStream() {
        return Mix.getDummy();
    }
}
