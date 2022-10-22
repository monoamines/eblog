layui.use('layim', function(layim){

    //先来个客服模式压压精
       layim.config({
          brief: true //是否简约模式（如果true则不显示主面板）

     });

    var $ = layui.jquery;

  var tiows =new tio.ws($,layim);

    tiows.openChatWindow();
    tiows.connect();


    //发送消息
    layim.on('sendMessage',function (res) {
        tiows.sendChatMessage(res)
    })

});