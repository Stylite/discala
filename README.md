<h1 style="display: flex; align-items: center;"><img src="discala.png" width="32px" height="32px">Discala</h1>

A Discord API wrapper for Scala.

Discala was created mainly just as a fun project, and in order to replace [discord-akka](https://github.com/eaceaser/discord-akka), which hasn't been updated in 2 years. As well as this, I wanted to move away from huge ecosystem libraries like Play or Akka, so Discala relies mainly on small, one-shot, open-source libraries.

## Installation

Discala isn't ready for production use yet. Installation instructions will be added when it's ready for release, or you can feel free to download the source and manually install it yourself if you're willing to deal with all sorts of bugs, weirdness, and general lack of usability.

## Dependencies

- Scala 2.12
- Discord API/Gateway v6
- [scalaj-http](https://github.com/scalaj/scalaj-http) 2.3.0
- [nv-websocket-client](https://github.com/TakahikoKawasaki/nv-websocket-client) 2.3
- [circe](https://circe.github.io/circe/) 0.8.0
- [Macro Paradise](https://github.com/scalamacros/paradise) 2.1.0

**SBT Plugins**

- [sbt-revolver](https://github.com/spray/sbt-revolver) 0.9.0
