Feature: Sample Screen functionality

  @Regression
  Scenario Outline: Verify page contains grid
    Given I open '<url>' url
    When I login as '<user>' username '<pass>' password
    Then I verify page contains grid

    Examples:
      | url                           | user             | pass            |
      | https://www.saucedemo.com/v1/ | standard_user    | secret_sauce    |

  @Regression
  Scenario: Verify page contains grid
    Given I open '<url>' url
    When I login as '<user>' username '<pass>' password
    Then I verify page contains grid
    And I verify grid is not empty
    And I list all grid items
    And I expect number of items with price '15.99' to be equal '2'
