Feature: makeCoffee
  Scenario: Person want to buy a coffee.
    Given I have money 25$ and want to buy a Latte.
    When I go to cashier and order a Latte.
    Then I should get a Latte.