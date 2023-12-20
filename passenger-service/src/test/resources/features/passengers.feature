Feature: Passenger-service operation
  Scenario: Create a new passenger
    Given The passenger-service is running
    Given The passenger data with first name "Test", last name "Test", email "test_cucumber@test.com", and phone "+375444444444"
    When The client adds a new passenger
    Then The response should contain the new passenger details

  Scenario: Delete a passenger
    Given the passenger ID is 1
    When the client deletes the passenger by ID
    Then the response should indicate a successful deletion