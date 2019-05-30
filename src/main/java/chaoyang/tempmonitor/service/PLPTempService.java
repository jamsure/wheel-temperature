package chaoyang.tempmonitor.service;

import chaoyang.tempmonitor.mapper.PLPTempMapper;
import chaoyang.tempmonitor.model.PLPTempInfo;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@ComponentScan({"chaoyang.tempmonitor.mapper"})
@Service("plpTempService")
public class PLPTempService implements IPLPTempService{

    @Resource
    private PLPTempMapper plpTempMapper;

    @Override
    public void insert(PLPTempInfo plpTempInfo) {
        plpTempMapper.insert(plpTempInfo);
    }

    @Override
    public void update(PLPTempInfo plpTempInfo) {

    }

    @Override
    public PLPTempInfo find(int id) {
        return null;
    }

    @Override
    public void delete(int sample_id) {
        plpTempMapper.delete(sample_id);
    }
}
