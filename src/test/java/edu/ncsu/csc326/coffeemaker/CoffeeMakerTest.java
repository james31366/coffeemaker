/*
 * Copyright (c) 2009,  Sarah Heckman, Laurie Williams, Dright Ho
 * All Rights Reserved.
 *
 * Permission has been explicitly granted to the University of Minnesota
 * Software Engineering Center to use and distribute this source for
 * educational purposes, including delivering online education through
 * Coursera or other entities.
 *
 * No warranty is given regarding this software, including warranties as
 * to the correctness or completeness of this software, including
 * fitness for purpose.
 *
 *
 * Modifications
 * 20171114 - Ian De Silva - Updated to comply with JUnit 4 and to adhere to
 * 							 coding standards.  Added test documentation.
 */
package edu.ncsu.csc326.coffeemaker;

import static edu.ncsu.csc326.coffeemaker.Main.checkInventory;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc326.coffeemaker.exceptions.InventoryException;
import edu.ncsu.csc326.coffeemaker.exceptions.RecipeException;
import org.mockito.internal.matchers.Null;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * Unit tests for CoffeeMaker class.
 *
 * @author Sarah Heckman
 */
public class CoffeeMakerTest {

    //Used to capture the System.out
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    /**
     * The object under test.
     */
    private CoffeeMaker coffeeMaker;
    // Sample recipes to use in testing.
    private Recipe recipe1;
    private Recipe recipe2;
    private Recipe recipe3;
    private Recipe recipe4;

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

    /**
     * Initializes some recipes to test with and the {@link CoffeeMaker}
     * object we wish to test.
     *
     * @throws RecipeException if there was an error parsing the ingredient
     *                         amount when setting up the recipe.
     */
    @Before
    public void setUp() throws RecipeException {
        coffeeMaker = new CoffeeMaker();

        System.setOut(new PrintStream(outputStreamCaptor));

        //Set up for r1
        recipe1 = createRecipe("Coffee", "0", "3", "1", "1", "50");

        //Set up for r2
        recipe2 = createRecipe("Mocha", "20", "3", "1", "1", "75");

        //Set up for r3
        recipe3 = createRecipe("Latte", "0", "3", "3", "1", "100");

        //Set up for r4
        recipe4 = createRecipe("Hot Chocolate", "4", "0", "1", "1", "65");
    }


    /**
     * The Recipe Book cannot add more than three Recipe into List if add more than three it should fail.
     */
    @Test
    public void testAddRecipeMoreThanThree() {
        assertTrue(coffeeMaker.addRecipe(recipe1));
        assertTrue(coffeeMaker.addRecipe(recipe2));
        assertTrue(coffeeMaker.addRecipe(recipe3));
        assertFalse("Cannot add the recipe more that 3 recipes", coffeeMaker.addRecipe(recipe4));
    }

    /**
     * The Recipe Book cannot add the same recipe into the list.
     */
    @Test
    public void testAddSameRecipe() {
        assertTrue(coffeeMaker.addRecipe(recipe1));
        assertFalse("Cannot add the same recipe", coffeeMaker.addRecipe(recipe1));
    }

    /**
     * The Recipe's name cannot be blank if blank it shouldn't be added to list.
     */
    @Test
    public void testAddBlankNameRecipe() throws RecipeException {
        Recipe tmpRecipe = createRecipe("", "0", "3", "1", "1", "50");
        assertFalse("Shouldn't add the black name into the recipe book", coffeeMaker.addRecipe(tmpRecipe));
    }

    /**
     * Check delete recipe function that work correctly or not.
     *
     * @throws RecipeException Exception for Recipe Class
     */
    @Test
    public void testDeleteRecipe() throws RecipeException {
        CoffeeMaker tmpCoffeeMaker = new CoffeeMaker();
        Recipe tmpRecipe = createRecipe("Latte", "0", "3", "1", "1", "50");
        assertTrue(tmpCoffeeMaker.addRecipe(tmpRecipe));
        assertEquals(tmpCoffeeMaker.deleteRecipe(0), "Latte");
    }

    /**
     * Delete the null recipe.
     */
    @Test
    public void testDeleteNullRecipe() {
        assertNull(coffeeMaker.deleteRecipe(0));
    }

    /**
     * Delete the recipe that input value of recipeToDelete is negative number.
     *
     * @throws RecipeException Exception for Recipe Class
     */
    @Test
    public void testDeleteNegativeArrayRecipe() throws RecipeException {
        assertThrows(RecipeException.class, () -> coffeeMaker.deleteRecipe(-1));
    }

    /**
     * Delete the recipe that input value of recipeToDelete is not in list.
     */
    @Test
    public void testDeleteRecipeNotInList() {
        assertThrows(RecipeException.class, () -> coffeeMaker.deleteRecipe(11));
    }

    /**
     * Check edit recipe function with normal way.
     *
     * @throws RecipeException Exception for Recipe class.
     */
    @Test
    public void testEditRecipe() throws RecipeException {
        CoffeeMaker tmpCoffeeMaker = new CoffeeMaker();
        Recipe tmpRecipe = createRecipe("Latte", "0", "3", "1", "1", "50");
        assertTrue(tmpCoffeeMaker.addRecipe(tmpRecipe));

        Recipe newRecipe = createRecipe("Latte", "5", "3", "1", "1", "75");
        assertEquals(tmpCoffeeMaker.editRecipe(0, newRecipe), "Latte");
    }

    /**
     * Edit the recipe that input value of recipeToEdit as NegativeNumber.
     *
     * @throws RecipeException Exception for Recipe class.
     */
    @Test
    public void testEditNegativeArrayRecipe() throws RecipeException {
        Recipe newRecipe = createRecipe("Latte", "5", "3", "1", "1", "75");
        assertThrows(RecipeException.class, () -> coffeeMaker.editRecipe(-1, newRecipe));
    }

    /**
     * Edit the recipe that input value of recipeToEdit is not in List.
     *
     * @throws RecipeException Exception for Recipe class.
     */
    @Test
    public void testEditRecipeNotInList() throws RecipeException {
        Recipe newRecipe = createRecipe("Latte", "5", "3", "1", "1", "75");
        assertThrows(RecipeException.class, () -> coffeeMaker.editRecipe(11, newRecipe));
    }

    /**
     * Given a coffee maker with the default inventory
     * When we add inventory with well-formed quantities
     * Then we do not get an exception trying to read the inventory quantities.
     *
     * @throws InventoryException if there was an error parsing the quantity
     *                            to a positive integer.
     */
    @Test
    public void testAddPositiveIngredientToInventory() throws InventoryException {
        coffeeMaker.addInventory("1", "0", "0", "0");
        assertEquals("Positive coffee amount should be able to add to Inventory ", "Coffee: 16\nMilk: 15\nSugar: 15\nChocolate: 15\n", coffeeMaker.checkInventory());

        coffeeMaker.addInventory("0", "1", "0", "0");
        assertEquals("Positive milk amount should be able to add to Inventory ", "Coffee: 16\nMilk: 16\nSugar: 15\nChocolate: 15\n", coffeeMaker.checkInventory());

        coffeeMaker.addInventory("0", "0", "1", "0");
        assertEquals("Positive sugar amount should be able to add to Inventory ", "Coffee: 16\nMilk: 16\nSugar: 16\nChocolate: 15\n", coffeeMaker.checkInventory());

        coffeeMaker.addInventory("0", "0", "0", "1");
        assertEquals("Positive chocolate amount should be able to add to Inventory ", "Coffee: 16\nMilk: 16\nSugar: 16\nChocolate: 16\n", coffeeMaker.checkInventory());
    }

    /**
     * Add the Negative Coffee amount into add Inventory function.
     */
    @Test
    public void testAddNegativeIngredientToInventory() {
        StringBuffer buf = createStringBuffer(15, 15, 15, 15);
        assertThrows("Check amtCoffee to be Negative", InventoryException.class, () -> {
            coffeeMaker.addInventory("-1", "7", "0", "9");
            assertEquals("Negative Coffee amount couldn't add to Inventory", buf.toString(), coffeeMaker.checkInventory());
        });
        assertThrows("Check amtMilk to be Negative", InventoryException.class, () -> {
            coffeeMaker.addInventory("4", "-1", "0", "9");
            assertEquals("Negative Milk amount couldn't add to Inventory", buf.toString(), coffeeMaker.checkInventory());
        });
        assertThrows("Check amtChocolate to be Negative", InventoryException.class, () -> {
            coffeeMaker.addInventory("4", "7", "0", "-1");
            assertEquals("Negative Sugar amount couldn't add to Inventory", buf.toString(), coffeeMaker.checkInventory());
        });
        assertThrows("Check amtSugar to be Negative", InventoryException.class, () -> {
            coffeeMaker.addInventory("4", "7", "-1", "9");
            assertEquals("Negative Chocolate amount couldn't add to Inventory", buf.toString(), coffeeMaker.checkInventory());
        });
    }

    /**
     * Given a coffee maker with the default inventory
     * When we add inventory with malformed quantities (i.e., a negative
     * quantity and a non-numeric string)
     * Then we get an inventory exception
     */
    @Test
    public void testAddInventoryException() {
        StringBuffer buf = createStringBuffer(15, 15, 15, 15);
        assertThrows("Input String in amtCoffee", InventoryException.class, () -> {
            coffeeMaker.addInventory("asdf", "7", "0", "3");
            assertEquals("Coffee amount as String couldn't add to Inventory", buf.toString(), coffeeMaker.checkInventory());
        });
        assertThrows("Input String in amtMilk", InventoryException.class, () -> {
            coffeeMaker.addInventory("4", "asdf", "0", "3");
            assertEquals("Milk amount as String couldn't add to Inventory", buf.toString(), coffeeMaker.checkInventory());
        });
        assertThrows("Input String in amtSugar", InventoryException.class, () -> {
            coffeeMaker.addInventory("4", "7", "asdf", "3");
            assertEquals("Sugar amount as String couldn't add to Inventory", buf.toString(), coffeeMaker.checkInventory());
        });
        assertThrows("Input String in amtChocolate", InventoryException.class, () -> {
            coffeeMaker.addInventory("4", "7", "0", "asdf");
            assertEquals("Chocolate amount as String couldn't add to Inventory", buf.toString(), coffeeMaker.checkInventory());
        });
    }

    @Test
    public void testCheckInventoryToInventory() {
        StringBuffer buf = createStringBuffer(15, 15, 15, 15);
        assertEquals(buf.toString(), coffeeMaker.checkInventory());
    }

    /**
     * Given a coffee maker with one valid recipe
     * When we make coffee, selecting the valid recipe and paying more than
     * the coffee costs
     * Then we get the correct change back.
     */
    @Test
    public void testPurchaseBeverage() {
        coffeeMaker.addRecipe(recipe1);
        assertEquals(25, coffeeMaker.makeCoffee(0, 75));
    }

    @Test
    public void testPurchaseBeverageWithInventory() {
        coffeeMaker.addRecipe(recipe1);
        assertEquals(25, coffeeMaker.makeCoffee(0, 75));

        StringBuffer buf = createStringBuffer(12, 14, 14, 15);
        assertEquals(buf.toString(), coffeeMaker.checkInventory());
    }

    @Test
    public void testPurchaseBeverageWithNullRecipe() {
        assertThrows(InventoryException.class, () -> {
            coffeeMaker.makeCoffee(3, 75);
        });
    }

    @Test
    public void testPurchaseBeverageWithNotEnoughIngredient() throws RecipeException {
        Recipe blackCoffee = createRecipe("Black Coffee", "16", "16", "16", "16", "50");
        coffeeMaker.addRecipe(blackCoffee);
        assertEquals(100, coffeeMaker.makeCoffee(0, 100));
    }

    @Test
    public void testPurchaseBeverageWithNotEnoughMoney() {
        coffeeMaker.addRecipe(recipe1);
        assertEquals(1, coffeeMaker.makeCoffee(0, 1));
    }


}
