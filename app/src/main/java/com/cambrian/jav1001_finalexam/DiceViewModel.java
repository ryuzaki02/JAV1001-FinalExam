package com.cambrian.jav1001_finalexam;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class DiceViewModel {
    ArrayList<Dice> dices = new ArrayList<Dice>();
    ArrayList<String> previousRounds = new ArrayList<String>();
    boolean isRollAgain = false;
    String previousValue = "";
    private Dice selectedDice;

    /**
     * Constructor that creates the view model's object and initialise static dices
     * @params: nothing
     * @return: view model object
     */
    DiceViewModel() {
        addStaticDices();
        selectedDice = (Dice) dices.toArray()[0];
    }

    /**
     * Method to add static dice objects to dice array
     * @params: nothing
     * @return: nothing
     */
    public void addStaticDices() {
        dices = new ArrayList<Dice>();
        dices.add(new Dice("4 Sided Dice", 4));
        dices.add(new Dice("6 Sided Dice", 6));
        dices.add(new Dice("8 Sided Dice", 8));
        dices.add(new Dice("10 Sided Dice", 10));
        dices.add(new Dice("True 10 Sided Dice", 10));
        dices.add(new Dice("12 Sided Dice", 12));
        dices.add(new Dice("20 Sided Dice", 20));
        dices.add(new Dice("Add New Dice", 0));
    }

    /**
     * Adds value to History to show in the list view
     * @params: value - New value after a dice roll
     * @return: nothing
     */
    public void addPreviousRound(String value) {
        previousRounds.add(0, selectedDice.getDiceName() + ": " + value);
    }

    /**
     * Checks if user entered sides already present in the dices array or not
     * @params: sides - New user entered sides text
     * @return: boolean - true or false
     */
    public boolean checkIfSidesExists(int sides) {
        for (Dice dice:
             dices) {
            if (dice.getDiceSides() == sides) {
                return true;
            }
        }
        return false;
    }

    /**
     * Convert array list of dices array to list of dice name strings
     * @params: nothing
     * @return: string array of dice names
     */
    public String[] getDiceNames() {
        String[] diceNames = new String[dices.size()];
        for (int i = 0; i < diceNames.length; i++) {
            Dice dice = (Dice) dices.toArray()[i];
            diceNames[i] = dice.getDiceName();
        }
        return diceNames;
    }

    /**
     * Sets user selected dice to the local variable for further usage
     * @params: position - Adds dice to particular index
     * @return: nothing
     */
    public void setSelectedDice(int position) {
        Dice dice = (Dice) this.dices.toArray()[position];
        this.selectedDice = dice;
    }

    /**
     * Returns selected dice object from local variable
     * @params: nothing
     * @return: dice object
     */
    public Dice getSelectedDice() {
        return this.selectedDice;
    }
 }
