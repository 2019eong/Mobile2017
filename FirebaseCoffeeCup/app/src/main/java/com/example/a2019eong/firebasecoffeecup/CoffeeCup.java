package com.example.a2019eong.firebasecoffeecup;

/**
 * Created by 2019eong on 10/25/2017.
 */

public class CoffeeCup {

    private String mCoffeeType;
    private int mAmtOfCoffee;
    public CoffeeCup()
    {
    }

    public CoffeeCup(String type, int amt)
    {
        mCoffeeType = type;
        mAmtOfCoffee = amt;
    }
    public String getCoffeeType()
    {
        return mCoffeeType;
    }
    public void setCoffeeType(String type)
    {
        mCoffeeType = type;
    }
    public int getAmtOfCoffee()
    {
        return mAmtOfCoffee;
    }
    public void setAmtOfCoffee(int amt)
    {
        mAmtOfCoffee = amt;
    }
}
