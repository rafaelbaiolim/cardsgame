## Homework Deck of Cards Game

- REST API developed according to the challenge requirements specified in the GoTo company's selection process.

## Technologies

1. Java
2. Maven
3. Spring Boot
4. Swagger SpringDoc

## Project Execution

### Requirements

1. Java 22 or higher
2. JAVA_HOME system variable settled

### **Installation**

- Clone repository

```
$ git clone https://github.com/rafaelbaiolim/cardsgame.git 
```

- Go to project directory

```
$ cd cardsgame
```

- Install Maven dependencies

```
$ mvnw install
```

### **Automated Test Execution**

```
$ mvnw test
```

### **Usage**

The default application configuration parameters have already been placed in
`src/main/resources/`. The default port is set to 8080 and can be accessed via
http://localhost:8080. All API endpoints are prefixed with `/api/v1`.

#### Spring Initialization:

```
mvnw spring-boot:run
```

#### Swagger:

For swagger interactive API access:

```
http://localhost:8080/swagger-ui 
```

#### Postman Collection

> To test a full game scenario step by step, there is a Postman collection located at
> the [src/test/postman](https://github.com/rafaelbaiolim/cardsgame/blob/main/src/test/postman/CardGame.postman_collection.json)
> path. All the steps for the `PlayingGame` methods, are organized in sequence and have been dynamically configured. In
> each step, the response body is saved to a Postman collection variable, making it available for subsequent requests.

> Please note that this scenario collection assumes the presence of only one Game, Deck, and Player in the steps.
> However, it is still possible to duplicate calls to these steps to conduct further testing as desired.

## API Mapping

| Action                        | Method | Body Parameter | URL                                                         |
|-------------------------------|--------|----------------|-------------------------------------------------------------|
| Create new game               | POST   | name           | http://localhost:8080/api/games                             |
| Delete a game                 | DELETE |                | http://localhost:8080/api/games/{uuid}                      |
| List All games                | GET    |                | http://localhost:8080/api/games                             |
| Create a Deck                 | POST   |                | http://localhost:8080/api/decks                             |
| Delete a Deck                 | DELETE |                | http://localhost:8080/api/decks/{uuid}                      |
| List All Decks                | GET    |                | http://localhost:8080/api/decks                             |
| Create a Player               | POST   | name           | http://localhost:8080/api/players                           |
| Delete a Player               | DELETE |                | http://localhost:8080/api/players/{uuid}                    |
| List All Players              | GET    |                | http://localhost:8080/api/players                           |
| Add Deck to a Game            | PUT    | deckUuid       | http://localhost:8080/api/games/{gameUuid}/decks            |
| Add Player to a Game          | PUT    | playerUuid     | http://localhost:8080/api/games/{gameUuid}/players          |
| Shuffle Cards of Game         | PATCH  |                | http://localhost:8080/api/games/{gameUuid}/shuffleDeckCards |
| Deal Cards to Players         | PATCH  |                | http://localhost:8080/api/games/{gameUuid}/dealCards        |
| Get Cards of a Player in Game | GET    |                | http://localhost:8080/api/v1/players/{playerUuid}/cards     |
| Get Players of a Game         | GET    |                | http://localhost:8080/api/v1/games/{gameUuid}/players       |
| Get Undealt Cards of Game     | GET    |                | http://localhost:8080/api/v1/games/{gameUuid}/cards/undealt |
| List Game Events              | GET    |                | http://localhost:8080/api/v1/events                         |

### Project observations and Limitations

- The `dealCard` method was implemented to deal cards in order to each player that was added to the game. So, in each
  iteration, if the game has two or more players, for example, the first card will be given to player1, then the second
  card to player2, and so on until the cards in the game deck end.

- The `shuffle` method needs to be called at least one time for game iterations to be available.

- After shuffling, all available decks in the game will be merged and will be in random order before each deal
  iteration.

- There is no treatment for renaming or checking for game, player, and decks duplication. If the `bodyParameter` has a
  duplicated name for the entities, it will create a new object with the same name, but with a different UUID for each
  object.

- To treat concurrency access, all operations in the datasource are using a thread-safe lock with synchronized control.
  A more robust treatment for concurrency is possible, but for simplification and to deploy all requirements, that was
  the option used.

- No authentication was implemented; moreover, it was not a requirement of the challenge. It's a valid observation,
  thinking about real-world usage of the API.

- There is no container or automated deployment configuration for the project, and that is a point that also could be
  implemented out of the scope of the challenge.

- This project could also have more automated unit and integration tests, but to implement all requirements of the
  challenge, it was opted to keep essential tests for working and testing the API.

- The event listener for displaying chronological actions currently features a basic implementation. A more 
sophisticated approach, such as utilizing an observer pattern or a well-structured strategy, could enhance
its functionality. Furthermore, while there are numerous possibilities for enriching this historical event log
with detailed information, the current implementation is intentionally simplified for ease of understanding and
maintenance.




