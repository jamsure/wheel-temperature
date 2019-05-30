package chaoyang.tempmonitor.model;

public class SampleInfo {
    private int room_number;   //房间编号
    private String sample_reporter_name;    //试验报告名称
    private String sample_brand;    //样本品牌
    private String tire_number;    //轮胎号
    private String sample_number;    //实验编号
    private String sample_load_rode;  //负荷指数
    private String sample_test_date;  //试验日期
    private String sample_conversion;   //滚动阻力转鼓直径换算
    private String sample_inspection_unit;   //送检单位
    private String sample_tire_radius;   //轮胎名义半径
    private String sample_production_number;   //生产编号
    private String cam_ip;
    private int cam_station_number;   //仓位号
    private String sample_specification; //实验轮胎规格
    private String sample_level;  //层级
    private String sample_pattern;  //花纹
    private String sample_person;  //送样人
    private String sample_scheme;  //方案
    private String sample_filePath;  //文件保存路径
    private String type_cam_station;//相机位置类型
    private String sample_object_name;//项目名称
    private String sample_running_state;//仓位运行状态
    private float first_point_frequency;//第一次取点频率
    private float first_time_interval;//第一次时间间隔
    private float second_time_interval;//第二次时间间隔
    private float second_point_frequency;//第二次取点频率
    private float third_point_frequency;//第三次取点频率

    public float getFirst_time_interval() {
        return first_time_interval;
    }

    public void setFirst_time_interval(float first_time_interval) {
        this.first_time_interval = first_time_interval;
    }

    public float getSecond_time_interval() {
        return second_time_interval;
    }

    public void setSecond_time_interval(float second_time_interval) {
        this.second_time_interval = second_time_interval;
    }

    public float getFirst_point_frequency() {
        return first_point_frequency;
    }

    public void setFirst_point_frequency(float first_point_frequency) {
        this.first_point_frequency = first_point_frequency;
    }

    public float getSecond_point_frequency() {
        return second_point_frequency;
    }

    public void setSecond_point_frequency(float second_point_frequency) {
        this.second_point_frequency = second_point_frequency;
    }

    public float getThird_point_frequency() {
        return third_point_frequency;
    }

    public void setThird_point_frequency(float third_point_frequency) {
        this.third_point_frequency = third_point_frequency;
    }



    public String getSample_running_state() {
        return sample_running_state;
    }

    public void setSample_running_state(String sample_running_state) {
        this.sample_running_state = sample_running_state;
    }

    public String getSample_object_name() {
        return sample_object_name;
    }

    public void setSample_object_name(String sample_object_name) {
        this.sample_object_name = sample_object_name;
    }

    public String getType_cam_station(){return type_cam_station;}
    public void setType_cam_station(String type_cam_station){this.type_cam_station=type_cam_station;}
    public int getRoom_number() {
        return room_number;
    }

    public void setRoom_number(int room_number) {
        this.room_number = room_number;
    }

    public String getSample_reporter_name() {
        return sample_reporter_name;
    }

    public void setSample_reporter_name(String sample_reporter_name) {
        this.sample_reporter_name = sample_reporter_name;
    }

    public String getSample_brand() {
        return sample_brand;
    }

    public void setSample_brand(String sample_brand) {
        this.sample_brand = sample_brand;
    }


    public String getTire_number() {
        return tire_number;
    }

    public void setTire_number(String tire_number) {
        this.tire_number = tire_number;
    }

    public String getSample_load_rode() {
        return sample_load_rode;
    }

    public void setSample_load_rode(String sample_load_rode) {
        this.sample_load_rode = sample_load_rode;
    }

    public String getSample_test_date() {
        return sample_test_date;
    }

    public void setSample_test_date(String sample_test_date) {
        this.sample_test_date = sample_test_date;
    }

    public String getSample_conversion() {
        return sample_conversion;
    }

    public void setSample_conversion(String sample_conversion) {
        this.sample_conversion = sample_conversion;
    }

    public String getSample_inspection_unit() {
        return sample_inspection_unit;
    }

    public void setSample_inspection_unit(String sample_inspection_unit) {
        this.sample_inspection_unit = sample_inspection_unit;
    }

    public String getSample_tire_radius() {
        return sample_tire_radius;
    }

    public void setSample_tire_radius(String sample_tire_radius) {
        this.sample_tire_radius = sample_tire_radius;
    }

    public String getSample_production_number() {
        return sample_production_number;
    }

    public void setSample_production_number(String sample_production_number) {
        this.sample_production_number = sample_production_number;
    }

    public String getCam_ip() {
        return cam_ip;
    }

    public void setCam_ip(String cam_ip) {
        this.cam_ip = cam_ip;
    }

    public int getCam_station_number() {
        return cam_station_number;
    }

    public void setCam_station_number(int cam_station_number) {
        this.cam_station_number = cam_station_number;
    }

    public String getSample_number() {
        return sample_number;
    }

    public void setSample_number(String sample_number) {
        this.sample_number = sample_number;
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

    public String getSample_filePath() {
        return sample_filePath;
    }

    public void setSample_filePath(String sample_filePath) {
        this.sample_filePath = sample_filePath;
    }
}
