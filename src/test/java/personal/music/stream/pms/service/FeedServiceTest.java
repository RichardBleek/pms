package personal.music.stream.pms.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.rometools.rome.feed.synd.SyndFeed;

import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.*;
import org.springframework.test.context.junit4.SpringRunner;
import personal.music.stream.pms.mix.Mix;
import personal.music.stream.pms.service.FeedService;
import personal.music.stream.pms.service.MixService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

@SpringBootTest
public class FeedServiceTest {

    @Mock
    MixService mixService;

    private FeedService feedService;

    @BeforeEach
    public void before() {
        feedService = new FeedService(mixService, "http://localhost:8080", "pms");

        Mockito.when(mixService.mixStream()).thenReturn(Flux.just(
                new Mix("semblance", "Semblance", "j", "http://a.com/b.jpg", "http://a.com/b.m4a", new Date()),
                new Mix("slipstream", "Slipstream", "j","http://a.com/c.jpg", "http://a.com/c.m4a", new Date())
        ));
    }

    @Test
    public void syndFeed() {
        Mono<SyndFeed> syndFeedMono = feedService.syndFeed();
        syndFeedMono.subscribe(f -> {
            assertThat(f).isNotNull();
            assertThat(f.getEntries().size()).isEqualTo(2);
        });
    }

    @Test
    public void syndFeedString() {
        Mono<String> stringOutput = feedService.syndFeedString();
        stringOutput.subscribe(s -> assertThat(s).isNotNull());
    }

}
