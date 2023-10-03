package jensostertag.pushserver.protocol;

public enum MessageType {
    // General
    ERROR,

    // Messages from clients
    CLIENT_SUBSCRIBE,
    CLIENT_UNSUBSCRIBE,

    // Messages to clients
    CLIENT_ACK,
    CLIENT_PUSH,

    // Messages from servers
    SERVER_BROADCAST,
    SERVER_SEND_TO
}
