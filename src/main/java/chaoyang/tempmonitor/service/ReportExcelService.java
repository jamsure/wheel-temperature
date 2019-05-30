package chaoyang.tempmonitor.service;


import chaoyang.tempmonitor.mapper.ReportExcelMapper;
import chaoyang.tempmonitor.model.PointTempInfo;
import chaoyang.tempmonitor.model.Room;
import chaoyang.tempmonitor.model.SampleInfo;
import chaoyang.tempmonitor.model.StartTimeAndTimeDiff;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@ComponentScan({"chaoyang.tempmonitor.mapper"})
@Service("reportExcelService")
public class ReportExcelService implements IReportExcelService{

    @Resource
    private ReportExcelMapper reportExcelMapper;

    @Override
    public List<PointTempInfo> getReportExcelTemp(SampleInfo sampleInfo) {
        return reportExcelMapper.getReportExcelTemp(sampleInfo);
    }

    @Override
    public Float getReportExcelSurf(SampleInfo sampleInfo) {
        return reportExcelMapper.getReportExcelSurf(sampleInfo);
    }

    @Override
    public StartTimeAndTimeDiff getHighestTemp(SampleInfo sampleInfo) {
        return reportExcelMapper.getHighestTemp(sampleInfo);
    }

    @Override
    public String getNineFiveTime(SampleInfo sampleInfo) {
        return reportExcelMapper.getNineFiveTime(sampleInfo);
    }

    @Override
    public List<Room> getCam_ip_position(Map<String, Object> map) {
        List<Room> list = reportExcelMapper.getCam_ip_position(map);
        return list;
    }
}
