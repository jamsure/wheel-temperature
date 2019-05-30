package chaoyang.tempmonitor.controller;

import chaoyang.tempmonitor.model.*;
import chaoyang.tempmonitor.service.ReportExcelService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/***
 * 生成excel报表
 * title 标题名称
 */

@RestController
@ComponentScan({"chaoyang.tempmonitor.service"})
@MapperScan("chaoyang.tempmonitor.mapper")
public class ReportExcelController {
    @Resource
    private ReportExcelService reportExcelService;

    //生成报表
    @RequestMapping(value="/generateExcelReport",method = {RequestMethod.POST})
    @ResponseBody
    public void generateExcelReport(HttpServletRequest request){
        RegisterController registerC = new RegisterController();
        SetIntervalFrequency setIntervalFreq=null;
        //获取sample信息
        SampleInfo sampleInfo1 = null;
        SampleInfo sampleInfo2 = null;
        SampleInfoSearch sampleInfoSearch1 = null;
        SampleInfoSearch sampleInfoSearch2 = null;

        String sample1 = request.getParameter("sampleInfo1");
        String sample2 = request.getParameter("sampleInfo2");
        if(!sample1.equals("0")  && !sample2.equals("0")){
            sampleInfoSearch1 = JSON.parseObject(sample1, new TypeReference<SampleInfoSearch>() {});
            sampleInfoSearch2 = JSON.parseObject(sample2, new TypeReference<SampleInfoSearch>() {});
            sampleInfo1 = registerC.insertSampleInfo(sampleInfoSearch1,setIntervalFreq);
            sampleInfo2 = registerC.insertSampleInfo(sampleInfoSearch2,setIntervalFreq);
            getExcelReport(sampleInfo1,sampleInfo2);
        }else{
            if(!sample1.equals("0")){
                sampleInfoSearch1 = JSON.parseObject(sample1, new TypeReference<SampleInfoSearch>() {});
                sampleInfo1 = registerC.insertSampleInfo(sampleInfoSearch1,setIntervalFreq);
                getExcelReportOne(sampleInfo1);
            }

            if(!sample2.equals("0")){
                sampleInfoSearch2 = JSON.parseObject(sample2, new TypeReference<SampleInfoSearch>() {});
                sampleInfo2 = registerC.insertSampleInfo(sampleInfoSearch2,setIntervalFreq);
                getExcelReportTwo(sampleInfo2);
            }
        }
    }

    //生成1号和2号工位报表信息
public void getExcelReport(SampleInfo sampleInfo1,SampleInfo sampleInfo2){   //List<SampleInfo> list, SampleInfo sampleInfo,String cam_ip
    StringBuffer fatherFileName1 = new StringBuffer();
    StringBuffer fatherFileName2 = new StringBuffer();
    Map<String, Object> room1 = new HashMap<String, Object>();
    Map<String, Object> room2 = new HashMap<String, Object>();
    room1.put("room_number",sampleInfo1.getRoom_number());
    room1.put("cam_station_number",sampleInfo1.getCam_station_number());
    room2.put("room_number",sampleInfo2.getRoom_number());
    room2.put("cam_station_number",sampleInfo2.getCam_station_number());
    List<Room> cam_station_one,cam_station_two;
    String fileName1, fileName2, filePath,filePath2;
    cam_station_two = reportExcelService.getCam_ip_position(room2);
    cam_station_one = reportExcelService.getCam_ip_position(room1);
    ReportExcelInfo reportExcel_one_L = null, reportExcel_one_M = null, reportExcel_one_R = null,reportExcel_two_L = null, reportExcel_two_M = null, reportExcel_two_R = null;

    for(int i=0;i<cam_station_one.size();i++){
        sampleInfo1.setCam_ip(cam_station_one.get(i).getCam_ip());
        fatherFileName1.append(cam_station_one.get(i).getCam_ip()+" ");
        if(cam_station_one.get(i).getCam_position().equals("L")){
            reportExcel_one_L = returnReportExcel(sampleInfo1);
        }
        if(cam_station_one.get(i).getCam_position().equals("M")){
            reportExcel_one_M = returnReportExcel(sampleInfo1);
        }
        if(cam_station_one.get(i).getCam_position().equals("R")){
            reportExcel_one_R = returnReportExcel(sampleInfo1);
        }
    }
    fatherFileName1.deleteCharAt(fatherFileName1.length()-1);
    for(int j=0;j<cam_station_two.size();j++){
        sampleInfo2.setCam_ip(cam_station_two.get(j).getCam_ip());
        fatherFileName2.append(cam_station_two.get(j).getCam_ip()+" ");
        if(cam_station_two.get(j).getCam_position().equals("L")){
            reportExcel_two_L = returnReportExcel(sampleInfo2);
        }
        if(cam_station_two.get(j).getCam_position().equals("M")){
            reportExcel_two_M = returnReportExcel(sampleInfo2);
        }
        if(cam_station_two.get(j).getCam_position().equals("R")){
            reportExcel_two_R = returnReportExcel(sampleInfo2);
        }
    }
    fatherFileName2.deleteCharAt(fatherFileName2.length()-1);
    //规格+层级+花纹+试验日期+试制编号+送样人+轮胎工号
    //fileName1 = sampleInfo1.getSample_specification()+"_"+sampleInfo1.getSample_level()+"_"+sampleInfo1.getSample_pattern()+"_"+sampleInfo1.getSample_test_date()+"_"+sampleInfo1.getSample_number()+"_"+sampleInfo1.getSample_person()+"_"+sampleInfo1.getTire_number()+"_"+sampleInfo1.getRoom_number()+"_"+sampleInfo1.getCam_station_number()+"_"+fatherFileName1+"_.xls";
    fileName1 = sampleInfo1.getSample_specification()+"_"+sampleInfo1.getSample_level()+"_"+sampleInfo1.getSample_pattern()+"_"+sampleInfo1.getSample_test_date()+"_"+sampleInfo1.getSample_number()+"_"+sampleInfo1.getSample_person()+"_"+sampleInfo1.getTire_number()+".xls";
    fileName2 = sampleInfo2.getSample_specification()+"_"+sampleInfo2.getSample_level()+"_"+sampleInfo2.getSample_pattern()+"_"+sampleInfo2.getSample_test_date()+"_"+sampleInfo2.getSample_number()+"_"+sampleInfo2.getSample_person()+"_"+sampleInfo2.getTire_number()+".xls";
    filePath = "D://"+sampleInfo1.getRoom_number()+"//"+sampleInfo1.getCam_station_number()+"//"+sampleInfo1.getSample_test_date()+"//";
    filePath2= "D://"+sampleInfo2.getRoom_number()+"//"+sampleInfo2.getCam_station_number()+"//"+sampleInfo2.getSample_test_date()+"//";

    HSSFWorkbook wb = new HSSFWorkbook();       //创建工作簿
    HSSFSheet sheet = wb.createSheet(fileName1);      //创建工作表
    HSSFWorkbook wb2 = new HSSFWorkbook();       //创建工作簿
    //HSSFSheet sheet2 = wb2.createSheet(fileName2);      //创建工作表
    sheet.setDefaultColumnWidth(16);    //设置默认列宽

    CellRangeAddress cellRangeAddress_0_0_0_9 = new CellRangeAddress(0,0,0,9);   //创建合并单元格
    sheet.addMergedRegion(cellRangeAddress_0_0_0_9);

    HSSFCellStyle xssfCellStyle = wb.createCellStyle();
    Font font = wb.createFont();
    Row row_0 = sheet.createRow(0);       //创建行
    Cell cell_0_0 = row_0.createCell(0);    //创建列
    cell_0_0.setCellValue("红外检测报告");
    font.setFontHeightInPoints((short)20);
    xssfCellStyle.setFont(font);
    xssfCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);   //居中对齐
    cell_0_0.setCellStyle(xssfCellStyle);

    CellRangeAddress cellRangeAddress_1_1_0_9 = new CellRangeAddress(1,1,0,9);   //创建合并单元格
    sheet.addMergedRegion(cellRangeAddress_1_1_0_9);

    Row row_1 = sheet.createRow(1);
    Cell cell_1_0 = row_1.createCell(0);
    cell_1_0.setCellValue("一、测试信息");
    CellRangeAddress cellRangeAddress_2_2_0_1 = new CellRangeAddress(2,2,0,1);
    sheet.addMergedRegion(cellRangeAddress_2_2_0_1);

    HSSFCellStyle hssfCellStyle = wb.createCellStyle();
    hssfCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);   //水平居中对齐


    Row row_2 = sheet.createRow(2);
    Cell cell_2_0 = row_2.createCell(0);
    cell_2_0.setCellValue("报告编号");
    Cell cell_2_3 = row_2.createCell(3);
    cell_2_3.setCellValue("测试机台");
    CellRangeAddress cellRangeAddress_2_2_4_5 = new CellRangeAddress(2,2,4,5);
    sheet.addMergedRegion(cellRangeAddress_2_2_4_5);

    Cell cell_2_6 = row_2.createCell(6);
    cell_2_6.setCellValue("测试日期");
    CellRangeAddress cellRangeAddress_2_2_7_9 = new CellRangeAddress(2,2,7,9);
    sheet.addMergedRegion(cellRangeAddress_2_2_7_9);
    row_2.setRowStyle(hssfCellStyle);
    Row row_3 = sheet.createRow(3);
    Cell cell_3_0 = row_3.createCell(0);
    cell_3_0.setCellValue("工位");
    Cell cell_3_1 = row_3.createCell(1);
    cell_3_1.setCellValue("规格");
    Cell cell_3_2 = row_3.createCell(2);
    cell_3_2.setCellValue("层级");
    Cell cell_3_3 = row_3.createCell(3);
    cell_3_3.setCellValue("花纹");
    Cell cell_3_4 = row_3.createCell(4);
    cell_3_4.setCellValue("品牌");
    Cell cell_3_5 = row_3.createCell(5);
    cell_3_5.setCellValue("胎号");
    Cell cell_3_6 = row_3.createCell(6);
    cell_3_6.setCellValue("胎冠区域最高温度");
    Cell cell_3_7 = row_3.createCell(7);
    cell_3_7.setCellValue("胎内最高温度");
    Cell cell_3_8 = row_3.createCell(8);
    cell_3_8.setCellValue("胎外最高温度");
    Cell cell_3_9 = row_3.createCell(9);
    cell_3_9.setCellValue("方案");
    row_3.setRowStyle(hssfCellStyle);
    Row row_4 = sheet.createRow(4);
    Cell cell_4_0 = row_4.createCell(0);
    cell_4_0.setCellValue("1#");
    row_4.setRowStyle(hssfCellStyle);
    Row row_5 = sheet.createRow(5);
    Cell cell_5_0 = row_5.createCell(0);
    cell_5_0.setCellValue("2#");
    row_5.setRowStyle(hssfCellStyle);

    CellRangeAddress cellRangeAddress_8_8_0_9 = new CellRangeAddress(8,8,0,9);
    sheet.addMergedRegion(cellRangeAddress_8_8_0_9);
    Row row_8 = sheet.createRow(8);
    Cell cell_8_0 = row_8.createCell(0);
    cell_8_0.setCellValue("二、测试过程数据");

    CellRangeAddress cellRangeAddress_6_7_0_9 = new CellRangeAddress(6,7,0,9);   //创建合并单元格
    sheet.addMergedRegion(cellRangeAddress_6_7_0_9);

    CellRangeAddress cellRangeAddress_9_9_0_9 = new CellRangeAddress(9,9,0,9);   //创建合并单元格
    sheet.addMergedRegion(cellRangeAddress_9_9_0_9);
    CellRangeAddress cellRangeAddress_10_10_0_1 = new CellRangeAddress(10,10,0,1);   //创建合并单元格
    sheet.addMergedRegion(cellRangeAddress_10_10_0_1);
    CellRangeAddress cellRangeAddress_10_10_2_4 = new CellRangeAddress(10,10,2,4);   //创建合并单元格
    sheet.addMergedRegion(cellRangeAddress_10_10_2_4);
    CellRangeAddress cellRangeAddress_10_10_5_6 = new CellRangeAddress(10,10,5,6);   //创建合并单元格
    sheet.addMergedRegion(cellRangeAddress_10_10_5_6);
    CellRangeAddress cellRangeAddress_10_10_7_9 = new CellRangeAddress(10,10,7,9);   //创建合并单元格
    sheet.addMergedRegion(cellRangeAddress_10_10_7_9);

    CellRangeAddress cellRangeAddress_10_12_2_4 = new CellRangeAddress(10,12,2,4);   //创建合并单元格
    sheet.addMergedRegion(cellRangeAddress_10_12_2_4);
    CellRangeAddress cellRangeAddress_10_12_7_9 = new CellRangeAddress(10,12,7,9);   //创建合并单元格
    sheet.addMergedRegion(cellRangeAddress_10_12_7_9);

    CellRangeAddress cellRangeAddress__21_24_2_4= new CellRangeAddress(21,24,2,4);   //创建合并单元格
    sheet.addMergedRegion(cellRangeAddress__21_24_2_4);
    CellRangeAddress cellRangeAddress__21_24_7_9= new CellRangeAddress(21,24,7,9);   //创建合并单元格
    sheet.addMergedRegion(cellRangeAddress__21_24_7_9);

    CellRangeAddress cellRangeAddress_13_13_2_3 = new CellRangeAddress(13,13,2,3);   //创建合并单元格
    sheet.addMergedRegion(cellRangeAddress_13_13_2_3);
    CellRangeAddress cellRangeAddress_14_14_2_3 = new CellRangeAddress(14,14,2,3);   //创建合并单元格
    sheet.addMergedRegion(cellRangeAddress_14_14_2_3);
    CellRangeAddress cellRangeAddress_15_15_2_3 = new CellRangeAddress(15,15,2,3);   //创建合并单元格
    sheet.addMergedRegion(cellRangeAddress_15_15_2_3);
    CellRangeAddress cellRangeAddress_16_16_2_3 = new CellRangeAddress(16,16,2,3);   //创建合并单元格
    sheet.addMergedRegion(cellRangeAddress_16_16_2_3);
    CellRangeAddress cellRangeAddress_17_17_2_3 = new CellRangeAddress(17,17,2,3);   //创建合并单元格
    sheet.addMergedRegion(cellRangeAddress_17_17_2_3);
    CellRangeAddress cellRangeAddress_18_18_2_3 = new CellRangeAddress(18,18,2,3);   //创建合并单元格
    sheet.addMergedRegion(cellRangeAddress_18_18_2_3);
    CellRangeAddress cellRangeAddress_19_19_2_3 = new CellRangeAddress(19,19,2,3);   //创建合并单元格
    sheet.addMergedRegion(cellRangeAddress_19_19_2_3);
    CellRangeAddress cellRangeAddress_20_20_2_3 = new CellRangeAddress(20,20,2,3);   //创建合并单元格
    sheet.addMergedRegion(cellRangeAddress_20_20_2_3);

    CellRangeAddress cellRangeAddress_13_13_7_8 = new CellRangeAddress(13,13,7,8);   //创建合并单元格
    sheet.addMergedRegion(cellRangeAddress_13_13_7_8);
    CellRangeAddress cellRangeAddress_14_14_7_8 = new CellRangeAddress(14,14,7,8);   //创建合并单元格
    sheet.addMergedRegion(cellRangeAddress_14_14_7_8);
    CellRangeAddress cellRangeAddress_15_15_7_8 = new CellRangeAddress(15,15,7,8);   //创建合并单元格
    sheet.addMergedRegion(cellRangeAddress_15_15_7_8);
    CellRangeAddress cellRangeAddress_16_16_7_8 = new CellRangeAddress(16,16,7,8);   //创建合并单元格
    sheet.addMergedRegion(cellRangeAddress_16_16_7_8);
    CellRangeAddress cellRangeAddress_17_17_7_8 = new CellRangeAddress(17,17,7,8);   //创建合并单元格
    sheet.addMergedRegion(cellRangeAddress_17_17_7_8);
    CellRangeAddress cellRangeAddress_18_18_7_8 = new CellRangeAddress(18,18,7,8);   //创建合并单元格
    sheet.addMergedRegion(cellRangeAddress_18_18_7_8);
    CellRangeAddress cellRangeAddress_19_19_7_8 = new CellRangeAddress(19,19,7,8);   //创建合并单元格
    sheet.addMergedRegion(cellRangeAddress_19_19_7_8);
    CellRangeAddress cellRangeAddress_20_20_7_8 = new CellRangeAddress(20,20,7,8);   //创建合并单元格
    sheet.addMergedRegion(cellRangeAddress_20_20_7_8);

    CellRangeAddress cellRangeAddress_24_24_0_9 = new CellRangeAddress(24,24,0,9);   //创建合并单元格
    sheet.addMergedRegion(cellRangeAddress_24_24_0_9);

    Row row_9 = sheet.createRow(9);
    Cell cell_9_0 = row_9.createCell(0);
    cell_9_0.setCellValue("1、胎冠位置");
    row_9.setRowStyle(hssfCellStyle);
    Row row_10 = sheet.createRow(10);
    Cell cell_10_0 = row_10.createCell(0);
    cell_10_0.setCellValue("1#工位");
    Cell cell_10_5 = row_10.createCell(5);
    cell_10_5.setCellValue("2#工位");
    row_10.setRowStyle(hssfCellStyle);

    Row row_13 = sheet.createRow(13);
    Cell cell_13_2 = row_13.createCell(2);
    cell_13_2.setCellValue("最高温度");
    Cell cell_13_7 = row_13.createCell(7);
    cell_13_7.setCellValue("最高温度");

    Row row_14 = sheet.createRow(14);
    Cell cell_14_2 = row_14.createCell(2);
    cell_14_2.setCellValue("最高温度发生时间");
    Cell cell_14_7 = row_14.createCell(7);
    cell_14_7.setCellValue("最高温度发生时间");

    Row row_15 = sheet.createRow(15);
    Cell cell_15_2 = row_15.createCell(2);
    cell_15_2.setCellValue("第一次到达95以上时间");
    Cell cell_15_7 = row_15.createCell(7);
    cell_15_7.setCellValue("第一次到达95以上时间");

    Row row_16 = sheet.createRow(16);
    Cell cell_16_2 = row_16.createCell(2);
//    cell_16_2.setCellValue("定点1最高温度");
    cell_16_2.setCellValue("区域最高温度");
    Cell cell_16_7 = row_16.createCell(7);
//    cell_16_7.setCellValue("定点1最高温度");
    cell_16_7.setCellValue("区域最高温度");

    Row row_17 = sheet.createRow(17);
    Cell cell_17_2 = row_17.createCell(2);
    cell_17_2.setCellValue("测温点1最高温度");
    Cell cell_17_7 = row_17.createCell(7);
    cell_17_7.setCellValue("测温点1最高温度");

    Row row_18 = sheet.createRow(18);
    Cell cell_18_2 = row_18.createCell(2);
    cell_18_2.setCellValue("测温点2最高温度");
    Cell cell_18_7 = row_18.createCell(7);
    cell_18_7.setCellValue("测温点2最高温度");

    Row row_19 = sheet.createRow(19);
    Cell cell_19_2 = row_19.createCell(2);
    cell_19_2.setCellValue("测温点3最高温度");
    Cell cell_19_7 = row_19.createCell(7);
    cell_19_7.setCellValue("测温点3最高温度");

    Row row_20 = sheet.createRow(20);
    Cell cell_20_2 = row_20.createCell(2);
    cell_20_2.setCellValue("测温点4最高温度");
    Cell cell_20_7 = row_20.createCell(7);
    cell_20_7.setCellValue("测温点4最高温度");

    CellRangeAddress cellRangeAddress_25_25_0_9 = new CellRangeAddress(25,25,0,9);   //创建合并单元格
    sheet.addMergedRegion(cellRangeAddress_25_25_0_9);
    Row row_25 = sheet.createRow(25);
    Cell cell_25_0 = row_25.createCell(0);
    cell_25_0.setCellValue("曲线变化");

    CellRangeAddress cellRangeAddress_26_66_0_9 = new CellRangeAddress(26,66,0,9);   //创建合并单元格
    sheet.addMergedRegion(cellRangeAddress_26_66_0_9);


    CellRangeAddress cellRangeAddress_67_67_0_9 = new CellRangeAddress(67,67,0,9);   //创建合并单元格
    sheet.addMergedRegion(cellRangeAddress_67_67_0_9);
    CellRangeAddress cellRangeAddress_68_68_0_1 = new CellRangeAddress(68,68,0,1);   //创建合并单元格
    sheet.addMergedRegion(cellRangeAddress_68_68_0_1);
    CellRangeAddress cellRangeAddress_68_68_2_4 = new CellRangeAddress(68,68,2,4);   //创建合并单元格
    sheet.addMergedRegion(cellRangeAddress_68_68_2_4);
    CellRangeAddress cellRangeAddress_68_68_5_6 = new CellRangeAddress(68,68,5,6);   //创建合并单元格
    sheet.addMergedRegion(cellRangeAddress_68_68_5_6);
    CellRangeAddress cellRangeAddress_68_68_7_9 = new CellRangeAddress(68,68,7,9);   //创建合并单元格
    sheet.addMergedRegion(cellRangeAddress_68_68_7_9);
    Row row_67 = sheet.createRow(67);
    Cell cell_67_67_0_0 = row_67.createCell(0);
    cell_67_67_0_0.setCellValue("2、胎内位置");


    Row row_68 = sheet.createRow(68);
    Cell cell_68_68_0_0 = row_68.createCell(0);
    cell_68_68_0_0.setCellValue("1#工位");
    Cell cell_68_68_5_5 = row_68.createCell(5);
    cell_68_68_5_5.setCellValue("2#工位");
    CellRangeAddress cellRangeAddress_68_70_2_4 = new CellRangeAddress(68,70,2,4);   //创建合并单元格
    sheet.addMergedRegion(cellRangeAddress_68_70_2_4);
    CellRangeAddress cellRangeAddress_68_70_7_9 = new CellRangeAddress(68,70,7,9);   //创建合并单元格
    sheet.addMergedRegion(cellRangeAddress_68_70_7_9);
    CellRangeAddress cellRangeAddress_79_82_2_4 = new CellRangeAddress(79,82,2,4);   //创建合并单元格
    sheet.addMergedRegion(cellRangeAddress_79_82_2_4);
    CellRangeAddress cellRangeAddress_79_82_7_9 = new CellRangeAddress(79,82,7,9);   //创建合并单元格
    sheet.addMergedRegion(cellRangeAddress_79_82_7_9);

    CellRangeAddress cellRangeAddress_71_71_2_3 = new CellRangeAddress(71,71,2,3);   //创建合并单元格
    sheet.addMergedRegion(cellRangeAddress_71_71_2_3);
    CellRangeAddress cellRangeAddress_72_72_2_3 = new CellRangeAddress(72,72,2,3);   //创建合并单元格
    sheet.addMergedRegion(cellRangeAddress_72_72_2_3);
    CellRangeAddress cellRangeAddress_73_73_2_3 = new CellRangeAddress(73,73,2,3);   //创建合并单元格
    sheet.addMergedRegion(cellRangeAddress_73_73_2_3);
    CellRangeAddress cellRangeAddress_74_74_2_3 = new CellRangeAddress(74,74,2,3);   //创建合并单元格
    sheet.addMergedRegion(cellRangeAddress_74_74_2_3);
    CellRangeAddress cellRangeAddress_75_75_2_3 = new CellRangeAddress(75,75,2,3);   //创建合并单元格
    sheet.addMergedRegion(cellRangeAddress_75_75_2_3);
    CellRangeAddress cellRangeAddress_76_76_2_3 = new CellRangeAddress(76,76,2,3);   //创建合并单元格
    sheet.addMergedRegion(cellRangeAddress_76_76_2_3);
    CellRangeAddress cellRangeAddress_77_77_2_3 = new CellRangeAddress(77,77,2,3);   //创建合并单元格
    sheet.addMergedRegion(cellRangeAddress_77_77_2_3);
    CellRangeAddress cellRangeAddress_78_78_2_3 = new CellRangeAddress(78,78,2,3);   //创建合并单元格
    sheet.addMergedRegion(cellRangeAddress_78_78_2_3);

    CellRangeAddress cellRangeAddress_71_71_7_8 = new CellRangeAddress(71,71,7,8);   //创建合并单元格
    sheet.addMergedRegion(cellRangeAddress_71_71_7_8);
    CellRangeAddress cellRangeAddress_72_72_7_8 = new CellRangeAddress(72,72,7,8);   //创建合并单元格
    sheet.addMergedRegion(cellRangeAddress_72_72_7_8);
    CellRangeAddress cellRangeAddress_73_73_7_8 = new CellRangeAddress(73,73,7,8);   //创建合并单元格
    sheet.addMergedRegion(cellRangeAddress_73_73_7_8);
    CellRangeAddress cellRangeAddress_74_74_7_8 = new CellRangeAddress(74,74,7,8);   //创建合并单元格
    sheet.addMergedRegion(cellRangeAddress_74_74_7_8);
    CellRangeAddress cellRangeAddress_75_75_7_8 = new CellRangeAddress(75,75,7,8);   //创建合并单元格
    sheet.addMergedRegion(cellRangeAddress_75_75_7_8);
    CellRangeAddress cellRangeAddress_76_76_7_8 = new CellRangeAddress(76,76,7,8);   //创建合并单元格
    sheet.addMergedRegion(cellRangeAddress_76_76_7_8);
    CellRangeAddress cellRangeAddress_77_77_7_8 = new CellRangeAddress(77,77,7,8);   //创建合并单元格
    sheet.addMergedRegion(cellRangeAddress_77_77_7_8);
    CellRangeAddress cellRangeAddress_78_78_7_8 = new CellRangeAddress(78,78,7,8);   //创建合并单元格
    sheet.addMergedRegion(cellRangeAddress_78_78_7_8);

    CellRangeAddress cellRangeAddress_82_82_0_9 = new CellRangeAddress(82,82,0,9);   //创建合并单元格
    sheet.addMergedRegion(cellRangeAddress_82_82_0_9);

    Row row_71 = sheet.createRow(71);
    Cell cell_71_2 = row_71.createCell(2);
    cell_71_2.setCellValue("最高温度");
    Cell cell_71_7 = row_71.createCell(7);
    cell_71_7.setCellValue("最高温度");

    Row row_72 = sheet.createRow(72);
    Cell cell_72_2 = row_72.createCell(2);
    cell_72_2.setCellValue("最高温度发生时间");
    Cell cell_72_7 = row_72.createCell(7);
    cell_72_7.setCellValue("最高温度发生时间");

    Row row_73 = sheet.createRow(73);
    Cell cell_73_2 = row_73.createCell(2);
    cell_73_2.setCellValue("第一次到达95以上时间");
    Cell cell_73_7 = row_73.createCell(7);
    cell_73_7.setCellValue("第一次到达95以上时间");

    Row row_74 = sheet.createRow(74);
    Cell cell_74_2 = row_74.createCell(2);
//    cell_74_2.setCellValue("定点1最高温度");
    cell_74_2.setCellValue("区域最高温度");
    Cell cell_74_7 = row_74.createCell(7);
//    cell_74_7.setCellValue("定点1最高温度");
    cell_74_7.setCellValue("区域最高温度");
    Row row_75 = sheet.createRow(75);
    Cell cell_75_2 = row_75.createCell(2);
    cell_75_2.setCellValue("测温点1最高温度");
    Cell cell_75_7 = row_75.createCell(7);
    cell_75_7.setCellValue("测温点1最高温度");

    Row row_76 = sheet.createRow(76);
    Cell cell_76_2 = row_76.createCell(2);
    cell_76_2.setCellValue("测温点2最高温度");
    Cell cell_76_7 = row_76.createCell(7);
    cell_76_7.setCellValue("测温点2最高温度");

    Row row_77 = sheet.createRow(77);
    Cell cell_77_2 = row_77.createCell(2);
    cell_77_2.setCellValue("测温点3最高温度");
    Cell cell_77_7 = row_77.createCell(7);
    cell_77_7.setCellValue("测温点3最高温度");

    Row row_78 = sheet.createRow(78);
    Cell cell_78_2 = row_78.createCell(2);
    cell_78_2.setCellValue("测温点4最高温度");
    Cell cell_78_7 = row_78.createCell(7);
    cell_78_7.setCellValue("测温点4最高温度");

    CellRangeAddress cellRangeAddress_83_83_0_9 = new CellRangeAddress(83,83,0,9);   //创建合并单元格
    sheet.addMergedRegion(cellRangeAddress_83_83_0_9);
    Row row_83 = sheet.createRow(83);
    Cell cell_83_0 = row_83.createCell(0);
    cell_83_0.setCellValue("3、胎外位置");
    Row row_84 = sheet.createRow(84);
    Cell cell_84_84_0_0 = row_84.createCell(0);
    cell_84_84_0_0.setCellValue("1#工位");
    Cell cell_84_84_5_5 = row_84.createCell(5);
    cell_84_84_5_5.setCellValue("2#工位");

    Row row_87 = sheet.createRow(87);
    Cell cell_87_2 = row_87.createCell(2);
    cell_87_2.setCellValue("最高温度");
    Cell cell_87_7 = row_87.createCell(7);
    cell_87_7.setCellValue("最高温度");

    Row row_88 = sheet.createRow(88);
    Cell cell_88_2 = row_88.createCell(2);
    cell_88_2.setCellValue("最高温度发生时间");
    Cell cell_88_7 = row_88.createCell(7);
    cell_88_7.setCellValue("最高温度发生时间");

    Row row_89 = sheet.createRow(89);
    Cell cell_89_2 = row_89.createCell(2);
    cell_89_2.setCellValue("第一次到达95以上时间");
    Cell cell_89_7 = row_89.createCell(7);
    cell_89_7.setCellValue("第一次到达95以上时间");

    Row row_90 = sheet.createRow(90);
    Cell cell_90_2 = row_90.createCell(2);
//    cell_90_2.setCellValue("定点1最高温度");
    cell_90_2.setCellValue("区域最高温度");
    Cell cell_90_7 = row_90.createCell(7);
//    cell_90_7.setCellValue("定点1最高温度");
    cell_90_7.setCellValue("区域最高温度");

    Row row_91 = sheet.createRow(91);
    Cell cell_91_2 = row_91.createCell(2);
    cell_91_2.setCellValue("测温点1最高温度");
    Cell cell_91_7 = row_91.createCell(7);
    cell_91_7.setCellValue("测温点1最高温度");

    Row row_92 = sheet.createRow(92);
    Cell cell_92_2 = row_92.createCell(2);
    cell_92_2.setCellValue("测温点2最高温度");
    Cell cell_92_7 = row_92.createCell(7);
    cell_92_7.setCellValue("测温点2最高温度");

    Row row_93 = sheet.createRow(93);
    Cell cell_93_2 = row_93.createCell(2);
    cell_93_2.setCellValue("测温点3最高温度");
    Cell cell_93_7 = row_93.createCell(7);
    cell_93_7.setCellValue("测温点3最高温度");

    Row row_94 = sheet.createRow(94);
    Cell cell_94_2 = row_94.createCell(2);
    cell_94_2.setCellValue("测温点4最高温度");
    Cell cell_94_7 = row_94.createCell(7);
    cell_94_7.setCellValue("测温点4最高温度");

    CellRangeAddress cellRangeAddress_84_84_0_1 = new CellRangeAddress(84,84,0,1);   //创建合并单元格
    sheet.addMergedRegion(cellRangeAddress_84_84_0_1);
    CellRangeAddress cellRangeAddress_84_86_2_4 = new CellRangeAddress(84,86,2,4);   //创建合并单元格
    sheet.addMergedRegion(cellRangeAddress_84_86_2_4);
    CellRangeAddress cellRangeAddress_84_84_5_6 = new CellRangeAddress(84,84,5,6);   //创建合并单元格
    sheet.addMergedRegion(cellRangeAddress_84_84_5_6);
    CellRangeAddress cellRangeAddress_84_86_7_9 = new CellRangeAddress(84,86,7,9);   //创建合并单元格
    sheet.addMergedRegion(cellRangeAddress_84_86_7_9);


    CellRangeAddress cellRangeAddress_87_87_2_3 = new CellRangeAddress(87,87,2,3);   //创建合并单元格
    sheet.addMergedRegion(cellRangeAddress_87_87_2_3);
    CellRangeAddress cellRangeAddress_88_88_2_3 = new CellRangeAddress(88,88,2,3);   //创建合并单元格
    sheet.addMergedRegion(cellRangeAddress_88_88_2_3);
    CellRangeAddress cellRangeAddress_89_89_2_3 = new CellRangeAddress(89,89,2,3);   //创建合并单元格
    sheet.addMergedRegion(cellRangeAddress_89_89_2_3);
    CellRangeAddress cellRangeAddress_90_90_2_3 = new CellRangeAddress(90,90,2,3);   //创建合并单元格
    sheet.addMergedRegion(cellRangeAddress_90_90_2_3);
    CellRangeAddress cellRangeAddress_91_91_2_3 = new CellRangeAddress(91,91,2,3);   //创建合并单元格
    sheet.addMergedRegion(cellRangeAddress_91_91_2_3);
    CellRangeAddress cellRangeAddress_92_92_2_3 = new CellRangeAddress(92,92,2,3);   //创建合并单元格
    sheet.addMergedRegion(cellRangeAddress_92_92_2_3);
    CellRangeAddress cellRangeAddress_93_93_2_3 = new CellRangeAddress(93,93,2,3);   //创建合并单元格
    sheet.addMergedRegion(cellRangeAddress_93_93_2_3);
    CellRangeAddress cellRangeAddress_94_94_2_3 = new CellRangeAddress(94,94,2,3);   //创建合并单元格
    sheet.addMergedRegion(cellRangeAddress_94_94_2_3);

    CellRangeAddress cellRangeAddress_87_87_7_8 = new CellRangeAddress(87,87,7,8);   //创建合并单元格
    sheet.addMergedRegion(cellRangeAddress_87_87_7_8);
    CellRangeAddress cellRangeAddress_88_88_7_8 = new CellRangeAddress(88,88,7,8);   //创建合并单元格
    sheet.addMergedRegion(cellRangeAddress_88_88_7_8);
    CellRangeAddress cellRangeAddress_89_89_7_8 = new CellRangeAddress(89,89,7,8);   //创建合并单元格
    sheet.addMergedRegion(cellRangeAddress_89_89_7_8);
    CellRangeAddress cellRangeAddress_90_90_7_8 = new CellRangeAddress(90,90,7,8);   //创建合并单元格
    sheet.addMergedRegion(cellRangeAddress_90_90_7_8);
    CellRangeAddress cellRangeAddress_91_91_7_8 = new CellRangeAddress(91,91,7,8);   //创建合并单元格
    sheet.addMergedRegion(cellRangeAddress_91_91_7_8);
    CellRangeAddress cellRangeAddress_92_92_7_8 = new CellRangeAddress(92,92,7,8);   //创建合并单元格
    sheet.addMergedRegion(cellRangeAddress_92_92_7_8);
    CellRangeAddress cellRangeAddress_93_93_7_8 = new CellRangeAddress(93,93,7,8);   //创建合并单元格
    sheet.addMergedRegion(cellRangeAddress_93_93_7_8);
    CellRangeAddress cellRangeAddress_94_94_7_8 = new CellRangeAddress(94,94,7,8);   //创建合并单元格
    sheet.addMergedRegion(cellRangeAddress_94_94_7_8);

    CellRangeAddress cellRangeAddress_97_100_0_9 = new CellRangeAddress(97,100,0,9);   //创建合并单元格
    sheet.addMergedRegion(cellRangeAddress_97_100_0_9);

    CellRangeAddress cellRangeAddress_95_97_0_9 = new CellRangeAddress(95,97,2,4);   //创建合并单元格
    sheet.addMergedRegion(cellRangeAddress_95_97_0_9);
    CellRangeAddress cellRangeAddress_95_97_7_9 = new CellRangeAddress(95,97,7,9);   //创建合并单元格
    sheet.addMergedRegion(cellRangeAddress_95_97_7_9);


    Row row_104 = sheet.createRow(104);
    Cell cell_104_8 = row_104.createCell(8);
    cell_104_8.setCellValue(sampleInfo1.getSample_test_date());
    Row row_105 = sheet.createRow(105);
    Cell cell_105_8 = row_105.createCell(8);
    cell_105_8.setCellValue("质管处");



    //测试信息
    Cell cell_2_2 = row_2.createCell(2);
    cell_2_2.setCellValue(sampleInfo1.getSample_test_date()+" "+sampleInfo1.getSample_number());
    Cell cell_2_4 = row_2.createCell(4);
    cell_2_4.setCellValue(sampleInfo1.getRoom_number());
    Cell cell_2_7 = row_2.createCell(7);
    cell_2_7.setCellValue(sampleInfo1.getSample_test_date());
    //1#工位测试信息
    Cell cell_4_1 = row_4.createCell(1);
    cell_4_1.setCellValue(sampleInfo1.getSample_specification());
    Cell cell_4_2 = row_4.createCell(2);
    cell_4_2.setCellValue(sampleInfo1.getSample_level());
    Cell cell_4_3 = row_4.createCell(3);
    cell_4_3.setCellValue(sampleInfo1.getSample_pattern());
    Cell cell_4_4 = row_4.createCell(4);
    cell_4_4.setCellValue(sampleInfo1.getSample_brand());
    Cell cell_4_5 = row_4.createCell(5);
    cell_4_5.setCellValue(sampleInfo1.getTire_number());
    Cell cell_4_6 = row_4.createCell(6);
    if(reportExcel_one_M != null){
        cell_4_6.setCellValue(reportExcel_one_M.getHighestTemp());
    }
    Cell cell_4_7 = row_4.createCell(7);
    if(reportExcel_one_L != null){
        cell_4_7.setCellValue(reportExcel_one_L.getHighestTemp());
    }
    Cell cell_4_8 = row_4.createCell(8);
    if(reportExcel_one_R != null){
        cell_4_8.setCellValue(reportExcel_one_R.getHighestTemp());
    }
    Cell cell_4_9 = row_4.createCell(9);
    cell_4_9.setCellValue(sampleInfo1.getSample_scheme());
    //2#测试信息
    Cell cell_5_1 = row_5.createCell(1);
    cell_5_1.setCellValue(sampleInfo2.getSample_specification());
    Cell cell_5_2 = row_5.createCell(2);
    cell_5_2.setCellValue(sampleInfo2.getSample_level());
    Cell cell_5_3 = row_5.createCell(3);
    cell_5_3.setCellValue(sampleInfo2.getSample_pattern());
    Cell cell_5_4 = row_5.createCell(4);
    cell_5_4.setCellValue(sampleInfo1.getSample_brand());
    Cell cell_5_5 = row_5.createCell(5);
    cell_5_5.setCellValue(sampleInfo2.getTire_number());
    Cell cell_5_6 = row_5.createCell(6);
    if(reportExcel_two_M != null){
        cell_5_6.setCellValue(reportExcel_two_M.getHighestTemp());
    }
    Cell cell_5_7 = row_5.createCell(7);
    if(reportExcel_two_L != null){
        cell_5_7.setCellValue(reportExcel_two_L.getHighestTemp());
    }
    Cell cell_5_8 = row_5.createCell(8);
    if(reportExcel_two_R != null){
        cell_5_8.setCellValue(reportExcel_two_R.getHighestTemp());
    }

    Cell cell_5_9 = row_5.createCell(9);
    cell_5_9.setCellValue(sampleInfo2.getSample_scheme());

    //1#工位胎冠
    if(reportExcel_one_M != null){
        Cell cell_13_4 = row_13.createCell(4);
        cell_13_4.setCellValue(reportExcel_one_M.getHighestTemp());
        Cell cell_14_4 = row_14.createCell(4);
        cell_14_4.setCellValue(reportExcel_one_M.getHighestTempTime());
        Cell cell_15_4 = row_15.createCell(4);
        cell_15_4.setCellValue(reportExcel_one_M.getReachNFTempTime());
        Cell cell_16_4 = row_16.createCell(4);
        cell_16_4.setCellValue(reportExcel_one_M.getPointOneHighestTemp());
        Cell cell_17_4 = row_17.createCell(4);
        cell_17_4.setCellValue(reportExcel_one_M.getPointTwoHighestTemp());
        Cell cell_18_4 = row_18.createCell(4);
        cell_18_4.setCellValue(reportExcel_one_M.getPointThreeHighestTemp());
        Cell cell_19_4 = row_19.createCell(4);
        cell_19_4.setCellValue(reportExcel_one_M.getPointFourHighestTemp());
        Cell cell_20_4 = row_20.createCell(4);
        cell_20_4.setCellValue(reportExcel_one_M.getPointFiveHighestTemp());
    }


    //2#工位胎冠
    if(reportExcel_two_M != null){
        Cell cell_13_9 = row_13.createCell(9);
        cell_13_9.setCellValue(reportExcel_two_M.getHighestTemp());
        Cell cell_14_9 = row_14.createCell(9);
        cell_14_9.setCellValue(reportExcel_two_M.getHighestTempTime());
        Cell cell_15_9 = row_15.createCell(9);
        cell_15_9.setCellValue(reportExcel_two_M.getReachNFTempTime());
        Cell cell_16_9 = row_16.createCell(9);
        cell_16_9.setCellValue(reportExcel_two_M.getPointOneHighestTemp());
        Cell cell_17_9 = row_17.createCell(9);
        cell_17_9.setCellValue(reportExcel_two_M.getPointTwoHighestTemp());
        Cell cell_18_9 = row_18.createCell(9);
        cell_18_9.setCellValue(reportExcel_two_M.getPointThreeHighestTemp());
        Cell cell_19_9 = row_19.createCell(9);
        cell_19_9.setCellValue(reportExcel_two_M.getPointFourHighestTemp());
        Cell cell_20_9 = row_20.createCell(9);
        cell_20_9.setCellValue(reportExcel_two_M.getPointFiveHighestTemp());
    }


    //1#工位胎侧区域1(L)
    if(reportExcel_one_L != null){
        Cell cell_71_4 = row_71.createCell(4);
        cell_71_4.setCellValue(reportExcel_one_L.getHighestTemp());
        Cell cell_72_4 = row_72.createCell(4);
        cell_72_4.setCellValue(reportExcel_one_L.getHighestTempTime());
        Cell cell_73_4 = row_73.createCell(4);
        cell_73_4.setCellValue(reportExcel_one_L.getReachNFTempTime());
        Cell cell_74_4 = row_74.createCell(4);
        cell_74_4.setCellValue(reportExcel_one_L.getPointOneHighestTemp());
        Cell cell_75_4 = row_75.createCell(4);
        cell_75_4.setCellValue(reportExcel_one_L.getPointTwoHighestTemp());
        Cell cell_76_4 = row_76.createCell(4);
        cell_76_4.setCellValue(reportExcel_one_L.getPointThreeHighestTemp());
        Cell cell_77_4 = row_77.createCell(4);
        cell_77_4.setCellValue(reportExcel_one_L.getPointFourHighestTemp());
        Cell cell_78_4 = row_78.createCell(4);
        cell_78_4.setCellValue(reportExcel_one_L.getPointFiveHighestTemp());
    }


    //2#工位胎侧区域1(L)
    if(reportExcel_two_L != null){
        Cell cell_71_9 = row_71.createCell(9);
        cell_71_9.setCellValue(reportExcel_two_L.getHighestTemp());
        Cell cell_72_9 = row_72.createCell(9);
        cell_72_9.setCellValue(reportExcel_two_L.getHighestTempTime());
        Cell cell_73_9 = row_73.createCell(9);
        cell_73_9.setCellValue(reportExcel_two_L.getReachNFTempTime());
        Cell cell_74_9 = row_74.createCell(9);
        cell_74_9.setCellValue(reportExcel_two_L.getPointOneHighestTemp());
        Cell cell_75_9 = row_75.createCell(9);
        cell_75_9.setCellValue(reportExcel_two_L.getPointTwoHighestTemp());
        Cell cell_76_9 = row_76.createCell(9);
        cell_76_9.setCellValue(reportExcel_two_L.getPointThreeHighestTemp());
        Cell cell_77_9 = row_77.createCell(9);
        cell_77_9.setCellValue(reportExcel_two_L.getPointFourHighestTemp());
        Cell cell_78_9 = row_78.createCell(9);
        cell_78_9.setCellValue(reportExcel_two_L.getPointFiveHighestTemp());
    }


    //1#工位胎侧区域2(R)
    if(reportExcel_one_R != null){
        Cell cell_87_4 = row_87.createCell(4);
        cell_87_4.setCellValue(reportExcel_one_R.getHighestTemp());
        Cell cell_88_4 = row_88.createCell(4);
        cell_88_4.setCellValue(reportExcel_one_R.getHighestTempTime());
        Cell cell_89_4 = row_89.createCell(4);
        cell_89_4.setCellValue(reportExcel_one_R.getReachNFTempTime());
        Cell cell_90_4 = row_90.createCell(4);
        cell_90_4.setCellValue(reportExcel_one_R.getPointOneHighestTemp());
        Cell cell_91_4 = row_91.createCell(4);
        cell_91_4.setCellValue(reportExcel_one_R.getPointTwoHighestTemp());
        Cell cell_92_4 = row_92.createCell(4);
        cell_92_4.setCellValue(reportExcel_one_R.getPointThreeHighestTemp());
        Cell cell_93_4 = row_93.createCell(4);
        cell_93_4.setCellValue(reportExcel_one_R.getPointFourHighestTemp());
        Cell cell_94_4 = row_94.createCell(4);
        cell_94_4.setCellValue(reportExcel_one_R.getPointFiveHighestTemp());
    }



    //2#工位胎侧区域2(R)
    if(reportExcel_two_R != null){
        Cell cell_87_9 = row_87.createCell(9);
        cell_87_9.setCellValue(reportExcel_two_R.getHighestTemp());
        Cell cell_88_9 = row_88.createCell(9);
        cell_88_9.setCellValue(reportExcel_two_R.getHighestTempTime());
        Cell cell_89_9 = row_89.createCell(9);
        cell_89_9.setCellValue(reportExcel_two_R.getReachNFTempTime());
        Cell cell_90_9 = row_90.createCell(9);
        cell_90_9.setCellValue(reportExcel_two_R.getPointOneHighestTemp());
        Cell cell_91_9 = row_91.createCell(9);
        cell_91_9.setCellValue(reportExcel_two_R.getPointTwoHighestTemp());
        Cell cell_92_9 = row_92.createCell(9);
        cell_92_9.setCellValue(reportExcel_two_R.getPointThreeHighestTemp());
        Cell cell_93_9 = row_93.createCell(9);
        cell_93_9.setCellValue(reportExcel_two_R.getPointFourHighestTemp());
        Cell cell_94_9 = row_94.createCell(9);
        cell_94_9.setCellValue(reportExcel_two_R.getPointFiveHighestTemp());
    }


    setRowStyle(hssfCellStyle,sheet,2,6,0,10);
    setRowStyle(hssfCellStyle,sheet,13,21,0,10);
    setRowStyle(hssfCellStyle,sheet,71,79,0,10);
    setRowStyle(hssfCellStyle,sheet,87,95,0,10);

    BufferedImage bufferImg1M = null;
    BufferedImage bufferImg1R = null;
    BufferedImage bufferImg1L = null;
    BufferedImage bufferImg2M = null;
    BufferedImage bufferImg2R = null;
    BufferedImage bufferImg2L = null;
    BufferedImage bufferImg1MQX = null;
    BufferedImage bufferImg1RQX = null;
    BufferedImage bufferImg1LQX = null;
    BufferedImage bufferImg2MQX = null;
    BufferedImage bufferImg2RQX = null;
    BufferedImage bufferImg2LQX = null;

    try {
        //1#胎冠热成像图
        if(reportExcel_one_M != null){
            ByteArrayOutputStream byteArrayOut1M = new ByteArrayOutputStream(); // 先把读进来的图片放到一个ByteArrayOutputStream中，以便产生ByteArray
            bufferImg1M = ImageIO.read(new File(reportExcel_one_M.getPlp_photo_position()));     //将图片读到BufferedImage
            ImageIO.write(bufferImg1M, "png", byteArrayOut1M);        // 将图片写入流中
            HSSFPatriarch hssfPatriarch1M = sheet.createDrawingPatriarch();    //将图片写入excel中
            /**
             * 该构造函数有8个参数
             * 前四个参数是控制图片在单元格的位置，分别是图片距离单元格left，top，right，bottom的像素距离
             * 后四个参数，前连个表示图片左上角所在的cellNum和 rowNum，后天个参数对应的表示图片右下角所在的cellNum和 rowNum，
             **/
            HSSFClientAnchor anchor1M = new HSSFClientAnchor(0,0,0,0,(short)0,11,(short)2,24);
            hssfPatriarch1M.createPicture(anchor1M,wb.addPicture(byteArrayOut1M.toByteArray(),HSSFWorkbook.PICTURE_TYPE_JPEG));    //导入图片
        }


        //2#胎冠热成像图
        if(reportExcel_two_M != null){
            ByteArrayOutputStream byteArrayOut2M = new ByteArrayOutputStream();
            bufferImg2M = ImageIO.read(new File(reportExcel_two_M.getPlp_photo_position()));     //将图片读到BufferedImage
            ImageIO.write(bufferImg2M, "png", byteArrayOut2M);        // 将图片写入流中
            HSSFPatriarch hssfPatriarch2M = sheet.createDrawingPatriarch();
            HSSFClientAnchor anchor2M = new HSSFClientAnchor(0,0,0,0,(short)5,11,(short)7,24);
            hssfPatriarch2M.createPicture(anchor2M,wb.addPicture(byteArrayOut2M.toByteArray(),HSSFWorkbook.PICTURE_TYPE_JPEG));    //导入图片
        }


        //1#胎冠曲线图
        String picturePath1MQX = "D://"+sampleInfo1.getRoom_number()+"//"+sampleInfo1.getCam_station_number()+"//"+sampleInfo1.getSample_test_date()+"//";
        String pictureFile1MQX = sampleInfo1.getRoom_number()+"-"+sampleInfo1.getCam_station_number()+"-"+sampleInfo1.getSample_test_date()+"-"+sampleInfo1.getTire_number()+".jpeg";
        String wholePicturePath1MQX = picturePath1MQX + pictureFile1MQX;
        ByteArrayOutputStream byteArrayOut1MQX = new ByteArrayOutputStream();
        bufferImg1MQX = ImageIO.read(new File(wholePicturePath1MQX));     //将图片读到BufferedImage
        ImageIO.write(bufferImg1MQX, "png", byteArrayOut1MQX);        // 将图片写入流中
        HSSFPatriarch hssfPatriarch1MQX = sheet.createDrawingPatriarch();
        HSSFClientAnchor anchor1MQX = new HSSFClientAnchor(0,0,0,0,(short)1,27,(short)9,46);
        hssfPatriarch1MQX.createPicture(anchor1MQX,wb.addPicture(byteArrayOut1MQX.toByteArray(),HSSFWorkbook.PICTURE_TYPE_JPEG));    //导入图片
        CellRangeAddress cellRangeAddress_46_46_0_9 = new CellRangeAddress(46,46,0,9);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_46_46_0_9);

        //2#胎冠曲线图
        String picturePath2MQX = "D://"+sampleInfo2.getRoom_number()+"//"+sampleInfo2.getCam_station_number()+"//"+sampleInfo2.getSample_test_date()+"//";
        String pictureFile2MQX = sampleInfo2.getRoom_number()+"-"+sampleInfo2.getCam_station_number()+"-"+sampleInfo2.getSample_test_date()+"-"+sampleInfo2.getTire_number()+".jpeg";
        String wholePicturePath2MQX = picturePath2MQX + pictureFile2MQX;
        ByteArrayOutputStream byteArrayOut2MQX = new ByteArrayOutputStream();
        bufferImg2MQX = ImageIO.read(new File(wholePicturePath2MQX));     //将图片读到BufferedImage
        ImageIO.write(bufferImg2MQX, "png", byteArrayOut2MQX);        // 将图片写入流中
        HSSFPatriarch hssfPatriarch2MQX = sheet.createDrawingPatriarch();
        HSSFClientAnchor anchor2MQX = new HSSFClientAnchor(0,0,0,0,(short)1,47,(short)9,66);
        hssfPatriarch2MQX.createPicture(anchor2MQX,wb.addPicture(byteArrayOut2MQX.toByteArray(),HSSFWorkbook.PICTURE_TYPE_JPEG));    //导入图片

        //1#胎侧-1热成像图
        if(reportExcel_one_L != null){
            ByteArrayOutputStream byteArrayOut1L = new ByteArrayOutputStream();
            bufferImg1L = ImageIO.read(new File(reportExcel_one_L.getPlp_photo_position()));     //将图片读到BufferedImage
            ImageIO.write(bufferImg1L, "png", byteArrayOut1L);        // 将图片写入流中
            HSSFPatriarch hssfPatriarch1L = sheet.createDrawingPatriarch();
            HSSFClientAnchor anchor1L = new HSSFClientAnchor(0,0,0,0,(short)0,69,(short)2,82);
            hssfPatriarch1L.createPicture(anchor1L,wb.addPicture(byteArrayOut1L.toByteArray(),HSSFWorkbook.PICTURE_TYPE_JPEG));    //导入图片
        }



        //2#胎侧-1热成像图
        if(reportExcel_two_L != null){
            ByteArrayOutputStream byteArrayOut2L = new ByteArrayOutputStream();
            bufferImg2L = ImageIO.read(new File(reportExcel_two_L.getPlp_photo_position()));     //将图片读到BufferedImage
            ImageIO.write(bufferImg2L, "png", byteArrayOut2L);        // 将图片写入流中
            HSSFPatriarch hssfPatriarch2L = sheet.createDrawingPatriarch();
            HSSFClientAnchor anchor2L = new HSSFClientAnchor(0,0,0,0,(short)5,69,(short)7,82);
            hssfPatriarch2L.createPicture(anchor2L,wb.addPicture(byteArrayOut2L.toByteArray(),HSSFWorkbook.PICTURE_TYPE_JPEG));    //导入图片

        }

        //1#胎侧-2热成像图
        if(reportExcel_one_R != null){
            ByteArrayOutputStream byteArrayOut1R = new ByteArrayOutputStream();
            bufferImg1R = ImageIO.read(new File(reportExcel_one_R.getPlp_photo_position()));     //将图片读到BufferedImage
            ImageIO.write(bufferImg1R, "png", byteArrayOut1R);        // 将图片写入流中
            HSSFPatriarch hssfPatriarch1R = sheet.createDrawingPatriarch();
            HSSFClientAnchor anchor1R = new HSSFClientAnchor(0,0,0,0,(short)0,85,(short)2,98);
            hssfPatriarch1R.createPicture(anchor1R,wb.addPicture(byteArrayOut1R.toByteArray(),HSSFWorkbook.PICTURE_TYPE_JPEG));    //导入图片
        }



        //2#胎侧-2热成像图
        if(reportExcel_two_R != null){
            ByteArrayOutputStream byteArrayOut2R = new ByteArrayOutputStream();
            bufferImg2R = ImageIO.read(new File(reportExcel_two_R.getPlp_photo_position()));     //将图片读到BufferedImage
            ImageIO.write(bufferImg2R, "png", byteArrayOut2R);        // 将图片写入流中
            HSSFPatriarch hssfPatriarch2R = sheet.createDrawingPatriarch();
            HSSFClientAnchor anchor2R = new HSSFClientAnchor(0,0,0,0,(short)5,85,(short)7,98);
            hssfPatriarch2R.createPicture(anchor2R,wb.addPicture(byteArrayOut2R.toByteArray(),HSSFWorkbook.PICTURE_TYPE_JPEG));    //导入图片
        }


        String wholeFilePath1 = filePath+fileName1;
        String wholeFilePath2 = filePath2+fileName2;
        File file1 = new File(wholeFilePath1);
        File file2 = new File(wholeFilePath2);
        if(!file1.getParentFile().exists()){     //若该路径下不存在此文件目录则创建
            boolean result1 = file1.getParentFile().mkdirs();
            if (!result1) {
                System.out.println("创建失败");
            }
        }
        if(!file2.getParentFile().exists()){
            boolean result2 = file1.getParentFile().mkdirs();
            if (!result2) {
                System.out.println("创建失败");
            }

        }
        FileOutputStream fos1 = new FileOutputStream(file1);
        FileOutputStream fos2 = new FileOutputStream(file2);
        wb.write(fos1);
        wb.write(fos2);
        fos1.close();
        fos2.close();
    } catch (IOException e) {
        e.printStackTrace();
        System.out.println("io erorr : " + e.getMessage());
    }
}

//生成1号工位报表信息
    public void getExcelReportOne(SampleInfo sampleInfo1){   //List<SampleInfo> list, SampleInfo sampleInfo,String cam_ip
        StringBuffer fatherFileName1 = new StringBuffer();
        Map<String, Object> room1 = new HashMap<String, Object>();
        room1.put("room_number",sampleInfo1.getRoom_number());
        room1.put("cam_station_number",sampleInfo1.getCam_station_number());
        List<Room> cam_station_one = reportExcelService.getCam_ip_position(room1);
        ReportExcelInfo reportExcel_one_L = null, reportExcel_one_M = null, reportExcel_one_R = null;

        for(int i=0;i<cam_station_one.size();i++){
            sampleInfo1.setCam_ip(cam_station_one.get(i).getCam_ip());
            fatherFileName1.append(cam_station_one.get(i).getCam_ip()+" ");
            if(cam_station_one.get(i).getCam_position().equals("L")){
                reportExcel_one_L = returnReportExcel(sampleInfo1);
            }
            if(cam_station_one.get(i).getCam_position().equals("M")){
                reportExcel_one_M = returnReportExcel(sampleInfo1);
            }
            if(cam_station_one.get(i).getCam_position().equals("R")){
                reportExcel_one_R = returnReportExcel(sampleInfo1);
            }
        }
        fatherFileName1.deleteCharAt(fatherFileName1.length()-1);
        String fileName = sampleInfo1.getSample_specification()+"_"+sampleInfo1.getSample_level()+"_"+sampleInfo1.getSample_pattern()+"_"+sampleInfo1.getSample_test_date()+"_"+sampleInfo1.getSample_number()+"_"+sampleInfo1.getSample_person()+"_"+sampleInfo1.getTire_number()+".xls";
        //String fileName = sampleInfo1.getSample_specification()+"_"+sampleInfo1.getSample_level()+"_"+sampleInfo1.getSample_pattern()+"_"+sampleInfo1.getSample_test_date()+"_"+sampleInfo1.getSample_number()+"_"+sampleInfo1.getSample_person()+"_"+sampleInfo1.getTire_number()+"_"+sampleInfo1.getRoom_number()+"_"+sampleInfo1.getCam_station_number()+"_"+fatherFileName1+"_.xls";
        String filePath = "D://"+sampleInfo1.getRoom_number()+"//"+sampleInfo1.getCam_station_number()+"//"+sampleInfo1.getSample_test_date()+"//";

        HSSFWorkbook wb = new HSSFWorkbook();       //创建工作簿
        HSSFSheet sheet = wb.createSheet(fileName);      //创建工作表
        sheet.setDefaultColumnWidth(16);    //设置默认列宽

        CellRangeAddress cellRangeAddress_0_0_0_9 = new CellRangeAddress(0,0,0,9);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_0_0_0_9);

        HSSFCellStyle xssfCellStyle = wb.createCellStyle();
        Font font = wb.createFont();
        Row row_0 = sheet.createRow(0);       //创建行
        Cell cell_0_0 = row_0.createCell(0);    //创建列
        cell_0_0.setCellValue("红外检测报告");
        font.setFontHeightInPoints((short)20);
        xssfCellStyle.setFont(font);
        xssfCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);   //居中对齐
        cell_0_0.setCellStyle(xssfCellStyle);

        CellRangeAddress cellRangeAddress_1_1_0_9 = new CellRangeAddress(1,1,0,9);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_1_1_0_9);

        Row row_1 = sheet.createRow(1);
        Cell cell_1_0 = row_1.createCell(0);
        cell_1_0.setCellValue("一、测试信息");
        CellRangeAddress cellRangeAddress_2_2_0_1 = new CellRangeAddress(2,2,0,1);
        sheet.addMergedRegion(cellRangeAddress_2_2_0_1);

        HSSFCellStyle hssfCellStyle = wb.createCellStyle();
        hssfCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);   //水平居中对齐


        Row row_2 = sheet.createRow(2);
        Cell cell_2_0 = row_2.createCell(0);
        cell_2_0.setCellValue("报告编号");
        Cell cell_2_3 = row_2.createCell(3);
        cell_2_3.setCellValue("测试机台");
        CellRangeAddress cellRangeAddress_2_2_4_5 = new CellRangeAddress(2,2,4,5);
        sheet.addMergedRegion(cellRangeAddress_2_2_4_5);

        Cell cell_2_6 = row_2.createCell(6);
        cell_2_6.setCellValue("测试日期");
        CellRangeAddress cellRangeAddress_2_2_7_9 = new CellRangeAddress(2,2,7,9);
        sheet.addMergedRegion(cellRangeAddress_2_2_7_9);
        row_2.setRowStyle(hssfCellStyle);
        Row row_3 = sheet.createRow(3);
        Cell cell_3_0 = row_3.createCell(0);
        cell_3_0.setCellValue("工位");
        Cell cell_3_1 = row_3.createCell(1);
        cell_3_1.setCellValue("规格");
        Cell cell_3_2 = row_3.createCell(2);
        cell_3_2.setCellValue("层级");
        Cell cell_3_3 = row_3.createCell(3);
        cell_3_3.setCellValue("花纹");
        Cell cell_3_4 = row_3.createCell(4);
        cell_3_4.setCellValue("品牌");
        Cell cell_3_5 = row_3.createCell(5);
        cell_3_5.setCellValue("胎号");
        Cell cell_3_6 = row_3.createCell(6);
        cell_3_6.setCellValue("胎冠区域最高温度");
        Cell cell_3_7 = row_3.createCell(7);
        cell_3_7.setCellValue("胎内最高温度");
        Cell cell_3_8 = row_3.createCell(8);
        cell_3_8.setCellValue("胎外最高温度");
        Cell cell_3_9 = row_3.createCell(9);
        cell_3_9.setCellValue("方案");
        row_3.setRowStyle(hssfCellStyle);
        Row row_4 = sheet.createRow(4);
        Cell cell_4_0 = row_4.createCell(0);
        cell_4_0.setCellValue("1#");
        row_4.setRowStyle(hssfCellStyle);
        Row row_5 = sheet.createRow(5);
        Cell cell_5_0 = row_5.createCell(0);
        cell_5_0.setCellValue("2#");
        row_5.setRowStyle(hssfCellStyle);

        CellRangeAddress cellRangeAddress_8_8_0_9 = new CellRangeAddress(8,8,0,9);
        sheet.addMergedRegion(cellRangeAddress_8_8_0_9);
        Row row_8 = sheet.createRow(8);
        Cell cell_8_0 = row_8.createCell(0);
        cell_8_0.setCellValue("二、测试过程数据");

        CellRangeAddress cellRangeAddress_6_7_0_9 = new CellRangeAddress(6,7,0,9);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_6_7_0_9);

        CellRangeAddress cellRangeAddress_9_9_0_9 = new CellRangeAddress(9,9,0,9);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_9_9_0_9);
        CellRangeAddress cellRangeAddress_10_10_0_1 = new CellRangeAddress(10,10,0,1);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_10_10_0_1);
        CellRangeAddress cellRangeAddress_10_10_2_4 = new CellRangeAddress(10,10,2,4);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_10_10_2_4);
        CellRangeAddress cellRangeAddress_10_10_5_6 = new CellRangeAddress(10,10,5,6);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_10_10_5_6);
        CellRangeAddress cellRangeAddress_10_10_7_9 = new CellRangeAddress(10,10,7,9);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_10_10_7_9);

        CellRangeAddress cellRangeAddress_10_12_2_4 = new CellRangeAddress(10,12,2,4);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_10_12_2_4);
        CellRangeAddress cellRangeAddress_10_12_7_9 = new CellRangeAddress(10,12,7,9);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_10_12_7_9);

        CellRangeAddress cellRangeAddress__21_24_2_4= new CellRangeAddress(21,24,2,4);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress__21_24_2_4);
        CellRangeAddress cellRangeAddress__21_24_7_9= new CellRangeAddress(21,24,7,9);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress__21_24_7_9);

        CellRangeAddress cellRangeAddress_13_13_2_3 = new CellRangeAddress(13,13,2,3);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_13_13_2_3);
        CellRangeAddress cellRangeAddress_14_14_2_3 = new CellRangeAddress(14,14,2,3);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_14_14_2_3);
        CellRangeAddress cellRangeAddress_15_15_2_3 = new CellRangeAddress(15,15,2,3);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_15_15_2_3);
        CellRangeAddress cellRangeAddress_16_16_2_3 = new CellRangeAddress(16,16,2,3);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_16_16_2_3);
        CellRangeAddress cellRangeAddress_17_17_2_3 = new CellRangeAddress(17,17,2,3);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_17_17_2_3);
        CellRangeAddress cellRangeAddress_18_18_2_3 = new CellRangeAddress(18,18,2,3);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_18_18_2_3);
        CellRangeAddress cellRangeAddress_19_19_2_3 = new CellRangeAddress(19,19,2,3);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_19_19_2_3);
        CellRangeAddress cellRangeAddress_20_20_2_3 = new CellRangeAddress(20,20,2,3);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_20_20_2_3);

        CellRangeAddress cellRangeAddress_13_13_7_8 = new CellRangeAddress(13,13,7,8);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_13_13_7_8);
        CellRangeAddress cellRangeAddress_14_14_7_8 = new CellRangeAddress(14,14,7,8);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_14_14_7_8);
        CellRangeAddress cellRangeAddress_15_15_7_8 = new CellRangeAddress(15,15,7,8);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_15_15_7_8);
        CellRangeAddress cellRangeAddress_16_16_7_8 = new CellRangeAddress(16,16,7,8);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_16_16_7_8);
        CellRangeAddress cellRangeAddress_17_17_7_8 = new CellRangeAddress(17,17,7,8);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_17_17_7_8);
        CellRangeAddress cellRangeAddress_18_18_7_8 = new CellRangeAddress(18,18,7,8);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_18_18_7_8);
        CellRangeAddress cellRangeAddress_19_19_7_8 = new CellRangeAddress(19,19,7,8);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_19_19_7_8);
        CellRangeAddress cellRangeAddress_20_20_7_8 = new CellRangeAddress(20,20,7,8);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_20_20_7_8);

        CellRangeAddress cellRangeAddress_24_24_0_9 = new CellRangeAddress(24,24,0,9);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_24_24_0_9);

        Row row_9 = sheet.createRow(9);
        Cell cell_9_0 = row_9.createCell(0);
        cell_9_0.setCellValue("1、胎冠位置");
        row_9.setRowStyle(hssfCellStyle);
        Row row_10 = sheet.createRow(10);
        Cell cell_10_0 = row_10.createCell(0);
        cell_10_0.setCellValue("1#工位");
        Cell cell_10_5 = row_10.createCell(5);
        cell_10_5.setCellValue("2#工位");
        row_10.setRowStyle(hssfCellStyle);

        Row row_13 = sheet.createRow(13);
        Cell cell_13_2 = row_13.createCell(2);
        cell_13_2.setCellValue("最高温度");
        Cell cell_13_7 = row_13.createCell(7);
        cell_13_7.setCellValue("最高温度");

        Row row_14 = sheet.createRow(14);
        Cell cell_14_2 = row_14.createCell(2);
        cell_14_2.setCellValue("最高温度发生时间");
        Cell cell_14_7 = row_14.createCell(7);
        cell_14_7.setCellValue("最高温度发生时间");

        Row row_15 = sheet.createRow(15);
        Cell cell_15_2 = row_15.createCell(2);
        cell_15_2.setCellValue("第一次到达95以上时间");
        Cell cell_15_7 = row_15.createCell(7);
        cell_15_7.setCellValue("第一次到达95以上时间");

        Row row_16 = sheet.createRow(16);
        Cell cell_16_2 = row_16.createCell(2);
//        cell_16_2.setCellValue("定点1最高温度");
        cell_16_2.setCellValue("区域最高温度");
        Cell cell_16_7 = row_16.createCell(7);
//        cell_16_7.setCellValue("定点1最高温度");
        cell_16_7.setCellValue("区域最高温度");

        Row row_17 = sheet.createRow(17);
        Cell cell_17_2 = row_17.createCell(2);
        cell_17_2.setCellValue("测温点1最高温度");
        Cell cell_17_7 = row_17.createCell(7);
        cell_17_7.setCellValue("测温点1最高温度");

        Row row_18 = sheet.createRow(18);
        Cell cell_18_2 = row_18.createCell(2);
        cell_18_2.setCellValue("测温点2最高温度");
        Cell cell_18_7 = row_18.createCell(7);
        cell_18_7.setCellValue("测温点2最高温度");

        Row row_19 = sheet.createRow(19);
        Cell cell_19_2 = row_19.createCell(2);
        cell_19_2.setCellValue("测温点3最高温度");
        Cell cell_19_7 = row_19.createCell(7);
        cell_19_7.setCellValue("测温点3最高温度");

        Row row_20 = sheet.createRow(20);
        Cell cell_20_2 = row_20.createCell(2);
        cell_20_2.setCellValue("测温点4最高温度");
        Cell cell_20_7 = row_20.createCell(7);
        cell_20_7.setCellValue("测温点4最高温度");

        CellRangeAddress cellRangeAddress_25_25_0_9 = new CellRangeAddress(25,25,0,9);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_25_25_0_9);
        Row row_25 = sheet.createRow(25);
        Cell cell_25_0 = row_25.createCell(0);
        cell_25_0.setCellValue("曲线变化");

        CellRangeAddress cellRangeAddress_26_66_0_9 = new CellRangeAddress(26,66,0,9);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_26_66_0_9);


        CellRangeAddress cellRangeAddress_67_67_0_9 = new CellRangeAddress(67,67,0,9);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_67_67_0_9);
        CellRangeAddress cellRangeAddress_68_68_0_1 = new CellRangeAddress(68,68,0,1);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_68_68_0_1);
        CellRangeAddress cellRangeAddress_68_68_2_4 = new CellRangeAddress(68,68,2,4);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_68_68_2_4);
        CellRangeAddress cellRangeAddress_68_68_5_6 = new CellRangeAddress(68,68,5,6);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_68_68_5_6);
        CellRangeAddress cellRangeAddress_68_68_7_9 = new CellRangeAddress(68,68,7,9);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_68_68_7_9);
        Row row_67 = sheet.createRow(67);
        Cell cell_67_67_0_0 = row_67.createCell(0);
        cell_67_67_0_0.setCellValue("2、胎内位置");


        Row row_68 = sheet.createRow(68);
        Cell cell_68_68_0_0 = row_68.createCell(0);
        cell_68_68_0_0.setCellValue("1#工位");
        Cell cell_68_68_5_5 = row_68.createCell(5);
        cell_68_68_5_5.setCellValue("2#工位");
        CellRangeAddress cellRangeAddress_68_70_2_4 = new CellRangeAddress(68,70,2,4);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_68_70_2_4);
        CellRangeAddress cellRangeAddress_68_70_7_9 = new CellRangeAddress(68,70,7,9);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_68_70_7_9);
        CellRangeAddress cellRangeAddress_79_82_2_4 = new CellRangeAddress(79,82,2,4);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_79_82_2_4);
        CellRangeAddress cellRangeAddress_79_82_7_9 = new CellRangeAddress(79,82,7,9);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_79_82_7_9);

        CellRangeAddress cellRangeAddress_71_71_2_3 = new CellRangeAddress(71,71,2,3);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_71_71_2_3);
        CellRangeAddress cellRangeAddress_72_72_2_3 = new CellRangeAddress(72,72,2,3);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_72_72_2_3);
        CellRangeAddress cellRangeAddress_73_73_2_3 = new CellRangeAddress(73,73,2,3);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_73_73_2_3);
        CellRangeAddress cellRangeAddress_74_74_2_3 = new CellRangeAddress(74,74,2,3);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_74_74_2_3);
        CellRangeAddress cellRangeAddress_75_75_2_3 = new CellRangeAddress(75,75,2,3);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_75_75_2_3);
        CellRangeAddress cellRangeAddress_76_76_2_3 = new CellRangeAddress(76,76,2,3);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_76_76_2_3);
        CellRangeAddress cellRangeAddress_77_77_2_3 = new CellRangeAddress(77,77,2,3);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_77_77_2_3);
        CellRangeAddress cellRangeAddress_78_78_2_3 = new CellRangeAddress(78,78,2,3);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_78_78_2_3);

        CellRangeAddress cellRangeAddress_71_71_7_8 = new CellRangeAddress(71,71,7,8);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_71_71_7_8);
        CellRangeAddress cellRangeAddress_72_72_7_8 = new CellRangeAddress(72,72,7,8);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_72_72_7_8);
        CellRangeAddress cellRangeAddress_73_73_7_8 = new CellRangeAddress(73,73,7,8);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_73_73_7_8);
        CellRangeAddress cellRangeAddress_74_74_7_8 = new CellRangeAddress(74,74,7,8);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_74_74_7_8);
        CellRangeAddress cellRangeAddress_75_75_7_8 = new CellRangeAddress(75,75,7,8);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_75_75_7_8);
        CellRangeAddress cellRangeAddress_76_76_7_8 = new CellRangeAddress(76,76,7,8);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_76_76_7_8);
        CellRangeAddress cellRangeAddress_77_77_7_8 = new CellRangeAddress(77,77,7,8);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_77_77_7_8);
        CellRangeAddress cellRangeAddress_78_78_7_8 = new CellRangeAddress(78,78,7,8);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_78_78_7_8);

        CellRangeAddress cellRangeAddress_82_82_0_9 = new CellRangeAddress(82,82,0,9);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_82_82_0_9);

        Row row_71 = sheet.createRow(71);
        Cell cell_71_2 = row_71.createCell(2);
        cell_71_2.setCellValue("最高温度");
        Cell cell_71_7 = row_71.createCell(7);
        cell_71_7.setCellValue("最高温度");

        Row row_72 = sheet.createRow(72);
        Cell cell_72_2 = row_72.createCell(2);
        cell_72_2.setCellValue("最高温度发生时间");
        Cell cell_72_7 = row_72.createCell(7);
        cell_72_7.setCellValue("最高温度发生时间");

        Row row_73 = sheet.createRow(73);
        Cell cell_73_2 = row_73.createCell(2);
        cell_73_2.setCellValue("第一次到达95以上时间");
        Cell cell_73_7 = row_73.createCell(7);
        cell_73_7.setCellValue("第一次到达95以上时间");

        Row row_74 = sheet.createRow(74);
        Cell cell_74_2 = row_74.createCell(2);
//        cell_74_2.setCellValue("定点1最高温度");
        cell_74_2.setCellValue("区域最高温度");
        Cell cell_74_7 = row_74.createCell(7);
//        cell_74_7.setCellValue("定点1最高温度");
        cell_74_7.setCellValue("区域最高温度");

        Row row_75 = sheet.createRow(75);
        Cell cell_75_2 = row_75.createCell(2);
        cell_75_2.setCellValue("测温点1最高温度");
        Cell cell_75_7 = row_75.createCell(7);
        cell_75_7.setCellValue("测温点1最高温度");

        Row row_76 = sheet.createRow(76);
        Cell cell_76_2 = row_76.createCell(2);
        cell_76_2.setCellValue("测温点2最高温度");
        Cell cell_76_7 = row_76.createCell(7);
        cell_76_7.setCellValue("测温点2最高温度");

        Row row_77 = sheet.createRow(77);
        Cell cell_77_2 = row_77.createCell(2);
        cell_77_2.setCellValue("测温点3最高温度");
        Cell cell_77_7 = row_77.createCell(7);
        cell_77_7.setCellValue("测温点3最高温度");

        Row row_78 = sheet.createRow(78);
        Cell cell_78_2 = row_78.createCell(2);
        cell_78_2.setCellValue("测温点4最高温度");
        Cell cell_78_7 = row_78.createCell(7);
        cell_78_7.setCellValue("测温点4最高温度");

        CellRangeAddress cellRangeAddress_83_83_0_9 = new CellRangeAddress(83,83,0,9);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_83_83_0_9);
        Row row_83 = sheet.createRow(83);
        Cell cell_83_0 = row_83.createCell(0);
        cell_83_0.setCellValue("3、胎外位置");
        Row row_84 = sheet.createRow(84);
        Cell cell_84_84_0_0 = row_84.createCell(0);
        cell_84_84_0_0.setCellValue("1#工位");
        Cell cell_84_84_5_5 = row_84.createCell(5);
        cell_84_84_5_5.setCellValue("2#工位");

        Row row_87 = sheet.createRow(87);
        Cell cell_87_2 = row_87.createCell(2);
        cell_87_2.setCellValue("最高温度");
        Cell cell_87_7 = row_87.createCell(7);
        cell_87_7.setCellValue("最高温度");

        Row row_88 = sheet.createRow(88);
        Cell cell_88_2 = row_88.createCell(2);
        cell_88_2.setCellValue("最高温度发生时间");
        Cell cell_88_7 = row_88.createCell(7);
        cell_88_7.setCellValue("最高温度发生时间");

        Row row_89 = sheet.createRow(89);
        Cell cell_89_2 = row_89.createCell(2);
        cell_89_2.setCellValue("第一次到达95以上时间");
        Cell cell_89_7 = row_89.createCell(7);
        cell_89_7.setCellValue("第一次到达95以上时间");

        Row row_90 = sheet.createRow(90);
        Cell cell_90_2 = row_90.createCell(2);
//        cell_90_2.setCellValue("定点1最高温度");
        cell_90_2.setCellValue("区域最高温度");
        Cell cell_90_7 = row_90.createCell(7);
//        cell_90_7.setCellValue("定点1最高温度");
        cell_90_7.setCellValue("区域最高温度");

        Row row_91 = sheet.createRow(91);
        Cell cell_91_2 = row_91.createCell(2);
        cell_91_2.setCellValue("测温点1最高温度");
        Cell cell_91_7 = row_91.createCell(7);
        cell_91_7.setCellValue("测温点1最高温度");

        Row row_92 = sheet.createRow(92);
        Cell cell_92_2 = row_92.createCell(2);
        cell_92_2.setCellValue("测温点2最高温度");
        Cell cell_92_7 = row_92.createCell(7);
        cell_92_7.setCellValue("测温点2最高温度");

        Row row_93 = sheet.createRow(93);
        Cell cell_93_2 = row_93.createCell(2);
        cell_93_2.setCellValue("测温点3最高温度");
        Cell cell_93_7 = row_93.createCell(7);
        cell_93_7.setCellValue("测温点3最高温度");

        Row row_94 = sheet.createRow(94);
        Cell cell_94_2 = row_94.createCell(2);
        cell_94_2.setCellValue("测温点4最高温度");
        Cell cell_94_7 = row_94.createCell(7);
        cell_94_7.setCellValue("测温点4最高温度");

        CellRangeAddress cellRangeAddress_84_84_0_1 = new CellRangeAddress(84,84,0,1);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_84_84_0_1);
        CellRangeAddress cellRangeAddress_84_86_2_4 = new CellRangeAddress(84,86,2,4);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_84_86_2_4);
        CellRangeAddress cellRangeAddress_84_84_5_6 = new CellRangeAddress(84,84,5,6);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_84_84_5_6);
        CellRangeAddress cellRangeAddress_84_86_7_9 = new CellRangeAddress(84,86,7,9);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_84_86_7_9);


        CellRangeAddress cellRangeAddress_87_87_2_3 = new CellRangeAddress(87,87,2,3);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_87_87_2_3);
        CellRangeAddress cellRangeAddress_88_88_2_3 = new CellRangeAddress(88,88,2,3);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_88_88_2_3);
        CellRangeAddress cellRangeAddress_89_89_2_3 = new CellRangeAddress(89,89,2,3);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_89_89_2_3);
        CellRangeAddress cellRangeAddress_90_90_2_3 = new CellRangeAddress(90,90,2,3);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_90_90_2_3);
        CellRangeAddress cellRangeAddress_91_91_2_3 = new CellRangeAddress(91,91,2,3);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_91_91_2_3);
        CellRangeAddress cellRangeAddress_92_92_2_3 = new CellRangeAddress(92,92,2,3);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_92_92_2_3);
        CellRangeAddress cellRangeAddress_93_93_2_3 = new CellRangeAddress(93,93,2,3);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_93_93_2_3);
        CellRangeAddress cellRangeAddress_94_94_2_3 = new CellRangeAddress(94,94,2,3);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_94_94_2_3);

        CellRangeAddress cellRangeAddress_87_87_7_8 = new CellRangeAddress(87,87,7,8);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_87_87_7_8);
        CellRangeAddress cellRangeAddress_88_88_7_8 = new CellRangeAddress(88,88,7,8);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_88_88_7_8);
        CellRangeAddress cellRangeAddress_89_89_7_8 = new CellRangeAddress(89,89,7,8);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_89_89_7_8);
        CellRangeAddress cellRangeAddress_90_90_7_8 = new CellRangeAddress(90,90,7,8);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_90_90_7_8);
        CellRangeAddress cellRangeAddress_91_91_7_8 = new CellRangeAddress(91,91,7,8);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_91_91_7_8);
        CellRangeAddress cellRangeAddress_92_92_7_8 = new CellRangeAddress(92,92,7,8);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_92_92_7_8);
        CellRangeAddress cellRangeAddress_93_93_7_8 = new CellRangeAddress(93,93,7,8);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_93_93_7_8);
        CellRangeAddress cellRangeAddress_94_94_7_8 = new CellRangeAddress(94,94,7,8);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_94_94_7_8);

        CellRangeAddress cellRangeAddress_97_100_0_9 = new CellRangeAddress(97,100,0,9);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_97_100_0_9);

        CellRangeAddress cellRangeAddress_95_97_0_9 = new CellRangeAddress(95,97,2,4);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_95_97_0_9);
        CellRangeAddress cellRangeAddress_95_97_7_9 = new CellRangeAddress(95,97,7,9);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_95_97_7_9);


        Row row_104 = sheet.createRow(104);
        Cell cell_104_8 = row_104.createCell(8);
        cell_104_8.setCellValue(sampleInfo1.getSample_test_date());
        Row row_105 = sheet.createRow(105);
        Cell cell_105_8 = row_105.createCell(8);
        cell_105_8.setCellValue("质管处");



        //测试信息
        Cell cell_2_2 = row_2.createCell(2);
        cell_2_2.setCellValue(sampleInfo1.getSample_test_date()+" "+sampleInfo1.getSample_number());
        Cell cell_2_4 = row_2.createCell(4);
        cell_2_4.setCellValue(sampleInfo1.getRoom_number());
        Cell cell_2_7 = row_2.createCell(7);
        cell_2_7.setCellValue(sampleInfo1.getSample_test_date());
        //1#工位测试信息
        Cell cell_4_1 = row_4.createCell(1);
        cell_4_1.setCellValue(sampleInfo1.getSample_specification());
        Cell cell_4_2 = row_4.createCell(2);
        cell_4_2.setCellValue(sampleInfo1.getSample_level());
        Cell cell_4_3 = row_4.createCell(3);
        cell_4_3.setCellValue(sampleInfo1.getSample_pattern());
        Cell cell_4_4 = row_4.createCell(4);
        cell_4_4.setCellValue(sampleInfo1.getSample_brand());
        Cell cell_4_5 = row_4.createCell(5);
        cell_4_5.setCellValue(sampleInfo1.getTire_number());
        Cell cell_4_6 = row_4.createCell(6);
//        cell_4_6.setCellValue(reportExcel_one_M.getHighestTemp());
        Cell cell_4_7 = row_4.createCell(7);
//        cell_4_7.setCellValue(reportExcel_one_L.getHighestTemp());
        Cell cell_4_8 = row_4.createCell(8);
//        cell_4_8.setCellValue(reportExcel_one_R.getHighestTemp());
        Cell cell_4_9 = row_4.createCell(9);
        cell_4_9.setCellValue(sampleInfo1.getSample_scheme());

        //1#工位胎冠
        if(reportExcel_one_M != null){
            cell_4_6.setCellValue(reportExcel_one_M.getHighestTemp());

            Cell cell_13_4 = row_13.createCell(4);
            cell_13_4.setCellValue(reportExcel_one_M.getHighestTemp());
            Cell cell_14_4 = row_14.createCell(4);
            cell_14_4.setCellValue(reportExcel_one_M.getHighestTempTime());
            Cell cell_15_4 = row_15.createCell(4);
            cell_15_4.setCellValue(reportExcel_one_M.getReachNFTempTime());
            Cell cell_16_4 = row_16.createCell(4);
            cell_16_4.setCellValue(reportExcel_one_M.getPointOneHighestTemp());
            Cell cell_17_4 = row_17.createCell(4);
            cell_17_4.setCellValue(reportExcel_one_M.getPointTwoHighestTemp());
            Cell cell_18_4 = row_18.createCell(4);
            cell_18_4.setCellValue(reportExcel_one_M.getPointThreeHighestTemp());
            Cell cell_19_4 = row_19.createCell(4);
            cell_19_4.setCellValue(reportExcel_one_M.getPointFourHighestTemp());
            Cell cell_20_4 = row_20.createCell(4);
            cell_20_4.setCellValue(reportExcel_one_M.getPointFiveHighestTemp());
        }



        //1#工位胎侧区域1(L)
        if(reportExcel_one_L != null){
            cell_4_7.setCellValue(reportExcel_one_L.getHighestTemp());

            Cell cell_71_4 = row_71.createCell(4);
            cell_71_4.setCellValue(reportExcel_one_L.getHighestTemp());
            Cell cell_72_4 = row_72.createCell(4);
            cell_72_4.setCellValue(reportExcel_one_L.getHighestTempTime());
            Cell cell_73_4 = row_73.createCell(4);
            cell_73_4.setCellValue(reportExcel_one_L.getReachNFTempTime());
            Cell cell_74_4 = row_74.createCell(4);
            cell_74_4.setCellValue(reportExcel_one_L.getPointOneHighestTemp());
            Cell cell_75_4 = row_75.createCell(4);
            cell_75_4.setCellValue(reportExcel_one_L.getPointTwoHighestTemp());
            Cell cell_76_4 = row_76.createCell(4);
            cell_76_4.setCellValue(reportExcel_one_L.getPointThreeHighestTemp());
            Cell cell_77_4 = row_77.createCell(4);
            cell_77_4.setCellValue(reportExcel_one_L.getPointFourHighestTemp());
            Cell cell_78_4 = row_78.createCell(4);
            cell_78_4.setCellValue(reportExcel_one_L.getPointFiveHighestTemp());
        }



        //1#工位胎侧区域2(R)
        if(reportExcel_one_R != null){
            cell_4_8.setCellValue(reportExcel_one_R.getHighestTemp());

            Cell cell_87_4 = row_87.createCell(4);
            cell_87_4.setCellValue(reportExcel_one_R.getHighestTemp());
            Cell cell_88_4 = row_88.createCell(4);
            cell_88_4.setCellValue(reportExcel_one_R.getHighestTempTime());
            Cell cell_89_4 = row_89.createCell(4);
            cell_89_4.setCellValue(reportExcel_one_R.getReachNFTempTime());
            Cell cell_90_4 = row_90.createCell(4);
            cell_90_4.setCellValue(reportExcel_one_R.getPointOneHighestTemp());
            Cell cell_91_4 = row_91.createCell(4);
            cell_91_4.setCellValue(reportExcel_one_R.getPointTwoHighestTemp());
            Cell cell_92_4 = row_92.createCell(4);
            cell_92_4.setCellValue(reportExcel_one_R.getPointThreeHighestTemp());
            Cell cell_93_4 = row_93.createCell(4);
            cell_93_4.setCellValue(reportExcel_one_R.getPointFourHighestTemp());
            Cell cell_94_4 = row_94.createCell(4);
            cell_94_4.setCellValue(reportExcel_one_R.getPointFiveHighestTemp());
        }



        setRowStyle(hssfCellStyle,sheet,2,6,0,10);
        setRowStyle(hssfCellStyle,sheet,13,21,0,10);
        setRowStyle(hssfCellStyle,sheet,71,79,0,10);
        setRowStyle(hssfCellStyle,sheet,87,95,0,10);


        BufferedImage bufferImg1M = null;
        BufferedImage bufferImg1R = null;
        BufferedImage bufferImg1L = null;
        BufferedImage bufferImg1MQX = null;
        BufferedImage bufferImg1RQX = null;
        BufferedImage bufferImg1LQX = null;

        try {
            //1#胎冠热成像图
            if(reportExcel_one_M != null){
                ByteArrayOutputStream byteArrayOut1M = new ByteArrayOutputStream(); // 先把读进来的图片放到一个ByteArrayOutputStream中，以便产生ByteArray
                bufferImg1M = ImageIO.read(new File(reportExcel_one_M.getPlp_photo_position()));     //将图片读到BufferedImage
                ImageIO.write(bufferImg1M, "png", byteArrayOut1M);        // 将图片写入流中
                HSSFPatriarch hssfPatriarch1M = sheet.createDrawingPatriarch();    //将图片写入excel中
                HSSFClientAnchor anchor1M = new HSSFClientAnchor(0,0,0,0,(short)0,11,(short)2,24);
                hssfPatriarch1M.createPicture(anchor1M,wb.addPicture(byteArrayOut1M.toByteArray(),HSSFWorkbook.PICTURE_TYPE_JPEG));    //导入图片

            }

            /**
             * 该构造函数有8个参数
             * 前四个参数是控制图片在单元格的位置，分别是图片距离单元格left，top，right，bottom的像素距离
             * 后四个参数，前连个表示图片左上角所在的cellNum和 rowNum，后天个参数对应的表示图片右下角所在的cellNum和 rowNum，
             **/


            //1#胎冠曲线图
            String picturePath1MQX = "D://"+sampleInfo1.getRoom_number()+"//"+sampleInfo1.getCam_station_number()+"//"+sampleInfo1.getSample_test_date()+"//";
            String pictureFile1MQX = sampleInfo1.getRoom_number()+"-"+sampleInfo1.getCam_station_number()+"-"+sampleInfo1.getSample_test_date()+"-"+sampleInfo1.getTire_number()+".jpeg";
            String wholePicturePath1MQX = picturePath1MQX + pictureFile1MQX;
            ByteArrayOutputStream byteArrayOut1MQX = new ByteArrayOutputStream();
            bufferImg1MQX = ImageIO.read(new File(wholePicturePath1MQX));     //将图片读到BufferedImage
            ImageIO.write(bufferImg1MQX, "png", byteArrayOut1MQX);        // 将图片写入流中
            HSSFPatriarch hssfPatriarch1MQX = sheet.createDrawingPatriarch();
            HSSFClientAnchor anchor1MQX = new HSSFClientAnchor(0,0,0,0,(short)1,27,(short)9,46);
            hssfPatriarch1MQX.createPicture(anchor1MQX,wb.addPicture(byteArrayOut1MQX.toByteArray(),HSSFWorkbook.PICTURE_TYPE_JPEG));    //导入图片
            CellRangeAddress cellRangeAddress_46_46_0_9 = new CellRangeAddress(46,46,0,9);   //创建合并单元格
            sheet.addMergedRegion(cellRangeAddress_46_46_0_9);

            //1#胎侧-1热成像图
            if(reportExcel_one_L != null){
                ByteArrayOutputStream byteArrayOut1L = new ByteArrayOutputStream();
                bufferImg1L = ImageIO.read(new File(reportExcel_one_L.getPlp_photo_position()));     //将图片读到BufferedImage
                ImageIO.write(bufferImg1L, "png", byteArrayOut1L);        // 将图片写入流中
                HSSFPatriarch hssfPatriarch1L = sheet.createDrawingPatriarch();
                HSSFClientAnchor anchor1L = new HSSFClientAnchor(0,0,0,0,(short)0,69,(short)2,82);
                hssfPatriarch1L.createPicture(anchor1L,wb.addPicture(byteArrayOut1L.toByteArray(),HSSFWorkbook.PICTURE_TYPE_JPEG));    //导入图片
            }


            //1#胎侧-2热成像图
            if(reportExcel_one_R != null){
                ByteArrayOutputStream byteArrayOut1R = new ByteArrayOutputStream();
                bufferImg1R = ImageIO.read(new File(reportExcel_one_R.getPlp_photo_position()));     //将图片读到BufferedImage
                ImageIO.write(bufferImg1R, "png", byteArrayOut1R);        // 将图片写入流中
                HSSFPatriarch hssfPatriarch1R = sheet.createDrawingPatriarch();
                HSSFClientAnchor anchor1R = new HSSFClientAnchor(0,0,0,0,(short)0,85,(short)2,98);
                hssfPatriarch1R.createPicture(anchor1R,wb.addPicture(byteArrayOut1R.toByteArray(),HSSFWorkbook.PICTURE_TYPE_JPEG));    //导入图片
            }


            String wholeFilePath = filePath+fileName;
            File file = new File(wholeFilePath);
            if(!file.getParentFile().exists()){     //若该路径下不存在此文件目录则创建
                boolean result = file.getParentFile().mkdirs();
                if (!result) {
                    System.out.println("创建失败");
                }
            }
            FileOutputStream fos = new FileOutputStream(file);
            wb.write(fos);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("io erorr : " + e.getMessage());
        }
    }

    //生成2号仓位报表信息
    public void getExcelReportTwo(SampleInfo sampleInfo2){   //List<SampleInfo> list, SampleInfo sampleInfo,String cam_ip
        StringBuffer fatherFileName2 = new StringBuffer();
        Map<String, Object> room2 = new HashMap<String, Object>();
        room2.put("room_number",sampleInfo2.getRoom_number());
        room2.put("cam_station_number",sampleInfo2.getCam_station_number());
        List<Room>  cam_station_two = reportExcelService.getCam_ip_position(room2);
        ReportExcelInfo reportExcel_two_L = null, reportExcel_two_M = null, reportExcel_two_R = null;

        for(int j=0;j<cam_station_two.size();j++){
            sampleInfo2.setCam_ip(cam_station_two.get(j).getCam_ip());
            fatherFileName2.append(cam_station_two.get(j).getCam_ip()+" ");
            if(cam_station_two.get(j).getCam_position().equals("L")){
                reportExcel_two_L = returnReportExcel(sampleInfo2);
            }
            if(cam_station_two.get(j).getCam_position().equals("M")){
                reportExcel_two_M = returnReportExcel(sampleInfo2);
            }
            if(cam_station_two.get(j).getCam_position().equals("R")){
                reportExcel_two_R = returnReportExcel(sampleInfo2);
            }
        }
        fatherFileName2.deleteCharAt(fatherFileName2.length()-1);
        String fileName = sampleInfo2.getSample_specification()+"_"+sampleInfo2.getSample_level()+"_"+sampleInfo2.getSample_pattern()+"_"+sampleInfo2.getSample_test_date()+"_"+sampleInfo2.getSample_number()+"_"+sampleInfo2.getSample_person()+"_"+sampleInfo2.getTire_number()+".xls";
        //String fileName = sampleInfo2.getSample_specification()+"_"+sampleInfo2.getSample_level()+"_"+sampleInfo2.getSample_pattern()+"_"+sampleInfo2.getSample_test_date()+"_"+sampleInfo2.getSample_number()+"_"+sampleInfo2.getSample_person()+"_"+sampleInfo2.getTire_number()+"_"+sampleInfo2.getRoom_number()+"_"+sampleInfo2.getCam_station_number()+"_"+fatherFileName2+"_.xls";
        String filePath = "D://"+sampleInfo2.getRoom_number()+"//"+sampleInfo2.getCam_station_number()+"//"+sampleInfo2.getSample_test_date()+"//";

        HSSFWorkbook wb = new HSSFWorkbook();       //创建工作簿
        HSSFSheet sheet = wb.createSheet(fileName);      //创建工作表
        sheet.setDefaultColumnWidth(16);    //设置默认列宽

        CellRangeAddress cellRangeAddress_0_0_0_9 = new CellRangeAddress(0,0,0,9);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_0_0_0_9);

        HSSFCellStyle xssfCellStyle = wb.createCellStyle();
        Font font = wb.createFont();
        Row row_0 = sheet.createRow(0);       //创建行
        Cell cell_0_0 = row_0.createCell(0);    //创建列
        cell_0_0.setCellValue("红外检测报告");
        font.setFontHeightInPoints((short)20);
        xssfCellStyle.setFont(font);
        xssfCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);   //居中对齐
        cell_0_0.setCellStyle(xssfCellStyle);

        CellRangeAddress cellRangeAddress_1_1_0_9 = new CellRangeAddress(1,1,0,9);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_1_1_0_9);

        Row row_1 = sheet.createRow(1);
        Cell cell_1_0 = row_1.createCell(0);
        cell_1_0.setCellValue("一、测试信息");
        CellRangeAddress cellRangeAddress_2_2_0_1 = new CellRangeAddress(2,2,0,1);
        sheet.addMergedRegion(cellRangeAddress_2_2_0_1);

        HSSFCellStyle hssfCellStyle = wb.createCellStyle();
        hssfCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);   //水平居中对齐


        Row row_2 = sheet.createRow(2);
        Cell cell_2_0 = row_2.createCell(0);
        cell_2_0.setCellValue("报告编号");
        Cell cell_2_3 = row_2.createCell(3);
        cell_2_3.setCellValue("测试机台");
        CellRangeAddress cellRangeAddress_2_2_4_5 = new CellRangeAddress(2,2,4,5);
        sheet.addMergedRegion(cellRangeAddress_2_2_4_5);

        Cell cell_2_6 = row_2.createCell(6);
        cell_2_6.setCellValue("测试日期");
        CellRangeAddress cellRangeAddress_2_2_7_9 = new CellRangeAddress(2,2,7,9);
        sheet.addMergedRegion(cellRangeAddress_2_2_7_9);
        row_2.setRowStyle(hssfCellStyle);
        Row row_3 = sheet.createRow(3);
        Cell cell_3_0 = row_3.createCell(0);
        cell_3_0.setCellValue("工位");
        Cell cell_3_1 = row_3.createCell(1);
        cell_3_1.setCellValue("规格");
        Cell cell_3_2 = row_3.createCell(2);
        cell_3_2.setCellValue("层级");
        Cell cell_3_3 = row_3.createCell(3);
        cell_3_3.setCellValue("花纹");
        Cell cell_3_4 = row_3.createCell(4);
        cell_3_4.setCellValue("品牌");
        Cell cell_3_5 = row_3.createCell(5);
        cell_3_5.setCellValue("胎号");
        Cell cell_3_6 = row_3.createCell(6);
        cell_3_6.setCellValue("胎冠区域最高温度");
        Cell cell_3_7 = row_3.createCell(7);
        cell_3_7.setCellValue("胎内最高温度");
        Cell cell_3_8 = row_3.createCell(8);
        cell_3_8.setCellValue("胎外最高温度");
        Cell cell_3_9 = row_3.createCell(9);
        cell_3_9.setCellValue("方案");
        row_3.setRowStyle(hssfCellStyle);
        Row row_4 = sheet.createRow(4);
        Cell cell_4_0 = row_4.createCell(0);
        cell_4_0.setCellValue("1#");
        row_4.setRowStyle(hssfCellStyle);
        Row row_5 = sheet.createRow(5);
        Cell cell_5_0 = row_5.createCell(0);
        cell_5_0.setCellValue("2#");
        row_5.setRowStyle(hssfCellStyle);

        CellRangeAddress cellRangeAddress_8_8_0_9 = new CellRangeAddress(8,8,0,9);
        sheet.addMergedRegion(cellRangeAddress_8_8_0_9);
        Row row_8 = sheet.createRow(8);
        Cell cell_8_0 = row_8.createCell(0);
        cell_8_0.setCellValue("二、测试过程数据");

        CellRangeAddress cellRangeAddress_6_7_0_9 = new CellRangeAddress(6,7,0,9);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_6_7_0_9);

        CellRangeAddress cellRangeAddress_9_9_0_9 = new CellRangeAddress(9,9,0,9);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_9_9_0_9);
        CellRangeAddress cellRangeAddress_10_10_0_1 = new CellRangeAddress(10,10,0,1);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_10_10_0_1);
        CellRangeAddress cellRangeAddress_10_10_2_4 = new CellRangeAddress(10,10,2,4);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_10_10_2_4);
        CellRangeAddress cellRangeAddress_10_10_5_6 = new CellRangeAddress(10,10,5,6);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_10_10_5_6);
        CellRangeAddress cellRangeAddress_10_10_7_9 = new CellRangeAddress(10,10,7,9);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_10_10_7_9);

        CellRangeAddress cellRangeAddress_10_12_2_4 = new CellRangeAddress(10,12,2,4);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_10_12_2_4);
        CellRangeAddress cellRangeAddress_10_12_7_9 = new CellRangeAddress(10,12,7,9);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_10_12_7_9);

        CellRangeAddress cellRangeAddress__21_24_2_4= new CellRangeAddress(21,24,2,4);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress__21_24_2_4);
        CellRangeAddress cellRangeAddress__21_24_7_9= new CellRangeAddress(21,24,7,9);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress__21_24_7_9);

        CellRangeAddress cellRangeAddress_13_13_2_3 = new CellRangeAddress(13,13,2,3);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_13_13_2_3);
        CellRangeAddress cellRangeAddress_14_14_2_3 = new CellRangeAddress(14,14,2,3);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_14_14_2_3);
        CellRangeAddress cellRangeAddress_15_15_2_3 = new CellRangeAddress(15,15,2,3);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_15_15_2_3);
        CellRangeAddress cellRangeAddress_16_16_2_3 = new CellRangeAddress(16,16,2,3);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_16_16_2_3);
        CellRangeAddress cellRangeAddress_17_17_2_3 = new CellRangeAddress(17,17,2,3);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_17_17_2_3);
        CellRangeAddress cellRangeAddress_18_18_2_3 = new CellRangeAddress(18,18,2,3);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_18_18_2_3);
        CellRangeAddress cellRangeAddress_19_19_2_3 = new CellRangeAddress(19,19,2,3);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_19_19_2_3);
        CellRangeAddress cellRangeAddress_20_20_2_3 = new CellRangeAddress(20,20,2,3);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_20_20_2_3);

        CellRangeAddress cellRangeAddress_13_13_7_8 = new CellRangeAddress(13,13,7,8);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_13_13_7_8);
        CellRangeAddress cellRangeAddress_14_14_7_8 = new CellRangeAddress(14,14,7,8);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_14_14_7_8);
        CellRangeAddress cellRangeAddress_15_15_7_8 = new CellRangeAddress(15,15,7,8);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_15_15_7_8);
        CellRangeAddress cellRangeAddress_16_16_7_8 = new CellRangeAddress(16,16,7,8);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_16_16_7_8);
        CellRangeAddress cellRangeAddress_17_17_7_8 = new CellRangeAddress(17,17,7,8);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_17_17_7_8);
        CellRangeAddress cellRangeAddress_18_18_7_8 = new CellRangeAddress(18,18,7,8);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_18_18_7_8);
        CellRangeAddress cellRangeAddress_19_19_7_8 = new CellRangeAddress(19,19,7,8);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_19_19_7_8);
        CellRangeAddress cellRangeAddress_20_20_7_8 = new CellRangeAddress(20,20,7,8);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_20_20_7_8);

        CellRangeAddress cellRangeAddress_24_24_0_9 = new CellRangeAddress(24,24,0,9);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_24_24_0_9);

        Row row_9 = sheet.createRow(9);
        Cell cell_9_0 = row_9.createCell(0);
        cell_9_0.setCellValue("1、胎冠位置");
        row_9.setRowStyle(hssfCellStyle);
        Row row_10 = sheet.createRow(10);
        Cell cell_10_0 = row_10.createCell(0);
        cell_10_0.setCellValue("1#工位");
        Cell cell_10_5 = row_10.createCell(5);
        cell_10_5.setCellValue("2#工位");
        row_10.setRowStyle(hssfCellStyle);

        Row row_13 = sheet.createRow(13);
        Cell cell_13_2 = row_13.createCell(2);
        cell_13_2.setCellValue("最高温度");
        Cell cell_13_7 = row_13.createCell(7);
        cell_13_7.setCellValue("最高温度");

        Row row_14 = sheet.createRow(14);
        Cell cell_14_2 = row_14.createCell(2);
        cell_14_2.setCellValue("最高温度发生时间");
        Cell cell_14_7 = row_14.createCell(7);
        cell_14_7.setCellValue("最高温度发生时间");

        Row row_15 = sheet.createRow(15);
        Cell cell_15_2 = row_15.createCell(2);
        cell_15_2.setCellValue("第一次到达95以上时间");
        Cell cell_15_7 = row_15.createCell(7);
        cell_15_7.setCellValue("第一次到达95以上时间");

        Row row_16 = sheet.createRow(16);
        Cell cell_16_2 = row_16.createCell(2);
//        cell_16_2.setCellValue("定点1最高温度");
        cell_16_2.setCellValue("区域最高温度");
        Cell cell_16_7 = row_16.createCell(7);
//        cell_16_7.setCellValue("定点1最高温度");
        cell_16_7.setCellValue("区域最高温度");

        Row row_17 = sheet.createRow(17);
        Cell cell_17_2 = row_17.createCell(2);
        cell_17_2.setCellValue("测温点1最高温度");
        Cell cell_17_7 = row_17.createCell(7);
        cell_17_7.setCellValue("测温点1最高温度");

        Row row_18 = sheet.createRow(18);
        Cell cell_18_2 = row_18.createCell(2);
        cell_18_2.setCellValue("测温点2最高温度");
        Cell cell_18_7 = row_18.createCell(7);
        cell_18_7.setCellValue("测温点2最高温度");

        Row row_19 = sheet.createRow(19);
        Cell cell_19_2 = row_19.createCell(2);
        cell_19_2.setCellValue("测温点3最高温度");
        Cell cell_19_7 = row_19.createCell(7);
        cell_19_7.setCellValue("测温点3最高温度");

        Row row_20 = sheet.createRow(20);
        Cell cell_20_2 = row_20.createCell(2);
        cell_20_2.setCellValue("测温点4最高温度");
        Cell cell_20_7 = row_20.createCell(7);
        cell_20_7.setCellValue("测温点4最高温度");

        CellRangeAddress cellRangeAddress_25_25_0_9 = new CellRangeAddress(25,25,0,9);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_25_25_0_9);
        Row row_25 = sheet.createRow(25);
        Cell cell_25_0 = row_25.createCell(0);
        cell_25_0.setCellValue("曲线变化");

        CellRangeAddress cellRangeAddress_26_66_0_9 = new CellRangeAddress(26,66,0,9);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_26_66_0_9);


        CellRangeAddress cellRangeAddress_67_67_0_9 = new CellRangeAddress(67,67,0,9);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_67_67_0_9);
        CellRangeAddress cellRangeAddress_68_68_0_1 = new CellRangeAddress(68,68,0,1);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_68_68_0_1);
        CellRangeAddress cellRangeAddress_68_68_2_4 = new CellRangeAddress(68,68,2,4);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_68_68_2_4);
        CellRangeAddress cellRangeAddress_68_68_5_6 = new CellRangeAddress(68,68,5,6);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_68_68_5_6);
        CellRangeAddress cellRangeAddress_68_68_7_9 = new CellRangeAddress(68,68,7,9);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_68_68_7_9);
        Row row_67 = sheet.createRow(67);
        Cell cell_67_67_0_0 = row_67.createCell(0);
        cell_67_67_0_0.setCellValue("2、胎内位置");


        Row row_68 = sheet.createRow(68);
        Cell cell_68_68_0_0 = row_68.createCell(0);
        cell_68_68_0_0.setCellValue("1#工位");
        Cell cell_68_68_5_5 = row_68.createCell(5);
        cell_68_68_5_5.setCellValue("2#工位");
        CellRangeAddress cellRangeAddress_68_70_2_4 = new CellRangeAddress(68,70,2,4);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_68_70_2_4);
        CellRangeAddress cellRangeAddress_68_70_7_9 = new CellRangeAddress(68,70,7,9);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_68_70_7_9);
        CellRangeAddress cellRangeAddress_79_82_2_4 = new CellRangeAddress(79,82,2,4);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_79_82_2_4);
        CellRangeAddress cellRangeAddress_79_82_7_9 = new CellRangeAddress(79,82,7,9);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_79_82_7_9);

        CellRangeAddress cellRangeAddress_71_71_2_3 = new CellRangeAddress(71,71,2,3);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_71_71_2_3);
        CellRangeAddress cellRangeAddress_72_72_2_3 = new CellRangeAddress(72,72,2,3);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_72_72_2_3);
        CellRangeAddress cellRangeAddress_73_73_2_3 = new CellRangeAddress(73,73,2,3);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_73_73_2_3);
        CellRangeAddress cellRangeAddress_74_74_2_3 = new CellRangeAddress(74,74,2,3);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_74_74_2_3);
        CellRangeAddress cellRangeAddress_75_75_2_3 = new CellRangeAddress(75,75,2,3);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_75_75_2_3);
        CellRangeAddress cellRangeAddress_76_76_2_3 = new CellRangeAddress(76,76,2,3);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_76_76_2_3);
        CellRangeAddress cellRangeAddress_77_77_2_3 = new CellRangeAddress(77,77,2,3);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_77_77_2_3);
        CellRangeAddress cellRangeAddress_78_78_2_3 = new CellRangeAddress(78,78,2,3);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_78_78_2_3);

        CellRangeAddress cellRangeAddress_71_71_7_8 = new CellRangeAddress(71,71,7,8);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_71_71_7_8);
        CellRangeAddress cellRangeAddress_72_72_7_8 = new CellRangeAddress(72,72,7,8);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_72_72_7_8);
        CellRangeAddress cellRangeAddress_73_73_7_8 = new CellRangeAddress(73,73,7,8);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_73_73_7_8);
        CellRangeAddress cellRangeAddress_74_74_7_8 = new CellRangeAddress(74,74,7,8);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_74_74_7_8);
        CellRangeAddress cellRangeAddress_75_75_7_8 = new CellRangeAddress(75,75,7,8);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_75_75_7_8);
        CellRangeAddress cellRangeAddress_76_76_7_8 = new CellRangeAddress(76,76,7,8);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_76_76_7_8);
        CellRangeAddress cellRangeAddress_77_77_7_8 = new CellRangeAddress(77,77,7,8);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_77_77_7_8);
        CellRangeAddress cellRangeAddress_78_78_7_8 = new CellRangeAddress(78,78,7,8);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_78_78_7_8);

        CellRangeAddress cellRangeAddress_82_82_0_9 = new CellRangeAddress(82,82,0,9);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_82_82_0_9);

        Row row_71 = sheet.createRow(71);
        Cell cell_71_2 = row_71.createCell(2);
        cell_71_2.setCellValue("最高温度");
        Cell cell_71_7 = row_71.createCell(7);
        cell_71_7.setCellValue("最高温度");

        Row row_72 = sheet.createRow(72);
        Cell cell_72_2 = row_72.createCell(2);
        cell_72_2.setCellValue("最高温度发生时间");
        Cell cell_72_7 = row_72.createCell(7);
        cell_72_7.setCellValue("最高温度发生时间");

        Row row_73 = sheet.createRow(73);
        Cell cell_73_2 = row_73.createCell(2);
        cell_73_2.setCellValue("第一次到达95以上时间");
        Cell cell_73_7 = row_73.createCell(7);
        cell_73_7.setCellValue("第一次到达95以上时间");

        Row row_74 = sheet.createRow(74);
        Cell cell_74_2 = row_74.createCell(2);
//        cell_74_2.setCellValue("定点1最高温度");
        cell_74_2.setCellValue("区域最高温度");
        Cell cell_74_7 = row_74.createCell(7);
//        cell_74_7.setCellValue("定点1最高温度");
        cell_74_7.setCellValue("区域最高温度");
        Row row_75 = sheet.createRow(75);
        Cell cell_75_2 = row_75.createCell(2);
        cell_75_2.setCellValue("测温点1最高温度");
        Cell cell_75_7 = row_75.createCell(7);
        cell_75_7.setCellValue("测温点1最高温度");

        Row row_76 = sheet.createRow(76);
        Cell cell_76_2 = row_76.createCell(2);
        cell_76_2.setCellValue("测温点2最高温度");
        Cell cell_76_7 = row_76.createCell(7);
        cell_76_7.setCellValue("测温点2最高温度");

        Row row_77 = sheet.createRow(77);
        Cell cell_77_2 = row_77.createCell(2);
        cell_77_2.setCellValue("测温点3最高温度");
        Cell cell_77_7 = row_77.createCell(7);
        cell_77_7.setCellValue("测温点3最高温度");

        Row row_78 = sheet.createRow(78);
        Cell cell_78_2 = row_78.createCell(2);
        cell_78_2.setCellValue("测温点4最高温度");
        Cell cell_78_7 = row_78.createCell(7);
        cell_78_7.setCellValue("测温点4最高温度");

        CellRangeAddress cellRangeAddress_83_83_0_9 = new CellRangeAddress(83,83,0,9);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_83_83_0_9);
        Row row_83 = sheet.createRow(83);
        Cell cell_83_0 = row_83.createCell(0);
        cell_83_0.setCellValue("3、胎外位置");
        Row row_84 = sheet.createRow(84);
        Cell cell_84_84_0_0 = row_84.createCell(0);
        cell_84_84_0_0.setCellValue("1#工位");
        Cell cell_84_84_5_5 = row_84.createCell(5);
        cell_84_84_5_5.setCellValue("2#工位");

        Row row_87 = sheet.createRow(87);
        Cell cell_87_2 = row_87.createCell(2);
        cell_87_2.setCellValue("最高温度");
        Cell cell_87_7 = row_87.createCell(7);
        cell_87_7.setCellValue("最高温度");

        Row row_88 = sheet.createRow(88);
        Cell cell_88_2 = row_88.createCell(2);
        cell_88_2.setCellValue("最高温度发生时间");
        Cell cell_88_7 = row_88.createCell(7);
        cell_88_7.setCellValue("最高温度发生时间");

        Row row_89 = sheet.createRow(89);
        Cell cell_89_2 = row_89.createCell(2);
        cell_89_2.setCellValue("第一次到达95以上时间");
        Cell cell_89_7 = row_89.createCell(7);
        cell_89_7.setCellValue("第一次到达95以上时间");

        Row row_90 = sheet.createRow(90);
        Cell cell_90_2 = row_90.createCell(2);
//        cell_90_2.setCellValue("定点1最高温度");
        cell_90_2.setCellValue("区域最高温度");
        Cell cell_90_7 = row_90.createCell(7);
//        cell_90_7.setCellValue("定点1最高温度");
        cell_90_7.setCellValue("区域最高温度");
        Row row_91 = sheet.createRow(91);
        Cell cell_91_2 = row_91.createCell(2);
        cell_91_2.setCellValue("测温点1最高温度");
        Cell cell_91_7 = row_91.createCell(7);
        cell_91_7.setCellValue("测温点1最高温度");

        Row row_92 = sheet.createRow(92);
        Cell cell_92_2 = row_92.createCell(2);
        cell_92_2.setCellValue("测温点2最高温度");
        Cell cell_92_7 = row_92.createCell(7);
        cell_92_7.setCellValue("测温点2最高温度");

        Row row_93 = sheet.createRow(93);
        Cell cell_93_2 = row_93.createCell(2);
        cell_93_2.setCellValue("测温点3最高温度");
        Cell cell_93_7 = row_93.createCell(7);
        cell_93_7.setCellValue("测温点3最高温度");

        Row row_94 = sheet.createRow(94);
        Cell cell_94_2 = row_94.createCell(2);
        cell_94_2.setCellValue("测温点4最高温度");
        Cell cell_94_7 = row_94.createCell(7);
        cell_94_7.setCellValue("测温点4最高温度");

        CellRangeAddress cellRangeAddress_84_84_0_1 = new CellRangeAddress(84,84,0,1);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_84_84_0_1);
        CellRangeAddress cellRangeAddress_84_86_2_4 = new CellRangeAddress(84,86,2,4);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_84_86_2_4);
        CellRangeAddress cellRangeAddress_84_84_5_6 = new CellRangeAddress(84,84,5,6);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_84_84_5_6);
        CellRangeAddress cellRangeAddress_84_86_7_9 = new CellRangeAddress(84,86,7,9);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_84_86_7_9);


        CellRangeAddress cellRangeAddress_87_87_2_3 = new CellRangeAddress(87,87,2,3);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_87_87_2_3);
        CellRangeAddress cellRangeAddress_88_88_2_3 = new CellRangeAddress(88,88,2,3);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_88_88_2_3);
        CellRangeAddress cellRangeAddress_89_89_2_3 = new CellRangeAddress(89,89,2,3);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_89_89_2_3);
        CellRangeAddress cellRangeAddress_90_90_2_3 = new CellRangeAddress(90,90,2,3);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_90_90_2_3);
        CellRangeAddress cellRangeAddress_91_91_2_3 = new CellRangeAddress(91,91,2,3);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_91_91_2_3);
        CellRangeAddress cellRangeAddress_92_92_2_3 = new CellRangeAddress(92,92,2,3);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_92_92_2_3);
        CellRangeAddress cellRangeAddress_93_93_2_3 = new CellRangeAddress(93,93,2,3);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_93_93_2_3);
        CellRangeAddress cellRangeAddress_94_94_2_3 = new CellRangeAddress(94,94,2,3);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_94_94_2_3);

        CellRangeAddress cellRangeAddress_87_87_7_8 = new CellRangeAddress(87,87,7,8);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_87_87_7_8);
        CellRangeAddress cellRangeAddress_88_88_7_8 = new CellRangeAddress(88,88,7,8);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_88_88_7_8);
        CellRangeAddress cellRangeAddress_89_89_7_8 = new CellRangeAddress(89,89,7,8);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_89_89_7_8);
        CellRangeAddress cellRangeAddress_90_90_7_8 = new CellRangeAddress(90,90,7,8);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_90_90_7_8);
        CellRangeAddress cellRangeAddress_91_91_7_8 = new CellRangeAddress(91,91,7,8);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_91_91_7_8);
        CellRangeAddress cellRangeAddress_92_92_7_8 = new CellRangeAddress(92,92,7,8);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_92_92_7_8);
        CellRangeAddress cellRangeAddress_93_93_7_8 = new CellRangeAddress(93,93,7,8);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_93_93_7_8);
        CellRangeAddress cellRangeAddress_94_94_7_8 = new CellRangeAddress(94,94,7,8);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_94_94_7_8);

        CellRangeAddress cellRangeAddress_97_100_0_9 = new CellRangeAddress(97,100,0,9);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_97_100_0_9);

        CellRangeAddress cellRangeAddress_95_97_0_9 = new CellRangeAddress(95,97,2,4);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_95_97_0_9);
        CellRangeAddress cellRangeAddress_95_97_7_9 = new CellRangeAddress(95,97,7,9);   //创建合并单元格
        sheet.addMergedRegion(cellRangeAddress_95_97_7_9);



        Row row_104 = sheet.createRow(104);
        Cell cell_104_8 = row_104.createCell(8);
        cell_104_8.setCellValue(sampleInfo2.getSample_test_date());
        Row row_105 = sheet.createRow(105);
        Cell cell_105_8 = row_105.createCell(8);
        cell_105_8.setCellValue("质管处");



        //测试信息
        Cell cell_2_2 = row_2.createCell(2);
        cell_2_2.setCellValue(sampleInfo2.getSample_test_date()+" "+sampleInfo2.getSample_number());
        Cell cell_2_4 = row_2.createCell(4);
        cell_2_4.setCellValue(sampleInfo2.getRoom_number());
        Cell cell_2_7 = row_2.createCell(7);
        cell_2_7.setCellValue(sampleInfo2.getSample_test_date());

        //2#测试信息
        Cell cell_5_1 = row_5.createCell(1);
        cell_5_1.setCellValue(sampleInfo2.getSample_specification());
        Cell cell_5_2 = row_5.createCell(2);
        cell_5_2.setCellValue(sampleInfo2.getSample_level());
        Cell cell_5_3 = row_5.createCell(3);
        cell_5_3.setCellValue(sampleInfo2.getSample_pattern());
        Cell cell_5_4 = row_5.createCell(4);
        cell_5_4.setCellValue(sampleInfo2.getSample_brand());
        Cell cell_5_5 = row_5.createCell(5);
        cell_5_5.setCellValue(sampleInfo2.getTire_number());
        Cell cell_5_6 = row_5.createCell(6);
        if(reportExcel_two_M != null){
            cell_5_6.setCellValue(reportExcel_two_M.getHighestTemp());
        }

        Cell cell_5_7 = row_5.createCell(7);
        if(reportExcel_two_L != null){
            cell_5_7.setCellValue(reportExcel_two_L.getHighestTemp());
        }

        Cell cell_5_8 = row_5.createCell(8);
        if(reportExcel_two_R != null){
            cell_5_8.setCellValue(reportExcel_two_R.getHighestTemp());
        }

        Cell cell_5_9 = row_5.createCell(9);
        cell_5_9.setCellValue(sampleInfo2.getSample_scheme());



        //2#工位胎冠
        if(reportExcel_two_M != null){
            Cell cell_13_9 = row_13.createCell(9);
            cell_13_9.setCellValue(reportExcel_two_M.getHighestTemp());
            Cell cell_14_9 = row_14.createCell(9);
            cell_14_9.setCellValue(reportExcel_two_M.getHighestTempTime());
            Cell cell_15_9 = row_15.createCell(9);
            cell_15_9.setCellValue(reportExcel_two_M.getReachNFTempTime());
            Cell cell_16_9 = row_16.createCell(9);
            cell_16_9.setCellValue(reportExcel_two_M.getPointOneHighestTemp());
            Cell cell_17_9 = row_17.createCell(9);
            cell_17_9.setCellValue(reportExcel_two_M.getPointTwoHighestTemp());
            Cell cell_18_9 = row_18.createCell(9);
            cell_18_9.setCellValue(reportExcel_two_M.getPointThreeHighestTemp());
            Cell cell_19_9 = row_19.createCell(9);
            cell_19_9.setCellValue(reportExcel_two_M.getPointFourHighestTemp());
            Cell cell_20_9 = row_20.createCell(9);
            cell_20_9.setCellValue(reportExcel_two_M.getPointFiveHighestTemp());
        }

        //2#工位胎侧区域1(L)
        if(reportExcel_two_L != null){
            Cell cell_71_9 = row_71.createCell(9);
            cell_71_9.setCellValue(reportExcel_two_L.getHighestTemp());
            Cell cell_72_9 = row_72.createCell(9);
            cell_72_9.setCellValue(reportExcel_two_L.getHighestTempTime());
            Cell cell_73_9 = row_73.createCell(9);
            cell_73_9.setCellValue(reportExcel_two_L.getReachNFTempTime());
            Cell cell_74_9 = row_74.createCell(9);
            cell_74_9.setCellValue(reportExcel_two_L.getPointOneHighestTemp());
            Cell cell_75_9 = row_75.createCell(9);
            cell_75_9.setCellValue(reportExcel_two_L.getPointTwoHighestTemp());
            Cell cell_76_9 = row_76.createCell(9);
            cell_76_9.setCellValue(reportExcel_two_L.getPointThreeHighestTemp());
            Cell cell_77_9 = row_77.createCell(9);
            cell_77_9.setCellValue(reportExcel_two_L.getPointFourHighestTemp());
            Cell cell_78_9 = row_78.createCell(9);
            cell_78_9.setCellValue(reportExcel_two_L.getPointFiveHighestTemp());
        }



        //2#工位胎侧区域2(R)
        if(reportExcel_two_R != null){
            Cell cell_87_9 = row_87.createCell(9);
            cell_87_9.setCellValue(reportExcel_two_R.getHighestTemp());
            Cell cell_88_9 = row_88.createCell(9);
            cell_88_9.setCellValue(reportExcel_two_R.getHighestTempTime());
            Cell cell_89_9 = row_89.createCell(9);
            cell_89_9.setCellValue(reportExcel_two_R.getReachNFTempTime());
            Cell cell_90_9 = row_90.createCell(9);
            cell_90_9.setCellValue(reportExcel_two_R.getPointOneHighestTemp());
            Cell cell_91_9 = row_91.createCell(9);
            cell_91_9.setCellValue(reportExcel_two_R.getPointTwoHighestTemp());
            Cell cell_92_9 = row_92.createCell(9);
            cell_92_9.setCellValue(reportExcel_two_R.getPointThreeHighestTemp());
            Cell cell_93_9 = row_93.createCell(9);
            cell_93_9.setCellValue(reportExcel_two_R.getPointFourHighestTemp());
            Cell cell_94_9 = row_94.createCell(9);
            cell_94_9.setCellValue(reportExcel_two_R.getPointFiveHighestTemp());
        }



        setRowStyle(hssfCellStyle,sheet,2,6,0,10);
        setRowStyle(hssfCellStyle,sheet,13,21,0,10);
        setRowStyle(hssfCellStyle,sheet,71,79,0,10);
        setRowStyle(hssfCellStyle,sheet,87,95,0,10);


        BufferedImage bufferImg2M = null;
        BufferedImage bufferImg2R = null;
        BufferedImage bufferImg2L = null;
        BufferedImage bufferImg2MQX = null;
        BufferedImage bufferImg2RQX = null;
        BufferedImage bufferImg2LQX = null;

        try {

            //2#胎冠热成像图
            if(reportExcel_two_M != null){
                ByteArrayOutputStream byteArrayOut2M = new ByteArrayOutputStream();
                bufferImg2M = ImageIO.read(new File(reportExcel_two_M.getPlp_photo_position()));     //将图片读到BufferedImage
                ImageIO.write(bufferImg2M, "png", byteArrayOut2M);        // 将图片写入流中
                HSSFPatriarch hssfPatriarch2M = sheet.createDrawingPatriarch();
                HSSFClientAnchor anchor2M = new HSSFClientAnchor(0,0,0,0,(short)5,11,(short)7,24);
                hssfPatriarch2M.createPicture(anchor2M,wb.addPicture(byteArrayOut2M.toByteArray(),HSSFWorkbook.PICTURE_TYPE_JPEG));    //导入图片
            }



            //2#胎冠曲线图
            String picturePath2MQX = "D://"+sampleInfo2.getRoom_number()+"//"+sampleInfo2.getCam_station_number()+"//"+sampleInfo2.getSample_test_date()+"//";
            String pictureFile2MQX = sampleInfo2.getRoom_number()+"-"+sampleInfo2.getCam_station_number()+"-"+sampleInfo2.getSample_test_date()+"-"+sampleInfo2.getTire_number()+".jpeg";
            String wholePicturePath2MQX = picturePath2MQX + pictureFile2MQX;
            ByteArrayOutputStream byteArrayOut2MQX = new ByteArrayOutputStream();
            bufferImg2MQX = ImageIO.read(new File(wholePicturePath2MQX));     //将图片读到BufferedImage
            ImageIO.write(bufferImg2MQX, "png", byteArrayOut2MQX);        // 将图片写入流中
            HSSFPatriarch hssfPatriarch2MQX = sheet.createDrawingPatriarch();
            HSSFClientAnchor anchor2MQX = new HSSFClientAnchor(0,0,0,0,(short)1,47,(short)9,66);
            hssfPatriarch2MQX.createPicture(anchor2MQX,wb.addPicture(byteArrayOut2MQX.toByteArray(),HSSFWorkbook.PICTURE_TYPE_JPEG));    //导入图片


            //2#胎侧-1热成像图
            if(reportExcel_two_L != null){
                ByteArrayOutputStream byteArrayOut2L = new ByteArrayOutputStream();
                bufferImg2L = ImageIO.read(new File(reportExcel_two_L.getPlp_photo_position()));     //将图片读到BufferedImage
                ImageIO.write(bufferImg2L, "png", byteArrayOut2L);        // 将图片写入流中
                HSSFPatriarch hssfPatriarch2L = sheet.createDrawingPatriarch();
                HSSFClientAnchor anchor2L = new HSSFClientAnchor(0,0,0,0,(short)5,69,(short)7,82);
                hssfPatriarch2L.createPicture(anchor2L,wb.addPicture(byteArrayOut2L.toByteArray(),HSSFWorkbook.PICTURE_TYPE_JPEG));    //导入图片
            }


            //2#胎侧-2热成像图
            if(reportExcel_two_R != null){
                ByteArrayOutputStream byteArrayOut2R = new ByteArrayOutputStream();
                bufferImg2R = ImageIO.read(new File(reportExcel_two_R.getPlp_photo_position()));     //将图片读到BufferedImage
                ImageIO.write(bufferImg2R, "png", byteArrayOut2R);        // 将图片写入流中
                HSSFPatriarch hssfPatriarch2R = sheet.createDrawingPatriarch();
                HSSFClientAnchor anchor2R = new HSSFClientAnchor(0,0,0,0,(short)5,85,(short)7,98);
                hssfPatriarch2R.createPicture(anchor2R,wb.addPicture(byteArrayOut2R.toByteArray(),HSSFWorkbook.PICTURE_TYPE_JPEG));    //导入图片
            }


            String wholeFilePath = filePath+fileName;
            File file = new File(wholeFilePath);
            if(!file.getParentFile().exists()){     //若该路径下不存在此文件目录则创建
                boolean result = file.getParentFile().mkdirs();
                if (!result) {
                    System.out.println("创建失败");
                }
            }
            FileOutputStream fos = new FileOutputStream(file);
            wb.write(fos);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("io erorr : " + e.getMessage());
        }
    }

    //得到报表中内容信息
public ReportExcelInfo returnReportExcel(SampleInfo sampleInfo){
    ReportExcelInfo reportExcel = new ReportExcelInfo();
    List<PointTempInfo> point = new ArrayList<PointTempInfo>();
    point = reportExcelService.getReportExcelTemp(sampleInfo);
//    Float surface = reportExcelService.getReportExcelSurf(sampleInfo);
//    reportExcel.setPointOneHighestTemp(String.valueOf(surface));
    for(int i=0;i<point.size();i++){
        switch (point.get(i).getSample_type_number()){
            case 1: reportExcel.setPointOneHighestTemp(String.valueOf(point.get(i).getPlp_fMaxTemperature()));
                break;
            case 2: reportExcel.setPointTwoHighestTemp(String.valueOf(point.get(i).getPlp_fMaxTemperature()));
                break;
            case 3: reportExcel.setPointThreeHighestTemp(String.valueOf(point.get(i).getPlp_fMaxTemperature()));
                break;
            case 4: reportExcel.setPointFourHighestTemp(String.valueOf(point.get(i).getPlp_fMaxTemperature()));
                break;
            case 5: reportExcel.setPointFiveHighestTemp(String.valueOf(point.get(i).getPlp_fMaxTemperature()));
                break;
             default: break;
        }
    }
    StartTimeAndTimeDiff STTD = reportExcelService.getHighestTemp(sampleInfo);
    String plp_timeD = reportExcelService.getNineFiveTime(sampleInfo);
   if(plp_timeD == null){
       plp_timeD = "0";
   }
   long plp_timeDiff = Long.valueOf(plp_timeD);
    SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    long startHighestTempTime,fixStartTime,nineFiveTime;
    String highestTempTime = null, reachNineFiveTime = null;
    Date dHighestTempTime = new Date();
    Date dReachNineFiveTime = new Date();
    try {
        Date dSTTD = sdf.parse(STTD.getPlp_fixStartTime());
        fixStartTime = dSTTD.getTime();
        startHighestTempTime = fixStartTime + (STTD.getPlp_timeDiff())*1000;
        nineFiveTime = fixStartTime + plp_timeDiff*1000;
        dHighestTempTime.setTime(startHighestTempTime);
        dReachNineFiveTime.setTime(nineFiveTime);
        highestTempTime = sdf.format(dHighestTempTime);
        reachNineFiveTime = sdf.format(dReachNineFiveTime);
        if(plp_timeD.equals("0")){
            reachNineFiveTime = "0";
        }

    } catch (ParseException e) {
        System.out.println("日期格式转换失败！");
        e.printStackTrace();
    }
    reportExcel.setHighestTemp(String.valueOf(STTD.getPlp_fMaxTemperature()));
    reportExcel.setHighestTempTime(highestTempTime);
    reportExcel.setReachNFTempTime(reachNineFiveTime);
    reportExcel.setPlp_photo_position(STTD.getPlp_photo_position());
    return reportExcel;
}

//设置单元格
    public void setRowBorder(CellRangeAddress cellRangeAddress, HSSFSheet sheet, HSSFWorkbook wb) throws Exception {
        RegionUtil.setBorderLeft(1, cellRangeAddress, sheet, wb);
        RegionUtil.setBorderBottom(1, cellRangeAddress, sheet, wb);
        RegionUtil.setBorderRight(1, cellRangeAddress, sheet, wb);
        RegionUtil.setBorderTop(1, cellRangeAddress, sheet, wb);
    }

    //设置行居中等样式
    public void setRowStyle(HSSFCellStyle hssfCellStyle,HSSFSheet sheet,int firstRow,int secondRow,int firstCell,int secondCell) {
        for(int i=firstRow;i<secondRow;i++){
            for(int j=firstCell;j<secondCell;j++){
                HSSFRow tempRow = sheet.getRow(i);
                HSSFCell cell_temp = tempRow.getCell(j);
                if(cell_temp==null){
                    cell_temp = tempRow.createCell(j);
                    cell_temp.setCellStyle(hssfCellStyle);
                }else{
                    cell_temp.setCellStyle(hssfCellStyle);
                }
            }
        }
    }

}
