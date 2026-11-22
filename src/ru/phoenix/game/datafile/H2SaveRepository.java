package ru.phoenix.game.datafile;

import ru.phoenix.core.db.Database;
import ru.phoenix.game.property.Characteristic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Сохранения в локальной H2: метаданные слота, персонажи и биомы -- таблицы,
 * состояние мира -- версионированный BLOB (см. {@link WorldCodec}).
 */
public final class H2SaveRepository implements SaveRepository {

    private static final String INSERT_PERSON =
            "INSERT INTO save_person(slot, ord, person_type, experience, exp_level, total_health, health, health_charge, "
                    + "total_manna, manna, manna_charge, total_stamina, stamina, stamina_charge, initiative, initiative_charge, "
                    + "total_action_point, current_action_point, physical_power, magic_power, move_points, jump_points, speed, "
                    + "final_vision, vision, temp_vision) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    @Override
    public SaveGame load(int slot) {
        Connection connection = Database.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM save_slot WHERE slot = ?")) {
            statement.setInt(1, slot);
            try (ResultSet result = statement.executeQuery()) {
                if (!result.next()) {
                    return null;
                }
                SaveGame game = new SaveGame();
                game.setTime(new TimeElement(
                        result.getInt("game_day"),
                        result.getInt("game_hour"),
                        result.getInt("game_minute"),
                        result.getFloat("game_second")));
                game.setCurrentBiom(result.getFloat("current_biom"));
                game.setCurrentKm(result.getInt("current_km"));
                game.setPersonStructs(loadPersons(connection, slot));
                game.setBioms(loadBioms(connection, slot));
                game.setCampLocation(loadWorld(connection, slot));
                return game;
            }
        } catch (SQLException e) {
            throw new SaveException("Failed to load save slot " + slot, e);
        }
    }

    @Override
    public void save(int slot, SaveGame saveGame, byte[] heightmapPng) {
        if (saveGame == null) {
            throw new SaveException("SaveGame is null");
        }
        Connection connection = Database.getInstance().getConnection();
        try {
            connection.setAutoCommit(false);
            delete(connection, slot);
            insertSlot(connection, slot, saveGame, heightmapPng);
            insertPersons(connection, slot, saveGame.getPersonStructs());
            insertBioms(connection, slot, saveGame.getBioms());
            insertWorld(connection, slot, saveGame.getCampLocation());
            connection.commit();
        } catch (SQLException | SaveException e) {
            rollback(connection);
            if (e instanceof SaveException) {
                throw (SaveException) e;
            }
            throw new SaveException("Failed to save slot " + slot, e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                // соединение всё равно закрывается при выходе из игры
            }
        }
    }

    @Override
    public byte[] loadHeightmap(int slot) {
        Connection connection = Database.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement("SELECT heightmap FROM save_slot WHERE slot = ?")) {
            statement.setInt(1, slot);
            try (ResultSet result = statement.executeQuery()) {
                if (!result.next()) {
                    return null;
                }
                return result.getBytes("heightmap");
            }
        } catch (SQLException e) {
            throw new SaveException("Failed to load heightmap of slot " + slot, e);
        }
    }

    @Override
    public boolean exists(int slot) {
        Connection connection = Database.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement("SELECT slot FROM save_slot WHERE slot = ?")) {
            statement.setInt(1, slot);
            try (ResultSet result = statement.executeQuery()) {
                return result.next();
            }
        } catch (SQLException e) {
            throw new SaveException("Failed to check save slot " + slot, e);
        }
    }

    @Override
    public void delete(int slot) {
        Connection connection = Database.getInstance().getConnection();
        try {
            delete(connection, slot);
        } catch (SQLException e) {
            throw new SaveException("Failed to delete save slot " + slot, e);
        }
    }

    private void delete(Connection connection, int slot) throws SQLException {
        // дочерние таблицы удаляются каскадом
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM save_slot WHERE slot = ?")) {
            statement.setInt(1, slot);
            statement.executeUpdate();
        }
    }

    private void insertSlot(Connection connection, int slot, SaveGame game, byte[] heightmapPng) throws SQLException {
        TimeElement time = game.getTime() == null ? new TimeElement() : game.getTime();
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO save_slot(slot, updated_at, game_day, game_hour, game_minute, game_second, current_biom, current_km, heightmap) "
                        + "VALUES (?,?,?,?,?,?,?,?,?)")) {
            statement.setInt(1, slot);
            statement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            statement.setInt(3, time.getDay());
            statement.setInt(4, time.getHour());
            statement.setInt(5, time.getMinut());
            statement.setFloat(6, time.getSecond());
            statement.setFloat(7, game.getCurrentBiom());
            statement.setInt(8, game.getCurrentKm());
            statement.setBytes(9, heightmapPng);
            statement.executeUpdate();
        }
    }

    private void insertPersons(Connection connection, int slot, List<PersonStruct> persons) throws SQLException {
        if (persons == null || persons.isEmpty()) {
            return;
        }
        try (PreparedStatement statement = connection.prepareStatement(INSERT_PERSON)) {
            int ord = 0;
            for (PersonStruct person : persons) {
                Characteristic characteristic = person.getCharacteristic();
                statement.setInt(1, slot);
                statement.setInt(2, ord++);
                statement.setInt(3, person.getType());
                statement.setInt(4, characteristic.getExperience());
                statement.setInt(5, characteristic.getLevel());
                statement.setInt(6, characteristic.getTotalHealth());
                statement.setInt(7, characteristic.getHealth());
                statement.setInt(8, characteristic.getHealthCharge());
                statement.setInt(9, characteristic.getTotalManna());
                statement.setInt(10, characteristic.getManna());
                statement.setInt(11, characteristic.getMannaCharge());
                statement.setInt(12, characteristic.getTotalStamina());
                statement.setInt(13, characteristic.getStamina());
                statement.setInt(14, characteristic.getStaminaCharge());
                statement.setInt(15, characteristic.getInitiative());
                statement.setInt(16, characteristic.getInitiativeCharge());
                statement.setInt(17, characteristic.getTotalActionPoint());
                statement.setInt(18, characteristic.getCurentActionPoint());
                statement.setInt(19, characteristic.getPhysicalPower());
                statement.setInt(20, characteristic.getMagicPower());
                statement.setInt(21, characteristic.getMove());
                statement.setInt(22, characteristic.getJump());
                statement.setInt(23, characteristic.getSpeed());
                statement.setInt(24, characteristic.getFinalVision());
                statement.setInt(25, characteristic.getVision());
                statement.setFloat(26, characteristic.getTempVision());
                statement.addBatch();
            }
            statement.executeBatch();
        }
    }

    private void insertBioms(Connection connection, int slot, float[] bioms) throws SQLException {
        if (bioms == null || bioms.length == 0) {
            return;
        }
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO save_biom(slot, biom_index, biom_value) VALUES (?,?,?)")) {
            for (int i = 0; i < bioms.length; i++) {
                statement.setInt(1, slot);
                statement.setInt(2, i);
                statement.setFloat(3, bioms[i]);
                statement.addBatch();
            }
            statement.executeBatch();
        }
    }

    private void insertWorld(Connection connection, int slot, SaveData world) throws SQLException {
        if (world == null) {
            throw new SaveException("Camp location is missing in save");
        }
        byte[] payload = WorldCodec.encode(world);
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO save_world(slot, payload) VALUES (?,?)")) {
            statement.setInt(1, slot);
            statement.setBytes(2, payload);
            statement.executeUpdate();
        }
    }

    private List<PersonStruct> loadPersons(Connection connection, int slot) throws SQLException {
        List<PersonStruct> persons = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM save_person WHERE slot = ? ORDER BY ord")) {
            statement.setInt(1, slot);
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    Characteristic characteristic = new Characteristic();
                    characteristic.setExperience(result.getInt("experience"));
                    characteristic.setLevel(result.getInt("exp_level"));
                    characteristic.setTotalHealth(result.getInt("total_health"));
                    characteristic.setHealth(result.getInt("health"));
                    characteristic.setHealthCharge(result.getInt("health_charge"));
                    characteristic.setTotalManna(result.getInt("total_manna"));
                    characteristic.setManna(result.getInt("manna"));
                    characteristic.setMannaCharge(result.getInt("manna_charge"));
                    characteristic.setTotalStamina(result.getInt("total_stamina"));
                    characteristic.setStamina(result.getInt("stamina"));
                    characteristic.setStaminaCharge(result.getInt("stamina_charge"));
                    characteristic.setInitiative(result.getInt("initiative"));
                    characteristic.setInitiativeCharge(result.getInt("initiative_charge"));
                    characteristic.setTotalActionPoint(result.getInt("total_action_point"));
                    characteristic.setCurentActionPoint(result.getInt("current_action_point"));
                    characteristic.setPhysicalPower(result.getInt("physical_power"));
                    characteristic.setMagicPower(result.getInt("magic_power"));
                    characteristic.setMove(result.getInt("move_points"));
                    characteristic.setJump(result.getInt("jump_points"));
                    characteristic.setSpeed(result.getInt("speed"));
                    characteristic.setFinalVision(result.getInt("final_vision"));
                    characteristic.setVision(result.getInt("vision"));
                    characteristic.setTempVision(result.getFloat("temp_vision"));

                    PersonStruct person = new PersonStruct();
                    person.setType(result.getInt("person_type"));
                    person.setCharacteristic(characteristic);
                    persons.add(person);
                }
            }
        }
        return persons;
    }

    private float[] loadBioms(Connection connection, int slot) throws SQLException {
        List<Float> values = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT biom_value FROM save_biom WHERE slot = ? ORDER BY biom_index")) {
            statement.setInt(1, slot);
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    values.add(result.getFloat("biom_value"));
                }
            }
        }
        float[] bioms = new float[values.size()];
        for (int i = 0; i < values.size(); i++) {
            bioms[i] = values.get(i);
        }
        return bioms;
    }

    private SaveData loadWorld(Connection connection, int slot) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT payload FROM save_world WHERE slot = ?")) {
            statement.setInt(1, slot);
            try (ResultSet result = statement.executeQuery()) {
                if (!result.next()) {
                    throw new SaveException("World state is missing for slot " + slot);
                }
                return WorldCodec.decode(result.getBytes("payload"));
            }
        }
    }

    private void rollback(Connection connection) {
        try {
            connection.rollback();
        } catch (SQLException e) {
            // соединение переводится в autocommit ниже
        }
    }
}
