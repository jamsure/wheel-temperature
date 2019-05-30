package chaoyang.tempmonitor.service;

import chaoyang.tempmonitor.model.SampleInfo;

import java.util.List;
import java.util.Map;


public interface ISampleService{
    public void insert(SampleInfo sampleInfo);
    public List<SampleInfo> get_Room_state();
    public void update_state(Map<String,String> map);
    public List<SampleInfo> get_Current_room_data(int room_number);

}
