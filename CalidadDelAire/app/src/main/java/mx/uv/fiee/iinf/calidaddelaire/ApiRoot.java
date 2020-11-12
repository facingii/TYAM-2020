package mx.uv.fiee.iinf.calidaddelaire;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

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
