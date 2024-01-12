Feature: Ride Flow
  Scenario: A passenger books a ride
    Given An existing passenger with id 1 creates a ride request with start point "Start point" and destination point "Destination point"
    When A passenger sends this request to the create ride endpoint
    Then A driver's status should be changed to "BUSY" with id 1
    And A passenger should get details of ride order with status "DRIVER_EN_ROUTE"

  Scenario: A driver arrived to start point
    Given The driver with id 1
    When A driver starts the ride
    Then A ride's status should be changed to "IN_PROGRESS"

  Scenario: Passenger apply promo code for ride
    When A passenger with id 1 apply promo-code with name "PROMO_CODE"
    Then A ride's status of promoCodeApply is true
    And Ride has lower price

  Scenario: A driver finishes the ride
    When A driver with id 1 finishes the ride
    Then A ride's status should be changed to "COMPLETED"
    And Finish date should be set
    And A driver's status should be changed to "AVAILABLE"