package chaoyang.tempmonitor.model;

public class StartTimeAndTimeDiff {
    private String plp_photo_position;
    private String plp_fixStartTime;
    private long plp_timeDiff;
    private float plp_fMaxTemperature;   //定点1最高温度

    public String getPlp_photo_position() {
        return plp_photo_position;
    }

    public void setPlp_photo_position(String plp_photo_position) {
        this.plp_photo_position = plp_photo_position;
    }

    public String getPlp_fixStartTime() {
        return plp_fixStartTime;
    }

    public void setPlp_fixStartTime(String plp_fixStartTime) {
        this.plp_fixStartTime = plp_fixStartTime;
    }

    public long getPlp_timeDiff() {
        return plp_timeDiff;
    }

    public void setPlp_timeDiff(long plp_timeDiff) {
        this.plp_timeDiff = plp_timeDiff;
    }

    public float getPlp_fMaxTemperature() {
        return plp_fMaxTemperature;
    }

    public void setPlp_fMaxTemperature(float plp_fMaxTemperature) {
        this.plp_fMaxTemperature = plp_fMaxTemperature;
    }
}
