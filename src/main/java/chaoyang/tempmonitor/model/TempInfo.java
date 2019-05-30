package chaoyang.tempmonitor.model;

public class TempInfo {
    private int id;
    private String tire_number;    //轮胎号
    private int room_number;    //房间编号
    private String cam_ip;      //摄像机ip地址
    private int temp_dwSize;    //结构体大小
    private String temp_dwRelativeTime;   //相对时标
    private String temp_dwAbsTime;    //绝对时标
    private String temp_szRuleName;    //规则名称
    private String temp_byRuleID;     //规则id号
    private String temp_byRuleCalibType;    //规则标定类型：0- 点，1- 框，2- 线
    private int temp_wPresetNo;       //预置点号
    private String temp_byThermometryUnit;   // 测温单位
    private String temp_byDataType;   //  数据状态类型
    private String temp_byRes1;   //保留，置为0
    private String temp_bySpecialPointThermType;    //是否支持特殊点测温，按位表示不同类型的特殊点：
    private String temp_fCenterPointTemperature;   //中心点温度
    private String temp_fHighestPointTemperature;    //最高点温度
    private String temp_fLowestPointTemperature;    //最低点温度
    private String temp_byRes;         //保留，置为0
    private int cam_station_number;   //仓位号

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getTemp_dwSize() {
        return temp_dwSize;
    }

    public void setTemp_dwSize(int temp_dwSize) {
        this.temp_dwSize = temp_dwSize;
    }

    public String getTemp_dwRelativeTime() {
        return temp_dwRelativeTime;
    }

    public void setTemp_dwRelativeTime(String temp_dwRelativeTime) {
        this.temp_dwRelativeTime = temp_dwRelativeTime;
    }

    public String getTemp_dwAbsTime() {
        return temp_dwAbsTime;
    }

    public void setTemp_dwAbsTime(String temp_dwAbsTime) {
        this.temp_dwAbsTime = temp_dwAbsTime;
    }

    public String getTemp_szRuleName() {
        return temp_szRuleName;
    }

    public void setTemp_szRuleName(String temp_szRuleName) {
        this.temp_szRuleName = temp_szRuleName;
    }

    public String getTemp_byRuleID() {
        return temp_byRuleID;
    }

    public void setTemp_byRuleID(String temp_byRuleID) {
        this.temp_byRuleID = temp_byRuleID;
    }

    public String getTemp_byRuleCalibType() {
        return temp_byRuleCalibType;
    }

    public void setTemp_byRuleCalibType(String temp_byRuleCalibType) {
        this.temp_byRuleCalibType = temp_byRuleCalibType;
    }

    public int getTemp_wPresetNo() {
        return temp_wPresetNo;
    }

    public void setTemp_wPresetNo(int temp_wPresetNo) {
        this.temp_wPresetNo = temp_wPresetNo;
    }

    public String getTemp_byThermometryUnit() {
        return temp_byThermometryUnit;
    }

    public void setTemp_byThermometryUnit(String temp_byThermometryUnit) {
        this.temp_byThermometryUnit = temp_byThermometryUnit;
    }

    public String getTemp_byDataType() {
        return temp_byDataType;
    }

    public void setTemp_byDataType(String temp_byDataType) {
        this.temp_byDataType = temp_byDataType;
    }

    public String getTemp_byRes1() {
        return temp_byRes1;
    }

    public void setTemp_byRes1(String temp_byRes1) {
        this.temp_byRes1 = temp_byRes1;
    }

    public String getTemp_bySpecialPointThermType() {
        return temp_bySpecialPointThermType;
    }

    public void setTemp_bySpecialPointThermType(String temp_bySpecialPointThermType) {
        this.temp_bySpecialPointThermType = temp_bySpecialPointThermType;
    }

    public String getTemp_fCenterPointTemperature() {
        return temp_fCenterPointTemperature;
    }

    public void setTemp_fCenterPointTemperature(String temp_fCenterPointTemperature) {
        this.temp_fCenterPointTemperature = temp_fCenterPointTemperature;
    }

    public String getTemp_fHighestPointTemperature() {
        return temp_fHighestPointTemperature;
    }

    public void setTemp_fHighestPointTemperature(String temp_fHighestPointTemperature) {
        this.temp_fHighestPointTemperature = temp_fHighestPointTemperature;
    }

    public String getTemp_fLowestPointTemperature() {
        return temp_fLowestPointTemperature;
    }

    public void setTemp_fLowestPointTemperature(String temp_fLowestPointTemperature) {
        this.temp_fLowestPointTemperature = temp_fLowestPointTemperature;
    }

    public String getTemp_byRes() {
        return temp_byRes;
    }

    public void setTemp_byRes(String temp_byRes) {
        this.temp_byRes = temp_byRes;
    }

    public String getTire_number() {
        return tire_number;
    }

    public void setTire_number(String tire_number) {
        this.tire_number = tire_number;
    }

    public int getCam_station_number() {
        return cam_station_number;
    }

    public void setCam_station_number(int cam_station_number) {
        this.cam_station_number = cam_station_number;
    }


}
