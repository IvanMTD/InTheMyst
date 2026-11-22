package ru.phoenix.game.hud.inventory;

import ru.phoenix.core.buffer.vbo.MeshConfig;
import ru.phoenix.core.buffer.vbo.VertexBufferObject;
import ru.phoenix.core.debug.Logger;
import ru.phoenix.core.kernel.Camera;
import ru.phoenix.core.kernel.Input;
import ru.phoenix.core.kernel.Window;
import ru.phoenix.core.loader.texture.Texture2D;
import ru.phoenix.core.math.Projection;
import ru.phoenix.core.math.Vector2f;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.core.shader.Shader;
import ru.phoenix.game.content.characters.Character;
import ru.phoenix.game.content.inventory.Backpack;
import ru.phoenix.game.content.inventory.Equipment;
import ru.phoenix.game.content.inventory.EquipmentSlot;
import ru.phoenix.game.content.inventory.Item;
import ru.phoenix.game.content.inventory.ItemType;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

/**
 * HUD инвентаря персонажа — рисуется через мировые координаты + матрицу модели.
 * Аналогично SelfIndicators над головой персонажа.
 * 
 * Левая панель: 5 слотов экипировки (голова, тело, ботинки, оружие, доп.)
 * Правая панель: рюкзак 5×5
 */
public class InventoryHUD {
    private static InventoryHUD instance;

    private boolean isOpen;
    private Character selectedCharacter;

    // Размеры (в мировых единицах, аналогично индикаторам)
    private final float SLOT_SIZE_W = 0.25f;    // размер ячейки в мировых единицах (уменьшено в 3 раза)
    private final float SLOT_GAP_W = 0.03f;     // отступ между ячейками
    private final float PADDING_W = 0.05f;      // отступ внутри окна
    private final float TITLE_H_W = 0.07f;      // высота заголовка
    private final float PANELS_GAP = 0.1f;      // промежуток между левой и правой панелью

    // VBO
    private VertexBufferObject slotVbo;
    private Shader hudShader;

    // Drag & Drop
    private Item draggedItem;
    private TargetType dragSource;
    private int dragSourceX, dragSourceY;
    private EquipmentSlot dragSourceSlot;
    private boolean isDragging;
    private Vector3f dragWorldPos;

    // Placeholder текстуры
    private Texture2D emptySlotTexture;
    private Texture2D[] typePlaceholderTextures;

    private InventoryHUD() {
        createPlaceholderTextures();
        initVbo();
        initShader();
    }

    private void initShader() {
        hudShader = new Shader();
        hudShader.createVertexShader("VS_inventory.glsl");
        hudShader.createFragmentShader("FS_inventory.glsl");
        hudShader.createProgram();
    }

    public static InventoryHUD getInstance() {
        if (instance == null) {
            instance = new InventoryHUD();
        }
        return instance;
    }

    private void initVbo() {
        slotVbo = new MeshConfig();
    }

    private void createPlaceholderTextures() {
        int size = 64;
        // Фон слота — тёмный серый с яркой границей
        emptySlotTexture = createColoredTexture(size, size, 0.15f, 0.15f, 0.2f, 1.0f, true);

        typePlaceholderTextures = new Texture2D[ItemType.values().length];
        typePlaceholderTextures[ItemType.WEAPON.ordinal()] = createColoredTexture(size, size, 0.9f, 0.2f, 0.2f, 1.0f, true);
        typePlaceholderTextures[ItemType.OFF_HAND.ordinal()] = createColoredTexture(size, size, 0.2f, 0.4f, 0.9f, 1.0f, true);
        typePlaceholderTextures[ItemType.HELMET.ordinal()] = createColoredTexture(size, size, 0.9f, 0.9f, 0.2f, 1.0f, true);
        typePlaceholderTextures[ItemType.ARMOR.ordinal()] = createColoredTexture(size, size, 0.2f, 0.9f, 0.2f, 1.0f, true);
        typePlaceholderTextures[ItemType.BOOTS.ordinal()] = createColoredTexture(size, size, 0.7f, 0.3f, 0.9f, 1.0f, true);
        typePlaceholderTextures[ItemType.CONSUMABLE.ordinal()] = createColoredTexture(size, size, 0.9f, 0.6f, 0.1f, 1.0f, true);
        typePlaceholderTextures[ItemType.MISC.ordinal()] = createColoredTexture(size, size, 0.5f, 0.5f, 0.5f, 1.0f, true);
    }

    private Texture2D createColoredTexture(int w, int h, float r, float g, float b) {
        return createColoredTexture(w, h, r, g, b, 1.0f, false);
    }

    private Texture2D createColoredTexture(int w, int h, float r, float g, float b, float alpha, boolean hasBorder) {
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                boolean isBorder = hasBorder && (x == 0 || x == w-1 || y == 0 || y == h-1 || x == 1 || x == w-2 || y == 1 || y == h-2);
                float cr = isBorder ? 0.7f : r;
                float cg = isBorder ? 0.7f : g;
                float cb = isBorder ? 0.75f : b;
                int ir = (int)(cr * 255);
                int ig = (int)(cg * 255);
                int ib = (int)(cb * 255);
                int ia = (int)(alpha * 255);
                pixels[y * w + x] = (ia << 24) | (ir << 16) | (ig << 8) | ib;
            }
        }
        return Texture2D.createTextureFromPixels(pixels, w, h);
    }

    public void open(Character character) {
        this.selectedCharacter = character;
        this.isOpen = true;
        this.isDragging = false;
        this.draggedItem = null;
    }

    public void close() {
        this.isOpen = false;
        this.selectedCharacter = null;
        this.isDragging = false;
        this.draggedItem = null;
    }

    public boolean isOpen() { return isOpen; }

    public void update() {
        if (!isOpen || selectedCharacter == null) return;

        // TODO: реализовать drag-and-drop когда будет нужен hit-test
    }

    /** Отрисовка в экранных координатах (screen space) — аналогично курсору */
    public void draw(Shader shader) {
        if (!isOpen || selectedCharacter == null) {
            Logger.info("[InventoryHUD] draw() skipped: isOpen=" + isOpen + ", selected=" + (selectedCharacter != null));
            return;
        }

        Logger.info("[InventoryHUD] DRAWING inventory!");

        // ТЕСТ: рисуем в фиксированной позиции экрана (центр)
        float inventoryX = Window.getInstance().getWidth() / 2.0f - 150.0f;
        float inventoryY = Window.getInstance().getHeight() / 2.0f - 100.0f;
        Logger.info("[InventoryHUD] Drawing at fixed screen pos: (" + inventoryX + "," + inventoryY + ")");
        
        // Размеры в пикселях
        float slotSize = 40.0f;    // размер ячейки в пикселях
        float slotGap = 5.0f;      // отступ между ячейками
        float padding = 8.0f;      // отступ внутри панели
        float panelsGap = 15.0f;   // промежуток между панелями

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        // Левая панель — экипировка
        drawEquipmentSlotsScreen(shader, inventoryX, inventoryY, slotSize, slotGap, padding, panelsGap);

        // Правая панель — рюкзак
        drawBackpackGridScreen(shader, inventoryX, inventoryY, slotSize, slotGap, padding, panelsGap);

        // Перетаскиваемый предмет
        drawDraggedItemScreen(shader, inventoryX, inventoryY, slotSize, slotGap, padding, panelsGap);

        glDisable(GL_BLEND);
        Logger.info("[InventoryHUD] Draw complete");
    }
    
    /** Конвертировать мировые координаты в экранные */
    private Vector2f worldToScreen(Vector3f worldPos) {
        // Вычисляем viewport
        float width = Window.getInstance().getWidth();
        float height = Window.getInstance().getHeight();
        
        // Преобразуем мировые координаты в экранные
        // Используем упрощённую проекцию
        Vector3f cameraPos = Camera.getInstance().getPos();
        Vector3f cameraFront = Camera.getInstance().getFront();
        
        // Вектор от камеры к объекту
        Vector3f toObject = new Vector3f(worldPos.getX() - cameraPos.getX(),
                                         worldPos.getY() - cameraPos.getY(),
                                         worldPos.getZ() - cameraPos.getZ());
        
        // Проецируем на плоскость камеры
        float depth = -toObject.dot(cameraFront);
        if (depth <= 0.01f) depth = 0.01f;
        
        // Экранная позиция (упрощённо)
        float fov = 45.0f; // FOV камеры
        float aspect = width / height;
        float scale = (float)(height / (2.0 * Math.tan(Math.toRadians(fov / 2.0))));
        
        // Векторы камеры
        Vector3f worldUp = new Vector3f(0.0f, 1.0f, 0.0f);
        Vector3f right = new Vector3f(cameraFront).cross(worldUp).normalize();
        Vector3f up = new Vector3f(right).cross(cameraFront).normalize();
        
        float screenX = width / 2.0f + toObject.dot(right) * scale / depth;
        float screenY = height / 2.0f + toObject.dot(up) * scale / depth;
        
        // НЕ инвертируем Y — ортографическая проекция камеры уже настроена с Y=0 сверху
        // (top=0, bottom=height в Camera.setOrtho())
        
        return new Vector2f(screenX, screenY);
    }

    private void drawEquipmentSlotsScreen(Shader shader, float startX, float startY, float slotSize, float slotGap, float padding, float panelsGap) {
        Equipment equipment = selectedCharacter.getEquipment();
        EquipmentSlot[] slots = EquipmentSlot.values();
        int slotCount = slots.length;
        
        float panelWidth = slotSize + padding * 2;
        float panelHeight = slotCount * slotSize + (slotCount - 1) * slotGap + padding * 2;
        
        float x = startX - panelWidth - panelsGap;
        float y = startY;
        
        // Фон панели (более тёмный)
        drawQuadScreen(shader, x, y, panelWidth, panelHeight, createColoredTexture(8, 8, 0.05f, 0.05f, 0.1f, 0.95f, false), false);
        
        // Слоты экипировки (снизу вверх)
        float slotStartX = x + padding;
        float slotStartY = y + padding;
        for (int i = 0; i < slotCount; i++) {
            EquipmentSlot slot = slots[i];
            float slotY = slotStartY + i * (slotSize + slotGap);
            
            Item item = equipment.getItem(slot);
            Texture2D tex = getTextureForItem(item);
            drawQuadScreen(shader, slotStartX, slotY, slotSize, slotSize, tex, false);
        }
    }

    private void drawBackpackGridScreen(Shader shader, float startX, float startY, float slotSize, float slotGap, float padding, float panelsGap) {
        Backpack backpack = selectedCharacter.getBackpack();
        
        float panelSize = Backpack.GRID_SIZE * slotSize + (Backpack.GRID_SIZE - 1) * slotGap + padding * 2;
        
        // Фон панели (более тёмный)
        drawQuadScreen(shader, startX, startY, panelSize, panelSize, createColoredTexture(8, 8, 0.05f, 0.05f, 0.1f, 0.95f, false), false);
        
        // Ячейки рюкзака (снизу вверх, слева направо)
        float slotStartX = startX + padding;
        float slotStartY = startY + padding;
        for (int by = 0; by < Backpack.GRID_SIZE; by++) {
            for (int bx = 0; bx < Backpack.GRID_SIZE; bx++) {
                float x = slotStartX + bx * (slotSize + slotGap);
                float y = slotStartY + by * (slotSize + slotGap);
                
                Item item = backpack.getItem(bx, by);
                Texture2D tex = getTextureForItem(item);
                drawQuadScreen(shader, x, y, slotSize, slotSize, tex, false);
            }
        }
    }

    private void drawDraggedItemScreen(Shader shader, float startX, float startY, float slotSize, float slotGap, float padding, float panelsGap) {
        if (!isDragging || draggedItem == null) return;
        // Рисуем перетаскиваемый предмет у курсора
        Vector2f mousePos = Input.getInstance().getCursorPosition();
        drawQuadScreen(shader, mousePos.getX(), mousePos.getY(), slotSize, slotSize, getTextureForItem(draggedItem), false);
    }

    private void drawQuadScreen(Shader shader, float x, float y, float w, float h, Texture2D texture, boolean isHovered) {
        // В экранных координатах Y инвертирован (0 сверху)
        float[] positions = new float[]{
                x,       y + h, 0.0f,  // top-left
                x,       y,     0.0f,  // bottom-left
                x + w,   y,     0.0f,  // bottom-right
                x + w,   y + h, 0.0f   // top-right
        };
        float[] texCoords = new float[]{0,1, 0,0, 1,0, 1,1};
        int[] indices = new int[]{0,1,2, 0,2,3};

        slotVbo.allocate(positions, null, texCoords, null, null, null, null, null, indices);

        // Используем HUD шейдер с ортографической проекцией
        hudShader.useProgram();
        hudShader.setUniformBlock("matrices", 0);
        
        Projection proj = new Projection();
        proj.getModelMatrix().identity();
        hudShader.setUniform("model_m", proj.getModelMatrix());
        hudShader.setUniform("gamma", 2.2f);
        hudShader.setUniform("onTarget", isHovered ? 1 : 0);

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texture.getTextureID());
        hudShader.setUniform("image", 0);

        slotVbo.draw();
    }

    private Texture2D getTextureForItem(Item item) {
        if (item == null) return emptySlotTexture;
        int texIdx = item.getIconTextureId();
        if (texIdx > 0) return emptySlotTexture; // TODO: реальная текстура
        Texture2D placeholder = typePlaceholderTextures[item.getType().ordinal()];
        return placeholder != null ? placeholder : emptySlotTexture;
    }

    Character getSelectedCharacter() { return selectedCharacter; }

    // ====== Вспомогательные классы ======

    private enum TargetType { EQUIPMENT, BACKPACK }

    private static class DropTarget {
        TargetType type;
        EquipmentSlot slot;
        Equipment equipment;
        Backpack backpack;
        int x, y;

        DropTarget(TargetType type, EquipmentSlot slot, Equipment equipment, Backpack backpack, int x, int y) {
            this.type = type;
            this.slot = slot;
            this.equipment = equipment;
            this.backpack = backpack;
            this.x = x;
            this.y = y;
        }

        boolean containsItem() {
            if (type == TargetType.EQUIPMENT) {
                return equipment.getItem(slot) != null;
            } else {
                return backpack.getItem(x, y) != null;
            }
        }
    }
}
