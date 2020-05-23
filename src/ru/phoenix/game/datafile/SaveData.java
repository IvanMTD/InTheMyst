package ru.phoenix.game.datafile;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.List;

public class SaveData implements Externalizable{

    private static final long serialVersionUID = 1L;

    private float biom;
    private int sizeX;
    private int sizeZ;
    private StructData[][] structData;
    private int eSize;
    private List<SaveElement> elements;

    public SaveData(){
        biom = 0.0f;
        sizeX = 0;
        sizeZ = 0;
        structData = new StructData[sizeX][sizeZ];
        eSize = 0;
        elements = new ArrayList<>();
    }

    public float getBiom() {
        return biom;
    }

    public void setBiom(float biom) {
        this.biom = biom;
    }

    public int getSizeX() {
        return sizeX;
    }

    public void setSizeX(int sizeX) {
        this.sizeX = sizeX;
    }

    public int getSizeZ() {
        return sizeZ;
    }

    public void setSizeZ(int sizeZ) {
        this.sizeZ = sizeZ;
    }

    public StructData[][] getStructData() {
        return structData;
    }

    public void setStructData(StructData[][] structData) {
        sizeX = structData.length;
        sizeZ = structData[0].length;
        this.structData = structData;
    }

    public int geteSize() {
        return eSize;
    }

    public void seteSize(int eSize) {
        this.eSize = eSize;
    }

    public List<SaveElement> getElements() {
        return elements;
    }

    public void setElements(List<SaveElement> elements) {
        eSize = elements.size();
        this.elements = elements;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(getBiom());
        out.writeObject(getSizeX());
        out.writeObject(getSizeZ());
        for(int x=0; x<getSizeX(); x++){
            for(int z=0; z<getSizeZ(); z++){
                getStructData()[x][z].writeExternal(out);
            }
        }
        eSize = elements.size();
        out.writeObject(eSize);
        for(SaveElement saveElement : elements){
            saveElement.writeExternal(out);
        }
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        setBiom((float)in.readObject());
        setSizeX((int)in.readObject());
        setSizeZ((int)in.readObject());
        setStructData(new StructData[getSizeX()][getSizeZ()]);
        for(int x=0; x<getSizeX(); x++){
            for(int z=0; z<getSizeZ(); z++){
                getStructData()[x][z] = new StructData();
                getStructData()[x][z].readExternal(in);
            }
        }
        seteSize((int)in.readObject());
        elements = new ArrayList<>();
        for(int i=0; i<geteSize(); i++){
            SaveElement saveElement = new SaveElement();
            saveElement.readExternal(in);
            elements.add(saveElement);
        }
    }
}
