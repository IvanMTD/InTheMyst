package ru.phoenix.game.datafile;

import org.junit.jupiter.api.Test;
import ru.phoenix.core.math.Vector3f;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WorldCodecTest {

    @Test
    void roundTripPreservesWorld() {
        SaveData source = SaveFixtures.sampleWorld();

        SaveData restored = WorldCodec.decode(WorldCodec.encode(source));

        assertNotNull(restored);
        assertEquals(source.getBiom(), restored.getBiom());
        assertEquals(source.getSizeX(), restored.getSizeX());
        assertEquals(source.getSizeZ(), restored.getSizeZ());

        assertEquals(source.getStructData()[0][1].getCurrentHeight(),
                restored.getStructData()[0][1].getCurrentHeight());
        assertEquals(source.getStructData()[1][1].isRoad(),
                restored.getStructData()[1][1].isRoad());

        assertEquals(1, restored.getTreeElements().size());
        SaveElement tree = restored.getTreeElements().get(0);
        assertEquals(7, tree.getType());
        assertEquals(2, tree.getTextureNum());
        assertEquals(2, tree.getBlockedPos().size());
        assertEquals(1.0f, tree.getBlockedPos().get(1).getX());
        assertEquals(0.0f, tree.getBlockedPos().get(1).getY());
        assertEquals(5.0f, tree.getGrassMatrix()[0].get(0, 3));
        assertEquals(0, tree.getStoneMatrix().length);

        assertEquals(1, restored.getvList().size());
        Vector3f vector = restored.getvList().get(0);
        assertEquals(9.0f, vector.getX());
        assertEquals(8.0f, vector.getY());
        assertEquals(7.0f, vector.getZ());
    }

    @Test
    void emptyWorldRoundTrips() {
        SaveData restored = WorldCodec.decode(WorldCodec.encode(new SaveData()));

        assertEquals(0, restored.getSizeX());
        assertEquals(0, restored.getSizeZ());
        assertTrue(restored.getTreeElements().isEmpty());
        assertTrue(restored.getPlantElements().isEmpty());
        assertTrue(restored.getStoneElement().isEmpty());
        assertTrue(restored.getvList().isEmpty());
    }

    @Test
    void decodeRejectsEmptyPayload() {
        assertThrows(SaveException.class, () -> WorldCodec.decode(new byte[0]));
        assertThrows(SaveException.class, () -> WorldCodec.decode(null));
    }

    @Test
    void decodeRejectsUnknownVersion() {
        byte[] payload = {0, 0, 0, 99};
        assertThrows(SaveException.class, () -> WorldCodec.decode(payload));
    }

    @Test
    void decodeRejectsTruncatedPayload() {
        byte[] payload = Arrays.copyOf(WorldCodec.encode(SaveFixtures.sampleWorld()), 9);
        assertThrows(SaveException.class, () -> WorldCodec.decode(payload));
    }

    @Test
    void encodeRejectsNullWorld() {
        assertThrows(SaveException.class, () -> WorldCodec.encode(null));
    }
}
