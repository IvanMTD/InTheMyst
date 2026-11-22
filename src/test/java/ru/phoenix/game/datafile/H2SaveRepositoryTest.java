package ru.phoenix.game.datafile;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.phoenix.core.db.Database;
import ru.phoenix.core.math.Vector3f;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class H2SaveRepositoryTest {

    private SaveRepository repository;

    @BeforeEach
    void setUp() {
        String url = "jdbc:h2:mem:myst-" + UUID.randomUUID() + ";DB_CLOSE_DELAY=-1";
        Database.getInstance().init(url);
        repository = new H2SaveRepository();
    }

    @AfterEach
    void tearDown() {
        Database.getInstance().close();
    }

    @Test
    void missingSlotLoadsAsNull() {
        assertFalse(repository.exists(3));
        assertNull(repository.load(3));
        assertNull(repository.loadHeightmap(3));
    }

    @Test
    void savesAndLoadsFullGame() {
        byte[] heightmap = {1, 2, 3, 4};

        repository.save(2, SaveFixtures.sampleGame(), heightmap);

        assertTrue(repository.exists(2));
        SaveGame loaded = repository.load(2);
        assertNotNull(loaded);

        assertEquals(3, loaded.getTime().getDay());
        assertEquals(4, loaded.getTime().getHour());
        assertEquals(5, loaded.getTime().getMinut());
        assertEquals(6.5f, loaded.getTime().getSecond());
        assertEquals(25.0f, loaded.getCurrentBiom());
        assertEquals(1, loaded.getCurrentKm());
        assertArrayEquals(new float[]{10.0f, 20.0f, 25.0f}, loaded.getBioms());

        assertEquals(2, loaded.getPersonStructs().size());
        PersonStruct hero = loaded.getPersonStructs().get(0);
        assertEquals(0x51111, hero.getType());
        assertEquals(5, hero.getCharacteristic().getLevel());
        assertEquals(42, hero.getCharacteristic().getExperience());
        assertEquals(37, hero.getCharacteristic().getHealth());
        assertEquals(9.5f, hero.getCharacteristic().getTempVision());

        SaveData world = loaded.getCampLocation();
        assertNotNull(world);
        assertEquals(2, world.getSizeX());
        assertEquals(2, world.getSizeZ());
        assertTrue(world.getStructData()[1][1].isRoad());
        assertEquals(1.0f, world.getStructData()[0][0].getPosition().getY());

        assertEquals(1, world.getTreeElements().size());
        SaveElement tree = world.getTreeElements().get(0);
        assertEquals(7, tree.getType());
        assertEquals(2, tree.getBlockedPos().size());
        assertEquals(5.0f, tree.getGrassMatrix()[0].get(0, 3));
        assertEquals(1.0f, tree.getGrassMatrix()[0].get(3, 3));
        assertEquals(7.0f, world.getvList().get(0).getZ());

        assertArrayEquals(heightmap, repository.loadHeightmap(2));
    }

    @Test
    void saveOverwritesExistingSlot() {
        repository.save(4, SaveFixtures.sampleGame(), new byte[]{1});

        SaveGame second = SaveFixtures.sampleGame();
        second.setTime(new TimeElement(9, 10, 11, 12.0f));
        second.setCurrentBiom(40.0f);
        second.setBioms(new float[]{40.0f});
        repository.save(4, second, new byte[]{9, 9});

        SaveGame loaded = repository.load(4);
        assertEquals(9, loaded.getTime().getDay());
        assertEquals(40.0f, loaded.getCurrentBiom());
        assertArrayEquals(new float[]{40.0f}, loaded.getBioms());
        assertArrayEquals(new byte[]{9, 9}, repository.loadHeightmap(4));
    }

    @Test
    void saveWithoutHeightmapKeepsSaveReadable() {
        repository.save(5, SaveFixtures.sampleGame(), null);

        assertTrue(repository.exists(5));
        assertNull(repository.loadHeightmap(5));
        assertNotNull(repository.load(5));
    }

    @Test
    void deleteRemovesSlotWithChildren() {
        repository.save(1, SaveFixtures.sampleGame(), new byte[]{1, 2});
        assertTrue(repository.exists(1));

        repository.delete(1);

        assertFalse(repository.exists(1));
        assertNull(repository.load(1));
        assertNull(repository.loadHeightmap(1));
        // повторное удаление не должно падать
        repository.delete(1);
    }

    @Test
    void saveRejectsNullGame() {
        assertThrows(SaveException.class, () -> repository.save(1, null, null));
    }

    @Test
    void savedGameSurvivesNewRepositoryInstance() {
        repository.save(3, SaveFixtures.sampleGame(), null);

        SaveRepository another = new H2SaveRepository();
        SaveGame loaded = another.load(3);
        assertNotNull(loaded);
        assertEquals(3, loaded.getTime().getDay());
        assertEquals(new Vector3f(9.0f, 8.0f, 7.0f).getZ(), loaded.getCampLocation().getvList().get(0).getZ());
    }
}
