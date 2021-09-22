package edu.ncsu.csc326.coffeemaker;

import edu.ncsu.csc326.coffeemaker.exceptions.InventoryException;
import edu.ncsu.csc326.coffeemaker.exceptions.RecipeException;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.Objects;

import static org.junit.Assert.*;

public class CoffeeMakerStep {

    private CoffeeMaker coffeeMaker;
    private RecipeBook recipeBook;
    private StringBuffer buffer;
    private int recipeIndex;
    private int amtPaid;
    private boolean error;

    /**
     * Help the tester create recipe easier.
     *
     * @param name         Name of the recipe
     * @param amtChocolate amount of chocolate in recipe
     * @param amtCoffee    amount of coffee in recipe
     * @param amtMilk      amount of milk in recipe
     * @param amtSugar     amount of sugar in recipe
     * @param price        price of recipe
     * @return The recipe that you need to create.
     * @throws RecipeException if there was an error parsing the ingredient
     *                         amount when setting up the recipe.
     */
    private static Recipe createRecipe(String name, String amtChocolate, String amtCoffee, String amtMilk, String amtSugar, String price) throws RecipeException {
        Recipe recipe = new Recipe();
        recipe.setName(name);
        recipe.setAmtChocolate(amtChocolate);
        recipe.setAmtCoffee(amtCoffee);
        recipe.setAmtMilk(amtMilk);
        recipe.setAmtSugar(amtSugar);
        recipe.setPrice(price);

        return recipe;
    }

    /**
     * Create the String Buffer easier for check the toString of Inventory
     *
     * @param amtCoffee    amount of coffee in recipe
     * @param amtMilk      amount of milk in recipe
     * @param amtSugar     amount of sugar in recipe
     * @param amtChocolate amount of chocolate in recipe
     * @return StringBuffer
     */
    private static StringBuffer createStringBuffer(int amtCoffee, int amtMilk, int amtSugar, int amtChocolate) {
        StringBuffer buf = new StringBuffer();
        buf.append("Coffee: ");
        buf.append(amtCoffee);
        buf.append("\n");
        buf.append("Milk: ");
        buf.append(amtMilk);
        buf.append("\n");
        buf.append("Sugar: ");
        buf.append(amtSugar);
        buf.append("\n");
        buf.append("Chocolate: ");
        buf.append(amtChocolate);
        buf.append("\n");

        return buf;
    }

    private int findRecipeIDByName(Recipe[] recipeArray, String name) throws RecipeException {
        int result = 0;
        for (int i = 0; i < recipeArray.length; i++) {
            if (name.equals(recipeArray[i].getName())) {
                result = i;
                break;
            } else if (i == recipeArray.length - 1) {
                throw new RecipeException("Cannot find the Recipe.");
            }
        }
        return result;
    }

    public void setUpCoffeeMaker() {
        buffer = createStringBuffer(15, 15, 15, 15);
        recipeBook = new RecipeBook();
        coffeeMaker = new CoffeeMaker(recipeBook, new Inventory());
        recipeIndex = 0;
        amtPaid = 0;
        error = false;
    }

    @Given("I have a coffee maker with empty recipe book.")
    public void iHaveACoffeeMakerWithEmptyRecipeBook() {
        setUpCoffeeMaker();
    }

    @Given("I have a coffee maker with a default recipe book.")
    public void iHaveACoffeeMaker() throws RecipeException {
        setUpCoffeeMaker();
        //Set up for r1
        Recipe recipe1 = createRecipe("Coffee", "0", "3", "1", "1", "50");

        //Set up for r2
        Recipe recipe2 = createRecipe("Latte", "0", "3", "3", "1", "100");

        //Set up for r3
        Recipe recipe3 = createRecipe("Black Coffee", "16", "16", "16", "16", "50");


        coffeeMaker.addRecipe(recipe1);
        coffeeMaker.addRecipe(recipe2);
        coffeeMaker.addRecipe(recipe3);
    }

    @When("I want to buy a {string} with {int}.")
    public void iWantToBuyAWith(String recipeToPurchase, int pay) {
        amtPaid = pay;
        try {
            Recipe[] recipeArray = coffeeMaker.getRecipes();
            recipeIndex = findRecipeIDByName(recipeArray, recipeToPurchase);
        } catch (Exception e) {
            System.out.println("Cannot find the recipe.");
            error = true;
            return;
        }
        String recipeName = coffeeMaker.getRecipes()[recipeIndex].getName();
        System.out.printf("Coffee maker found the %s. And your wallet have %d$%n", recipeName, amtPaid);
    }

    @Then("I got the withdrawal {int}.")
    public void iShouldGetAAndGotTheWithdrawal(int withdrawal) {
        assertEquals(withdrawal, coffeeMaker.makeCoffee(recipeIndex, amtPaid));
        System.out.printf("Your withdrawal is %d$%n", withdrawal);
    }

    @Then("I got the withdrawal {int} with check Inventory.")
    public void iShouldGetAAndGotTheWithdrawalWithCheckInventory(int withdrawal) {
        iShouldGetAAndGotTheWithdrawal(withdrawal);
        StringBuffer buf = createStringBuffer(12, 12, 14, 15);
        assertEquals(buf.toString(), coffeeMaker.checkInventory());
    }

    @When("I add a {string} {string} units.")
    public void iAddAIngredientsAmountUnits(String ingredient, String units) throws InventoryException {
        try {
            int addUnit = Integer.parseInt(units);
            switch (ingredient) {
                case "Coffee":
                    coffeeMaker.addInventory(units, "0", "0", "0");
                    buffer = createStringBuffer(15 + addUnit, 15, 15, 15);
                    break;
                case "Milk":
                    coffeeMaker.addInventory("0", units, "0", "0");
                    buffer = createStringBuffer(15, 15 + addUnit, 15, 15);
                    break;
                case "Chocolate":
                    coffeeMaker.addInventory("0", "0", "0", units);
                    buffer = createStringBuffer(15, 15, 15, 15 + addUnit);
                    break;
                case "Sugar":
                    coffeeMaker.addInventory("0", "0", units, "0");
                    buffer = createStringBuffer(15, 15, 15 + addUnit, 15);
                    break;
            }
        } catch (Exception e) {
            error = true;
            switch (ingredient) {
                case "Coffee":
                    assertThrows("Check amtCoffee to be Negative", InventoryException.class, () -> {
                        coffeeMaker.addInventory(units, "0", "0", "0");
                        assertEquals("Negative Coffee amount couldn't add to Inventory", buffer.toString(), coffeeMaker.checkInventory());
                    });
                    break;
                case "Milk":
                    assertThrows("Check amtMilk to be Negative", InventoryException.class, () -> {
                        coffeeMaker.addInventory("0", units, "0", "0");
                        assertEquals("Negative Milk amount couldn't add to Inventory", buffer.toString(), coffeeMaker.checkInventory());
                    });
                    break;
                case "Chocolate":
                    assertThrows("Check amtChocolate to be Negative", InventoryException.class, () -> {
                        coffeeMaker.addInventory("0", "0", "0", units);
                        assertEquals("Negative Sugar amount couldn't add to Inventory", buffer.toString(), coffeeMaker.checkInventory());
                    });
                    break;
                case "Sugar":
                    assertThrows("Check amtSugar to be Negative", InventoryException.class, () -> {
                        coffeeMaker.addInventory("0", "0", units, "0");
                        assertEquals("Negative Chocolate amount couldn't add to Inventory", buffer.toString(), coffeeMaker.checkInventory());
                    });
                    break;
            }
        }
    }

    @Then("The {string} have {int} units.")
    public void theIngredientsHaveUnits(String ingredient, int addUnit) {
        StringBuffer resultBuffer = new StringBuffer();
        switch (ingredient) {
            case "Coffee":
                resultBuffer = createStringBuffer(addUnit, 15, 15, 15);
                break;
            case "Milk":
                resultBuffer = createStringBuffer(15, addUnit, 15, 15);
                break;
            case "Chocolate":
                resultBuffer = createStringBuffer(15, 15, 15, addUnit);
                break;
            case "Sugar":
                resultBuffer = createStringBuffer(15, 15, addUnit, 15);
                break;
        }

        assertEquals("Ingredient should be able to add to Inventory ", resultBuffer.toString(), coffeeMaker.checkInventory());
    }

    @Then("I check the inventory.")
    public void iCheckTheInventory() {
        assertEquals(buffer.toString(), coffeeMaker.checkInventory());
    }

    @Given("I create a recipe book.")
    public void iHaveAnRecipeBook() {
        setUpCoffeeMaker();
    }

    @When("I add a {string} recipe.")
    public void iAddARecipe(String recipeName) throws RecipeException {
        Recipe recipe = createRecipe(recipeName, "0", "3", "1", "1", "50");
        assertTrue(coffeeMaker.addRecipe(recipe));
    }

    @Then("The recipe book have all recipe.")
    public void checkRecipeBook() {
        assertArrayEquals("Recipe should have only 3 recipe in recipe book.", new Recipe[3], coffeeMaker.getRecipes());
    }

    @Then("I cannot add a {string} recipe.")
    public void iCannotAddARecipeAgain(String recipeName) throws RecipeException {
        Recipe recipe = createRecipe(recipeName, "0", "3", "1", "1", "50");
        assertFalse("Cannot add the same recipe", coffeeMaker.addRecipe(recipe));
    }

    @Then("I delete the {string} recipe.")
    public void iDeleteTheRecipe(String recipeName) throws RecipeException {
        Recipe [] recipeArray = recipeBook.getRecipes();
        recipeIndex = findRecipeIDByName(recipeArray, recipeName);
        assertEquals(recipeName, coffeeMaker.deleteRecipe(recipeIndex));
    }

    @Then("I delete the Null recipe.")
    public void iDeleteTheNullRecipe() {
        assertNull(coffeeMaker.deleteRecipe(0));
    }

    @Then("I delete the recipe at {int} index.")
    public void iDeleteTheRecipeAtIndex(int rID) {
        assertThrows(RecipeException.class, () -> coffeeMaker.deleteRecipe(rID));
    }

    @Then("I edit the {string} recipe to {string} {string} {string} {string} {string}.")
    public void iEditTheRecipeTo(String recipeName, String amtChocolate, String amtCoffee, String amtMilk, String amtSugar, String price) throws RecipeException {
        Recipe [] recipeArray = recipeBook.getRecipes();
        recipeIndex = findRecipeIDByName(recipeArray, recipeName);
        Recipe newRecipe = createRecipe(recipeName, amtChocolate, amtCoffee, amtMilk, amtSugar, price);
        assertEquals(coffeeMaker.editRecipe(0, newRecipe), "Latte");
    }

    @Then("I edit the {string} recipe to {string} {string} {string} {string} {string} at {int} index.")
    public void iEditTheRecipeAtIndex(int rID, String recipeName, String amtChocolate, String amtCoffee, String amtMilk, String amtSugar, String price) throws RecipeException {
        Recipe newRecipe = createRecipe(recipeName, amtChocolate, amtCoffee, amtMilk, amtSugar, price);
        assertThrows(RecipeException.class, () -> coffeeMaker.editRecipe(rID, newRecipe));
    }
}
