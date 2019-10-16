package ru.phoenix.game.content.object.active.property;

public class Characteristic {
    private final int MAX_LEVEL     = 99;
    private final int NEXT_LEVEL    = 100;

    // Опыт и уровень
    private int experience;
    private int level;

    // Здоровье
    private int totalHealth;
    private int health;
    private int healthCharge;
    // Манна
    private int totalManna;
    private int manna;
    private int mannaCharge;
    // Стамина
    private int totalStamina; // Максимальная стамина
    private int stamina; // Текущий уровень стамины
    private int staminaCharge; // Востановление стамины
    // Инициатива
    private int initiative; // Текущий уровень инициативы
    private int initiativeCharge; // Скорость накопления инициативы
    // Очки действия
    private int totalActionPoint;
    private int curentActionPoint;
    // Характеристики
    private int physicalPower;
    private int magicPower;
    // Пораметры перемещения
    private int move;
    private int jump;
    private int speed;

    // Конструкторы класса
    public Characteristic(){
        // Опыт и уровень
        setExperience(0);
        setLevel(1);
        // Очки действия
        setTotalActionPoint(2);
        setCurentActionPoint(getTotalActionPoint());
        // Инициатива
        setInitiative(0);
        setInitiativeCharge(15);
        // Здоровье
        setTotalHealth(30);
        setHealth(getTotalHealth());
        setHealthCharge(0);
        // Манна
        setTotalManna(30);
        setManna(getTotalManna());
        setMannaCharge(0);
        // Стамина
        setTotalStamina(100);
        setStamina(getTotalStamina());
        setStaminaCharge(15);
        // Характеристики
        setPhysicalPower(10);
        setMagicPower(10);
        // Движения
        setMove(6);
        setJump(1);
        setSpeed(2);
    }

    public Characteristic(Characteristic characteristic){
        // Опыт и уровень
        setExperience(characteristic.getExperience());
        setLevel(characteristic.getLevel());
        // Очки действия
        setTotalActionPoint(characteristic.getTotalActionPoint());
        setCurentActionPoint(getTotalActionPoint());
        // Инициатива
        setInitiative(characteristic.getInitiative());
        setInitiativeCharge(characteristic.getInitiativeCharge());
        // Здоровье
        setTotalHealth(characteristic.getTotalHealth());
        setHealth(getTotalHealth());
        setHealthCharge(characteristic.getHealthCharge());
        // Манна
        setTotalManna(characteristic.getTotalManna());
        setManna(getTotalManna());
        setMannaCharge(characteristic.getMannaCharge());
        // Стамина
        setTotalStamina(characteristic.getTotalStamina());
        setStamina(getTotalStamina());
        setStaminaCharge(characteristic.getStaminaCharge());
        // Характеристики
        setPhysicalPower(characteristic.getPhysicalPower());
        setMagicPower(characteristic.getMagicPower());
        // Движения
        setMove(characteristic.getMove());
        setJump(characteristic.getJump());
        setSpeed(characteristic.getSpeed());
    }

    // ОПЫТ И УРОВЕНЬ
    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    // ОЧКИ ДЕЙСТВИЯ
    public int getTotalActionPoint() {
        return totalActionPoint;
    }

    public void setTotalActionPoint(int totalActionPoint) {
        this.totalActionPoint = totalActionPoint;
    }

    public int getCurentActionPoint() {
        return curentActionPoint;
    }

    public void setCurentActionPoint(int curentActionPoint) {
        this.curentActionPoint = curentActionPoint;
    }

    // ИНИЦИАТИВА
    public int getInitiative() {
        return initiative;
    }

    public void setInitiative(int initiative) {
        this.initiative = initiative;
    }

    public int getInitiativeCharge() {
        return initiativeCharge;
    }

    public void setInitiativeCharge(int initiativeCharge) {
        this.initiativeCharge = initiativeCharge;
    }

    //ЗДОРОВЬЕ
    public int getTotalHealth() {
        return totalHealth;
    }

    public void setTotalHealth(int totalHealth) {
        this.totalHealth = totalHealth;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
        if(getHealth() > getTotalHealth()){
            setHealth(getTotalHealth());
        }
    }

    public int getHealthCharge() {
        return healthCharge;
    }

    public void setHealthCharge(int healthCharge) {
        this.healthCharge = healthCharge;
    }

    // МАННА
    public int getTotalManna() {
        return totalManna;
    }

    public void setTotalManna(int totalManna) {
        this.totalManna = totalManna;
    }

    public int getManna() {
        return manna;
    }

    public void setManna(int manna) {
        this.manna = manna;
        if(getManna() > getTotalManna()){
            setManna(getTotalManna());
        }
    }

    public int getMannaCharge() {
        return mannaCharge;
    }

    public void setMannaCharge(int mannaCharge) {
        this.mannaCharge = mannaCharge;
    }

    // СТАМИНА
    public int getTotalStamina() {
        return totalStamina;
    }

    public void setTotalStamina(int totalStamina) {
        this.totalStamina = totalStamina;
    }

    public int getStamina() {
        return stamina;
    }

    public void setStamina(int stamina) {
        this.stamina = stamina;
        if(getStamina() > getTotalStamina()){
            setStamina(getTotalStamina());
        }
    }

    public int getStaminaCharge() {
        return staminaCharge;
    }

    public void setStaminaCharge(int staminaCharge) {
        this.staminaCharge = staminaCharge;
    }

    // ХАРАКТЕРИСТИКИ
    public int getPhysicalPower() {
        return physicalPower;
    }

    public void setPhysicalPower(int physicalPower) {
        this.physicalPower = physicalPower;
    }

    public int getMagicPower() {
        return magicPower;
    }

    public void setMagicPower(int magicPower) {
        this.magicPower = magicPower;
    }

    //ДВИЖЕНИЕ
    public int getMove() {
        return move;
    }

    public void setMove(int move) {
        this.move = move;
    }

    public int getJump() {
        return jump;
    }

    public void setJump(int jump) {
        this.jump = jump;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    // Формулы
    public void updateIndicators(){
        setHealth(getHealth() + getHealthCharge());
        setManna(getManna() + getMannaCharge());
        setStamina(getStamina() + getStaminaCharge());
    }

    public float getMovementSpeed() {
        return speed * 0.006f;
    }
}