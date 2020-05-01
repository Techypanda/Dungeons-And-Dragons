package com.jonathan.model;

import com.jonathan.model.Enchantment;
import com.jonathan.model.Wearable;

public class DamageEnchantment extends Enchantment { /* Basic Damage Increase, Can be used with any value. */
    private int dmgIncrease;
    public DamageEnchantment(Wearable next, int dmgIncrement) {
        super(next, String.format("Damage Enchantment (+%d)", dmgIncrement));
        dmgIncrease = dmgIncrement;
    }
    @Override
    public int getEffect() {
        return (dmgIncrease + next.getEffect()); /* This means that you can use this as a generic + increase, covers both +2 enchant and +5 aswell as any extra devs might want. */
    }
}