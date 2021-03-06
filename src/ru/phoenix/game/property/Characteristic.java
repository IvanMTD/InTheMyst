package ru.phoenix.game.property;

import java.io.*;

public class Characteristic implements Externalizable {

    private static final long serialVersionUID = 1L;

    private transient final int MAX_LEVEL     = 99;
    private transient final int NEXT_LEVEL    = 100;

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
    // Параметры обзора
    private int finalVision;
    private int vision;
    private float tempVision;

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
        setJump(2);
        setSpeed(2);
        // Обзор
        setFinalVision(10);
        setVision(getFinalVision());
        setTempVision(getVision());
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
        // Обзор
        setFinalVision(characteristic.getFinalVision());
        setVision(characteristic.getFinalVision());
        setTempVision(characteristic.getVision());
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

    // ОБЗОР
    public int getVision() {
        return vision;
    }

    public void setVision(int vision) {
        this.vision = vision;
    }

    public int getFinalVision() {
        return finalVision;
    }

    public float getTempVision() {
        return tempVision;
    }

    public void setTempVision(float tempVision) {
        this.tempVision = tempVision;
    }

    public void setFinalVision(int finalVision) {
        this.finalVision = finalVision;
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

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        // Опыт и уровень
        out.writeObject(experience);
        out.writeObject(level);
        // Здоровье
        out.writeObject(totalHealth);
        out.writeObject(health);
        out.writeObject(healthCharge);
        // Манна
        out.writeObject(totalManna);
        out.writeObject(manna);
        out.writeObject(mannaCharge);
        // Стамина
        out.writeObject(totalStamina); // Максимальная стамина
        out.writeObject(stamina); // Текущий уровень стамины
        out.writeObject(staminaCharge); // Востановление стамины
        // Инициатива
        out.writeObject(initiative); // Текущий уровень инициативы
        out.writeObject(initiativeCharge); // Скорость накопления инициативы
        // Очки действия
        out.writeObject(totalActionPoint);
        out.writeObject(curentActionPoint);
        // Характеристики
        out.writeObject(physicalPower);
        out.writeObject(magicPower);
        // Пораметры перемещения
        out.writeObject(move);
        out.writeObject(jump);
        out.writeObject(speed);
        // Параметры обзора
        out.writeObject(finalVision);
        out.writeObject(vision);
        out.writeObject(tempVision);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        setExperience((int)in.readObject());
        setLevel((int)in.readObject());
        // Здоровье
        setTotalHealth((int)in.readObject());
        setHealth((int)in.readObject());
        setHealthCharge((int)in.readObject());
        // Манна
        setTotalManna((int)in.readObject());
        setManna((int)in.readObject());
        setMannaCharge((int)in.readObject());
        // Стамина
        setTotalStamina((int)in.readObject());
        setStamina((int)in.readObject());
        setStaminaCharge((int)in.readObject());
        // Инициатива
        setInitiative((int)in.readObject());
        setInitiativeCharge((int)in.readObject());
        // Очки действия
        setTotalActionPoint((int)in.readObject());
        setCurentActionPoint((int)in.readObject());
        // Характеристики
        setPhysicalPower((int)in.readObject());
        setMagicPower((int)in.readObject());
        // Пораметры перемещения
        setMove((int)in.readObject());
        setJump((int)in.readObject());
        setSpeed((int)in.readObject());
        // Параметры обзора
        setFinalVision((int)in.readObject());
        setVision((int)in.readObject());
        setTempVision((float)in.readObject());
    }
}