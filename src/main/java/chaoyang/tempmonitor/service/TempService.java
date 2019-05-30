package chaoyang.tempmonitor.service;

import chaoyang.tempmonitor.mapper.TempMapper;
import chaoyang.tempmonitor.model.*;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@ComponentScan({"chaoyang.tempmonitor.mapper"})
@Service("tempService")
public class TempService implements ITempService {
    @Resource
    private TempMapper tempMapper;

    @Override
    public void insert(TempInfo tempInfo) {
        tempMapper.insert(tempInfo);
    }

    @Override
    public void update(TempInfo tempInfo) {

    }

    @Override
    public List<TempInput> find(int sample_id) {
        return tempMapper.find(sample_id);
    }

    @Override
    public void delete(int sample_id) {
        System.out.println("11"+sample_id);
        tempMapper.delete(sample_id);
    }

    @Override
    public List<CamIpAndCamPosi> getCamIP(Map<String, Object> map) {
        return tempMapper.getCamIP(map);
    }

    @Override
    public List<Integer> getSampleType(Map<String, Object> map) {
        return tempMapper.getSampleType(map);
    }

    @Override
    public List<String> getTireNumber(Map<String, Object> map) {
        return tempMapper.getTireNumber(map);
    }

    @Override
    public String getStartTime(Map<String, Object> map) {
        return tempMapper.getStartTime(map);
    }

    @Override
    public List<String> getRuleID(Map<String, Object> map) {
        return tempMapper.getRuleID(map);
    }

    @Override
    public List<String> getTestDate(Map<String, Object> map) {
        return tempMapper.getTestDate(map);
    }

    @Override
    public String showImage(Map<String, Object> map) {
        return tempMapper.showImage(map);
    }

    @Override
    public List<Integer> getSampleTypeNum(Map<String, Object> map) {
        return tempMapper.getSampleTypeNum(map);
    }

    @Override
    public List<SearchTemp> getTempInfo(Map<String, Object> map) {
        return tempMapper.getTempInfo(map);
    }

    @Override
    public List<SearchTemp> SearchTemp(int roomid)
    {
        return tempMapper.SearchTemp(roomid);
    }

    @Override
    public List<SearchTemp> selectTemp(TempInput tempInput) {
        return tempMapper.selectTemp(tempInput);
    }

    @Override
    public String getCam_position(String cam_ip)
    {
        return tempMapper.getCam_position(cam_ip);
    }

    @Override
    public String getImage(Map<String,Object> map)
    {
        return tempMapper.getImage(map);
    }

    @Override
    public String getImageAll(Map<String,Object> map)
    {
        return tempMapper.getImageAll(map);
    }

    //get the temperature information for dashboard
    @Override
    public List<TempDashboard> getTempInfoDash(Map<String,Object> map)
    {
        return tempMapper.getTempInfoDash(map);
    }

    @Override
    public List<String> getSampleName1(Map<String, Object> map) {
        return tempMapper.getSampleName(map);
    }

    @Override
    public SampleInfoHis getSampleInfoHis(Map<String, Object> map){
        return tempMapper.getSampleInfoHis(map);
    }

    @Override
    public List<QueryData> queryData(Map<String, Object> map){
        return tempMapper.queryData(map);
    }
    @Override
    public String queryDataCount(Map<String, Object> map){
        return tempMapper.queryDataCount(map);
    }
}
