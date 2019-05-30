package chaoyang.tempmonitor.service;

import chaoyang.tempmonitor.model.PointInfo;

public interface IPointService {

    public void insert(PointInfo pointInfo);

    public void update(PointInfo pointInfo);

    public PointInfo find(int sample_id);

    public void delete(int sample_id);

}
