function startService(action, operation) {
    let xhr = new XMLHttpRequest();
    let hostname = location.hostname;
    xhr.open('POST',  window.location.protocol + '//' + hostname + ':8090/is_working', true);
    xhr.setRequestHeader('Access-Control-Allow-Origin', '*');
    xhr.setRequestHeader('Access-Control-Allow-Methods', 'POST');
    xhr.setRequestHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.onload = function () {
        if (xhr.status === 200) {
            let data = JSON.parse(xhr.responseText);
            let status = data["result"];
            console.log(status);
            if (operation === "service"){
                if (status) {

                }
            }
            else if (operation === "check"){
                let startButton = document.getElementById("start");
                let stopButton = document.getElementById("stop");
                let btnStatus;
                if (status){
                    btnStatus = true;
                } else {
                    btnStatus = false;
                }
                stopButton.disabled = !btnStatus;
                startButton.disabled = btnStatus;
            }
        }
    }
    let body = "{" +
        " \"action\" : \"" + action + "\"" +
        "}";
    xhr.send(body);
}

function startInput(isStarted) {
    let startButton = document.getElementById("start");
    let stopButton = document.getElementById("stop");
    stopButton.disabled = !isStarted;
    startButton.disabled = isStarted;

    let action;
    if (isStarted) {
        action = "start"
    } else if (isStarted === false) {
        action = "stop";
    }

    startService(action, "service");
}