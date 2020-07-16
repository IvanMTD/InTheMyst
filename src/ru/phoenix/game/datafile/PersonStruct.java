package ru.phoenix.game.datafile;

import ru.phoenix.game.property.Characteristic;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class PersonStruct implements Externalizable {

    private static final long serialVersionUID = 1L;

    private int type;
    private Characteristic characteristic;

    public PersonStruct(){
        type = 0;
        characteristic = new Characteristic();
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Characteristic getCharacteristic() {
        return characteristic;
    }

    public void setCharacteristic(Characteristic characteristic) {
        this.characteristic = characteristic;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(type);
        characteristic.writeExternal(out);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        setType((int)in.readObject());
        characteristic.readExternal(in);
    }
}
