package chaoyang.tempmonitor.mapper;


import chaoyang.tempmonitor.model.PointTempInfo;
import chaoyang.tempmonitor.model.Room;
import chaoyang.tempmonitor.model.SampleInfo;
import chaoyang.tempmonitor.model.StartTimeAndTimeDiff;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ReportExcelMapper {
    public List<PointTempInfo> getReportExcelTemp(SampleInfo sampleInfo);

    public Float getReportExcelSurf(SampleInfo sampleInfo);

    public List<Room> getCam_ip_position(Map<String, Object> map);

    public StartTimeAndTimeDiff getHighestTemp(SampleInfo sampleInfo);

    public String getNineFiveTime(SampleInfo sampleInfo);

}
