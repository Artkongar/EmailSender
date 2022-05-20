function getRandomInt(max) {
    return Math.floor(Math.random() * max);
}

function callToSendMail() {
    let xhr = new XMLHttpRequest();
    let hostname = location.hostname;
    xhr.open('POST', 'http://' + hostname + ':8088/send_message', true);
    xhr.setRequestHeader('Access-Control-Allow-Origin', '*');
    xhr.setRequestHeader('Access-Control-Allow-Methods', 'POST');
    xhr.setRequestHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
    xhr.setRequestHeader('Content-Type', 'application/json');

    let randomJokeType = getRandomInt(17);
    let messageStr = "{" +
        " \"jokeType\" : \"" + randomJokeType + "\"" +
        "}";
    xhr.send(messageStr);
}

setInterval(callToSendMail, 14400000);


