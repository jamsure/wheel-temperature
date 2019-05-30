package chaoyang.tempmonitor.model;

public class PointInfo {
    private int point_id;  //点编号
    private String tire_number;    //轮胎号
    private int sample_type;   //测温点类型（0-点/1-线/2-框）
    private int sample_type_number;   //样本测试点个数编号
    private int point_type_index;   //线框类型中点个数编号
    private float point_x;   //测试点横坐标
    private float point_y;    //测试点纵坐标
    private int cam_station_number;  //仓位号

    public int getPoint_id() {
        return point_id;
    }

    public void setPoint_id(int point_id) {
        this.point_id = point_id;
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

    public int getCam_station_number() {
        return cam_station_number;
    }

    public void setCam_station_number(int cam_station_number) {
        this.cam_station_number = cam_station_number;
    }

}
