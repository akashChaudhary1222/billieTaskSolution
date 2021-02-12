Feature: First Solution of Task 2 using multiple test scenarios

  Background: We have valid Auth token
    Given Valid Auth token is created successfully

  Scenario Outline: We need to validate if data in Get api response is equal to the last updated or created booking request
    Given we have '<checkin>' and '<checkout>' dates of astronaut '<firstname>' and '<lastname>'
    And our astronaut has paid '<depositpaid>' and has additional choice for '<additionalneeds>' and agreed to pay '<totalprice>'
    When we '<action>' bookings for our astronauts
    Then we should get HTTP response code 200
    And response should have "bookingid" non null
    And we shall get data in response equal to the request we made for '<action>'
    When we make a call to GetBookings API with bookingId
    Then data given in response body should be equal to the last actual data from '<action>' event

    Examples:
      | firstname | lastname | totalprice | depositpaid | additionalneeds | checkin    | checkout   | action |
      | Roger     | Pillai   | 500        | false       | Hot Chocolate   | 2021-02-18 | 2021-02-28 | create |
      | Roger     | Pillai   | 500        | true        | Hot Chocolate   | 2021-02-18 | 2021-02-28 | update |

  Scenario Outline: Get and delete the bookings for our astronauts
    Given we have '<firstname>' and '<lastname>' and bookingIds of our astronauts
    When we delete these bookings by their ID
    Then we should get HTTP response code 201
    And response should be equal to "Created"
    When we make a call to GetBookings API with bookingId
    Then we should get HTTP response code 404

    Examples:
      | firstname | lastname |
      | Roger     | Pillai   |