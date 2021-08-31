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

    /**
     * The object under test.
     */
    private CoffeeMaker coffeeMaker;

    // Sample recipes to use in testing.
    private Recipe recipe1;
    private Recipe recipe2;
    private Recipe recipe3;
    private Recipe recipe4;

    //Used to capture the System.out
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

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
        recipe1 = new Recipe();
        recipe1.setName("Coffee");
        recipe1.setAmtChocolate("0");
        recipe1.setAmtCoffee("3");
        recipe1.setAmtMilk("1");
        recipe1.setAmtSugar("1");
        recipe1.setPrice("50");

        //Set up for r2
        recipe2 = new Recipe();
        recipe2.setName("Mocha");
        recipe2.setAmtChocolate("20");
        recipe2.setAmtCoffee("3");
        recipe2.setAmtMilk("1");
        recipe2.setAmtSugar("1");
        recipe2.setPrice("75");

        //Set up for r3
        recipe3 = new Recipe();
        recipe3.setName("Latte");
        recipe3.setAmtChocolate("0");
        recipe3.setAmtCoffee("3");
        recipe3.setAmtMilk("3");
        recipe3.setAmtSugar("1");
        recipe3.setPrice("100");

        //Set up for r4
        recipe4 = new Recipe();
        recipe4.setName("Hot Chocolate");
        recipe4.setAmtChocolate("4");
        recipe4.setAmtCoffee("0");
        recipe4.setAmtMilk("1");
        recipe4.setAmtSugar("1");
        recipe4.setPrice("65");
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
        Recipe tmpRecipe = new Recipe();
        tmpRecipe.setName("");
        tmpRecipe.setAmtChocolate("0");
        tmpRecipe.setAmtCoffee("3");
        tmpRecipe.setAmtMilk("1");
        tmpRecipe.setAmtSugar("1");
        tmpRecipe.setPrice("50");
        assertFalse("Shouldn't add the black name into the recipe book", coffeeMaker.addRecipe(tmpRecipe));
    }

    @Test
    public void testDeleteRecipe() throws RecipeException {
        CoffeeMaker tmpCoffeeMaker = new CoffeeMaker();
        Recipe tmpRecipe = new Recipe();
        tmpRecipe.setName("Latte");
        tmpRecipe.setAmtChocolate("0");
        tmpRecipe.setAmtCoffee("3");
        tmpRecipe.setAmtMilk("1");
        tmpRecipe.setAmtSugar("1");
        tmpRecipe.setPrice("50");
        assertTrue(tmpCoffeeMaker.addRecipe(tmpRecipe));
        assertEquals(tmpCoffeeMaker.deleteRecipe(0), "Latte");
    }

    @Test
    public void testDeleteNullRecipe() throws RecipeException {
        assertNull(coffeeMaker.deleteRecipe(0));
    }

    @Test
    public void testDeleteNegativeArrayRecipe() throws RecipeException {
        assertThrows(RecipeException.class, () -> coffeeMaker.deleteRecipe(-1));
    }

    @Test
    public void testDeleteRecipeNotInList() throws RecipeException {
        assertThrows(RecipeException.class, () -> coffeeMaker.deleteRecipe(11));
    }

    @Test
    public void testEditRecipe() throws RecipeException {
        CoffeeMaker tmpCoffeeMaker = new CoffeeMaker();
        Recipe tmpRecipe = new Recipe();
        tmpRecipe.setName("Latte");
        tmpRecipe.setAmtChocolate("0");
        tmpRecipe.setAmtCoffee("3");
        tmpRecipe.setAmtMilk("1");
        tmpRecipe.setAmtSugar("1");
        tmpRecipe.setPrice("50");

        Recipe newRecipe = new Recipe();
        newRecipe.setName("Latte");
        newRecipe.setAmtChocolate("5");
        newRecipe.setAmtCoffee("3");
        newRecipe.setAmtMilk("1");
        newRecipe.setAmtSugar("1");
        newRecipe.setPrice("75");
        assertTrue(tmpCoffeeMaker.addRecipe(tmpRecipe));
        assertEquals(tmpCoffeeMaker.editRecipe(0, newRecipe), "Latte");
    }

    @Test
    public void testEditNegativeArrayRecipe() throws RecipeException {
        Recipe newRecipe = new Recipe();
        newRecipe.setName("Latte");
        newRecipe.setAmtChocolate("5");
        newRecipe.setAmtCoffee("3");
        newRecipe.setAmtMilk("1");
        newRecipe.setAmtSugar("1");
        newRecipe.setPrice("75");
        assertThrows(RecipeException.class, () -> coffeeMaker.editRecipe(-1, newRecipe));
    }

    @Test
    public void testEditRecipeNotInList() throws RecipeException {
        Recipe newRecipe = new Recipe();
        newRecipe.setName("Latte");
        newRecipe.setAmtChocolate("5");
        newRecipe.setAmtCoffee("3");
        newRecipe.setAmtMilk("1");
        newRecipe.setAmtSugar("1");
        newRecipe.setPrice("75");
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
    public void testAddInventory() throws InventoryException {
        coffeeMaker.addInventory("4", "7", "0", "9");
    }

    @Test
    public void testAddNegativeIngredientToInventory() {
        assertThrows("Check amtCoffee to be Negative", InventoryException.class, () -> {
            coffeeMaker.addInventory("-1", "7", "0", "9");
            coffeeMaker.checkInventory();
        });
        assertThrows("Check amtMilk to be Negative", InventoryException.class, () -> {
            coffeeMaker.addInventory("4", "-1", "0", "9");
            coffeeMaker.checkInventory();
        });
        assertThrows("Check amtSugar to be Negative", InventoryException.class, () -> {
            coffeeMaker.addInventory("4", "7", "-1", "9");
            coffeeMaker.checkInventory();
        });
        assertThrows("Check amtChocolate to be Negative", InventoryException.class, () -> {
            coffeeMaker.addInventory("4", "7", "0", "-1");
            coffeeMaker.checkInventory();
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
        assertThrows("Input String in amtCoffee", InventoryException.class, () -> {
            coffeeMaker.addInventory("asdf", "-1", "0", "3");
            coffeeMaker.checkInventory();
        });
        assertThrows("Input String in amtMilk", InventoryException.class, () -> {
            coffeeMaker.addInventory("4", "asdf", "0", "3");
            coffeeMaker.checkInventory();
        });
        assertThrows("Input String in amtSugar", InventoryException.class, () -> {
            coffeeMaker.addInventory("4", "-1", "asdf", "3");
            coffeeMaker.checkInventory();
        });
        assertThrows("Input String in amtChocolate", InventoryException.class, () -> {
            coffeeMaker.addInventory("4", "-1", "0", "asdf");
            coffeeMaker.checkInventory();
        });
    }

    @Test
    public void testCheckInventory() throws InventoryException {
        StringBuffer buf = new StringBuffer();
        buf.append("Coffee: ");
        buf.append(15);
        buf.append("\n");
        buf.append("Milk: ");
        buf.append(15);
        buf.append("\n");
        buf.append("Sugar: ");
        buf.append(15);
        buf.append("\n");
        buf.append("Chocolate: ");
        buf.append(15);
        buf.append("\n");
        assertEquals(coffeeMaker.checkInventory(), buf.toString());
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
    public void testPurchaseBeverageWithInventory() throws InventoryException {
        coffeeMaker.addRecipe(recipe1);
        assertEquals(25, coffeeMaker.makeCoffee(0, 75));

        StringBuffer buf = new StringBuffer();
        buf.append("Coffee: ");
        buf.append(12);
        buf.append("\n");
        buf.append("Milk: ");
        buf.append(14);
        buf.append("\n");
        buf.append("Sugar: ");
        buf.append(14);
        buf.append("\n");
        buf.append("Chocolate: ");
        buf.append(15);
        buf.append("\n");

        assertEquals(coffeeMaker.checkInventory(), buf.toString());
    }
}
