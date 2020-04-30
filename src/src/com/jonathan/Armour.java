package com.jonathan;

import java.util.Random;

public class Armour extends Item implements Wearable {
    private String material;
    public Armour(String inName, int inCost, int inMinEffect, int inMaxEffect, String inMaterial) {
        super(inName, inCost, inMinEffect, inMaxEffect);
        material = inMaterial;
    }
    @Override
    public String toString() {
        return String.format("%s, Material: %s, Cost: %d, Armour: %d - %d", name, material, cost,
        minimumEffect, maxEffect);
    }
    @Override
    public int getStat() {
        Random rand = new Random();
        return rand.nextInt((maxEffect - minimumEffect) + 1) + minimumEffect;
    }
}