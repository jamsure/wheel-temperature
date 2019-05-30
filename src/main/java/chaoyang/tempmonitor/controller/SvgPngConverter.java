package chaoyang.tempmonitor.controller;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;

import java.io.*;

/**
 *@Description: 将svg转换为png格式的图片
 */
public class SvgPngConverter {

    /**
     *@Description: 将svg字符串转换为png
     *@Author:
     *@param svgCode svg代码
     *@param pngFilePath  保存的路径
     *@throws IOException io异常
     *@throws TranscoderException svg代码异常
     */
    public static void convertToPng(String svgCode,String pngFilePath) throws IOException,TranscoderException{

        File file = new File (pngFilePath);

        FileOutputStream outputStream = null;
        try {
            if(!file.getParentFile().exists()){     //若该路径下不存在此文件目录则创建
                boolean result = file.getParentFile().mkdirs();
                if (!result) {
                    System.out.println("创建失败");
                }
            }
            file.createNewFile ();
            outputStream = new FileOutputStream (file);
            convertToPng (svgCode, outputStream);

        }

        finally {
            if (outputStream != null) {
                try {
                    outputStream.close ();
                } catch (IOException e) {
                    e.printStackTrace ();
                }
            }
        }
    }

    /**
     *@Description: 将svgCode转换成png文件，直接输出到流中
     *@param svgCode svg代码
     *@param outputStream 输出流
     *@throws TranscoderException 异常
     *@throws IOException io异常
     */
    public static void convertToPng(String svgCode,OutputStream outputStream) throws TranscoderException,IOException{
        try {
            byte[] bytes = svgCode.getBytes ("UTF-8");
//            PNGTranscoder t = new PNGTranscoder ();
            JPEGTranscoder t = new JPEGTranscoder();
            TranscoderInput input = new TranscoderInput (new ByteArrayInputStream (bytes));
            TranscoderOutput output = new TranscoderOutput (outputStream);
            t.transcode (input, output);
            outputStream.flush ();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close ();
                } catch (IOException e) {
                    e.printStackTrace ();
                }
            }
        }
    }
}

