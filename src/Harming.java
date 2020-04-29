public class Harming extends Item implements Potion {
    public Harming(String inName, int inCost, int inMinEffect, int inMaxEffect) {
        super(inName, inCost, inMinEffect, inMaxEffect);
    }
    @Override
    public void effect(Character target) {
        System.out.println("Attack Potion!");
    }
    @Override
    public String toString() {
        return String.format("%s Harming Potion, Cost: %d, Damage: %d - %d", name, cost,
        minimumEffect, maxEffect);
    }
}