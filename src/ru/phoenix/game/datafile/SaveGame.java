package ru.phoenix.game.datafile;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.List;

public class SaveGame implements Externalizable {

    private static final long serialVersionUID = 1L;

    private TimeElement time;

    private SaveData campLocation;
    private int personArraySize;
    private List<PersonStruct> personStructs;

    private float currentBiom;
    private int currentKm;
    private int biomArraySize;
    private float[]bioms;

    public SaveGame(){
        time = new TimeElement();
        campLocation = new SaveData();
        personStructs = new ArrayList<>();
    }

    public TimeElement getTime() {
        return time;
    }

    public void setTime(TimeElement time) {
        this.time = time;
    }

    public SaveData getCampLocation() {
        return campLocation;
    }

    public void setCampLocation(SaveData campLocation) {
        this.campLocation = campLocation;
    }

    public List<PersonStruct> getPersonStructs() {
        return personStructs;
    }

    public void setPersonStructs(List<PersonStruct> personStructs) {
        this.personArraySize = personStructs.size();
        this.personStructs = personStructs;
    }

    public float getCurrentBiom() {
        return currentBiom;
    }

    public void setCurrentBiom(float currentBiom) {
        this.currentBiom = currentBiom;
    }

    public int getCurrentKm() {
        return currentKm;
    }

    public void setCurrentKm(int currentKm) {
        this.currentKm = currentKm;
    }

    public float[] getBioms() {
        return bioms;
    }

    public void setBioms(float[] bioms) {
        this.bioms = bioms;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        time.writeExternal(out);
        campLocation.writeExternal(out);
        out.writeObject(personStructs.size());
        for(PersonStruct personStruct : personStructs){
            personStruct.writeExternal(out);
        }
        out.writeObject(currentBiom);
        out.writeObject(currentKm);
        out.writeObject(bioms.length);
        for (float biom : bioms) {
            out.writeObject(biom);
        }
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        time = new TimeElement();
        time.readExternal(in);
        campLocation = new SaveData();
        campLocation.readExternal(in);
        personArraySize = (int)in.readObject();
        personStructs = new ArrayList<>();
        for(int i=0; i<personArraySize; i++){
            PersonStruct personStruct = new PersonStruct();
            personStruct.readExternal(in);
            personStructs.add(personStruct);
        }
        currentBiom = (float)in.readObject();
        currentKm = (int)in.readObject();
        biomArraySize = (int)in.readObject();
        bioms = new float[biomArraySize];
        for(int i=0; i<bioms.length; i++){
            bioms[i] = (float)in.readObject();
        }
    }
}
