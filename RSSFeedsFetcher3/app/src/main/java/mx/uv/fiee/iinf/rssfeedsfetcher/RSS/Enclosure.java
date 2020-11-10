package mx.uv.fiee.iinf.rssfeedsfetcher.RSS;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root (name = "enclosure", strict = false)
public class Enclosure {

    @Attribute
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
