package com.example.ishaandhamija.sunodilse;

/**
 * Created by ishaandhamija on 20/04/17.
 */

class Check {

    String name;
    boolean ticked;

    public String getName() {
        return name;
    }

    public boolean isTicked() {
        return ticked;
    }

    public Check(String name, boolean ticked) {

        this.name = name;
        this.ticked = ticked;
    }

    public void setTicked(boolean ticked) {
        this.ticked = ticked;
    }
}
