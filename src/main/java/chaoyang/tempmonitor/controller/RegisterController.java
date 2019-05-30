package chaoyang.tempmonitor.controller;

import chaoyang.tempmonitor.mapper.SampleMapper;
import chaoyang.tempmonitor.model.*;
import chaoyang.tempmonitor.service.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

//import com.fasterxml.jackson.core.type.TypeReference;

/**
 *    函数:      "注册"  按钮单击响应函数
 *     函数描述:	注册登录设备
 */

@RestController
@ComponentScan({"chaoyang.tempmonitor.service"})
@MapperScan("chaoyang.tempmonitor.mapper")
public class RegisterController {
    private String message;

    @Resource
    private SampleService sampleService;
    @Resource
    private SampleMapper sampleMapper;
    @Resource
    private TempService tempService;

    @Resource
    private PointService pointService;

    @Resource
    private PLPTempService plpTempService;


    NativeLong lChannel; //通道号

    static HCNetSDK hCNetSDK = HCNetSDK.INSTANCE;

    HCNetSDK.NET_DVR_IPPARACFG  m_strIpparaCfg;//IP参数


    //设备注册功能
    @RequestMapping("/register")
    private boolean registered(String m_sDeviceIP,int camPort,String camUserName,String camPassword){
        hCNetSDK.NET_DVR_Init();
        int iPort = camPort;
        HCNetSDK.NET_DVR_DEVICEINFO_V30 m_strDeviceInfo = new HCNetSDK.NET_DVR_DEVICEINFO_V30();
        NativeLong lUserID;  //用户句柄
        lUserID = hCNetSDK.NET_DVR_Login_V30(m_sDeviceIP, (short) iPort, camUserName, camPassword, m_strDeviceInfo);
        long userID = lUserID.longValue();
        if (userID == -1)
        {
            m_sDeviceIP = "";//登录未成功,IP置为空
            message =  "注册失败";
            System.out.println(message);
            return false;
        }
        else
        {
            message = "注册成功";
            System.out.println(message);
            return true;
        }

    }



    //根据线程ID查找线程
    public static Thread findThread(String threadName) {
        ThreadGroup group = Thread.currentThread().getThreadGroup();
        while(group != null) {
            Thread[] threads = new Thread[(int)(group.activeCount() * 1.2)];
            int count = group.enumerate(threads, true);
            for(int i = 0; i < count; i++) {
                if(threadName.equals(threads[i].getName())) {
                    return threads[i];
                }
            }
            group = group.getParent();
        }
        return null;
    }

    private void closeThread(String room_number,List<Room> list,String cam_station_number){
        Map<String,String> map=new HashMap<>();
        map.put("room_number",room_number);
        map.put("cam_station_number",cam_station_number);
        sampleService.update_state(map);
        for(int i=0;i<list.size();i++){
            String cam_ip = list.get(i).getCam_ip();
            String threadName = room_number+cam_station_number+cam_ip;
            Thread t = findThread(threadName);
            t.interrupt();
        }

    }

    //关闭各摄像机插入温度信息线程
    @RequestMapping(value="/stopInsertRealTemp",method = {RequestMethod.POST})
    @ResponseBody
    public void stopInsertRealTemp(HttpServletRequest request){
        String cam_station_number1 = request.getParameter("cam_station_number1");
        String cam_station_number2 = request.getParameter("cam_station_number2");
        String room_number = request.getParameter("room_number");
        if(!cam_station_number1.equals("0")  && !cam_station_number2.equals("0")){
            String dataCamInfo1 = request.getParameter("dataCamInfo1");
            List<Room> list1 = JSON.parseArray(dataCamInfo1, Room.class);
            String dataCamInfo2 = request.getParameter("dataCamInfo2");
            List<Room> list2 = JSON.parseArray(dataCamInfo2, Room.class);
            closeThread(room_number,list1,cam_station_number1);
            closeThread(room_number,list2,cam_station_number2);
        }
        if(!cam_station_number1.equals("0") && cam_station_number2.equals("0")){
            String dataCamInfo1 = request.getParameter("dataCamInfo1");
            List<Room> list1 = JSON.parseArray(dataCamInfo1, Room.class);
            closeThread(room_number,list1,cam_station_number1);
        }
        if(cam_station_number1.equals("0") && !cam_station_number2.equals("0")){
            String dataCamInfo2 = request.getParameter("dataCamInfo2");
            List<Room> list2 = JSON.parseArray(dataCamInfo2, Room.class);
            closeThread(room_number,list2,cam_station_number2);
        }

    }

    //实时插入多线摄像机温度
    @RequestMapping(value="/controlRealTempMeas", method = {RequestMethod.POST})
    @ResponseBody
    private void controlRealTempMeas(HttpServletRequest request){
        String filePath1,filePath2;
        long fixStartTime = System.currentTimeMillis();
        String setIntFreq = request.getParameter("setIntervalFreq");
        SetIntervalFrequency setIntervalFreq = JSON.parseObject(setIntFreq, new TypeReference<SetIntervalFrequency>() {});

        //获取sample信息
        SampleInfo sampleInfo1 = null;
        SampleInfo sampleInfo2 = null;
        SampleInfoSearch sampleInfoSearch1 = null;
        SampleInfoSearch sampleInfoSearch2 = null;
        String sample1 = request.getParameter("sampleInfo1");
        String sample2 = request.getParameter("sampleInfo2");
        if(!sample1.equals("0")  && !sample2.equals("0")){
            String dataCamInfo1 = request.getParameter("dataCamInfo1");
            List<Room> list1 = JSON.parseArray(dataCamInfo1, Room.class);
            String dataCamInfo2 = request.getParameter("dataCamInfo2");
            List<Room> list2 = JSON.parseArray(dataCamInfo2, Room.class);
            sampleInfoSearch1 = JSON.parseObject(sample1, new TypeReference<SampleInfoSearch>() {});
            sampleInfo1 = insertSampleInfo(sampleInfoSearch1,setIntervalFreq);
            filePath1 = "D://"+sampleInfo1.getRoom_number()+"//"+sampleInfo1.getCam_station_number()+"//"+sampleInfo1.getSample_test_date()+"//";
            sampleInfo1.setSample_filePath(filePath1);
            for (int i = 0; i < list1.size(); i++) {
                sampleInfo1.setCam_ip(list1.get(i).getCam_ip());
                System.out.println(sampleInfo1.getSample_object_name());
                sampleService.insert(sampleInfo1);
                //sampleMapper.insert(sampleInfo1);
            }

            TempThread t1 = new TempThread(list1, sampleInfo1, fixStartTime, setIntervalFreq);
            t1.getTempByThread();

            sampleInfoSearch2 = JSON.parseObject(sample2, new TypeReference<SampleInfoSearch>() {});
            sampleInfo2 = insertSampleInfo(sampleInfoSearch2,setIntervalFreq);
            filePath2 = "D://"+sampleInfo2.getRoom_number()+"//"+sampleInfo2.getCam_station_number()+"//"+sampleInfo2.getSample_test_date()+"//";
            sampleInfo2.setSample_filePath(filePath2);

            for (int i = 0; i < list2.size(); i++) {
                sampleInfo2.setCam_ip(list2.get(i).getCam_ip());
                sampleService.insert(sampleInfo2);
            }
            TempThread t2 = new TempThread(list2, sampleInfo2, fixStartTime, setIntervalFreq);
            t2.getTempByThread();
        }else{
            if(!sample1.equals("0")){
                String dataCamInfo1 = request.getParameter("dataCamInfo1");
                List<Room> list1 = JSON.parseArray(dataCamInfo1, Room.class);
                //System.out.println("我在这里"+list1.get(0).getCam_ip());
                sampleInfoSearch1 = JSON.parseObject(sample1, new TypeReference<SampleInfoSearch>() {});
                sampleInfo1 = insertSampleInfo(sampleInfoSearch1,setIntervalFreq);
                filePath1 = "D://"+sampleInfo1.getRoom_number()+"//"+sampleInfo1.getCam_station_number()+"//"+sampleInfo1.getSample_test_date()+"//";
                sampleInfo1.setSample_filePath(filePath1);
                for (int i = 0; i < list1.size(); i++) {
                    sampleInfo1.setCam_ip(list1.get(i).getCam_ip());
                    sampleService.insert(sampleInfo1);
                }
                TempThread t1 = new TempThread(list1, sampleInfo1, fixStartTime, setIntervalFreq);
                t1.getTempByThread();
            }
            if(!sample2.equals("0")){

                String dataCamInfo2 = request.getParameter("dataCamInfo2");
                List<Room> list2 = JSON.parseArray(dataCamInfo2, Room.class);
                sampleInfoSearch2 = JSON.parseObject(sample2, new TypeReference<SampleInfoSearch>() {});
                sampleInfo2 = insertSampleInfo(sampleInfoSearch2,setIntervalFreq);
                filePath2 = "D://"+sampleInfo2.getRoom_number()+"//"+sampleInfo2.getCam_station_number()+"//"+sampleInfo2.getSample_test_date()+"//";
                sampleInfo2.setSample_filePath(filePath2);
                for (int i = 0; i < list2.size(); i++) {
                    System.out.println(list2.get(i).getCam_ip());
                    sampleInfo2.setCam_ip(list2.get(i).getCam_ip());
                    sampleService.insert(sampleInfo2);
                }
                TempThread t2 = new TempThread(list2, sampleInfo2, fixStartTime, setIntervalFreq);
                t2.getTempByThread();
            }
        }
    }
    //实时获取机床最新状态，以及仓位信息
    @RequestMapping(value="/get_Room_state", method = {RequestMethod.POST})
    @ResponseBody
    private Map<Integer,String> getRoomstate(HttpServletRequest request){
        List<SampleInfo> list=sampleService.get_Room_state();
        Map<Integer,String> roomstate=new HashMap<>();
        for(int i=0;i<9;i++){
            roomstate.put(i+1,"false");
        }
        for(int i=0;i<list.size();i++){

                roomstate.put(list.get(i).getRoom_number(),list.get(i).getSample_running_state());
        }
        //return "haoaha";
        return roomstate;

    }
    public SampleInfo insertSampleInfo(SampleInfoSearch sampleInfoSearch,SetIntervalFrequency setIntervalFreq){
        SampleInfo sampleInfo = new SampleInfo();
        sampleInfo.setRoom_number(sampleInfoSearch.getRoom_number());
        sampleInfo.setSample_reporter_name(sampleInfoSearch.getSample_reporter_name());
        sampleInfo.setSample_number(sampleInfoSearch.getSample_number());
        sampleInfo.setSample_load_rode(sampleInfoSearch.getSample_load_rode());
        sampleInfo.setSample_test_date(sampleInfoSearch.getSample_test_date());
        sampleInfo.setSample_conversion(sampleInfoSearch.getSample_conversion());
        sampleInfo.setSample_inspection_unit(sampleInfoSearch.getSample_inspection_unit());
        sampleInfo.setSample_tire_radius(sampleInfoSearch.getSample_tire_radius());
        sampleInfo.setSample_production_number(sampleInfoSearch.getSample_production_number());
        sampleInfo.setTire_number(sampleInfoSearch.getTire_number());
        sampleInfo.setCam_station_number(sampleInfoSearch.getCam_station_number());
        sampleInfo.setSample_specification(sampleInfoSearch.getSample_specification());
        sampleInfo.setSample_brand(sampleInfoSearch.getSample_brand());
        sampleInfo.setSample_level(sampleInfoSearch.getSample_level());
        sampleInfo.setSample_pattern(sampleInfoSearch.getSample_pattern());
        sampleInfo.setSample_person(sampleInfoSearch.getSample_person());
        sampleInfo.setSample_scheme(sampleInfoSearch.getSample_scheme());
        sampleInfo.setSample_object_name(sampleInfoSearch.getSample_object_name());
        sampleInfo.setSample_running_state("true");
        if(setIntervalFreq!=null){
            sampleInfo.setFirst_point_frequency(setIntervalFreq.getFirst_point_frequency());
            sampleInfo.setFirst_time_interval(setIntervalFreq.getFirst_time_interval());
            sampleInfo.setSecond_point_frequency(setIntervalFreq.getSecond_point_frequency());
            sampleInfo.setSecond_time_interval(setIntervalFreq.getSecond_time_interval());
            sampleInfo.setThird_point_frequency(setIntervalFreq.getThird_point_frequency());
        }

        return sampleInfo;
    }

    //抓图
    private String capturePicture(NativeLong lUserID, SampleInfo sampleInfo, String cam_ip,int sample_type, String currTime) {
        lChannel = new NativeLong(1);

        long startTime = System.currentTimeMillis();
        HCNetSDK.NET_DVR_JPEGPARA jpeg = new HCNetSDK.NET_DVR_JPEGPARA();
        jpeg.wPicSize = 2;  // 设置图片的分辨率
        jpeg.wPicQuality = 1;  // 设置图片质量

        IntByReference a = new IntByReference();   //设置图片大小
        ByteBuffer jpegBuffer = ByteBuffer.allocate(1024 * 1024);
        currTime = currTime.replace(" ","_");
        currTime = currTime.replace(":","_");
        String filePath = "D://"+sampleInfo.getRoom_number()+"//"+sampleInfo.getCam_station_number()+"//"+sampleInfo.getSample_test_date()+"//"+cam_ip+"//";
        String fileName =  String.valueOf(sampleInfo.getRoom_number())+"_"+cam_ip+"_"+sampleInfo.getTire_number()+"_"+String.valueOf(sample_type)+"_"+currTime+".jpg";
        String fileWholePath = filePath+fileName;
        File file = new File(fileWholePath);
        if(!file.getParentFile().exists()){     //若该路径下不存在此文件目录则创建
            boolean result = file.getParentFile().mkdirs();
            if (!result) {
                System.out.println("创建失败");
            }
        }
        boolean is = hCNetSDK.NET_DVR_CaptureJPEGPicture_NEW(lUserID, lChannel, jpeg, jpegBuffer, 1024 * 1024, a);
        System.out.println("抓图到内存耗时：[" + (System.currentTimeMillis() - startTime) + "ms]");
        if (is) {
            System.out.println("抓取成功,返回长度：" + a.getValue());
        } else {
            System.out.println("抓取失败：" + hCNetSDK.NET_DVR_GetLastError());
        }

        // 存储本地，写入内容
        BufferedOutputStream outputStream = null;
        try {
            outputStream = new BufferedOutputStream(new FileOutputStream(file));
            outputStream.write(jpegBuffer.array(), 0, a.getValue());
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return fileWholePath;
    }

    //温度信息回调函数
    public class FRemoteConfigCallback implements HCNetSDK.FRemoteConfigCallback
    {
        private long currTime;
        private long timeDiff;
        private boolean captureFlag = false;
        private long fixStartTime;
        private String m_sDeviceIP;
        private SampleInfo sampleInfo;
        private String plp_photo_position;
        private NativeLong lUserID;
        List<String> list = new ArrayList<String>();

        public FRemoteConfigCallback(NativeLong lUserID,String m_sDeviceIP, SampleInfo sampleInfo,long fixStartTime) {
            this.m_sDeviceIP = m_sDeviceIP;
            this.fixStartTime = fixStartTime;
            this.sampleInfo = sampleInfo;
            this.lUserID = lUserID;
        }

        public void invoke(int dwType, Pointer lpBuffer, int dwBufLen, Pointer pUserData) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//24小时制
            Date date = new Date();
            Timestamp dateTime = new Timestamp(date.getTime());
            date.setTime(fixStartTime);
            String fSTime = sdf.format(date);
            if(dwType == HCNetSDK.NET_SDK_CALLBACK_TYPE.NET_SDK_CALLBACK_TYPE_DATA.getValue()) {
                TempInfo tempInfo = new TempInfo();
                PointInfo pointInfo = new PointInfo();
                PLPTempInfo plpTempInfo = new PLPTempInfo();
                HCNetSDK.NET_DVR_THERMOMETRY_UPLOAD lpThermometry = new HCNetSDK.NET_DVR_THERMOMETRY_UPLOAD();
                lpThermometry.write();
                Pointer ptemp = lpThermometry.getPointer();
                ptemp.write(0, lpBuffer.getByteArray(0, lpThermometry.size()), 0, lpThermometry.size());
                lpThermometry.read();

                tempInfo.setTire_number(sampleInfo.getTire_number());
                tempInfo.setCam_station_number(sampleInfo.getCam_station_number());
                tempInfo.setRoom_number(sampleInfo.getRoom_number());
                tempInfo.setCam_ip(m_sDeviceIP);
                tempInfo.setTemp_dwSize(lpThermometry.dwSize);
                tempInfo.setTemp_dwRelativeTime(String.valueOf(lpThermometry.dwRelativeTime));
                tempInfo.setTemp_dwAbsTime(String.valueOf(lpThermometry.dwAbsTime));
                tempInfo.setTemp_szRuleName(String.valueOf(lpThermometry.szRuleName));
                tempInfo.setTemp_byRuleID(String.valueOf(lpThermometry.byRuleID));
                tempInfo.setTemp_byRuleCalibType(String.valueOf(lpThermometry.byRuleCalibType));
                tempInfo.setTemp_wPresetNo(lpThermometry.wPresetNo);
                tempInfo.setTemp_byThermometryUnit(String.valueOf(lpThermometry.byThermometryUnit));
                tempInfo.setTemp_byDataType(String.valueOf(lpThermometry.byDataType));
                tempInfo.setTemp_byRes1(String.valueOf(lpThermometry.byRes1));
                tempInfo.setTemp_bySpecialPointThermType(String.valueOf(lpThermometry.bySpecialPointThermType));
                tempInfo.setTemp_fCenterPointTemperature(String.valueOf(lpThermometry.fCenterPointTemperature));
                tempInfo.setTemp_fHighestPointTemperature(String.valueOf(lpThermometry.fHighestPointTemperature));
                tempInfo.setTemp_fLowestPointTemperature(String.valueOf(lpThermometry.fLowestPointTemperature));
                tempInfo.setTemp_byRes(String.valueOf(lpThermometry.byRes));

                plpTempInfo.setTire_number(sampleInfo.getTire_number());
                plpTempInfo.setCam_station_number(sampleInfo.getCam_station_number());
                plpTempInfo.setSample_type(lpThermometry.byRuleCalibType);
                plpTempInfo.setCam_ip(m_sDeviceIP);
                plpTempInfo.setPlp_currTime(dateTime);

                String ruleID = String.valueOf(lpThermometry.byRuleID);
//                System.out.println("kanaknwo "+ruleID);
                int sample_type_number = Integer.parseInt(ruleID);

                currTime = System.currentTimeMillis();
                timeDiff = currTime - fixStartTime;
                long td = timeDiff / 1000;
                int tDiff = Integer.parseInt(String.valueOf(td));
                String time = sdf.format(currTime);
                if(!captureFlag){
                    plp_photo_position = capturePicture(lUserID, sampleInfo,m_sDeviceIP, lpThermometry.byRuleCalibType, time);
                    captureFlag = true;
                }

                if (!list.contains(ruleID)) {
                    if (lpThermometry.byRuleCalibType == 0) {
                            pointInfo.setPoint_type_index(1);
                            pointInfo.setTire_number(sampleInfo.getTire_number());
                            pointInfo.setSample_type(lpThermometry.byRuleCalibType);
                            pointInfo.setSample_type_number(sample_type_number);
                            pointInfo.setPoint_x(lpThermometry.struPointThermCfg.struPoint.fx);
                            pointInfo.setPoint_y(lpThermometry.struPointThermCfg.struPoint.fy);
                            pointInfo.setCam_station_number(sampleInfo.getCam_station_number());

                            plpTempInfo.setPlp_fMaxTemperature(lpThermometry.struPointThermCfg.fTemperature);
                            plpTempInfo.setPlp_fMinTemperature(lpThermometry.struPointThermCfg.fTemperature);
                            plpTempInfo.setPlp_fAverageTemperature(lpThermometry.struPointThermCfg.fTemperature);
                            plpTempInfo.setPlp_fTemperatureDiff(0);
                            plpTempInfo.setPlp_dwPointNum(1);
                            plpTempInfo.setPlp_byRes(String.valueOf(lpThermometry.struPointThermCfg.byRes));

                            plpTempInfo.setSample_type_number(sample_type_number);
                            plpTempInfo.setPlp_fixStartTime(fSTime);
                            plpTempInfo.setPlp_timeDiff(tDiff);
                            plpTempInfo.setPlp_photo_position(plp_photo_position);
                            pointService.insert(pointInfo);
                            plpTempService.insert(plpTempInfo);
                            tempService.insert(tempInfo);
                            System.out.println("点测温");
                            System.out.println("温度：" + lpThermometry.struPointThermCfg.fTemperature);
                            System.out.println("监测点坐标：(" + lpThermometry.struPointThermCfg.struPoint.fx + "," + lpThermometry.struPointThermCfg.struPoint.fy + ")");

                    } else if (lpThermometry.byRuleCalibType == 1 || lpThermometry.byRuleCalibType == 2) {
                            for (int i = 1; i <= lpThermometry.struLinePolygonThermCfg.struRegion.dwPointNum; i++) {
                                int j = i;
                                pointInfo.setTire_number(sampleInfo.getTire_number());
                                pointInfo.setSample_type(lpThermometry.byRuleCalibType);
                                pointInfo.setSample_type_number(sample_type_number);
                                pointInfo.setPoint_type_index(i);
                                pointInfo.setPoint_x(lpThermometry.struLinePolygonThermCfg.struRegion.struPos[j - 1].fx);
                                pointInfo.setPoint_y(lpThermometry.struLinePolygonThermCfg.struRegion.struPos[j - 1].fy);
                                pointInfo.setCam_station_number(sampleInfo.getCam_station_number());
                                pointService.insert(pointInfo);
                            }
                            plpTempInfo.setPlp_fMaxTemperature(lpThermometry.struLinePolygonThermCfg.fMaxTemperature);
                            plpTempInfo.setPlp_fMinTemperature(lpThermometry.struLinePolygonThermCfg.fMinTemperature);
                            plpTempInfo.setPlp_fAverageTemperature(lpThermometry.struLinePolygonThermCfg.fAverageTemperature);
                            plpTempInfo.setPlp_fTemperatureDiff(lpThermometry.struLinePolygonThermCfg.fTemperatureDiff);
                            plpTempInfo.setPlp_dwPointNum(lpThermometry.struLinePolygonThermCfg.struRegion.dwPointNum);
                            plpTempInfo.setPlp_byRes(String.valueOf(lpThermometry.struLinePolygonThermCfg.byRes));

                            plpTempInfo.setPlp_photo_position(plp_photo_position);
                            plpTempInfo.setSample_type_number(sample_type_number);
                            plpTempInfo.setPlp_fixStartTime(fSTime);
                            plpTempInfo.setPlp_timeDiff(tDiff);
                            plpTempService.insert(plpTempInfo);
                            tempService.insert(tempInfo);
                            System.out.println("线测温监测点个数：" + lpThermometry.struLinePolygonThermCfg.struRegion.dwPointNum);
                            for (int i = 0; i < lpThermometry.struLinePolygonThermCfg.struRegion.dwPointNum; i++) {
                                System.out.println("第" + i + "监测点横坐标：(" + lpThermometry.struLinePolygonThermCfg.struRegion.struPos[i].fx + "," + lpThermometry.struLinePolygonThermCfg.struRegion.struPos[i].fy + ")");
                            }
                        }

                    if (lpThermometry != null || !lpThermometry.equals(null)) {
                        lpThermometry.clear();
                        lpThermometry = null;
                    }
                } else if (dwType == HCNetSDK.NET_SDK_CALLBACK_TYPE.NET_SDK_CALLBACK_TYPE_STATUS.getValue()) {
                    int dwStatus = lpBuffer.getInt(0);
                    if (dwStatus == HCNetSDK.NET_SDK_CALLBACK_STATUS.NET_SDK_CALLBACK_STATUS_SUCCESS.getValue()) {
                        System.out.println("123");
                    } else if (dwStatus == HCNetSDK.NET_SDK_CALLBACK_STATUS.NET_SDK_CALLBACK_STATUS_FAILED.getValue()) {
                        System.out.println("456");
                    }
                }
              list.add(ruleID);
            }
        }
    }



    class TempThread {

        private List<Room> list;
        private SampleInfo sampleInfo;
        private long fixStartTime;
        private SetIntervalFrequency setIntervalFreq;

        public TempThread() { }

        public TempThread(List<Room> list, SampleInfo sampleInfo,long fixStartTime,SetIntervalFrequency setIntervalFreq) {
            this.list = list;
            this.sampleInfo = sampleInfo;
            this.fixStartTime = fixStartTime;
            this.setIntervalFreq = setIntervalFreq;
        }

        public void getTempByThread() {
            for (int i = 0; i < list.size(); i++) {
                String camUserName = list.get(i).getCam_user();
                String camPassword = list.get(i).getCam_pwd();
                String m_sDeviceIP = list.get(i).getCam_ip();
                int camPort = Integer.parseInt(list.get(i).getCam_port().toString()) * 100;
                sampleInfo.setCam_ip(m_sDeviceIP);
                Thread t = new Thread(new obtainTempThread(m_sDeviceIP, camPort, camUserName, camPassword, sampleInfo, fixStartTime,setIntervalFreq));
                String tName = String.valueOf(sampleInfo.getRoom_number())+String.valueOf(sampleInfo.getCam_station_number())+m_sDeviceIP;
                t.setName(tName);
                t.start();
            }
        }
    }

    class obtainTempThread implements Runnable{
        private String m_sDeviceIP;
        private int camPort;
        private String camUserName;
        private String camPassword;
        private long fixStartTime;
        private SampleInfo sampleInfo;
        private SetIntervalFrequency setIntervalFreq;
        private long getCurrTime;
        private long getCurrTimeSec;

        public obtainTempThread(){}
        public obtainTempThread(String m_sDeviceIP, int camPort, String camUserName, String camPassword, SampleInfo sampleInfo,long fixStartTime, SetIntervalFrequency setIntervalFreq){
            this.m_sDeviceIP = m_sDeviceIP;
            this.camPort = camPort;
            this.camUserName = camUserName;
            this.camPassword = camPassword;
            this.fixStartTime = fixStartTime;
            this.sampleInfo = sampleInfo;
            this.setIntervalFreq = setIntervalFreq;
        }
        @Override
        public void run() {
            boolean initSuc = hCNetSDK.NET_DVR_Init();
            if (!initSuc) {
                System.out.println("SDK初始化失败");
            }
            hCNetSDK.NET_DVR_SetConnectTime(2000, 1);
            hCNetSDK.NET_DVR_SetReconnect(10000, true);

            HCNetSDK.NET_DVR_DEVICEINFO_V30 m_strDeviceInfo = new HCNetSDK.NET_DVR_DEVICEINFO_V30();
            NativeLong lUserID;  //用户句柄
            lUserID = hCNetSDK.NET_DVR_Login_V30(m_sDeviceIP, (short) camPort, camUserName, camPassword, m_strDeviceInfo);
            long userID = lUserID.longValue();
            if(userID<0){
                System.out.println("注册失败！"+hCNetSDK.NET_DVR_GetLastError());
                hCNetSDK.NET_DVR_Cleanup();
            }
            boolean flag = false;
            long first_time_interval = new Float(setIntervalFreq.getFirst_time_interval()*60*60*1000).longValue();
            long second_time_interval = new Float(setIntervalFreq.getSecond_time_interval()*60*60*1000).longValue();
            long first_point_freq = new Float(setIntervalFreq.getFirst_point_frequency()*60*1000).longValue();
            long second_point_freq = new Float(setIntervalFreq.getSecond_point_frequency()*60*1000).longValue();
            long third_point_freq = new Float(setIntervalFreq.getThird_point_frequency()*60*1000).longValue();
            while (!Thread.currentThread().isInterrupted()) {
                getCurrTime = System.currentTimeMillis();
                if(!flag){
                    realTimeTempMeasurement(m_sDeviceIP, lUserID, sampleInfo, fixStartTime);
                    getCurrTimeSec = getCurrTime;
                    flag = true;
                    sleepThread(first_point_freq);
                }else{
                    if((getCurrTime-fixStartTime)<first_time_interval){
                        if(getCurrTime-getCurrTimeSec>=first_point_freq){
                            realTimeTempMeasurement(m_sDeviceIP, lUserID, sampleInfo, fixStartTime);
                            getCurrTimeSec = getCurrTime;
                            sleepThread(first_point_freq);
                        }
                    }else if((getCurrTime-fixStartTime)>=first_time_interval && (getCurrTime-fixStartTime)<(first_time_interval+second_time_interval)){
                        if(getCurrTime-getCurrTimeSec>=second_point_freq){
                            realTimeTempMeasurement(m_sDeviceIP, lUserID, sampleInfo, fixStartTime);
                            getCurrTimeSec = getCurrTime;
                            sleepThread(second_point_freq);
                        }
                    }else{
                        if(getCurrTime-getCurrTimeSec>=third_point_freq) {
                            realTimeTempMeasurement(m_sDeviceIP, lUserID, sampleInfo, fixStartTime);
                            getCurrTimeSec = getCurrTime;
                            sleepThread(third_point_freq);
                        }
                    }
                }
            }
                hCNetSDK.NET_DVR_Logout(lUserID);
                hCNetSDK.NET_DVR_Cleanup();
        }

        public void sleepThread(long freq){
            try {
                Thread.currentThread().sleep(freq-5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("让cpu休息一下");
//                e.printStackTrace();
            }
        }

        public synchronized void realTimeTempMeasurement(String m_sDeviceIP, NativeLong lUserID, SampleInfo sampleInfo,long fixStartTime) {
            int dwChannel = 1;
            HCNetSDK.NET_DVR_REALTIME_THERMOMETRY_COND struThermCond = new HCNetSDK.NET_DVR_REALTIME_THERMOMETRY_COND();
            struThermCond.dwSize = struThermCond.size();
            struThermCond.byRuleID = 0;
            struThermCond.byMode = 1;
            struThermCond.dwChan = dwChannel;
            struThermCond.write();
            NativeLong lHandle;
            FRemoteConfigCallback fr = new FRemoteConfigCallback(lUserID, m_sDeviceIP,sampleInfo, fixStartTime);
            lHandle = hCNetSDK.NET_DVR_StartRemoteConfig(lUserID, hCNetSDK.NET_DVR_GET_REALTIME_THERMOMETRY, struThermCond.getPointer(), struThermCond.size(), fr, null);
            long lhandle = lHandle.longValue();
            if (lhandle < 0) {
                System.out.println("失败啦" + hCNetSDK.NET_DVR_GetLastError());
            } else {
                System.out.println("NET_DVR_GET_REALTIME_THERMOMETRY is successful!");
            }

            try {
                Thread.sleep(5000);  //等待一段时间，接收实时测温结果
            } catch (InterruptedException e) {
                System.out.println("在沉睡中被停止！进入catch！" + Thread.currentThread().isInterrupted());
                Thread.currentThread().interrupt();
            }
            if (!(hCNetSDK.NET_DVR_StopRemoteConfig(lHandle))) {
                System.out.println("NET_DVR_StopRemoteConfig failed, error code:" + hCNetSDK.NET_DVR_GetLastError());
            }
//                hCNetSDK.NET_DVR_Logout(lUserID);
//                hCNetSDK.NET_DVR_Cleanup();
        }
    }


}
