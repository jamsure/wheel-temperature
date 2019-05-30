package chaoyang.tempmonitor.model;

public class TempDashboard {

    public  int cam_id;
    public String cam_position;
    public int sample_type_number;
    public int plp_timediff;
    public float plp_fMaxTemperature;



    public TempDashboard() {
    }

    public int getCam_id() {
        return cam_id;
    }

    public void setCam_id(int cam_id) {
        this.cam_id = cam_id;
    }

    public String getCam_position() {
        return cam_position;
    }

    public void setCam_position(String cam_position) {
        this.cam_position = cam_position;
    }

    public int getSample_type_number() {
        return sample_type_number;
    }

    public void setSample_type_number(int sample_type_number) {
        this.sample_type_number = sample_type_number;
    }

    public int getPlp_timediff() {
        return plp_timediff;
    }

    public void setPlp_timediff(int plp_timediff) {
        this.plp_timediff = plp_timediff;
    }

    public float getPlp_fMaxTemperature() {
        return plp_fMaxTemperature;
    }

    public void setPlp_fMaxTemperature(float plp_fMaxTemperature) {
        this.plp_fMaxTemperature = plp_fMaxTemperature;
    }
}
