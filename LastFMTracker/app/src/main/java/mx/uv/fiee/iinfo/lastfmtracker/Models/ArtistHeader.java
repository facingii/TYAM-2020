package mx.uv.fiee.iinfo.lastfmtracker.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Clase modelo para el arreglo obtenido como resultado de invocar al método Top Artist.
 */
public class ArtistHeader {
    public ArrayList<Artist> artist;
}