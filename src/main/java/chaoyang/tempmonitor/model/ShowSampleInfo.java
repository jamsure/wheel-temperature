package chaoyang.tempmonitor.model;

import java.util.List;

public class ShowSampleInfo {
    private int room_number;   //房间编号
    private String sample_specification; //规格
    private String sample_level;  //层级
    private String sample_pattern;  //花纹
    private String sample_test_date;  //试验日期
    private String sample_number;    //实验编号
    private String sample_person;  //送样人
    private String sample_scheme;  //方案
    private List<String> cam_IPList;  //获取仓位多摄像头ip
    private String tire_number;    //轮胎号
    private int cam_station_number;  //仓位号

    public int getRoom_number() {
        return room_number;
    }

    public void setRoom_number(int room_number) {
        this.room_number = room_number;
    }

    public String getSample_specification() {
        return sample_specification;
    }

    public void setSample_specification(String sample_specification) {
        this.sample_specification = sample_specification;
    }

    public String getSample_level() {
        return sample_level;
    }

    public void setSample_level(String sample_level) {
        this.sample_level = sample_level;
    }

    public String getSample_pattern() {
        return sample_pattern;
    }

    public void setSample_pattern(String sample_pattern) {
        this.sample_pattern = sample_pattern;
    }

    public String getSample_person() {
        return sample_person;
    }

    public void setSample_person(String sample_person) {
        this.sample_person = sample_person;
    }

    public String getSample_scheme() {
        return sample_scheme;
    }

    public void setSample_scheme(String sample_scheme) {
        this.sample_scheme = sample_scheme;
    }

    public List<String> getCam_IPList() {
        return cam_IPList;
    }

    public void setCam_IPList(List<String> cam_IPList) {
        this.cam_IPList = cam_IPList;
    }

    public String getTire_number() {
        return tire_number;
    }

    public void setTire_number(String tire_number) {
        this.tire_number = tire_number;
    }

    public String getSample_test_date() {
        return sample_test_date;
    }

    public void setSample_test_date(String sample_test_date) {
        this.sample_test_date = sample_test_date;
    }

    public String getSample_number() {
        return sample_number;
    }

    public void setSample_number(String sample_number) {
        this.sample_number = sample_number;
    }

    public int getCam_station_number() {
        return cam_station_number;
    }

    public void setCam_station_number(int cam_station_number) {
        this.cam_station_number = cam_station_number;
    }
}
