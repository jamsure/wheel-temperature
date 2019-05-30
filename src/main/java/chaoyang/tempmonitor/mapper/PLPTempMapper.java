package chaoyang.tempmonitor.mapper;

import chaoyang.tempmonitor.model.PLPTempInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PLPTempMapper {
    public void insert(PLPTempInfo plpTempInfo);

    public void update(PLPTempInfo plpTempInfo);

    public PLPTempInfo find(int id);

    public void delete(int sample_id);
}
