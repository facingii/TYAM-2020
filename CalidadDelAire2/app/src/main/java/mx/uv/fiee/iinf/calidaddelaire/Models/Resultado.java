package mx.uv.fiee.iinf.calidaddelaire.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Resultado {
    @SerializedName ("_id")
    private String id;

    @SerializedName ("stations")
    private ArrayList<Estacion> estaciones;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<Estacion> getEstaciones() {
        return estaciones;
    }

    public void setEstaciones(ArrayList<Estacion> estaciones) {
        this.estaciones = estaciones;
    }
}
