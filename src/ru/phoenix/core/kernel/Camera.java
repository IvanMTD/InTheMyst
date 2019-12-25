package ru.phoenix.core.kernel;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import ru.phoenix.core.math.Projection;
import ru.phoenix.core.math.Vector2f;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.game.logic.element.grid.Cell;
import ru.phoenix.game.property.GameController;

import java.nio.DoubleBuffer;

import static org.lwjgl.glfw.GLFW.*;

public class Camera {
    //constant
    private static final float MAX_DIST = 40.0f;
    private static final float MIN_DIST = 10.0f;
    private static final float SCROLL_SENS = 1.1f;

    private static Camera instance = null;

    private Projection perspective;
    private Projection ortho;

    private Vector3f pos;
    private Vector3f front;
    private Vector3f up;
    private Vector3f direction;
    private Vector2f lastCursorPos;
    private Vector2f lastCursorPos2;

    private float corW;
    private float corH;

    private float turn;
    private float yaw;
    private float pitch;
    private float fov;
    private float hypotenuse;
    private float offset;

    private boolean cameraControlLock;
    private float turnCounter;
    private boolean stopTurn;
    private boolean turnButtonBlock;

    // УПРОВЛЕНИЕ ДВИЖЕНИЕМ КАМЕРЫ
    private boolean prepare;
    private Vector3f moveDirection;
    private float moveDistance;
    private float moveOffset;
    private float totalMove;

    public static Camera getInstance(){
        if(instance == null){
            instance = new Camera();
        }
        return instance;
    }

    private Camera(){
        init();
        GLFWFramebufferSizeCallback framebufferSizeCallback = glfwSetFramebufferSizeCallback(Window.getInstance().getWindow(), (window, width, height) -> {
            if (height == 0) {
                height = 1;
            }
            Window.getInstance().setWidth(width);
            Window.getInstance().setHeight(height);
            updateProjection();
            Window.getInstance().setViewport(width, height);
        });
    }

    private void init(){
        turnCounter = 0.0f;
        stopTurn = false;
        turnButtonBlock = false;
        DoubleBuffer lx = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer ly = BufferUtils.createDoubleBuffer(1);
        glfwGetCursorPos(Window.getInstance().getWindow(),lx,ly);
        lastCursorPos = new Vector2f((float)lx.get(),(float)ly.get());
        lastCursorPos2 = null;
        prepare = true;

        corW = ((float)Window.getInstance().getWidth() / (float)Window.getInstance().getHeight()) * 0.9f;
        corH = (1.0f + ((float)Window.getInstance().getHeight() / (float)Window.getInstance().getWidth())) * 0.9f;

        turn = 0.0f;
        fov = 20.0f;
        yaw = 46.0f;
        pitch = -30.0f;
        offset = 0.0f;
        cameraControlLock = false;

        perspective = new Projection();
        ortho = new Projection();

        direction = new Vector3f();
        hypotenuse = 10.0f;
        float cathetus = (float) Math.sin(Math.toRadians(Math.abs(pitch))) * hypotenuse;


        // Установка позиции!
        pos = new Vector3f(0.0f, cathetus,-cathetus); // позиция камеры
        front = new Vector3f(0.0f,-cathetus, cathetus).normalize(); // точка в которую смотрит камера
        up = new Vector3f(0.0f,1.0f,0.0f); // вектор определяющий верх камеры

        //
        direction.setX((float) (Math.cos(Math.toRadians(pitch)) * Math.cos(Math.toRadians(yaw))));
        direction.setY((float) Math.sin(Math.toRadians(pitch)));
        direction.setZ((float) (Math.cos(Math.toRadians(pitch)) * Math.sin(Math.toRadians(yaw))));

        pos = pos.add(front.normalize().mul(hypotenuse)).add(direction.normalize().mul(-hypotenuse));
        front = direction.normalize();
        //

        setPerspective();
        setOrtho();
    }

    public void update(float minW, float maxW, float minH, float maxH, Cell[][] grid){
        updateViewMatrix();
        float movementSpeedX = 0.05f;
        float movementSpeedY = 0.088f;
        Vector2f currentCursorPosition = Input.getInstance().getCursorPosition();

        if(!isCameraControlLock()) {
            if (Input.getInstance().isPressed(GLFW_KEY_W)) {
                setPos(pos.add(new Vector3f(front.mul(movementSpeedY).getX(), 0.0f, front.mul(movementSpeedY).getZ())));
            }
            if (Input.getInstance().isPressed(GLFW_KEY_S)) {
                setPos(pos.sub(new Vector3f(front.mul(movementSpeedY).getX(), 0.0f, front.mul(movementSpeedY).getZ())));
            }
            if (Input.getInstance().isPressed(GLFW_KEY_A)) {
                setPos(pos.sub(new Vector3f(front.cross(up)).normalize().mul(movementSpeedX)));
            }
            if (Input.getInstance().isPressed(GLFW_KEY_D)) {
                setPos(pos.add(new Vector3f(front.cross(up)).normalize().mul(movementSpeedX)));
            }
            if(GameController.getInstance().isLeftHold()){
                if(lastCursorPos2 != null){
                    float corectionX = (hypotenuse / Window.getInstance().getWidth()) / corW;
                    float corectionY = (hypotenuse / Window.getInstance().getHeight()) / corH;
                    if(lastCursorPos2.getX() > currentCursorPosition.getX()){ // Right move;
                        float speed = (lastCursorPos2.getX() - currentCursorPosition.getX()) * corectionX;
                        setPos(pos.add(new Vector3f(front.cross(up)).normalize().mul(speed)));
                    }
                    if(lastCursorPos2.getX() < currentCursorPosition.getX()){ // Left move;
                        float speed = (currentCursorPosition.getX() - lastCursorPos2.getX()) * corectionX;
                        setPos(pos.sub(new Vector3f(front.cross(up)).normalize().mul(speed)));
                    }
                    if(lastCursorPos2.getY() > currentCursorPosition.getY()){ //  Down move;
                        float speed = (lastCursorPos2.getY() - currentCursorPosition.getY()) * corectionY;
                        setPos(pos.sub(new Vector3f(front.mul(speed).getX(), 0.0f, front.mul(speed).getZ())));
                    }
                    if(lastCursorPos2.getY() < currentCursorPosition.getY()){ //  Up move;
                        float speed = (currentCursorPosition.getY() - lastCursorPos2.getY()) * corectionY;
                        setPos(pos.add(new Vector3f(front.mul(speed).getX(), 0.0f, front.mul(speed).getZ())));
                    }
                    lastCursorPos2 = new Vector2f(currentCursorPosition);
                }else{
                    lastCursorPos2 = new Vector2f(Input.getInstance().getCursorPosition());
                }
            }

            zoomInOut();

            if(!turnButtonBlock) {
                if (Input.getInstance().isPressed(GLFW_KEY_Q)) {
                    turn = -2.0f;
                    stopTurn = false;
                    turnButtonBlock = true;
                } else if (Input.getInstance().isPressed(GLFW_KEY_E)) {
                    turn = 2.0f;
                    stopTurn = false;
                    turnButtonBlock = true;
                }
            }
            // УПРОВЛЕНИЕ ПОВОРОТАМИ - НАЧАЛО
            float temp = turnCounter;
            turnCounter += turn;
            if(turnCounter >= 90.0f){
                turn = turnCounter - temp;
                stopTurn = true;
            }else if(turnCounter <= -90.0f){
                turn = turnCounter - temp;
                stopTurn = true;
            }
            yaw -= turn;
            if(yaw < 0){
                yaw = 360 + yaw;
            }
            if(yaw > 359){
                yaw = yaw - 360;
            }
            if(stopTurn){
                turnCounter = 0.0f;
                turn = 0.0f;
                turnButtonBlock = false;
            }
            direction.setX((float) (Math.cos(Math.toRadians(pitch)) * Math.cos(Math.toRadians(yaw))));
            direction.setY((float) Math.sin(Math.toRadians(pitch)));
            direction.setZ((float) (Math.cos(Math.toRadians(pitch)) * Math.sin(Math.toRadians(yaw))));
            pos = pos.add(front.normalize().mul(hypotenuse)).add(direction.normalize().mul(-hypotenuse));
            front = direction.normalize();
            // УПРОВЛЕНИЕ ПОВОРОТАМИ - КОНЕЦ
            // КОНТРОЛЬ ОСТОНОВКИ КАМЕРЫ - НАЧАЛО
            if(getPos().add(getFront().mul(getHypotenuse())).getX() < minW){
                Vector3f stopPoint = new Vector3f(getPos().add(getFront().mul(getHypotenuse())));
                Vector3f ray = new Vector3f(getFront().mul(-getHypotenuse()));
                Vector3f stop = new Vector3f(minW,stopPoint.getY(),stopPoint.getZ()).add(ray);
                setPos(stop);
            }

            if(getPos().add(getFront().mul(getHypotenuse())).getX() > maxW){
                Vector3f stopPoint = new Vector3f(getPos().add(getFront().mul(getHypotenuse())));
                Vector3f ray = new Vector3f(getFront().mul(-getHypotenuse()));
                Vector3f stop = new Vector3f(maxW,stopPoint.getY(),stopPoint.getZ()).add(ray);
                setPos(stop);
            }

            if(getPos().add(getFront().mul(getHypotenuse())).getZ() < minH){
                Vector3f stopPoint = new Vector3f(getPos().add(getFront().mul(getHypotenuse())));
                Vector3f ray = new Vector3f(getFront().mul(-getHypotenuse()));
                Vector3f stop = new Vector3f(stopPoint.getX(),stopPoint.getY(),minH).add(ray);
                setPos(stop);
            }

            if(getPos().add(getFront().mul(getHypotenuse())).getZ() > maxH){
                Vector3f stopPoint = new Vector3f(getPos().add(getFront().mul(getHypotenuse())));
                Vector3f ray = new Vector3f(getFront().mul(-getHypotenuse()));
                Vector3f stop = new Vector3f(stopPoint.getX(),stopPoint.getY(),maxH).add(ray);
                setPos(stop);
            }
            // КОНТРОЛЬ ОСТОНОВКИ КАМЕРЫ - КОНЕЦ
        }

        // Контроль высоты - начало
        Vector3f currentCameraLookPos = getPos().add(getFront().mul(getHypotenuse()));
        float offset = 0.5f;
        float spOffset = 0.020f;
        if(grid != null) {
            int x = Math.round(currentCameraLookPos.getX());
            int z = Math.round(currentCameraLookPos.getZ());

            if((minW <= x && x <= maxW) && (minH <= z && z <= maxH)) {
                if (grid[x][z].getPosition().getX() - offset <= currentCameraLookPos.getX() && currentCameraLookPos.getX() <= grid[x][z].getPosition().getX() + offset) {
                    if (grid[x][z].getPosition().getZ() - offset <= currentCameraLookPos.getZ() && currentCameraLookPos.getZ() <= grid[x][z].getPosition().getZ() + offset) {
                        if (currentCameraLookPos.getY() != grid[x][z].getCurrentHeight()) {
                            if (currentCameraLookPos.getY() < grid[x][z].getCurrentHeight()) {
                                Camera.getInstance().getPos().setY(Camera.getInstance().getPos().getY() + spOffset);
                                if (Camera.getInstance().getPos().add(Camera.getInstance().getFront().mul(Camera.getInstance().getHypotenuse())).getY() >= grid[x][z].getCurrentHeight()) {
                                    Camera.getInstance().getPos().setY(Camera.getInstance().getPos().getY() - spOffset);
                                }
                            } else {
                                Camera.getInstance().getPos().setY(Camera.getInstance().getPos().getY() - spOffset);
                                if (Camera.getInstance().getPos().add(Camera.getInstance().getFront().mul(Camera.getInstance().getHypotenuse())).getY() <= grid[x][z].getCurrentHeight()) {
                                    Camera.getInstance().getPos().setY(Camera.getInstance().getPos().getY() + spOffset);
                                }
                            }
                        }
                    }
                }
            }
        }
        // Контрль высоты - конец

        if(Input.getInstance().getCursorMove()){
            lastCursorPos = new Vector2f(Input.getInstance().getCursorPosition());
            lastCursorPos2 = new Vector2f(Input.getInstance().getCursorPosition());
        }
    }

    public boolean cameraMove(Vector3f targetPosition){
        boolean over = false;
        Vector3f currenPos = new Vector3f(getPos().add(getFront().mul(getHypotenuse())));
        if(prepare){
            moveDirection = new Vector3f(targetPosition.sub(currenPos)).normalize();
            moveDistance = Math.abs(new Vector3f(targetPosition.sub(currenPos)).length());
            moveOffset = 0.1f;
            totalMove = 0.0f;
            prepare = false;
            setCameraControlLock(true);
        }else{
            Vector3f newPos = new Vector3f(currenPos.add(moveDirection.mul(moveOffset)));
            totalMove += moveOffset;
            if(totalMove >= moveDistance){
                Vector3f reverseFront = new Vector3f(getFront()).mul(-1.0f);
                Vector3f camPos = new Vector3f(targetPosition.add(reverseFront.mul(getHypotenuse())));
                Camera.getInstance().setPos(camPos);
                over = true;
                prepare = true;
                setCameraControlLock(false);
            }else {
                Vector3f reverseFront = new Vector3f(getFront()).mul(-1.0f);
                Vector3f camPos = new Vector3f(newPos.add(reverseFront.mul(getHypotenuse())));
                Camera.getInstance().setPos(camPos);
            }
        }
        return over;
    }

    private void zoomInOut(){
        if(Input.getInstance().getScrollOffset() != 0.0f) {
            offset = Input.getInstance().getScrollOffset();
            if(Math.abs(offset) <= 1.0f){
                offset *= 0.4f;
            }else{
                offset *= 0.5f;
            }
        }

        float temp = hypotenuse;
        hypotenuse -= offset;
        if(offset > 0.0f){
            offset /= SCROLL_SENS;
            if(offset < 0.01f){
                offset = 0.0f;
            }
        }else if(offset < 0.0f){
            offset /= SCROLL_SENS;
            if(offset > -0.01f){
                offset = 0.0f;
            }
        }
        if(hypotenuse > MAX_DIST){
            hypotenuse = MAX_DIST;
        }else if(hypotenuse < MIN_DIST){
            hypotenuse = MIN_DIST;
        }

        float result = temp - hypotenuse;

        setPos(pos.add(front.mul(result)));
    }

    private void updateProjection(){
        setPerspective();
        setOrtho();
    }

    private void setPerspective(){
        perspective.setPerspective(fov);
        perspective.setView(
                pos.getX(), pos.getY() ,pos.getZ(),
                pos.getX() + front.getX(), pos.getY() + front.getY(),pos.getZ() + front.getZ(),
                up.getX(), up.getY(), up.getZ()
        );
    }

    public Projection getPerspective() {
        return perspective;
    }

    private void setOrtho(){
        float left = 0.0f;
        float right = Window.getInstance().getWidth();
        float top = 0.0f;
        float bottom = Window.getInstance().getHeight();
        float near = -1.0f;
        float far = 100.0f;
        ortho.setOrtho(left,right,bottom,top,near,far);
    }

    public Projection getOrtho() {
        return ortho;
    }

    private void updateViewMatrix() {
        perspective.setView(
                pos.getX(), pos.getY() ,pos.getZ(),
                pos.getX() + front.getX(), pos.getY() + front.getY(),pos.getZ() + front.getZ(),
                up.getX(), up.getY(), up.getZ()
        );
    }

    public Vector3f getPos() {
        return pos;
    }

    public void setPos(Vector3f pos) {
        this.pos = pos;
    }

    public Vector3f getFront() {
        return front;
    }

    public void setFront(Vector3f front) {
        this.front = front;
    }

    public Vector3f getUp() {
        return up;
    }

    public void setUp(Vector3f up) {
        this.up = up;
    }

    public float getHypotenuse() {
        return hypotenuse;
    }

    public boolean isCameraControlLock() {
        return cameraControlLock;
    }

    public void setCameraControlLock(boolean cameraControlLock) {
        this.cameraControlLock = cameraControlLock;
    }

    public float getYaw(){
        return yaw;
    }
}
