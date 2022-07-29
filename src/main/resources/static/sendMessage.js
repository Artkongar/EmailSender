function callToSendMail() {
    let xhr = new XMLHttpRequest();
    let hostname = location.hostname;
    xhr.open('POST', window.location.protocol + '//' + hostname + ':' + window.location.port + '/send_message', true);
    xhr.setRequestHeader('Access-Control-Allow-Origin', '*');
    xhr.setRequestHeader('Access-Control-Allow-Methods', 'POST');
    xhr.setRequestHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.onload = function () {
        if (xhr.status === 200) {
            let status = xhr.responseText;
            if (status == 'true') {
                console.log("Successful");
            }
        }
    }
    xhr.send();
}




