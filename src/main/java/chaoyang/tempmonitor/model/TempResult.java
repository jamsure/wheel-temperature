package chaoyang.tempmonitor.model;

public class TempResult {
    private int sample_id;   //样本编号
    private int room_number;    //房间编号
    private String cam_ip;   //摄像机IP
    private int point_type;    //测温类型
    private int point_type_number;  //样本测试点编号
    private int point_type_index;   //测试样本点中点个数编号
    private float point_x;    //点横坐标
    private float point_y;   //点纵坐标
    private float plp_fMaxTemperature;   //最大温度
    private float plp_fMinTemperature;    //最小温度
    private float plp_fAverageTemperature;   //平均温度
    private float plp_fTemperatureDiff;    //温差
    private float plp_fTemperature;    //点温度

    public int getSample_id() {
        return sample_id;
    }

    public void setSample_id(int sample_id) {
        this.sample_id = sample_id;
    }

    public int getRoom_number() {
        return room_number;
    }

    public void setRoom_number(int room_number) {
        this.room_number = room_number;
    }

    public String getCam_ip() {
        return cam_ip;
    }

    public void setCam_ip(String cam_ip) {
        this.cam_ip = cam_ip;
    }

    public int getPoint_type() {
        return point_type;
    }

    public void setPoint_type(int point_type) {
        this.point_type = point_type;
    }

    public int getPoint_type_number() {
        return point_type_number;
    }

    public void setPoint_type_number(int point_type_number) {
        this.point_type_number = point_type_number;
    }

    public int getPoint_type_index() {
        return point_type_index;
    }

    public void setPoint_type_index(int point_type_index) {
        this.point_type_index = point_type_index;
    }

    public float getPoint_x() {
        return point_x;
    }

    public void setPoint_x(float point_x) {
        this.point_x = point_x;
    }

    public float getPoint_y() {
        return point_y;
    }

    public void setPoint_y(float point_y) {
        this.point_y = point_y;
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

    public float getPlp_fTemperature() {
        return plp_fTemperature;
    }

    public void setPlp_fTemperature(float plp_fTemperature) {
        this.plp_fTemperature = plp_fTemperature;
    }
}
