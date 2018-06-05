if (window.console) {
    console.log("Welcome to your Play application's JavaScript!");
}

window.onload = function () {
    var socket = new WebSocket("ws://localhost:9001/chat");
    socket.onmessage = function (event) {
        console.log(event);
    }
};
