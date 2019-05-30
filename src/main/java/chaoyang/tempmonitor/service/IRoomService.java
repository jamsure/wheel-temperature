package chaoyang.tempmonitor.service;

import chaoyang.tempmonitor.model.Room;

import java.util.List;
import java.util.Map;

public interface IRoomService {

    public List<Room> findRoom (int room_number);

    public List<Room> findRoomMethod (Map<String, Object> map);

    public Room showip (int id );

    public List<Room> ShowIpOne (int id);

    public void insertRoomInfo (Room room);

    public void deleteRoomInfo (Map<String, Object> map);
}
