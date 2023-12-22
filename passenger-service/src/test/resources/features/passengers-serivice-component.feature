Feature: Passenger Service compoment-test

  Scenario: Retrieve all passengers
    Given The passenger service business logic is available
    When The business logic is executed to retrieve all passengers
    Then The response should contain a list of passengers

  Scenario: Create a new passenger
    Given The passenger service business logic is available
    When A new passenger is created with first name "Test", last name "Test", email "test_cucumber@test.com", and phone "+375444444444"
    Then Method save should be invoked

  Scenario: Retrieve a specific passenger by ID
    Given The passenger service business logic is available
    When The business logic is executed to retrieve a passenger by ID 1
    Then The response should contain the details of the specified passenger

  Scenario: Delete a passenger by ID
    Given The passenger service business logic is available
    When The business logic is executed to delete a passenger by ID
    Then The passenger should be deleted from the database