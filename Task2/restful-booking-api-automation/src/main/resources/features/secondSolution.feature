Feature: Second solution of Task 2 with end to end CRUD operation in one scenario

  Scenario: End to end booking flow for our Billie Mars astronaut based on Task 2 requirements
    Given our astronaut is an authorised user and has following request details available to proceed
    When we perform "create" booking request to the restful-booker App with following data
      | firstname | lastname | totalprice | depositpaid | additionalneeds | checkin    | checkout   |
      | Solomon   | Northup  | 1200       | false       | Hot Chocolate   | 2021-02-18 | 2021-02-28 |
      | Billie    | Martian  | 1200       | true        | Samosa with tea | 2021-02-18 | 2021-02-28 |
    Then response should have HTTP status code 200
    And response should have "bookingid" and it should not be null
    And data given in response body should be equal to the data in "create" request
    When we perform "update" booking request to the restful-booker App with following data
      | firstname | lastname | totalprice | depositpaid | additionalneeds             | checkin    | checkout   |
      | Solomon   | Northup  | 1200       | true        | Need rover to carry luggage | 2021-02-18 | 2021-03-01 |
    Then response should have HTTP status code 200
    And data given in response body should be equal to the data in "update" request
    When we "GET" the created booking by booking Id
    Then response should have HTTP status code 200
    And data given in response body should be equal to the data in "update" request
    When we "DELETE" the created booking by booking Id
    And response should have HTTP status code 201
    When we "GET" the created booking by booking Id
    And  response should have HTTP status code 404

