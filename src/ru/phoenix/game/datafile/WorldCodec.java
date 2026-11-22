package ru.phoenix.game.datafile;

import ru.phoenix.core.math.Matrix4f;
import ru.phoenix.core.math.Vector2f;
import ru.phoenix.core.math.Vector3f;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Компактный бинарный формат состояния мира ({@link SaveData}).
 * Заменяет Java-сериализацию: формат явный, с номером версии в начале.
 * При изменении структуры данных поднимайте {@link #FORMAT_VERSION} и добавляйте ветку чтения старой версии.
 */
public final class WorldCodec {

    public static final int FORMAT_VERSION = 1;

    private WorldCodec() {
    }

    public static byte[] encode(SaveData data) {
        if (data == null) {
            throw new SaveException("World state is null");
        }
        try (ByteArrayOutputStream buffer = new ByteArrayOutputStream();
             DataOutputStream out = new DataOutputStream(buffer)) {
            out.writeInt(FORMAT_VERSION);
            out.writeFloat(data.getBiom());

            StructData[][] grid = data.getStructData();
            int sizeX = grid == null ? 0 : grid.length;
            int sizeZ = sizeX == 0 ? 0 : grid[0].length;
            out.writeInt(sizeX);
            out.writeInt(sizeZ);
            for (int x = 0; x < sizeX; x++) {
                for (int z = 0; z < sizeZ; z++) {
                    writeStruct(out, grid[x][z]);
                }
            }

            writeElements(out, data.getTreeElements());
            writeElements(out, data.getPlantElements());
            writeElements(out, data.getStoneElement());

            List<Vector3f> vectors = data.getvList();
            out.writeInt(vectors.size());
            for (Vector3f vector : vectors) {
                out.writeFloat(vector.getX());
                out.writeFloat(vector.getY());
                out.writeFloat(vector.getZ());
            }

            out.flush();
            return buffer.toByteArray();
        } catch (IOException e) {
            throw new SaveException("Failed to encode world state", e);
        }
    }

    public static SaveData decode(byte[] payload) {
        if (payload == null || payload.length == 0) {
            throw new SaveException("World state payload is empty");
        }
        try (DataInputStream in = new DataInputStream(new ByteArrayInputStream(payload))) {
            int version = in.readInt();
            if (version != FORMAT_VERSION) {
                throw new SaveException("Unsupported world format version: " + version);
            }

            SaveData data = new SaveData();
            data.setBiom(in.readFloat());

            int sizeX = in.readInt();
            int sizeZ = in.readInt();
            if (sizeX > 0 && sizeZ > 0) {
                StructData[][] grid = new StructData[sizeX][sizeZ];
                for (int x = 0; x < sizeX; x++) {
                    for (int z = 0; z < sizeZ; z++) {
                        grid[x][z] = readStruct(in);
                    }
                }
                data.setStructData(grid);
            }

            data.setTreeElements(readElements(in));
            data.setPlantElements(readElements(in));
            data.setStoneElement(readElements(in));

            int vectorCount = in.readInt();
            List<Vector3f> vectors = new ArrayList<>(vectorCount);
            for (int i = 0; i < vectorCount; i++) {
                vectors.add(new Vector3f(in.readFloat(), in.readFloat(), in.readFloat()));
            }
            data.setvList(vectors);

            return data;
        } catch (IOException e) {
            throw new SaveException("Failed to decode world state", e);
        }
    }

    private static void writeStruct(DataOutputStream out, StructData cell) throws IOException {
        out.writeFloat(cell.getCurrentHeight());
        out.writeFloat(cell.getCurrentOriginalHeight());
        out.writeFloat(cell.getPosition().getX());
        out.writeFloat(cell.getPosition().getY());
        out.writeFloat(cell.getPosition().getZ());
        out.writeBoolean(cell.isRoad());
    }

    private static StructData readStruct(DataInputStream in) throws IOException {
        float height = in.readFloat();
        float originalHeight = in.readFloat();
        Vector3f position = new Vector3f(in.readFloat(), in.readFloat(), in.readFloat());
        boolean road = in.readBoolean();
        return new StructData(height, originalHeight, position, road);
    }

    private static void writeElements(DataOutputStream out, List<SaveElement> elements) throws IOException {
        int count = elements == null ? 0 : elements.size();
        out.writeInt(count);
        for (int i = 0; i < count; i++) {
            writeElement(out, elements.get(i));
        }
    }

    private static void writeElement(DataOutputStream out, SaveElement element) throws IOException {
        List<Vector2f> blocked = element.getBlockedPos();
        int blockedCount = blocked == null ? 0 : blocked.size();
        out.writeInt(blockedCount);
        for (int i = 0; i < blockedCount; i++) {
            out.writeFloat(blocked.get(i).getX());
            out.writeFloat(blocked.get(i).getY());
        }

        Vector3f position = element.getElementPos();
        out.writeFloat(position.getX());
        out.writeFloat(position.getY());
        out.writeFloat(position.getZ());
        out.writeInt(element.getType());
        out.writeInt(element.getTextureNum());
        out.writeFloat(element.getCurrentHeight());
        out.writeFloat(element.getObjectWidth());
        out.writeFloat(element.getObjectHeight());

        writeMatrices(out, element.getGrassMatrix());
        writeMatrices(out, element.getStoneMatrix());
    }

    private static SaveElement readElement(DataInputStream in) throws IOException {
        int blockedCount = in.readInt();
        List<Vector2f> blocked = new ArrayList<>(blockedCount);
        for (int i = 0; i < blockedCount; i++) {
            blocked.add(new Vector2f(in.readFloat(), in.readFloat()));
        }

        SaveElement element = new SaveElement();
        element.setBlockedPos(blocked);
        element.setElementPos(new Vector3f(in.readFloat(), in.readFloat(), in.readFloat()));
        element.setType(in.readInt());
        element.setTextureNum(in.readInt());
        element.setCurrentHeight(in.readFloat());
        element.setObjectWidth(in.readFloat());
        element.setObjectHeight(in.readFloat());
        element.setGrassMatrix(readMatrices(in));
        element.setStoneMatrix(readMatrices(in));
        return element;
    }

    private static List<SaveElement> readElements(DataInputStream in) throws IOException {
        int count = in.readInt();
        List<SaveElement> elements = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            elements.add(readElement(in));
        }
        return elements;
    }

    private static void writeMatrices(DataOutputStream out, Matrix4f[] matrices) throws IOException {
        int count = matrices == null ? 0 : matrices.length;
        out.writeInt(count);
        for (int i = 0; i < count; i++) {
            Matrix4f matrix = matrices[i];
            for (int row = 0; row < 4; row++) {
                for (int column = 0; column < 4; column++) {
                    out.writeFloat(matrix.get(row, column));
                }
            }
        }
    }

    private static Matrix4f[] readMatrices(DataInputStream in) throws IOException {
        int count = in.readInt();
        Matrix4f[] matrices = new Matrix4f[count];
        for (int i = 0; i < count; i++) {
            Matrix4f matrix = new Matrix4f();
            for (int row = 0; row < 4; row++) {
                for (int column = 0; column < 4; column++) {
                    matrix.set(row, column, in.readFloat());
                }
            }
            matrices[i] = matrix;
        }
        return matrices;
    }
}
