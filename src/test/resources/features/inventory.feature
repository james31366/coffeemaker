Feature: Inventory

  Scenario Outline: Add positive ingredients to inventory.
    Given I have a coffee maker with a default recipe book.
    When I add a <ingredients> <amount> units.
    Then The <ingredients> have <addUnit> units.
    Examples:
      | ingredients | amount | addUnit |
      | "Coffee"    | "1"    | 16      |
      | "Milk"      | "1"    | 16      |
      | "Sugar"     | "1"    | 16      |
      | "Chocolate" | "1"    | 16      |

  Scenario Outline: Add negative ingredients to inventory.
    Given I have a coffee maker with a default recipe book.
    When I add a <ingredients> <amount> units.
    Then The <ingredients> have <addUnit> units.
    Examples:
      | ingredients | amount | addUnit |
      | "Coffee"    | "-1"   | 15      |
      | "Milk"      | "-1"   | 15      |
      | "Sugar"     | "-1"   | 15      |
      | "Chocolate" | "-1"   | 15      |

  Scenario Outline: Add ingredients value as string to inventory.
    Given I have a coffee maker with a default recipe book.
    When I add a <ingredients> <amount> units.
    Then The <ingredients> have <addUnit> units.
    Examples:
      | ingredients | amount | addUnit |
      | "Coffee"    | "asdf" | 15      |
      | "Milk"      | "asdf" | 15      |
      | "Sugar"     | "asdf" | 15      |
      | "Chocolate" | "asdf" | 15      |

  Scenario: Check inventory.
    Given I have a coffee maker with a default recipe book.
    When I add a "Coffee" "1" units.
    Then I check the inventory.