package ru.phoenix.game.datafile;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class TimeElement implements Externalizable {

    private static final long serialVersionUID = 1L;

    private int day;
    private int hour;
    private int minut;
    private float second;

    public TimeElement(){
        day = 0;
        hour = 0;
        minut = 0;
        second = 0;
    }

    public TimeElement(int day, int hour, int minut, float second) {
        this.day = day;
        this.hour = hour;
        this.minut = minut;
        this.second = second;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinut() {
        return minut;
    }

    public void setMinut(int minut) {
        this.minut = minut;
    }

    public float getSecond() {
        return second;
    }

    public void setSecond(float second) {
        this.second = second;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(day);
        out.writeObject(hour);
        out.writeObject(minut);
        out.writeObject(second);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        day = (int)in.readObject();
        hour = (int)in.readObject();
        minut = (int)in.readObject();
        second = (float)in.readObject();
    }

    @Override
    public String toString(){
        return day + " " + hour + " " + minut + " " + second;
    }
}
