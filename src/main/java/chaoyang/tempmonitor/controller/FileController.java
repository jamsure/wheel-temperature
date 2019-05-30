package chaoyang.tempmonitor.controller;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

//import com.fasterxml.jackson.core.type.TypeReference;
@RestController
@ComponentScan({"chaoyang.tempmonitor.service"})
@MapperScan("chaoyang.tempmonitor.mapper")
public class FileController {
//文件下载
@RequestMapping(value="/downloadFile",method = {RequestMethod.POST})
@ResponseBody
    public String finalDownloadFile(HttpServletRequest request,HttpServletResponse response){
//        String fileName =sampleInfo.getSample_specification()+"_"+sampleInfo.getSample_level()+"_"+sampleInfo.getSample_pattern()+"_"+sampleInfo.getSample_test_date()+"_"+sampleInfo.getSample_number()+"_"+sampleInfo.getSample_person()+"_"+sampleInfo.getTire_number()+".xls";
//        String realPath = "D://"+sampleInfo.getRoom_number()+"//"+sampleInfo.getCam_station_number()+"//"+sampleInfo.getSample_test_date()+"//";
//    String fileName = "dalaoyang.jpg";// 文件名
//    String realPath = "A://tempfile//spring_boot_learn//springboot_learn//springboot_upload_download//down";

        String fileName = request.getParameter("sample_spec")+"_"+request.getParameter("sample_lev")+"_"+request.getParameter("sample_patt")+"_"+request.getParameter("sample_tes")+"_"+request.getParameter("sample_num")+"_"+request.getParameter("sample_per")+"_"+request.getParameter("tire_num")+".xls";// 文件名
       System.out.println("wode"+fileName);
        String realPath = "D://"+request.getParameter("room_num")+"//"+request.getParameter("cam_station_num")+"//"+request.getParameter("sample_tes");
        System.out.println("wode"+realPath);
        File file = new File(realPath,fileName);
        if (fileName != null) {
            //设置文件路径

            //File file = new File(realPath , fileName);
            if (file.exists()) {
                response.setContentType("application/force-download");// 设置强制下载不打开
                response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);// 设置文件名
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer, 0, i);
                        i = bis.read(buffer);
                    }
                    return "下载成功";
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return "下载失败";

    }
}
