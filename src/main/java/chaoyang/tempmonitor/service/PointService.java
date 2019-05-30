package chaoyang.tempmonitor.service;

import chaoyang.tempmonitor.mapper.PointMapper;
import chaoyang.tempmonitor.model.PointInfo;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@ComponentScan({"chaoyang.tempmonitor.mapper"})
@Service("pointService")
public class PointService implements IPointService {

    @Resource
    private PointMapper pointMapper;

    @Override
    public void insert(PointInfo pointInfo) {
        pointMapper.insert(pointInfo);
    }

    @Override
    public void update(PointInfo pointInfo) {

    }

    @Override
    public PointInfo find(int id) {
        return null;
    }

    @Override
    public void delete(int sample_id) {
        pointMapper.delete(sample_id);
    }
}
