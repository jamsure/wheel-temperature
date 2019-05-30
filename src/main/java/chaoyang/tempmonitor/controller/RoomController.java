package chaoyang.tempmonitor.controller;


import chaoyang.tempmonitor.model.Room;
import chaoyang.tempmonitor.service.RoomService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import chaoyang.tempmonitor.model.SampleInfoSearch;


@RestController
@ComponentScan({"chaoyang.tempmonitor.service"})
@MapperScan("chaoyang.tempmonitor.mapper")
public class RoomController {
    @Resource
    private RoomService roomService;

    @RequestMapping("/showip")
    public String showip() {
        Room id = roomService.showip(1);
        ModelAndView mav = new ModelAndView();
        return "000" + id.getCam_ip();
    }

    @RequestMapping(value = "/findRoom",method = {RequestMethod.POST})
    @ResponseBody
    public List<Room> findRoom(@RequestParam Map <String,Object> param){
        int room_number = Integer.parseInt(param.get("room_number").toString());
        List<Room> ips= roomService.findRoom(room_number);
        return ips;
    }

    @RequestMapping(value = "/findRoomMethod",method = {RequestMethod.POST})
    @ResponseBody
    public List<Room> findRoomMethod(@RequestParam Map <String,Object> param){
        int room_number = Integer.parseInt(param.get("room_number").toString());
        int cam_station_number = Integer.parseInt(param.get("cam_station_number").toString());
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("room_number", room_number);
        map.put("cam_station_number", cam_station_number);
        List<Room> list= roomService.findRoomMethod(map);
        return list;
    }

    @RequestMapping(value = "/insertRoomInfo",method = {RequestMethod.POST})
    @ResponseBody
    public void insertRoomInfo(HttpServletRequest request){
        String roomInfo1 = request.getParameter("roomInfo");
        Room roomInfo = JSON.parseObject(roomInfo1, new TypeReference<Room>() {});
        Room room = new Room();
        room.setRoom_number(roomInfo.getRoom_number());
        room.setCam_id(roomInfo.getCam_id());
        room.setCam_position(roomInfo.getCam_position());
        room.setCam_ip(roomInfo.getCam_ip());
        room.setCam_user(roomInfo.getCam_user());
        room.setCam_pwd(roomInfo.getCam_pwd());
        room.setCam_port(roomInfo.getCam_port());
        room.setCam_type(roomInfo.getCam_type());
        room.setCam_channel(roomInfo.getCam_channel());
        room.setCam_station_number(roomInfo.getCam_station_number());
        roomService.insertRoomInfo(room);
    }


}
