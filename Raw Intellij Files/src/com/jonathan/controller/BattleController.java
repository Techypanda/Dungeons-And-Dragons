package com.jonathan.controller;

import com.jonathan.model.*;
import com.jonathan.model.Character;
import com.jonathan.view.BattleViewer;

import java.util.Random;

/**
 * Purpose: To Control Battles
 * Author: Jonathan Wright
 * Date: 23/05/2020
 */
public class BattleController {
    private double[] probabilities;
    private Battle battle;
    private BattleViewer theView;
    private Player player;
    private InventoryController invController;

    /**
     * Alternate Constructor for Battle Controller
     * @param player Injected Player for battle
     * @param view Injected View for battle
     * @param invController Injected Inventory Controller
     */
    public BattleController(Player player, BattleViewer view, InventoryController invController) {
        probabilities = new double[4];
        probabilities[0] = 0.5;
        probabilities[1] = 0.3;
        probabilities[2] = 0.2;
        probabilities[3] = 0;
        this.invController = invController;
        this.player = player;
        this.theView = view;
    } /* Battle Controller Does Not need to start battle here. */

    /**
     * Generates a enemy for the battle. - Fairly sure I got the algorithm for this wrong. It seems to work?
     * @return A Generated Enemy based off current probabilities
     */
    private Character generateEnemy() {
        /* Statistics Problem?? */
        double fullWeight = 1.0; /* Should Never Add Up to more than this, so no need to sum them. */
        double roll = Math.random() * fullWeight;
        int selection = -1;
        double weightSum = 0;
        int i = 0;
        for (; i < probabilities.length; i++) {
            weightSum += probabilities[i];
            if (weightSum >= roll)
                break;
        }
        selection = i + 1;
        switch(selection) {
            case 1:
                return new Slime();
            case 2:
                return new Goblin();
            case 3:
                return new Ogre();
            case 4:
                return new Dragon();
            default:
                System.out.println("Received selection = " + selection);
                return null;
        }
    }

    /**
     * Adjusts probabilities each battle.
     */
    private void adjustProbabilities() {
        double dragonInc = 0;
        if (probabilities[0] > 0.05) {
            probabilities[0] -= 0.05;
            dragonInc += 0.05;
        }
        if (probabilities[1] > 0.05) {
            probabilities[1] -= 0.05;
            dragonInc += 0.05;
        }
        if (probabilities[2] > 0.05) {
            probabilities[2] -= 0.05;
            dragonInc += 0.05;
        }
        probabilities[3] += dragonInc;
    }

    /**
     * Starts a new battle.
     */
    public void beginBattle() {
        Random rand = new Random();
        Character enemy = generateEnemy();
        Battle battle = new Battle(player, enemy);
        theView.setBattle(battle); // OBSERVER PATTERN, VIEW UPDATING BASED OFF BATTLE MODEL
        theView.announceBattle(player.getName(), enemy.getName());
        while (battle.getWinner() == null) {
            if (battle.getTurn() == 1) { /* Player */
                int response = theView.requestOption();
                if (response == 1) {
                    if (player.hasPotions()) {
                        String potionName = theView.queryPotion(player.showPotions());
                        Damage[] dmgPotion = player.getDamagePotion(potionName);
                        if (dmgPotion.length > 1) {
                            String[] potionDesc = new String[dmgPotion.length];
                            for (int i = 0; i < dmgPotion.length; i++)
                                potionDesc[i] = dmgPotion[i].toString();
                            Potion selected = dmgPotion[theView.multipleSelection(potionDesc)];
                            int potionDmg = selected.getEffect();
                            int beforeHP = enemy.getHP();
                            enemy.takeDamage(potionDmg);
                            theView.damagePotion(potionName, enemy.getName(), potionDmg, beforeHP, enemy.getHP());
                            battle.incrementTurn();
                            invController.removeFromInventory(selected);
                        } else if (dmgPotion.length == 1) {
                            int potionDmg = dmgPotion[0].getEffect();
                            int beforeHP = enemy.getHP();
                            enemy.takeDamage(potionDmg);
                            theView.damagePotion(potionName, enemy.getName(), potionDmg, beforeHP, enemy.getHP());
                            battle.incrementTurn();
                            invController.removeFromInventory(dmgPotion[0]);
                        } else {
                            Healing[] hpPot = player.getHealingPotion(potionName);
                            if (hpPot.length > 1) {
                                String[] potionDesc = new String[hpPot.length];
                                for (int i = 0; i < hpPot.length; i++)
                                    potionDesc[i] = hpPot[i].toString();
                                Potion selected = hpPot[theView.multipleSelection(potionDesc)];
                                int potionHeal = selected.getEffect();
                                int healHP = player.getHP() + potionHeal;
                                if (healHP > 100) healHP = 100;
                                player.setHP(healHP);
                                theView.healPotion(potionName, player.getName(), potionHeal, player.getHP());
                                battle.incrementTurn();
                                invController.removeFromInventory(selected);
                            } else if (hpPot.length == 1) {
                                int potionHeal = hpPot[0].getEffect();
                                int healHP = player.getHP() + potionHeal;
                                if (healHP > 100) healHP = 100;
                                player.setHP(healHP);
                                theView.healPotion(potionName, player.getName(), potionHeal, player.getHP());
                                battle.incrementTurn();
                                invController.removeFromInventory(hpPot[0]);
                            } else {
                                theView.failedPotion(potionName, "That potion does not exist in your inventory.");
                            }
                        }
                    } else {
                        theView.noPotions();
                    }
                } else
                    battle.doBattle();
            } else { /* AI */
                battle.doBattle();
            }
        }
        adjustProbabilities();
        player.setGold(player.getGold() + battle.getReward());
    }
}
