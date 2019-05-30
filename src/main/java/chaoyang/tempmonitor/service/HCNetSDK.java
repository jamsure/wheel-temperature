package chaoyang.tempmonitor.service;

import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.StdCallLibrary;

import java.nio.ByteBuffer;

public interface HCNetSDK extends StdCallLibrary {
    HCNetSDK INSTANCE = (HCNetSDK) Native.loadLibrary("C:\\Projects\\tempmonitor\\lib\\HCNetSDK.dll",
            HCNetSDK.class);

    /**宏定义**/
    //常量
    public static final int SERIALNO_LEN = 48;   //序列号长度
    public static final int MAX_IP_DEVICE = 32;    //允许接入的最大IP设备数
    public static final int MAX_ANALOG_CHANNUM = 32;    //最大32个模拟通道
    public static final int MAX_IP_CHANNEL = 32;   //允许加入的最多IP通道数
    public static final int NAME_LEN = 32;    //用户名长度
    public static final int PASSWD_LEN = 16;    //密码长度
    //IP接入配置参数 （NET_DVR_IPPARACFG结构）
    public static final int NET_DVR_GET_IPPARACFG = 1048;    //获取IP接入配置信息
    public static final int NET_DVR_SET_IPPARACFG = 1049;    //设置IP接入配置信息
    public static final int NET_DVR_GET_REALTIME_THERMOMETRY = 3629; //实时温度检测


    //实时测温回调函数接口
    public static interface FRemoteConfigCallback extends StdCallCallback {
        public void invoke(int dwType, Pointer lpBuffer, int dwBufLen, Pointer pUserData);
    }

    //NET_DVR_Login_V30()参数结构
    public static class NET_DVR_DEVICEINFO_V30 extends Structure
    {
        public  byte[] sSerialNumber = new byte[SERIALNO_LEN];  //序列号
        public  byte byAlarmInPortNum;		        //报警输入个数
        public  byte byAlarmOutPortNum;		        //报警输出个数
        public  byte byDiskNum;				    //硬盘个数
        public  byte byDVRType;				    //设备类型, 1:DVR 2:ATM DVR 3:DVS ......
        public  byte byChanNum;				    //模拟通道个数
        public  byte byStartChan;			        //起始通道号,例如DVS-1,DVR - 1
        public  byte byAudioChanNum;                //语音通道数
        public  byte byIPChanNum;					//最大数字通道个数
        public  byte[] byRes1 = new byte[24];					//保留
    }

    public static class NET_DVR_IPPARACFG extends Structure {/* IP接入配置结构 */
        public  int dwSize;			                            /* 结构大小 */
        public  NET_DVR_IPDEVINFO[]  struIPDevInfo = new NET_DVR_IPDEVINFO[MAX_IP_DEVICE];    /* IP设备 */
        public   byte[] byAnalogChanEnable = new byte[MAX_ANALOG_CHANNUM];        /* 模拟通道是否启用，从低到高表示1-32通道，0表示无效 1有效 */
        public NET_DVR_IPCHANINFO[] struIPChanInfo = new NET_DVR_IPCHANINFO[MAX_IP_CHANNEL];	/* IP通道 */
    }

    //IPC接入参数配置
    public static class NET_DVR_IPDEVINFO extends Structure {/* IP设备结构 */
        public   int dwEnable;				    /* 该IP设备是否启用 */
        public   byte[] sUserName = new byte[NAME_LEN];		/* 用户名 */
        public   byte[] sPassword = new byte[PASSWD_LEN];	    /* 密码 */
        public   NET_DVR_IPADDR struIP = new NET_DVR_IPADDR();			/* IP地址 */
        public   short wDVRPort;			 	    /* 端口号 */
        public   byte[] byres = new byte[34];				/* 保留 */
    }

    public static class NET_DVR_IPADDR extends Structure {
        public byte[] sIpV4 = new byte[16];
        public byte[] byRes = new byte[128];

        public String toString() {
            return "NET_DVR_IPADDR.sIpV4: " + new String(sIpV4) + "\n" + "NET_DVR_IPADDR.byRes: " + new String(byRes) + "\n";
        }
    }

    //配置输入输出参数结构体
    public static class NET_DVR_IPCHANINFO extends Structure {/* IP通道匹配参数 */
        public byte lpCondBuffer;
        public   byte byEnable;					/* 该通道是否启用 */
        public  byte byIPID;					/* IP设备ID 取值1- MAX_IP_DEVICE */
        public  byte byChannel;					/* 通道号 */
        public   byte[] byres = new byte[33];					/* 保留 */
    }

    public static class NET_DVR_REALTIME_THERMOMETRY_COND extends Structure
    {
        public int dwSize;
        public int dwChan;
        public byte byRuleID;
        public byte byMode;
        public byte[] byRes = new byte[62];
    }


    public enum NET_SDK_CALLBACK_TYPE {
        NET_SDK_CALLBACK_TYPE_STATUS(0),
        NET_SDK_CALLBACK_TYPE_PROGRESS(1),
        NET_SDK_CALLBACK_TYPE_DATA(2);
        private final int value;
        NET_SDK_CALLBACK_TYPE(int value){
            this.value = value;
        }
        public int getValue(){
            return value;
        }
    }

    public enum NET_SDK_CALLBACK_STATUS {
        NET_SDK_CALLBACK_STATUS_SUCCESS (1000),
        NET_SDK_CALLBACK_STATUS_PROCESSING(1001),
        NET_SDK_CALLBACK_STATUS_FAILED(1002);
        private final int value;
        NET_SDK_CALLBACK_STATUS(int value){
            this.value = value;
        }
        public int getValue(){
            return value;
        }
    }

    //校时结构参数
    public static class NET_DVR_TIME extends Structure {//校时结构参数
        public int dwYear;		//年
        public int dwMonth;		//月
        public int dwDay;		//日
        public int dwHour;		//时
        public int dwMinute;		//分
        public int dwSecond;		//秒

        public String toString() {
            return "NET_DVR_TIME.dwYear: " + dwYear + "\n" + "NET_DVR_TIME.dwMonth: \n" + dwMonth + "\n" + "NET_DVR_TIME.dwDay: \n" + dwDay + "\n" + "NET_DVR_TIME.dwHour: \n" + dwHour + "\n" + "NET_DVR_TIME.dwMinute: \n" + dwMinute + "\n" + "NET_DVR_TIME.dwSecond: \n" + dwSecond;
        }

        //用于列表中显示
        public String toStringTime()
        {
            return  String.format("%02d/%02d/%02d%02d:%02d:%02d", dwYear, dwMonth, dwDay, dwHour, dwMinute, dwSecond);
        }

        //存储文件名使用
        public String toStringTitle()
        {
            return  String.format("Time%02d%02d%02d%02d%02d%02d", dwYear, dwMonth, dwDay, dwHour, dwMinute, dwSecond);
        }
    }

    public static class NET_DVR_THERMOMETRY_UPLOAD extends Structure
    {
        // public static class ByReference extends NET_DVR_TEMP_HUMI_INFO implements Structure.ByReference{}
        public int dwSize;
        public int dwRelativeTime;
        public int dwAbsTime;
        public byte[] szRuleName = new byte[32];
        public byte byRuleID;
        public byte byRuleCalibType;
        public short wPresetNo;
        public NET_DVR_POINT_THERM_CFG struPointThermCfg;
        public NET_DVR_LINEPOLYGON_THERM_CFG struLinePolygonThermCfg;
        public byte byThermometryUnit;
        public byte byDataType;
        public byte byRes1;
        public byte bySpecialPointThermType;
        public byte fCenterPointTemperature;
        public byte fHighestPointTemperature;
        public byte fLowestPointTemperature;
        public byte[] byRes = new byte[112];
    }

    public static class NET_DVR_POINT_THERM_CFG extends Structure
    {
        // public static class ByReference extends NET_DVR_TEMP_HUMI_INFO implements Structure.ByReference{}
        public float fTemperature;
        public NET_VCA_POINT struPoint;
        public byte[] byRes = new byte[120];
    }

    //JPEG图像信息结构体。
    public static class NET_DVR_JPEGPARA extends Structure
    {
        public short wPicSize;
        public short wPicQuality;
    }

    public static class NET_VCA_POINT extends Structure
    {
        // public static class ByReference extends NET_DVR_TEMP_HUMI_INFO implements Structure.ByReference{}
        public float fx;
        public float fy;
    }

    public static class NET_DVR_LINEPOLYGON_THERM_CFG extends Structure
    {
        // public static class ByReference extends NET_DVR_TEMP_HUMI_INFO implements Structure.ByReference{}
        public float fMaxTemperature;
        public float fMinTemperature;
        public float fAverageTemperature;
        public float fTemperatureDiff;
        public NET_VCA_POLYGON struRegion;
        public byte[] byRes = new byte[32];
    }

    public static class NET_VCA_POLYGON extends Structure
    {
        // public static class ByReference extends NET_DVR_TEMP_HUMI_INFO implements Structure.ByReference{}
        public int dwPointNum;
        public NET_VCA_POINT[] struPos = new NET_VCA_POINT[10];
    }

    NativeLong NET_DVR_Login_V30(String sDVRIP, short wDVRPort, String sUserName, String sPassword, NET_DVR_DEVICEINFO_V30 lpDeviceInfo);
    boolean  NET_DVR_Logout_V30(NativeLong lUserID);
    boolean  NET_DVR_GetDVRConfig(NativeLong lUserID, int dwCommand, NativeLong lChannel, Pointer lpOutBuffer, int dwOutBufferSize, IntByReference lpBytesReturned);
    boolean  NET_DVR_Init();
    boolean  NET_DVR_SetConnectTime(int dwWaitTime, int dwTryTimes );
    boolean  NET_DVR_SetReconnect(int dwInterval, boolean bEnableRecon );

    //定时抓图
    boolean  NET_DVR_CaptureJPEGPicture_NEW(NativeLong lUserID, NativeLong lChannel, NET_DVR_JPEGPARA lpJpegPara, ByteBuffer sJpegPicBuffer, int dwPicSize, IntByReference lpSizeReturned);
    //实时测温
    NativeLong  NET_DVR_StartRemoteConfig(NativeLong lUserID, int dwCommand, Pointer lpInBuffer, int dwInBufferLen, FRemoteConfigCallback cbStateCallback,  Pointer pUserData);
    //获取错误信息
    int  NET_DVR_GetLastError();
    boolean  NET_DVR_StopRemoteConfig(NativeLong lHandle);
    //释放SDK资源
    boolean NET_DVR_Cleanup();

    boolean  NET_DVR_Logout(NativeLong lUserID);
}

