package ru.phoenix.game.datafile;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class SaveGame implements Externalizable {

    private static final long serialVersionUID = 1L;

    private SaveData campLocation;

    public SaveGame(){
        campLocation = new SaveData();
    }

    public SaveData getCampLocation() {
        return campLocation;
    }

    public void setCampLocation(SaveData campLocation) {
        this.campLocation = campLocation;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(campLocation);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        campLocation = (SaveData)in.readObject();
    }
}
