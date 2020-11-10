package mx.uv.fiee.iinf.rssfeedsfetcher;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;

public class XMLFeedParser {
    /**
     * Parsear el documento XML en POJO's
     */
    public LinkedList<RSSItem> parse (String xml) {
        XmlPullParserFactory pullParserFactory;
        LinkedList<RSSItem> items = null;
        int count = 1;
        try {
            pullParserFactory = XmlPullParserFactory.newInstance ();
            XmlPullParser parser = pullParserFactory.newPullParser ();

            StringReader reader = new StringReader (xml);

            parser.setFeature (XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput (reader);

            int eventTye = parser.getEventType ();
            items = new LinkedList<> ();
            RSSItem currentItem = null;

            while (eventTye != XmlPullParser.END_DOCUMENT) {
                switch (eventTye) {
                    case XmlPullParser.START_TAG:
                        if ("item".equals (parser.getName ())) {
                            currentItem = new RSSItem ();
                            count++;
                        } else if (currentItem != null) {
                            if ("title".equals (parser.getName())) {
                                currentItem.title = parser.nextText ();
                            } else if ("link".equals(parser.getName())) {
                                currentItem.link = parser.nextText ();
                            } else if ("description".equals(parser.getName())) {
                                currentItem.description = parser.nextText ();
                            } else if ("enclosure".equals(parser.getName())) {
                                Enclosure enclosure = new Enclosure ();
                                enclosure.url = parser.getAttributeValue (null, "url");
                                enclosure.length = parser.getAttributeValue (null, "length");
                                enclosure.type = parser.getAttributeValue (null, "type");
                                currentItem.enclosure = enclosure;
                            } else if ("guid".equals(parser.getName())) {
                                currentItem.guid = parser.nextText ();
                            } else if ("pubDate".equals(parser.getName())) {
                                currentItem.pubDate = parser.nextText ();
                            } else if ("source".equals(parser.getName())) {
                                currentItem.source = parser.nextText ();
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if ("item".equals (parser.getName ())) {
                            items.add (currentItem);
                            currentItem = null;
                        }
                        break;
                }

                eventTye = parser.next ();
            }

        } catch (IOException | XmlPullParserException ex) {
            ex.printStackTrace ();
        }

        return items;
    }

    /**
     * POJO's
     */
    class RSSItem {
        public String title;
        public String link;
        public String description;
        public Enclosure enclosure;
        public String guid;
        public String pubDate;
        public String source;
    }

    class Enclosure {
        String url;
        String length;
        String type;
    }

}
