package chaoyang.tempmonitor.model;

public class Room {
    private Integer id;
    private Integer room_number;      //房间号
    private Integer cam_id;    //摄像头id
    private String cam_position;    //摄像头位置
    private String cam_ip;      //摄像头ip地址
    private String cam_user;   //摄像头账号
    private String cam_pwd;    //摄像头密码
    private Integer cam_port;   //端口号
    private Integer cam_type;
    private Integer cam_channel;  //通道号
    private Integer cam_station_number;   //工位号
 //   private Integer tire_number;   //轮胎编号

    public Room(){}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRoom_number() {
        return room_number;
    }

    public void setRoom_number(Integer room_number) {
        this.room_number = room_number;
    }

    public Integer getCam_id() {
        return cam_id;
    }

    public void setCam_id(Integer cam_id) {
        this.cam_id = cam_id;
    }

    public String getCam_position() {
        return cam_position;
    }

    public void setCam_position(String cam_position) {
        this.cam_position = cam_position;
    }

    public String getCam_ip() {
        return cam_ip;
    }

    public void setCam_ip(String cam_ip) {
        this.cam_ip = cam_ip;
    }

    public String getCam_user() {
        return cam_user;
    }

    public void setCam_user(String cam_user) {
        this.cam_user = cam_user;
    }

    public String getCam_pwd() {
        return cam_pwd;
    }

    public void setCam_pwd(String cam_pwd) {
        this.cam_pwd = cam_pwd;
    }

    public Integer getCam_port() {
        return cam_port;
    }

    public void setCam_port(Integer cam_port) {
        this.cam_port = cam_port;
    }

    public Integer getCam_type() {
        return cam_type;
    }

    public void setCam_type(Integer cam_type) {
        this.cam_type = cam_type;
    }

    public Integer getCam_channel() {
        return cam_channel;
    }

    public void setCam_channel(Integer cam_channel) {
        this.cam_channel = cam_channel;
    }

    public Integer getCam_station_number() {
        return cam_station_number;
    }

    public void setCam_station_number(Integer cam_station_number) {
        this.cam_station_number = cam_station_number;
    }

}
