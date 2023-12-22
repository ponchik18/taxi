Feature: Passenger-service operation for end-to-end-test
  Scenario: Create a new passenger
    Given The passenger-service is running
    Given The passenger data with first name "Test", last name "Test", email "test_cucumber@test.com", and phone "+375444444444"
    When The client adds a new passenger
    Then The response should contain the new passenger details

  Scenario: Delete a passenger
    Given The passenger-service is running
    Given The passenger ID is 1
    When The client deletes the passenger by ID
    Then The response should indicate a successful deletion

  Scenario: Update passenger details
    Given The passenger-service is running
    Given The passenger ID is 1
    Given The passenger data with first name "Test", last name "Test", email "test_cucumber@test.com", and phone "+375444444444"
    When The client updates the passenger data first name "New", last name "New", email "test_cucumber_new@test.com", and phone "+375789456123"
    Then The response should contain the updated passenger details

  Scenario: Get unexciting passenger
    Given The passenger-service is running
    When The client requests the passenger by ID
    Then The response should return 404