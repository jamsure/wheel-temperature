package chaoyang.tempmonitor.controller;

import chaoyang.tempmonitor.model.*;
import chaoyang.tempmonitor.service.PLPTempService;
import chaoyang.tempmonitor.service.PointService;
import chaoyang.tempmonitor.service.TempService;
import com.alibaba.fastjson.JSON;
import org.apache.batik.transcoder.Transcoder;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.lang.System.out;

//import net.sf.json.JSONObject;


@RestController
@ComponentScan({"chaoyang.tempmonitor.service"})
@MapperScan("chaoyang.tempmonitor.mapper")
public class TempController {
    @Resource
    private TempService tempService;
    @Resource
    private PointService pointService;
    @Resource
    private PLPTempService plpTempService;

    @RequestMapping("/deleteTemp")
    private void deleteTemp(int sample_id) {
        tempService.delete(sample_id);
        pointService.delete(sample_id);
        plpTempService.delete(sample_id);
    }

    @RequestMapping("/updateTemp")
    private void updateTemp(int sample_id, int room_number) {
    }

    @RequestMapping("/findTemp")
    private List<TempInput> findTemp(int sample_id) {
        List<TempInput> list = tempService.find(sample_id);
        for (int i = 0; i < list.size(); i++) {
            //        System.out.println("第"+i+"个点坐标：（"+list.get(i).getPoint_x()+","+list.get(i).getPoint_y()+")");
        }
        return list;
    }

    @RequestMapping(value = "/getSampleType", method = {RequestMethod.POST})
    @ResponseBody
    public List<Integer> getSampleType(@RequestParam Map<String, Object> param) {
        String tire_number = param.get("tire_number").toString();
        String cam_ip = param.get("cam_ip").toString();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("tire_number", tire_number);
        map.put("cam_ip", cam_ip);
        List<Integer> list = tempService.getSampleType(map);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("list", list);
        return list;
    }


    @RequestMapping(value = "/getSampleName", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> getSampleName(@RequestParam Map<String, Object> param) {
        int room_number = Integer.parseInt(param.get("room_number").toString());
        String cam_ip = param.get("cam_ip").toString();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("room_number", room_number);
        map.put("cam_ip", cam_ip);
        List<String> list = new ArrayList<String>();
        list = tempService.getSampleName1(map);
        Map<String, Object> hashmap = new HashMap<String, Object>();
        hashmap.put("list", list);
        return hashmap;
    }

    @RequestMapping(value = "/getTireNumber", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> getTireNumber(@RequestParam Map<String, Object> param) {
        int room_number = Integer.parseInt(param.get("room_number").toString());
        String cam_ip = param.get("cam_ip").toString();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("room_number", room_number);
        map.put("cam_ip", cam_ip);
        List<String> list = new ArrayList<String>();
        list = tempService.getTireNumber(map);
        Map<String, Object> hashmap = new HashMap<String, Object>();
        hashmap.put("list", list);
        return hashmap;
    }

    @RequestMapping(value = "/getRuleID", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> getRuleID(@RequestParam Map<String, Object> param) {
        int room_number = Integer.parseInt(param.get("room_number").toString());
        int cam_station_number = Integer.parseInt(param.get("cam_station_number").toString());
        String temp_cam_ip = param.get("cam_ip").toString();
        String[] cam_ip = temp_cam_ip.split("-");
        String tire_number = param.get("tire_number").toString();
        String sample_test_date = param.get("sample_test_date").toString();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("room_number", room_number);
        map.put("cam_station_number", cam_station_number);
        map.put("cam_ip", cam_ip[0]);
        map.put("tire_number", tire_number);
        map.put("sample_test_date", sample_test_date);
        List<String> list = new ArrayList<String>();
        list = tempService.getRuleID(map);
        String fixStartTime = tempService.getStartTime(map);
        Map<String, Object> hashmap = new HashMap<String, Object>();
        hashmap.put("list", list);
        hashmap.put("fixStartTime", fixStartTime);
        return hashmap;
    }

    @RequestMapping(value = "/getTestDate", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> getTestDate(@RequestParam Map<String, Object> param) {
        int room_number = Integer.parseInt(param.get("room_number").toString());
        String cam_ip = param.get("cam_ip").toString();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("room_number", room_number);
        map.put("cam_ip", cam_ip);
        List<String> list = tempService.getTestDate(map);
        Map<String, Object> hashmap = new HashMap<String, Object>();
        hashmap.put("list", list);
        return hashmap;
    }

    @RequestMapping(value = "/getSampleTypeNum", method = {RequestMethod.POST})
    @ResponseBody
    public List<Integer> getSampleTypeNum(@RequestParam Map<String, Object> param) {
        String tire_number = param.get("tire_number").toString();
        int sample_type = Integer.parseInt(param.get("sample_type").toString());
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("tire_number", tire_number);
        map.put("sample_type", sample_type);
        List<Integer> list = tempService.getSampleTypeNum(map);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("list", list);
        return list;
    }

    @RequestMapping(value = "/getTempInfo", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> getTempInfo(@RequestParam Map<String, Object> param) {
        int cam_station_number = Integer.parseInt(param.get("cam_station_number").toString());
        int room_number = Integer.parseInt(param.get("room_number").toString());
        String cam_ip = param.get("cam_ip").toString();
        String tire_number = param.get("tire_number").toString();
        String startT = param.get("sample_test_date").toString();
        String sample_test_date = startT +" 00:00:00 123";
        String stopT = param.get("stopTime").toString();
        Calendar cStartTime = Calendar.getInstance();
        Calendar cStopTime = Calendar.getInstance();
        long startTime = 0;
        long stopTime;
        long diffTime = 0;
        try {
            cStartTime.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS").parse(sample_test_date));
            startTime = cStartTime.getTimeInMillis();
            if(!stopT.equals("0")){
                stopT = stopT + " 00:00:00 123";
                cStopTime.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS").parse(stopT));
                stopTime = cStopTime.getTimeInMillis();
                diffTime = (stopTime-startTime)/1000;
            }
        } catch (ParseException e) {
            out.println("日期格式转换错误！");
            e.printStackTrace();
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("room_number", room_number);
        map.put("cam_station_number", cam_station_number);
        map.put("cam_ip", cam_ip);
        map.put("tire_number", tire_number);
        map.put("sample_test_date", sample_test_date);
        map.put("plp_timeDiff", diffTime);
        List<SearchTemp> list = tempService.getTempInfo(map);
        String imageLink = tempService.getImage(map);
        System.out.println(imageLink);
        imageLink = imageLink.substring(7);
        System.out.println(imageLink);
        Map<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("list", list);
        hashMap.put("imageLink", imageLink);
        hashMap.put("list", list);
        return hashMap;
    }


    @RequestMapping(value = "/getDiffCamTempInfo", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> getDiffCamTempInfo(@RequestParam Map<String, Object> param) {
        int cam_station_number = Integer.parseInt(param.get("cam_station_number").toString());
        //System.out.println("data[0].cam_station_number="+cam_station_number);
        int room_number = Integer.parseInt(param.get("room_number").toString());
        //System.out.println("data[0].room_number="+room_number);
        String tire_number = param.get("tire_number").toString();
        //System.out.println("data[0].tire_number="+tire_number);
        String startT = param.get("sample_test_date").toString();
        String sample_test_date = startT +" 00:00:00 123";
        String stopT = param.get("stopTime").toString();
        Calendar cStartTime = Calendar.getInstance();
        Calendar cStopTime = Calendar.getInstance();
        long startTime = 0;
        long stopTime;
        long diffTime = 0;
        try {
            cStartTime.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS").parse(sample_test_date));
            startTime = cStartTime.getTimeInMillis();
            if(!stopT.equals("0")){
                stopT = stopT + " 00:00:00 123";
//                System.out.println(stopT);
                cStopTime.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS").parse(stopT));
                stopTime = cStopTime.getTimeInMillis();
                diffTime = (stopTime-startTime)/1000;
            }
        } catch (ParseException e) {
            out.println("日期格式转换错误！");
            e.printStackTrace();
        }



        Map<String, Object> map = new HashMap<String, Object>();
        map.put("room_number", room_number);
        map.put("cam_station_number", cam_station_number);
        map.put("tire_number", tire_number);
        map.put("sample_test_date", sample_test_date);
        map.put("plp_timeDiff", diffTime);
        List<SearchTemp> listTL = new ArrayList<>(),listTL1=new ArrayList<>(),listTL2=new ArrayList<>(),listTM = new ArrayList<>(),listTM1=new ArrayList<>(),listTM2=new ArrayList<>(),listTR = new ArrayList<>(),listTR1=new ArrayList<>(),listTR2=new ArrayList<>();
        List<SearchTemp> listL = null,listL1=null,listL2=null,listM = null ,listM1=null,listM2=null,listR = null,listR1=null,listR2=null;
        List<CamIpAndCamPosi> IPAndPosi = tempService.getCamIP(map);

        for(int i=0;i<IPAndPosi.size();i++){
            String camPosi = IPAndPosi.get(i).getCam_position();
            String cam_ip = IPAndPosi.get(i).getCam_ip();
            map.put("cam_ip", cam_ip);
            if(camPosi.equals("L")){

                List<SearchTemp> listLSum = tempService.getTempInfo(map);
                for(int j=0;j<listLSum.size();j++){
                   if(listLSum.get(j).sample_type_number==1){listTL.add(listLSum.get(j));}
                    if(listLSum.get(j).sample_type_number==2){listTL1.add(listLSum.get(j));}
                    if(listLSum.get(j).sample_type_number==3){listTL2.add(listLSum.get(j));}
                }
                //listTL = tempService.getTempInfo(map);
            }else if(camPosi.equals("M")){
                List<SearchTemp> listMSum = tempService.getTempInfo(map);
                for(int j=0;j<listMSum.size();j++){
                    if(listMSum.get(j).sample_type_number==1){listTM.add(listMSum.get(j));}
                    if(listMSum.get(j).sample_type_number==2){listTM1.add(listMSum.get(j));}
                    if(listMSum.get(j).sample_type_number==3){listTM2.add(listMSum.get(j));}
                }
//                listTM = tempService.getTempInfo(map);
            }else if(camPosi.equals("R")){
                List<SearchTemp> listRSum = tempService.getTempInfo(map);
                for(int j=0;j<listRSum.size();j++){
                    if(listRSum.get(j).sample_type_number==1){listTR.add(listRSum.get(j));}
                    if(listRSum.get(j).sample_type_number==2){listTR1.add(listRSum.get(j));}
                    if(listRSum.get(j).sample_type_number==3){listTR2.add(listRSum.get(j));}
                }
//                listTR = tempService.getTempInfo(map);
            }
        }
        if(listTL != null){
            listL = getMinusTen(listTL);
        }
        if(listTL1 != null){
            listL1 = getMinusTen(listTL1);
        }
        if(listTL2 != null){
            listL2 = getMinusTen(listTL2);
        }
        if(listTM != null){
            listM = getMinusTen(listTM);
        }
        if(listTM1 != null){
            listM1 = getMinusTen(listTM1);
        }
        if(listTM2 != null){
            listM2 = getMinusTen(listTM2);
        }
        if(listTR  != null){
            listR = getMinusTen(listTR);
        }
        if(listTR1  != null){
            listR1 = getMinusTen(listTR1);
        }
        if(listTR2  != null){
            listR2 = getMinusTen(listTR2);
        }
        Map<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("listL", listL);
        hashMap.put("listL1", listL1);
        hashMap.put("listL2", listL2);
        hashMap.put("listM", listM);
        hashMap.put("listM1", listM1);
        hashMap.put("listM2", listM2);
        hashMap.put("listR", listR);
        hashMap.put("listR1", listR1);
        hashMap.put("listR2", listR2);
        hashMap.put("listIP", IPAndPosi);
        return hashMap;
    }

    public List<SearchTemp> getMinusTen(List<SearchTemp> listT){
        int indexN = 20;
        List<SearchTemp> list = new ArrayList<SearchTemp>();
            for(int a=0;a<listT.size();a+=indexN){
                list.add(listT.get(a))  ;
            }
            if(listT.size()%indexN != 0){
                list.add(listT.get(listT.size()-1));
            }
        return list;
    }


    @RequestMapping(value = "getTempInfoDash", method = {RequestMethod.POST})
    @ResponseBody
    public List<TempDashboard> getTempInfoDash(@RequestParam Map<String, Object> param) {
        int room_number = Integer.parseInt(param.get("room_number").toString());
        String tire_number = param.get("tire_number").toString();
        String cam_station_number = param.get("cam_station_number").toString();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("room_number", room_number);
        map.put("tire_number", tire_number);
        map.put("cam_station_number", cam_station_number);

        List<TempDashboard> listT = tempService.getTempInfoDash(map);
        int indexN = 10;
        List<TempDashboard> list = new ArrayList<TempDashboard>();
        for(int a=0;a<listT.size();a+=indexN){
            list.add(listT.get(a))  ;
        }
        if(listT.size()%indexN != 0){
            list.add(listT.get(listT.size()-1));
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("list", list);
        return list;
    }

//完成选择文件功能
    @RequestMapping(value = "ChooseFile", method = {RequestMethod.POST})
    @ResponseBody
    public ShowSampleInfo ChooseFile(@RequestParam Map<String, Object> param) {
        System.setProperty("java.awt.headless", "false");
        String fileNameInit = param.get("fileName").toString();
        ShowSampleInfo showSampleInfo = new ShowSampleInfo();
        List<String> list = new ArrayList<String>();
          String[] fileName = fileNameInit.split("_");
          showSampleInfo.setSample_specification(fileName[0]);
          showSampleInfo.setSample_level(fileName[1]);
          showSampleInfo.setSample_pattern(fileName[2]);
          showSampleInfo.setSample_test_date(fileName[3]);
          showSampleInfo.setSample_number(fileName[4]);
          showSampleInfo.setSample_person(fileName[5]);
          showSampleInfo.setTire_number(fileName[6]);
          showSampleInfo.setRoom_number(Integer.parseInt(fileName[7]));
          showSampleInfo.setCam_station_number(Integer.parseInt(fileName[8]));
          String[] cam_ipL = fileName[9].split(" ");
          for(int i=0;i<cam_ipL.length;i++) {
              String cam_position = tempService.getCam_position(cam_ipL[i]);
              String cam_position_ip = cam_ipL[i]+"-"+cam_position;
              list.add(cam_position_ip);
          }
          showSampleInfo.setCam_IPList(list);
//        }

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("showSampleInfo", showSampleInfo);
        return showSampleInfo;
    }

    //测试一下传递json
    @RequestMapping(value = "/testJson", method = {RequestMethod.POST})
    @ResponseBody
    public String testJson(HttpServletRequest request) {
        String jsonStr = request.getParameter("jsonStr");
        List<Room> jsonArrar = JSON.parseArray(jsonStr, Room.class);
        return "hahhah";
    }

    class Param {
        public List<Room> room;
    }

    @RequestMapping(value = "/getImage", method = {RequestMethod.POST})
    @ResponseBody
    public String getImage(@RequestParam Map<String, Object> param) {
        String sample_test_date = param.get("sample_test_date").toString();
        String tire_number = param.get("tire_number").toString();
        int room_number = Integer.parseInt(param.get("room_number").toString());
        int cam_station_number = Integer.parseInt(param.get("cam_station_number").toString());
        int timeDiff = Integer.parseInt(param.get("timeDiff").toString());
        String cam_ip = param.get("cam_ip").toString();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("sample_test_date", sample_test_date);
        map.put("timeDiff", timeDiff);
        map.put("cam_ip", cam_ip);
        map.put("tire_number", tire_number);
        map.put("room_number", room_number);
        map.put("cam_station_number", cam_station_number);
        String imageLink = tempService.getImage(map);
        String fImageLink = imageLink.substring(4);
        return fImageLink;
    }


    @RequestMapping(value = "/showImage", method = {RequestMethod.POST})
    @ResponseBody
    public String showImage(@RequestParam Map<String, Object> param) {
        int room_number = Integer.parseInt(param.get("room_number").toString());
        String cam_ip = param.get("cam_ip").toString();
        String tire_number = param.get("tire_number").toString();
        int sample_type = Integer.parseInt(param.get("sample_type").toString());
        int sample_type_number = Integer.parseInt(param.get("sample_type_number").toString());
        String plp_currTime = param.get("plp_currTime").toString();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("room_number", room_number);
        map.put("cam_ip", cam_ip);
        map.put("tire_number", tire_number);
        map.put("sample_type", sample_type);
        map.put("sample_type_number", sample_type_number);
        map.put("plp_currTime", plp_currTime);
        String image = tempService.showImage(map);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("image", image);
        return image;
    }

    @RequestMapping(value = "/searchTemp", method = {RequestMethod.POST})
    @ResponseBody
    public List<SearchTemp> SearchTemp(@RequestParam Map<String, Object> param) {
        int roomid = Integer.parseInt(param.get("roomid").toString());
        List<SearchTemp> temps = tempService.SearchTemp(roomid);
        return temps;
    }

    @RequestMapping(value = "/selectTemp", produces = "text/html;charset=UTF-8;", method = {RequestMethod.POST})
    @ResponseBody
    public List<SearchTemp> selectTemp(@RequestParam Map<String, Object> param) {
        TempInput tempInput = new TempInput();
        tempInput.setRoom_number(Integer.parseInt(param.get("room_number").toString()));
        tempInput.setCam_ip(param.get("cam_ip").toString());
        tempInput.settire_number(param.get("tire_number").toString());
        tempInput.setSample_type(Integer.parseInt(param.get("sample_type").toString()));
        tempInput.setSample_type_number(Integer.parseInt(param.get("sample_type_number").toString()));
        List<SearchTemp> temps = tempService.selectTemp(tempInput);
        return temps;
    }

    @RequestMapping(value = "/saveAsImage", method = {RequestMethod.POST})
    @ResponseBody
    public void toSaveAsImage(HttpServletResponse response,HttpServletRequest request) throws ServletException, IOException{
        request.setCharacterEncoding("utf-8");
        //设置UTF-8编码，解决乱码问题
           String type = request.getParameter("type");
           String svg = request.getParameter("svg");
           String filename = request.getParameter("filename");
           filename = filename==null?"chart":filename;
           ServletOutputStream out = response.getOutputStream();
           if (null != type && null != svg) {
               svg = svg.replaceAll(":rect", "rect");
               String ext = "";
               Transcoder t = null;
               if (type.equals("image/png")) {
                   ext = "png";
                   t = new PNGTranscoder();
               }
               else if (type.equals("image/jpeg")){
                   ext = "jpg";
                   t = new JPEGTranscoder();
               }
               response.addHeader("Content-Disposition", "attachment; filename="+ filename + "."+ext);
               response.addHeader("Content-Type", type);

               if (null != t) {
                   TranscoderInput input = new TranscoderInput(new StringReader(svg));
                   TranscoderOutput output = new TranscoderOutput(out);
                   try {
                       t.transcode(input, output);
                   } catch (TranscoderException e) {
                       out.print("Problem transcoding stream. See the web logs for more details.");
                       e.printStackTrace();
                   }
                   OutputStreamWriter writer = new OutputStreamWriter(out, "UTF-8");
                   writer.append(svg);
                   writer.close();
               } else
                   out.print("Invalid type: " + type);
           } else {
               response.addHeader("Content-Type", "text/html");
               out.println("Usage:\n\tParameter [svg]: The DOM Element to be converted." +
                       "\n\tParameter [type]: The destination MIME type for the elment to be transcoded.");
           }
               out.flush();
               out.close();
    }
    //保存折线图到指定路径
    @RequestMapping(value = "/saveImageToLocal")
    public String SaveImageToLocal(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String svg1 = request.getParameter("svg1");
        String svg2 = request.getParameter("svg2");
        String room_number = request.getParameter("room_number");
        String position1 = request.getParameter("position1");
        String position2 = request.getParameter("position2");
        String sample_test_date1 = request.getParameter("sample_test_date1");
        String sample_test_date2 = request.getParameter("sample_test_date2");
        String tire_number1 = request.getParameter("tire_number1");
        String tire_number2 = request.getParameter("tire_number2");
        if(position1.equals("true")) //如果1号仓位选中
        {
            String picName1 = room_number + "-" + "1"  + "-" + sample_test_date1 +"-"+ tire_number1 + ".jpeg";
            String picPath1 = "d://" + room_number + "//" + "1" + "//" +sample_test_date1+"//"+ picName1;
            try {
                SvgPngConverter.convertToPng(svg1,picPath1);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TranscoderException e) {
                e.printStackTrace();
            }
        }
        if (position2.equals("true"))
        {
            String picName2 = room_number + "-" + "2"  + "-" + sample_test_date2+"-"+ tire_number2 + ".jpeg";
            String picPath2 = "d://" + room_number + "//" + "2" + "//"+sample_test_date2+"//"+ picName2;
            try {
                SvgPngConverter.convertToPng(svg2,picPath2);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TranscoderException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @RequestMapping(value = "saveImageToLocalTwo")
    public String SaveImageToLocalTwo(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String svg = request.getParameter("svg");
        String picName = new SimpleDateFormat("yyyyyMMddHHmmssSSS").format(new Date()) + ".jpeg";
        String picPath = "d://" + picName;
        try{
            SvgPngConverter.convertToPng(svg,picPath);
        } catch (IOException e){
            e.printStackTrace();
        } catch (TranscoderException e){
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "saveImageToLocalThree")
    public String SaveImageToLocalThree(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String svg = request.getParameter("svg");
        String picName = new SimpleDateFormat("yyyyyMMddHHmmss").format(new Date()) + ".jpeg";
        String picPath = "d://" + picName;
        try{
            SvgPngConverter.convertToPng(svg,picPath);
        } catch (IOException e){
            e.printStackTrace();
        } catch (TranscoderException e){
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/queryData", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> queryData(@RequestParam Map<String, Object> param) {
        int room_number,cam_station_number;
        int limit=Integer.parseInt(param.get("nums").toString());
        int pagestart=(Integer.parseInt(param.get("curr").toString())-1)*limit;
        String roomN = param.get("room_number").toString();
        String camSN = param.get("cam_station_number").toString();
        if(roomN.equals(null) || roomN.equals("") || roomN == null || roomN == ""){
            room_number = -1;
        }else{
            room_number = Integer.parseInt(param.get("room_number").toString());
        }

        if(camSN.equals(null) || camSN.equals("") || camSN == null || camSN == ""){
            cam_station_number = -1;
        }else{
            cam_station_number = Integer.parseInt(param.get("cam_station_number").toString());
        }
        String tire_number = param.get("tire_number").toString();
        String sample_specification = param.get("sample_specification").toString();
        String sample_level = param.get("sample_level").toString();
        String sample_pattern = param.get("sample_pattern").toString();
        String sample_number = param.get("sample_number").toString();
        String sample_person = param.get("sample_person").toString();
        String sample_object_name=param.get("sample_object_name").toString();
        String sample_test_date = param.get("sample_test_date").toString();
        String stopTime = param.get("stopTime").toString();

        Map<String, Object> map = new HashMap<String, Object>();
//        System.out.println("roomnumber="+room_number);
        map.put("room_number", String.valueOf(room_number));
//        System.out.println("roomnumber="+map.get(room_number));
        map.put("cam_station_number", cam_station_number);
        map.put("tire_number", tire_number);
        map.put("sample_specification", sample_specification);
        map.put("sample_level", sample_level);
        map.put("sample_pattern", sample_pattern);
        map.put("sample_number", sample_number);
        map.put("sample_person", sample_person);
        map.put("sample_object_name",sample_object_name);
        map.put("sample_test_date", sample_test_date);
        map.put("stopTime", stopTime);
        map.put("curr",pagestart);
        map.put("nums",limit);
//        for(int i=0;i<map.size();i++){
//            System.out.println("我在这里"+map.get("room_number"));
//            System.out.println("我在这里"+map.get("stopTime"));
//        }

        //System.out.println("limit="+map.get("curr")+"page="+map.get("nums"));
        List<QueryData> list = tempService.queryData(map);
        String count=tempService.queryDataCount(map);
//            System.out.println("stoptime="+map.get(stopTime));
//            System.out.println("rooN="+map.get(room_number));

        Map<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("data", list);
        hashMap.put("code",0);
        hashMap.put("msg",":");
        hashMap.put("count",count);
        return hashMap;
    }


}
