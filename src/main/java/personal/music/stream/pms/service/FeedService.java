package personal.music.stream.pms.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.rometools.modules.itunes.EntryInformation;
import com.rometools.modules.itunes.EntryInformationImpl;
import com.rometools.rome.feed.rss.Enclosure;
import com.rometools.rome.feed.synd.*;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import personal.music.stream.pms.mix.Mix;
import reactor.core.publisher.Mono;

@Service
public class FeedService {

    private Logger log = LoggerFactory.getLogger(FeedService.class);

    private String baseUrl;
    private MixService mixService;

    public FeedService(MixService mixService, String hostName, String applicationPath) {
        this.mixService = mixService;
        this.baseUrl = hostName + applicationPath;
    }

    public Mono<SyndFeed> syndFeed() {
        //feed object
        SyndFeed feed = new SyndFeedImpl();

        //create entries
        mixService.mixStream()
                .map(this::toEntry)
                .collectList()
                .subscribe(feed::setEntries);

        //feed info
        feed.setFeedType("rss_2.0");
        feed.setTitle("Personal Music Stream - Jaydee music");
        feed.setDescription("Stream you personal music");
        feed.getEntries().stream().findFirst().ifPresent(syndEntry -> feed.setAuthor(syndEntry.getAuthor()));
        feed.setLink(baseUrl + "/rss");

        //feed link info
        SyndLink link = new SyndLinkImpl();
        link.setRel("self");
        link.setHref(baseUrl + "/rss");
        link.setTitle("Personal Music Stream - Jaydee music");
        feed.setLinks(Collections.singletonList(link));

        //feed image
        SyndImageImpl image = new SyndImageImpl();
        image.setUrl(baseUrl + "/file/channel-image.jpg");
        image.setTitle("Personal Music Stream - Jaydee music");
        image.setLink(baseUrl + "/rss");
        image.setHeight(144);
        image.setWidth(144);
        feed.setImage(image);

        return Mono.just(feed);
    }

    private SyndEntry toEntry(Mix mix) {
        SyndEntry entry = new SyndEntryImpl();

        SyndEnclosure enclosure = new SyndEnclosureImpl();
        enclosure.setUrl(mix.getFullUrl());
        enclosure.setType("audio/m4a");
        entry.setEnclosures(Collections.singletonList(enclosure));
        entry.setTitle(mix.getName());
        entry.setUri(mix.getFullUrl());
        entry.setPublishedDate(mix.getPublishedDate());
        entry.setAuthor(mix.getAuthor());
        SyndCategory category = new SyndCategoryImpl();
        category.setName("music");
        entry.setCategories(Collections.singletonList(category));

        EntryInformation entryInformation = new EntryInformationImpl();
        entryInformation.setTitle(mix.getName());
        entryInformation.setAuthor(mix.getAuthor());
        try {
            entryInformation.setImage(new URL(mix.getImageUrl()));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        entry.getModules().add(entryInformation);

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
