package chaoyang.tempmonitor.service;


import chaoyang.tempmonitor.mapper.SampleMapper;
import chaoyang.tempmonitor.model.SampleInfo;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@ComponentScan({"chaoyang.tempmonitor.mapper"})
@Service("sampleService")
public class SampleService implements ISampleService{

    @Resource
    private SampleMapper sampleMapper;

    @Override
    public void insert(SampleInfo sampleInfo) {
        sampleMapper.insert(sampleInfo);
    }
    @Override
    public List<SampleInfo> get_Room_state(){return sampleMapper.get_Room_state();}
    @Override
    public void update_state(Map<String,String> map){ sampleMapper.update_state(map); }
    @Override
    public List<SampleInfo> get_Current_room_data(int room_number){ return sampleMapper.get_Current_room_data(room_number);}
}
