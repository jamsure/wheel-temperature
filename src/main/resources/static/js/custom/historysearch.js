jQuery(document).ready(function () {

    jQuery('#overviewselect, input:checkbox').uniform();

    ///// DATE PICKER /////
    jQuery("#datepickfrom, #datepickto").datepicker();

    ///// SLIM SCROLL /////
    jQuery('#scroll1').slimscroll({
        color: '#666',
        size: '10px',
        width: 'auto',
        height: '175px'
    });

    ///// ACCORDION /////
    jQuery('#accordion').accordion({autoHeight: false});

    ///// SIMPLE CHART /////
    var LMaxTemp = [[0, 2], [1, 6], [2, 3], [3, 8], [4, 5], [5, 13], [6, 20]];
    var LMaxTemp1 = [[0, 2], [1, 6], [2, 3], [3, 8], [4, 5], [5, 13], [6, 20]];
    var LMaxTemp2 = [[0, 2], [1, 6], [2, 3], [3, 8], [4, 5], [5, 13], [6, 20]];
    var MMaxTemp = [[0, 5], [1, 4], [2, 4], [3, 1], [4, 9], [5, 10], [6, 20]];
    var MMaxTemp1 = [[0, 5], [1, 4], [2, 4], [3, 1], [4, 9], [5, 10], [6, 20]];
    var MMaxTemp2 = [[0, 5], [1, 4], [2, 4], [3, 1], [4, 9], [5, 10], [6, 20]];
    var RMaxTemp = [[0, 7], [1, 7], [2, 7], [3, 8], [4, 4], [5, 11], [6, 20]];
    var RMaxTemp1 = [[0, 7], [1, 7], [2, 7], [3, 8], [4, 4], [5, 11], [6, 20]];
    var RMaxTemp2 = [[0, 7], [1, 7], [2, 7], [3, 8], [4, 4], [5, 11], [6, 20]];

    $("#roomN").change(function () {
        $("#ccid").find("option").not(":first").remove();
        $("#sampleid").find("option").not(":first").remove();
        $("#sampleTypeN").find("option").not(":first").remove();
        var st = document.getElementById('sampleType');
        st[0].selected = true;
        var room_number = $("select[name=rid]").val();
        console.log("aaaaa");
        $.ajax({
            url: '/getCamIP',
            type: "post",
            data: {
                "room_number": room_number
            },
            cache: false,
            error: function () {
                alert("你所查询的内容不存在！")
            },
            success: function (data) {
                if (data.length != 0) {

                    for (var i = 0; i < data.length; i++) {
                        var option = "<option value=\"" + data[i].cam_ip + "\">" + data[i].cam_ip + "-" + data[i].cam_position +"-"+data[i].cam_station_number+ "</option>";  //动态添加数据
                        $("select[name=campId]").append(option);
                    }

                }
            }
        });
    });

    $("#ccid").change(function () {

        $("#sampleid").find("option").not(":first").remove();
        $("#sampleType").find("option").not(":first").remove();
        var room_number = $("select[name=rid]").val();
        var cam_ip = $("select[name=campId]").val();
        $.ajax({
            url: '/getSampleName',
            type: "post",
            contentType: "application/x-www-form-urlencoded; charset=utf-8",
            data: {
                "room_number": room_number,
                "cam_ip": cam_ip
            },
            cache: false,
            dataType: "json",
            error: function () {
                alert("你所查询的内容不存在！")
            },
            success: function (data1) {
                var data = data1.list;
                if (data.length != 0) {
                    for (var i = 0; i < data.length; i++) {
                        var option = "<option value=\"" + data[i] + "\">" + data[i] + "</option>";  //动态添加数据
                        $("select[name=tire_number]").append(option);
                    }
                }
            }
        });
    });

    $("#sampleid").change(function () {
        $("#sampleType").find("option").not(":first").remove();
        var tire_number = $("select[name=tire_number]").val();
        var cam_ip = $("select[name=campId]").val();
        $.ajax({
            url: '/getSampleType',
            type: "post",
            contentType: "application/x-www-form-urlencoded; charset=utf-8",
            data: {
                "tire_number": tire_number,
                "cam_ip": cam_ip
            },
            cache: false,
            dataType: "json",
            error: function () {
                alert("你所查询的内容不存在！")
            },
            success: function (data) {
                if (data.length != 0) {
                    for (var i = 0; i < data.length; i++) {
                        if (data[i] == 0) {
                            var option = "<option value=\"" + data[i] + "\">" + "点" + "</option>";  //动态添加数据
                        } else if (data[i] == 1) {
                            var option = "<option value=\"" + data[i] + "\">" + "框" + "</option>";
                        } else if (data[i] == 2) {
                            var option = "<option value=\"" + data[i] + "\">" + "线" + "</option>";
                        }
                        $("select[name=sample_type]").append(option);
                    }
                }
            }
        });
    });


    //点击html页面单元格获取数据并跳转查询
    $("#tb").on('dblclick', function () {
        var dataFromHtml = new Array();
        var content;
        var rowNumber;
        var cellNumber;
        var tabs = document.getElementById("tb");
        var td = event.srcElement; // 通过event.srcelement 获取激活事件的对象 td
        content = td.innerHTML;
        if(content.length != 0){
            rowNumber = td.parentElement.rowIndex;
            cellNumber = td.cellIndex;
            console.log("行号：" + rowNumber + "，内容：" + td.innerHTML);
            if(cellNumber>=0 && cellNumber <9){
                for(var m=0;m<4;m++){
                    dataFromHtml[m] = tabs.rows[rowNumber].cells[m+5].innerHTML;
                }
                SearchTemp(dataFromHtml);
            }

        }

    });

    //单击下载报表
    $("#tb").on('click', function () {
        var dataFromHtml = new Array();
        var content;
        var rowNumber;
        var cellNumber;
        var tabs = document.getElementById("tb");
        var td = event.srcElement; // 通过event.srcelement 获取激活事件的对象 td
        content = td.innerHTML;
        if(content.length != 0){
            rowNumber = td.parentElement.parentElement.rowIndex;
            cellNumber = td.parentElement.cellIndex;
            console.log("行号：" + rowNumber + "列号："+ cellNumber+ "，内容：" + td.innerHTML);
            if(cellNumber == 9){
                for(var m=0;m<9;m++){
                    dataFromHtml[m] = tabs.rows[rowNumber].cells[m].innerHTML;
                }
                downloadExcel(dataFromHtml);
            }

        }

    });

    function DownloadExcel(dataFromHtml){
        //定义一个form表单
        var form = $("<form>");
        form.attr("style", "display:none");
        form.attr("target", "");
        form.attr("action", '/downloadFile');
        form.attr("method","POST");
        var sample_specification=$("<input name='sample_spec'>");
        sample_specification.attr("value", dataFromHtml[0]);
        var sample_level = $("<input name='sample_lev'>");
        sample_level.attr("value",dataFromHtml[1]);
        var sample_pattern=$("<input name='sample_patt'>");
        sample_pattern.attr("value",dataFromHtml[2]);
        var sample_number=$("<input name='sample_num'>");
        sample_number.attr("value",dataFromHtml[3]);
        var sample_person=$("<input name='sample_per'>");
        sample_person.attr("value",dataFromHtml[4]);
        var sample_test_date=$("<input name='sample_tes'>");
        sample_test_date.attr("value",dataFromHtml[5]);
        var tire_number=$("<input name='tire_num'>");
        tire_number.attr("value",dataFromHtml[6]);
        var room_number=$("<input name='room_num'>");
        room_number.attr("value",dataFromHtml[7]);
        var cam_station_number=$("<input name='cam_station_num'>");
        cam_station_number.attr("value",dataFromHtml[8]);
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
        //     //定义一个form表单
//     var form = $("<form>");
//     form.attr("style", "display:none");
//     form.attr("target", "");
//     form.attr("action", '/downloadFile');
//     form.attr("method","POST");
//     var sample_specification = $("<input name='sample_spec'>");
//     //sample_specification.attr("type", "hidden");
//     sample_specification.attr("value", $("#sample_specification").val());
//     var sample_level = $("<input name='sample_lev'>");
//     //sample_level.attr("type","hidden");
//     sample_level.attr("value",$("#sample_level").val());
//     var sample_pattern=$("<input name='sample_patt'>");
//     //sample_pattern.attr("type","hidden");
//     sample_pattern.attr("value",$("#sample_pattern").val());
//     var sample_test_date=$("<input name='sample_tes'>");
//     //sample_test_date.attr("type","hidden");
//     sample_test_date.attr("value",$("#sample_test_date").val());
//     var sample_number=$("<input name='sample_num'>");
//     //sample_number.attr("type","hidden");
//     sample_number.attr("value",$("#sample_number").val());
//     var sample_person=$("<input name='sample_per'>");
//     //sample_person.attr("type","hidden");
//     sample_person.attr("value",$("#sample_person").val());
//     var tire_number=$("<input name='tire_num'>");
//     // tire_number.attr("type","hidden");
//     tire_number.attr("value",$("#tire_number").val());
//     var room_number=$("<input name='room_num'>");
//     //room_number.attr("type","hidden");
//     room_number.attr("value",$("#roomN").val());
//     //console.log(document.getElementById("roomN").innerHTML);
//     var cam_station_number=$("<input name='cam_station_num'>");
//     //cam_station_number.attr("type","hidden");
//     cam_station_number.attr("value",$("#cam_station_number").val());
//     $("body").append(form);
//     form.append(sample_specification);
//     form.append(sample_level);
//     form.append(sample_pattern);
//     form.append(sample_test_date);
//     form.append(sample_number);
//     form.append(sample_person);
//     form.append(tire_number);
//     form.append(room_number);
//     form.append(cam_station_number);
//     form.submit();
//     form.remove()
    }

    function SearchTemp(dataFromHtml) {
        var sample_test_date = dataFromHtml[0];
        var tire_number = dataFromHtml[1];
        var room_number = dataFromHtml[2];
        var cam_station_number = dataFromHtml[3];
        var stopTime = $("#stoptime").val();
        if (stopTime.length == 0) {
            stopTime = 0;
        }
        $.ajax({
            url: '/getDiffCamTempInfo',
            type: "post",
            contentType: "application/x-www-form-urlencoded; charset=utf-8",
            data: {
                "room_number": room_number,
                "cam_station_number": cam_station_number,
                "tire_number": tire_number,
                "sample_test_date": sample_test_date,
                "stopTime": stopTime
            },
            cache: false,
            dataType: "json",
            error: function () {
                alert("你所查询的内容不存在！")
            },
            success: function (data1) {
                var dataL = data1.listL;
                var dataM = data1.listM;
                var dataR = data1.listR;
                var listIP = data1.listIP;
                // document.getElementById("CaptureImage").src = data1.imageLink;
                console.log(data1);
                if (data1.length != 0) {
                    var getOneLMaxTemp = [];
                    var getOneMMaxTemp = [];
                    var getOneRMaxTemp = [];
                    if (dataL != null){
                        for (var i = 0;i<dataL.length;i++)
                        {
                            var list1 = [];
                            list1[0] = dataL[i].plp_timediff;
                            list1[1] = dataL[i].plp_fMaxTemperature ;
                            getOneLMaxTemp.push(list1);
                        }
                        // console.log(getOneLMaxTemp);
                    }
                    if ( dataM != null ){
                        for (var i = 0;i<dataM.length;i++)
                        {
                            var list2 = [];
                            list2[0] = dataM[i].plp_timediff;
                            list2[1] = dataM[i].plp_fMaxTemperature ;
                            getOneMMaxTemp.push(list2);
                        }
                    }
                    if (dataR != null){
                        for (var i = 0;i<dataR.length;i++)
                        {
                            var list3 = [];
                            list3[0] = dataR[i].plp_timediff;
                            list3[1] = dataR[i].plp_fMaxTemperature ;
                            getOneRMaxTemp.push(list3);
                        }
                    }
                    // starttime = data[0].plp_fixStartTime;
                    // $("#starttime").val(starttime);
                    // endtime = data[i - 1].plp_fixStartTime;
                    // $("#stoptime").val(endtime);
                    setMaxTemperature(getOneLMaxTemp, getOneMMaxTemp, getOneRMaxTemp,dataFromHtml,listIP);




                    // setMaxTemperature(getMaxTemp, getMinTemp, getAveTemp);
                }

            }
        });
    }
    //设置折线图折线默认颜色
    Highcharts.setOptions({
        colors:['#ED561B','#058DC7','#50B432']
    });
    //绘制折线图
    // function setMaxTemperature(getOneLMaxTemp, getOneMMaxTemp, getOneRMaxTemp,dataFromHtml,listIP) {
    //     LMaxTemp = getOneLMaxTemp;
    //     MMaxTemp = getOneMMaxTemp;
    //     RMaxTemp = getOneRMaxTemp;
    //     var cam_ipL,cam_ipM,cam_ipR;
    //     for(var i=0;i<listIP.length;i++){
    //         switch (listIP[i].cam_position) {
    //             case "L": cam_ipL = listIP[i].cam_ip; break;
    //             case "M": cam_ipM = listIP[i].cam_ip; break;
    //             case "R": cam_ipR = listIP[i].cam_ip; break;
    //         }
    //     }
    //
    //     var plot = Highcharts.chart('chartplace',{
    //         chart:{
    //             backgroundColor:'#FCFCFC',
    //             type:'line',
    //             plotBorderWidth:1,
    //             spacingRight:30
    //         },
    //         title:{
    //             text:'轮胎温度折线图'
    //         },
    //         series:[{
    //             name:'胎内子口温度',
    //             data:LMaxTemp
    //         },{
    //             name:'胎内肩部温度',
    //             data:LMaxTemp1
    //         },{
    //             name:'胎内侧部中间温度',
    //             data:LMaxTemp2
    //         },{
    //             name:'胎冠区域温度',
    //             data:MMaxTemp
    //         },{
    //             name:'胎冠中间温度',
    //             data:MMaxTemp1
    //         },{
    //             name:'胎冠肩部温度',
    //             data:MMaxTemp2
    //         },{
    //             name:'胎外子口温度',
    //             data:RMaxTemp
    //         },{
    //             name:'胎外肩部温度',
    //             data:RMaxTemp1
    //         },{
    //             name:'胎外侧部中间温度',
    //             data:RMaxTemp2
    //         }],
    //         legend: {
    //             layout: 'vertical',
    //             align: 'right',
    //             verticalAlign: 'middle'
    //         },
    //         yAxis:{
    //             min:0,
    //             max:120,
    //             tickInterval:10, //刻度值
    //             title:{
    //                 text:'温度值'
    //             },
    //             gridLineColor:'#CCCCCC',
    //             gridLineWidth:1
    //         },
    //         xAxis:{
    //             gridLineColor:'#CCCCCC',
    //             gridLineWidth:0
    //         },
    //         lines: {
    //             show: true,
    //             fill: true,
    //             fillColor: {colors: [{opacity: 0.05}, {opacity: 0.15}, {opacity: 0.25}]}
    //         },
    //         credits:{ //去除右下角标识
    //             enabled:false
    //         },
    //         grid: {
    //             hoverable: true,
    //             clickable: true,
    //             borderColor: '#ccc',
    //             borderWidth: 1,
    //             labelMargin: 10
    //         },
    //         plotOptions:{
    //             allowPointSelect: true,  //允许使用鼠标选中数据点
    //             series:{
    //                 cursor:'pointer', //鼠标滑过数据列时鼠标的形状
    //                 events:{
    //                     click:function(e){
    //                         // alert(e.point.category);
    //                         //  alert(e.point.x.toFixed(2));
    //                         if (e) {
    //                             // var sample_test_date = dataFromHtml[0];
    //                             // var cam_ip = dataFromHtml[4];
    //                             // console.log(e.name);
    //                             console.log(this.name);
    //                             if(this.name == "胎内最高温度"){
    //                                 dataFromHtml[4] = cam_ipL;
    //                             }else if(this.name == "胎冠最高温度"){
    //                                 dataFromHtml[4] = cam_ipM;
    //                             }else if(this.name == "胎外最高温度"){
    //                                 dataFromHtml[4] = cam_ipR;
    //                             }
    //                             getImage(e.point.x.toFixed(2), dataFromHtml);
    //                         }
    //                     }
    //                 }
    //
    //             }
    //         },
    //         exporting:{
    //             sourceWidth: 1000,
    //             sourceHeight: 400,
    //             enabled:false
    //         }
    //
    //
    //     });
    // }

    //默认折线图
    var plot = Highcharts.chart('chartplace',{
        chart:{
            backgroundColor:'#FCFCFC',
            type:'line',
            //borderWidth:1,
            plotBorderWidth:1,
            spacingRight:30
        },
        title:{
            text:'轮胎温度折线图'
        },
        series:[{
            name:'胎内子口温度',
            data:LMaxTemp
        },{
            name:'胎内肩部温度',
            data:LMaxTemp1
        },{
            name:'胎内侧部中间温度',
            data:LMaxTemp2
        },{
            name:'胎冠区域温度',
            data:MMaxTemp
        },{
            name:'胎冠中间温度',
            data:MMaxTemp1
        },{
            name:'胎冠肩部温度',
            data:MMaxTemp2
        },{
            name:'胎外子口温度',
            data:RMaxTemp
        },{
            name:'胎外肩部温度',
            data:RMaxTemp1
        },{
            name:'胎外侧部中间温度',
            data:RMaxTemp2
        }],
        legend: {
            layout: 'vertical',
            align: 'right',
            verticalAlign: 'middle'
        },
        yAxis:{
            min:0,
            max:120,
            // tickPixelInterval:30, //调整y轴刻度的间距
            tickInterval:10, //刻度值
            title:{
                text:'温度值'
            },
            gridLineColor:'#CCCCCC',
            gridLineWidth:1
        },
        xAxis:{
            gridLineColor:'#CCCCCC',
            gridLineWidth:0
        },
        lines: {
            show: true,
            fill: true,
            fillColor: {colors: [{opacity: 0.05}, {opacity: 0.15}, {opacity: 0.25}]}
        },
        credits:{
            enabled:false
        },
        grid: {
            hoverable: true,
            clickable: true,
            borderColor: '#ccc',
            borderWidth: 1,
            labelMargin: 10
        },
        plotOptions:{
            allowPointSelect: true,
            series:{
                cursor:'pointer', //鼠标滑过数据列时鼠标的形状
                events:{
                    click:function(e){
                        // alert(this.name)
                    }
                }
            }
        },
        exporting:{
            sourceWidth: 1000,
            sourceHeight: 400,
            enabled:false
        }

    });


    //get the link of the image
    function getImage(timeDiff, dataFromHtml) {
        var sample_test_date = dataFromHtml[0];
        var tire_number = dataFromHtml[1];
        var room_number = dataFromHtml[2];
        var cam_station_number = dataFromHtml[3];
        var cam_ip = dataFromHtml[4];
        var timeDiff = parseInt(timeDiff);
        console.log(dataFromHtml);

        $.ajax({
            url: '/getImage',
            type: "post",
            contentType: "application/x-www-form-urlencoded; charset=utf-8",
            data: {
                "sample_test_date": sample_test_date,
                "tire_number": tire_number,
                "room_number": room_number,
                "cam_station_number": cam_station_number,
                "timeDiff": timeDiff,
                "cam_ip": cam_ip
            },
            cache: false,
            dataType: "text",
            error: function () {
                alert("你所查询的内容不存在！")
            },
            success: function (data) {
                document.getElementById("CaptureImage").src=data;
                console.log(data)
            }
        });
    }




    ///// SWITCHING LIST FROM 3 COLUMNS TO 2 COLUMN LIST /////
    function rearrangeShortcuts() {
        if (jQuery(window).width() < 430) {
            if (jQuery('.shortcuts li.one_half').length == 0) {
                var count = 0;
                jQuery('.shortcuts li').removeAttr('class');
                jQuery('.shortcuts li').each(function () {
                    jQuery(this).addClass('one_half');
                    if (count % 2 != 0) jQuery(this).addClass('last');
                    count++;
                });
            }
        } else {
            if (jQuery('.shortcuts li.one_half').length > 0) {
                jQuery('.shortcuts li').removeAttr('class');
            }
        }
    }

    rearrangeShortcuts();

    ///// ON RESIZE WINDOW /////
    jQuery(window).resize(function () {
        rearrangeShortcuts();
    });


});


function ChooseFile() {
    // event.stopPropagation();
    $("#camIP").find("option").not(":first").remove();
    $("#ruleID").find("option").not(":first").remove();
    var fileInput = document.getElementById('chooseFile');
    var fileName;
    // fileInput.addEventListener('change', function() {
    // 获取File引用:
    var file = fileInput.files[0];
    fileName = file.name;
    console.log(fileName);
    // 读取文件:
    var reader = new FileReader();
    reader.onload = function(e) {
        var data = e.target.result; // 'data:image/jpeg;base64,/9j/4AAQSk...(base64编码)...}'
    };
    // 以DataURL的形式读取文件:
    reader.readAsDataURL(file);
    // });
    $.ajax({
        url: '/ChooseFile',
        type: "post",
        contentType: "application/x-www-form-urlencoded; charset=utf-8",
        data: {
            "fileName":fileName
        },
        cache: false,
        dataType: "json",
        error: function () {
            alert("你所选择的文件不存在！")
        },
        success: function (data) {
            if (data.length != 0) {
                $("#room_number").val(data.room_number);
                $("#cam_station_number").val(data.cam_station_number);
                $("#sample_specification").val(data.sample_specification);
                $("#sample_level").val(data.sample_level);
                $("#sample_pattern").val(data.sample_pattern);
                $("#tire_number").val(data.tire_number);
                $("#sample_test_date").val(data.sample_test_date);
                for (var i = 0; i < data.cam_IPList.length; i++) {
                    var option = "<option value=\"" + data.cam_IPList[i] + "\">" + data.cam_IPList[i] + "</option>";  //动态添加数据
                    $("select[name=camIP]").append(option);
                }
            }
        }
    });
}

// function downloadExcel(){
//     //定义一个form表单
//     var form = $("<form>");
//     form.attr("style", "display:none");
//     form.attr("target", "");
//     form.attr("action", '/downloadFile');
//     form.attr("method","POST");
//     var sample_specification = $("<input name='sample_spec'>");
//     //sample_specification.attr("type", "hidden");
//     sample_specification.attr("value", $("#sample_specification").val());
//     var sample_level = $("<input name='sample_lev'>");
//     //sample_level.attr("type","hidden");
//     sample_level.attr("value",$("#sample_level").val());
//     var sample_pattern=$("<input name='sample_patt'>");
//     //sample_pattern.attr("type","hidden");
//     sample_pattern.attr("value",$("#sample_pattern").val());
//     var sample_test_date=$("<input name='sample_tes'>");
//     //sample_test_date.attr("type","hidden");
//     sample_test_date.attr("value",$("#sample_test_date").val());
//     var sample_number=$("<input name='sample_num'>");
//     //sample_number.attr("type","hidden");
//     sample_number.attr("value",$("#sample_number").val());
//     var sample_person=$("<input name='sample_per'>");
//     //sample_person.attr("type","hidden");
//     sample_person.attr("value",$("#sample_person").val());
//     var tire_number=$("<input name='tire_num'>");
//     // tire_number.attr("type","hidden");
//     tire_number.attr("value",$("#tire_number").val());
//     var room_number=$("<input name='room_num'>");
//     //room_number.attr("type","hidden");
//     room_number.attr("value",$("#roomN").val());
//     //console.log(document.getElementById("roomN").innerHTML);
//     var cam_station_number=$("<input name='cam_station_num'>");
//     //cam_station_number.attr("type","hidden");
//     cam_station_number.attr("value",$("#cam_station_number").val());
//     $("body").append(form);
//     form.append(sample_specification);
//     form.append(sample_level);
//     form.append(sample_pattern);
//     form.append(sample_test_date);
//     form.append(sample_number);
//     form.append(sample_person);
//     form.append(tire_number);
//     form.append(room_number);
//     form.append(cam_station_number);
//     form.submit();
//     form.remove();
// }

$("#camIP").change(function () {
    $("#ruleID").find("option").not(":first").remove();
    var room_number = $("#room_number").val();
    var cam_station_number = $("#cam_station_number").val();
    var tire_number = $("#tire_number").val();
    var sample_test_date = $("#sample_test_date").val();
    var cam_ip = $("#camIP").val();
    $.ajax({
        url: '/getRuleID',
        type: "post",
        contentType: "application/x-www-form-urlencoded; charset=utf-8",
        data: {
            "tire_number": tire_number,
            "cam_station_number": cam_station_number,
            "room_number": room_number,
            "sample_test_date": sample_test_date,
            "cam_ip": cam_ip
        },
        cache: false,
        dataType: "json",
        error: function () {
            alert("你所查询的内容不存在！")
        },
        success: function (data) {
            if (data.length != 0) {
                $("#starttime").val(data.fixStartTime);
                for (var i = 0; i < data.list.length; i++) {
                    var option = "<option value=\"" + data.list[i] + "\">" +data.list[i] + "</option>";  //动态添加数据
                    $("select[name=ruleID]").append(option);
                }
            }
        }
    });
});

function getImage(timeDiff, dataFromHtml) {
    var sample_test_date = dataFromHtml[0];
    var tire_number = dataFromHtml[1];
    var room_number = dataFromHtml[2];
    var cam_station_number = dataFromHtml[3];
    var cam_ip = dataFromHtml[4];
    var timeDiff = parseInt(timeDiff);
    console.log(dataFromHtml);

    $.ajax({
        url: '/getImage',
        type: "post",
        contentType: "application/x-www-form-urlencoded; charset=utf-8",
        data: {
            "sample_test_date": sample_test_date,
            "tire_number": tire_number,
            "room_number": room_number,
            "cam_station_number": cam_station_number,
            "timeDiff": timeDiff,
            "cam_ip": cam_ip
        },
        cache: false,
        dataType: "text",
        error: function () {
            alert("你所查询的内容不存在！")
        },
        success: function (data) {
            document.getElementById("CaptureImage").src=data;
            console.log(data)
        }
    });

}

//模糊查询
function setMaxTemperature(getOneLMaxTemp,getOneLMaxTemp1,getOneLMaxTemp2, getOneMMaxTemp,getOneMMaxTemp1,getOneMMaxTemp2, getOneRMaxTemp,getOneRMaxTemp1,getOneRMaxTemp2, dataFromHtml , listIP) {
    LMaxTemp = getOneLMaxTemp;
    LMaxTemp1=getOneLMaxTemp1;
    LMaxTemp2=getOneLMaxTemp2;
    MMaxTemp = getOneMMaxTemp;
    MMaxTemp1 = getOneMMaxTemp1;
    MMaxTemp2 = getOneMMaxTemp2;
    RMaxTemp = getOneRMaxTemp;
    RMaxTemp1 = getOneRMaxTemp1;
    RMaxTemp2 = getOneRMaxTemp2;
    var cam_ipL,cam_ipM,cam_ipR;
    for(var i=0;i<listIP.length;i++){
        switch (listIP[i].cam_position) {
            case "L": cam_ipL = listIP[i].cam_ip; break;
            case "M": cam_ipM = listIP[i].cam_ip; break;
            case "R": cam_ipR = listIP[i].cam_ip; break;
        }
    }

    var plot = Highcharts.chart('chartplace',{
        chart:{
            backgroundColor:'#FCFCFC',
            type:'line',
            plotBorderWidth:1,
            spacingRight:30
        },
        title:{
            text:'轮胎温度折线图'
        },
        series:[{
            name:'胎内子口温度',
            data:LMaxTemp
        },{
            name:'胎内肩部温度',
            data:LMaxTemp1
        },{
            name:'胎内侧部中间温度',
            data:LMaxTemp2
        },{
            name:'胎冠区域温度',
            data:MMaxTemp
        },{
            name:'胎冠中间温度',
            data:MMaxTemp1
        },{
            name:'胎冠肩部温度',
            data:MMaxTemp2
        },{
            name:'胎外子口温度',
            data:RMaxTemp
        },{
            name:'胎外肩部温度',
            data:RMaxTemp1
        },{
            name:'胎外侧部中间温度',
            data:RMaxTemp2
        }],
        legend: {
            layout: 'vertical',
            align: 'right',
            verticalAlign: 'middle'
        },
        yAxis:{
            min:0,
            max:120,
            tickInterval:10, //刻度值
            title:{
                text:'温度值'
            },
            gridLineColor:'#CCCCCC',
            gridLineWidth:1
        },
        xAxis:{
            gridLineColor:'#CCCCCC',
            gridLineWidth:0
        },
        lines: {
            show: true,
            fill: true,
            fillColor: {colors: [{opacity: 0.05}, {opacity: 0.15}, {opacity: 0.25}]}
        },
        credits:{ //去除右下角标识
            enabled:false
        },
        grid: {
            hoverable: true,
            clickable: true,
            borderColor: '#ccc',
            borderWidth: 1,
            labelMargin: 10
        },
        plotOptions:{
            allowPointSelect: true,  //允许使用鼠标选中数据点
            series:{
                cursor:'pointer', //鼠标滑过数据列时鼠标的形状
                events:{
                    click:function(e){
                        // alert(e.point.category);
                        //  alert(e.point.x.toFixed(2));
                        if (e) {
                            // var sample_test_date = dataFromHtml[0];
                            // var cam_ip = dataFromHtml[4];
                            // console.log(e.name);
                            console.log(this.name);
                            if(this.name == "胎内子口温度"||this.name=="胎内肩部温度"||this.name=="胎内侧部中间温度"){
                                dataFromHtml[4] = cam_ipL;
                            } else if(this.name =="胎冠区域温度"||this.name =="胎冠中间温度"||this.name =="胎冠肩部温度"){
                                dataFromHtml[4] = cam_ipM;
                            }else if(this.name =="胎外子口温度"||this.name =="胎外肩部温度"||this.name =="胎外侧部中间温度"){
                                dataFromHtml[4] = cam_ipR;
                            }
                            getImage(e.point.x.toFixed(2), dataFromHtml);
                        }
                    }
                }

            }
        },
        exporting:{
            sourceWidth: 1000,
            sourceHeight: 400,
            enabled:false
        }


    });

}

function DownloadExcel(dataFromHtml) {
    var form = $("<form>");
    form.attr("style", "display:none");
    form.attr("target", "");
    form.attr("action", '/downloadFile');
    form.attr("method","POST");
    var sample_specification=$("<input name='sample_spec'>");
    sample_specification.attr("value", dataFromHtml[0]);
    var sample_level = $("<input name='sample_lev'>");
    sample_level.attr("value",dataFromHtml[1]);
    var sample_pattern=$("<input name='sample_patt'>");
    sample_pattern.attr("value",dataFromHtml[2]);
    var sample_number=$("<input name='sample_num'>");
    sample_number.attr("value",dataFromHtml[3]);
    var sample_person=$("<input name='sample_per'>");
    sample_person.attr("value",dataFromHtml[4]);
    var sample_test_date=$("<input name='sample_tes'>");
    sample_test_date.attr("value",dataFromHtml[5]);
    var tire_number=$("<input name='tire_num'>");
    tire_number.attr("value",dataFromHtml[6]);
    var room_number=$("<input name='room_num'>");
    room_number.attr("value",dataFromHtml[7]);
    var cam_station_number=$("<input name='cam_station_num'>");
    cam_station_number.attr("value",dataFromHtml[8]);
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

// var sampleData;
function queryData() {
    var room_number = $("#room_number").val();
    var cam_station_number = $("#cam_station_number").val();
    var tire_number = $("#tire_number").val();
    var sample_specification = $("#sample_specification").val();
    var sample_level = $("#sample_level").val();
    var sample_pattern = $("#sample_pattern").val();
    var sample_test_date = $("#sample_test_date").val();
    var sample_number = $("#sample_number").val();
    var sample_person = $("#sample_person").val();
    var sample_object_name=$("#sample_object_name").val();
    var stopTime = $("#stoptime").val();
    layui.use(['table','layer'], function(){
        var table = layui.table;
        var layer=layui.layer;
        table.render({
            elem: '#test'
            ,url:'/queryData'
            ,method:'post'
            ,cellMinWidth: 80
            ,limit:15
            ,limits:[15,30,45]
            ,request: {
                pageName: 'curr'
                ,limitName: 'nums'
            }
            ,where:{
                "room_number":room_number,
                "cam_station_number":cam_station_number,
                "tire_number":tire_number,
                "stopTime":stopTime,
                "sample_specification":sample_specification,
                "sample_level":sample_level,
                "sample_pattern":sample_pattern,
                "sample_number":sample_number,
                "sample_person":sample_person,
                "sample_test_date":sample_test_date,
                "sample_object_name":sample_object_name
            }
            ,toolbar: '#toolbarDemo'
            ,title: '用户数据表'
            ,cols: [[
                {type: 'checkbox' }
                ,{field:'sample_object_name',title:'项目名称'}
                ,{field:'sample_specification', title:'规格', unresize: true, sort: true}
                ,{field:'sample_level', title:'层级' }
                ,{field:'sample_pattern', title:'花纹'}
                ,{field:'sample_number', title:'试制编号', sort: true}
                ,{field:'sample_person', title:'送样人' }
                ,{field:'sample_test_date', title:'试验日期',sort: true}
                ,{field:'tire_number', title:'轮胎工号',  sort: true}
                ,{field:'room_number', title:'机床号',sort: true}
                ,{field:'cam_station_number', title:'工位号', sort: true}
                ,{fixed: 'right', width:150, align:'center', toolbar: '#barDemo'}

            ]]
            ,page: true
        });
        table.on('toolbar(test)', function(obj){
            var checkStatus = table.checkStatus(obj.config.id);
            switch(obj.event){
                case 'getCheckData':
                    if(checkStatus.data.length==1)
                    {
                        var data = checkStatus.data;
                        var stopTime = $("#stoptime").val();
                        if (stopTime.length == 0) {
                            stopTime = 0;
                        }
                        data[0].stopTime=stopTime;
                        var dataFromHtml = new Array();
                       dataFromHtml[0]=data[0].sample_test_date;
                       dataFromHtml[1]=data[0].tire_number;
                       dataFromHtml[2]=data[0].room_number;
                       dataFromHtml[3]=data[0].cam_station_number;
                       //var viewnumber=dataFromHtml[2]+"-"+dataFromHtml[3]+" "+dataFromHtml[1]+"号";
                       //console.log(viewnumber);
                        $.ajax({
                            url: '/getDiffCamTempInfo',
                            type: "post",
                            contentType: "application/x-www-form-urlencoded; charset=utf-8",
                            data: data[0],
                            cache: false,
                            dataType: "json",
                            error: function () {
                                alert("你所查询的内容不存在！")
                            },
                            success: function (data1) {
                                //alert(JSON.stringify(data1));
                                var dataL = data1.listL;
                                var dataL1=data1.listL1;
                                var dataL2=data1.listL2;
                                var dataM = data1.listM;
                                var dataM1=data1.listM1;
                                var dataM2=data1.listM2;
                                var dataR = data1.listR;
                                var dataR1=data1.listR1;
                                var dataR2=data1.listR2;
                                var listIP = data1.listIP;
                                // document.getElementById("CaptureImage1").src=data1.imageLink;
                                if (data1.length != 0) {
                                    var getOneLMaxTemp = [];
                                    var getOneLMaxTemp1 = [];
                                    var getOneLMaxTemp2 = [];
                                    var getOneMMaxTemp = [];
                                    var getOneMMaxTemp1 =[];
                                    var getOneMMaxTemp2 = [];
                                    var getOneRMaxTemp = [];
                                    var getOneRMaxTemp1=[];
                                    var getOneRMaxTemp2=[];
                                    if (dataL != null){
                                        for (var i = 0;i<dataL.length;i++)
                                        {
                                            var list1 = [];
                                            list1[0] = dataL[i].plp_timediff;
                                            list1[1] = dataL[i].plp_fMaxTemperature ;
                                            getOneLMaxTemp.push(list1);
                                        }
                                        // console.log(getOneLMaxTemp);
                                    }
                                    if (dataL1 != null){
                                        for (var i = 0;i<dataL1.length;i++)
                                        {
                                            var list1 = [];
                                            list1[0] = dataL1[i].plp_timediff;
                                            list1[1] = dataL1[i].plp_fMaxTemperature ;
                                            getOneLMaxTemp1.push(list1);
                                        }
                                        // console.log(getOneLMaxTemp);
                                    }
                                    if (dataL2 != null){
                                        for (var i = 0;i<dataL2.length;i++)
                                        {
                                            var list1 = [];
                                            list1[0] = dataL2[i].plp_timediff;
                                            list1[1] = dataL2[i].plp_fMaxTemperature ;
                                            getOneLMaxTemp2.push(list1);
                                        }
                                        // console.log(getOneLMaxTemp);
                                    }
                                    if ( dataM != null ){
                                        for (var i = 0;i<dataM.length;i++)
                                        {
                                            var list2 = [];
                                            list2[0] = dataM[i].plp_timediff;
                                            list2[1] = dataM[i].plp_fMaxTemperature ;
                                            getOneMMaxTemp.push(list2);
                                        }
                                    }
                                    if ( dataM1 != null ){
                                        for (var i = 0;i<dataM1.length;i++)
                                        {
                                            var list2 = [];
                                            list2[0] = dataM1[i].plp_timediff;
                                            list2[1] = dataM1[i].plp_fMaxTemperature ;
                                            getOneMMaxTemp1.push(list2);
                                        }
                                    }
                                    if ( dataM2 != null ){
                                        for (var i = 0;i<dataM2.length;i++)
                                        {
                                            var list2 = [];
                                            list2[0] = dataM2[i].plp_timediff;
                                            list2[1] = dataM2[i].plp_fMaxTemperature ;
                                            getOneMMaxTemp2.push(list2);
                                        }
                                    }
                                    if (dataR != null){
                                        for (var i = 0;i<dataR.length;i++)
                                        {
                                            var list3 = [];
                                            list3[0] = dataR[i].plp_timediff;
                                            list3[1] = dataR[i].plp_fMaxTemperature ;
                                            getOneRMaxTemp.push(list3);
                                        }
                                    }
                                    if (dataR1 != null){
                                        for (var i = 0;i<dataR1.length;i++)
                                        {
                                            var list3 = [];
                                            list3[0] = dataR1[i].plp_timediff;
                                            list3[1] = dataR1[i].plp_fMaxTemperature ;
                                            getOneRMaxTemp1.push(list3);
                                        }
                                    }
                                    if (dataR2 != null){
                                        for (var i = 0;i<dataR2.length;i++)
                                        {
                                            var list3 = [];
                                            list3[0] = dataR2[i].plp_timediff;
                                            list3[1] = dataR2[i].plp_fMaxTemperature ;
                                            getOneRMaxTemp2.push(list3);
                                        }
                                    }
                                    setMaxTemperature(getOneLMaxTemp,getOneLMaxTemp1,getOneLMaxTemp2, getOneMMaxTemp,getOneMMaxTemp1,getOneMMaxTemp2, getOneRMaxTemp,getOneRMaxTemp1,getOneRMaxTemp2,dataFromHtml,listIP);





                                    // setMaxTemperature(getMaxTemp, getMinTemp, getAveTemp);
                                }

                            }
                        });
                    }
                    else{
                        layer.msg('请选择1条数据！',{offset: [500,500]});
                    }
                    break;
                case 'getCheckLength':
                    var data = checkStatus.data;
                    layer.msg('选中了：'+ data.length + ' 个');
                    break;
                case 'isAll':
                    layer.msg(checkStatus.isAll ? '全选': '未全选');
                    break;
            };
        });
        table.on('tool(test)', function(obj){

            if(obj.event === 'edit'){
                var data = obj.data;
                var dataFromHtml = new Array();
                dataFromHtml[0]=data.sample_specification;
                dataFromHtml[1]=data.sample_level;
                dataFromHtml[2]=data.sample_pattern;
                dataFromHtml[3]=data.sample_number;
                dataFromHtml[4]=data.sample_person;
                dataFromHtml[5]=data.sample_test_date;
                dataFromHtml[6]=data.tire_number;
                dataFromHtml[7]=data.room_number;
                dataFromHtml[8]=data.cam_station_number;
                DownloadExcel(dataFromHtml)

            }
        });


    });
}
//设置滑动表格首行不动
function initScrollBar(mainSelector, leftBlankSelector, rightScrollBarSelector, theadTableSelector, tbodyTableSelector, width, height){
    // initStyle
    var w = null;
    if(width){
        w = width + 'px';
    }else{
        w = 'auto';
    }
    var h = null;
    if(height){
        h = height + 'px';
    }else{
        h = 'auto';
    }
    $(mainSelector).css({
        "position":"relative"
    });
    $(leftBlankSelector).css({
        "overflow-x": "auto",
        "position": "relative",
        "width": w
    });
    $(theadTableSelector).css({
        "display": "block",
        "background-color": "#dedede",
        "position": "absolute",
        "background-color": "#dedede",
        "top":"0"
    });
    $(theadTableSelector + " thead").css({
        "display": "block"
    });
    $(tbodyTableSelector).css({
        "overflow-y": "auto",
        "display": "block",
        "height": h
    });

    // initTableUI
    var ttttt = [];
    $(tbodyTableSelector + " thead tr th").each(function(index, jq){
        jq = $(jq);
        ttttt[index] = jq.width();
    });

    $(theadTableSelector + " thead tr th").each(function(index, jq){
        jq = $(jq);
        jq.css("width", ttttt[index]+"px");
    });
    $(theadTableSelector + " thead").css({"width":$(tbodyTableSelector + " thead").width()});
    $(theadTableSelector).css("width", $(theadTableSelector + " thead").width() + "px");

    if(($(tbodyTableSelector + " thead").height()+$(tbodyTableSelector + " tbody").height())>$(tbodyTableSelector).height() ){
        var scrollBarWidth = $(tbodyTableSelector).width() - $(tbodyTableSelector)[0].clientWidth;
        $(tbodyTableSelector).css("width", ($(theadTableSelector + " thead").width() + scrollBarWidth) + "px");
    }else{
        $(tbodyTableSelector).css("width", ($(theadTableSelector + " thead").width() + 0) + "px");
    }

    //initBg2UI
    var scrollBarWidth = $(tbodyTableSelector).width() - $(tbodyTableSelector)[0].clientWidth;
    $(rightScrollBarSelector).css({
        "position":"absolute",
        "height":$(tbodyTableSelector).height()+"px",
        "width":"18px",
        "top":"0",
        "left":($(leftBlankSelector).width()<$(theadTableSelector).width()?$(leftBlankSelector).width()-scrollBarWidth:$(theadTableSelector).width())+"px",
        "overflow-y":"auto"
    });
    $(rightScrollBarSelector).children().css({
        "width":"1px",
        "height":($(tbodyTableSelector + " thead").height() + $(tbodyTableSelector + " tbody").height())+"px",
        "overflow-y":"auto"
    });

    var flag1 = false;
    var flag2 = false;
    $(rightScrollBarSelector).scroll(function(){
        if(!flag2){
            flag1 = true;
            $(tbodyTableSelector)[0].scrollTop = $(rightScrollBarSelector)[0].scrollTop;
        }else{
            flag2 = false;
        }
    });
    $(tbodyTableSelector).scroll(function(){
        if(!flag1){
            flag2 = true;
            $(rightScrollBarSelector)[0].scrollTop = $(tbodyTableSelector)[0].scrollTop;
        }else{
            flag1 = false;
        }
    });
}



//点击html页面单元格获取数据并跳转查询
// var dataFromHtml = new Array();
// function clickCell() {
//     var content;
//     var rowNumber;
//     var tabs = document.getElementById("tb");
//     var td = event.srcElement; // 通过event.srcelement 获取激活事件的对象 td
//     content = td.innerHTML;
//     if(content.length != 0){
//         rowNumber = td.parentElement.rowIndex;
//         console.log("行号：" + rowNumber + "，内容：" + td.innerHTML);
//
//             for(var m=0;m<5;m++){
//                 dataFromHtml[m] = tabs.rows[rowNumber].cells[m+5].innerHTML;
//             }
//         console.log(dataFromHtml);
//     }
// }



//点击html页面单元格获取数据并跳转查询
// var dataFromHtml = new Array();
// var tempCellNum=5;
// function clickCell() {
//     var arr = new Array();
//     var content;
//     var cellNumber;
//     var rowNumber;
//     var tabs = document.getElementById("tb");
//     var td = event.srcElement; // 通过event.srcelement 获取激活事件的对象 td
//     content = td.innerHTML;
//     if(content.length != 0){
//         rowNumber = td.parentElement.rowIndex;
//         cellNumber = td.cellIndex;
//         console.log("行号：" + rowNumber + "，列号：" + cellNumber + "，内容：" + td.innerHTML);
//
//         if(cellNumber == 6 && tempCellNum == 5){
//             for(var m=0;m<cellNumber+1;m++){
//                 dataFromHtml[m] = tabs.rows[rowNumber].cells[m].innerHTML;
//             }
//         }
//         console.log(dataFromHtml);
//         console.log(cellNumber);
//         console.log(tempCellNum);
//         if(cellNumber == 9 && tempCellNum == 8){
//             tempCellNum = tempCellNum+1;
//             dataFromHtml[9] = content;
//             tabs.rows[rowNumber].cells[cellNumber].style.background = 'silver';   //设置点击选中的单元格
//             // dataFromHtml[6] = tire_number;
//             // dataFromHtml[7] = room_number;
//             // dataFromHtml[8] = cam_station_number;
//             // dataFromHtml[9] = cam_ip;
//         }else if(cellNumber>tempCellNum && cellNumber<9){
//             tempCellNum = tempCellNum+1;
//             tabs.rows[rowNumber].cells[cellNumber].style.background = 'silver';   //设置点击选中的单元格
//             switch (cellNumber) {
//                 case 6: dataFromHtml[6] = content; break;
//                 case 7: dataFromHtml[7] = content; break;
//                 case 8: dataFromHtml[8] = content; break;
//             }
//             for (var i = 0; i < sampleData.list.length; i++) {
//                 var flag = true;
//                 var dataFromDB = new Array();
//                 var s = sampleData.list[i];
//                 dataFromDB[0] = s.sample_specification;
//                 dataFromDB[1] = s.sample_level;
//                 dataFromDB[2] = s.sample_pattern;
//                 dataFromDB[3] = s.sample_number;
//                 dataFromDB[4] = s.sample_person;
//                 dataFromDB[5] = s.sample_test_date;
//                 dataFromDB[6] = s.tire_number;
//                 dataFromDB[7] = s.room_number;
//                 dataFromDB[8] = s.cam_station_number;
//                 dataFromDB[9] = s.cam_ip;
//                 console.log(dataFromDB);
//                 for (var j = 0; j < cellNumber+1; j++) {
//                     if (dataFromHtml[j] != dataFromDB[j]) {
//                         flag = false;
//                     }
//                     console.log(flag);
//                 }
//                 if (flag) {
//                     arr.push(dataFromDB[cellNumber+1]);
//                 }
//             }
//             console.log('aee:'+arr);
//             var rowLen = tabs.rows.length;
//             var cellLen = tabs.rows.item(rowNumber).cells.length;
//             for(var x=0;x<rowLen;x++){
//                 for(var y=cellNumber+1;y<cellLen;y++){
//                     tabs.rows[x].deleteCell(y);
//                 }
//             }
//
//             for(var w=0;w<arr.length;w++){
//                 var temp = tabs.rows[w].insertCell(cellNumber+1);
//                 temp.innerHTML = arr[w];
//             }
//         }
//     }
// }

