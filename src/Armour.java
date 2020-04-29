public class Armour extends Item {
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
}