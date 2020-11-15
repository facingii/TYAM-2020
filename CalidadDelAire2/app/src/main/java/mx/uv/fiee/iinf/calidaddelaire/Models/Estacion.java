package mx.uv.fiee.iinf.calidaddelaire.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import mx.uv.fiee.iinf.calidaddelaire.Models.Indice;
import mx.uv.fiee.iinf.calidaddelaire.Models.Medida;
import mx.uv.fiee.iinf.calidaddelaire.Models.Ubicacion;

public class Estacion {

    @SerializedName ("indexes")
    private ArrayList<Indice> indices;

    @SerializedName ("measurements")
    private ArrayList<Medida> medidas;

    @SerializedName ("location")
    private Ubicacion ubicacion;

    private String source_id;
    private String name;
    private String id;

    public ArrayList<Indice> getIndices() {
        return indices;
    }

    public void setIndices(ArrayList<Indice> indices) {
        this.indices = indices;
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }

    public ArrayList<Medida> getMedidas() {
        return medidas;
    }

    public void setMedidas(ArrayList<Medida> medidas) {
        this.medidas = medidas;
    }

    public String getSource_id() {
        return source_id;
    }

    public void setSource_id(String source_id) {
        this.source_id = source_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
