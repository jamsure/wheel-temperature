package chaoyang.tempmonitor.mapper;


import chaoyang.tempmonitor.model.SampleInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SampleMapper {
    public void insert(SampleInfo sampleInfo);
    public List<SampleInfo> get_Room_state();
    public void update_state(Map<String,String> map);
    public List<SampleInfo> get_Current_room_data(int room_number);

//    public void update(PointInfo pointInfo);

  //  public PointInfo find(int id);

   // public void delete(int sample_id);
}
