package ru.phoenix.game.hud.elements.basic;

import ru.phoenix.core.buffer.vbo.MeshConfig;
import ru.phoenix.core.buffer.vbo.VertexBufferObject;
import ru.phoenix.core.kernel.Window;
import ru.phoenix.core.loader.texture.Texture;
import ru.phoenix.core.loader.texture.Texture2D;
import ru.phoenix.core.math.Projection;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.core.shader.Shader;
import ru.phoenix.game.hud.elements.Hud;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL21.GL_SRGB_ALPHA;
import static ru.phoenix.core.config.Constants.GROUP_A;

public class BasicHudElement implements Hud {
    public static final int STANDART    = 0x100100;
    public static final int VERTICAL    = 0x100101;
    public static final int HORIZONTAL  = 0x100102;

    private Texture texture;
    private VertexBufferObject vbo;
    private Projection projection;
    private Vector3f position;

    private int group;
    private float id;

    private boolean discard;
    private boolean onTarget;
    private boolean selected;
    private boolean capture;

    public BasicHudElement(int type, String path, Vector3f position, float size, boolean discard){
        initTexture(path);
        initVbo(type,size);
        initProjection(position);

        group = GROUP_A;
        id = -1.0f;
        onTarget = false;
        selected = false;
        capture = false;
        this.discard = discard;
    }

    public BasicHudElement(int type, String path, Vector3f position, float size, boolean discard, int group, float id){
        initTexture(path);
        initVbo(type,size);
        initProjection(position);
        
        this.group = group;
        this.id = id;
        onTarget = false;
        selected = false;
        capture = false;
        this.discard = discard;
    }

    private void initTexture(String path){
        texture = new Texture2D();
        texture.setup(null,path,GL_SRGB_ALPHA,GL_CLAMP_TO_EDGE);
    }

    private void initVbo(int type, float size){
        vbo = new MeshConfig();
        float x = 0.0f;
        float y = 0.0f;

        if(type == STANDART) {
            x = (Window.getInstance().getWidth() * size / 100.0f) / 2.0f;
            y = texture.getHeight() * x / texture.getWidth();
        }else if(type == VERTICAL){
            x = (Window.getInstance().getWidth() * size / 100.0f) / 2.0f;
            y = Window.getInstance().getHeight() / 2.0f;
        }else if(type == HORIZONTAL){
            x = Window.getInstance().getWidth() / 2.0f;
            y = (Window.getInstance().getHeight() * size / 100.0f) / 2.0f;
        }

        float[] pos = new float[]{
                -x,  y, 0.0f,
                -x, -y, 0.0f,
                 x, -y, 0.0f,
                 x,  y, 0.0f
        };
        float[] tex = new float[]{
                0.0f,1.0f,
                0.0f,0.0f,
                1.0f,0.0f,
                1.0f,1.0f
        };
        int[] indices = new int[]{
                0,1,2,
                0,2,3
        };
        vbo.allocate(pos,null,tex,null,null,null,null,null,indices);
    }

    private void initProjection(Vector3f position){
        projection = new Projection();
        projection.setTranslation(position);
        this.position = position;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
        projection.getModelMatrix().identity();
        projection.setTranslation(position);
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public float getId() {
        return id;
    }

    public void setId(float id) {
        this.id = id;
    }

    @Override
    public boolean isOnTarget() {
        return onTarget;
    }

    @Override
    public void setOnTarget(boolean onTarget) {
        this.onTarget = onTarget;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isCapture() {
        return capture;
    }

    public void setCapture(boolean capture) {
        this.capture = capture;
    }

    public boolean isDiscard() {
        return discard;
    }

    public void setDiscard(boolean discard) {
        this.discard = discard;
    }

    @Override
    public void update(Vector3f pixel, boolean leftClick) {
        float pixelID = pixel.getX();
        if (pixelID == id) {
            setOnTarget(true);
            if(leftClick){
                setSelected(true);
            }else{
                setSelected(false);
            }
        }else {
            setOnTarget(false);
            setSelected(false);
        }
    }

    @Override
    public void draw(Shader shader){
        setUniforms(shader);
        vbo.draw();
    }

    private void setUniforms(Shader shader){

        projection.getModelMatrix().identity();
        projection.setTranslation(position);
        if(isOnTarget()){
            projection.setScaling(1.02f);
        }
        if(isSelected()){
            projection.setScaling(0.95f);
        }

        shader.setUniform("model_m",projection.getModelMatrix());

        shader.setUniform("tune",0);
        shader.setUniform("gamma",Window.getInstance().getGamma());
        shader.setUniform("onTarget",isOnTarget() ? 1 : 0);
        shader.setUniform("group", getGroup());
        shader.setUniform("id",getId());
        shader.setUniform("discardControl", isDiscard() ? 1 : 0);

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texture.getTextureID());
        shader.setUniform("image", 0);
    }
}
