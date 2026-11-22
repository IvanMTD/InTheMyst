package ru.phoenix.game.content.inventory;

/** Слоты экипировки персонажа */
public enum EquipmentSlot {
    HELMET("Голова"),
    ARMOR("Тело"),
    BOOTS("Ботинки"),
    WEAPON("Оружие"),
    OFF_HAND("Доп. оружие");

    private final String displayName;

    EquipmentSlot(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
