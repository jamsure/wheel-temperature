package chaoyang.tempmonitor.service;

import chaoyang.tempmonitor.model.PLPTempInfo;

public interface IPLPTempService {
    public void insert(PLPTempInfo plpTempInfo);

    public void update(PLPTempInfo plpTempInfo);

    public PLPTempInfo find(int id);

    public void delete(int sample_id);
}
