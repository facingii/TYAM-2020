package mx.uv.fiee.iinf.calidaddelaire.Models;

public class Indice {

    private String calculatationTime;
    private String responsiblePollutant;
    private String value;
    private String scale;

    public String getCalculatationTime() {
        return calculatationTime;
    }

    public void setCalculatationTime(String calculatationTime) {
        this.calculatationTime = calculatationTime;
    }

    public String getResponsiblePollutant() {
        return responsiblePollutant;
    }

    public void setResponsiblePollutant(String responsiblePollutant) {
        this.responsiblePollutant = responsiblePollutant;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }
}
