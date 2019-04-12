package personal.music.stream.pms.feed;

import com.rometools.rome.feed.rss.Item;

public class Channel extends com.rometools.rome.feed.rss.Channel {

    public void addItem(Item i){
        getItems().add(i);
    }
}
