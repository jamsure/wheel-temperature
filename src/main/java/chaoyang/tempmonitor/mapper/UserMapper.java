package chaoyang.tempmonitor.mapper;

import chaoyang.tempmonitor.model.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    public void insert(User user);

    public void update(User user);

    public void delete(int id);

    public User find(int id);
}
