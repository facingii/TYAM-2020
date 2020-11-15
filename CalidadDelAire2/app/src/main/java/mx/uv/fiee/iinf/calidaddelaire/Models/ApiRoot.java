package mx.uv.fiee.iinf.calidaddelaire.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import mx.uv.fiee.iinf.calidaddelaire.Models.Resultado;

public class ApiRoot {
    @SerializedName ("results")
    private ArrayList<Resultado> resultados;

    public ArrayList<Resultado> getResultados() {
        return resultados;
    }

    public void setResultados(ArrayList<Resultado> resultados) {
        this.resultados = resultados;
    }
}
