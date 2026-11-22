package ru.phoenix.game.content.inventory;

import ru.phoenix.core.math.Vector2f;

/**
 * Базовый класс предмета в инвентаре.
 * Каждый предмет имеет уникальный ID, тип, название и иконку (текстуру).
 */
public class Item {
    private static int nextId = 1;

    private final int id;
    private final String name;
    private final ItemType type;
    private final String iconPath;  // путь к текстуре иконки
    private final int iconTextureId; // ID загруженной текстуры (0 = placeholder)

    /** Создать предмет с текстурой-заглушкой (будет сгенерирована программно) */
    public Item(String name, ItemType type) {
        this.id = nextId++;
        this.name = name;
        this.type = type;
        this.iconPath = "";
        this.iconTextureId = 0;
    }

    /** Создать предмет с файлом текстуры */
    public Item(String name, ItemType type, String iconPath) {
        this.id = nextId++;
        this.name = name;
        this.type = type;
        this.iconPath = iconPath;
        this.iconTextureId = 0; // загрузка текстуры будет при инициализации
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public ItemType getType() { return type; }
    public String getIconPath() { return iconPath; }
    public int getIconTextureId() { return iconTextureId; }

    /** Совместим ли предмет с данным слотом экипировки */
    public boolean fitsSlot(EquipmentSlot slot) {
        switch (type) {
            case WEAPON:   return slot == EquipmentSlot.WEAPON;
            case OFF_HAND: return slot == EquipmentSlot.OFF_HAND;
            case HELMET:   return slot == EquipmentSlot.HELMET;
            case ARMOR:    return slot == EquipmentSlot.ARMOR;
            case BOOTS:    return slot == EquipmentSlot.BOOTS;
            default:       return false;
        }
    }

    @Override
    public String toString() {
        return name + " [" + type + "]";
    }
}
