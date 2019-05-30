package chaoyang.tempmonitor.service;

import chaoyang.tempmonitor.mapper.UserMapper;
import chaoyang.tempmonitor.model.User;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@ComponentScan({"chaoyang.tempmonitor.mapper"})
@Service("userService")
public class UserService implements IUserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public void insert(User user){userMapper.insert(user);}

    public void update(User user) {userMapper.update(user);}

    public User find(int id){
        return userMapper.find(id);
    }

    public void delete(int id) {userMapper.delete(id); }
}
