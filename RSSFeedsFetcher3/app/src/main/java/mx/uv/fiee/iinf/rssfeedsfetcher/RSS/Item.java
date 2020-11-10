package mx.uv.fiee.iinf.rssfeedsfetcher.RSS;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root (name = "item", strict = false)
public class Item {

    @Element (name = "title")
    private String title;

    @Element (name = "enclosure")
    private Enclosure enclosure;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Enclosure getEnclosure() {
        return enclosure;
    }

    public void setEnclosure(Enclosure enclosure) {
        this.enclosure = enclosure;
    }

}
