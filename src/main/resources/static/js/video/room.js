// 管理房间内摄像头信息


function insertRoomInfo(){
    var room = new Object();
    room.room_number = document.getElementById("room_number").value;
    room.cam_id = document.getElementById("cam_id").value;
    room.cam_position = document.getElementById("cam_position").value;
    room.cam_ip = document.getElementById("cam_ip").value;
    room.cam_user = document.getElementById("cam_user").value;
    room.cam_pwd = document.getElementById("cam_pwd").value;
    room.cam_port = document.getElementById("cam_port").value;
    room.cam_type = document.getElementById("cam_type").value;
    room.cam_channel = document.getElementById("cam_channel").value;
    room.cam_station_number = document.getElementById("cam_station_number").value;
  //  alert(sampleInfo.cam_station_number);
 //   alert(dataCamInfo[0].cam_ip);
    var JSONObj = new Object();
    var roomI = JSON.stringify(room);
    JSONObj.roomInfo = roomI;

    $.ajax({
        url: '/insertRoomInfo',
        type: "POST",
        data: JSONObj,
        cache: false,
        //      contentType: 'application/json;charset=utf-8',
        dataType:'json',
        error: function () {
            // alert(sampleInfo.room_number);
     //       alert("失败！")
        },
        success: function () {
           // alert("成功")
        }
    });
}
