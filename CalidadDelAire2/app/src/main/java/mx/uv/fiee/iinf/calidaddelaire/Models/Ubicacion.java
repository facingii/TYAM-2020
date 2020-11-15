package mx.uv.fiee.iinf.calidaddelaire.Models;

import java.io.Serializable;

public class Ubicacion implements Serializable  {

    private String alt;
    private String lon;
    private String lat;

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }
}
