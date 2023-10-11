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

<h2 id="setup">Setup</h2>

<h2 id="usage">Usage</h2>

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
This project is licensed under the [MIT License](./LICENSE).
