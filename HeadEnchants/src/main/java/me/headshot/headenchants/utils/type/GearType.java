package me.headshot.headenchants.utils.type;

public enum GearType {
    ARMOR(ItemType.HELMET, ItemType.CHESTPLATE, ItemType.LEGGINGS, ItemType.BOOTS),
    TOOL(ItemType.SHOVEL, ItemType.PICKAXE, ItemType.AXE),
    WEAPON(ItemType.SWORD, ItemType.BOW);

    private ItemType[] itemTypes;

    GearType(ItemType... itemTypes) {
        this.itemTypes = itemTypes;
    }

    public boolean isType(ItemType itemType) {
        for(ItemType type : itemTypes){
            if(itemType == type)
                return true;
        }
        return false;
    }

    public ItemType[] getItemTypes() {
        return itemTypes;
    }
}
