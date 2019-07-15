package personal.music.stream.pms.service;

import com.rometools.rome.feed.synd.SyndFeed;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;
import personal.music.stream.pms.mix.Mix;
import personal.music.stream.pms.service.FeedService;
import personal.music.stream.pms.service.MixService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

@RunWith(SpringRunner.class)
public class FeedServiceTest {

    @Mock
    MixService mixService;

    private FeedService feedService;

    @Before
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
            Assert.assertNotNull(f);
            Assert.assertEquals(2, f.getEntries().size());
        });
    }

    @Test
    public void syndFeedString() {
        Mono<String> stringOutput = feedService.syndFeedString();
        stringOutput.subscribe(Assert::assertNotNull);
    }

}
