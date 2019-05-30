package chaoyang.tempmonitor.service;

import chaoyang.tempmonitor.model.PointTempInfo;
import chaoyang.tempmonitor.model.Room;
import chaoyang.tempmonitor.model.SampleInfo;
import chaoyang.tempmonitor.model.StartTimeAndTimeDiff;

import java.util.List;
import java.util.Map;


public interface IReportExcelService {

    public List<PointTempInfo> getReportExcelTemp(SampleInfo sampleInfo);

    public Float getReportExcelSurf(SampleInfo sampleInfo);

    public List<Room> getCam_ip_position(Map<String, Object> map);

    public StartTimeAndTimeDiff getHighestTemp(SampleInfo sampleInfo);

    public String getNineFiveTime(SampleInfo sampleInfo);

}
