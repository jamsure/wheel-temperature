package chaoyang.tempmonitor.model;

public class ReportExcelInfo {
    private String highestTemp;   //最高温度
    private String highestTempTime;   //达到最高温度时间
    private String reachNFTempTime;     //第一次达到95摄氏度时间
    private String pointOneHighestTemp;   //定点1最高温度
    private String pointTwoHighestTemp;    //定点2最高温度
    private String pointThreeHighestTemp;  //定点3最高温度
    private String pointFourHighestTemp;   //定点4最高温度
    private String pointFiveHighestTemp;     //定点5最高温度
    private String plp_photo_position;

    public String getHighestTemp() {
        return highestTemp;
    }

    public void setHighestTemp(String highestTemp) {
        this.highestTemp = highestTemp;
    }

    public String getHighestTempTime() {
        return highestTempTime;
    }

    public void setHighestTempTime(String highestTempTime) {
        this.highestTempTime = highestTempTime;
    }

    public String getReachNFTempTime() {
        return reachNFTempTime;
    }

    public void setReachNFTempTime(String reachNFTempTime) {
        this.reachNFTempTime = reachNFTempTime;
    }

    public String getPointOneHighestTemp() {
        return pointOneHighestTemp;
    }

    public void setPointOneHighestTemp(String pointOneHighestTemp) {
        this.pointOneHighestTemp = pointOneHighestTemp;
    }

    public String getPointTwoHighestTemp() {
        return pointTwoHighestTemp;
    }

    public void setPointTwoHighestTemp(String pointTwoHighestTemp) {
        this.pointTwoHighestTemp = pointTwoHighestTemp;
    }

    public String getPointThreeHighestTemp() {
        return pointThreeHighestTemp;
    }

    public void setPointThreeHighestTemp(String pointThreeHighestTemp) {
        this.pointThreeHighestTemp = pointThreeHighestTemp;
    }

    public String getPointFourHighestTemp() {
        return pointFourHighestTemp;
    }

    public void setPointFourHighestTemp(String pointFourHighestTemp) {
        this.pointFourHighestTemp = pointFourHighestTemp;
    }

    public String getPointFiveHighestTemp() {
        return pointFiveHighestTemp;
    }

    public void setPointFiveHighestTemp(String pointFiveHighestTemp) {
        this.pointFiveHighestTemp = pointFiveHighestTemp;
    }

    public String getPlp_photo_position() {
        return plp_photo_position;
    }

    public void setPlp_photo_position(String plp_photo_position) {
        this.plp_photo_position = plp_photo_position;
    }
}
