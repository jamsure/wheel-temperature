package chaoyang.tempmonitor.controller;

import chaoyang.tempmonitor.model.SampleInfo;
import chaoyang.tempmonitor.service.SampleService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@ComponentScan({"chaoyang.tempmonitor.service"})
@MapperScan("chaoyang.tempmonitor.mapper")
public class SampleController {

    @Resource
    private SampleService sampleService;

    @RequestMapping(value="/insertSample",method = {RequestMethod.POST})
    @ResponseBody
    public void insertSample(@RequestParam Map<String,Object> param){
        SampleInfo sampleInfo = new SampleInfo();
        sampleInfo.setRoom_number(Integer.parseInt(param.get("room_number").toString()));
        sampleInfo.setSample_reporter_name(param.get("sample_reporter_name").toString());
        sampleInfo.setTire_number(param.get("tire_number").toString());
        sampleInfo.setSample_test_date(param.get("sample_test_date").toString());
        sampleInfo.setSample_inspection_unit(param.get("sample_inspection_unit").toString());
        sampleInfo.setSample_production_number(param.get("sample_production_number").toString());
        sampleInfo.setSample_brand(param.get("sample_brand").toString());
        sampleInfo.setSample_load_rode(param.get("sample_load_rode").toString());
        sampleInfo.setSample_tire_radius(param.get("sample_tire_radius").toString());
        sampleInfo.setSample_conversion(param.get("sample_conversion").toString());
        sampleInfo.setCam_ip(param.get("cam_ip").toString());
        sampleInfo.setCam_station_number(Integer.parseInt(param.get("cam_station_number").toString()));
        sampleService.insert(sampleInfo);

    }
    @RequestMapping(value="/get_current_room_data",method = {RequestMethod.POST})
    @ResponseBody
    public List<SampleInfo> getCurrentroomdata(HttpServletRequest request){
        int roomnumber=Integer.parseInt(request.getParameter("room_number"));
        //System.out.println("wozaizheli"+roomnumber);
        List<SampleInfo> list= sampleService.get_Current_room_data(roomnumber);
//        for(int i=0;i<list.size();i++){
//            System.out.println(list.get(i).getCam_station_number()+" "+list.get(i).getSample_running_state());
//        }
        return list;


    }



}
