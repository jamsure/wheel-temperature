package chaoyang.tempmonitor.service;


import chaoyang.tempmonitor.model.*;

import java.util.List;
import java.util.Map;

public interface ITempService {
    public void insert(TempInfo tempInfo);

    public void update(TempInfo tempInfo);

    public List<TempInput> find(int sample_id);

    public void delete(int sample_id);

    public List<CamIpAndCamPosi> getCamIP(Map<String, Object> map);

    public List<Integer> getSampleType(Map<String, Object> map);

    public List<String> getTireNumber(Map<String, Object> map);

    public List<String> getTestDate(Map<String, Object> map);

    public List<String> getRuleID(Map<String, Object> map);

    public String getStartTime(Map<String, Object> map);

    public String showImage(Map<String, Object> map);

    public List<Integer> getSampleTypeNum(Map<String, Object> map);

    public List<SearchTemp> getTempInfo(Map<String, Object> map);

    public List<SearchTemp> SearchTemp(int roomid);

    public List<SearchTemp> selectTemp(TempInput tempInput);

    public String getImage(Map<String, Object> map);

    public String getImageAll(Map<String, Object> map);

    public String getCam_position(String cam_ip);

    //get temperature information for the dashboard
    public List<TempDashboard> getTempInfoDash(Map<String, Object> map);

    public List<String> getSampleName1(Map<String, Object> map);

    public SampleInfoHis getSampleInfoHis(Map<String, Object> map);

    public List<QueryData> queryData(Map<String, Object> map);
    public String queryDataCount(Map<String, Object> map);
}
