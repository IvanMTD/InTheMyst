package ru.phoenix.game.content.inventory;

import java.util.HashMap;
import java.util.Map;

/**
 * Слоты экипировки персонажа.
 * 5 слотов: голова, тело, ботинки, оружие, доп. оружие.
 */
public class Equipment {
    private final Map<EquipmentSlot, Item> slots;

    public Equipment() {
        slots = new HashMap<>();
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            slots.put(slot, null);
        }
    }

    /** Экипировать предмет. Возвращает старый предмет из слота (или null). */
    public Item equip(Item item) {
        // Определяем подходящий слот
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (item.fitsSlot(slot)) {
                Item old = slots.get(slot);
                slots.put(slot, item);
                return old;
            }
        }
        return null; // предмет не подходит ни в один слот
    }

    /** Экипировать в конкретный слот */
    public Item equip(Item item, EquipmentSlot slot) {
        if (!item.fitsSlot(slot)) return null;
        Item old = slots.get(slot);
        slots.put(slot, item);
        return old;
    }

    /** Снять предмет из слота */
    public Item unequip(EquipmentSlot slot) {
        Item item = slots.get(slot);
        slots.put(slot, null);
        return item;
    }

    /** Получить предмет из слота */
    public Item getItem(EquipmentSlot slot) {
        return slots.get(slot);
    }

    /** Заменить предмет в слоте */
    public Item swapItem(EquipmentSlot slot, Item newItem) {
        if (!newItem.fitsSlot(slot)) return null;
        Item old = slots.get(slot);
        slots.put(slot, newItem);
        return old;
    }

    /** Все слоты */
    public Map<EquipmentSlot, Item> getSlots() {
        return slots;
    }

    /** Слоты в виде массива (для удобного обхода в UI) */
    public EquipmentSlot[] getSlotOrder() {
        return EquipmentSlot.values();
    }
}
