package jensostertag.pushserver.protocol;

public enum MessageType {
    // General
    ERROR,

    // Messages from clients
    CLIENT_SUBSCRIBE_CHANNEL,
    CLIENT_UNSUBSCRIBE_CHANNEL,

    // Messages to clients
    CLIENT_ACK,
    CLIENT_PUSH,

    // Messages from servers
    SERVER_CHANNEL_CREATE,
    SERVER_CHANNEL_DELETE,
    SERVER_CHANNEL_PING,
    SERVER_PUSH_MESSAGE,

    // Messages to servers
    SERVER_ACK,
    SERVER_SYSADMIN
}
