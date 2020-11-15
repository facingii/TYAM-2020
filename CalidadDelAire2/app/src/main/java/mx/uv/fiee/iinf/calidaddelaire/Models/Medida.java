package mx.uv.fiee.iinf.calidaddelaire.Models;

public class Medida {

    private String averagedOverInHours;
    private String time;
    private String value;
    private String unit;
    private String pollutant;


    public String getAveragedOverInHours() {
        return averagedOverInHours;
    }

    public void setAveragedOverInHours(String averagedOverInHours) {
        this.averagedOverInHours = averagedOverInHours;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getPollutant() {
        return pollutant;
    }

    public void setPollutant(String pollutant) {
        this.pollutant = pollutant;
    }
}
