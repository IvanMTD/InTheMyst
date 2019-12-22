package ru.phoenix.game.logic.generator.components;

import ru.phoenix.core.buffer.vbo.MeshConfig;
import ru.phoenix.core.buffer.vbo.VertexBufferObject;
import ru.phoenix.core.config.Default;
import ru.phoenix.core.kernel.Camera;
import ru.phoenix.core.kernel.CoreEngine;
import ru.phoenix.core.loader.texture.Texture;
import ru.phoenix.core.loader.texture.Texture2D;
import ru.phoenix.core.math.Projection;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.core.shader.Shader;
import ru.phoenix.game.logic.element.grid.Cell;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL21.GL_SRGB_ALPHA;
import static ru.phoenix.core.config.Constants.GROUP_A;

public class Reservoir {
    private String texturePath;
    private VertexBufferObject vbo;
    private Texture texture;
    List<TextureCoordinates>textureCoordinates;
    List<TextureCoordinates>totalTexCoords;

    private int row;
    private int column;

    private float counter;
    private float lastCount;

    private int textureIndex;

    public Reservoir(String texturePath, int row, int column){
        this.row = row;
        this.column = column;
        this.texturePath = texturePath;
        this.texture = new Texture2D();
        textureCoordinates = new ArrayList<>();
        totalTexCoords = new ArrayList<>();
        vbo = new MeshConfig(GL_DYNAMIC_DRAW,GL_TRIANGLES);
        textureIndex = 0;
    }

    public void init(Cell[][] grid) {

        this.texture.setup(null, texturePath, GL_SRGB_ALPHA, GL_CLAMP_TO_EDGE);

        int totalWidth = texture.getWidth();
        int totalHeight = texture.getHeight();

        float pictureWidth = (float)totalWidth / (float)row;
        float pictureHeight = (float)totalHeight / (float)column;

        float baseX = pictureWidth * 1.0f / totalWidth;
        float baseY = pictureHeight * 1.0f / totalHeight;

        for(int c = 1; c <= column; c++){
            for(int r = 1; r <= row; r++){
                TextureCoordinates texCoord = new TextureCoordinates();
                float x = (pictureWidth * r) * 1.0f / totalWidth;
                float y = (pictureHeight * c) * 1.0f / totalHeight;

                float[] tex = new float[]{
                        x - baseX,  y - baseY,
                        x - baseX,  y,
                        x,          y,
                        x,          y - baseY
                };
                texCoord.setCoord(tex);
                textureCoordinates.add(texCoord);
            }
        }

        List<Float> positions = new ArrayList<>();
        List<Float> normals = new ArrayList<>();
        List<Float> texCoords1 = new ArrayList<>();
        List<Float> texCoords2 = new ArrayList<>();
        List<Float> texCoords3 = new ArrayList<>();
        List<Float> texCoords4 = new ArrayList<>();
        List<Float> texCoords5 = new ArrayList<>();
        List<Float> texCoords6 = new ArrayList<>();
        List<Float> texCoords7 = new ArrayList<>();
        List<Float> texCoords8 = new ArrayList<>();
        List<Float> texCoords9 = new ArrayList<>();
        List<Float> texCoords10 = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();
        int index = 0;

        for (int x = 0; x < grid.length; x++) {
            for (int z = 0; z < grid[0].length; z++) {
                if (grid[x][z].getPosition().getY() < 0) {
                    Vector3f newPos = new Vector3f(x, -0.75f, z);
                    Vector3f p1 = new Vector3f(newPos.getX() - 0.5f, newPos.getY(),newPos.getZ() + 0.5f);
                    Vector3f p2 = new Vector3f(newPos.getX() - 0.5f, newPos.getY(),newPos.getZ() - 0.5f);
                    Vector3f p3 = new Vector3f(newPos.getX() + 0.5f, newPos.getY(),newPos.getZ() - 0.5f);
                    Vector3f p4 = new Vector3f(newPos.getX() + 0.5f, newPos.getY(),newPos.getZ() + 0.5f);
                    positions.addAll(Arrays.asList(p1.getX(),p1.getY(),p1.getZ()));
                    positions.addAll(Arrays.asList(p2.getX(),p2.getY(),p2.getZ()));
                    positions.addAll(Arrays.asList(p3.getX(),p3.getY(),p3.getZ()));
                    positions.addAll(Arrays.asList(p4.getX(),p4.getY(),p4.getZ()));
                    normals.addAll(Arrays.asList(0.0f,1.0f,0.0f));
                    normals.addAll(Arrays.asList(0.0f,1.0f,0.0f));
                    normals.addAll(Arrays.asList(0.0f,1.0f,0.0f));
                    normals.addAll(Arrays.asList(0.0f,1.0f,0.0f));
                    for(int i=0; i<textureCoordinates.get(0).getCoord().length; i++){
                        texCoords1.add(textureCoordinates.get(0).getCoord()[i]);
                        texCoords2.add(textureCoordinates.get(1).getCoord()[i]);
                        texCoords3.add(textureCoordinates.get(2).getCoord()[i]);
                        texCoords4.add(textureCoordinates.get(3).getCoord()[i]);
                        texCoords5.add(textureCoordinates.get(4).getCoord()[i]);
                        texCoords6.add(textureCoordinates.get(5).getCoord()[i]);
                        texCoords7.add(textureCoordinates.get(6).getCoord()[i]);
                        texCoords8.add(textureCoordinates.get(7).getCoord()[i]);
                        texCoords9.add(textureCoordinates.get(8).getCoord()[i]);
                        texCoords10.add(textureCoordinates.get(9).getCoord()[i]);
                    }
                    indices.add(index++);
                    indices.add(index++);
                    indices.add(index);
                    index -= 2;
                    indices.add(index);
                    index += 2;
                    indices.add(index++);
                    indices.add(index++);

                    if (grid[x][z].getPosition().getY() < -0.5f) {
                        grid[x][z].setWater(true);
                    }
                }
            }
        }

        for(int x=0; x<grid.length; x++){
            for(int z=0; z<grid[0].length; z++){
                if (grid[x][z].getPosition().getY() < 0) {
                    Vector3f p1;
                    Vector3f p2;
                    Vector3f p3;
                    Vector3f p4;
                    if (x == 0) { // left
                        for (float someY = -1.25f; someY >= -4.25f; someY -= 1.0f) {
                            Vector3f p = new Vector3f((float) x - 0.48f, someY, z);
                            p1 = new Vector3f(p.getX(), p.getY() + 0.5f, p.getZ() + 0.5f);
                            p2 = new Vector3f(p.getX(), p.getY() - 0.5f, p.getZ() + 0.5f);
                            p3 = new Vector3f(p.getX(), p.getY() - 0.5f, p.getZ() - 0.5f);
                            p4 = new Vector3f(p.getX(), p.getY() + 0.5f, p.getZ() - 0.5f);
                            positions.addAll(Arrays.asList(p1.getX(), p1.getY(), p1.getZ()));
                            positions.addAll(Arrays.asList(p2.getX(), p2.getY(), p2.getZ()));
                            positions.addAll(Arrays.asList(p3.getX(), p3.getY(), p3.getZ()));
                            positions.addAll(Arrays.asList(p4.getX(), p4.getY(), p4.getZ()));
                            normals.addAll(Arrays.asList(1.0f, 0.0f, 0.0f));
                            normals.addAll(Arrays.asList(1.0f, 0.0f, 0.0f));
                            normals.addAll(Arrays.asList(1.0f, 0.0f, 0.0f));
                            normals.addAll(Arrays.asList(1.0f, 0.0f, 0.0f));
                            for(int i=0; i<textureCoordinates.get(0).getCoord().length; i++){
                                texCoords1.add(textureCoordinates.get(0).getCoord()[i]);
                                texCoords2.add(textureCoordinates.get(1).getCoord()[i]);
                                texCoords3.add(textureCoordinates.get(2).getCoord()[i]);
                                texCoords4.add(textureCoordinates.get(3).getCoord()[i]);
                                texCoords5.add(textureCoordinates.get(4).getCoord()[i]);
                                texCoords6.add(textureCoordinates.get(5).getCoord()[i]);
                                texCoords7.add(textureCoordinates.get(6).getCoord()[i]);
                                texCoords8.add(textureCoordinates.get(7).getCoord()[i]);
                                texCoords9.add(textureCoordinates.get(8).getCoord()[i]);
                                texCoords10.add(textureCoordinates.get(9).getCoord()[i]);
                            }
                            indices.add(index++);
                            indices.add(index++);
                            indices.add(index);
                            index -= 2;
                            indices.add(index);
                            index += 2;
                            indices.add(index++);
                            indices.add(index++);
                        }
                    }
                    if (x == grid.length - 1) { // right
                        for (float someY = -1.25f; someY >= -4.25f; someY -= 1.0f) {
                            Vector3f p = new Vector3f((float) x + 0.48f, someY, z);
                            p1 = new Vector3f(p.getX(), p.getY() + 0.5f, p.getZ() - 0.5f);
                            p2 = new Vector3f(p.getX(), p.getY() - 0.5f, p.getZ() - 0.5f);
                            p3 = new Vector3f(p.getX(), p.getY() - 0.5f, p.getZ() + 0.5f);
                            p4 = new Vector3f(p.getX(), p.getY() + 0.5f, p.getZ() + 0.5f);
                            positions.addAll(Arrays.asList(p1.getX(), p1.getY(), p1.getZ()));
                            positions.addAll(Arrays.asList(p2.getX(), p2.getY(), p2.getZ()));
                            positions.addAll(Arrays.asList(p3.getX(), p3.getY(), p3.getZ()));
                            positions.addAll(Arrays.asList(p4.getX(), p4.getY(), p4.getZ()));
                            normals.addAll(Arrays.asList(1.0f, 0.0f, 0.0f));
                            normals.addAll(Arrays.asList(1.0f, 0.0f, 0.0f));
                            normals.addAll(Arrays.asList(1.0f, 0.0f, 0.0f));
                            normals.addAll(Arrays.asList(1.0f, 0.0f, 0.0f));
                            for(int i=0; i<textureCoordinates.get(0).getCoord().length; i++){
                                texCoords1.add(textureCoordinates.get(0).getCoord()[i]);
                                texCoords2.add(textureCoordinates.get(1).getCoord()[i]);
                                texCoords3.add(textureCoordinates.get(2).getCoord()[i]);
                                texCoords4.add(textureCoordinates.get(3).getCoord()[i]);
                                texCoords5.add(textureCoordinates.get(4).getCoord()[i]);
                                texCoords6.add(textureCoordinates.get(5).getCoord()[i]);
                                texCoords7.add(textureCoordinates.get(6).getCoord()[i]);
                                texCoords8.add(textureCoordinates.get(7).getCoord()[i]);
                                texCoords9.add(textureCoordinates.get(8).getCoord()[i]);
                                texCoords10.add(textureCoordinates.get(9).getCoord()[i]);
                            }
                            indices.add(index++);
                            indices.add(index++);
                            indices.add(index);
                            index -= 2;
                            indices.add(index);
                            index += 2;
                            indices.add(index++);
                            indices.add(index++);
                        }
                    }
                    if (z == 0.0f) { // down
                        for (float someY = -1.25f; someY >= -4.25f; someY -= 1.0f) {
                            Vector3f p = new Vector3f(x, someY, (float) z - 0.48f);
                            p1 = new Vector3f(p.getX() - 0.5f, p.getY() + 0.5f, p.getZ());
                            p2 = new Vector3f(p.getX() - 0.5f, p.getY() - 0.5f, p.getZ());
                            p3 = new Vector3f(p.getX() + 0.5f, p.getY() - 0.5f, p.getZ());
                            p4 = new Vector3f(p.getX() + 0.5f, p.getY() + 0.5f, p.getZ());
                            positions.addAll(Arrays.asList(p1.getX(), p1.getY(), p1.getZ()));
                            positions.addAll(Arrays.asList(p2.getX(), p2.getY(), p2.getZ()));
                            positions.addAll(Arrays.asList(p3.getX(), p3.getY(), p3.getZ()));
                            positions.addAll(Arrays.asList(p4.getX(), p4.getY(), p4.getZ()));
                            normals.addAll(Arrays.asList(0.0f, 0.0f, 1.0f));
                            normals.addAll(Arrays.asList(0.0f, 0.0f, 1.0f));
                            normals.addAll(Arrays.asList(0.0f, 0.0f, 1.0f));
                            normals.addAll(Arrays.asList(0.0f, 0.0f, 1.0f));
                            for(int i=0; i<textureCoordinates.get(0).getCoord().length; i++){
                                texCoords1.add(textureCoordinates.get(0).getCoord()[i]);
                                texCoords2.add(textureCoordinates.get(1).getCoord()[i]);
                                texCoords3.add(textureCoordinates.get(2).getCoord()[i]);
                                texCoords4.add(textureCoordinates.get(3).getCoord()[i]);
                                texCoords5.add(textureCoordinates.get(4).getCoord()[i]);
                                texCoords6.add(textureCoordinates.get(5).getCoord()[i]);
                                texCoords7.add(textureCoordinates.get(6).getCoord()[i]);
                                texCoords8.add(textureCoordinates.get(7).getCoord()[i]);
                                texCoords9.add(textureCoordinates.get(8).getCoord()[i]);
                                texCoords10.add(textureCoordinates.get(9).getCoord()[i]);
                            }
                            indices.add(index++);
                            indices.add(index++);
                            indices.add(index);
                            index -= 2;
                            indices.add(index);
                            index += 2;
                            indices.add(index++);
                            indices.add(index++);
                        }
                    }
                    if (z == grid[0].length - 1) { // up
                        for (float someY = -1.25f; someY >= -4.25f; someY -= 1.0f) {
                            Vector3f p = new Vector3f(x, someY, (float) z + 0.48f);
                            p1 = new Vector3f(p.getX() + 0.5f, p.getY() + 0.5f, p.getZ());
                            p2 = new Vector3f(p.getX() + 0.5f, p.getY() - 0.5f, p.getZ());
                            p3 = new Vector3f(p.getX() - 0.5f, p.getY() - 0.5f, p.getZ());
                            p4 = new Vector3f(p.getX() - 0.5f, p.getY() + 0.5f, p.getZ());
                            positions.addAll(Arrays.asList(p1.getX(), p1.getY(), p1.getZ()));
                            positions.addAll(Arrays.asList(p2.getX(), p2.getY(), p2.getZ()));
                            positions.addAll(Arrays.asList(p3.getX(), p3.getY(), p3.getZ()));
                            positions.addAll(Arrays.asList(p4.getX(), p4.getY(), p4.getZ()));
                            normals.addAll(Arrays.asList(0.0f, 0.0f, 1.0f));
                            normals.addAll(Arrays.asList(0.0f, 0.0f, 1.0f));
                            normals.addAll(Arrays.asList(0.0f, 0.0f, 1.0f));
                            normals.addAll(Arrays.asList(0.0f, 0.0f, 1.0f));
                            for(int i=0; i<textureCoordinates.get(0).getCoord().length; i++){
                                texCoords1.add(textureCoordinates.get(0).getCoord()[i]);
                                texCoords2.add(textureCoordinates.get(1).getCoord()[i]);
                                texCoords3.add(textureCoordinates.get(2).getCoord()[i]);
                                texCoords4.add(textureCoordinates.get(3).getCoord()[i]);
                                texCoords5.add(textureCoordinates.get(4).getCoord()[i]);
                                texCoords6.add(textureCoordinates.get(5).getCoord()[i]);
                                texCoords7.add(textureCoordinates.get(6).getCoord()[i]);
                                texCoords8.add(textureCoordinates.get(7).getCoord()[i]);
                                texCoords9.add(textureCoordinates.get(8).getCoord()[i]);
                                texCoords10.add(textureCoordinates.get(9).getCoord()[i]);
                            }
                            indices.add(index++);
                            indices.add(index++);
                            indices.add(index);
                            index -= 2;
                            indices.add(index);
                            index += 2;
                            indices.add(index++);
                            indices.add(index++);
                        }
                    }
                }
            }
        }

        TextureCoordinates tex = new TextureCoordinates();
        tex.setCoord(getTexCoords(texCoords1));
        totalTexCoords.add(tex);
        tex = new TextureCoordinates();
        tex.setCoord(getTexCoords(texCoords2));
        totalTexCoords.add(tex);
        tex = new TextureCoordinates();
        tex.setCoord(getTexCoords(texCoords3));
        totalTexCoords.add(tex);
        tex = new TextureCoordinates();
        tex.setCoord(getTexCoords(texCoords4));
        totalTexCoords.add(tex);
        tex = new TextureCoordinates();
        tex.setCoord(getTexCoords(texCoords5));
        totalTexCoords.add(tex);
        tex = new TextureCoordinates();
        tex.setCoord(getTexCoords(texCoords6));
        totalTexCoords.add(tex);
        tex = new TextureCoordinates();
        tex.setCoord(getTexCoords(texCoords7));
        totalTexCoords.add(tex);
        tex = new TextureCoordinates();
        tex.setCoord(getTexCoords(texCoords8));
        totalTexCoords.add(tex);
        tex = new TextureCoordinates();
        tex.setCoord(getTexCoords(texCoords9));
        totalTexCoords.add(tex);
        tex = new TextureCoordinates();
        tex.setCoord(getTexCoords(texCoords10));
        totalTexCoords.add(tex);

        vbo.allocate(getPositions(positions),getNormals(normals),getTexCoords(texCoords1),null,null,null,null,null,getIndices(indices));
    }

    private float[] getPositions(List<Float> poses){
        float[] positions = new float[poses.size()];
        int index = 0;
        for (Float pos : poses) {
            positions[index] = pos;
            index++;
        }
        return positions;
    }

    private float[] getNormals(List<Float> norms){
        float[] normals = new float[norms.size()];
        int index = 0;
        for (Float norm : norms) {
            normals[index] = norm;
            index++;
        }
        return normals;
    }

    private float[] getTexCoords(List<Float> texCoords){
        float[]tex = new float[texCoords.size()];

        int index = 0;
        for(Float t : texCoords){
            tex[index] = t;
            index++;
        }

        return tex;
    }

    private int[] getIndices(List<Integer> ids){
        int[] indices = new int[ids.size()];
        int index = 0;
        for (Integer id : ids) {
            indices[index] = id;
            index++;
        }
        return indices;
    }

    public void update(){
        if (counter > 20.0f) {
            textureIndex++;
            if(textureIndex >= textureCoordinates.size()){
                textureIndex = 0;
            }

            vbo.setNewTex(totalTexCoords.get(textureIndex).getCoord());

            counter = 0.0f;
        }
        float tik = (float) glfwGetTime() - lastCount;
        counter += (tik * CoreEngine.getFps());
        lastCount = (float) glfwGetTime();
    }

    public void draw(Shader shader){
        setUniforms(shader);
        vbo.draw();
    }

    private void setUniforms(Shader shader) {
        shader.setUniform("instance", 0);
        shader.setUniform("animated", 0);
        shader.setUniform("board", 0);
        shader.setUniform("grid", 0);
        shader.setUniform("isActive", 0);
        shader.setUniform("bigTree", 0);
        shader.setUniform("viewDirect", new Vector3f(Camera.getInstance().getFront().normalize()));
        shader.setUniform("turn", 0);
        shader.setUniform("discardReverse", 0);
        shader.setUniform("discardControl", -1.0f);
        shader.setUniform("noPaint", 0);
        // доп данные
        shader.setUniform("model_m", new Projection().getModelMatrix());
        shader.setUniform("xOffset", 0.0f);
        shader.setUniform("yOffset", Default.getOffset());
        shader.setUniform("zOffset", 0.0f);
        shader.setUniform("group", GROUP_A);
        shader.setUniform("id", 0.0f);
        shader.setUniform("onTarget", 0);
        shader.setUniform("water", 1);
        // end
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texture.getTextureID());
        shader.setUniform("image", 0);
    }
}

class TextureCoordinates{
    private float[] coord;

    public float[] getCoord() {
        return coord;
    }

    public void setCoord(float[] coord) {
        this.coord = coord;
    }
}
