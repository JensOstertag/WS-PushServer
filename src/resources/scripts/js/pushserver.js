class PushServer {
    WS_CONNECTION = null;
    CONNECTION_STATUS = false;
    MESSAGE_HANDLERS = {};

    constructor(websocketUrl) {
        this.initConnection(websocketUrl);
    }

    subscribe(channelName, callback) {
        if(!this.CONNECTION_STATUS) {
            throw new Error('The connection to the server is not established yet or has been closed.');
        }

        this.MESSAGE_HANDLERS[channelName] = callback;

        this.WS_CONNECTION.send(JSON.stringify({
            messageType: "CLIENT_ACTION",
            action: "SUBSCRIBE",
            "data": {
                "channel": channelName
            }
        }));
    }

    unsubscribe(channelName) {
        if(!this.CONNECTION_STATUS) {
            throw new Error('The connection to the server is not established yet or has been closed.');
        }

        delete this.MESSAGE_HANDLERS[channelName];

        this.WS_CONNECTION.send(JSON.stringify({
            messageType: "CLIENT_ACTION",
            action: "UNSUBSCRIBE",
            "data": {
                "channel": channelName
            }
        }));
    }

    disconnect() {
        if(!this.CONNECTION_STATUS) {
            throw new Error('The connection to the server is not established yet or has been closed.');
        }

        this.WS_CONNECTION.close();
    }

    initConnection(websocketUrl) {
        this.WS_CONNECTION = new WebSocket(websocketUrl);

        this.WS_CONNECTION.onmessage = (event) => {
            const json = JSON.parse(event.data);

            if(!json || json.messageType !== "CLIENT_ACK" || json.code !== 200 || json.message !== "Connected") {
                throw new Error("Couldn't connect to the server.");
            }

            this.CONNECTION_STATUS = true;
            this.WS_CONNECTION.onmessage = this.handleMessage;
        }

        this.WS_CONNECTION.onclose = () => {
            this.CONNECTION_STATUS = false;
        }
    }

    handleMessage(event) {
        const json = JSON.parse(event.data);

        if(!json || json.messageType !== "CLIENT_PUSH" || json.code !== 200 || json.message !== "Push Message" || !json.data.pushMessage) {
            return;
        }

        const channelName = json.data.pushMessage.channel;
        const pushMessage = json.data.pushMessage.message;

        if(!this.MESSAGE_HANDLERS[channelName]) {
            return;
        }

        this.MESSAGE_HANDLERS[channelName](pushMessage);
    }
}
