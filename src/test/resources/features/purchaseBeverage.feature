Feature: PurchaseBeverage

  Scenario: Person want to buy a coffee.
    Given I have a coffee maker with a default recipe book.
    When I want to buy a "Latte" with 125.
    Then I got the withdrawal 25.

  Scenario: Person want to buy a coffee and check Inventory.
    Given I have a coffee maker with a default recipe book.
    When I want to buy a "Latte" with 125.
    Then I got the withdrawal 25 with check Inventory.

  Scenario: Person want to buy a coffee but empty recipe book.
    Given I have a coffee maker with empty recipe book.
    When I want to buy a "Lat" with 125.
    Then I got the withdrawal 125.

  Scenario: Person want to buy a coffee but not have enough ingredients.
    Given I have a coffee maker with a default recipe book.
    When I want to buy a "Black Coffee" with 125.
    Then I got the withdrawal 125.

  Scenario: Person want to buy a coffee but not have enough ingredients.
    Given I have a coffee maker with a default recipe book.
    When I want to buy a "Latte" with 25.
    Then I got the withdrawal 25.