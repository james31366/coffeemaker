Feature: Recipe

  Scenario: Cannot add recipe more than three.
    Given I create a recipe book.
    When I add a "Latte" recipe.
    And I add a "Coffee" recipe.
    And I add a "Black Coffee" recipe.
    Then The recipe book have all recipe.

  Scenario: Add the same recipe twice.
    Given I create a recipe book.
    When I add a "Latte" recipe.
    Then I cannot add a "Latte" recipe.

  Scenario: Add the blank name recipe.
    Given I create a recipe book.
    When I add a "" recipe.
    Then I cannot add a "" recipe.

  Scenario: Delete recipe.
    Given I create a recipe book.
    When I add a "Latte" recipe.
    Then I delete the "Latte" recipe.

  Scenario: Delete null recipe.
    Given I create a recipe book.
    Then I delete the Null recipe.

  Scenario: Delete negative index recipe book.
    Given I create a recipe book.
    Then I delete the recipe at -1 index.

  Scenario: Delete recipe not in list array recipe.
    Given I create a recipe book.
    Then I delete the recipe at 11 index.

  Scenario: Edit recipe.
    Given I create a recipe book.
    When I add a "Latte" recipe.
    Then I edit the "Latte" recipe to "5" "3" "1" "1" "75".

  Scenario: Edit negative index recipe book.
    Given I create a recipe book.
    Then I edit the "Latte" recipe to "5" "3" "1" "1" "75" at -1 index.

  Scenario: Edit recipe nor in list array recipe.
    Given I create a recipe book.
    Then I edit the "Latte" recipe to "5" "3" "1" "1" "75" at 11 index.