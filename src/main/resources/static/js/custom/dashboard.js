
//本地下载图片到指定路径
var saveImage1;
var saveImage2;
var cam_number1;
var cam_number2;
var LOnePOne = [[0, 2], [1, 6], [2, 3], [3, 8], [4, 5], [5, 13], [6, 20]];
var LOnePTwo=  [[0, 2], [1, 6], [2, 3], [3, 8], [4, 5], [5, 13], [6, 20]];
var LOnePThree=  [[0, 2], [1, 6], [2, 3], [3, 8], [4, 5], [5, 13], [6, 20]];
//var LOnePFour=  [[0, 2], [1, 6], [2, 3], [3, 8], [4, 5], [5, 13], [6, 20]];
var MOnePOne = [[0, 2], [1, 6], [2, 3], [3, 8], [4, 5], [5, 13], [6, 20]];
var MOnePTwo=  [[0, 2], [1, 6], [2, 3], [3, 8], [4, 5], [5, 13], [6, 20]];
var MOnePThree=  [[0, 2], [1, 6], [2, 3], [3, 8], [4, 5], [5, 13], [6, 20]];
//var MOnePFour=  [[0, 2], [1, 6], [2, 3], [3, 8], [4, 5], [5, 13], [6, 20]];
var ROnePOne = [[0, 2], [1, 6], [2, 3], [3, 8], [4, 5], [5, 13], [6, 20]];
var ROnePTwo=  [[0, 2], [1, 6], [2, 3], [3, 8], [4, 5], [5, 13], [6, 20]];
var ROnePThree=  [[0, 2], [1, 6], [2, 3], [3, 8], [4, 5], [5, 13], [6, 20]];
//var ROnePFour=  [[0, 2], [1, 6], [2, 3], [3, 8], [4, 5], [5, 13], [6, 20]];

//仓位2 全局变量//修改
var LTwoPOne = [[0, 2], [1, 6], [2, 3], [3, 8], [4, 5], [5, 13], [6, 20]];
var LTwoPTwo=  [[0, 2], [1, 6], [2, 3], [3, 8], [4, 5], [5, 13], [6, 20]];
var LTwoPThree=  [[0, 2], [1, 6], [2, 3], [3, 8], [4, 5], [5, 13], [6, 20]];
//var LTwoPFour=  [[0, 2], [1, 6], [2, 3], [3, 8], [4, 5], [5, 13], [6, 20]];
var MTwoPOne = [[0, 2], [1, 6], [2, 3], [3, 8], [4, 5], [5, 13], [6, 20]];
var MTwoPTwo=  [[0, 2], [1, 6], [2, 3], [3, 8], [4, 5], [5, 13], [6, 20]];
var MTwoPThree=  [[0, 2], [1, 6], [2, 3], [3, 8], [4, 5], [5, 13], [6, 20]];
//var MTwoPFour=  [[0, 2], [1, 6], [2, 3], [3, 8], [4, 5], [5, 13], [6, 20]];
var RTwoPOne = [[0, 2], [1, 6], [2, 3], [3, 8], [4, 5], [5, 13], [6, 20]];
var RTwoPTwo=  [[0, 2], [1, 6], [2, 3], [3, 8], [4, 5], [5, 13], [6, 20]];
var RTwoPThree=  [[0, 2], [1, 6], [2, 3], [3, 8], [4, 5], [5, 13], [6, 20]];
//var RTwoPFour=  [[0, 2], [1, 6], [2, 3], [3, 8], [4, 5], [5, 13], [6, 20]];
var cam_number1=$("#roomnumber").val()+"-1-";
//alert(cam_number1);
var cam_number2=$("#roomnumber").val()+"-2-";
var interval;
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

    ///// SIMPLE CHART ///////修改

    $("#refresh_rate").change(function(){
        var options=$("#refresh_rate option:selected");
        if(options.val()=="thirty")
        {
            clearInterval(interval);
            //$("#showBtnContent").html("开始绘制曲线...");
            interval = setInterval(getTempInfoDash,1800000);
        }
        else if(options.val()=="ten")
        {
            clearInterval(interval);
            //$("#showBtnContent").html("开始绘制曲线...");
            interval = setInterval(getTempInfoDash,600000);
        }
        else if(options.val()=="five")
        {
            clearInterval(interval);
            //$("#showBtnContent").html("开始绘制曲线...");
            interval = setInterval(getTempInfoDash,300000);
        }
        else{
            clearInterval(interval);
            //$("#showBtnContent").html("开始绘制曲线...");
            interval = setInterval(getTempInfoDash,60000);
        }
    })
    //开始检测按钮集成绘制曲线功能
    $("#startRecord").on('click',getTempInfoDashClick1);
    //结束试验按钮停止绘制图表
    $("#stopInsTemp").on('click',function () {

        clearInterval(interval);   //停止调用setInterval
        // defaultchart()
        ImageSaveFile();//保存图表图片
    });




    //保存图片


//修改 仓位1
//     function getTempInfoDatabase(room_number, tire_number, cam_station_number) {
//         $.ajax({
//             url: '/getTempInfoDash',
//             type: "post",
//             contentType: "application/x-www-form-urlencoded; charset=utf-8",
//             data: {
//                 "room_number": room_number,
//                 "tire_number": tire_number,
//                 "cam_station_number": cam_station_number
//             },
//             cache: false,
//             dataType: "json",
//             error: function () {
//                 alert("你所查询的内容不存在！")
//             },
//             success: function (data) {
//
//                 if (data.length != 0) {
//
//                     //获取仓位1的变量
//                     var getLOnePOne = [];
//                     var getLOnePTwo = [];
//                     var getLOnePThree = [];
//                     var getLOnePFour = [];
//                     var getMOnePOne = [];
//                     var getMOnePTwo = [];
//                     var getMOnePThree = [];
//                     var getMOnePFour = [];
//                     var getROnePOne = [];
//                     var getROnePTwo = [];
//                     var getROnePThree = [];
//                     var getROnePFour = [];
//
//
//                     data.map(function (arr) {
//                         //获取仓位1的list
//                         var listLOnePOne = [];
//                         var listLOnePTwo = [];
//                         var listLOnePThree = [];
//                         var listLOnePFour = [];
//                         var listMOnePOne = [];
//                         var listMOnePTwo = [];
//                         var listMOnePThree = [];
//                         var listMOnePFour = [];
//                         var listROnePOne = [];
//                         var listROnePTwo = [];
//                         var listROnePThree = [];
//                         var listROnePFour = [];
//
//                         if (cam_station_number == 1) {//针对仓位1进行数据的获取
//                             if (arr.cam_position == "L") {//针对左侧的摄像头进行获取
//                                 if (arr.sample_type_number == 1) {
//                                     listLOnePOne[0] = arr.plp_timediff;
//                                     listLOnePOne[1] = arr.plp_fMaxTemperature;
//                                     getLOnePOne.push(listLOnePOne);
//                                 }
//                                 if (arr.sample_type_number == 2) {
//                                     listLOnePTwo[0] = arr.plp_timediff;
//                                     listLOnePTwo[1] = arr.plp_fMaxTemperature;
//                                     getLOnePTwo.push(listLOnePTwo);
//                                 }
//                                 if (arr.sample_type_number == 3) {
//                                     listLOnePThree[0] = arr.plp_timediff;
//                                     listLOnePThree[1] = arr.plp_fMaxTemperature;
//                                     getLOnePThree.push(listLOnePThree);
//                                 }
//                                 if (arr.sample_type_number == 4) {
//                                     listLOnePFour[0] = arr.plp_timediff;
//                                     listLOnePFour[1] = arr.plp_fMaxTemperature;
//                                     getLOnePFour.push(listLOnePFour);
//                                 }
//                             }
//                             if (arr.cam_position == "M") {//针对中间的摄像头进行获取
//                                 if (arr.sample_type_number == 1) {
//                                     listMOnePOne[0] = arr.plp_timediff;
//                                     listMOnePOne[1] = arr.plp_fMaxTemperature;
//                                     getMOnePOne.push(listMOnePOne);
//                                 }
//                                 if (arr.sample_type_number == 2) {
//                                     listMOnePTwo[0] = arr.plp_timediff;
//                                     listMOnePTwo[1] = arr.plp_fMaxTemperature;
//                                     getMOnePTwo.push(listMOnePTwo);
//                                 }
//                                 if (arr.sample_type_number == 3) {
//                                     listMOnePThree[0] = arr.plp_timediff;
//                                     listMOnePThree[1] = arr.plp_fMaxTemperature;
//                                     getMOnePThree.push(listMOnePThree);
//                                 }
//                                 if (arr.sample_type_number == 4) {
//                                     listMOnePFour[0] = arr.plp_timediff;
//                                     listMOnePFour[1] = arr.plp_fMaxTemperature;
//                                     getMOnePFour.push(listMOnePFour);
//                                 }
//                             }
//                             if (arr.cam_position == "R") {//针对右侧的摄像头进行获取
//                                 if (arr.sample_type_number == 1) {
//                                     listROnePOne[0] = arr.plp_timediff;
//                                     listROnePOne[1] = arr.plp_fMaxTemperature;
//                                     getROnePOne.push(listROnePOne);
//                                 }
//                                 if (arr.sample_type_number == 2) {
//                                     listROnePTwo[0] = arr.plp_timediff;
//                                     listROnePTwo[1] = arr.plp_fMaxTemperature;
//                                     getROnePTwo.push(listROnePTwo);
//                                 }
//                                 if (arr.sample_type_number == 3) {
//                                     listROnePThree[0] = arr.plp_timediff;
//                                     listROnePThree[1] = arr.plp_fMaxTemperature;
//                                     getROnePThree.push(listROnePThree);
//                                 }
//                                 if (arr.sample_type_number == 4) {
//                                     listROnePFour[0] = arr.plp_timediff;
//                                     listROnePFour[1] = arr.plp_fMaxTemperature;
//                                     getROnePFour.push(listROnePFour);
//                                 }
//                             }
//
//                         }
//
//                     })
//
//                     setTempFigureOne(getLOnePOne,getLOnePTwo,getLOnePThree,getLOnePFour,getMOnePOne,getMOnePTwo,getMOnePThree,getMOnePFour,getROnePOne,getROnePTwo,getROnePThree,getROnePFour);
//                     //setTempFigureTwo(getLTwoPOne, getLTwoPTwo, getLTwoPThree, getLTwoPFour, getLTwoPFive, getMTwoPOne, getMTwoPTwo, getMTwoPThree, getMTwoPFour, getMTwoPFive, getRTwoPOne, getRTwoPTwo, getRTwoPThree, getRTwoPFour, getRTwoPFive);
//
//                 }
//             }
//         })
//         ;
//     }
//     //仓位2
//     function getTempInfoDatabase2(room_number, tire_number, cam_station_number) {
//         $.ajax({
//             url: '/getTempInfoDash',
//             type: "post",
//             contentType: "application/x-www-form-urlencoded; charset=utf-8",
//             data: {
//                 "room_number": room_number,
//                 "tire_number": tire_number,
//                 "cam_station_number": cam_station_number
//             },
//             cache: false,
//             dataType: "json",
//             error: function () {
//                 alert("你所查询的内容不存在！")
//             },
//             success: function (data) {
//
//                 if (data.length != 0) {
//
//                     //获取仓位2的变量
//                     var getLTwoPOne = [];
//                     var getLTwoPTwo = [];
//                     var getLTwoPThree = [];
//                     var getLTwoPFour = [];
//                     var getMTwoPOne = [];
//                     var getMTwoPTwo = [];
//                     var getMTwoPThree = [];
//                     var getMTwoPFour = [];
//                     var getRTwoPOne = [];
//                     var getRTwoPTwo = [];
//                     var getRTwoPThree = [];
//                     var getRTwoPFour = [];
//
//                     data.map(function (arr) {
//                         //获取仓位2的list
//                         var listLTwoPOne = [];
//                         var listLTwoPTwo = [];
//                         var listLTwoPThree = [];
//                         var listLTwoPFour = [];
//                         var listMTwoPOne = [];
//                         var listMTwoPTwo = [];
//                         var listMTwoPThree = [];
//                         var listMTwoPFour = [];
//                         var listRTwoPOne = [];
//                         var listRTwoPTwo = [];
//                         var listRTwoPThree = [];
//                         var listRTwoPFour = [];
//
//                         if (cam_station_number == 2) {//针对仓位2进行数据的获取
//                             if (arr.cam_position == "L") {//针对左侧的摄像头进行获取
//                                 if (arr.sample_type_number == 1) {
//                                     listLTwoPOne[0] = arr.plp_timediff;
//                                     listLTwoPOne[1] = arr.plp_fMaxTemperature;
//                                     getLTwoPOne.push(listLTwoPOne);
//                                 }
//                                 if (arr.sample_type_number == 2) {
//                                     listLTwoPTwo[0] = arr.plp_timediff;
//                                     listLTwoPTwo[1] = arr.plp_fMaxTemperature;
//                                     getLTwoPTwo.push(listLTwoPTwo);
//                                 }
//                                 if (arr.sample_type_number == 3) {
//                                     listLTwoPThree[0] = arr.plp_timediff;
//                                     listLTwoPThree[1] = arr.plp_fMaxTemperature;
//                                     getLTwoPThree.push(listLTwoPThree);
//                                 }
//                                 if (arr.sample_type_number == 4) {
//                                     listLTwoPFour[0] = arr.plp_timediff;
//                                     listLTwoPFour[1] = arr.plp_fMaxTemperature;
//                                     getLTwoPFour.push(listLTwoPFour);
//                                 }
//
//                             }
//                             if (arr.cam_position == "M") {//针对中间的摄像头进行获取
//                                 if (arr.sample_type_number == 1) {
//                                     listMTwoPOne[0] = arr.plp_timediff;
//                                     listMTwoPOne[1] = arr.plp_fMaxTemperature;
//                                     getMTwoPOne.push(listMTwoPOne);
//                                 }
//                                 if (arr.sample_type_number == 2) {
//                                     listMTwoPTwo[0] = arr.plp_timediff;
//                                     listMTwoPTwo[1] = arr.plp_fMaxTemperature;
//                                     getMTwoPTwo.push(listMTwoPTwo);
//                                 }
//                                 if (arr.sample_type_number == 3) {
//                                     listMTwoPThree[0] = arr.plp_timediff;
//                                     listMTwoPThree[1] = arr.plp_fMaxTemperature;
//                                     getMTwoPThree.push(listMTwoPThree);
//                                 }
//                                 if (arr.sample_type_number == 4) {
//                                     listMTwoPFour[0] = arr.plp_timediff;
//                                     listMTwoPFour[1] = arr.plp_fMaxTemperature;
//                                     getMTwoPFour.push(listMTwoPFour);
//                                 }
//                             }
//                             if (arr.cam_position == "R") {//针对右侧的摄像头进行获取
//                                 if (arr.sample_type_number == 1) {
//                                     listRTwoPOne[0] = arr.plp_timediff;
//                                     listRTwoPOne[1] = arr.plp_fMaxTemperature;
//                                     getRTwoPOne.push(listRTwoPOne);
//                                 }
//                                 if (arr.sample_type_number == 2) {
//                                     listRTwoPTwo[0] = arr.plp_timediff;
//                                     listRTwoPTwo[1] = arr.plp_fMaxTemperature;
//                                     getRTwoPTwo.push(listRTwoPTwo);
//                                 }if (arr.sample_type_number == 3) {
//                                     listRTwoPThree[0] = arr.plp_timediff;
//                                     listRTwoPThree[1] = arr.plp_fMaxTemperature;
//                                     getRTwoPThree.push(listRTwoPThree);
//                                 }if (arr.sample_type_number ==4) {
//                                     listRTwoPFour[0] = arr.plp_timediff;
//                                     listRTwoPFour[1] = arr.plp_fMaxTemperature;
//                                     getRTwoPFour.push(listRTwoPFour);
//                                 }
//
//
//                             }
//
//                         }
//                     })
//
//                     //setTempFigureOne(getLOnePOne, getLOnePTwo, getLOnePThree, getLOnePFour, getLOnePFive, getMOnePOne, getMOnePTwo, getMOnePThree, getMOnePFour, getMOnePFive, getROnePOne, getROnePTwo, getROnePThree, getROnePFour, getROnePFive);
//                     setTempFigureTwo(getLTwoPOne,getLTwoPTwo,getLTwoPThree,getLTwoPFour,getMTwoPOne,getMTwoPTwo,getMTwoPThree,getMTwoPFour,getRTwoPOne,getRTwoPTwo,getRTwoPThree,getRTwoPFour);
//
//                 }
//             }
//         })
//         ;
//     }



//保存图片按钮触发
    // $('#saveImage').on('click',function () {
    //     // var position = $("input[name=chkRadio1]").;
    //     var position1 = document.getElementsByName('chkRadio1')[0].checked;
    //     var position2 = document.getElementsByName('chkRadio2')[0].checked;
    //     if(!position1 && !position2){
    //         alert("请选择仓位号!");
    //     }else {
    //         var svg1 = saveImage1.getSVG();
    //         var svg2 = saveImage2.getSVG();
    //         var room_number= $("#roomnumber").val();
    //             var sample_test_date1 = $("#sample_test_date1").val();
    //             var sample_test_date2 = $("#sample_test_date2").val();
    //             var tire_number1 = $("#tire_number1").val();
    //             var tire_number2 =$("#tire_number2").val();
    //     $.ajax({
    //         url:'saveImageToLocal',
    //         type:"post",
    //         data:{
    //             "svg1":svg1,
    //             "svg2":svg2,
    //             "room_number":room_number,
    //             "position1":position1,
    //             "position2":position2,
    //             "sample_test_date1":sample_test_date1,
    //             "sample_test_date2":sample_test_date2,
    //             "tire_number1":tire_number1,
    //             "tire_number2":tire_number2
    //         },
    //         dataType:'json',
    //         catch:false,
    //         async:false,
    //         success:function (data) {
    //             //console.log(data);
    //             alert("保存图片成功");
    //         },
    //         error:function (msg) {
    //             alert(msg);
    //         }
    //     });
    //     }
    // });




    //默认折线图
    function defaultchart(){
        saveImage1 = Highcharts.chart('chartplace1',{
            chart:{
                backgroundColor:'#FCFCFC',
                type:'line',
                //borderWidth:1,
                plotBorderWidth:1,
                spacingRight:10
            },
            title:{
                text:'1号仓位轮胎温度折线图'
            },
            series: [{
                name: cam_number1+'胎内子口温度', data: LOnePOne
            },{
                name: cam_number1+'胎内肩部温度', data: LOnePTwo
            },{
                name: cam_number1+'胎内侧部中间温度', data: LOnePThree
            },{
                name: cam_number1+'胎冠区域温度', data: MOnePOne
            },{
                name: cam_number1+'胎冠中间温度', data: MOnePTwo
            },{
                name: cam_number1+'胎冠肩部温度', data: MOnePThree
            },{
                name: cam_number1+'胎外子口温度', data: ROnePOne
            },{
                name: cam_number1+'胎外肩部温度', data: ROnePTwo
            },{
                name: cam_number1+'胎外侧部中间温度', data: ROnePThree
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
                hoverable: true, clickable: true,
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
                }},
            exporting:{
                sourceWidth: 1000,
                sourceHeight: 400,
                enabled:false
            }
        });
        //2号仓位折线图
        saveImage2 = Highcharts.chart('chartplace2',{
            chart:{
                backgroundColor:'#FCFCFC',
                type:'line',
                //borderWidth:1,
                plotBorderWidth:1,
                spacingRight:10
            },
            title:{
                text:'2号仓位轮胎温度折线图'
            },
            series: [{
                name: cam_number2+'胎内子口温度', data: LTwoPOne
            },{
                name: cam_number2+'胎内肩部温度', data: LTwoPTwo
            },{
                name: cam_number2+'胎内侧部中间温度', data: LTwoPThree
            },{
                name: cam_number2+'胎冠区域温度', data: MTwoPOne
            },{
                name: cam_number2+'胎冠中间温度', data: MTwoPTwo
            },{
                name: cam_number2+'胎冠肩部温度', data: MTwoPThree
            },{
                name: cam_number2+'胎外子口温度', data: RTwoPOne
            },{
                name: cam_number2+'胎外肩部温度', data: RTwoPTwo
            },{
                name: cam_number2+'胎外侧部中间温度', data: RTwoPThree
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
                hoverable: true, clickable: true,
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
                }},
            exporting:{
                sourceWidth: 1000,
                sourceHeight: 400,
                enabled:false
            }
        });


    }
    defaultchart()



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

    getCurrentRoomdata($("#roomnumber").val())
    getTempInfoDash()
    clearInterval(interval);
    //$("#showBtnContent").html("开始绘制曲线...");
    interval = setInterval(getTempInfoDash,1800000);
});
function getTempInfoDashClick1() {
    //console.log("1111");
    getTempInfoDash();
    clearInterval(interval);
    //$("#showBtnContent").html("开始绘制曲线...");
    interval = setInterval(getTempInfoDash,1800000);
    //$("#startRecord").attr("disabled",true);
}
function getTempInfoDash() {
    // console.log("nihao")
    //获取前台仓位是否需要获取温度。分别返回true 和false。
    var position1 =$('#chkRadio1').prop("checked");
    var position2 =$('#chkRadio2').prop("checked");
    console.log("在这里"+position1)
    var camInfo = dataCamInfo;
    var room_number, tire_number, cam_station_number

    if (position1 == true) {//如果1号仓位是被选中的，那么才会对1号仓位的图像进行绘制。
        tire_number = $("#tire_number1").val();//轮胎编号
        room_number = $("#roomnumber").val();//房间号
        cam_station_number = 1;//摄像头仓位号
        getTempInfoDatabase(room_number, tire_number, cam_station_number);
    }
    if (position2 == true) {
        tire_number = $("#tire_number2").val();//轮胎编号
        room_number = $("#roomnumber").val();//房间号
        cam_station_number = 2;//摄像头仓位号
        getTempInfoDatabase2(room_number, tire_number, cam_station_number);
    }
}
//修改 仓位1
function getTempInfoDatabase(room_number, tire_number, cam_station_number) {
    $.ajax({
        url: '/getTempInfoDash',
        type: "post",
        contentType: "application/x-www-form-urlencoded; charset=utf-8",
        data: {
            "room_number": room_number,
            "tire_number": tire_number,
            "cam_station_number": cam_station_number
        },
        cache: false,
        dataType: "json",
        error: function () {
            alert("你所查询的内容不存在！")
        },
        success: function (data) {

            if (data.length != 0) {

                //获取仓位1的变量
                var getLOnePOne = [];
                var getLOnePTwo = [];
                var getLOnePThree = [];
                var getLOnePFour = [];
                var getMOnePOne = [];
                var getMOnePTwo = [];
                var getMOnePThree = [];
                var getMOnePFour = [];
                var getROnePOne = [];
                var getROnePTwo = [];
                var getROnePThree = [];
                var getROnePFour = [];


                data.map(function (arr) {
                    //获取仓位1的list
                    var listLOnePOne = [];
                    var listLOnePTwo = [];
                    var listLOnePThree = [];
                    var listLOnePFour = [];
                    var listMOnePOne = [];
                    var listMOnePTwo = [];
                    var listMOnePThree = [];
                    var listMOnePFour = [];
                    var listROnePOne = [];
                    var listROnePTwo = [];
                    var listROnePThree = [];
                    var listROnePFour = [];

                    if (cam_station_number == 1) {//针对仓位1进行数据的获取
                        if (arr.cam_position == "L") {//针对左侧的摄像头进行获取
                            if (arr.sample_type_number == 1) {
                                listLOnePOne[0] = arr.plp_timediff;
                                listLOnePOne[1] = arr.plp_fMaxTemperature;
                                getLOnePOne.push(listLOnePOne);
                            }
                            if (arr.sample_type_number == 2) {
                                listLOnePTwo[0] = arr.plp_timediff;
                                listLOnePTwo[1] = arr.plp_fMaxTemperature;
                                getLOnePTwo.push(listLOnePTwo);
                            }
                            if (arr.sample_type_number == 3) {
                                listLOnePThree[0] = arr.plp_timediff;
                                listLOnePThree[1] = arr.plp_fMaxTemperature;
                                getLOnePThree.push(listLOnePThree);
                            }
                            if (arr.sample_type_number == 4) {
                                listLOnePFour[0] = arr.plp_timediff;
                                listLOnePFour[1] = arr.plp_fMaxTemperature;
                                getLOnePFour.push(listLOnePFour);
                            }
                        }
                        if (arr.cam_position == "M") {//针对中间的摄像头进行获取
                            if (arr.sample_type_number == 1) {
                                listMOnePOne[0] = arr.plp_timediff;
                                listMOnePOne[1] = arr.plp_fMaxTemperature;
                                getMOnePOne.push(listMOnePOne);
                            }
                            if (arr.sample_type_number == 2) {
                                listMOnePTwo[0] = arr.plp_timediff;
                                listMOnePTwo[1] = arr.plp_fMaxTemperature;
                                getMOnePTwo.push(listMOnePTwo);
                            }
                            if (arr.sample_type_number == 3) {
                                listMOnePThree[0] = arr.plp_timediff;
                                listMOnePThree[1] = arr.plp_fMaxTemperature;
                                getMOnePThree.push(listMOnePThree);
                            }
                            if (arr.sample_type_number == 4) {
                                listMOnePFour[0] = arr.plp_timediff;
                                listMOnePFour[1] = arr.plp_fMaxTemperature;
                                getMOnePFour.push(listMOnePFour);
                            }
                        }
                        if (arr.cam_position == "R") {//针对右侧的摄像头进行获取
                            if (arr.sample_type_number == 1) {
                                listROnePOne[0] = arr.plp_timediff;
                                listROnePOne[1] = arr.plp_fMaxTemperature;
                                getROnePOne.push(listROnePOne);
                            }
                            if (arr.sample_type_number == 2) {
                                listROnePTwo[0] = arr.plp_timediff;
                                listROnePTwo[1] = arr.plp_fMaxTemperature;
                                getROnePTwo.push(listROnePTwo);
                            }
                            if (arr.sample_type_number == 3) {
                                listROnePThree[0] = arr.plp_timediff;
                                listROnePThree[1] = arr.plp_fMaxTemperature;
                                getROnePThree.push(listROnePThree);
                            }
                            if (arr.sample_type_number == 4) {
                                listROnePFour[0] = arr.plp_timediff;
                                listROnePFour[1] = arr.plp_fMaxTemperature;
                                getROnePFour.push(listROnePFour);
                            }
                        }

                    }

                })

                setTempFigureOne(getLOnePOne,getLOnePTwo,getLOnePThree,getLOnePFour,getMOnePOne,getMOnePTwo,getMOnePThree,getMOnePFour,getROnePOne,getROnePTwo,getROnePThree,getROnePFour);
                //setTempFigureTwo(getLTwoPOne, getLTwoPTwo, getLTwoPThree, getLTwoPFour, getLTwoPFive, getMTwoPOne, getMTwoPTwo, getMTwoPThree, getMTwoPFour, getMTwoPFive, getRTwoPOne, getRTwoPTwo, getRTwoPThree, getRTwoPFour, getRTwoPFive);

            }
        }
    })
    ;
}
//仓位2
function getTempInfoDatabase2(room_number, tire_number, cam_station_number) {
    $.ajax({
        url: '/getTempInfoDash',
        type: "post",
        contentType: "application/x-www-form-urlencoded; charset=utf-8",
        data: {
            "room_number": room_number,
            "tire_number": tire_number,
            "cam_station_number": cam_station_number
        },
        cache: false,
        dataType: "json",
        error: function () {
            alert("你所查询的内容不存在！")
        },
        success: function (data) {

            if (data.length != 0) {

                //获取仓位2的变量
                var getLTwoPOne = [];
                var getLTwoPTwo = [];
                var getLTwoPThree = [];
                var getLTwoPFour = [];
                var getMTwoPOne = [];
                var getMTwoPTwo = [];
                var getMTwoPThree = [];
                var getMTwoPFour = [];
                var getRTwoPOne = [];
                var getRTwoPTwo = [];
                var getRTwoPThree = [];
                var getRTwoPFour = [];

                data.map(function (arr) {
                    //获取仓位2的list
                    var listLTwoPOne = [];
                    var listLTwoPTwo = [];
                    var listLTwoPThree = [];
                    var listLTwoPFour = [];
                    var listMTwoPOne = [];
                    var listMTwoPTwo = [];
                    var listMTwoPThree = [];
                    var listMTwoPFour = [];
                    var listRTwoPOne = [];
                    var listRTwoPTwo = [];
                    var listRTwoPThree = [];
                    var listRTwoPFour = [];

                    if (cam_station_number == 2) {//针对仓位2进行数据的获取
                        if (arr.cam_position == "L") {//针对左侧的摄像头进行获取
                            if (arr.sample_type_number == 1) {
                                listLTwoPOne[0] = arr.plp_timediff;
                                listLTwoPOne[1] = arr.plp_fMaxTemperature;
                                getLTwoPOne.push(listLTwoPOne);
                            }
                            if (arr.sample_type_number == 2) {
                                listLTwoPTwo[0] = arr.plp_timediff;
                                listLTwoPTwo[1] = arr.plp_fMaxTemperature;
                                getLTwoPTwo.push(listLTwoPTwo);
                            }
                            if (arr.sample_type_number == 3) {
                                listLTwoPThree[0] = arr.plp_timediff;
                                listLTwoPThree[1] = arr.plp_fMaxTemperature;
                                getLTwoPThree.push(listLTwoPThree);
                            }
                            if (arr.sample_type_number == 4) {
                                listLTwoPFour[0] = arr.plp_timediff;
                                listLTwoPFour[1] = arr.plp_fMaxTemperature;
                                getLTwoPFour.push(listLTwoPFour);
                            }

                        }
                        if (arr.cam_position == "M") {//针对中间的摄像头进行获取
                            if (arr.sample_type_number == 1) {
                                listMTwoPOne[0] = arr.plp_timediff;
                                listMTwoPOne[1] = arr.plp_fMaxTemperature;
                                getMTwoPOne.push(listMTwoPOne);
                            }
                            if (arr.sample_type_number == 2) {
                                listMTwoPTwo[0] = arr.plp_timediff;
                                listMTwoPTwo[1] = arr.plp_fMaxTemperature;
                                getMTwoPTwo.push(listMTwoPTwo);
                            }
                            if (arr.sample_type_number == 3) {
                                listMTwoPThree[0] = arr.plp_timediff;
                                listMTwoPThree[1] = arr.plp_fMaxTemperature;
                                getMTwoPThree.push(listMTwoPThree);
                            }
                            if (arr.sample_type_number == 4) {
                                listMTwoPFour[0] = arr.plp_timediff;
                                listMTwoPFour[1] = arr.plp_fMaxTemperature;
                                getMTwoPFour.push(listMTwoPFour);
                            }
                        }
                        if (arr.cam_position == "R") {//针对右侧的摄像头进行获取
                            if (arr.sample_type_number == 1) {
                                listRTwoPOne[0] = arr.plp_timediff;
                                listRTwoPOne[1] = arr.plp_fMaxTemperature;
                                getRTwoPOne.push(listRTwoPOne);
                            }
                            if (arr.sample_type_number == 2) {
                                listRTwoPTwo[0] = arr.plp_timediff;
                                listRTwoPTwo[1] = arr.plp_fMaxTemperature;
                                getRTwoPTwo.push(listRTwoPTwo);
                            }if (arr.sample_type_number == 3) {
                                listRTwoPThree[0] = arr.plp_timediff;
                                listRTwoPThree[1] = arr.plp_fMaxTemperature;
                                getRTwoPThree.push(listRTwoPThree);
                            }if (arr.sample_type_number ==4) {
                                listRTwoPFour[0] = arr.plp_timediff;
                                listRTwoPFour[1] = arr.plp_fMaxTemperature;
                                getRTwoPFour.push(listRTwoPFour);
                            }


                        }

                    }
                })

                //setTempFigureOne(getLOnePOne, getLOnePTwo, getLOnePThree, getLOnePFour, getLOnePFive, getMOnePOne, getMOnePTwo, getMOnePThree, getMOnePFour, getMOnePFive, getROnePOne, getROnePTwo, getROnePThree, getROnePFour, getROnePFive);
                setTempFigureTwo(getLTwoPOne,getLTwoPTwo,getLTwoPThree,getLTwoPFour,getMTwoPOne,getMTwoPTwo,getMTwoPThree,getMTwoPFour,getRTwoPOne,getRTwoPTwo,getRTwoPThree,getRTwoPFour);

            }
        }
    })
    ;
}
//绘制1号仓位折线图
function setTempFigureOne(LOnePOne1,LOnePOne2,LOnePOne3,LOnePOne4,MOnePOne1,MOnePOne2,MOnePOne3,MOnePOne4,ROnePOne1,ROnePOne2,ROnePOne3,ROnePOne4) {

    LOnePOne = LOnePOne1;
    LOnePTwo =LOnePOne2;
    LOnePThree=LOnePOne3;
    //LOnePFour=LOnePOne4;
    MOnePOne = MOnePOne1;
    MOnePTwo =MOnePOne2;
    MOnePThree=MOnePOne3;
    //MOnePFour =MOnePOne4;
    ROnePOne = ROnePOne1;
    ROnePTwo =ROnePOne2;
    ROnePThree=ROnePOne3;
    //ROnePFour=ROnePOne4;

    saveImage1 = Highcharts.chart('chartplace1', {
        chart: {
            backgroundColor: '#FCFCFC',
            type: 'line',
            //borderWidth:1,
            plotBorderWidth: 1,
            spacingRight: 10
        },
        title: {
            text: '1号仓位轮胎温度折线图'
        },
        series: [{
            name: cam_number1+'胎内子口温度', data: LOnePOne
        },{
            name: cam_number1+'胎内肩部温度', data: LOnePTwo
        },{
            name: cam_number1+'胎内侧部中间温度', data: LOnePThree, visible:false
        },{
            name: cam_number1+'胎冠区域温度', data: MOnePOne
        },{
            name: cam_number1+'胎冠中间温度', data: MOnePTwo, visible:false
        },{
            name: cam_number1+'胎冠肩部温度', data: MOnePThree, visible:false
        },{
            name: cam_number1+'胎外子口温度', data: ROnePOne
        },{
            name: cam_number1+'胎外肩部温度', data: ROnePTwo
        },{
            name: cam_number1+'胎外侧部中间温度', data: ROnePThree, visible:false
        }],
        legend: {
            layout: 'vertical',
            align: 'right',
            verticalAlign: 'middle'
        },
        yAxis: {
            min: 0,
            max: 120,
            // tickPixelInterval:30, //调整y轴刻度的间距
            tickInterval: 10, //刻度值
            title: {
                text: '温度值'
            },
            gridLineColor: '#CCCCCC',
            gridLineWidth: 1
        },
        xAxis: {
            gridLineColor: '#CCCCCC',
            gridLineWidth: 0
        },
        lines: {
            show: true,
            fill: true,
            fillColor: {colors: [{opacity: 0.05}, {opacity: 0.15}, {opacity: 0.25}]}
        },
        credits: {
            enabled: false
        },
        grid: {
            hoverable: true, clickable: true,
            borderColor: '#ccc',
            borderWidth: 1,
            labelMargin: 10
        },
        plotOptions: {
            allowPointSelect: true,
            series: {
                cursor: 'pointer', //鼠标滑过数据列时鼠标的形状
                events: {
                    click: function (e) {
                        // alert(this.name)
                    }
                }
            }
        },
        exporting: {
            sourceWidth: 1000,
            sourceHeight: 400,
            enabled: false
        }
    });
}
//修改
//绘制2号仓位折线图
function setTempFigureTwo(LTwoPOne1,LTwoPOne2,LTwoPOne3,LTwoPOne4,MTwoPOne1,MTwoPOne2,MTwoPOne3,MTwoPOne4, RTwoPOne1,RTwoPOne2,RTwoPOne3,RTwoPOne4) {
    LTwoPOne = LTwoPOne1;
    LTwoPTwo = LTwoPOne2;
    LTwoPThree = LTwoPOne3;
    //LTwoPFour = LTwoPOne4;
    MTwoPOne = MTwoPOne1;
    MTwoPTwo = MTwoPOne2;
    MTwoPThree = MTwoPOne3;
    //MTwoPFour = MTwoPOne4;
    RTwoPOne = RTwoPOne1;
    RTwoPTwo = RTwoPOne2;
    RTwoPThree = RTwoPOne3;
    //RTwoPFour = RTwoPOne4;

    saveImage2 = Highcharts.chart('chartplace2', {
        chart: {
            backgroundColor: '#FCFCFC',
            type: 'line',
            //borderWidth:1,
            plotBorderWidth: 1,
            spacingRight: 10
        },
        title: {
            text: '2号仓位轮胎温度折线图'
        },
        series: [{
            name: cam_number2+'胎内子口温度', data: LTwoPOne
        },{
            name: cam_number2+'胎内肩部温度', data: LTwoPTwo
        },{
            name: cam_number2+'胎内侧部中间温度', data: LTwoPThree, visible:false
        },{
            name: cam_number2+'胎冠区域温度', data: MTwoPOne
        },{
            name: cam_number2+'胎冠中间温度', data: MTwoPTwo, visible:false
        },{
            name: cam_number2+'胎冠肩部温度', data: MTwoPThree, visible:false
        },{
            name: cam_number2+'胎外子口温度', data: RTwoPOne
        },{
            name: cam_number2+'胎外肩部温度', data: RTwoPTwo
        },{
            name: cam_number2+'胎外侧部中间温度', data: RTwoPThree, visible:false
        }],
        legend: {
            layout: 'vertical',
            align: 'right',
            verticalAlign: 'middle'
        },
        yAxis: {
            min: 0,
            max: 120,
            // tickPixelInterval:30, //调整y轴刻度的间距
            tickInterval: 10, //刻度值
            title: {
                text: '温度值'
            },
            gridLineColor: '#CCCCCC',
            gridLineWidth: 1
        },
        xAxis: {
            gridLineColor: '#CCCCCC',
            gridLineWidth: 0
        },
        lines: {
            show: true,
            fill: true,
            fillColor: {colors: [{opacity: 0.05}, {opacity: 0.15}, {opacity: 0.25}]}
        },
        credits: {
            enabled: false
        },
        grid: {
            hoverable: true, clickable: true,
            borderColor: '#ccc',
            borderWidth: 1,
            labelMargin: 10
        },
        plotOptions: {
            allowPointSelect: true,
            series: {
                cursor: 'pointer', //鼠标滑过数据列时鼠标的形状
                events: {
                    click: function (e) {
                        // alert(this.name)
                    }
                }
            }
        },
        exporting: {
            sourceWidth: 1000,
            sourceHeight: 400,
            enabled: false
        }
    });
}

function ImageSaveFile() {
    var position1 = document.getElementsByName('chkRadio1')[0].checked;
    var position2 = document.getElementsByName('chkRadio2')[0].checked;
    if(!position1 && !position2){
        alert("请选择仓位号!");
    }else {
        var svg1 = saveImage1.getSVG();
        var svg2 = saveImage2.getSVG();
        var room_number= $("#roomnumber").val();
        var sample_test_date1 = $("#sample_test_date1").val();
        var sample_test_date2 = $("#sample_test_date2").val();
        var tire_number1 = $("#tire_number1").val();
        var tire_number2 =$("#tire_number2").val();
        $.ajax({
            url:'saveImageToLocal',
            type:"post",
            data:{
                "svg1":svg1,
                "svg2":svg2,
                "room_number":room_number,
                "position1":position1,
                "position2":position2,
                "sample_test_date1":sample_test_date1,
                "sample_test_date2":sample_test_date2,
                "tire_number1":tire_number1,
                "tire_number2":tire_number2
            },
            dataType:'json',
            catch:false,
            async:false,
            success:function (data) {

            },
            error:function (msg) {


            }
        });
    }
}
function cleanSpelChar(th){
    if(/["'<>%;)(&+]/.test(th.value)){
        $(th).val(th.value.replace(/["'<>%;)(&+]/,""));
    }

}
