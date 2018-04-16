function callNativeApp () {
    try {
        webkit.messageHandlers.callbackHandler.postMessage("Send from JavaScript");
    } catch(err) {
        console.log('error');
    }
}

setTimeout(function () {
    callNativeApp();
}, 5000);

function redHeader() {
    document.querySelector('h1').style.color = "red";
}


function sendAlert ( ) {
    try {
        webkit.messageHandlers.callbackHandler.postMessage("This is an alert");
    } catch(err) {
        console.log('error');
    }
}
