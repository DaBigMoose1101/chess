# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)]([https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA])

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

## Sequence Diagram
[view Diagram here.]([https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2GADEaMBUljAASij2SKoWckgQaIEA7gAWSGBiiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAETtlMEAtih9pX0wfQA0U7jqydAc45MzUyjDwEgIK1MAvpjCJTAFrOxclOX9g1AjYxNTs33zqotQyw9rfRtbO58HbE43FgpyOonKUCiMUyUAAFJForFKJEAI4+NRgACUh2KohOhVk8iUKnU5XsKDAAFUOrCbndsYTFMo1Kp8UYdKUAGJITgwamURkwHRhOnAUaYRnElknUG4lTlNA+BAIHEiFRsyXM0kgSFyFD8uE3RkM7RS9Rs4ylBQcDh8jqM1VUPGnTUk1SlHUoPUKHxgVKw4C+1LGiWmrWs06W622n1+h1g9W5U6Ai5lCJQpFQSKqJVYFPAmWFI6XGDXDp3SblVZPQN++oQADW6ErU32jsohfgyHM5QATE4nN0y0MxWMYFXHlNa6l6020C3Vgd0BxTF5fP4AtB2OSYAAZCDRJIBNIZLLdvJF4ol6p1JqtG5Dgbl0e7L4vN4fRftkGFfMl4e3C+nxPO+SyvgC5wFrKaooBCGYwvC8HIigaIYtiCaGC6YZumSFIGrSz6jCaRLhhaHLcryBqCsKMCiuKrrSkml4weU9RQOioYkW6ZHlLGqTmIQeowFU+ivEsnFMtxTFFCxMBsRxGGdn+ab7oe2a5pgymdsWaYARW46tl806zs2BlfjpnbZD2MD9oOvRPiOowLpOfTGY2pkTn0S6cKu3h+IEXgoOge4Hr4zDHukmSYFZF4ySU5QVNIACiu5JfUSXNC0BjqAkaDdG5c7fmyylXAV6AQUCHbQU68owAgoW+rCqlhai6KxOhcqYQS2EsuUMQAGrAMgHABkGJloMRkmMYUloUTGQbUWEZVoBJZoRtV4JyexYiKVhXG9TAkLDBANB8Yyo11u5E2raRkYcumx00DAZ3aDdUkbbV8k7Z1SmQZczW+upCB5n9ILVZcirKkV0kxWAfYDkOBxmD5nh+RukK2ru0IwAA4qOrIRae0Xnsw4NphUONpZl9ijvlY1XdDv6g6V9NzhVqZshhuFgHjoyqLCNN81jsStWhjrOt1+2kjAA1DUgI3LZNa08XNdqOYYNHLW9jEfbBW0KT9e1TdL5I8-jsJK7dM33TjFIwILajC1g4uJsmzO8470LZtgiQGJpoPaVeul9B7qjjBU-QOwAktIKwzH0J6ZAaFYTPHOgIKADbJ0BfTxw7AByOdeY0BwWTDJPw3Z-Sh+HkejjHcfTAnkX6oR9y5036eZ9nTmp03BdF3sJfIyuqProE2A+FA2DcPAuqZLjo4pC3xM5KTzHxZUtQNNTtPBKz6BDgPoyl0HxXM6Wy2zMfKDs1BG+bZ6eoe7CcDzygHui+1LtdTIPXS7LYaF0ZxXUtlJa2XIeS2h7hrJaB8VoMXNNJLm+tvowQ1P-d0MAABmUCX43zAdNdk5RVYeyFGEG+2skG6wVEqFUu1JbGywU-TI+DRyFyIlQiMECYDABtIvUYMBID21HE7LhgdZLR2kD-X6lU0xvy9Kw0cQMQZyIkZvOuowG4TAAIy9gAMwABYvLQ1OLDSuj4pErD0UYkxy5fLjwCJYFAyoIDJBgAAKQgDyARhgAhdxAA2Ve5hOZBwSlUSkd4WgOzppdOcQ4Z7AGcVAOAEB6pQGvvXaQp9qBVSZnIlmcT0CzESck1J6TMlaOyf7NRNCYAACtvFoBfl4nkn8UJtSxDIo2a1uYwOAeNQhSCIG22YDA8hvD4HiOQZ1codE0E1V-ogrBuDOBsKqUM7hxCYCkKXjRKR0y6mUIYX-KWWC-BaCUaMc6N9ZjzJKRnMpaToCYhgN0t2BTPFNPaTYX2YgtIzNyWmSG9Cz7lzXqUWyiMR4OP8gELwSSuxelgMAbAM9BIEESMvImsNQlAoSslVK6VMrGEZmcApPQOAuIpLfGpHMaEgG4HgYwr8mVZg6WLE5yzyiMuRSyzZPFDobBOoYKir1lmCqOiK56C1xWYMlcKp6DsxESsBYs0oIL3n5NTOURAyKVF0qgmTWhUMy5mIrlC3oSN7FAA])

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```

