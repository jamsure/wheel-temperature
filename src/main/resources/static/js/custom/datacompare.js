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
    var OneLMaxTemp = [[0, 2], [1, 6], [2, 3], [3, 8], [4, 5], [5, 13], [6, 20]];
    var OneLMaxTemp1 = [[0, 2], [1, 6], [2, 3], [3, 8], [4, 5], [5, 13], [6, 20]];
    var OneLMaxTemp2 = [[0, 2], [1, 6], [2, 3], [3, 8], [4, 5], [5, 13], [6, 20]];
    var TwoLMaxTemp = [[0, 5], [1, 4], [2, 4], [3, 1], [4, 9], [5, 10], [6, 20]];
    var TwoLMaxTemp1 = [[0, 5], [1, 4], [2, 4], [3, 1], [4, 9], [5, 10], [6, 20]];
    var TwoLMaxTemp2 = [[0, 5], [1, 4], [2, 4], [3, 1], [4, 9], [5, 10], [6, 20]];
    var OneMMaxTemp = [[0, 7], [1, 7], [2, 7], [3, 8], [4, 4], [5, 11], [6, 20]];
    var OneMMaxTemp1 = [[0, 7], [1, 7], [2, 7], [3, 8], [4, 4], [5, 11], [6, 20]];
    var OneMMaxTemp2 = [[0, 7], [1, 7], [2, 7], [3, 8], [4, 4], [5, 11], [6, 20]];
    var TwoMMaxTemp = [[0, 2], [1, 6], [2, 3], [3, 8], [4, 5], [5, 13], [6, 20]];
    var TwoMMaxTemp1 = [[0, 2], [1, 6], [2, 3], [3, 8], [4, 5], [5, 13], [6, 20]];
    var TwoMMaxTemp2 = [[0, 2], [1, 6], [2, 3], [3, 8], [4, 5], [5, 13], [6, 20]];
    var OneRMaxTemp = [[0, 5], [1, 4], [2, 4], [3, 1], [4, 9], [5, 10], [6, 20]];
    var OneRMaxTemp1 = [[0, 5], [1, 4], [2, 4], [3, 1], [4, 9], [5, 10], [6, 20]];
    var OneRMaxTemp2 = [[0, 5], [1, 4], [2, 4], [3, 1], [4, 9], [5, 10], [6, 20]];
    var TwoRMaxTemp = [[0, 7], [1, 7], [2, 7], [3, 8], [4, 4], [5, 11], [6, 20]];
    var TwoRMaxTemp1 = [[0, 7], [1, 7], [2, 7], [3, 8], [4, 4], [5, 11], [6, 20]];
    var TwoRMaxTemp2 = [[0, 7], [1, 7], [2, 7], [3, 8], [4, 4], [5, 11], [6, 20]];

    $("#queryData1").on('click', function (){
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
        //console.log(sample_object_name);
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
                        if(checkStatus.data.length==2)
                        {
                            var data = checkStatus.data;
                            var stopTime = $("#stoptime").val();
                            if (stopTime.length == 0) {
                                stopTime = 0;
                            }
                            data[0].stopTime=stopTime;
                            data[1].stopTime=stopTime;
                            var DataFromHtml1 = new Array();
                            DataFromHtml1[0]=data[0].sample_test_date;
                            DataFromHtml1[1]=data[0].tire_number;
                            DataFromHtml1[2]=data[0].room_number;
                            DataFromHtml1[3]=data[0].cam_station_number;
                            var viewnumber1=DataFromHtml1[2]+"-"+DataFromHtml1[3]+" "+DataFromHtml1[1]+"号";
                            var DataFromHtml2 = new Array();
                            DataFromHtml2[0]=data[1].sample_test_date;
                            DataFromHtml2[1]=data[1].tire_number;
                            DataFromHtml2[2]=data[1].room_number;
                            DataFromHtml2[3]=data[1].cam_station_number;
                            var viewnumber2=DataFromHtml2[2]+"-"+DataFromHtml2[3]+" "+DataFromHtml2[1]+"号";

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
                                    var dataL = data1.listL;
                                    var dataL1= data1.listL1;
                                    var dataL2=data1.listL2;
                                    var dataM = data1.listM;
                                    var dataM1=data1.listM1;
                                    var dataM2=data1.listM2;
                                    var dataR = data1.listR;
                                    var dataR1=data1.listR1;
                                    var dataR2=data1.listR2;
                                    listIP1 = data1.listIP;
                                    // document.getElementById("CaptureImage1").src=data1.imageLink;
                                    if (data1.length != 0) {
                                        var getOneLMaxTemp = [];
                                        var getOneLMaxTemp1 = [];
                                        var getOneLMaxTemp2 = [];
                                        var getOneMMaxTemp = [];
                                        var getOneMMaxTemp1 = [];
                                        var getOneMMaxTemp2 = [];
                                        var getOneRMaxTemp = [];
                                        var getOneRMaxTemp1 = [];
                                        var getOneRMaxTemp2 = [];

                                        if (dataL != null){
                                            for (var i = 0;i<dataL.length;i++)
                                            {
                                                var list1 = [];
                                                list1[0] = dataL[i].plp_timediff;
                                                list1[1] = dataL[i].plp_fMaxTemperature ;
                                                getOneLMaxTemp.push(list1);
                                            }
                                        }
                                        if (dataL1 != null){
                                            for (var i = 0;i<dataL1.length;i++)
                                            {
                                                var list1 = [];
                                                list1[0] = dataL1[i].plp_timediff;
                                                list1[1] = dataL1[i].plp_fMaxTemperature ;
                                                getOneLMaxTemp1.push(list1);
                                            }
                                        }
                                        if (dataL2 != null){
                                            for (var i = 0;i<dataL2.length;i++)
                                            {
                                                var list1 = [];
                                                list1[0] = dataL2[i].plp_timediff;
                                                list1[1] = dataL2[i].plp_fMaxTemperature ;
                                                getOneLMaxTemp2.push(list1);
                                            }
                                        }
                                        if (dataM != null){
                                            for (var i = 0;i<dataM.length;i++)
                                            {
                                                var list2 = [];
                                                list2[0] = dataM[i].plp_timediff;
                                                list2[1] = dataM[i].plp_fMaxTemperature ;
                                                getOneMMaxTemp.push(list2);
                                            }
                                        }
                                        if (dataM1 != null){
                                            for (var i = 0;i<dataM1.length;i++)
                                            {
                                                var list2 = [];
                                                list2[0] = dataM1[i].plp_timediff;
                                                list2[1] = dataM1[i].plp_fMaxTemperature ;
                                                getOneMMaxTemp1.push(list2);
                                            }
                                        }
                                        if (dataM2 != null){
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
                                        setMaxTemperature(getOneLMaxTemp,getOneLMaxTemp1,getOneLMaxTemp2, getOneMMaxTemp,getOneMMaxTemp1,getOneMMaxTemp2, getOneRMaxTemp,getOneRMaxTemp1,getOneRMaxTemp2,DataFromHtml1,DataFromHtml2,viewnumber1,viewnumber2);
                                    }

                                    $.ajax({
                                        url: '/getDiffCamTempInfo',
                                        type: "post",
                                        contentType: "application/x-www-form-urlencoded; charset=utf-8",
                                        data: data[1],
                                        cache: false,
                                        dataType: "json",
                                        error: function () {
                                            alert("你所查询的内容不存在！")
                                        },
                                        success: function (data1) {
                                            var dataL = data1.listL;
                                            var dataL1=data1.listL1;
                                            var dataL2=data1.listL2;
                                            var dataM = data1.listM;
                                            var dataM1=data1.listM1;
                                            var dataM2=data1.listM2;
                                            var dataR = data1.listR;
                                            var dataR1=data1.listR1;
                                            var dataR2=data1.listR2;
                                            listIP2 = data1.listIP;
                                            //document.getElementById("CaptureImage2").src=data1.imageLink;
                                            if (data1.length != 0) {
                                                var getTwoLMaxTemp = [];
                                                var getTwoLMaxTemp1 = [];
                                                var getTwoLMaxTemp2 = [];
                                                var getTwoMMaxTemp = [];
                                                var getTwoMMaxTemp1 = [];
                                                var getTwoMMaxTemp2 = [];
                                                var getTwoRMaxTemp = [];
                                                var getTwoRMaxTemp1 = [];
                                                var getTwoRMaxTemp2 = [];
                                                if (dataL != null){
                                                    for (var i = 0;i<dataL.length;i++)
                                                    {
                                                        var list1 = [];
                                                        list1[0] = dataL[i].plp_timediff;
                                                        list1[1] = dataL[i].plp_fMaxTemperature ;
                                                        getTwoLMaxTemp.push(list1);
                                                    }
                                                }
                                                if (dataL1 != null){
                                                    for (var i = 0;i<dataL1.length;i++)
                                                    {
                                                        var list1 = [];
                                                        list1[0] = dataL1[i].plp_timediff;
                                                        list1[1] = dataL1[i].plp_fMaxTemperature ;
                                                        getTwoLMaxTemp1.push(list1);
                                                    }
                                                }
                                                if (dataL2 != null){
                                                    for (var i = 0;i<dataL2.length;i++)
                                                    {
                                                        var list1 = [];
                                                        list1[0] = dataL2[i].plp_timediff;
                                                        list1[1] = dataL2[i].plp_fMaxTemperature ;
                                                        getTwoLMaxTemp2.push(list1);
                                                    }
                                                }
                                                if (dataM != null){
                                                    for (var i = 0;i<dataM.length;i++)
                                                    {
                                                        var list2 = [];
                                                        list2[0] = dataM[i].plp_timediff;
                                                        list2[1] = dataM[i].plp_fMaxTemperature ;
                                                        getTwoMMaxTemp.push(list2);
                                                    }
                                                }
                                                if (dataM1 != null){
                                                    for (var i = 0;i<dataM1.length;i++)
                                                    {
                                                        var list2 = [];
                                                        list2[0] = dataM1[i].plp_timediff;
                                                        list2[1] = dataM1[i].plp_fMaxTemperature ;
                                                        getTwoMMaxTemp1.push(list2);
                                                    }
                                                }
                                                if (dataM2 != null){
                                                    for (var i = 0;i<dataM2.length;i++)
                                                    {
                                                        var list2 = [];
                                                        list2[0] = dataM2[i].plp_timediff;
                                                        list2[1] = dataM2[i].plp_fMaxTemperature ;
                                                        getTwoMMaxTemp2.push(list2);
                                                    }
                                                }
                                                if (dataR != null){
                                                    for (var i = 0;i<dataR.length;i++)
                                                    {
                                                        var list3 = [];
                                                        list3[0] = dataR[i].plp_timediff;
                                                        list3[1] = dataR[i].plp_fMaxTemperature ;
                                                        getTwoRMaxTemp.push(list3);
                                                    }
                                                }
                                                if (dataR2 != null){
                                                    for (var i = 0;i<dataR2.length;i++)
                                                    {
                                                        var list3 = [];
                                                        list3[0] = dataR2[i].plp_timediff;
                                                        list3[1] = dataR2[i].plp_fMaxTemperature ;
                                                        getTwoRMaxTemp2.push(list3);
                                                    }
                                                }
                                                if (dataR1 != null){
                                                    for (var i = 0;i<dataR1.length;i++)
                                                    {
                                                        var list3 = [];
                                                        list3[0] = dataR1[i].plp_timediff;
                                                        list3[1] = dataR1[i].plp_fMaxTemperature ;
                                                        getTwoRMaxTemp1.push(list3);
                                                    }
                                                }
                                                setMaxTemperatureSec(getTwoLMaxTemp,getTwoLMaxTemp1,getTwoLMaxTemp2,getTwoMMaxTemp,getTwoMMaxTemp1,getTwoMMaxTemp2, getTwoRMaxTemp,getTwoRMaxTemp1,getTwoRMaxTemp2,DataFromHtml1,DataFromHtml2);

                                            }

                                        }
                                    });
                                }
                            });

                        }
                        else{
                            layer.msg('请选择两条数据！',{offset: [200,1000]});
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
                //console.log(obj)
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
                     downloadExcel1(dataFromHtml)

                }
            });


        });

    });


    $("#queryData2").on('click', function (){
        $("#tb2").html("");
        var room_number1 = $("#room_number1").val();
        var cam_station_number1 = $("#cam_station_number1").val();
        var tire_number1 = $("#tire_number1").val();
        var sample_specification = $("#sample_specification1").val();
        var sample_level = $("#sample_level1").val();
        var sample_pattern = $("#sample_pattern1").val();
        var sample_test_date = $("#sample_test_date1").val();
        var sample_number = $("#sample_number1").val();
        var sample_person = $("#sample_person1").val();
        var stopTime = $("#stoptime1").val();
        $.ajax({
            url: '/queryData',
            type: "post",
            contentType: "application/x-www-form-urlencoded; charset=utf-8",
            data: {
                "room_number":room_number1,
                "tire_number":tire_number1,
                "cam_station_number":cam_station_number1,
                "stopTime":stopTime,
                "sample_specification":sample_specification,
                "sample_level":sample_level,
                "sample_pattern":sample_pattern,
                "sample_number":sample_number,
                "sample_person":sample_person,
                "sample_test_date":sample_test_date
            },
            cache: false,
            dataType: "json",
            error: function () {
                 alert("数据查询错误，请重试！")
            },
            success: function (data) {
                sampleData2=data;
                var str="";
                if (data.list.length != 0) {
                    // var str1="<tr><td>规格</td><td>层级</td><td>花纹</td><td>试制编号</td><td>送样人</td><td>试验日期</td><td>轮胎工号</td><td>机床号</td><td>工位号</td><td>相机IP</td><td>下载报表</td></tr>"
                    // $("#tb2").append(str1);
                    for (var i = 0; i < data.list.length; i++) {
                        str = "<tr><td>" + data.list[i].sample_specification + "</td><td>" + data.list[i].sample_level + "</td><td>" + data.list[i].sample_pattern+ "</td><td>" + data.list[i].sample_number + "</td><td>" + data.list[i].sample_person + "</td><td>" + data.list[i].sample_test_date + "</td><td>" + data.list[i].tire_number + "</td><td>" + data.list[i].room_number + "</td><td>" + data.list[i].cam_station_number + "</td><td><a href='#'>" + "下载" + "</a></td></tr>";
                        $("#tb2").append(str);
                        $("#wtb2").trigger("create");
                        $("#wtb2").css("display","block");

                    }
                }
            }
        });
    });


    //点击html页面单元格获取数据并跳转查询
    $("#tb1").on('dblclick', function () {
        var dataFromHtml = new Array();
        var content;
        var rowNumber;
        var cellNumber;
        var tabs = document.getElementById("tb1");
        var td = event.srcElement; // 通过event.srcelement 获取激活事件的对象 td
        content = td.innerHTML;
        if(content.length != 0){
            rowNumber = td.parentElement.rowIndex;
            cellNumber = td.cellIndex;
            console.log("行号：" + rowNumber + "，内容：" + td.innerHTML);
            if(cellNumber>=0 && cellNumber <9) {
                for(var m=0;m<4;m++){
                    dataFromHtml[m] = tabs.rows[rowNumber].cells[m+5].innerHTML;
                }
            }
            getDataBoth(dataFromHtml);
        }

    });
    $("#tb1").on('click', function () {
        var dataFromHtml = new Array();
        var content;
        var rowNumber;
        var cellNumber;
        var tabs = document.getElementById("tb1");
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
                downloadExcel1(dataFromHtml);
            }

        }

    });
    function downloadExcel1(dataFromHtml){
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




//点击html页面单元格获取数据并跳转查询
    $("#tb2").on('dblclick', function () {
        var dataFromHtml = new Array();
        var content;
        var rowNumber;
        var cellNumber;
        var tabs = document.getElementById("tb2");
        var td = event.srcElement; // 通过event.srcelement 获取激活事件的对象 td
        content = td.innerHTML;
        if(content.length != 0){
            rowNumber = td.parentElement.rowIndex;
            cellNumber = td.cellIndex;
            console.log("行号：" + rowNumber + "，内容：" + td.innerHTML);
            if(cellNumber>=0 && cellNumber <9) {
                for (var m = 0; m < 4; m++) {
                    dataFromHtml[m] = tabs.rows[rowNumber].cells[m + 5].innerHTML;
                }
            }
            getDataBothSec(dataFromHtml);
        }

    });
    $("#tb2").on('click', function () {
        var dataFromHtml = new Array();
        var content;
        var rowNumber;
        var cellNumber;
        var tabs = document.getElementById("tb2");
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
                downloadExcel2(dataFromHtml);
            }

        }

    });
    function downloadExcel2(dataFromHtml){
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

    var listIP1='',dataFromHtml1='';
    function getDataBoth(dataFromHtml) {
        dataFromHtml1 = dataFromHtml;
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
                listIP1 = data1.listIP;
                // document.getElementById("CaptureImage1").src=data1.imageLink;
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
                    }
                    if (dataM != null){
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
                    setMaxTemperature(getOneLMaxTemp, getOneMMaxTemp, getOneRMaxTemp);
                }

            }
        });

    }

    var listIP2='',dataFromHtml2='';
    function getDataBothSec(dataFromHtml) {
        dataFromHtml2 = dataFromHtml;
        var sample_test_date = dataFromHtml[0];
        var tire_number = dataFromHtml[1];
        var room_number = dataFromHtml[2];
        var cam_station_number = dataFromHtml[3];
        var stopTime1 = $("#stoptime1").val();
        if(stopTime1.length == 0){
            stopTime1 = 0;
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
                "stopTime": stopTime1
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
                listIP2 = data1.listIP;
                document.getElementById("CaptureImage2").src=data1.imageLink;
                if (data1.length != 0) {
                    var getTwoLMaxTemp = [];
                    var getTwoMMaxTemp = [];
                    var getTwoRMaxTemp = [];
                    if (dataL != null){
                        for (var i = 0;i<dataL.length;i++)
                        {
                            var list1 = [];
                            list1[0] = dataL[i].plp_timediff;
                            list1[1] = dataL[i].plp_fMaxTemperature ;
                            getTwoLMaxTemp.push(list1);
                        }
                    }
                    if (dataM != null){
                        for (var i = 0;i<dataM.length;i++)
                        {
                            var list2 = [];
                            list2[0] = dataM[i].plp_timediff;
                            list2[1] = dataM[i].plp_fMaxTemperature ;
                            getTwoMMaxTemp.push(list2);
                        }
                    }
                    if (dataR != null){
                        for (var i = 0;i<dataR.length;i++)
                        {
                            var list3 = [];
                            list3[0] = dataR[i].plp_timediff;
                            list3[1] = dataR[i].plp_fMaxTemperature ;
                            getTwoRMaxTemp.push(list3);
                        }
                    }
                    setMaxTemperatureSec(getTwoLMaxTemp, getTwoMMaxTemp, getTwoRMaxTemp);

                }

            }
        });
    }

    function setMaxTemperature(getOneLMaxTemp,getOneLMaxTemp1,getOneLMaxTemp2, getOneMMaxTemp,getOneMMaxTemp1,getOneMMaxTemp2, getOneRMaxTemp,getOneRMaxTemp1,getOneRMaxTemp2,DataFromHtml1,DataFromHtml2,viewnumber1,viewnumber2) {
        OneLMaxTemp = getOneLMaxTemp;
        OneLMaxTemp1= getOneLMaxTemp1;
        OneLMaxTemp2= getOneLMaxTemp2
        OneMMaxTemp=  getOneMMaxTemp;
        OneMMaxTemp1= getOneMMaxTemp1;
        OneMMaxTemp2= getOneMMaxTemp2;
        OneRMaxTemp = getOneRMaxTemp;
        OneRMaxTemp1= getOneRMaxTemp1;
        OneRMaxTemp2= getOneRMaxTemp2;
        rePlotL(DataFromHtml1,DataFromHtml2,viewnumber1,viewnumber2);
        rePlotM(DataFromHtml1,DataFromHtml2,viewnumber1,viewnumber2);
        rePlotR(DataFromHtml1,DataFromHtml2,viewnumber1,viewnumber2);
    }

    function setMaxTemperatureSec(getTwoLMaxTemp,getTwoLMaxTemp1,getTwoLMaxTemp2, getTwoMMaxTemp,getTwoMMaxTemp1,getTwoMMaxTemp2,getTwoRMaxTemp,getTwoRMaxTemp1,getTwoRMaxTemp2,DataFromHtml1,DataFromHtml2) {
        TwoLMaxTemp = getTwoLMaxTemp;
        TwoLMaxTemp1 = getTwoLMaxTemp1;
        TwoLMaxTemp2 = getTwoLMaxTemp2;
        TwoMMaxTemp = getTwoMMaxTemp;
        TwoMMaxTemp1 = getTwoMMaxTemp1;
        TwoMMaxTemp2 = getTwoMMaxTemp2;
        TwoRMaxTemp = getTwoRMaxTemp;
        TwoRMaxTemp1 = getTwoRMaxTemp1;
        TwoRMaxTemp2 = getTwoRMaxTemp2;
        rePlotL(DataFromHtml1,DataFromHtml2);
        rePlotM(DataFromHtml1,DataFromHtml2);
        rePlotR(DataFromHtml1,DataFromHtml2);
    }

    //下载折线图到指定路径
    var saveImageL,saveImageM,saveImageR;
    $("#saveImage").on('click',function () {
        var svg = saveImageL.getSVG();
        $.ajax({
            url:'saveImageToLocalThree',
            type:"post",
            data:{
                'svg':svg
            },
            dataType:'Json',
            catch:false,
            async:false,
            success:function (data) {
                alert("图片保存成功");
            },
            error:function () {
                alert("图片保存失败");
            }
        });
    });


    //绘制折线图
    //设置折线图折线默认颜色
    Highcharts.setOptions({
        colors:['#ED561B','#058DC7']
    });
    function rePlotL(DataFromHtml1,DataFromHtml2,viewnumber1,viewnumber2) {
        var cam_ipOneL,cam_ipTwoL;
        console.log(listIP1);
        console.log(listIP2);
        if(listIP1.length != 0) {
            for (var i = 0; i < listIP1.length; i++) {
                if (listIP1[i].cam_position == "L") {
                    cam_ipOneL = listIP1[i].cam_ip;
                }
            }
        }
        if(listIP2.length != 0) {
            for (var j = 0; j < listIP2.length; j++) {
                if (listIP2[j].cam_position == "L") {
                    cam_ipTwoL = listIP2[j].cam_ip;
                }
            }
        }
        saveImageL = Highcharts.chart('chartplacel',{
            chart:{
                backgroundColor:'#FCFCFC',
                type:'line',
                //borderWidth:1,
                plotBorderWidth:1,
                spacingRight:30
            },
            title:{
                text:'轮胎温度对比折线图'
            },
            series:[{
                name:'1号胎内子口温度',
                data:OneLMaxTemp
            },{
                name:'2号胎内子口温度',
                data:TwoLMaxTemp
            },{
                name:'1号胎内肩部温度',
                data:OneLMaxTemp1
            },{
                name:'2号胎内肩部温度',
                data:TwoLMaxTemp1
            },{
                name:'1号胎内侧部中间温度',
                data:OneLMaxTemp2
            },{
                name:'2号胎内侧部中间温度',
                data:TwoLMaxTemp2
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
                            // alert(e.point.category);
                            //  alert(e.point.x.toFixed(2));
                            if (e) {
                                console.log(this.name);
                                if(this.name == "1号胎内子口温度"||this.name=="1号胎内肩部温度"||this.name=="1号胎内侧部中间温度"){
                                    DataFromHtml1[4] = cam_ipOneL;
                                    getImage(e.point.x.toFixed(2), DataFromHtml1)
                                }else if(this.name == "2号胎内子口温度"||this.name=="2号胎内肩部温度"||this.name=="2号胎内侧部中间温度"){
                                    DataFromHtml2[4] = cam_ipTwoL;
                                    getImageSec(e.point.x.toFixed(2), DataFromHtml2);
                                }
                                //console.log(dataFromHtml1);
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
    //默认折线图
    saveImageL = Highcharts.chart('chartplacel',{
        chart:{
            backgroundColor:'#FCFCFC',
            type:'line',
            //borderWidth:1,
            plotBorderWidth:1,
            spacingRight:30
        },
        title:{
            text:'轮胎温度对比折线图'
        },
        series:[{
            name:'1号胎内子口温度',
            data:OneLMaxTemp
        },{
            name:'2号胎内子口温度',
            data:TwoLMaxTemp
        },{
            name:'1号胎内肩部温度',
            data:OneLMaxTemp1
        },{
            name:'2号胎内肩部温度',
            data:TwoLMaxTemp1
        },{
            name:'1号胎内侧部中间温度',
            data:OneLMaxTemp2
        },{
            name:'2号胎内侧部中间温度',
            data:TwoLMaxTemp2
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


    function rePlotM(DataFromHtml1,DataFromHtml2) {
        var cam_ipOneM,cam_ipTwoM;
        if(listIP1.length != 0) {
            for (var i = 0; i < listIP1.length; i++) {
                if (listIP1[i].cam_position == "M") {
                    cam_ipOneM = listIP1[i].cam_ip;
                }
            }
        }
        if(listIP2.length != 0) {
            for (var j = 0; j < listIP2.length; j++) {
                if (listIP2[j].cam_position == "M") {
                    cam_ipTwoM = listIP2[j].cam_ip;
                }
            }
        }
        saveImageR = Highcharts.chart('chartplacem',{
            chart:{
                backgroundColor:'#FCFCFC',
                type:'line',
                //borderWidth:1,
                plotBorderWidth:1,
                spacingRight:30
            },
            title:{
                text:'轮胎温度对比折线图'
            },
            series:[{
                name:'1号胎冠区域温度',
                data:OneMMaxTemp
            },{
                name:'2号胎冠区域温度',
                data:TwoMMaxTemp
            },{
                name:'1号胎冠中间温度',
                data:OneMMaxTemp1
            },{
                name:'2号胎冠中间温度',
                data:TwoMMaxTemp1
            },{
                name:'1号胎冠肩部温度',
                data:OneMMaxTemp2
            },{
                name:'2号胎冠肩部温度',
                data:TwoMMaxTemp2
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
                            // alert(e.point.category);
                            //  alert(e.point.x.toFixed(2));
                            if (e) {
                                console.log(this.name);
                                if(this.name == "1号胎冠区域温度"||this.name == "1号胎冠中间温度"||this.name == "1号胎冠肩部温度"){
                                    DataFromHtml1[4] = cam_ipOneM;
                                    getImage(e.point.x.toFixed(2), DataFromHtml1)
                                }else if(this.name == "2号胎冠区域温度"||this.name == "2号胎冠中间温度"||this.name == "2号胎冠肩部温度"){
                                    DataFromHtml2[4] = cam_ipTwoM;
                                    getImageSec(e.point.x.toFixed(2), DataFromHtml2);
                                }
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
    //默认折线图
    saveImageM = Highcharts.chart('chartplacem',{
        chart:{
            backgroundColor:'#FCFCFC',
            type:'line',
            //borderWidth:1,
            plotBorderWidth:1,
            spacingRight:30
        },
        title:{
            text:'轮胎温度对比折线图'
        },
        series:[{
            name:'1号胎冠区域温度',
            data:OneMMaxTemp
        },{
            name:'2号胎冠区域温度',
            data:TwoMMaxTemp
        },{
            name:'1号胎冠中间温度',
            data:OneMMaxTemp1
        },{
            name:'2号胎冠中间温度',
            data:TwoMMaxTemp1
        },{
            name:'1号胎冠肩部温度',
            data:OneMMaxTemp2
        },{
            name:'2号胎冠肩部温度',
            data:TwoMMaxTemp2
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


    function rePlotR(DataFromHtml1,DataFromHtml2) {
        var cam_ipOneR,cam_ipTwoR;
        if(listIP1.length != 0){
            for(var i=0;i<listIP1.length;i++){
                if(listIP1[i].cam_position == "R"){
                    cam_ipOneR = listIP1[i].cam_ip;
                }
            }
        }
        if(listIP2.length != 0){
            for(var j=0;j<listIP2.length;j++){
                if(listIP2[j].cam_position == "R"){
                    cam_ipTwoR = listIP2[j].cam_ip;
                }
            }
        }
        saveImageR = Highcharts.chart('chartplacer',{
            chart:{
                backgroundColor:'#FCFCFC',
                type:'line',
                //borderWidth:1,
                plotBorderWidth:1,
                spacingRight:30
            },
            title:{
                text:'轮胎温度对比折线图'
            },
            series:[{
                name:'1号胎外子口温度',
                data:OneRMaxTemp
            },{
                name:'2号胎外子口温度',
                data:TwoRMaxTemp
            },{
                name:'1号胎外肩部温度',
                data:OneRMaxTemp1
            },{
                name:'2号胎外肩部温度',
                data:TwoRMaxTemp1
            },{
                name:'1号胎外侧部中间温度',
                data:OneRMaxTemp2
            },{
                name:'2号胎外侧部中间温度',
                data:TwoRMaxTemp2
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
                            // alert(e.point.category);
                            //  alert(e.point.x.toFixed(2));
                            if (e) {
                                console.log(this.name);
                                if(this.name == "1号胎外子口温度"||this.name == "1号胎外测温点温度"||this.name == "1号胎外侧部中间温度"){
                                    DataFromHtml1[4] = cam_ipOneR;
                                    getImage(e.point.x.toFixed(2), DataFromHtml1)
                                }else if(this.name == "2号胎外子口温度"||this.name == "2号胎外肩部温度"||this.name == "2号胎外侧部中间温度"){
                                    DataFromHtml2[4] = cam_ipTwoR;
                                    getImageSec(e.point.x.toFixed(2), DataFromHtml2);
                                }
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
    //默认折线图
    saveImageR = Highcharts.chart('chartplacer',{
        chart:{
            backgroundColor:'#FCFCFC',
            type:'line',
            //borderWidth:1,
            plotBorderWidth:1,
            spacingRight:30
        },
        title:{
            text:'轮胎温度对比折线图'
        },
        series:[{
            name:'1号胎外子口温度',
            data:OneRMaxTemp
        },{
            name:'2号胎外子口温度',
            data:TwoRMaxTemp
        },{
            name:'1号胎外肩部温度',
            data:OneRMaxTemp1
        },{
            name:'2号胎外肩部温度',
            data:TwoRMaxTemp1
        },{
            name:'1号胎外侧部中间温度',
            data:OneRMaxTemp2
        },{
            name:'2号胎外侧部中间温度',
            data:TwoRMaxTemp2
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
        $.ajax({
            url: '/getImage',
            type: "post",
            contentType: "application/x-www-form-urlencoded; charset=utf-8",
            data: {
                "sample_test_date": sample_test_date,
                "tire_number": tire_number,
                "cam_ip": cam_ip,
                "room_number": room_number,
                "cam_station_number": cam_station_number,
                "timeDiff": timeDiff
            },
            cache: false,
            dataType: "text",
            error: function () {
                alert("你所查询的内容不存在！")
            },
            success: function (data) {
                document.getElementById("CaptureImage1").src = data;
            }
        });
    }

    //get the link of the image for the second image
    function getImageSec(timeDiff, dataFromHtml) {
        var sample_test_date = dataFromHtml[0];
        var tire_number = dataFromHtml[1];
        var room_number = dataFromHtml[2];
        var cam_station_number = dataFromHtml[3];
        var cam_ip = dataFromHtml[4];
        var timeDiff = parseInt(timeDiff);
        $.ajax({
            url: '/getImage',
            type: "post",
            contentType: "application/x-www-form-urlencoded; charset=utf-8",
            data: {
                "sample_test_date": sample_test_date,
                "tire_number": tire_number,
                "cam_ip": cam_ip,
                "room_number": room_number,
                "cam_station_number": cam_station_number,
                "timeDiff": timeDiff
            },
            cache: false,
            dataType: "text",
            error: function () {
                alert("你所查询的内容不存在！")
            },
            success: function (data) {
                document.getElementById("CaptureImage2").src = data;
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

function ChooseFile1() {
    $("#camIP1").find("option").not(":first").remove();
    $("#ruleID1").find("option").not(":first").remove();
    var fileInput = document.getElementById('chooseFile1');
    var fileName;
    // fileInput.addEventListener('change', function() {
    // 获取File引用:
    var file = fileInput.files[0];
    fileName = file.name;

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
                $("#room_number1").val(data.room_number);
                $("#cam_station_number1").val(data.cam_station_number);
                $("#sample_specification1").val(data.sample_specification);
                $("#sample_level1").val(data.sample_level);
                $("#sample_pattern1").val(data.sample_pattern);
                $("#tire_number1").val(data.tire_number);
                $("#sample_test_date1").val(data.sample_test_date);
                for (var i = 0; i < data.cam_IPList.length; i++) {
                    var option = "<option value=\"" + data.cam_IPList[i] + "\">" + data.cam_IPList[i] + "</option>";  //动态添加数据
                    $("select[name=camIP1]").append(option);
                }
            }
        }
    });
}


$("#camIP1").change(function () {
    $("#ruleID1").find("option").not(":first").remove();
    var room_number1 = $("#room_number1").val();
    var cam_station_number1 = $("#cam_station_number1").val();
    var tire_number1 = $("#tire_number1").val();
    var sample_test_date1 = $("#sample_test_date1").val();
    var cam_ip1 = $("#camIP1").val();
    $.ajax({
        url: '/getRuleID',
        type: "post",
        contentType: "application/x-www-form-urlencoded; charset=utf-8",
        data: {
            "tire_number": tire_number1,
            "cam_station_number":cam_station_number1,
            "room_number": room_number1,
            "sample_test_date": sample_test_date1,
            "cam_ip": cam_ip1
        },
        cache: false,
        dataType: "json",
        error: function () {
            alert("你所查询的内容不存在！")
        },
        success: function (data) {
            if (data.length != 0) {
                $("#starttime1").val(data.fixStartTime);
                for (var i = 0; i < data.list.length; i++) {
                    var option = "<option value=\"" + data.list[i] + "\">" +data.list[i] + "</option>";  //动态添加数据
                    $("select[name=ruleID1]").append(option);
                }
            }
        }
    });
});


function ChooseFile2() {
    $("#camIP2").find("option").not(":first").remove();
    $("#ruleID2").find("option").not(":first").remove();
    var fileInput = document.getElementById('chooseFile2');
    var fileName;
    // fileInput.addEventListener('change', function() {
    // 获取File引用:
    var file = fileInput.files[0];
    fileName = file.name;

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
                $("#room_number2").val(data.room_number);
                $("#cam_station_number2").val(data.cam_station_number);
                $("#sample_specification2").val(data.sample_specification);
                $("#sample_level2").val(data.sample_level);
                $("#sample_pattern2").val(data.sample_pattern);
                $("#tire_number2").val(data.tire_number);
                $("#sample_test_date2").val(data.sample_test_date);
                for (var i = 0; i < data.cam_IPList.length; i++) {
                    var option = "<option value=\"" + data.cam_IPList[i] + "\">" + data.cam_IPList[i] + "</option>";  //动态添加数据
                    $("select[name=camIP2]").append(option);
                }
            }
        }
    });
}
function downloadExcel(){
    //定义一个form表单
    var form = $("<form>");
    form.attr("style", "display:none");
    form.attr("target", "");
    form.attr("action", '/downloadFile');
    form.attr("method","POST");
    var sample_specification = $("<input name='sample_spec'>");
    //sample_specification.attr("type", "hidden");
    sample_specification.attr("value", $("#sample_specification").val());
    var sample_level = $("<input name='sample_lev'>");
    //sample_level.attr("type","hidden");
    sample_level.attr("value",$("#sample_level").val());
    var sample_pattern=$("<input name='sample_patt'>");
    //sample_pattern.attr("type","hidden");
    sample_pattern.attr("value",$("#sample_pattern").val());
    var sample_test_date=$("<input name='sample_tes'>");
    //sample_test_date.attr("type","hidden");
    sample_test_date.attr("value",$("#sample_test_date").val());
    var sample_number=$("<input name='sample_num'>");
    //sample_number.attr("type","hidden");
    sample_number.attr("value",$("#sample_number").val());
    var sample_person=$("<input name='sample_per'>");
    //sample_person.attr("type","hidden");
    sample_person.attr("value",$("#sample_person").val());
    var tire_number=$("<input name='tire_num'>");
    // tire_number.attr("type","hidden");
    tire_number.attr("value",$("#tire_number").val());
    var room_number=$("<input name='room_num'>");
    //room_number.attr("type","hidden");
    room_number.attr("value",$("#roomN").val());
    //console.log(document.getElementById("roomN").innerHTML);
    var cam_station_number=$("<input name='cam_station_num'>");
    //cam_station_number.attr("type","hidden");
    cam_station_number.attr("value",$("#cam_station_number").val());
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
function downloadExcel1(){
    //定义一个form表单
    var form = $("<form>");
    form.attr("style", "display:none");
    form.attr("target", "");
    form.attr("action", '/downloadFile');
    form.attr("method","POST");
    var sample_specification = $("<input name='sample_spec'>");
    //sample_specification.attr("type", "hidden");
    sample_specification.attr("value", $("#sample_specification1").val());
    var sample_level = $("<input name='sample_lev'>");
    //sample_level.attr("type","hidden");
    sample_level.attr("value",$("#sample_level1").val());
    var sample_pattern=$("<input name='sample_patt'>");
    //sample_pattern.attr("type","hidden");
    sample_pattern.attr("value",$("#sample_pattern1").val());
    var sample_test_date=$("<input name='sample_tes'>");
    //sample_test_date.attr("type","hidden");
    sample_test_date.attr("value",$("#sample_test_date1").val());
    var sample_number=$("<input name='sample_num'>");
    //sample_number.attr("type","hidden");
    sample_number.attr("value",$("#sample_number1").val());
    var sample_person=$("<input name='sample_per'>");
    //sample_person.attr("type","hidden");
    sample_person.attr("value",$("#sample_person1").val());
    var tire_number=$("<input name='tire_num'>");
    // tire_number.attr("type","hidden");
    tire_number.attr("value",$("#tire_number1").val());
    var room_number=$("<input name='room_num'>");
    //room_number.attr("type","hidden");
    room_number.attr("value",$("#roomN1").val());
    //console.log(document.getElementById("roomN").innerHTML);
    var cam_station_number=$("<input name='cam_station_num'>");
    //cam_station_number.attr("type","hidden");
    cam_station_number.attr("value",$("#cam_station_number1").val());
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


$("#camIP2").change(function () {
    $("#ruleID2").find("option").not(":first").remove();
    var room_number2 = $("#room_number2").val();
    var cam_station_number2 = $("#cam_station_number2").val();
    var tire_number2 = $("#tire_number2").val();
    var sample_test_date2 = $("#sample_test_date2").val();
    var cam_ip2 = $("#camIP2").val();
    $.ajax({
        url: '/getRuleID',
        type: "post",
        contentType: "application/x-www-form-urlencoded; charset=utf-8",
        data: {
            "room_number": room_number2,
            "cam_station_number": cam_station_number2,
            "tire_number": tire_number2,
            "sample_test_date": sample_test_date2,
            "cam_ip": cam_ip2
        },
        cache: false,
        dataType: "json",
        error: function () {
            alert("你所查询的内容不存在！")
        },
        success: function (data) {
            if (data.length != 0) {
                $("#starttime2").val(data.fixStartTime);
                for (var i = 0; i < data.list.length; i++) {
                    var option = "<option value=\"" + data.list[i] + "\">" +data.list[i] + "</option>";  //动态添加数据
                    $("select[name=ruleID2]").append(option);
                }
            }
        }
    });
});


