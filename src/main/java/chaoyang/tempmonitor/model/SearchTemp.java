package chaoyang.tempmonitor.model;

public class SearchTemp {

    public String tire_number;
    public String plp_fixStartTime;
    public int plp_timediff;
    public float plp_fMaxTemperature;
    public float plp_fMinTemperature;
    public float plp_fAverageTemperature;
    public int sample_type_number;



    public int getSample_type_number() {
        return sample_type_number;
    }

    public void setSample_type_number(int sample_type_number) {
        this.sample_type_number = sample_type_number;
    }

    public SearchTemp() {
    }

    public String gettire_number() {
        return tire_number;
    }

    public void settire_number(String tire_number) {
        this.tire_number = tire_number;
    }

    public String getPlp_fixStartTime(){
        return  plp_fixStartTime;
    }

    public void setPlp_fixStartTime(String plp_fixstarttime){
        this.plp_fixStartTime = plp_fixstarttime;
    }

    public int getPlp_timediff() {
        return plp_timediff;
    }

    public void setPlp_timediff(int plp_timediff){
        this.plp_timediff = plp_timediff;
    }

    public float getPlp_fMaxTemperature() {
        return plp_fMaxTemperature;
    }

    public void setPlp_fMaxTemperature(float plp_fMaxTemperature) {
        this.plp_fMaxTemperature = plp_fMaxTemperature;
    }

    public float getPlp_fMinTemperature() {
        return plp_fMinTemperature;
    }

    public void setPlp_fMinTemperature(float plp_fMinTemperature) {
        this.plp_fMinTemperature = plp_fMinTemperature;
    }

    public float getPlp_fAverageTemperature() {
        return plp_fAverageTemperature;
    }

    public void setPlp_fAverageTemperature(float plp_fAverageTemperature) {
        this.plp_fAverageTemperature = plp_fAverageTemperature;
    }
}
