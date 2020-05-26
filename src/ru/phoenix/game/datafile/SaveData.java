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
    private int teSize;
    private List<SaveElement> treeElements;
    private int peSize;
    private List<SaveElement> plantElements;

    public SaveData(){
        biom = 0.0f;
        sizeX = 0;
        sizeZ = 0;
        structData = new StructData[sizeX][sizeZ];
        teSize = 0;
        treeElements = new ArrayList<>();
        plantElements = new ArrayList<>();
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

    public int getTeSize() {
        return teSize;
    }

    public void setTeSize(int teSize) {
        this.teSize = teSize;
    }

    public List<SaveElement> getTreeElements() {
        return treeElements;
    }

    public void setTreeElements(List<SaveElement> elements) {
        teSize = elements.size();
        this.treeElements = elements;
    }

    public int getPeSize() {
        return peSize;
    }

    public void setPeSize(int peSize) {
        this.peSize = peSize;
    }

    public List<SaveElement> getPlantElements() {
        return plantElements;
    }

    public void setPlantElements(List<SaveElement> plantElements) {
        peSize = plantElements.size();
        this.plantElements = plantElements;
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
        teSize = treeElements.size();
        out.writeObject(teSize);
        for(SaveElement saveElement : treeElements){
            saveElement.writeExternal(out);
        }
        peSize = plantElements.size();
        out.writeObject(peSize);
        for(SaveElement saveElement : plantElements){
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
        setTeSize((int)in.readObject());
        treeElements = new ArrayList<>();
        for(int i=0; i<getTeSize(); i++){
            SaveElement saveElement = new SaveElement();
            saveElement.readExternal(in);
            treeElements.add(saveElement);
        }
        setPeSize((int)in.readObject());
        plantElements = new ArrayList<>();
        for(int i=0; i<getPeSize(); i++){
            SaveElement saveElement = new SaveElement();
            saveElement.readExternal(in);
            plantElements.add(saveElement);
        }
    }
}
