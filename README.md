<div align="center">

![Header](img/header.jpg)

# WebSocket Push-Server

![Version](https://img.shields.io/badge/version-v1.0.0-yellow?style=for-the-badge)
![BuiltWith](https://img.shields.io/badge/built%20with-Java-orange?style=for-the-badge)
![Build](https://img.shields.io/github/actions/workflow/status/JensOstertag/WS-PushServer/deploy-image.yml?style=for-the-badge)
![License](https://img.shields.io/badge/license-MIT-blue?style=for-the-badge)



### WebSocket Push-Server with channeling capabilities

It allows HTTP clients to create channels and send push messages to WebSocket clients that are subscribed to the same channel.

[Introduction](#introduction) • [Setup](#setup) • [Usage](#usage) • [Dependencies](#dependencies) • [License](#license)

</div>

<hr>

<h2 id="introduction">Introduction</h2>

### Push architecture
A push architecture is used in applications in which the server has to send messages / data to the client without the client having to request it.
This is particularly useful for applications that require real-time data, such as chat applications or stock market applications.

However, it is difficult to achieve this with HTTP for web applications as it is primarily a request-response protocol.

### WebSockets
WebSocket connections are a solution to this problem.
They allow a persistent connection between client and server, which can be used to send messages in both directions.

### Push-Server
This project implements a Push-Server that uses WebSockets to create a push architecture that can be used for an unlimited amount of projects.

HTTP clients - let's call them publishers - can create channels where they can later send messages in.
One channel should only be used for one project, so that the messages are only sent to the clients of this project.

WebSocket clients - also called subscribers - can subscribe (or unsubscribe) to (from) an unlimited amount of channels once they've established a WebSocket connection.
They'll receive all messages that are sent to the channels they're subscribed to - and only those.
Publishers can also address a message to a specific list of subscribers.
Only those subscribers will receive the message - and only if they've subscribed to the channel.

For further information on how to use the Push-Server, see [Usage](#usage).

<h2 id="setup">Setup</h2>

This project is dockerized so you can easily run it in a container.
It's recommended to use the pre-built image from the [GitHub container registry](https://ghcr.io/jensostertag/pushserver) - however you are also free to build the image yourself.
Please follow the instructions below to get started.

1. Clone the repository on the target environment
   ```sh
   git clone https://github.com/JensOstertag/WS-PushServer.git pushserver
   ```
2. Configure the `docker-compose.yml` file
   - Change the ports if you want to use different ones from `5222` and `5223`
   - Àdd a custom network mode (depending on your existing setup)
3. Start the container with docker compose
   ```sh
   docker compose up -d
   ```

<h2 id="usage">Usage</h2>

For the following examples, the server should be reachable on `localhost`, the WebSocket service should be available on port `5222` and the HTTP service should be available on port `5223`.
You can change these ports when starting the docker container, in which case you have to adjust the parameters that are used in the examples below.

### Publishers
Publishers can create channels and send messages to the subscribers of these channels.

<details>
<summary><strong>Create channels</strong></summary>

You can create a channel by sending a `POST` request to the `/channel/create` route:
```http
POST /channel/create HTTP/1.1
Host: localhost:5223

{
  "messageType": "SERVER_ACTION",
  "action": "CREATE_CHANNEL",
  "data": {
    "channel": "channelName"
  }
}
```

| Field          | Description                                     |
|----------------|-------------------------------------------------|
| `data.channel` | The name of the channel that you want to create |

<details>
<summary>Response - Success</summary>

You'll receive a JSON response when the channel was created successfully:
```json
{
  "messageType": "SERVER_ACK",
  "code": 200,
  "message": "Created",
  "data": {
    "channel": "channelName",
    "channelToken": "CHANNEL_TOKEN"
  }
}
```

| Field               | Description                                                                 |
|---------------------|-----------------------------------------------------------------------------|
| `data.channel`      | The name of the channel that was created                                    |
| `data.channelToken` | The token that has to be used to send messages to the channel's subscribers |

The channel token needs to be saved in a secure place.
You'll need it every time you want to perform an action on the channel, such as sending a message to its subscribers.
</details>

<details>
<summary>Response - Error</summary>

If there was an error whilst creating the channel, you'll receive the following response:
```json
{
  "messageType": "ERROR",
  "code": 409,
  "message": "Message",
  "data": {
    "errorDetails": "Details"
  }
}
```

| Field               | Description                           |
|---------------------|---------------------------------------|
| `message`           | Short description of what happened    |
| `data.errorDetails` | Details about the error that occurred |
</details>
</details>

<details>
<summary><strong>Ping channels</strong></summary>

Channels can be pinged to check whether they exist or to get information about it.

```http
POST /channel/ping HTTP/1.1
Host: localhost:5223

{
  "messageType": "SERVER_ACTION",
  "action": "PING_CHANNEL",
  "data": {
    "channel": "channelName",
    "channelToken": "CHANNEL_TOKEN"
  }
}
```

| Field               | Description                                                                     |
|---------------------|---------------------------------------------------------------------------------|
| `data.channel`      | The name of the channel that you want to ping                                   |
| `data.channelToken` | The channel token or `null` if you only want to know whether the channel exists |

<details>
<summary>Response - Success</summary>

If the channel exists and the specified channel token is correct, you'll receive a response which looks like this:
```json
{
  "messageType": "SERVER_ACK",
  "code": 200,
  "message": "OK",
  "data": {
    "channel": "channelName",
    "channelToken": null
  }
}
```

| Field               | Description                                                      |
|---------------------|------------------------------------------------------------------|
| `data.channel`      | The name of the channel that was pinged                          |
| `data.channelToken` | Always `null` (it's only present to comply with the JSON schema) |
</details>

<details>
<summary>Response - Error</summary>

If there was an error whilst pinging the channel, the response will look like this:
```json
{
  "messageType": "ERROR",
  "code": 0,
  "message": "Message",
  "data": {
    "errorDetails": "Details"
  }
}
```

| Field               | Description                           |
|---------------------|---------------------------------------|
| `message`           | Short description of what happened    |
| `data.errorDetails` | Details about the error that occurred |
</details>
</details>

<details>
<summary><strong>Send messages</strong></summary>

Messages can be sent to all subscribers of a channel or to a list of specific subscribers.
    
```http
POST /push HTTP/1.1
Host: localhost:5223

{
  "messageType": "SERVER_ACTION",
  "action": "PUSH_MESSAGE",
  "data": {
    "channel": "channelName",
    "channelToken": "channelToken",
    "recipients": [],
    "message": "message"
  }
}
```

| Field               | Description                                                                                                                                          |
|---------------------|------------------------------------------------------------------------------------------------------------------------------------------------------|
| `data.channel`      | The name of the channel that you want to send the message in                                                                                         |
| `data.channelToken` | The channel token that you received when creating the channel                                                                                        |
| `data.recipients`   | A list of UUIDs of recipients that should receive the message <br> If this list is empty, the message will be sent to all subscribers of the channel |
| `data.message`      | The message that should be sent                                                                                                                      |

The recipients can be specified in order to create a private messaging system.
As this requires specification of the client's UUID, clients have to send an API call (or something comparable) to the backend of your application with their UUID upon subscribing to a channel.

<details>
<summary>Response - Success</summary>

In case of a successful message push, you'll receive the following response:
```json
{
  "messageType": "SERVER_ACK",
  "code": 200,
  "message": "Sent",
  "data": {
    "channel": "channelName",
    "channelToken": null
  }
}
```

| Field               | Description                                                      |
|---------------------|------------------------------------------------------------------|
| `data.channel`      | The name of the channel in which the message was sent            |
| `data.channelToken` | Always `null` (it's only present to comply with the JSON schema) |
</details>

<details>
<summary>Response - Error</summary>

If there was an error whilst sending the message, the response will look like this:
```json
{
  "messageType": "ERROR",
  "code": 0,
  "message": "Message",
  "data": {
    "errorDetails": "Details"
  }
}
```

| Field               | Description                           |
|---------------------|---------------------------------------|
| `message`           | Short description of what happened    |
| `data.errorDetails` | Details about the error that occurred |
</details>
</details>

<details>
<summary><strong>Delete channels</strong></summary>

It's a good practice to delete channels that are no longer in use.
This can be done by sending a `POST` request to the `/channel/delete` route:
```http
POST /channel/delete HTTP/1.1

{
  "messageType": "SERVER_ACTION",
  "action": "DELETE_CHANNEL",
  "data": {
    "channel": "channelName",
    "channelToken": "channelToken"
  }
}
```

| Field               | Description                                                   |
|---------------------|---------------------------------------------------------------|
| `data.channel`      | The name of the channel that you want to delete               |
| `data.channelToken` | The channel token that you received when creating the channel |

<details>
<summary>Response - Success</summary>

If the channel was deleted successfully, you'll receive the following response:
```json
{
  "messageType": "SERVER_ACK",
  "code": 200,
  "message": "Deleted",
  "data": {
    "channel": "channelName",
    "channelToken": null
  }
}
```

| Field               | Description                                                      |
|---------------------|------------------------------------------------------------------|
| `data.channel`      | The name of the channel that was deleted                         |
| `data.channelToken` | Always `null` (it's only present to comply with the JSON schema) |
</details>

<details>
<summary>Response - Error</summary>

If there was an error whilst deleting the channel, the response will look like this:
```json
{
  "messageType": "ERROR",
  "code": 0,
  "message": "Message",
  "data": {
    "errorDetails": "Details"
  }
}
```

| Field               | Description                           |
|---------------------|---------------------------------------|
| `message`           | Short description of what happened    |
| `data.errorDetails` | Details about the error that occurred |
</details>
</details>

### Subscribers
Subscribers can subscribe to channels and receive messages that are sent to these channels.

<details>
<summary><strong>Connection</strong></summary>

To connect to the Push-Server, a client has to establish a WebSocket connection to `ws://localhost:5222`.

The client will automatically receive a message from the server:
```json
{
  "messageType": "CLIENT_ACK",
  "code": 200,
  "message": "Connected",
  "data": {
    "uuid": "clientUuid",
    "subscribedChannels": []
  }
}
```

| Field                     | Description                                                                                                                 |
|---------------------------|-----------------------------------------------------------------------------------------------------------------------------|
| `data.uuid`               | The UUID of the client that was generated by the server <br> It is used by publishers to send a message to specific clients |
| `data.subscribedChannels` | A list of channels that the client is subscribed to <br> Initially empty                                                    |
</details>

<details>
<summary><strong>Subscribing to channels</strong></summary>

Before a client can receive messages, he has to subscribe to a channel.
This can be done by sending a message to the server:
```json
{
  "messageType": "CLIENT_ACTION",
  "uuid": "clientUuid",
  "action": "SUBSCRIBE",
  "data": {
    "channel": "channelName"
  }
}
```

| Field          | Description                                             |
|----------------|---------------------------------------------------------|
| `uuid`         | The UUID of the client that was generated by the server |
| `data.channel` | The name of the channel that should be subscribed       |

<details>
<summary>Response - Success</summary>

If the channel was subscribed successfully, the client will receive the following response:
```json
{
  "messageType": "CLIENT_ACK",
  "code": 200,
  "message": "Connected",
  "data": {
    "uuid": "clientUuid",
    "subscribedChannels": [
      "channelName"
    ]
  }
}
```

| Field                     | Description                                                                                                     |
|---------------------------|-----------------------------------------------------------------------------------------------------------------|
| `data.uuid`               | The UUID of the client that was generated by the server                                                         |
| `data.subscribedChannels` | A list of channels that the client is subscribed to <br> The channel that was subscribed to is now in this list |
</details>
</details>

<details>
<summary><strong>Unsubscribing from channels</strong></summary>

If a client wishes to no longer receive messages from a specific channel, he can unsubscribe from that channel by sending a message to the server:
```json
{
  "messageType": "CLIENT_ACTION",
  "uuid": "clientUuid",
  "action": "UNSUBSCRIBE",
  "data": {
    "channel": "channelName"
  }
}
```

| Field          | Description                                             |
|----------------|---------------------------------------------------------|
| `uuid`         | The UUID of the client that was generated by the server |
| `data.channel` | The name of the channel that should be unsubscribed     |

<details>
<summary>Response - Success</summary>

If the client unsubscribed from the channel successfully, the client will receive the following response:
```json
{
  "messageType": "CLIENT_ACK",
  "code": 200,
  "message": "Connected",
  "data": {
    "uuid": "clientUuid",
    "subscribedChannels": []
  }
}
```

| Field                     | Description                                                                                                          |
|---------------------------|----------------------------------------------------------------------------------------------------------------------|
| `data.uuid`               | The UUID of the client that was generated by the server                                                              |
| `data.subscribedChannels` | A list of channels that the client is subscribed to <br> The channel that was unsubscribed is no longer in this list |
</details>
</details>

<details>
<summary><strong>Receiving messages</strong></summary>

Messages are sent to the client in JSON format:
```json
{
  "messageType": "CLIENT_PUSH",
  "code": 200,
  "message": "Push Message",
  "data": {
    "uuid": "clientUuid",
    "subscribedChannels": [
      "channelName"
    ],
    "pushMessage": {
      "channel": "channelName",
      "message": "Message"
    }
  }
}
```

| Field                      | Description                                           |
|----------------------------|-------------------------------------------------------|
| `data.uuid`                | The client's UUID                                     |
| `data.subscribedChannels`  | The list of channels that the client is subscribed to |
| `data.pushMessage.channel` | The name of the channel that the message was sent on  |
| `data.pushMessage.message` | The raw message that was sent on the channel          |
</details>

<h2 id="dependencies">Dependencies</h2>
This project uses the following dependencies:

- Java
  - **Java-WebSocket** v1.5.4 - GitHub: [TooTallNate/Java-WebSocket](https://github.com/TooTallNate/Java-WebSocket), licensed under [MIT License](https://github.com/TooTallNate/Java-WebSocket/blob/master/LICENSE)
  - **Gson** v2.10.1 - GitHub: [google/gson](https://github.com/google/gson), licensed under [Apache License 2.0](https://github.com/google/gson/blob/main/LICENSE)
  - **Json-Schema-Validator** v1.0.87 - GitHub: [networknt/json-schema-validator](https://github.com/networknt/json-schema-validator), licensed under [Apache License 2.0](https://github.com/networknt/json-schema-validator/blob/master/LICENSE)
  - **TestNG** - GitHub: [cbeust/testng](https://github.com/testng-team/testng), licensed under [Apache License 2.0](https://github.com/testng-team/testng/blob/master/LICENSE.txt)
  - **Maven** - GitHub: [apache/maven](https://github.com/apache/maven), licensed under [Apache License 2.0](https://github.com/apache/maven/blob/master/LICENSE)
- Docker
  - **maven** 3.8.7-openjdk-21-slim - DockerHub: [maven](https://hub.docker.com/_/maven), licensed under [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0) 
  - **openjdk** 21-jdk-slim - DockerHub: [openjdk](https://hub.docker.com/_/openjdk), licensed under [GPLv2](https://openjdk.org/legal/gplv2+ce.html)
- GitHub Actions
  - **actions/checkout** v3 - GitHub: [actions/checkout](https://github.com/actions/checkout), licensed under [MIT License](https://github.com/actions/checkout/blob/main/LICENSE)
  - **docker/login-action** v1 - GitHub: [docker/login-action](https://github.com/docker/login-action), licensed under [Apache License 2.0](https://github.com/docker/login-action/blob/master/LICENSE)

<h2 id="license">License</h2>

This project is licensed under the <a href="./LICENSE">MIT License</a>.
