package mx.uv.fiee.iinf.tyam.API;

import mx.uv.fiee.iinf.tyam.BuildConfig;

public class ApiContants {
    public static final String TOP_ARTISTS_URL = "/2.0/?method=chart.gettopartists";
    public static final String TOP_TRACKS_URL = "/2.0/?method=chart.getTopTracks";
    public static final String API_BASE_URL = "https://ws.audioscrobbler.com";
    public static final String API_REQUEST_FORMAT = "json";
    public static final String API_KEY = BuildConfig.LAST_FM_API_KEYs;
    public static final int ARTIST_IMAGE_INDEX = 4;
    public static final int TRACK_IMAGE_INDEX = 3;
}
