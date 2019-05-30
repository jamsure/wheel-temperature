package chaoyang.tempmonitor.service;


import chaoyang.tempmonitor.mapper.RoomMapper;
import chaoyang.tempmonitor.model.Room;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@ComponentScan({"chaoyang.tempmonitor.mapper"})
@Service("roomService")
public class RoomService implements IRoomService {

    @Resource
    private RoomMapper RoomMapper;

    @Override
    public List<Room> findRoom (int room_number){
        return RoomMapper.findRoom(room_number);
    }

    @Override
    public List<Room> findRoomMethod(Map<String, Object> map) {
        return RoomMapper.findRoomMethod(map);
    }

    @Override
    public Room showip ( int id ) { return  RoomMapper.showip (id ); }


    @Override
    public List<Room> ShowIpOne(int id)
    {
        return RoomMapper.ShowIpOne(id);
    }

    @Override
    public void insertRoomInfo(Room room) {
        RoomMapper.insertRoomInfo(room);
    }

    @Override
    public void deleteRoomInfo(Map<String, Object> map) {
        RoomMapper.deleteRoomInfo(map);
    }

}
