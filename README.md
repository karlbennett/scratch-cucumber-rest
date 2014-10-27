scratch-cucumber-rest
=====================

An simple example of using Cucumber to test a RESTful endpoint. It has been written to test a CRUD enpoint for a user entity with the following scheme.

```json
{
    "email": "some.one@there.com",
    "firstName": "Some",
    "lastName": "One",
    "address": {
      "number": 1,
      "street": "That Raod",
      "suburb": "There",
      "city": "Metropolis",
      "postcode": "ABC123"
    }
}
```

#### Build

To build the source and run the unit test use the following command.

    mvn clean verify

#### Usage

To run the project against it's default REST endpoint use the following command.

    mvn clean verify -P cucumber

To change the URL of the endpoint edit the `src/test/resources/cucumber.properties` file and set the value of the `rest.baseUrl` property.
