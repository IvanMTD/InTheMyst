package ru.phoenix.game.content.stage.mainmenu;

import ru.phoenix.core.shader.Shader;
import ru.phoenix.game.content.stage.StudyArea;
import ru.phoenix.game.datafile.SaveData;
import ru.phoenix.game.logic.generator.Generator;

import java.io.*;

public class BackgroundArea {
    private Shader shader3D;
    private Shader shaderSprite;

    private StudyArea studyArea;

    public BackgroundArea() throws IOException, ClassNotFoundException {
        SaveData saveData = new SaveData();
        File fileDirect = new File("./data/save/data");
        File saveFile = new File(fileDirect,"backgroundArea.ser");
        if(saveFile.exists()){
            FileInputStream fileInputStream = new FileInputStream("./data/save/data/backgroundArea.ser");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            saveData = (SaveData)objectInputStream.readObject();
            objectInputStream.close();
            studyArea = Generator.loadPrepareArena(saveData);
        }else {
            FileOutputStream fileOutputStream = new FileOutputStream("./data/save/data/backgroundArea.ser");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            studyArea = Generator.getPreparedArea(25.0f, 50, 50,saveData);
            objectOutputStream.writeObject(saveData);
            objectOutputStream.close();
        }
    }

    public void init(){
        shader3D = getInit("game_object.glsl");
        shaderSprite = getInit("sprite.glsl");
    }

    private Shader getInit(String name){
        Shader shader = new Shader();
        shader.createVertexShader("VS_" + name);
        shader.createGeometryShader("GS_" + name);
        shader.createFragmentShader("FS_" + name);
        shader.createProgram();
        return shader;
    }

    public void draw(){
        shader3D.setUniform("currentHeight",25.0f);
        shader3D.setUniform("useMap",0);
        studyArea.draw(shader3D);

        shaderSprite.useProgram();
        shaderSprite.setUniformBlock("matrices", 0);
        shaderSprite.setUniform("w",studyArea.getMapX());
        shaderSprite.setUniform("h",studyArea.getMapZ());
        shaderSprite.setUniform("useMap",0);
        studyArea.drawSprites(shaderSprite);
        studyArea.drawWater(shaderSprite);
    }

    public void draw(Shader shader){
        studyArea.draw(shader);
        studyArea.drawShadowSprites(shader);
    }

    public void draw(Shader shader, boolean isShadow){
        if(isShadow) {
            studyArea.draw(shader);
            studyArea.drawShadowSprites(shader);
            studyArea.drawShadowPersons(shader, true);
        }else{
            studyArea.draw(shader);
        }
    }

    public Shader getShader3D() {
        return shader3D;
    }

    public StudyArea getStudyArea() {
        return studyArea;
    }
}
