package chaoyang.tempmonitor.model;

import java.sql.Timestamp;

public class PLPTempInfo {
    private int plp_temp_id;   //测试点温度信息编号
    private String tire_number;    //轮胎号
    private int sample_type;    //测试点类型（0-点/1-线/2-框）
    private int sample_type_number;    //样本测试点个数编号
    private float plp_fMaxTemperature;    //最大温度
    private float plp_fMinTemperature;   //最小温度
    private float plp_fAverageTemperature;   //平均温度
    private float plp_fTemperatureDiff;   //温差
    private int plp_dwPointNum; //点个数
    private String plp_byRes;   //保留，置为0
    private String plp_photo_position;    //抓取图片位置
    private String plp_fixStartTime;    //获取开始时间
    private int plp_timeDiff;    //时差
    private String cam_ip;
    private int cam_station_number;   //仓位号
    private Timestamp plp_currTime;  //当前时间

    public String getCam_ip() {
        return cam_ip;
    }

    public void setCam_ip(String cam_ip) {
        this.cam_ip = cam_ip;
    }

    public String getPlp_fixStartTime() {
        return plp_fixStartTime;
    }

    public void setPlp_fixStartTime(String plp_fixStartTime) {
        this.plp_fixStartTime = plp_fixStartTime;
    }

    public int getPlp_timeDiff() {
        return plp_timeDiff;
    }

    public void setPlp_timeDiff(int plp_timeDiff) {
        this.plp_timeDiff = plp_timeDiff;
    }

    public int getPlp_temp_id() {
        return plp_temp_id;
    }

    public void setPlp_temp_id(int plp_temp_id) {
        this.plp_temp_id = plp_temp_id;
    }

    public String getTire_number() {
        return tire_number;
    }

    public void setTire_number(String tire_number) {
        this.tire_number = tire_number;
    }

    public int getSample_type() {
        return sample_type;
    }

    public void setSample_type(int sample_type) {
        this.sample_type = sample_type;
    }

    public int getSample_type_number() {
        return sample_type_number;
    }

    public void setSample_type_number(int sample_type_number) {
        this.sample_type_number = sample_type_number;
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

    public float getPlp_fTemperatureDiff() {
        return plp_fTemperatureDiff;
    }

    public void setPlp_fTemperatureDiff(float plp_fTemperatureDiff) {
        this.plp_fTemperatureDiff = plp_fTemperatureDiff;
    }

    public int getPlp_dwPointNum() {
        return plp_dwPointNum;
    }

    public void setPlp_dwPointNum(int plp_dwPointNum) {
        this.plp_dwPointNum = plp_dwPointNum;
    }

    public String getPlp_byRes() {
        return plp_byRes;
    }

    public void setPlp_byRes(String plp_byRes) {
        this.plp_byRes = plp_byRes;
    }


    public String getPlp_photo_position() {
        return plp_photo_position;
    }

    public void setPlp_photo_position(String plp_photo_position) {
        this.plp_photo_position = plp_photo_position;
    }

    public int getCam_station_number() {
        return cam_station_number;
    }

    public void setCam_station_number(int cam_station_number) {
        this.cam_station_number = cam_station_number;
    }

    public Timestamp getPlp_currTime() {
        return plp_currTime;
    }

    public void setPlp_currTime(Timestamp plp_currTime) {
        this.plp_currTime = plp_currTime;
    }
}
