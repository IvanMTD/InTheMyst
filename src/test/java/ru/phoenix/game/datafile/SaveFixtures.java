package ru.phoenix.game.datafile;

import ru.phoenix.core.math.Matrix4f;
import ru.phoenix.core.math.Vector2f;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.game.property.Characteristic;

import java.util.ArrayList;
import java.util.List;

/**
 * Тестовые данные для слоя сохранений: небольшой, но полноценный SaveGame.
 */
final class SaveFixtures {

    private SaveFixtures() {
    }

    static SaveGame sampleGame() {
        SaveGame game = new SaveGame();
        game.setTime(new TimeElement(3, 4, 5, 6.5f));
        game.setCurrentBiom(25.0f);
        game.setCurrentKm(1);
        game.setBioms(new float[]{10.0f, 20.0f, 25.0f});

        PersonStruct hero = person(0x51111, 5, 42, 50, 37, 9.5f);
        PersonStruct archer = person(0x51112, 2, 7, 30, 30, 10.0f);
        game.setPersonStructs(new ArrayList<>(List.of(hero, archer)));

        game.setCampLocation(sampleWorld());
        return game;
    }

    static SaveData sampleWorld() {
        SaveData world = new SaveData();
        world.setBiom(25.0f);

        StructData[][] grid = new StructData[2][2];
        for (int x = 0; x < 2; x++) {
            for (int z = 0; z < 2; z++) {
                grid[x][z] = new StructData(x + z, 1.0f, new Vector3f(x, 1.0f, z), x == 1 && z == 1);
            }
        }
        world.setStructData(grid);

        SaveElement tree = new SaveElement();
        tree.setElementPos(new Vector3f(1.0f, 2.0f, 3.0f));
        tree.setBlockedPos(List.of(new Vector2f(0.0f, 0.0f), new Vector2f(1.0f, 0.0f)));
        tree.setType(7);
        tree.setTextureNum(2);
        tree.setCurrentHeight(1.5f);
        tree.setObjectWidth(0.5f);
        tree.setObjectHeight(0.6f);
        Matrix4f grass = new Matrix4f();
        grass.set(0, 3, 5.0f);
        grass.set(3, 3, 1.0f);
        tree.setGrassMatrix(new Matrix4f[]{grass});
        tree.setStoneMatrix(new Matrix4f[0]);

        world.setTreeElements(new ArrayList<>(List.of(tree)));
        world.setPlantElements(new ArrayList<>());
        world.setStoneElement(new ArrayList<>());
        world.setvList(new ArrayList<>(List.of(new Vector3f(9.0f, 8.0f, 7.0f))));
        return world;
    }

    static PersonStruct person(int type, int level, int experience, int totalHealth, int health, float tempVision) {
        Characteristic characteristic = new Characteristic();
        characteristic.setLevel(level);
        characteristic.setExperience(experience);
        characteristic.setTotalHealth(totalHealth);
        characteristic.setHealth(health);
        characteristic.setTempVision(tempVision);

        PersonStruct person = new PersonStruct();
        person.setType(type);
        person.setCharacteristic(characteristic);
        return person;
    }
}
