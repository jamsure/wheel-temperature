package chaoyang.tempmonitor.mapper;

import chaoyang.tempmonitor.model.Room;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface RoomMapper {

    public List<Room> findRoom (int room_number);

    public List<Room> findRoomMethod (Map<String, Object> map);

    public void insertRoomInfo (Room room);

    public void deleteRoomInfo (Map<String, Object> map);

    public Room showip (int id);

    public List<Room> ShowIpOne(int id);

}
