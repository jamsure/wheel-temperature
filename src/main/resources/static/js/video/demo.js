
// 初始化插件

// 全局保存当前选中窗口

var g_iWndIndex = 0; //可以不用设置这个变量，有窗口参数的接口中，不用传值，开发包会默认使用当前选择窗口
var interval;

$(function () {
    // 检查插件是否已经安装过
    var iRet = WebVideoCtrl.I_CheckPluginInstall();
    if (-1 == iRet) {
        alert("您还未安装过插件，双击开发包目录里的WebComponentsKit.exe安装！");
        return;
    }

    // 初始化插件参数及插入插件
    WebVideoCtrl.I_InitPlugin(500, 470, {
        bWndFull: true,     //是否支持单窗口双击全屏，默认支持 true:支持 false:不支持
        iPackageType: 2,    //2:PS 11:MP4
        iWndowType: 3,
        bNoPlugin: true,
        cbSelWnd: function (xmlDoc) {
            g_iWndIndex = parseInt($(xmlDoc).find("SelectWnd").eq(0).text(), 10);
            var szInfo = "当前选择的窗口编号：" + g_iWndIndex;
            showCBInfo(szInfo);
        },
        cbDoubleClickWnd: function (iWndIndex, bFullScreen) {
            var szInfo = "当前放大的窗口编号：" + iWndIndex;
            if (!bFullScreen) {            
                szInfo = "当前还原的窗口编号：" + iWndIndex;
            }
            showCBInfo(szInfo);
                        
            // 此处可以处理单窗口的码流切换
            // if (bFullScreen) {
            //     clickStartRealPlay1(1);
            // }
            // else if (bFullScreen) {
            //     clickStartRealPlay2(2);
            // }
        },
        cbEvent: function (iEventType, iParam1, iParam2) {
            if (2 == iEventType) {// 回放正常结束
                showCBInfo("窗口" + iParam1 + "回放结束！");
            } else if (-1 == iEventType) {
                showCBInfo("设备" + iParam1 + "网络错误！");
            } else if (3001 == iEventType) {
                clickStopRecord(g_szRecordType, iParam1);
            }
        },
        cbRemoteConfig: function () {
            showCBInfo("关闭远程配置库！");
        },
        cbInitPluginComplete: function () {
            WebVideoCtrl.I_InsertOBJECTPlugin("divPlugin");

            // 检查插件是否最新
            if (-1 == WebVideoCtrl.I_CheckPluginVersion()) {
                alert("检测到新的插件版本，双击开发包目录里的WebComponentsKit.exe升级！");
                return;
            }
        }
    });

    // 窗口事件绑定
    $(window).bind({
        resize: function () {
            var $Restart = $("#restartDiv");
            if ($Restart.length > 0) {
                var oSize = getWindowSize();
                $Restart.css({
                    width: oSize.width + "px",
                    height: oSize.height + "px"
                });
            }
        }
    });

    //初始化日期时间
    var szCurTime = dateFormat(new Date(), "yyyy-MM-dd");
    $("#starttime").val(szCurTime + " 00:00:00");
    $("#endtime").val(szCurTime + " 23:59:59");
});

// 显示操作信息
function showOPInfo(szInfo, status, xmlDoc) {
    var szTip = "<div>" + dateFormat(new Date(), "yyyy-MM-dd hh:mm:ss") + " " + szInfo;
    if (typeof status != "undefined" && status != 200) {
        var szStatusString = $(xmlDoc).find("statusString").eq(0).text();
        var szSubStatusCode = $(xmlDoc).find("subStatusCode").eq(0).text();
        if ("" === szSubStatusCode) {
            szTip += "(" + status + ", " + szStatusString + ")";
        } else {
            szTip += "(" + status + ", " + szSubStatusCode + ")";
        }
    }
    szTip += "</div>";

    $("#opinfo").html(szTip + $("#opinfo").html());
}

// 显示回调信息
function showCBInfo(szInfo) {
    szInfo = "<div>" + dateFormat(new Date(), "yyyy-MM-dd hh:mm:ss") + " " + szInfo + "</div>";
    $("#cbinfo").html(szInfo + $("#cbinfo").html());
}

// 格式化时间
function dateFormat(oDate, fmt) {
    var o = {
        "M+": oDate.getMonth() + 1, //月份
        "d+": oDate.getDate(), //日
        "h+": oDate.getHours(), //小时
        "m+": oDate.getMinutes(), //分
        "s+": oDate.getSeconds(), //秒
        "q+": Math.floor((oDate.getMonth() + 3) / 3), //季度
        "S": oDate.getMilliseconds()//毫秒
    };
    if (/(y+)/.test(fmt)) {
        fmt = fmt.replace(RegExp.$1, (oDate.getFullYear() + "").substr(4 - RegExp.$1.length));
    }
    for (var k in o) {
        if (new RegExp("(" + k + ")").test(fmt)) {
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        }
    }
    return fmt;
}

// 获取窗口尺寸
function getWindowSize() {
    var nWidth = $(this).width() + $(this).scrollLeft(),
        nHeight = $(this).height() + $(this).scrollTop();

    return {width: nWidth, height: nHeight};
}

// 打开选择框 0：文件夹  1：文件
function clickOpenFileDlg(id, iType) {
    var szDirPath = WebVideoCtrl.I_OpenFileDlg(iType);
    
    if (szDirPath != -1 && szDirPath != "" && szDirPath != null) {
        $("#" + id).val(szDirPath);
    }
}

// 获取本地参数
function clickGetLocalCfg() {
    var xmlDoc = WebVideoCtrl.I_GetLocalCfg();

    if (xmlDoc != null) {
        $("#netsPreach").val($(xmlDoc).find("BuffNumberType").eq(0).text());
        $("#wndSize").val($(xmlDoc).find("PlayWndType").eq(0).text());
        $("#rulesInfo").val($(xmlDoc).find("IVSMode").eq(0).text());
        $("#captureFileFormat").val($(xmlDoc).find("CaptureFileFormat").eq(0).text());
        $("#packSize").val($(xmlDoc).find("PackgeSize").eq(0).text());
        $("#recordPath").val($(xmlDoc).find("RecordPath").eq(0).text());
        $("#downloadPath").val($(xmlDoc).find("DownloadPath").eq(0).text());
        $("#previewPicPath").val($(xmlDoc).find("CapturePath").eq(0).text());
        $("#playbackPicPath").val($(xmlDoc).find("PlaybackPicPath").eq(0).text());
        $("#devicePicPath").val($(xmlDoc).find("DeviceCapturePath").eq(0).text());
        $("#playbackFilePath").val($(xmlDoc).find("PlaybackFilePath").eq(0).text());
        $("#protocolType").val($(xmlDoc).find("ProtocolType").eq(0).text());

        showOPInfo("本地配置获取成功！");
    } else {
        showOPInfo("本地配置获取失败！");
    }
}

// 设置本地参数
function clickSetLocalCfg() {
    var arrXml = [],
        szInfo = "";
    
    arrXml.push("<LocalConfigInfo>");
    arrXml.push("<PackgeSize>" + $("#packSize").val() + "</PackgeSize>");
    arrXml.push("<PlayWndType>" + $("#wndSize").val() + "</PlayWndType>");
    arrXml.push("<BuffNumberType>" + $("#netsPreach").val() + "</BuffNumberType>");
    arrXml.push("<RecordPath>" + $("#recordPath").val() + "</RecordPath>");
    arrXml.push("<CapturePath>" + $("#previewPicPath").val() + "</CapturePath>");
    arrXml.push("<PlaybackFilePath>" + $("#playbackFilePath").val() + "</PlaybackFilePath>");
    arrXml.push("<PlaybackPicPath>" + $("#playbackPicPath").val() + "</PlaybackPicPath>");
    arrXml.push("<DeviceCapturePath>" + $("#devicePicPath").val() + "</DeviceCapturePath>");
    arrXml.push("<DownloadPath>" + $("#downloadPath").val() + "</DownloadPath>");
    arrXml.push("<IVSMode>" + $("#rulesInfo").val() + "</IVSMode>");
    arrXml.push("<CaptureFileFormat>" + $("#captureFileFormat").val() + "</CaptureFileFormat>");
    arrXml.push("<ProtocolType>" + $("#protocolType").val() + "</ProtocolType>");
    arrXml.push("</LocalConfigInfo>");

    var iRet = WebVideoCtrl.I_SetLocalCfg(arrXml.join(""));

    if (0 == iRet) {
        szInfo = "本地配置设置成功！";
    } else {
        szInfo = "本地配置设置失败！";
    }
    showOPInfo(szInfo);
}

// 窗口分割数
function changeWndNum(iType) {
    iType = parseInt(iType, 10);
    WebVideoCtrl.I_ChangeWndNum(iType);
}


////连接数据库，获取当前房间所有摄像头信息
var dataCamInfo;
function showCamInfo(room_number){
    $.ajax(
        {
            url:'/findRoom',
            type:"post",
            data:{
                "room_number":room_number
   //             "cam_station_number":cam_station_number
            },
            // contentType : 'application/json; charset=utf-8',
            dataType:'json',
            cache:false,
            async:false,
            success:function (data) {
                 dataCamInfo = data;
            },
            error:function (msg) {
                alert(msg);
            }
        });
}


//登录当前房间所有摄像头
function clickToLogin(){
    var camIp,camPort,camUsername,camPassword;
    for (var i=0;dataCamInfo.length;i++)
    {
         camIp = dataCamInfo[i].cam_ip;
         camPort = dataCamInfo[i].cam_port;
         camUsername = dataCamInfo[i].cam_user;
         camPassword = dataCamInfo[i].cam_pwd;
        clickLogin(camIp,camPort,camUsername,camPassword);
    }
}


// 登录
function clickLogin(camIp,camPort,camUsername,camPassword) {
    var szIP = camIp,
        szPort = camPort,
        szUsername = camUsername,
        szPassword = camPassword;

    if ("" == szIP || "" == szPort) {
        return;
    }

    var szDeviceIdentify = szIP + "_" + szPort;

    var iRet = WebVideoCtrl.I_Login(szIP, 1, szPort, szUsername, szPassword, {
        success: function (xmlDoc) {
            showOPInfo(szDeviceIdentify + " 登录成功！");

            $("#ip").prepend("<option value='" + szDeviceIdentify + "'>" + szDeviceIdentify + "</option>");
            setTimeout(function () {
                $("#ip").val(szDeviceIdentify);
                getChannelInfo();
                getDevicePort();
            }, 10);
            clickToStartRealPlay();
        },
        error: function (status, xmlDoc) {
            showOPInfo(szDeviceIdentify + " 登录失败！", status, xmlDoc);
        }
    });

    if (-1 == iRet) {
        showOPInfo(szDeviceIdentify + " 已登录过！");
    }


}



// 退出
function clickLogout(camIp) {
    var szDeviceIdentify = camIp,
        szInfo = "";

    if (null == szDeviceIdentify) {
        return;
    }

    var iRet = WebVideoCtrl.I_Logout(szDeviceIdentify);
    if (0 == iRet) {
        szInfo = "退出成功！";
       // $("#ip option[value='" + szDeviceIdentify + "']").remove();
       //  getChannelInfo();
       //  getDevicePort();
        clickToStopRealPlay();
    } else {
        szInfo = "退出失败！";
    }
    showOPInfo(szDeviceIdentify + " " + szInfo);
}

//所有摄像头用户退出
function clickToLogout() {
    if (dataCamInfo != null) {
    for (var i=0;i<dataCamInfo.length;i++)
    {
        var camIp = dataCamInfo[i].cam_ip;
        // alert(camIp);
        if(camIp!=null)
        {
            clickLogout(camIp);
        }
    }
}
}



// 开始预览
function clickStartRealPlay(iStreamType,iWndIndex,camIp,camPort) {
    var oWndInfo = WebVideoCtrl.I_GetWindowStatus(iWndIndex),
        //szDeviceIdentify = "10.12.41.21",
        szDeviceIdentify = camIp,
        iRtspPort = camPort,
        iChannelID = "1",
        bZeroChannel = false,
        szInfo = "";
    if ("undefined" === typeof iStreamType) {
        iStreamType = parseInt(1, 10);
    }

    if (null == szDeviceIdentify) {
        return;
    }

    var startRealPlay = function () {
        WebVideoCtrl.I_StartRealPlay(szDeviceIdentify, {
            iRtspPort: iRtspPort,
            iStreamType: iStreamType,
            iChannelID: iChannelID,
            iWndIndex: iWndIndex,  //视频窗口
            bZeroChannel: bZeroChannel,
            success: function () {
                szInfo = "开始预览成功！";
                showOPInfo(szDeviceIdentify + " " + szInfo);
            },
            error: function (status, xmlDoc) {
                if (403 === status) {
                    szInfo = "设备不支持Websocket取流！";
                } else {
                    szInfo = "开始预览失败！";
                }
                showOPInfo(szDeviceIdentify + " " + szInfo);
            }
        });
    };

    if (oWndInfo != null) {// 已经在播放了，先停止
        WebVideoCtrl.I_Stop({
            iWndIndex:iWndIndex,
            success: function () {
                startRealPlay();
            }
        });
    } else {
        startRealPlay();
    }
}

//开始预览所有窗口的摄像头
function clickToStartRealPlay(){
    var camIp,camPort;
        for (var i=0;i<dataCamInfo.length;i++)
        {
            camIp = dataCamInfo[i].cam_ip;
            camPort = dataCamInfo[i].cam_port;
                clickStartRealPlay(1,i, camIp, camPort);
        }
}


// 停止预览
function clickStopRealPlay(iWndIndex) {
    var oWndInfo = WebVideoCtrl.I_GetWindowStatus(iWndIndex),
        szInfo = "";

    if (oWndInfo != null) {
        WebVideoCtrl.I_Stop({
            iWndIndex: iWndIndex,
            success: function () {
                szInfo = "停止预览成功！";
                showOPInfo(oWndInfo.szDeviceIdentify + " " + szInfo);
                // clickToLogout();
            },
            error: function () {
                szInfo = "停止预览失败！";
                showOPInfo(oWndInfo.szDeviceIdentify + " " + szInfo);
            }
        });
    }
}

function clickToStopRealPlay(){
    clickStopRealPlay(0);  //一号窗口停止预览
    clickStopRealPlay(1);  //二号窗口停止预览
    clickStopRealPlay(2);
    clickStopRealPlay(3);
    clickStopRealPlay(4);
    clickStopRealPlay(5);
    clickStopRealPlay(6);
    clickStopRealPlay(7);
    clickStopRealPlay(8);
}


// 获取设备信息
function clickGetDeviceInfo() {
    var szDeviceIdentify = $("#ip").val();

    if (null == szDeviceIdentify) {
        return;
    }

    WebVideoCtrl.I_GetDeviceInfo(szDeviceIdentify, {
        success: function (xmlDoc) {
            var arrStr = [];
            arrStr.push("设备名称：" + $(xmlDoc).find("deviceName").eq(0).text() + "\r\n");
            arrStr.push("设备ID：" + $(xmlDoc).find("deviceID").eq(0).text() + "\r\n");
            arrStr.push("型号：" + $(xmlDoc).find("model").eq(0).text() + "\r\n");
            arrStr.push("设备序列号：" + $(xmlDoc).find("serialNumber").eq(0).text() + "\r\n");
            arrStr.push("MAC地址：" + $(xmlDoc).find("macAddress").eq(0).text() + "\r\n");
            arrStr.push("主控版本：" + $(xmlDoc).find("firmwareVersion").eq(0).text() + " " + $(xmlDoc).find("firmwareReleasedDate").eq(0).text() + "\r\n");
            arrStr.push("编码版本：" + $(xmlDoc).find("encoderVersion").eq(0).text() + " " + $(xmlDoc).find("encoderReleasedDate").eq(0).text() + "\r\n");
            
            showOPInfo(szDeviceIdentify + " 获取设备信息成功！");
            alert(arrStr.join(""));
        },
        error: function (status, xmlDoc) {
            showOPInfo(szDeviceIdentify + " 获取设备信息失败！", status, xmlDoc);
        }
    });
}

// 获取通道
function getChannelInfo() {
   // var szDeviceIdentify = "10.12.41.21",
    var szDeviceIdentify =  $("#ip").val(),
        oSel = $("#channels").empty();

    if (null == szDeviceIdentify) {
        return;
    }

    // 模拟通道
    WebVideoCtrl.I_GetAnalogChannelInfo(szDeviceIdentify, {
        async: false,
        success: function (xmlDoc) {
            var oChannels = $(xmlDoc).find("VideoInputChannel");

            $.each(oChannels, function (i) {
                var id = $(this).find("id").eq(0).text(),
                    name = $(this).find("name").eq(0).text();
                if ("" == name) {
                    name = "Camera " + (i < 9 ? "0" + (i + 1) : (i + 1));
                }
                oSel.append("<option value='" + id + "' bZero='false'>" + name + "</option>");
            });
            showOPInfo(szDeviceIdentify + " 获取模拟通道成功！");
        },
        error: function (status, xmlDoc) {
            showOPInfo(szDeviceIdentify + " 获取模拟通道失败！", status, xmlDoc);
        }
    });
    // 数字通道
    WebVideoCtrl.I_GetDigitalChannelInfo(szDeviceIdentify, {
        async: false,
        success: function (xmlDoc) {
            var oChannels = $(xmlDoc).find("InputProxyChannelStatus");

            $.each(oChannels, function (i) {
                var id = $(this).find("id").eq(0).text(),
                    name = $(this).find("name").eq(0).text(),
                    online = $(this).find("online").eq(0).text();
                if ("false" == online) {// 过滤禁用的数字通道
                    return true;
                }
                if ("" == name) {
                    name = "IPCamera " + (i < 9 ? "0" + (i + 1) : (i + 1));
                }
                oSel.append("<option value='" + id + "' bZero='false'>" + name + "</option>");
            });
            showOPInfo(szDeviceIdentify + " 获取数字通道成功！");
        },
        error: function (status, xmlDoc) {
            showOPInfo(szDeviceIdentify + " 获取数字通道失败！", status, xmlDoc);
        }
    });
    // 零通道
    WebVideoCtrl.I_GetZeroChannelInfo(szDeviceIdentify, {
        async: false,
        success: function (xmlDoc) {
            var oChannels = $(xmlDoc).find("ZeroVideoChannel");
            
            $.each(oChannels, function (i) {
                var id = $(this).find("id").eq(0).text(),
                    name = $(this).find("name").eq(0).text();
                if ("" == name) {
                    name = "Zero Channel " + (i < 9 ? "0" + (i + 1) : (i + 1));
                }
                if ("true" == $(this).find("enabled").eq(0).text()) {// 过滤禁用的零通道
                    oSel.append("<option value='" + id + "' bZero='true'>" + name + "</option>");
                }
            });
            showOPInfo(szDeviceIdentify + " 获取零通道成功！");
        },
        error: function (status, xmlDoc) {
            showOPInfo(szDeviceIdentify + " 获取零通道失败！", status, xmlDoc);
        }
    });
}

// 获取端口
function getDevicePort() {
    var szDeviceIdentify = $("#ip").val();

    if (null == szDeviceIdentify) {
        return;
    }

    var oPort = WebVideoCtrl.I_GetDevicePort(szDeviceIdentify);
    if (oPort != null) {
        $("#deviceport").val(oPort.iDevicePort);
        $("#rtspport").val(oPort.iRtspPort);

        showOPInfo(szDeviceIdentify + " 获取端口成功！");
    } else {
        showOPInfo(szDeviceIdentify + " 获取端口失败！");
    }
}

// 获取数字通道
function clickGetDigitalChannelInfo() {
    var szDeviceIdentify = $("#ip").val(),
        iAnalogChannelNum = 0;

    $("#digitalchannellist").empty();

    if (null == szDeviceIdentify) {
        return;
    }

    // 模拟通道
    WebVideoCtrl.I_GetAnalogChannelInfo(szDeviceIdentify, {
        async: false,
        success: function (xmlDoc) {
            iAnalogChannelNum = $(xmlDoc).find("VideoInputChannel").length;
        },
        error: function () {
            
        }
    });

    // 数字通道
    WebVideoCtrl.I_GetDigitalChannelInfo(szDeviceIdentify, {
        async: false,
        success: function (xmlDoc) {
            var oChannels = $(xmlDoc).find("InputProxyChannelStatus");
            
            $.each(oChannels, function () {
                var id = parseInt($(this).find("id").eq(0).text(), 10),
                    ipAddress = $(this).find("ipAddress").eq(0).text(),
                    srcInputPort = $(this).find("srcInputPort").eq(0).text(),
                    managePortNo = $(this).find("managePortNo").eq(0).text(),
                    online = $(this).find("online").eq(0).text(),
                    proxyProtocol = $(this).find("proxyProtocol").eq(0).text();
                            
                var objTr = $("#digitalchannellist").get(0).insertRow(-1);
                var objTd = objTr.insertCell(0);
                objTd.innerHTML = (id - iAnalogChannelNum) < 10 ? "D0" + (id - iAnalogChannelNum) : "D" + (id - iAnalogChannelNum);
                objTd = objTr.insertCell(1);
                objTd.width = "25%";
                objTd.innerHTML = ipAddress;
                objTd = objTr.insertCell(2);
                objTd.width = "15%";
                objTd.innerHTML = srcInputPort;
                objTd = objTr.insertCell(3);
                objTd.width = "20%";
                objTd.innerHTML = managePortNo;
                objTd = objTr.insertCell(4);
                objTd.width = "15%";
                objTd.innerHTML = "true" == online ? "在线" : "离线";
                objTd = objTr.insertCell(5);
                objTd.width = "25%";
                objTd.innerHTML = proxyProtocol;
            });
            showOPInfo(szDeviceIdentify + " 获取数字通道成功！");
        },
        error: function (status, xmlDoc) {
            showOPInfo(szDeviceIdentify + " 没有数字通道！", status, xmlDoc);
        }
    });
}


// 打开声音
function clickOpenSound() {
    var oWndInfo = WebVideoCtrl.I_GetWindowStatus(g_iWndIndex),
        szInfo = "";

    if (oWndInfo != null) {
        var allWndInfo = WebVideoCtrl.I_GetWindowStatus();
        // 循环遍历所有窗口，如果有窗口打开了声音，先关闭
        for (var i = 0, iLen = allWndInfo.length; i < iLen; i++) {
            oWndInfo = allWndInfo[i];
            if (oWndInfo.bSound) {
                WebVideoCtrl.I_CloseSound(oWndInfo.iIndex);
                break;
            }
        }

        var iRet = WebVideoCtrl.I_OpenSound();

        if (0 == iRet) {
            szInfo = "打开声音成功！";
        } else {
            szInfo = "打开声音失败！";
        }
        showOPInfo(oWndInfo.szDeviceIdentify + " " + szInfo);
    }
}

// 关闭声音
function clickCloseSound() {
    var oWndInfo = WebVideoCtrl.I_GetWindowStatus(g_iWndIndex),
        szInfo = "";

    if (oWndInfo != null) {
        var iRet = WebVideoCtrl.I_CloseSound();
        if (0 == iRet) {
            szInfo = "关闭声音成功！";
        } else {
            szInfo = "关闭声音失败！";
        }
        showOPInfo(oWndInfo.szDeviceIdentify + " " + szInfo);
    }
}

// 设置音量
function clickSetVolume() {
    var oWndInfo = WebVideoCtrl.I_GetWindowStatus(g_iWndIndex),
        iVolume = parseInt($("#volume").val(), 10),
        szInfo = "";

    if (oWndInfo != null) {
        var iRet = WebVideoCtrl.I_SetVolume(iVolume);
        if (0 == iRet) {
            szInfo = "音量设置成功！";
        } else {
            szInfo = "音量设置失败！";
        }
        showOPInfo(oWndInfo.szDeviceIdentify + " " + szInfo);
    }
}

// 抓图
function clickCapturePic() {
    var oWndInfo = WebVideoCtrl.I_GetWindowStatus(g_iWndIndex),
        szInfo = "";

    if (oWndInfo != null) {
        var xmlDoc = WebVideoCtrl.I_GetLocalCfg();
        var szCaptureFileFormat = "0";
        if (xmlDoc != null) {
            szCaptureFileFormat = $(xmlDoc).find("CaptureFileFormat").eq(0).text();
        }

        var szChannelID = $("#channels").val();
        var szPicName = oWndInfo.szDeviceIdentify + "_" + szChannelID + "_" + new Date().getTime();
        
        szPicName += ("0" === szCaptureFileFormat) ? ".jpg": ".bmp";

        var iRet = WebVideoCtrl.I_CapturePic(szPicName, {
            bDateDir: true  //是否生成日期文件
        });
        if (0 == iRet) {
            szInfo = "抓图成功！";
        } else {
            szInfo = "抓图失败！";
        }
        showOPInfo(oWndInfo.szDeviceIdentify + " " + szInfo);
    }
}

// 开始录像
var g_szRecordType = "";
function clickStartRecord(szType) {
    var oWndInfo = WebVideoCtrl.I_GetWindowStatus(g_iWndIndex),
        szInfo = "";

    g_szRecordType = szType;

    if (oWndInfo != null) {
        var szChannelID = $("#channels").val(),
            szFileName = oWndInfo.szDeviceIdentify + "_" + szChannelID + "_" + new Date().getTime();

        WebVideoCtrl.I_StartRecord(szFileName, {
            bDateDir: true, //是否生成日期文件
            success: function () {
                if ('realplay' === szType) {
                    szInfo = "开始录像成功！";
                } else if ('playback' === szType) {
                    szInfo = "开始剪辑成功！";
                }
                showOPInfo(oWndInfo.szDeviceIdentify + " " + szInfo);
            },
            error: function () {
                if ('realplay' === szType) {
                    szInfo = "开始录像失败！";
                } else if ('playback' === szType) {
                    szInfo = "开始剪辑失败！";
                }
                showOPInfo(oWndInfo.szDeviceIdentify + " " + szInfo);
            }
        });
    }
}

// 停止录像
function clickStopRecord(szType, iWndIndex) {
    if ("undefined" === typeof iWndIndex) {
        iWndIndex = g_iWndIndex;
    }
    var oWndInfo = WebVideoCtrl.I_GetWindowStatus(iWndIndex),
        szInfo = "";

    if (oWndInfo != null) {
        WebVideoCtrl.I_StopRecord({
            success: function () {
                if ('realplay' === szType) {
                    szInfo = "停止录像成功！";
                } else if ('playback' === szType) {
                    szInfo = "停止剪辑成功！";
                }
                showOPInfo(oWndInfo.szDeviceIdentify + " " + szInfo);
            },
            error: function () {
                if ('realplay' === szType) {
                    szInfo = "停止录像失败！";
                } else if ('playback' === szType) {
                    szInfo = "停止剪辑失败！";
                }
                showOPInfo(oWndInfo.szDeviceIdentify + " " + szInfo);
            }
        });
    }
}

// 获取对讲通道
function clickGetAudioInfo() {
    var szDeviceIdentify = $("#ip").val();

    if (null == szDeviceIdentify) {
        return;
    }

    WebVideoCtrl.I_GetAudioInfo(szDeviceIdentify, {
        success: function (xmlDoc) {
            var oAudioChannels = $(xmlDoc).find("TwoWayAudioChannel"),
                oSel = $("#audiochannels").empty();
            $.each(oAudioChannels, function () {
                var id = $(this).find("id").eq(0).text();

                oSel.append("<option value='" + id + "'>" + id + "</option>");
            });
            showOPInfo(szDeviceIdentify + " 获取对讲通道成功！");
        },
        error: function (status, xmlDoc) {
            showOPInfo(szDeviceIdentify + " 获取对讲通道失败！", status, xmlDoc);
        }
    });
}

// 开始对讲
function clickStartVoiceTalk() {
    var szDeviceIdentify = $("#ip").val(),
        iAudioChannel = parseInt($("#audiochannels").val(), 10),
        szInfo = "";

    if (null == szDeviceIdentify) {
        return;
    }

    if (isNaN(iAudioChannel)) {
        alert("请选择对讲通道！");
        return;
    }

    var iRet = WebVideoCtrl.I_StartVoiceTalk(szDeviceIdentify, iAudioChannel);

    if (0 == iRet) {
        szInfo = "开始对讲成功！";
    } else {
        szInfo = "开始对讲失败！";
    }
    showOPInfo(szDeviceIdentify + " " + szInfo);
}

// 停止对讲
function clickStopVoiceTalk() {
    var szDeviceIdentify = $("#ip").val(),
        iRet = WebVideoCtrl.I_StopVoiceTalk(),
        szInfo = "";

    if (null == szDeviceIdentify) {
        return;
    }

    if (0 == iRet) {
        szInfo = "停止对讲成功！";
    } else {
        szInfo = "停止对讲失败！";
    }
    showOPInfo(szDeviceIdentify + " " + szInfo);
}

// 启用电子放大
function clickEnableEZoom() {
    var oWndInfo = WebVideoCtrl.I_GetWindowStatus(g_iWndIndex),
        szInfo = "";

    if (oWndInfo != null) {
        var iRet = WebVideoCtrl.I_EnableEZoom();
        if (0 == iRet) {
            szInfo = "启用电子放大成功！";
        } else {
            szInfo = "启用电子放大失败！";
        }
        showOPInfo(oWndInfo.szDeviceIdentify + " " + szInfo);
    }
}

// 禁用电子放大
function clickDisableEZoom() {
    var oWndInfo = WebVideoCtrl.I_GetWindowStatus(g_iWndIndex),
        szInfo = "";

    if (oWndInfo != null) {
        var iRet = WebVideoCtrl.I_DisableEZoom();
        if (0 == iRet) {
            szInfo = "禁用电子放大成功！";
        } else {
            szInfo = "禁用电子放大失败！";
        }
        showOPInfo(oWndInfo.szDeviceIdentify + " " + szInfo);
    }
}

// 启用3D放大
function clickEnable3DZoom() {
    var oWndInfo = WebVideoCtrl.I_GetWindowStatus(g_iWndIndex),
        szInfo = "";

    if (oWndInfo != null) {
        var iRet = WebVideoCtrl.I_Enable3DZoom();
        if (0 == iRet) {
            szInfo = "启用3D放大成功！";
        } else {
            szInfo = "启用3D放大失败！";
        }
        showOPInfo(oWndInfo.szDeviceIdentify + " " + szInfo);
    }
}

// 禁用3D放大
function clickDisable3DZoom() {
    var oWndInfo = WebVideoCtrl.I_GetWindowStatus(g_iWndIndex),
        szInfo = "";

    if (oWndInfo != null) {
        var iRet = WebVideoCtrl.I_Disable3DZoom();
        if (0 == iRet) {
            szInfo = "禁用3D放大成功！";
        } else {
            szInfo = "禁用3D放大失败！";
        }
        showOPInfo(oWndInfo.szDeviceIdentify + " " + szInfo);
    }
}

// 全屏
function clickFullScreen() {
    WebVideoCtrl.I_FullScreen(true);
}

// PTZ控制 9为自动，1,2,3,4,5,6,7,8为方向PTZ
var g_bPTZAuto = false;
function mouseDownPTZControl(iPTZIndex) {
    var oWndInfo = WebVideoCtrl.I_GetWindowStatus(g_iWndIndex),
        bZeroChannel = $("#channels option").eq($("#channels").get(0).selectedIndex).attr("bZero") == "true" ? true : false,
        iPTZSpeed = $("#ptzspeed").val();

    if (bZeroChannel) {// 零通道不支持云台
        return;
    }
    
    if (oWndInfo != null) {
        if (9 == iPTZIndex && g_bPTZAuto) {
            iPTZSpeed = 0;// 自动开启后，速度置为0可以关闭自动
        } else {
            g_bPTZAuto = false;// 点击其他方向，自动肯定会被关闭
        }

        WebVideoCtrl.I_PTZControl(iPTZIndex, false, {
            iPTZSpeed: iPTZSpeed,
            success: function (xmlDoc) {
                if (9 == iPTZIndex && g_bPTZAuto) {
                    showOPInfo(oWndInfo.szDeviceIdentify + " 停止云台成功！");
                } else {
                    showOPInfo(oWndInfo.szDeviceIdentify + " 开启云台成功！");
                }
                if (9 == iPTZIndex) {
                    g_bPTZAuto = !g_bPTZAuto;
                }
            },
            error: function (status, xmlDoc) {
                showOPInfo(oWndInfo.szDeviceIdentify + " 开启云台失败！", status, xmlDoc);
            }
        });
    }
}

// 方向PTZ停止
function mouseUpPTZControl() {
    var oWndInfo = WebVideoCtrl.I_GetWindowStatus(g_iWndIndex);

    if (oWndInfo != null) {
        WebVideoCtrl.I_PTZControl(1, true, {
            success: function (xmlDoc) {
                showOPInfo(oWndInfo.szDeviceIdentify + " 停止云台成功！");
            },
            error: function (status, xmlDoc) {
                showOPInfo(oWndInfo.szDeviceIdentify + " 停止云台失败！", status, xmlDoc);
            }
        });
    }
}

// 设置预置点
function clickSetPreset() {
    var oWndInfo = WebVideoCtrl.I_GetWindowStatus(g_iWndIndex),
        iPresetID = parseInt($("#preset").val(), 10);

    if (oWndInfo != null) {
        WebVideoCtrl.I_SetPreset(iPresetID, {
            success: function (xmlDoc) {
                showOPInfo(oWndInfo.szDeviceIdentify + " 设置预置点成功！");
            },
            error: function (status, xmlDoc) {
                showOPInfo(oWndInfo.szDeviceIdentify + " 设置预置点失败！", status, xmlDoc);
            }
        });
    }
}

// 调用预置点
function clickGoPreset() {
    var oWndInfo = WebVideoCtrl.I_GetWindowStatus(g_iWndIndex),
        iPresetID = parseInt($("#preset").val(), 10);

    if (oWndInfo != null) {
        WebVideoCtrl.I_GoPreset(iPresetID, {
            success: function (xmlDoc) {
                showOPInfo(oWndInfo.szDeviceIdentify + " 调用预置点成功！");
            },
            error: function (status, xmlDoc) {
                showOPInfo(oWndInfo.szDeviceIdentify + " 调用预置点失败！", status, xmlDoc);
            }
        });
    }
}

// 搜索录像
var g_iSearchTimes = 0;
function clickRecordSearch(iType) {
    var szDeviceIdentify = $("#ip").val(),
        iChannelID = parseInt($("#channels").val(), 10),
        bZeroChannel = $("#channels option").eq($("#channels").get(0).selectedIndex).attr("bZero") == "true" ? true : false,
        iStreamType = parseInt($("#record_streamtype").val(), 10),
        szStartTime = $("#starttime").val(),
        szEndTime = $("#endtime").val();

    if (null == szDeviceIdentify) {
        return;
    }

    if (bZeroChannel) {// 零通道不支持录像搜索
        return;
    }

    if (0 == iType) {// 首次搜索
        $("#searchlist").empty();
        g_iSearchTimes = 0;
    }

    WebVideoCtrl.I_RecordSearch(szDeviceIdentify, iChannelID, szStartTime, szEndTime, {
        iStreamType: iStreamType,
        iSearchPos: g_iSearchTimes * 40,
        success: function (xmlDoc) {
            if ("MORE" === $(xmlDoc).find("responseStatusStrg").eq(0).text()) {
                
                for(var i = 0, nLen = $(xmlDoc).find("searchMatchItem").length; i < nLen; i++) {
                    var szPlaybackURI = $(xmlDoc).find("playbackURI").eq(i).text();
                    if(szPlaybackURI.indexOf("name=") < 0) {
                        break;
                    }
                    var szStartTime = $(xmlDoc).find("startTime").eq(i).text();
                    var szEndTime = $(xmlDoc).find("endTime").eq(i).text();
                    var szFileName = szPlaybackURI.substring(szPlaybackURI.indexOf("name=") + 5, szPlaybackURI.indexOf("&size="));

                    var objTr = $("#searchlist").get(0).insertRow(-1);
                    var objTd = objTr.insertCell(0);
                    objTd.id = "downloadTd" + i;
                    objTd.innerHTML = g_iSearchTimes * 40 + (i + 1);
                    objTd = objTr.insertCell(1);
                    objTd.width = "30%";
                    objTd.innerHTML = szFileName;
                    objTd = objTr.insertCell(2);
                    objTd.width = "30%";
                    objTd.innerHTML = (szStartTime.replace("T", " ")).replace("Z", "");
                    objTd = objTr.insertCell(3);
                    objTd.width = "30%";
                    objTd.innerHTML = (szEndTime.replace("T", " ")).replace("Z", "");
                    objTd = objTr.insertCell(4);
                    objTd.width = "10%";
                    objTd.innerHTML = "<a href='javascript:;' onclick='clickStartDownloadRecord(" + (i + g_iSearchTimes * 40) + ");'>下载</a>";
                    $("#downloadTd" + (i + g_iSearchTimes * 40)).data("fileName", szFileName);
                    $("#downloadTd" + (i + g_iSearchTimes * 40)).data("playbackURI", szPlaybackURI);
                }

                g_iSearchTimes++;
                clickRecordSearch(1);// 继续搜索
            } else if ("OK" === $(xmlDoc).find("responseStatusStrg").eq(0).text()) {
                var iLength = $(xmlDoc).find("searchMatchItem").length;
                for(var i = 0; i < iLength; i++) {
                    var szPlaybackURI = $(xmlDoc).find("playbackURI").eq(i).text();
                    if(szPlaybackURI.indexOf("name=") < 0) {
                        break;
                    }
                    var szStartTime = $(xmlDoc).find("startTime").eq(i).text();
                    var szEndTime = $(xmlDoc).find("endTime").eq(i).text();
                    var szFileName = szPlaybackURI.substring(szPlaybackURI.indexOf("name=") + 5, szPlaybackURI.indexOf("&size="));

                    var objTr = $("#searchlist").get(0).insertRow(-1);
                    var objTd = objTr.insertCell(0);
                    objTd.id = "downloadTd" + i;
                    objTd.innerHTML = g_iSearchTimes * 40 + (i + 1);
                    objTd = objTr.insertCell(1);
                    objTd.width = "30%";
                    objTd.innerHTML = szFileName;
                    objTd = objTr.insertCell(2);
                    objTd.width = "30%";
                    objTd.innerHTML = (szStartTime.replace("T", " ")).replace("Z", "");
                    objTd = objTr.insertCell(3);
                    objTd.width = "30%";
                    objTd.innerHTML = (szEndTime.replace("T", " ")).replace("Z", "");
                    objTd = objTr.insertCell(4);
                    objTd.width = "10%";
                    objTd.innerHTML = "<a href='javascript:;' onclick='clickStartDownloadRecord(" + (i + g_iSearchTimes * 40) + ");'>下载</a>";
                    $("#downloadTd" + (i + g_iSearchTimes * 40)).data("fileName", szFileName);
                    $("#downloadTd" + (i + g_iSearchTimes * 40)).data("playbackURI", szPlaybackURI);
                }
                showOPInfo(szDeviceIdentify + " 搜索录像文件成功！");
            } else if("NO MATCHES" === $(xmlDoc).find("responseStatusStrg").eq(0).text()) {
                setTimeout(function() {
                    showOPInfo(szDeviceIdentify + " 没有录像文件！");
                }, 50);
            }
        },
        error: function (status, xmlDoc) {
            showOPInfo(szDeviceIdentify + " 搜索录像文件失败！", status, xmlDoc);
        }
    });
}

// 开始回放
function clickStartPlayback() {
    var oWndInfo = WebVideoCtrl.I_GetWindowStatus(g_iWndIndex),
        szDeviceIdentify = $("#ip").val(),
        iRtspPort = parseInt($("#rtspport").val(), 10),
        iStreamType = parseInt($("#record_streamtype").val(), 10),
        bZeroChannel = $("#channels option").eq($("#channels").get(0).selectedIndex).attr("bZero") == "true" ? true : false,
        iChannelID = parseInt($("#channels").val(), 10),
        szStartTime = $("#starttime").val(),
        szEndTime = $("#endtime").val(),
        szInfo = "",
        bChecked = $("#transstream").prop("checked"),
        iRet = -1;

    if (null == szDeviceIdentify) {
        return;
    }

    if (bZeroChannel) {// 零通道不支持回放
        return;
    }

    var startPlayback = function () {
        if (bChecked) {// 启用转码回放
            var oTransCodeParam = {
                TransFrameRate: "14",// 0：全帧率，5：1，6：2，7：4，8：6，9：8，10：10，11：12，12：16，14：15，15：18，13：20，16：22
                TransResolution: "1",// 255：Auto，3：4CIF，2：QCIF，1：CIF
                TransBitrate: "19"// 2：32K，3：48K，4：64K，5：80K，6：96K，7：128K，8：160K，9：192K，10：224K，11：256K，12：320K，13：384K，14：448K，15：512K，16：640K，17：768K，18：896K，19：1024K，20：1280K，21：1536K，22：1792K，23：2048K，24：3072K，25：4096K，26：8192K
            };
            WebVideoCtrl.I_StartPlayback(szDeviceIdentify, {
                iRtspPort: iRtspPort,
                iStreamType: iStreamType,
                iChannelID: iChannelID,
                szStartTime: szStartTime,
                szEndTime: szEndTime,
                oTransCodeParam: oTransCodeParam,
                success: function () {
                    szInfo = "开始回放成功！";
                    showOPInfo(szDeviceIdentify + " " + szInfo);
                },
                error: function (status, xmlDoc) {
                    if (403 === status) {
                        szInfo = "设备不支持Websocket取流！";
                    } else {
                        szInfo = "开始回放失败！";
                    }
                    showOPInfo(szDeviceIdentify + " " + szInfo);
                }
            });
        } else {
            WebVideoCtrl.I_StartPlayback(szDeviceIdentify, {
                iRtspPort: iRtspPort,
                iStreamType: iStreamType,
                iChannelID: iChannelID,
                szStartTime: szStartTime,
                szEndTime: szEndTime,
                success: function () {
                    szInfo = "开始回放成功！";
                    showOPInfo(szDeviceIdentify + " " + szInfo);
                },
                error: function (status, xmlDoc) {
                    if (403 === status) {
                        szInfo = "设备不支持Websocket取流！";
                    } else {
                        szInfo = "开始回放失败！";
                    }
                    showOPInfo(szDeviceIdentify + " " + szInfo);
                }
            });
        }
    };

    if (oWndInfo != null) {// 已经在播放了，先停止
        WebVideoCtrl.I_Stop({
            success: function () {
                startPlayback();
            }
        });
    } else {
        startPlayback();
    }
}

// 停止回放
function clickStopPlayback() {
    var oWndInfo = WebVideoCtrl.I_GetWindowStatus(g_iWndIndex),
        szInfo = "";

    if (oWndInfo != null) {
        WebVideoCtrl.I_Stop({
            success: function () {
                szInfo = "停止回放成功！";
                showOPInfo(oWndInfo.szDeviceIdentify + " " + szInfo);
            },
            error: function () {
                szInfo = "停止回放失败！";
                showOPInfo(oWndInfo.szDeviceIdentify + " " + szInfo);
            }
        });
    }
}

// 开始倒放
function clickReversePlayback() {
    var oWndInfo = WebVideoCtrl.I_GetWindowStatus(g_iWndIndex),
        szDeviceIdentify = $("#ip").val(),
        iRtspPort = parseInt($("#rtspport").val(), 10),
        iStreamType = parseInt($("#record_streamtype").val(), 10),
        bZeroChannel = $("#channels option").eq($("#channels").get(0).selectedIndex).attr("bZero") == "true" ? true : false,
        iChannelID = parseInt($("#channels").val(), 10),
        szStartTime = $("#starttime").val(),
        szEndTime = $("#endtime").val(),
        szInfo = "";

    if (null == szDeviceIdentify) {
        return;
    }

    if (bZeroChannel) {// 零通道不支持倒放
        return;
    }

    var reversePlayback = function () {
        var iRet = WebVideoCtrl.I_ReversePlayback(szDeviceIdentify, {
            iRtspPort: iRtspPort,
            iStreamType: iStreamType,
            iChannelID: iChannelID,
            szStartTime: szStartTime,
            szEndTime: szEndTime
        });

        if (0 == iRet) {
            szInfo = "开始倒放成功！";
        } else {
            szInfo = "开始倒放失败！";
        }
        showOPInfo(szDeviceIdentify + " " + szInfo);
    };

    if (oWndInfo != null) {// 已经在播放了，先停止
        WebVideoCtrl.I_Stop({
            success: function () {
                reversePlayback();
            }
        });
    } else {
        reversePlayback();
    }
}

// 单帧
function clickFrame() {
    var oWndInfo = WebVideoCtrl.I_GetWindowStatus(g_iWndIndex),
        szInfo = "";

    if (oWndInfo != null) {
        WebVideoCtrl.I_Frame({
            success: function () {
                szInfo = "单帧播放成功！";
                showOPInfo(oWndInfo.szDeviceIdentify + " " + szInfo);
            },
            error: function () {
                szInfo = "单帧播放失败！";
                showOPInfo(oWndInfo.szDeviceIdentify + " " + szInfo);
            }
        });
    }
}

// 暂停
function clickPause() {
    var oWndInfo = WebVideoCtrl.I_GetWindowStatus(g_iWndIndex),
        szInfo = "";

    if (oWndInfo != null) {
        WebVideoCtrl.I_Pause({
            success: function () {
                szInfo = "暂停成功！";
                showOPInfo(oWndInfo.szDeviceIdentify + " " + szInfo);
            },
            error: function () {
                szInfo = "暂停失败！";
                showOPInfo(oWndInfo.szDeviceIdentify + " " + szInfo);
            }
        });
    }
}

// 恢复
function clickResume() {
    var oWndInfo = WebVideoCtrl.I_GetWindowStatus(g_iWndIndex),
        szInfo = "";

    if (oWndInfo != null) {
        WebVideoCtrl.I_Resume({
            success: function () {
                szInfo = "恢复成功！";
                showOPInfo(oWndInfo.szDeviceIdentify + " " + szInfo);
            },
            error: function () {
                szInfo = "恢复失败！";
                showOPInfo(oWndInfo.szDeviceIdentify + " " + szInfo);
            }
        });
    }
}

// 慢放
function clickPlaySlow() {
    var oWndInfo = WebVideoCtrl.I_GetWindowStatus(g_iWndIndex),
        szInfo = "";

    if (oWndInfo != null) {
        WebVideoCtrl.I_PlaySlow({
            success: function () {
                szInfo = "慢放成功！";
                showOPInfo(oWndInfo.szDeviceIdentify + " " + szInfo);
            },
            error: function () {
                szInfo = "慢放失败！";
                showOPInfo(oWndInfo.szDeviceIdentify + " " + szInfo);
            }
        });
    }
}

// 快放
function clickPlayFast() {
    var oWndInfo = WebVideoCtrl.I_GetWindowStatus(g_iWndIndex),
        szInfo = "";

    if (oWndInfo != null) {
        WebVideoCtrl.I_PlayFast({
            success: function () {
                szInfo = "快放成功！";
                showOPInfo(oWndInfo.szDeviceIdentify + " " + szInfo);
            },
            error: function () {
                szInfo = "快放失败！";
                showOPInfo(oWndInfo.szDeviceIdentify + " " + szInfo);
            }
        });
    }
}

// OSD时间
function clickGetOSDTime() {
    var oWndInfo = WebVideoCtrl.I_GetWindowStatus(g_iWndIndex);
    
    if (oWndInfo != null) {
        var szTime = WebVideoCtrl.I_GetOSDTime({
            success: function (szOSDTime) {
                $("#osdtime").val(szOSDTime);
                showOPInfo(oWndInfo.szDeviceIdentify + " 获取OSD时间成功！");
            },
            error: function () {
                showOPInfo(oWndInfo.szDeviceIdentify + " 获取OSD时间失败！");
            }
        });
    }
}

// 下载录像
var g_iDownloadID = -1;
var g_tDownloadProcess = 0;
function clickStartDownloadRecord(i) {
    var szDeviceIdentify = $("#ip").val(),
        szChannelID = $("#channels").val(),
        szFileName = $("#downloadTd" + i).data("fileName"),
        szPlaybackURI = $("#downloadTd" + i).data("playbackURI");

    if (null == szDeviceIdentify) {
        return;
    }

    g_iDownloadID = WebVideoCtrl.I_StartDownloadRecord(szDeviceIdentify, szPlaybackURI, szFileName, {
        bDateDir: true  //是否生成日期文件
    });

    if (g_iDownloadID < 0) {
        var iErrorValue = WebVideoCtrl.I_GetLastError();
        if (34 == iErrorValue) {
            showOPInfo(szDeviceIdentify + " 已下载！");
        } else if (33 == iErrorValue) {
            showOPInfo(szDeviceIdentify + " 空间不足！");
        } else {
            showOPInfo(szDeviceIdentify + " 下载失败！");
        }
    } else {
        $("<div id='downProcess' class='freeze'></div>").appendTo("body");
        g_tDownloadProcess = setInterval("downProcess(" + i + ")", 1000);
    }
}
// 下载进度
function downProcess() {
    var iStatus = WebVideoCtrl.I_GetDownloadStatus(g_iDownloadID);
    if (0 == iStatus) {
        $("#downProcess").css({
            width: $("#searchlist").width() + "px",
            height: "100px",
            lineHeight: "100px",
            left: $("#searchdiv").offset().left + "px",
            top: $("#searchdiv").offset().top + "px"
        });
        var iProcess = WebVideoCtrl.I_GetDownloadProgress(g_iDownloadID);
        if (iProcess < 0) {
            clearInterval(g_tDownloadProcess);
            g_tDownloadProcess = 0;
            g_iDownloadID = -1;
        } else if (iProcess < 100) {
            $("#downProcess").text(iProcess + "%");
        } else {
            $("#downProcess").text("100%");
            setTimeout(function () {
                $("#downProcess").remove();
            }, 1000);

            WebVideoCtrl.I_StopDownloadRecord(g_iDownloadID);

            showOPInfo("录像下载完成！");
            clearInterval(g_tDownloadProcess);
            g_tDownloadProcess = 0;
            g_iDownloadID = -1;
        }
    } else {
        WebVideoCtrl.I_StopDownloadRecord(g_iDownloadID);

        clearInterval(g_tDownloadProcess);
        g_tDownloadProcess = 0;
        g_iDownloadID = -1;
    }
}

// 导出配置文件
function clickExportDeviceConfig() {
    var szDeviceIdentify = $("#ip").val(),
        szInfo = "";

    if (null == szDeviceIdentify) {
        return;
    }

    var iRet = WebVideoCtrl.I_ExportDeviceConfig(szDeviceIdentify);

    if (0 == iRet) {
        szInfo = "导出配置文件成功！";
    } else {
        szInfo = "导出配置文件失败！";
    }
    showOPInfo(szDeviceIdentify + " " + szInfo);
}

// 导入配置文件
function clickImportDeviceConfig() {
    var szDeviceIdentify = $("#ip").val(),
        szFileName = $("#configFile").val();

    if (null == szDeviceIdentify) {
        return;
    }

    if ("" == szFileName) {
        alert("请选择配置文件！");
        return;
    }

    var iRet = WebVideoCtrl.I_ImportDeviceConfig(szDeviceIdentify, szFileName);

    if (0 == iRet) {
        WebVideoCtrl.I_Restart(szDeviceIdentify, {
            success: function (xmlDoc) {
                $("<div id='restartDiv' class='freeze'>重启中...</div>").appendTo("body");
                var oSize = getWindowSize();
                $("#restartDiv").css({
                    width: oSize.width + "px",
                    height: oSize.height + "px",
                    lineHeight: oSize.height + "px",
                    left: 0,
                    top: 0
                });
                setTimeout("reconnect('" + szDeviceIdentify + "')", 20000);
            },
            error: function (status, xmlDoc) {
                showOPInfo(szDeviceIdentify + " 重启失败！", status, xmlDoc);
            }
        });
    } else {
        showOPInfo(szDeviceIdentify + " 导入失败！");
    }
}

// 重连
function reconnect(szDeviceIdentify) {
    WebVideoCtrl.I_Reconnect(szDeviceIdentify, {
        success: function (xmlDoc) {
            $("#restartDiv").remove();
        },
        error: function (status, xmlDoc) {
            if (401 == status) {// 无插件方案，重启后session已失效，程序执行登出，从已登录设备中删除
                $("#restartDiv").remove();
                clickLogout();
            } else {
                setTimeout(function () {reconnect(szDeviceIdentify);}, 5000);
            }
        }
    });
}

// 开始升级
var g_tUpgrade = 0;
function clickStartUpgrade(szDeviceIdentify) {
    var szDeviceIdentify = $("#ip").val(),
        szFileName = $("#upgradeFile").val();

    if (null == szDeviceIdentify) {
        return;
    }

    if ("" == szFileName) {
        alert("请选择升级文件！");
        return;
    }

    var iRet = WebVideoCtrl.I_StartUpgrade(szDeviceIdentify, szFileName);
    if (0 == iRet) {
        g_tUpgrade = setInterval("getUpgradeStatus('" + szDeviceIdentify + "')", 1000);
    } else {
        showOPInfo(szDeviceIdentify + " 升级失败！");
    }
}

// 获取升级状态
function getUpgradeStatus(szDeviceIdentify) {
    var iStatus = WebVideoCtrl.I_UpgradeStatus();
    if (iStatus == 0) {
        var iProcess = WebVideoCtrl.I_UpgradeProgress();
        if (iProcess < 0) {
            clearInterval(g_tUpgrade);
            g_tUpgrade = 0;
            showOPInfo(szDeviceIdentify + " 获取进度失败！");
            return;
        } else if (iProcess < 100) {
            if (0 == $("#restartDiv").length) {
                $("<div id='restartDiv' class='freeze'></div>").appendTo("body");
                var oSize = getWindowSize();
                $("#restartDiv").css({
                    width: oSize.width + "px",
                    height: oSize.height + "px",
                    lineHeight: oSize.height + "px",
                    left: 0,
                    top: 0
                });
            }
            $("#restartDiv").text(iProcess + "%");
        } else {
            WebVideoCtrl.I_StopUpgrade();
            clearInterval(g_tUpgrade);
            g_tUpgrade = 0;

            $("#restartDiv").remove();

            WebVideoCtrl.I_Restart(szDeviceIdentify, {
                success: function (xmlDoc) {
                    $("<div id='restartDiv' class='freeze'>重启中...</div>").appendTo("body");
                    var oSize = getWindowSize();
                    $("#restartDiv").css({
                        width: oSize.width + "px",
                        height: oSize.height + "px",
                        lineHeight: oSize.height + "px",
                        left: 0,
                        top: 0
                    });
                    setTimeout("reconnect('" + szDeviceIdentify + "')", 20000);
                },
                error: function (status, xmlDoc) {
                    showOPInfo(szDeviceIdentify + " 重启失败！", status, xmlDoc);
                }
            });
        }
    } else if (iStatus == 1) {
        WebVideoCtrl.I_StopUpgrade();
        showOPInfo(szDeviceIdentify + " 升级失败！");
        clearInterval(g_tUpgrade);
        g_tUpgrade = 0;
    } else if (iStatus == 2) {
        mWebVideoCtrl.I_StopUpgrade();
        showOPInfo(szDeviceIdentify + " 语言不匹配！");
        clearInterval(g_tUpgrade);
        g_tUpgrade = 0;
    } else {
        mWebVideoCtrl.I_StopUpgrade();
        showOPInfo(szDeviceIdentify + " 获取状态失败！");
        clearInterval(g_tUpgrade);
        g_tUpgrade = 0;
    }
}

// 检查插件版本
function clickCheckPluginVersion() {
    var iRet = WebVideoCtrl.I_CheckPluginVersion();
    if (0 == iRet) {
        alert("您的插件版本已经是最新的！");
    } else {
        alert("检测到新的插件版本！");
    }
}

// 远程配置库
function clickRemoteConfig() {
    var szDeviceIdentify = $("#ip").val(),
        iDevicePort = parseInt($("#deviceport").val(), 10) || "",
        szInfo = "";
    
    if (null == szDeviceIdentify) {
        return;
    }

    var iRet = WebVideoCtrl.I_RemoteConfig(szDeviceIdentify, {
        iDevicePort: iDevicePort,
        iLan: 1
    });

    if (-1 == iRet) {
        szInfo = "调用远程配置库失败！";
    } else {
        szInfo = "调用远程配置库成功！";
    }
    showOPInfo(szDeviceIdentify + " " + szInfo);
}

function clickRestoreDefault() {
    var szDeviceIdentify = $("#ip").val(),
        szMode = "basic";
    WebVideoCtrl.I_RestoreDefault(szDeviceIdentify, szMode, {
        timeout: 30000,
        success: function (xmlDoc) {
            $("#restartDiv").remove();
            showOPInfo(szDeviceIdentify + " 恢复默认参数成功！");
            //恢复完成后需要重启
            WebVideoCtrl.I_Restart(szDeviceIdentify, {
                success: function (xmlDoc) {
                    $("<div id='restartDiv' class='freeze'>重启中...</div>").appendTo("body");
                    var oSize = getWindowSize();
                    $("#restartDiv").css({
                        width: oSize.width + "px",
                        height: oSize.height + "px",
                        lineHeight: oSize.height + "px",
                        left: 0,
                        top: 0
                    });
                    setTimeout("reconnect('" + szDeviceIdentify + "')", 20000);
                },
                error: function (status, xmlDoc) {
                    showOPInfo(szDeviceIdentify + " 重启失败！", status, xmlDoc);
                }
            });
        },
        error: function (status, xmlDoc) {
            showOPInfo(szDeviceIdentify + " 恢复默认参数失败！", status, xmlDoc);
        }
    });
}

function PTZZoomIn() {
    var oWndInfo = WebVideoCtrl.I_GetWindowStatus(g_iWndIndex);

    if (oWndInfo != null) {
        WebVideoCtrl.I_PTZControl(10, false, {
            iWndIndex: g_iWndIndex,
            success: function (xmlDoc) {
                showOPInfo(oWndInfo.szDeviceIdentify + " 调焦+成功！");
            },
            error: function (status, xmlDoc) {
                showOPInfo(oWndInfo.szDeviceIdentify + "  调焦+失败！", status, xmlDoc);
            }
        });
    }
}

function PTZZoomout() {
    var oWndInfo = WebVideoCtrl.I_GetWindowStatus(g_iWndIndex);

    if (oWndInfo != null) {
        WebVideoCtrl.I_PTZControl(11, false, {
            iWndIndex: g_iWndIndex,
            success: function (xmlDoc) {
                showOPInfo(oWndInfo.szDeviceIdentify + " 调焦-成功！");
            },
            error: function (status, xmlDoc) {
                showOPInfo(oWndInfo.szDeviceIdentify + "  调焦-失败！", status, xmlDoc);
            }
        });
    }
}

function PTZZoomStop() {
    var oWndInfo = WebVideoCtrl.I_GetWindowStatus(g_iWndIndex);

    if (oWndInfo != null) {
        WebVideoCtrl.I_PTZControl(11, true, {
            iWndIndex: g_iWndIndex,
            success: function (xmlDoc) {
                showOPInfo(oWndInfo.szDeviceIdentify + " 调焦停止成功！");
            },
            error: function (status, xmlDoc) {
                showOPInfo(oWndInfo.szDeviceIdentify + "  调焦停止失败！", status, xmlDoc);
            }
        });
    }
}

function PTZFocusIn() {
    var oWndInfo = WebVideoCtrl.I_GetWindowStatus(g_iWndIndex);

    if (oWndInfo != null) {
        WebVideoCtrl.I_PTZControl(12, false, {
            iWndIndex: g_iWndIndex,
            success: function (xmlDoc) {
                showOPInfo(oWndInfo.szDeviceIdentify + " 聚焦+成功！");
            },
            error: function (status, xmlDoc) {
                showOPInfo(oWndInfo.szDeviceIdentify + "  聚焦+失败！", status, xmlDoc);
            }
        });
    }
}

function PTZFoucusOut() {
    var oWndInfo = WebVideoCtrl.I_GetWindowStatus(g_iWndIndex);

    if (oWndInfo != null) {
        WebVideoCtrl.I_PTZControl(13, false, {
            iWndIndex: g_iWndIndex,
            success: function (xmlDoc) {
                showOPInfo(oWndInfo.szDeviceIdentify + " 聚焦-成功！");
            },
            error: function (status, xmlDoc) {
                showOPInfo(oWndInfo.szDeviceIdentify + "  聚焦-失败！", status, xmlDoc);
            }
        });
    }
}

function PTZFoucusStop() {
    var oWndInfo = WebVideoCtrl.I_GetWindowStatus(g_iWndIndex);

    if (oWndInfo != null) {
        WebVideoCtrl.I_PTZControl(12, true, {
            iWndIndex: g_iWndIndex,
            success: function (xmlDoc) {
                showOPInfo(oWndInfo.szDeviceIdentify + " 聚焦停止成功！");
            },
            error: function (status, xmlDoc) {
                showOPInfo(oWndInfo.szDeviceIdentify + "  聚焦停止失败！", status, xmlDoc);
            }
        });
    }
}

function PTZIrisIn() {
    var oWndInfo = WebVideoCtrl.I_GetWindowStatus(g_iWndIndex);

    if (oWndInfo != null) {
        WebVideoCtrl.I_PTZControl(14, false, {
            iWndIndex: g_iWndIndex,
            success: function (xmlDoc) {
                showOPInfo(oWndInfo.szDeviceIdentify + " 光圈+成功！");
            },
            error: function (status, xmlDoc) {
                showOPInfo(oWndInfo.szDeviceIdentify + "  光圈+失败！", status, xmlDoc);
            }
        });
    }
}

function PTZIrisOut() {
    var oWndInfo = WebVideoCtrl.I_GetWindowStatus(g_iWndIndex);

    if (oWndInfo != null) {
        WebVideoCtrl.I_PTZControl(15, false, {
            iWndIndex: g_iWndIndex,
            success: function (xmlDoc) {
                showOPInfo(oWndInfo.szDeviceIdentify + " 光圈-成功！");
            },
            error: function (status, xmlDoc) {
                showOPInfo(oWndInfo.szDeviceIdentify + "  光圈-失败！", status, xmlDoc);
            }
        });
    }
}

function PTZIrisStop() {
    var oWndInfo = WebVideoCtrl.I_GetWindowStatus(g_iWndIndex);

    if (oWndInfo != null) {
        WebVideoCtrl.I_PTZControl(14, true, {
            iWndIndex: g_iWndIndex,
            success: function (xmlDoc) {
                showOPInfo(oWndInfo.szDeviceIdentify + " 光圈停止成功！");
            },
            error: function (status, xmlDoc) {
                showOPInfo(oWndInfo.szDeviceIdentify + "  光圈停止失败！", status, xmlDoc);
            }
        });
    }
}

// 切换模式
function changeIPMode(iType) {
    var arrPort = [0, 7071, 80];

    $("#serverport").val(arrPort[iType]);
}

// 获取设备IP
function clickGetDeviceIP() {
    var iDeviceMode = parseInt($("#devicemode").val(), 10),
        szAddress = $("#serveraddress").val(),
        iPort = parseInt($("#serverport").val(), 10) || 0,
        szDeviceID = $("#deviceid").val(),
        szDeviceInfo = "";

    szDeviceInfo = WebVideoCtrl.I_GetIPInfoByMode(iDeviceMode, szAddress, iPort, szDeviceID);

    if ("" == szDeviceInfo) {
        showOPInfo("设备IP和端口解析失败！");
    } else {
        showOPInfo("设备IP和端口解析成功！");

        var arrTemp = szDeviceInfo.split("-");
        $("#loginip").val(arrTemp[0]);
        $("#deviceport").val(arrTemp[1]);
    }
}

// 启用多边形绘制
var g_bEnableDraw = false;
function clickEnableDraw() {
    var iRet = WebVideoCtrl.I_SetPlayModeType(6);// 多边形模式

    if (0 === iRet) {
        g_bEnableDraw = true;

        showOPInfo("启用绘制成功！");
    } else {
        showOPInfo("启用绘制失败！");
    }
}

// 禁用多边形绘制
function clickDisableDraw() {
    var iRet = WebVideoCtrl.I_SetPlayModeType(0);// 预览模式
    if (0 === iRet) {
        g_bEnableDraw = false;
        
        showOPInfo("禁用绘制成功！");
    } else {
        showOPInfo("禁用绘制失败！");
    }
}

// 添加图形
function clickAddSnapPolygon() {
    if (!g_bEnableDraw) {
        return;
    }

    var szId = $("#snapId").val();
    var szName = encodeString($("#snapName").val());

    var szInfo = "<?xml version='1.0' encoding='utf-8'?>";
    szInfo += "<SnapPolygonList>";
    szInfo += "<SnapPolygon>";
    szInfo += "<id>" + szId + "</id>";          // [1, 32]
    szInfo += "<polygonType>1</polygonType>";
    szInfo += "<PointNumMax>17</PointNumMax>";  // [MinClosed, 17]
    szInfo += "<MinClosed>4</MinClosed>";       // [4, 17]
    szInfo += "<tips>#" + szId + "#" + szName + "</tips>";
    szInfo += "<isClosed>false</isClosed>";
    szInfo += "<color><r>0</r><g>255</g><b>0</b></color>";
    szInfo += "<pointList/>";
    szInfo += "</SnapPolygon>";
    szInfo += "</SnapPolygonList>";

    var iRet = WebVideoCtrl.I_SetSnapPolygonInfo(g_iWndIndex, szInfo);
    if (0 === iRet) {
        showOPInfo("窗口" + g_iWndIndex + "添加图形成功！");
    } else if (-1 === iRet) {
        showOPInfo("窗口" + g_iWndIndex + "添加图形失败！");
    } else if (-2 === iRet) {
        alert("参数错误！");
    } else if (-3 === iRet) {
        alert("图形个数达到上限！");
    } else if (-4 === iRet) {
        alert("图形ID已存在！");
    }
    WebVideoCtrl.I_SetSnapDrawMode(g_iWndIndex, 2);
}

// 删除图形
function clickDelSnapPolygon() {
    if (!g_bEnableDraw) {
        return;
    }

    var szId = $("#snapId").val();

    var iIndex = getSnapPolygon(szId);
    if (iIndex != -1) {
        var oXML = getSnapPolygon();
        $(oXML).find("SnapPolygon").eq(iIndex).remove();

        var szInfo = toXMLStr(oXML);

        WebVideoCtrl.I_ClearSnapInfo(g_iWndIndex);
        WebVideoCtrl.I_SetSnapPolygonInfo(g_iWndIndex, szInfo);
        WebVideoCtrl.I_SetSnapDrawMode(g_iWndIndex, 3);
    } else {
        alert("图形ID不存在！");
    }
}

// 编辑图形
function clickEditSnapPolygon() {
    if (!g_bEnableDraw) {
        return;
    }

    var iRet = WebVideoCtrl.I_SetSnapDrawMode(g_iWndIndex, 3);
    if (0 === iRet) {
        showOPInfo("窗口" + g_iWndIndex + "编辑图形成功！");
    } else {
        showOPInfo("窗口" + g_iWndIndex + "编辑图形失败！");
    }
}

// 停止编辑
function clickStopSnapPolygon() {
    if (!g_bEnableDraw) {
        return;
    }

    var iRet = WebVideoCtrl.I_SetSnapDrawMode(g_iWndIndex, -1);
    if (0 === iRet) {
        showOPInfo("窗口" + g_iWndIndex + "停止编辑成功！");
    } else {
        showOPInfo("窗口" + g_iWndIndex + "停止编辑失败！");
    }
}

function getSnapPolygon(szId) {
    var szInfo = WebVideoCtrl.I_GetSnapPolygonInfo(g_iWndIndex);
    var oXML = loadXML(szInfo);

    if (typeof szId === "undefined") {
        return oXML;
    } else {
        var iIndex = -1;

        var aNodeList = $(oXML).find("SnapPolygon");
        if (aNodeList.length > 0) {
            $.each(aNodeList, function (i) {
                if ($(this).find("id").text() === szId) {
                    iIndex = i;
                    return false;
                }
            });
        }

        return iIndex;
    }
}

// 获取图形，保存到自己数据库中
function clickGetSnapPolygon() {
    if (!g_bEnableDraw) {
        return;
    }

    var szInfo = WebVideoCtrl.I_GetSnapPolygonInfo(g_iWndIndex);

    alert(szInfo);
}

// 设置图形，页面打开时可以设置以前设置过的图形
function clickSetSnapPolygon() {
    if (!g_bEnableDraw) {
        return;
    }

    WebVideoCtrl.I_ClearSnapInfo(g_iWndIndex);

    var szInfo = "<?xml version='1.0' encoding='utf-8'?>";
    szInfo += "<SnapPolygonList>";
    szInfo += "<SnapPolygon>";
    szInfo += "<id>1</id>";
    szInfo += "<polygonType>1</polygonType>";
    szInfo += "<tips>#1#设置1</tips>";
    szInfo += "<isClosed>true</isClosed>";
    szInfo += "<color><r>0</r><g>255</g><b>0</b></color>";
    szInfo += "<pointList>";
    szInfo += "<point><x>0.737903</x><y>0.229730</y></point>";
    szInfo += "<point><x>0.947581</x><y>0.804054</y></point>";
    szInfo += "<point><x>0.362903</x><y>0.777027</y></point>";
    szInfo += "</pointList>";
    szInfo += "</SnapPolygon>";
    szInfo += "<SnapPolygon>";
    szInfo += "<id>2</id>";
    szInfo += "<polygonType>1</polygonType>";
    szInfo += "<tips>#2#设置2</tips>";
    szInfo += "<isClosed>true</isClosed>";
    szInfo += "<color><r>0</r><g>255</g><b>0</b></color>";
    szInfo += "<pointList>";
    szInfo += "<point><x>0.451613</x><y>0.216216</y></point>";
    szInfo += "<point><x>0.447581</x><y>0.729730</y></point>";
    szInfo += "<point><x>0.116935</x><y>0.554054</y></point>";
    szInfo += "</pointList>";
    szInfo += "</SnapPolygon>";
    szInfo += "</SnapPolygonList>";

    var iRet = WebVideoCtrl.I_SetSnapPolygonInfo(g_iWndIndex, szInfo);
    if (0 === iRet) {
        showOPInfo("窗口" + g_iWndIndex + "设置图形成功！");
    } else if (-1 === iRet) {
        showOPInfo("窗口" + g_iWndIndex + "设置图形失败！");
    } else if (-2 === iRet) {
        alert("参数错误！");
    } else if (-3 === iRet) {
        alert("图形个数达到上限！");
    } else if (-4 === iRet) {
        alert("图形ID已存在！");
    }
}

// 清空图形
function clickDelAllSnapPolygon() {
    if (!g_bEnableDraw) {
        return;
    }

    var iRet = WebVideoCtrl.I_ClearSnapInfo(g_iWndIndex);
    if (0 === iRet) {
        showOPInfo("窗口" + g_iWndIndex + "清空图形成功！");
    } else {
        showOPInfo("窗口" + g_iWndIndex + "清空图形失败！");
    }
}

// 设备抓图
function clickDeviceCapturePic() {
    var szInfo = "";
    var szDeviceIdentify = $("#ip").val();
    var bZeroChannel = $("#channels option").eq($("#channels").get(0).selectedIndex).attr("bZero") == "true" ? true : false;
    var iChannelID = parseInt($("#channels").val(), 10);
    var iResolutionWidth = parseInt($("#resolutionWidth").val(), 10);
    var iResolutionHeight = parseInt($("#resolutionHeight").val(), 10);

    if (null == szDeviceIdentify) {
        return;
    }
    
    if (bZeroChannel) {// 零通道不支持设备抓图
        return;
    }

    var szPicName = szDeviceIdentify + "_" + iChannelID + "_" + new Date().getTime();
    var iRet = WebVideoCtrl.I_DeviceCapturePic(szDeviceIdentify, iChannelID, szPicName, {
        bDateDir: true,  //是否生成日期文件
        iResolutionWidth: iResolutionWidth,
        iResolutionHeight: iResolutionHeight
    });

    if (0 == iRet) {
        szInfo = "设备抓图成功！";
    } else {
        szInfo = "设备抓图失败！";
    }
    showOPInfo(szDeviceIdentify + " " + szInfo);
}

function loadXML(szXml) {
    if(null == szXml || "" == szXml) {
        return null;
    }

    var oXmlDoc = null;

    if (window.DOMParser) {
        var oParser = new DOMParser();
        oXmlDoc = oParser.parseFromString(szXml, "text/xml");
    } else {
        oXmlDoc = new ActiveXObject("Microsoft.XMLDOM");
        oXmlDoc.async = false;
        oXmlDoc.loadXML(szXml);
    }

    return oXmlDoc;
}

function toXMLStr(oXmlDoc) {
    var szXmlDoc = "";

    try {
        var oSerializer = new XMLSerializer();
        szXmlDoc = oSerializer.serializeToString(oXmlDoc);
    } catch (e) {
        try {
            szXmlDoc = oXmlDoc.xml;
        } catch (e) {
            return "";
        }
    }
    if (szXmlDoc.indexOf("<?xml") == -1) {
        szXmlDoc = "<?xml version='1.0' encoding='utf-8'?>" + szXmlDoc;
    }

    return szXmlDoc;
}

function encodeString(str) {
    if (str) {
        return str.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
    } else {
        return "";
    }
}

function InsertSampleInfo(camIp) {
    var room_number = $("#roomnumber").val();
    var sample_reporter_name = $("#sample_reporter_name").val();
    var sample_number = $("#sample_number").val();
    var sample_test_date = $("#sample_test_date").val();
    var sample_inspection_unit = $("#sample_inspection_unit").val();
    var sample_production_number = $("#sample_production_number").val();
    var tire_number = $("#tire_number").val();
    var sample_tyre_standard = $("#sample_tyre_standard").val();
    var sample_load_rode = $("#sample_load_rode").val();
    var sample_tire_radius = $("#sample_tire_radius").val();
    var sample_conversion = $("#sample_conversion").val();
    $.ajax({
        url: '/insertSample',
        type: "post",
        contentType: "application/x-www-form-urlencoded; charset=utf-8",
        data: {
            "room_number": room_number,
            "sample_reporter_name": sample_reporter_name,
            "sample_number": sample_number,
            "sample_test_date": sample_test_date,
            "sample_inspection_unit": sample_inspection_unit,
            "sample_production_number": sample_production_number,
            "tire_number": tire_number,
            "sample_tyre_standard": sample_tyre_standard,
            "sample_load_rode": sample_load_rode,
            "sample_tire_radius": sample_tire_radius,
            "sample_conversion": sample_conversion,
            "cam_ip":camIp
        },
        cache: false,
        dataType:"json",
        error: function () {
            // alert("你所查询的内容不存在！")
        },
        success: function (data) {
   //         alert("成功了")
        }
    });
}

//获取当前房间所有摄像头
//var dataCamInfoz
function realTimeTempMeasurement(){
    var camIp,camPort,camUsername,camPassword;
    var tire_number = $("#tire_number").val();
//    showCamInfo(room_number);
    for (var i=0;i<dataCamInfo.length;i++)
    {
        camIp = dataCamInfo[i].cam_ip;
        camPort = dataCamInfo[i].cam_port;
        camUsername = dataCamInfo[i].cam_user;
        camPassword = dataCamInfo[i].cam_pwd;
        InsertSampleInfo(camIp);
        insertRealTemp(camIp,camPort,camUsername,camPassword,tire_number);
    }
}

//插入实时温度信息
function insertRealTemp(camIp,camPort,camUsername,camPassword,tire_number){
        $.ajax({
            url: '/realTimeTempMeasurement',
            type: "post",
            contentType: "application/x-www-form-urlencoded; charset=utf-8",
            data: {
                "m_sDeviceIP": camIp,
                "camPort": camPort,
                "camUserName": camUsername,
                "camPassword": camPassword,
                "tire_number": tire_number
            },
            cache: false,
            dataType:"json",
            error: function () {
                // alert("你所查询的内容不存在！")
            },
            success: function () {
   //             alert("成功了")
            }
        });
  //  }
}

var flag1 = true;
function chkRadio1(cam_station_number1) {
    cam_station_number1.checked = flag1;
    flag1 = !flag1;
}

var flag2 = true;
function chkRadio2(cam_station_number2) {
    cam_station_number2.checked = flag2;
    flag2 = !flag2;
}

//停止插入实时温度信息
function stopInsertRealTemp(){
    var cam_station_number1 = document.getElementsByName('chkRadio1')[0].checked;
    var cam_station_number2 = document.getElementsByName('chkRadio2')[0].checked;
    var room_number = document.getElementById("roomnumber").value;
    var label = document.getElementById("showBtnContent");
    var cam_station1,cam_station2;
    if(!cam_station_number1 && !cam_station_number2){
        alert("请选择需要关闭的仓位号！");
    }else{
        if(cam_station_number1 && cam_station_number2) {
            cam_station1 = 1;
            cam_station2 = 2;
            showStationCamInfo1(room_number,cam_station1);
            showStationCamInfo2(room_number,cam_station2);
            label.innerHTML = "温度获取均已结束";
        }
        if(cam_station_number1 && !cam_station_number2){
            cam_station1 = 1;
            cam_station2 = 0;
            showStationCamInfo1(room_number,cam_station1);
            dataCamInfo2=null;
            label.innerHTML = "仓位1温度获取已结束";
        }
        if(!cam_station_number1 && cam_station_number2){
            cam_station1 = 0;
            cam_station2 = 2;
            showStationCamInfo2(room_number,cam_station2);
            dataCamInfo1=null;
            label.innerHTML = "仓位2温度获取已结束";
        }
        var JSONObj = new Object();
        var dataCamInfoSearch1 = JSON.stringify(dataCamInfo1);
        JSONObj.dataCamInfo1 = dataCamInfoSearch1;
        var dataCamInfoSearch2 = JSON.stringify(dataCamInfo2);
        JSONObj.dataCamInfo2 = dataCamInfoSearch2;
        var cam_station_number1 = JSON.stringify(cam_station1);
        JSONObj.cam_station_number1 = cam_station_number1;
        var cam_station_number2 = JSON.stringify(cam_station2);
        JSONObj.cam_station_number2 = cam_station_number2;
        JSONObj.room_number = room_number;
        $.ajax({
            url: '/stopInsertRealTemp',
            type: "post",
            // contentType: "application/x-www-form-urlencoded; charset=utf-8",
            data: JSONObj,
            cache: false,
            dataType:"json",
            error: function () {
                //             alert("停止失败！")
            },
            success: function () {
                            // alert("停止成功")
            }
        });

    }
     //clearInterval(interval);
    //saveImage();
    //generateExcelReport();
   //saveImageFILe();
    //generateExcelReport();
    //window.onbeforeunload = onbeforeunload_handler1;
    $("#startRecord").attr("disabled",false);
    $("#startRecord").css("pointer-events","auto");
    $("#startRecord").css({
        "padding": "5px 15px", "display": "inline-block", "border": "1px solid #ccc" , "background": "#fff" , "font-weight": "bold !important", "font-size": "11px", "color": "#333 !important",
    "-moz-border-radius": "2px", "-webkit-border-radius": "2px", "border-radius": "2px", "-moz-box-shadow": "0 1px 0 #fff",
    "-webkit-box-shadow": "0 1px 0 #fff", "box-shadow": "0 1px 0 #fff", "line-height": "21px"});
    $("#startRecord").hover(
        function(){
            $("#startRecord") .css({
                "border": "1px solid #bbb", "background-color": "#f7f7f7", "-moz-box-shadow": "inset 0 1px 0 #fff",
                "-webkit-box-shadow": "inset 0 1px 0 #fff", "box-shadow": "inset 0 1px 0 #fff"});
        },
        function(){
            $("#startRecord").css({
                "padding": "5px 15px", "display": "inline-block", "border": "1px solid #ccc" , "background": "#fff" , "font-weight": "bold !important", "font-size": "11px", "color": "#333 !important",
                "-moz-border-radius": "2px", "-webkit-border-radius": "2px", "border-radius": "2px", "-moz-box-shadow": "0 1px 0 #fff",
                "-webkit-box-shadow": "0 1px 0 #fff", "box-shadow": "0 1px 0 #fff", "line-height": "21px"});

        }
    )

    //clearInterval(interval);
    ImageSaveFile();
    generateExcelReport();
    $("#refresh_rate").find("option[value ='thirty']").attr("selected",true);
}
var dataCamInfo1;
var dataCamInfo2;

function controlRealTempMeas(){
    //window.onbeforeunload = onbeforeunload_handler;
    var sampleInfo1 = new Object();
    var sampleInfo2 = new Object();
    var setIntFreq = new Object();
    var cam_station_number1 = document.getElementsByName('chkRadio1')[0].checked;
    var cam_station_number2 = document.getElementsByName('chkRadio2')[0].checked;

    if(!cam_station_number1 && !cam_station_number2){
        alert("请选择仓位号!");
    }else{
        var label = document.getElementById("showBtnContent");
        //label.innerHTML= "正在获取温度...";
        if(cam_station_number1 && cam_station_number2){

            sampleInfo1.room_number = document.getElementById("roomnumber").value;
            sampleInfo1.cam_station_number = 1;
            sampleInfo1.tire_number = document.getElementById("tire_number1").value;
            sampleInfo1.sample_specification = document.getElementById("sample_specification1").value;
            sampleInfo1.sample_load_rode = document.getElementById("sample_load_rode1").value;
            // sampleInfo1.sample_reporter_name = document.getElementById("sample_reporter_name1").value;
            sampleInfo1.sample_number = document.getElementById("sample_number1").value;
            if(sampleInfo1.sample_number==""){sampleInfo1.sample_number="null";document.getElementById("sample_number1").value="null"}
            sampleInfo1.sample_test_date = document.getElementById("sample_test_date1").value;
            // sampleInfo1.sample_inspection_unit = document.getElementById("sample_inspection_unit1").value;
            // sampleInfo1.sample_production_number = document.getElementById("sample_production_number1").value;
            // sampleInfo1.sample_tire_radius = document.getElementById("sample_tire_radius1").value;
            // sampleInfo1.sample_conversion = document.getElementById("sample_conversion1").value;
            sampleInfo1.sample_brand = document.getElementById("sample_brand1").value;
            sampleInfo1.sample_level = document.getElementById("sample_level1").value;
            sampleInfo1.sample_pattern = document.getElementById("sample_pattern1").value;
            sampleInfo1.sample_person = document.getElementById("sample_person1").value;
            sampleInfo1.sample_scheme = document.getElementById("sample_scheme1").value;
            sampleInfo1.sample_object_name=document.getElementById("sample_object_name1").value;

            sampleInfo2.room_number = document.getElementById("roomnumber").value;
            sampleInfo2.cam_station_number = 2;
            sampleInfo2.tire_number = document.getElementById("tire_number2").value;
            sampleInfo2.sample_specification = document.getElementById("sample_specification2").value;
            sampleInfo2.sample_load_rode = document.getElementById("sample_load_rode2").value;
           // sampleInfo2.sample_reporter_name = document.getElementById("sample_reporter_name2").value;
            sampleInfo2.sample_number = document.getElementById("sample_number2").value;
            if(sampleInfo2.sample_number==""){sampleInfo2.sample_number="null";document.getElementById("sample_number2").value="null"}
            sampleInfo2.sample_test_date = document.getElementById("sample_test_date2").value;
            //sampleInfo2.sample_inspection_unit = document.getElementById("sample_inspection_unit2").value;
            //sampleInfo2.sample_production_number = document.getElementById("sample_production_number2").value;
            //sampleInfo2.sample_tire_radius = document.getElementById("sample_tire_radius2").value;
            //sampleInfo2.sample_conversion = document.getElementById("sample_conversion2").value;
            sampleInfo2.sample_brand = document.getElementById("sample_brand2").value;
            sampleInfo2.sample_level = document.getElementById("sample_level2").value;
            sampleInfo2.sample_pattern = document.getElementById("sample_pattern2").value;
            sampleInfo2.sample_person = document.getElementById("sample_person2").value;
            sampleInfo2.sample_scheme = document.getElementById("sample_scheme2").value;
            sampleInfo2.sample_object_name=document.getElementById("sample_object_name2").value;


            setIntFreq.first_time_interval = document.getElementById("first_time_interval").value;
            setIntFreq.second_time_interval = document.getElementById("second_time_interval").value;
            setIntFreq.first_point_frequency = document.getElementById("first_point_frequency").value;
            setIntFreq.second_point_frequency = document.getElementById("second_point_frequency").value;
            setIntFreq.third_point_frequency = document.getElementById("third_point_frequency").value;
            if(sampleInfo1.tire_number==""||sampleInfo1.sample_specification==""||sampleInfo1.sample_load_rode==""||sampleInfo1.sample_number==""||sampleInfo1.sample_test_date==""||sampleInfo1.sample_brand==""||sampleInfo1.sample_level==""||sampleInfo1.sample_pattern==""||sampleInfo1.sample_person==""||sampleInfo2.tire_number==""||sampleInfo2.sample_specification==""||sampleInfo2.sample_load_rode==""||sampleInfo2.sample_number==""||sampleInfo2.sample_test_date==""||sampleInfo2.sample_brand==""||sampleInfo2.sample_level==""||sampleInfo2.sample_pattern==""||sampleInfo2.sample_person==""||setIntFreq.first_time_interval==""||setIntFreq.second_time_interval==""||setIntFreq.first_point_frequency==""||setIntFreq.second_point_frequency==""||setIntFreq.third_point_frequency==""){
                alert("请将必填内容填写完整！");
            }
            else{

                showStationCamInfo1(sampleInfo1.room_number,sampleInfo1.cam_station_number);
                showStationCamInfo2(sampleInfo2.room_number,sampleInfo2.cam_station_number);
                getcontrolRealTempMeas(dataCamInfo1,dataCamInfo2,sampleInfo1,sampleInfo2,setIntFreq);
            }

        }
        if(cam_station_number1 && !cam_station_number2){
            sampleInfo1.room_number = document.getElementById("roomnumber").value;
            sampleInfo1.cam_station_number = 1;
            sampleInfo1.tire_number = document.getElementById("tire_number1").value;
            sampleInfo1.sample_specification = document.getElementById("sample_specification1").value;
            sampleInfo1.sample_load_rode = document.getElementById("sample_load_rode1").value;
            //sampleInfo1.sample_reporter_name = document.getElementById("sample_reporter_name1").value;
            sampleInfo1.sample_number = document.getElementById("sample_number1").value;
            //console.log(sampleInfo1.sample_number);
            if(sampleInfo1.sample_number==""){sampleInfo1.sample_number="null" ;document.getElementById("sample_number1").value="null"}
           // console.log(sampleInfo1.sample_number);
            sampleInfo1.sample_test_date = document.getElementById("sample_test_date1").value;
            //sampleInfo1.sample_inspection_unit = document.getElementById("sample_inspection_unit1").value;
            //sampleInfo1.sample_production_number = document.getElementById("sample_production_number1").value;
            //sampleInfo1.sample_tire_radius = document.getElementById("sample_tire_radius1").value;
            //sampleInfo1.sample_conversion = document.getElementById("sample_conversion1").value;
            sampleInfo1.sample_brand = document.getElementById("sample_brand1").value;
            sampleInfo1.sample_level = document.getElementById("sample_level1").value;
            sampleInfo1.sample_pattern = document.getElementById("sample_pattern1").value;
            sampleInfo1.sample_person = document.getElementById("sample_person1").value;
            sampleInfo1.sample_scheme = document.getElementById("sample_scheme1").value;
            sampleInfo1.sample_object_name=document.getElementById("sample_object_name1").value;

            setIntFreq.first_time_interval = document.getElementById("first_time_interval").value;
            setIntFreq.second_time_interval = document.getElementById("second_time_interval").value;
            setIntFreq.first_point_frequency = document.getElementById("first_point_frequency").value;
            setIntFreq.second_point_frequency = document.getElementById("second_point_frequency").value;
            setIntFreq.third_point_frequency = document.getElementById("third_point_frequency").value;

            if(sampleInfo1.tire_number==""||sampleInfo1.sample_specification==""||sampleInfo1.sample_load_rode==""||sampleInfo1.sample_number==""||sampleInfo1.sample_test_date==""||sampleInfo1.sample_brand==""||sampleInfo1.sample_level==""||sampleInfo1.sample_pattern==""||sampleInfo1.sample_person==""||setIntFreq.first_time_interval==""||setIntFreq.second_time_interval==""||setIntFreq.first_point_frequency==""||setIntFreq.second_point_frequency==""||setIntFreq.third_point_frequency==""){
                alert("请将必填内容填写完整！");
            }
            else{
                dataCamInfo2 = null;
                sampleInfo2 = 0;
                showStationCamInfo1(sampleInfo1.room_number,sampleInfo1.cam_station_number);
                getcontrolRealTempMeas(dataCamInfo1,dataCamInfo2,sampleInfo1,sampleInfo2,setIntFreq);
            }

        }
        if(!cam_station_number1 && cam_station_number2){
            sampleInfo2.room_number = document.getElementById("roomnumber").value;
            sampleInfo2.cam_station_number = 2;
            sampleInfo2.tire_number = document.getElementById("tire_number2").value;
            sampleInfo2.sample_specification = document.getElementById("sample_specification2").value;
            sampleInfo2.sample_load_rode = document.getElementById("sample_load_rode2").value;
            //sampleInfo2.sample_reporter_name = document.getElementById("sample_reporter_name2").value;
            sampleInfo2.sample_number = document.getElementById("sample_number2").value;
            if(sampleInfo2.sample_number==""){sampleInfo2.sample_number="null";document.getElementById("sample_number2").value="null"}
            sampleInfo2.sample_test_date = document.getElementById("sample_test_date2").value;
            //sampleInfo2.sample_inspection_unit = document.getElementById("sample_inspection_unit2").value;
           // sampleInfo2.sample_production_number = document.getElementById("sample_production_number2").value;
            //sampleInfo2.sample_tire_radius = document.getElementById("sample_tire_radius2").value;
            //sampleInfo2.sample_conversion = document.getElementById("sample_conversion2").value;
            sampleInfo2.sample_brand = document.getElementById("sample_brand2").value;
            sampleInfo2.sample_level = document.getElementById("sample_level2").value;
            sampleInfo2.sample_pattern = document.getElementById("sample_pattern2").value;
            sampleInfo2.sample_person = document.getElementById("sample_person2").value;
            sampleInfo2.sample_scheme = document.getElementById("sample_scheme2").value;
            sampleInfo2.sample_object_name=document.getElementById("sample_object_name2").value;

            setIntFreq.first_time_interval = document.getElementById("first_time_interval").value;
            setIntFreq.second_time_interval = document.getElementById("second_time_interval").value;
            setIntFreq.first_point_frequency = document.getElementById("first_point_frequency").value;
            setIntFreq.second_point_frequency = document.getElementById("second_point_frequency").value;
            setIntFreq.third_point_frequency = document.getElementById("third_point_frequency").value;
            if(sampleInfo2.tire_number==""||sampleInfo2.sample_specification==""||sampleInfo2.sample_load_rode==""||sampleInfo2.sample_number==""||sampleInfo2.sample_test_date==""||sampleInfo2.sample_brand==""||sampleInfo2.sample_level==""||sampleInfo2.sample_pattern==""||sampleInfo2.sample_person==""||setIntFreq.first_time_interval==""||setIntFreq.second_time_interval==""||setIntFreq.first_point_frequency==""||setIntFreq.second_point_frequency==""||setIntFreq.third_point_frequency==""){
                alert("请将必填内容填写完整！");
            }
            else{
                sampleInfo1 = 0;
                dataCamInfo1 = null;
                showStationCamInfo2(sampleInfo2.room_number,sampleInfo2.cam_station_number);
                getcontrolRealTempMeas(dataCamInfo1,dataCamInfo2,sampleInfo1,sampleInfo2,setIntFreq);

            }


        }

    }


}
//把controlRealTempMeas中得到的dataCamInfo1,dataCamInfo2,sampleInfo1,sampleInfo2数据通过ajax传到后台java层并得到返回值
function getcontrolRealTempMeas(dataCamInfo1,dataCamInfo2,sampleInfo1,sampleInfo2,setIntFreq){
    var JSONObj = new Object();
    var dataCamInfoSearch1 = JSON.stringify(dataCamInfo1);
    JSONObj.dataCamInfo1 = dataCamInfoSearch1;
    var dataCamInfoSearch2 = JSON.stringify(dataCamInfo2);
    JSONObj.dataCamInfo2 = dataCamInfoSearch2;
    var sample1 = JSON.stringify(sampleInfo1);
    JSONObj.sampleInfo1 = sample1;
    var sample2 = JSON.stringify(sampleInfo2);
    JSONObj.sampleInfo2 = sample2;
    var setIntervalFreq = JSON.stringify(setIntFreq);
    JSONObj.setIntervalFreq = setIntervalFreq;
    $.ajax({
        url: '/controlRealTempMeas',
        type: "POST",
        data: JSONObj,
        cache: false,
        //      contentType: 'application/json;charset=utf-8',
        dataType:'json',
        error: function () {
            //                   alert("失败！")
        },
        success: function () {
            //                alert("成功")
            var label = document.getElementById("showBtnContent");
            label.style.color="blue";
            //label.style.fontSize="25px";
            label.innerHTML= "正在动态读取当中,请勿关闭窗口！！！！";
        }
    });
    document.getElementById("startRecord").disabled=true;

       //window.onbeforeunload = onbeforeunload_handler;
    $("#startRecord").attr("disabled",true);
    $("#startRecord").css("pointer-events","none");
    $("#startRecord").css("background-color","#A9A9A9");
    //设置三天后自动停止监测
    // var t1=window.setTimeout(function() {
    //     var e = document.createEvent("MouseEvents");
    //     e.initEvent("click", true, true);
    //     document.getElementById("stopInsTemp").dispatchEvent(e);
    // }, 1000 *259200);




}
//点击开始监测触发关闭窗口事件。
function onbeforeunload_handler(){


    var s=confirm("关闭当前页面将停止温度监测，并自动保存图片和报表，确定将关闭此页面！");
    var k=0;
    if(s==true){

            var e = document.createEvent("MouseEvents");
             e.initEvent("click", true, true);
             document.getElementById("stopInsTemp").dispatchEvent(e);
             setTimeout(function(){},1000);

             k=1;
             //alert("报表保存成功！");

    }
    if(k==1){return "线程已关闭!";}
    else{return "线程仍在运行!";}

}
//点击结束试验触发关闭窗口事件。
function onbeforeunload_handler1(){
    return;
}
//动态读取图表
function getTempInfoDashClick() {
    //console.log("1111");
    getTempInfoDash();
    clearInterval(interval);
    //$("#showBtnContent").html("开始绘制曲线...");
    interval = setInterval(getTempInfoDash,10000);
}
//生成报表
function generateExcelReport(){

    //alert("nihao12");
    //console.log("nihao2");
    var sampleInfo1 = new Object();
    var sampleInfo2 = new Object();
    var cam_station_number1 = document.getElementsByName('chkRadio1')[0].checked;
    var cam_station_number2 = document.getElementsByName('chkRadio2')[0].checked;
    if(!cam_station_number1 && !cam_station_number2){
        alert("请选择仓位号!");
    }else{
        if(cam_station_number1 && cam_station_number2){
            sampleInfo1.room_number = document.getElementById("roomnumber").value;
           // sampleInfo1.sample_reporter_name = document.getElementById("sample_reporter_name1").value;
            sampleInfo1.sample_number = document.getElementById("sample_number1").value;
            sampleInfo1.sample_test_date = document.getElementById("sample_test_date1").value;
            sampleInfo1.tire_number = document.getElementById("tire_number1").value;
            sampleInfo1.sample_specification = document.getElementById("sample_specification1").value;
            sampleInfo1.sample_brand = document.getElementById("sample_brand1").value;
            sampleInfo1.sample_level = document.getElementById("sample_level1").value;
            sampleInfo1.sample_pattern = document.getElementById("sample_pattern1").value;
            sampleInfo1.sample_person = document.getElementById("sample_person1").value;
            sampleInfo1.sample_scheme = document.getElementById("sample_scheme1").value;
            sampleInfo1.cam_station_number = 1;

            sampleInfo2.room_number = document.getElementById("roomnumber").value;
            sampleInfo2.sample_number = document.getElementById("sample_number2").value;
            sampleInfo2.sample_test_date = document.getElementById("sample_test_date2").value;
            sampleInfo2.tire_number = document.getElementById("tire_number2").value;
            sampleInfo2.sample_specification = document.getElementById("sample_specification2").value;
            sampleInfo2.sample_brand = document.getElementById("sample_brand2").value;
            sampleInfo2.sample_level = document.getElementById("sample_level2").value;
            sampleInfo2.sample_pattern = document.getElementById("sample_pattern2").value;
            sampleInfo2.sample_person = document.getElementById("sample_person2").value;
            sampleInfo2.sample_scheme = document.getElementById("sample_scheme2").value;
            sampleInfo2.cam_station_number = 2;

            showStationCamInfo1(sampleInfo1.room_number,sampleInfo1.cam_station_number);
            showStationCamInfo2(sampleInfo2.room_number,sampleInfo2.cam_station_number);
        }
        if(cam_station_number1 && !cam_station_number2){
            sampleInfo1.room_number = document.getElementById("roomnumber").value;
            //sampleInfo1.sample_reporter_name = document.getElementById("sample_reporter_name1").value;
            sampleInfo1.sample_number = document.getElementById("sample_number1").value;
            sampleInfo1.sample_test_date = document.getElementById("sample_test_date1").value;
            sampleInfo1.tire_number = document.getElementById("tire_number1").value;
            sampleInfo1.sample_specification = document.getElementById("sample_specification1").value;
            sampleInfo1.sample_brand = document.getElementById("sample_brand1").value;
            sampleInfo1.sample_level = document.getElementById("sample_level1").value;
            sampleInfo1.sample_pattern = document.getElementById("sample_pattern1").value;
            sampleInfo1.sample_person = document.getElementById("sample_person1").value;
            sampleInfo1.sample_scheme = document.getElementById("sample_scheme1").value;
            sampleInfo1.cam_station_number = 1;
            dataCamInfo2 = null;
            sampleInfo2 = 0;
            showStationCamInfo1(sampleInfo1.room_number,sampleInfo1.cam_station_number);
        }

        if(!cam_station_number1 && cam_station_number2){
            sampleInfo1 = 0;
            sampleInfo2.room_number = document.getElementById("roomnumber").value;
            //sampleInfo2.sample_reporter_name = document.getElementById("sample_reporter_name2").value;
            sampleInfo2.sample_number = document.getElementById("sample_number2").value;
            sampleInfo2.sample_test_date = document.getElementById("sample_test_date2").value;
            sampleInfo2.tire_number = document.getElementById("tire_number2").value;
            sampleInfo2.sample_specification = document.getElementById("sample_specification2").value;
            sampleInfo2.sample_brand = document.getElementById("sample_brand2").value;
            sampleInfo2.sample_level = document.getElementById("sample_level2").value;
            sampleInfo2.sample_pattern = document.getElementById("sample_pattern2").value;
            sampleInfo2.sample_person = document.getElementById("sample_person2").value;
            sampleInfo2.sample_scheme = document.getElementById("sample_scheme2").value;
            sampleInfo2.cam_station_number = 2;

            dataCamInfo1 = null;
            showStationCamInfo2(sampleInfo2.room_number,sampleInfo2.cam_station_number);
        }
        var JSONObj = new Object();
        var dataCamInfoSearch1 = JSON.stringify(dataCamInfo1);
        JSONObj.dataCamInfo1 = dataCamInfoSearch1;
        var dataCamInfoSearch2 = JSON.stringify(dataCamInfo2);
        JSONObj.dataCamInfo2 = dataCamInfoSearch2;
        var sample1 = JSON.stringify(sampleInfo1);
        JSONObj.sampleInfo1 = sample1;
        var sample2 = JSON.stringify(sampleInfo2);
        JSONObj.sampleInfo2 = sample2;
        $.ajax({
            url: '/generateExcelReport',
            type: "POST",
            data: JSONObj,
            cache: false,
            //      contentType: 'application/json;charset=utf-8',
            dataType:'json',
            error: function () {
                //                   alert("失败！")
            },
            success: function () {
                //                alert("成功")
            }
        });
    }
    alert("图片、报表已保存！")
}
//下载生成的报表 1.判断仓位号  2.根据仓位号使用情况调用downloadFileform方法
function downloadFile() {
    var sampleInfo1 = new Object();
    var sampleInfo2 = new Object();
    var cam_station_number1 = document.getElementsByName('chkRadio1')[0].checked;
    var cam_station_number2 = document.getElementsByName('chkRadio2')[0].checked;
    if (!cam_station_number1 && !cam_station_number2) {
        alert("请选择仓位号!");
    } else {
        if (cam_station_number1 && cam_station_number2) {
            sampleInfo1.room_number = document.getElementById("roomnumber").value;
            // sampleInfo1.sample_reporter_name = document.getElementById("sample_reporter_name1").value;
            sampleInfo1.sample_number = document.getElementById("sample_number1").value;
            sampleInfo1.sample_test_date = document.getElementById("sample_test_date1").value;
            sampleInfo1.tire_number = document.getElementById("tire_number1").value;
            sampleInfo1.sample_specification = document.getElementById("sample_specification1").value;
            sampleInfo1.sample_brand = document.getElementById("sample_brand1").value;
            sampleInfo1.sample_level = document.getElementById("sample_level1").value;
            sampleInfo1.sample_pattern = document.getElementById("sample_pattern1").value;
            sampleInfo1.sample_person = document.getElementById("sample_person1").value;
            sampleInfo1.sample_scheme = document.getElementById("sample_scheme1").value;
            sampleInfo1.cam_station_number = 1;
            sampleInfo2.room_number = document.getElementById("roomnumber").value;
            sampleInfo2.sample_number = document.getElementById("sample_number2").value;
            sampleInfo2.sample_test_date = document.getElementById("sample_test_date2").value;
            sampleInfo2.tire_number = document.getElementById("tire_number2").value;
            sampleInfo2.sample_specification = document.getElementById("sample_specification2").value;
            sampleInfo2.sample_brand = document.getElementById("sample_brand2").value;
            sampleInfo2.sample_level = document.getElementById("sample_level2").value;
            sampleInfo2.sample_pattern = document.getElementById("sample_pattern2").value;
            sampleInfo2.sample_person = document.getElementById("sample_person2").value;
            sampleInfo2.sample_scheme = document.getElementById("sample_scheme2").value;
            sampleInfo2.cam_station_number = 2;
            downloadFileform(sampleInfo1);
        }
        if (cam_station_number1 && !cam_station_number2) {
            sampleInfo1.room_number = document.getElementById("roomnumber").value;
            //sampleInfo1.sample_reporter_name = document.getElementById("sample_reporter_name1").value;
            sampleInfo1.sample_number = document.getElementById("sample_number1").value;
            sampleInfo1.sample_test_date = document.getElementById("sample_test_date1").value;
            sampleInfo1.tire_number = document.getElementById("tire_number1").value;
            sampleInfo1.sample_specification = document.getElementById("sample_specification1").value;
            sampleInfo1.sample_brand = document.getElementById("sample_brand1").value;
            sampleInfo1.sample_level = document.getElementById("sample_level1").value;
            sampleInfo1.sample_pattern = document.getElementById("sample_pattern1").value;
            sampleInfo1.sample_person = document.getElementById("sample_person1").value;
            sampleInfo1.sample_scheme = document.getElementById("sample_scheme1").value;
            sampleInfo1.cam_station_number = 1;
            dataCamInfo2 = null;
            sampleInfo2 = 0;
            downloadFileform(sampleInfo1);
        }

        if (!cam_station_number1 && cam_station_number2) {
            sampleInfo1 = 0;
            sampleInfo2.room_number = document.getElementById("roomnumber").value;
            //sampleInfo2.sample_reporter_name = document.getElementById("sample_reporter_name2").value;
            sampleInfo2.sample_number = document.getElementById("sample_number2").value;
            sampleInfo2.sample_test_date = document.getElementById("sample_test_date2").value;
            sampleInfo2.tire_number = document.getElementById("tire_number2").value;
            sampleInfo2.sample_specification = document.getElementById("sample_specification2").value;
            sampleInfo2.sample_brand = document.getElementById("sample_brand2").value;
            sampleInfo2.sample_level = document.getElementById("sample_level2").value;
            sampleInfo2.sample_pattern = document.getElementById("sample_pattern2").value;
            sampleInfo2.sample_person = document.getElementById("sample_person2").value;
            sampleInfo2.sample_scheme = document.getElementById("sample_scheme2").value;
            sampleInfo2.cam_station_number = 2;
            dataCamInfo1 = null;
            downloadFileform(sampleInfo2);
        }
    }
}
//downloadFileform(功能：利用form将需要下载表格的信息传递到后台controller层中的downloadFIle)
function downloadFileform(sampleInfo) {
            //定义一个form表单
            var form = $("<form>");
            form.attr("style", "display:none");
            form.attr("target", "");
            form.attr("action", '/downloadFile');
            form.attr("method","POST");
            var sample_specification = $("<input name='sample_spec'>");
            //sample_specification.attr("type", "hidden");
            sample_specification.attr("value", sampleInfo.sample_specification);
            var sample_level = $("<input name='sample_lev'>");
            //sample_level.attr("type","hidden");
            sample_level.attr("value",sampleInfo.sample_level);
            var sample_pattern=$("<input name='sample_patt'>");
            //sample_pattern.attr("type","hidden");
            sample_pattern.attr("value",sampleInfo.sample_pattern);
            var sample_test_date=$("<input name='sample_tes'>");
            //sample_test_date.attr("type","hidden");
            sample_test_date.attr("value",sampleInfo.sample_test_date);
            var sample_number=$("<input name='sample_num'>");
            //sample_number.attr("type","hidden");
            sample_number.attr("value",sampleInfo.sample_number);
            var sample_person=$("<input name='sample_per'>");
            //sample_person.attr("type","hidden");
            sample_person.attr("value",sampleInfo.sample_person);
            var tire_number=$("<input name='tire_num'>");
           // tire_number.attr("type","hidden");
            tire_number.attr("value",sampleInfo.tire_number);
            var room_number=$("<input name='room_num'>");
            //room_number.attr("type","hidden");
            room_number.attr("value",sampleInfo.room_number);
            var cam_station_number=$("<input name='cam_station_num'>");
            //cam_station_number.attr("type","hidden");
            cam_station_number.attr("value",sampleInfo.cam_station_number);
            $("body").append(form);
            form.append(sample_specification);
            form.append(sample_level);
            form.append(sample_pattern);
            form.append(sample_test_date);
            form.append(sample_number);
            form.append(sample_person);
            form.append(tire_number);
            form.append(room_number);
            form.append(cam_station_number);
            form.submit();
            form.remove();
    }

////连接数据库，获取当前房间所有仓位摄像头信息

function showStationCamInfo1(room_number, cam_station_number) {
        $.ajax(
            {
                url: '/findRoomMethod',
                type: "post",
                data: {
                    "room_number": room_number,
                    "cam_station_number": cam_station_number
                },
                // contentType : 'application/json; charset=utf-8',
                dataType: 'json',
                cache: false,
                async: false,
                success: function (data) {
                    dataCamInfo1 = data;
                },
                error: function (msg) {
                    alert(msg);
                }
            });
        //  return dataCamInfox;
    }


////连接数据库，获取当前房间所有仓位摄像头信息

    function showStationCamInfo2(room_number, cam_station_number) {
        $.ajax(
            {
                url: '/findRoomMethod',
                type: "post",
                data: {
                    "room_number": room_number,
                    "cam_station_number": cam_station_number
                },
                dataType: 'json',
                cache: false,
                async: false,
                success: function (data) {
                    dataCamInfo2 = data;
                },
                error: function (msg) {
                    alert(msg);
                }
            });
    }


//获得机床当前运行状态
function getRoomstate() {
    $.ajax({
        url: '/get_Room_state',
        type: "post",
        dataType: 'json',
        success:function(data){
        //console.log(data[1]);
            for(var i=1;i<=9;i++){
                var one_workplace=$('#one_workplace'+i);
                var one_workplace_img=$('#one_workplace_img'+i);
                if(data[i]=="false")
                {
                one_workplace_img.attr("src","images/luntaijingzhi1.jpg");
                one_workplace.removeClass("fontsetting");
                one_workplace.addClass("fontsetting1");
                one_workplace.html(i+"#机床空闲");


                }
                else if(data[i]=="true"){

                    one_workplace_img.attr("src","images/luntaigai.gif");
                    one_workplace.removeClass("fontsetting1");
                    one_workplace.addClass("fontsetting");
                    one_workplace.html(i+"#机床监测中...");


                }


            }

        },
        error:function (msg) {

            console.log(msg);
        }



    })

}

//获得机床当前数据
function getCurrentRoomdata(roomnumber){
    $.ajax({
        url:'/get_current_room_data',
        type: "post",
        async: false,
        data: {
            "room_number": roomnumber
        },
        dataType: 'json',
        success: function (data) {
            var list=data;
            //alert(list[1].sample_running_state);
            var j=0;
            for(var i=0;i<list.length;i++){
                if(list[i].cam_station_number==1&&list[i].sample_running_state=="true")
                {
                    //alert("nihao")
                    $("#chkRadio1").prop("checked",true);
                    console.log( "wo"+$("#chkRadio1").prop("checked"))
                    $("#tire_number1").val(list[i].tire_number);
                    $("#sample_specification1").val(list[i].sample_specification);
                    $("#sample_load_rode1").val(list[i].sample_load_rode);
                    $("#sample_number1").val(list[i].sample_number);
                    $("#sample_test_date1").val(list[i].sample_test_date);
                    $("#sample_brand1").val(list[i].sample_brand);
                    $("#sample_level1").val(list[i].sample_level);
                    $("#sample_pattern1").val(list[i].sample_pattern);
                    $("#sample_person1").val(list[i].sample_person);
                    $("#sample_scheme1").val(list[i].sample_scheme);
                    $("#sample_object_name1").val(list[i].sample_object_name);
                    $("#first_point_frequency").val(list[i].first_point_frequency);
                    $("#first_time_interval").val(list[i].first_time_interval);
                    $("#second_point_frequency").val(list[i].second_point_frequency);
                    $("#second_time_interval").val(list[i].second_time_interval);
                    $("#third_point_frequency").val(list[i].third_point_frequency);
                    $("#startRecord").attr("disabled",true);
                    $("#startRecord").css("pointer-events","none");
                    $("#startRecord").css("background-color","#A9A9A9");
                    var label = document.getElementById("showBtnContent");
                    label.style.color="blue";
                    //label.style.fontSize="25px";
                    label.innerHTML= "正在动态读取当中,请勿关闭窗口！！！！";

                }
                if(list[i].cam_station_number==2&&list[i].sample_running_state=="true")
                {
                    //alert("nihao")
                    $("#chkRadio2").prop("checked",true);
                    $("#tire_number2").val(list[i].tire_number);
                    $("#sample_specification2").val(list[i].sample_specification);
                    $("#sample_load_rode2").val(list[i].sample_load_rode);
                    $("#sample_number2").val(list[i].sample_number);
                    $("#sample_test_date2").val(list[i].sample_test_date);
                    $("#sample_brand2").val(list[i].sample_brand);
                    $("#sample_level2").val(list[i].sample_level);
                    $("#sample_pattern2").val(list[i].sample_pattern);
                    $("#sample_person2").val(list[i].sample_person);
                    $("#sample_scheme2").val(list[i].sample_scheme);
                    $("#sample_object_name2").val(list[i].sample_object_name);
                    $("#first_point_frequency").val(list[i].first_point_frequency);
                    $("#first_time_interval").val(list[i].first_time_interval);
                    $("#second_point_frequency").val(list[i].second_point_frequency);
                    $("#second_time_interval").val(list[i].second_time_interval);
                    $("#third_point_frequency").val(list[i].third_point_frequency);
                    $("#startRecord").attr("disabled",true);
                    $("#startRecord").css("pointer-events","none");
                    $("#startRecord").css("background-color","#A9A9A9");
                    var label = document.getElementById("showBtnContent");
                    label.style.color="blue";
                    //label.style.fontSize="25px";
                    label.innerHTML= "正在动态读取当中,请勿关闭窗口！！！！";

                }
                // if(list[i].sample_running_state=="false"){
                //     j++;
                // }
            }

            // if(j==2){
            //     $("#chkRadio1").prop("checked",false);
            //     $("#tire_number1").val("");
            //     $("#sample_specification1").val("");
            //     $("#sample_load_rode1").val("");
            //     $("#sample_number1").val("");
            //     $("#sample_test_date1").val("");
            //     $("#sample_brand1").val("");
            //     $("#sample_level1").val("");
            //     $("#sample_pattern1").val("");
            //     $("#sample_person1").val("");
            //     $("#sample_scheme1").val("");
            //     $("#sample_object_name1").val("");
            //     $("#first_point_frequency").val("");
            //     $("#first_time_interval").val("");
            //     $("#second_point_frequency").val("");
            //     $("#second_time_interval").val("");
            //     $("#third_point_frequency").val("");
            //     $("#chkRadio2").prop("checked",false);
            //     $("#tire_number2").val("");
            //     $("#sample_specification2").val("");
            //     $("#sample_load_rode2").val("");
            //     $("#sample_number2").val("");
            //     $("#sample_test_date2").val("");
            //     $("#sample_brand2").val("");
            //     $("#sample_level2").val("");
            //     $("#sample_pattern2").val("");
            //     $("#sample_person2").val("");
            //     $("#sample_scheme2").val("");
            //     $("#sample_object_name2").val("");
            //     $("#first_point_frequency").val("");
            //     $("#first_time_interval").val("");
            //     $("#second_point_frequency").val("");
            //     $("#second_time_interval").val("");
            //     $("#third_point_frequency").val("");
            //     $("#startRecord").attr("disabled",false);
            //     $("#startRecord").css("pointer-events","auto");
            //     $("#startRecord").css({
            //         "padding": "5px 15px", "display": "inline-block", "border": "1px solid #ccc" , "background": "#fff" , "font-weight": "bold !important", "font-size": "11px", "color": "#333 !important",
            //         "-moz-border-radius": "2px", "-webkit-border-radius": "2px", "border-radius": "2px", "-moz-box-shadow": "0 1px 0 #fff",
            //         "-webkit-box-shadow": "0 1px 0 #fff", "box-shadow": "0 1px 0 #fff", "line-height": "21px"});
            //     $("#startRecord").hover(
            //         function(){
            //             $("#startRecord") .css({
            //                 "border": "1px solid #bbb", "background-color": "#f7f7f7", "-moz-box-shadow": "inset 0 1px 0 #fff",
            //                 "-webkit-box-shadow": "inset 0 1px 0 #fff", "box-shadow": "inset 0 1px 0 #fff"});
            //         },
            //         function(){
            //             $("#startRecord").css({
            //                 "padding": "5px 15px", "display": "inline-block", "border": "1px solid #ccc" , "background": "#fff" , "font-weight": "bold !important", "font-size": "11px", "color": "#333 !important",
            //                 "-moz-border-radius": "2px", "-webkit-border-radius": "2px", "border-radius": "2px", "-moz-box-shadow": "0 1px 0 #fff",
            //                 "-webkit-box-shadow": "0 1px 0 #fff", "box-shadow": "0 1px 0 #fff", "line-height": "21px"});
            //
            //         }
            //     )
            //
            // }


        },
        error: function (msg) {
            console.log(msg);
        }

    })


}
