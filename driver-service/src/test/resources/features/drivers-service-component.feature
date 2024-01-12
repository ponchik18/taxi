Feature: Driver Service compoment-test

  Scenario: Retrieve all drivers
    Given The driver service business logic is available
    When The business logic is executed to retrieve all drivers
    Then The response should contain a list of driver

  Scenario: Create a new driver
    Given The driver service business logic is available
    When A new driver is created with first name "Test", last name "Test", email "test_cucumber@test.com", and phone "+375444444444", and licenseNumber "lic_45687"
    Then Method save should be invoked

  Scenario: Retrieve a specific driver by ID
    Given The driver service business logic is available
    When The business logic is executed to retrieve a driver by ID 1
    Then The response should contain the details of the specified driver

  Scenario: Delete a driver by ID
    Given The driver service business logic is available
    When The business logic is executed to delete a driver by ID
    Then The driver should be deleted from the database