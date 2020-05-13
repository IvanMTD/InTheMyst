package ru.phoenix.game.content.stage.mainmenu;

import ru.phoenix.core.shader.Shader;
import ru.phoenix.game.content.stage.StudyArea;
import ru.phoenix.game.logic.generator.Generator;

public class BackgroundArea {
    private Shader shader3D;
    private Shader shaderSprite;

    private StudyArea studyArea;

    public BackgroundArea(){
        studyArea = Generator.getPreparedArea(25.0f,50,50);
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

    public Shader getShader3D() {
        return shader3D;
    }

    public StudyArea getStudyArea() {
        return studyArea;
    }
}
