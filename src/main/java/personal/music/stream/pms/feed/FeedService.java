package personal.music.stream.pms.feed;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.rometools.rome.feed.synd.*;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedOutput;
import org.springframework.stereotype.Service;

import personal.music.stream.pms.mix.Mix;
import personal.music.stream.pms.mix.MixService;
import reactor.core.publisher.Mono;

@Service
public class FeedService {

    private String baseUrl;
    private MixService mixService;

    public FeedService(MixService mixService, String hostName, String applicationPath) {
        this.mixService = mixService;
        this.baseUrl = hostName + applicationPath;
    }

    public Mono<SyndFeed> syndFeed() {
        SyndFeed feed = new SyndFeedImpl();
        feed.setFeedType("rss_2.0");
        feed.setTitle("Personal Music Stream - Jaydee music");
        feed.setDescription("Stream you personal music");
        feed.setLink(baseUrl + "/rss");

        SyndLink link = new SyndLinkImpl();
        link.setRel("self");
        link.setHref(baseUrl + "/rss");
        link.setTitle("Personal Music Stream - Jaydee music");
        feed.setLinks(Collections.singletonList(link));

        SyndImageImpl image = new SyndImageImpl();
        image.setUrl(baseUrl + "/file/channel-image.jpg");
        image.setTitle("Personal Music Stream - Jaydee music");
        image.setLink(baseUrl + "/rss");
        image.setHeight(144);
        image.setWidth(144);
        feed.setImage(image);

        List<SyndEntry> entries = new ArrayList<>();
        mixService.mixStream().map(this::toEntry).subscribe(entries::add);
        feed.setEntries(entries);

        return Mono.just(feed);
    }

    private SyndEntry toEntry(Mix mix) {
        SyndEntry entry = new SyndEntryImpl();
        entry.setLink(mix.getFullUrl());
        entry.setTitle(mix.getName());
        entry.setUri(mix.getFullUrl());
        entry.setPublishedDate(new Date());
        SyndCategory category = new SyndCategoryImpl();
        category.setName("music");
        entry.setCategories(Collections.singletonList(category));
        return entry;
    }

    public Mono<String> syndFeedString() {
        return syndFeed().map(this::outputFeed);
    }

    private String outputFeed(SyndFeed f) {
        String s = null;
        try {
            s = new SyndFeedOutput().outputString(f);
        } catch (FeedException e) {
            e.printStackTrace();
        }
        return s;
    }
}
