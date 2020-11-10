package mx.uv.fiee.iinf.rssfeedsfetcher.RSS;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

import java.util.List;

@Root (name = "rss", strict = false)
public class Rss {

    @Element (name = "title")
    @Path ("channel")
    private String channelTitle;

    @ElementList (name = "item", inline = true)
    @Path ("channel")
    private List<Item> items;


    public String getChannelTitle() {
        return channelTitle;
    }

    public void setChannelTitle(String channelTitle) {
        this.channelTitle = channelTitle;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

}
