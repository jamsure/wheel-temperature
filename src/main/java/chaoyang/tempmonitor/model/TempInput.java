package chaoyang.tempmonitor.model;

/**
 * 查询实体类
 */
public class TempInput {
    private int room_number;    //房间编号
    private String cam_ip;   //摄像机IP
    private String tire_number;   //样本名称
    private int sample_type;    //测温类型
    private int sample_type_number;  //样本测试点编号

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

    public String gettire_number() {
        return tire_number;
    }

    public void settire_number(String tire_number) {
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
}
