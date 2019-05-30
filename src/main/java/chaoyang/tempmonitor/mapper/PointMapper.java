package chaoyang.tempmonitor.mapper;

import chaoyang.tempmonitor.model.PointInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PointMapper {

    public void insert(PointInfo pointInfo);

    public void update(PointInfo pointInfo);

    public PointInfo find(int id);

    public void delete(int sample_id);
}
