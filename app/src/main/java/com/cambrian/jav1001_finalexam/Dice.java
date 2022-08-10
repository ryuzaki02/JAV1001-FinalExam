package com.cambrian.jav1001_finalexam;

public class Dice {
    private String diceName;
    private int sides;

    /**
     * Construct to initialise Dice object
     * @params: sides - Number of sides, name - Name of dice
     * @return: dice object
     */
    Dice(String diceName, int sides) {
        this.diceName = diceName;
        this.sides = sides;
    }

    /**
     * Provides dice name
     * @params: nothing
     * @return: String - dice name
     */
    public String getDiceName() {
        return this.diceName;
    }

    /**
     * Provides dice sides
     * @params: nothing
     * @return: int - dice sides
     */
    public int getDiceSides() {
        return this.sides;
    }

    /**
     * Roll method to generate random number with respect to number of sides of dice
     * @params: nothing
     * @return: String - random string number
     */
    public String roll() {
        boolean isTrueDice = diceName.compareTo("True 10 Sided Dice") == 0;
        int rollItem = (int)(Math.random() * this.getDiceSides()) + (isTrueDice ? 0 : 1);
        int calculatedValue = rollItem * (sides == 10 ? 10 : 1);
        if (isTrueDice && calculatedValue == 0) {
            return "00";
        }
        return String.valueOf(calculatedValue);
    }
}
