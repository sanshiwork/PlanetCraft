var ws = (function () {
    // 连接服务器
    var ws = new WebSocket("ws://localhost:8080/planet/craft/socket");

    //连接发生错误的回调方法
    ws.onerror = function () {

    };

    //连接成功建立的回调方法
    ws.onopen = function (event) {

    };

    //接收到消息的回调方法
    ws.onmessage = function (event) {

    };

    //连接关闭的回调方法
    ws.onclose = function () {

    };

    //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
    window.onbeforeunload = function () {
        ws.close();
    };

    return ws;
})();