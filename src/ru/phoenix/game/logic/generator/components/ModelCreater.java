package ru.phoenix.game.logic.generator.components;

import ru.phoenix.core.debug.HowLong;
import ru.phoenix.core.loader.model.Mesh;
import ru.phoenix.core.loader.model.Vertex;
import ru.phoenix.core.math.Vector2f;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.game.logic.element.grid.Cell;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModelCreater {

    private static List<Vertex> vertices = new ArrayList<>();
    private static List<Integer> indices = new ArrayList<>();
    private static float textureOffset = 0.008f;

    public static Mesh start(int floor, Cell[][] grid, int width, int height, GraundTexture textures){
        int currentArea = 0;
        vertices.clear();
        indices.clear();
        int index = 0;

        HowLong.setup("модели");

        for(int x = 0; x <= width; x++){
            for(int z = 0; z <= height; z++) {

                int count = 0;

                Vector3f position = new Vector3f(x,grid[x][z].getCurrentHeight(),z);

                Vector3f leftPos = new Vector3f(position.add(new Vector3f(-1.0f, 0.0f, 0.0f)));
                boolean left = false;
                Vector3f upPos = new Vector3f(position.add(new Vector3f(0.0f, 0.0f, 1.0f)));
                boolean up = false;
                Vector3f rightPos = new Vector3f(position.add(new Vector3f(1.0f, 0.0f, 0.0f)));
                boolean right = false;
                Vector3f downPos = new Vector3f(position.add(new Vector3f(0.0f, 0.0f, -1.0f)));
                boolean down = false;

                if(0 <= leftPos.getX() && leftPos.getX() <= width && 0 <= leftPos.getZ() && leftPos.getZ() <= height) {
                    leftPos = new Vector3f(leftPos.getX(),grid[(int)leftPos.getX()][(int)leftPos.getZ()].getCurrentHeight(),leftPos.getZ());
                    left = true;
                }

                if(0 <= upPos.getX() && upPos.getX() <= width && 0 <= upPos.getZ() && upPos.getZ() <= height) {
                    upPos = new Vector3f(upPos.getX(),grid[(int)upPos.getX()][(int)upPos.getZ()].getCurrentHeight(),upPos.getZ());
                    up = true;
                }

                if(0 <= rightPos.getX() && rightPos.getX() <= width && 0 <= rightPos.getZ() && rightPos.getZ() <= height) {
                    rightPos = new Vector3f(rightPos.getX(),grid[(int)rightPos.getX()][(int)rightPos.getZ()].getCurrentHeight(),rightPos.getZ());
                    right = true;
                }

                if(0 <= downPos.getX() && downPos.getX() <= width && 0 <= downPos.getZ() && downPos.getZ() <= height) {
                    downPos = new Vector3f(downPos.getX(),grid[(int)downPos.getX()][(int)downPos.getZ()].getCurrentHeight(),downPos.getZ());
                    down = true;
                }

                if (left && up && right && down) {

                    boolean check = false;

                    float currentHeight = position.getY();
                    float leftHeight = leftPos.getY();
                    float upHeight = upPos.getY();
                    float rightHeight = rightPos.getY();
                    float downHeight = downPos.getY();

                    if (Math.abs(Math.round(currentHeight) - currentHeight) == 0.5f) {
                        if (currentHeight + 0.5 == leftHeight && currentHeight - 0.5f == upHeight && currentHeight - 0.5f == rightHeight && currentHeight - 0.5f == downHeight) { // RIGHT
                            check = true;
                            Vertex v0 = new Vertex();
                            Vertex v1 = new Vertex();
                            Vertex v2 = new Vertex();
                            Vertex v3 = new Vertex();

                            v0.setPosition(new Vector3f(position.getX() - 0.5f, position.getY() + 0.5f, position.getZ() - 0.5f));
                            v1.setPosition(new Vector3f(position.getX() + 0.5f, position.getY() - 0.5f, position.getZ() - 0.5f));
                            v2.setPosition(new Vector3f(position.getX() + 0.5f, position.getY() - 0.5f, position.getZ() + 0.5f));
                            v3.setPosition(new Vector3f(position.getX() - 0.5f, position.getY() + 0.5f, position.getZ() + 0.5f));

                            v0.setNormal(new Vector3f(0.5f, 0.5f, 0.0f));
                            v1.setNormal(new Vector3f(0.5f, 0.5f, 0.0f));
                            v2.setNormal(new Vector3f(0.5f, 0.5f, 0.0f));
                            v3.setNormal(new Vector3f(0.5f, 0.5f, 0.0f));

                            setUpperBevelPlaneTexCoord(currentArea,currentHeight,v0,v1,v2,v3);
                            tangentBitangent(v0,v1,v2,v3);

                            ArrayList<Vertex> cellVertices = new ArrayList<>();
                            vertices.add(v0);
                            Vertex v = new Vertex(v0);
                            v.setTexCoords(new Vector2f(0.0f,1.0f));
                            cellVertices.add(v);
                            vertices.add(v1);
                            v = new Vertex(v1);
                            v.setTexCoords(new Vector2f(0.0f,0.0f));
                            cellVertices.add(v);
                            vertices.add(v2);
                            v = new Vertex(v2);
                            v.setTexCoords(new Vector2f(1.0f,0.0f));
                            cellVertices.add(v);
                            vertices.add(v3);
                            v = new Vertex(v3);
                            v.setTexCoords(new Vector2f(1.0f,1.0f));
                            cellVertices.add(v);

                            indices.add(index++);
                            indices.add(index++);
                            indices.add(index);
                            index -= 2;
                            indices.add(index);
                            index += 2;
                            indices.add(index++);
                            indices.add(index++);
                            ArrayList<Integer> cellIndices = new ArrayList<>(Arrays.asList(0,1,2,0,2,3));
                            Mesh cellMesh = new Mesh(cellVertices,cellIndices,null);
                            grid[x][z].setupMesh(cellMesh);
                            grid[x][z].setupTexture(textures.getCursor(),textures.getGrayZona(),textures.getRedZona(),textures.getGreenZona(),textures.getGoldZona(),textures.getBlueZona());
                            grid[x][z].setBevel(true);
                            grid[x][z].setBevelAngle(90.0f);

                            v0 = new Vertex();
                            v1 = new Vertex();
                            v2 = new Vertex();

                            v0.setPosition(new Vector3f(upPos.getX() + 0.5f, position.getY() - 0.5f, upPos.getZ() - 0.5f));
                            v1.setPosition(new Vector3f(upPos.getX() - 0.5f, position.getY() - 0.5f, upPos.getZ() - 0.5f));
                            v2.setPosition(new Vector3f(upPos.getX() - 0.5f, position.getY() + 0.5f, upPos.getZ() - 0.5f));

                            v0.setNormal(new Vector3f(0.0f, 0.0f, 1.0f));
                            v1.setNormal(new Vector3f(0.0f, 0.0f, 1.0f));
                            v2.setNormal(new Vector3f(0.0f, 0.0f, 1.0f));

                           /* if (currentArea == PLAIN_AREA) {
                                if (upPos.getY() < -1.0f) {
                                    v0.setTexCoords(new Vector2f(0.0f, 0.0f));
                                    v1.setTexCoords(new Vector2f(1.0f / 4.0f, 0.0f));
                                    v2.setTexCoords(new Vector2f(1.0f / 4.0f, 1.0f / 4.0f));
                                } else {
                                    v0.setTexCoords(new Vector2f(0.0f, 1.0f / 4.0f));
                                    v1.setTexCoords(new Vector2f(1.0f / 4.0f, 1.0f / 4.0f));
                                    v2.setTexCoords(new Vector2f(1.0f / 4.0f, 2.0f / 4.0f));
                                }
                            } else if (currentArea == MOUNTAIN_AREA) {
                                if (position.getY() + 0.5f < -1.0f) {
                                    v0.setTexCoords(new Vector2f(1.0f / 4.0f + 0.005f, 0.0f + 0.005f));
                                    v1.setTexCoords(new Vector2f(2.0f / 4.0f - 0.005f, 0.0f + 0.005f));
                                    v2.setTexCoords(new Vector2f(2.0f / 4.0f - 0.005f, 1.0f / 4.0f - 0.005f));
                                } else if(position.getY() + 0.5f < 1.0f){
                                    v0.setTexCoords(new Vector2f(1.0f / 4.0f + 0.005f, 1.0f / 4.0f + 0.005f));
                                    v1.setTexCoords(new Vector2f(2.0f / 4.0f - 0.005f, 1.0f / 4.0f + 0.005f));
                                    v2.setTexCoords(new Vector2f(2.0f / 4.0f - 0.005f, 2.0f / 4.0f - 0.005f));
                                }else {
                                    v0.setTexCoords(new Vector2f(2.0f / 4.0f, 1.0f / 4.0f));
                                    v1.setTexCoords(new Vector2f(3.0f / 4.0f, 1.0f / 4.0f));
                                    v2.setTexCoords(new Vector2f(3.0f / 4.0f, 2.0f / 4.0f));
                                }
                            }*/

                            v0.setTexCoords(new Vector2f(0.0f, 0.0f));
                            v1.setTexCoords(new Vector2f(1.0f, 0.0f));
                            v2.setTexCoords(new Vector2f(1.0f, 1.0f));

                            v0.setTangents(new Vector3f(count,0.0f,0.0f));
                            v1.setTangents(new Vector3f(count,0.0f,0.0f));
                            v2.setTangents(new Vector3f(count,0.0f,0.0f));

                            vertices.add(v0);
                            vertices.add(v1);
                            vertices.add(v2);

                            indices.add(index++);
                            indices.add(index++);
                            indices.add(index++);

                            v0 = new Vertex();
                            v1 = new Vertex();
                            v2 = new Vertex();

                            v0.setPosition(new Vector3f(downPos.getX() - 0.5f, position.getY() + 0.5f, downPos.getZ() + 0.5f));
                            v1.setPosition(new Vector3f(downPos.getX() - 0.5f, position.getY() - 0.5f, downPos.getZ() + 0.5f));
                            v2.setPosition(new Vector3f(downPos.getX() + 0.5f, position.getY() - 0.5f, downPos.getZ() + 0.5f));

                            v0.setNormal(new Vector3f(0.0f, 0.0f, -1.0f));
                            v1.setNormal(new Vector3f(0.0f, 0.0f, -1.0f));
                            v2.setNormal(new Vector3f(0.0f, 0.0f, -1.0f));

                            /*if (currentArea == PLAIN_AREA) {
                                if (downPos.getY() < -1.0f) {
                                    v0.setTexCoords(new Vector2f(0.0f, 1.0f / 4.0f));
                                    v1.setTexCoords(new Vector2f(0.0f, 0.0f));
                                    v2.setTexCoords(new Vector2f(1.0f / 4.0f, 0.0f));
                                } else {
                                    v0.setTexCoords(new Vector2f(0.0f, 2.0f / 4.0f));
                                    v1.setTexCoords(new Vector2f(0.0f, 1.0f / 4.0f));
                                    v2.setTexCoords(new Vector2f(1.0f / 4.0f, 1.0f / 4.0f));
                                }
                            } else if (currentArea == MOUNTAIN_AREA) {
                                if (position.getY() + 0.5f < -1.0f) {
                                    v0.setTexCoords(new Vector2f(1.0f / 4.0f + 0.005f, 1.0f / 4.0f - 0.005f));
                                    v1.setTexCoords(new Vector2f(1.0f / 4.0f + 0.005f, 0.0f + 0.005f));
                                    v2.setTexCoords(new Vector2f(2.0f / 4.0f - 0.005f, 0.0f + 0.005f));
                                } else if(position.getY() + 0.5f < 1.0f){
                                    v0.setTexCoords(new Vector2f(1.0f / 4.0f + 0.005f, 2.0f / 4.0f - 0.005f));
                                    v1.setTexCoords(new Vector2f(1.0f / 4.0f + 0.005f, 1.0f / 4.0f + 0.005f));
                                    v2.setTexCoords(new Vector2f(2.0f / 4.0f - 0.005f, 1.0f / 4.0f + 0.005f));
                                }else {
                                    v0.setTexCoords(new Vector2f(2.0f / 4.0f, 2.0f / 4.0f));
                                    v1.setTexCoords(new Vector2f(2.0f / 4.0f, 1.0f / 4.0f));
                                    v2.setTexCoords(new Vector2f(3.0f / 4.0f, 1.0f / 4.0f));
                                }
                            }*/
                            v0.setTexCoords(new Vector2f(0.0f, 0.0f));
                            v1.setTexCoords(new Vector2f(1.0f, 0.0f));
                            v2.setTexCoords(new Vector2f(1.0f, 1.0f));

                            v0.setTangents(new Vector3f(count,0.0f,0.0f));
                            v1.setTangents(new Vector3f(count,0.0f,0.0f));
                            v2.setTangents(new Vector3f(count,0.0f,0.0f));

                            vertices.add(v0);
                            vertices.add(v1);
                            vertices.add(v2);

                            indices.add(index++);
                            indices.add(index++);
                            indices.add(index++);
                        } else if (currentHeight + 0.5 == leftHeight && currentHeight + 0.5f == upHeight && currentHeight - 0.5f == rightHeight && currentHeight + 0.5f == downHeight) { // RIGHT
                            check = true;
                            Vertex v0 = new Vertex();
                            Vertex v1 = new Vertex();
                            Vertex v2 = new Vertex();
                            Vertex v3 = new Vertex();

                            v0.setPosition(new Vector3f(position.getX() - 0.5f, position.getY() + 0.5f, position.getZ() - 0.5f));
                            v1.setPosition(new Vector3f(position.getX() + 0.5f, position.getY() - 0.5f, position.getZ() - 0.5f));
                            v2.setPosition(new Vector3f(position.getX() + 0.5f, position.getY() - 0.5f, position.getZ() + 0.5f));
                            v3.setPosition(new Vector3f(position.getX() - 0.5f, position.getY() + 0.5f, position.getZ() + 0.5f));

                            v0.setNormal(new Vector3f(0.5f, 0.5f, 0.0f));
                            v1.setNormal(new Vector3f(0.5f, 0.5f, 0.0f));
                            v2.setNormal(new Vector3f(0.5f, 0.5f, 0.0f));
                            v3.setNormal(new Vector3f(0.5f, 0.5f, 0.0f));

                            setUpperBevelPlaneTexCoord(currentArea,currentHeight,v0,v1,v2,v3);
                            tangentBitangent(v0,v1,v2,v3);

                            ArrayList<Vertex> cellVertices = new ArrayList<>();
                            vertices.add(v0);
                            Vertex v = new Vertex(v0);
                            v.setTexCoords(new Vector2f(0.0f,1.0f));
                            cellVertices.add(v);
                            vertices.add(v1);
                            v = new Vertex(v1);
                            v.setTexCoords(new Vector2f(0.0f,0.0f));
                            cellVertices.add(v);
                            vertices.add(v2);
                            v = new Vertex(v2);
                            v.setTexCoords(new Vector2f(1.0f,0.0f));
                            cellVertices.add(v);
                            vertices.add(v3);
                            v = new Vertex(v3);
                            v.setTexCoords(new Vector2f(1.0f,1.0f));
                            cellVertices.add(v);

                            indices.add(index++);
                            indices.add(index++);
                            indices.add(index);
                            index -= 2;
                            indices.add(index);
                            index += 2;
                            indices.add(index++);
                            indices.add(index++);
                            ArrayList<Integer> cellIndices = new ArrayList<>(Arrays.asList(0,1,2,0,2,3));
                            Mesh cellMesh = new Mesh(cellVertices,cellIndices,null);
                            grid[x][z].setupMesh(cellMesh);
                            grid[x][z].setupTexture(textures.getCursor(),textures.getGrayZona(),textures.getRedZona(),textures.getGreenZona(),textures.getGoldZona(),textures.getBlueZona());
                            grid[x][z].setBevel(true);
                            grid[x][z].setBevelAngle(90.0f);

                            v0 = new Vertex();
                            v1 = new Vertex();
                            v2 = new Vertex();

                            v0.setPosition(new Vector3f(upPos.getX() + 0.5f, position.getY() - 0.5f, upPos.getZ() - 0.5f));
                            v1.setPosition(new Vector3f(upPos.getX() - 0.5f, position.getY() - 0.5f, upPos.getZ() - 0.5f));
                            v2.setPosition(new Vector3f(upPos.getX() - 0.5f, position.getY() + 0.5f, upPos.getZ() - 0.5f));

                            v0.setNormal(new Vector3f(0.0f, 0.0f, 1.0f));
                            v1.setNormal(new Vector3f(0.0f, 0.0f, 1.0f));
                            v2.setNormal(new Vector3f(0.0f, 0.0f, 1.0f));

                            /*if (currentArea == PLAIN_AREA) {
                                if (upPos.getY() < -1.0f) {
                                    v0.setTexCoords(new Vector2f(0.0f, 0.0f));
                                    v1.setTexCoords(new Vector2f(1.0f / 4.0f, 0.0f));
                                    v2.setTexCoords(new Vector2f(1.0f / 4.0f, 1.0f / 4.0f));
                                } else {
                                    v0.setTexCoords(new Vector2f(0.0f, 1.0f / 4.0f));
                                    v1.setTexCoords(new Vector2f(1.0f / 4.0f, 1.0f / 4.0f));
                                    v2.setTexCoords(new Vector2f(1.0f / 4.0f, 2.0f / 4.0f));
                                }
                            } else if (currentArea == MOUNTAIN_AREA) {
                                if (position.getY() + 0.5f < -1.0f) {
                                    v0.setTexCoords(new Vector2f(1.0f / 4.0f + 0.005f, 0.0f + 0.005f));
                                    v1.setTexCoords(new Vector2f(2.0f / 4.0f - 0.005f, 0.0f + 0.005f));
                                    v2.setTexCoords(new Vector2f(2.0f / 4.0f - 0.005f, 1.0f / 4.0f - 0.005f));
                                } else if(position.getY() + 0.5f < 1.0f){
                                    v0.setTexCoords(new Vector2f(1.0f / 4.0f + 0.005f, 1.0f / 4.0f + 0.005f));
                                    v1.setTexCoords(new Vector2f(2.0f / 4.0f - 0.005f, 1.0f / 4.0f + 0.005f));
                                    v2.setTexCoords(new Vector2f(2.0f / 4.0f - 0.005f, 2.0f / 4.0f - 0.005f));
                                }else {
                                    v0.setTexCoords(new Vector2f(2.0f / 4.0f, 1.0f / 4.0f));
                                    v1.setTexCoords(new Vector2f(3.0f / 4.0f, 1.0f / 4.0f));
                                    v2.setTexCoords(new Vector2f(3.0f / 4.0f, 2.0f / 4.0f));
                                }
                            }*/

                            v0.setTexCoords(new Vector2f(0.0f, 0.0f));
                            v1.setTexCoords(new Vector2f(1.0f, 0.0f));
                            v2.setTexCoords(new Vector2f(1.0f, 1.0f));

                            v0.setTangents(new Vector3f(count,0.0f,0.0f));
                            v1.setTangents(new Vector3f(count,0.0f,0.0f));
                            v2.setTangents(new Vector3f(count,0.0f,0.0f));

                            vertices.add(v0);
                            vertices.add(v1);
                            vertices.add(v2);

                            indices.add(index++);
                            indices.add(index++);
                            indices.add(index++);

                            v0 = new Vertex();
                            v1 = new Vertex();
                            v2 = new Vertex();

                            v0.setPosition(new Vector3f(downPos.getX() - 0.5f, position.getY() + 0.5f, downPos.getZ() + 0.5f));
                            v1.setPosition(new Vector3f(downPos.getX() - 0.5f, position.getY() - 0.5f, downPos.getZ() + 0.5f));
                            v2.setPosition(new Vector3f(downPos.getX() + 0.5f, position.getY() - 0.5f, downPos.getZ() + 0.5f));

                            v0.setNormal(new Vector3f(0.0f, 0.0f, -1.0f));
                            v1.setNormal(new Vector3f(0.0f, 0.0f, -1.0f));
                            v2.setNormal(new Vector3f(0.0f, 0.0f, -1.0f));

                            /*if (currentArea == PLAIN_AREA) {
                                if (downPos.getY() < -1.0f) {
                                    v0.setTexCoords(new Vector2f(0.0f, 1.0f / 4.0f));
                                    v1.setTexCoords(new Vector2f(0.0f, 0.0f));
                                    v2.setTexCoords(new Vector2f(1.0f / 4.0f, 0.0f));
                                } else {
                                    v0.setTexCoords(new Vector2f(0.0f, 2.0f / 4.0f));
                                    v1.setTexCoords(new Vector2f(0.0f, 1.0f / 4.0f));
                                    v2.setTexCoords(new Vector2f(1.0f / 4.0f, 1.0f / 4.0f));
                                }
                            } else if (currentArea == MOUNTAIN_AREA) {
                                if (position.getY() + 0.5f < -1.0f) {
                                    v0.setTexCoords(new Vector2f(1.0f / 4.0f + 0.005f, 1.0f / 4.0f - 0.005f));
                                    v1.setTexCoords(new Vector2f(1.0f / 4.0f + 0.005f, 0.0f + 0.005f));
                                    v2.setTexCoords(new Vector2f(2.0f / 4.0f - 0.005f, 0.0f + 0.005f));
                                } else if(position.getY() + 0.5f < 1.0f){
                                    v0.setTexCoords(new Vector2f(1.0f / 4.0f + 0.005f, 2.0f / 4.0f - 0.005f));
                                    v1.setTexCoords(new Vector2f(1.0f / 4.0f + 0.005f, 1.0f / 4.0f + 0.005f));
                                    v2.setTexCoords(new Vector2f(2.0f / 4.0f - 0.005f, 1.0f / 4.0f + 0.005f));
                                }else {
                                    v0.setTexCoords(new Vector2f(2.0f / 4.0f, 2.0f / 4.0f));
                                    v1.setTexCoords(new Vector2f(2.0f / 4.0f, 1.0f / 4.0f));
                                    v2.setTexCoords(new Vector2f(3.0f / 4.0f, 1.0f / 4.0f));
                                }
                            }*/

                            v0.setTexCoords(new Vector2f(0.0f, 0.0f));
                            v1.setTexCoords(new Vector2f(1.0f, 0.0f));
                            v2.setTexCoords(new Vector2f(1.0f, 1.0f));

                            v0.setTangents(new Vector3f(count,0.0f,0.0f));
                            v1.setTangents(new Vector3f(count,0.0f,0.0f));
                            v2.setTangents(new Vector3f(count,0.0f,0.0f));

                            vertices.add(v0);
                            vertices.add(v1);
                            vertices.add(v2);

                            indices.add(index++);
                            indices.add(index++);
                            indices.add(index++);
                        }

                        if (currentHeight + 0.5 == upHeight && currentHeight - 0.5f == leftHeight && currentHeight - 0.5f == rightHeight && currentHeight - 0.5f == downHeight) { // DOWN
                            check = true;
                            Vertex v0 = new Vertex();
                            Vertex v1 = new Vertex();
                            Vertex v2 = new Vertex();
                            Vertex v3 = new Vertex();

                            v0.setPosition(new Vector3f(position.getX() - 0.5f, position.getY() + 0.5f, position.getZ() + 0.5f));
                            v1.setPosition(new Vector3f(position.getX() - 0.5f, position.getY() - 0.5f, position.getZ() - 0.5f));
                            v2.setPosition(new Vector3f(position.getX() + 0.5f, position.getY() - 0.5f, position.getZ() - 0.5f));
                            v3.setPosition(new Vector3f(position.getX() + 0.5f, position.getY() + 0.5f, position.getZ() + 0.5f));

                            v0.setNormal(new Vector3f(0.0f, 0.5f, -0.5f));
                            v1.setNormal(new Vector3f(0.0f, 0.5f, -0.5f));
                            v2.setNormal(new Vector3f(0.0f, 0.5f, -0.5f));
                            v3.setNormal(new Vector3f(0.0f, 0.5f, -0.5f));

                            setUpperBevelPlaneTexCoord(currentArea,currentHeight,v0,v1,v2,v3);
                            tangentBitangent(v0,v1,v2,v3);

                            ArrayList<Vertex> cellVertices = new ArrayList<>();
                            vertices.add(v0);
                            Vertex v = new Vertex(v0);
                            v.setTexCoords(new Vector2f(0.0f,1.0f));
                            cellVertices.add(v);
                            vertices.add(v1);
                            v = new Vertex(v1);
                            v.setTexCoords(new Vector2f(0.0f,0.0f));
                            cellVertices.add(v);
                            vertices.add(v2);
                            v = new Vertex(v2);
                            v.setTexCoords(new Vector2f(1.0f,0.0f));
                            cellVertices.add(v);
                            vertices.add(v3);
                            v = new Vertex(v3);
                            v.setTexCoords(new Vector2f(1.0f,1.0f));
                            cellVertices.add(v);

                            indices.add(index++);
                            indices.add(index++);
                            indices.add(index);
                            index -= 2;
                            indices.add(index);
                            index += 2;
                            indices.add(index++);
                            indices.add(index++);
                            ArrayList<Integer> cellIndices = new ArrayList<>(Arrays.asList(0,1,2,0,2,3));
                            Mesh cellMesh = new Mesh(cellVertices,cellIndices,null);
                            grid[x][z].setupMesh(cellMesh);
                            grid[x][z].setupTexture(textures.getCursor(),textures.getGrayZona(),textures.getRedZona(),textures.getGreenZona(),textures.getGoldZona(),textures.getBlueZona());
                            grid[x][z].setBevel(true);
                            grid[x][z].setBevelAngle(180.0f);

                            v0 = new Vertex();
                            v1 = new Vertex();
                            v2 = new Vertex();

                            v0.setPosition(new Vector3f(leftPos.getX() + 0.5f, position.getY() + 0.5f, leftPos.getZ() + 0.5f));
                            v1.setPosition(new Vector3f(leftPos.getX() + 0.5f, position.getY() - 0.5f, leftPos.getZ() + 0.5f));
                            v2.setPosition(new Vector3f(leftPos.getX() + 0.5f, position.getY() - 0.5f, leftPos.getZ() - 0.5f));

                            v0.setNormal(new Vector3f(-1.0f, 0.0f, 0.0f));
                            v1.setNormal(new Vector3f(-1.0f, 0.0f, 0.0f));
                            v2.setNormal(new Vector3f(-1.0f, 0.0f, 0.0f));

                            /*if (currentArea == PLAIN_AREA) {
                                if (leftPos.getY() < -1.0f) {
                                    v0.setTexCoords(new Vector2f(0.0f, 1.0f / 4.0f));
                                    v1.setTexCoords(new Vector2f(0.0f, 0.0f));
                                    v2.setTexCoords(new Vector2f(1.0f / 4.0f, 0.0f));
                                } else {
                                    v0.setTexCoords(new Vector2f(0.0f, 2.0f / 4.0f));
                                    v1.setTexCoords(new Vector2f(0.0f, 1.0f / 4.0f));
                                    v2.setTexCoords(new Vector2f(1.0f / 4.0f, 1.0f / 4.0f));
                                }
                            } else if (currentArea == MOUNTAIN_AREA) {
                                if (position.getY() + 0.5f < -1.0f) {
                                    v0.setTexCoords(new Vector2f(1.0f / 4.0f + 0.005f, 1.0f / 4.0f - 0.005f));
                                    v1.setTexCoords(new Vector2f(1.0f / 4.0f + 0.005f, 0.0f + 0.005f));
                                    v2.setTexCoords(new Vector2f(2.0f / 4.0f - 0.005f, 0.0f + 0.005f));
                                } else if(position.getY() + 0.5f < 1.0f){
                                    v0.setTexCoords(new Vector2f(1.0f / 4.0f + 0.005f, 2.0f / 4.0f - 0.005f));
                                    v1.setTexCoords(new Vector2f(1.0f / 4.0f + 0.005f, 1.0f / 4.0f + 0.005f));
                                    v2.setTexCoords(new Vector2f(2.0f / 4.0f + 0.005f, 1.0f / 4.0f + 0.005f));
                                }else {
                                    v0.setTexCoords(new Vector2f(2.0f / 4.0f, 2.0f / 4.0f));
                                    v1.setTexCoords(new Vector2f(2.0f / 4.0f, 1.0f / 4.0f));
                                    v2.setTexCoords(new Vector2f(3.0f / 4.0f, 1.0f / 4.0f));
                                }
                            }*/

                            v0.setTexCoords(new Vector2f(0.0f, 0.0f));
                            v1.setTexCoords(new Vector2f(1.0f, 0.0f));
                            v2.setTexCoords(new Vector2f(1.0f, 1.0f));

                            v0.setTangents(new Vector3f(count,0.0f,0.0f));
                            v1.setTangents(new Vector3f(count,0.0f,0.0f));
                            v2.setTangents(new Vector3f(count,0.0f,0.0f));

                            vertices.add(v0);
                            vertices.add(v1);
                            vertices.add(v2);

                            indices.add(index++);
                            indices.add(index++);
                            indices.add(index++);

                            v0 = new Vertex();
                            v1 = new Vertex();
                            v2 = new Vertex();

                            v0.setPosition(new Vector3f(rightPos.getX() - 0.5f, position.getY() + 0.5f, rightPos.getZ() + 0.5f));
                            v1.setPosition(new Vector3f(rightPos.getX() - 0.5f, position.getY() - 0.5f, rightPos.getZ() - 0.5f));
                            v2.setPosition(new Vector3f(rightPos.getX() - 0.5f, position.getY() - 0.5f, rightPos.getZ() + 0.5f));

                            v0.setNormal(new Vector3f(1.0f, 0.0f, 0.0f));
                            v1.setNormal(new Vector3f(1.0f, 0.0f, 0.0f));
                            v2.setNormal(new Vector3f(1.0f, 0.0f, 0.0f));

                            /*if (currentArea == PLAIN_AREA) {
                                if (rightPos.getY() < -1.0f) {
                                    v0.setTexCoords(new Vector2f(1.0f / 4.0f, 1.0f / 4.0f));
                                    v1.setTexCoords(new Vector2f(0.0f, 0.0f));
                                    v2.setTexCoords(new Vector2f(1.0f / 4.0f, 0.0f));
                                } else {
                                    v0.setTexCoords(new Vector2f(1.0f / 4.0f, 2.0f / 4.0f));
                                    v1.setTexCoords(new Vector2f(0.0f, 1.0f / 4.0f));
                                    v2.setTexCoords(new Vector2f(1.0f / 4.0f, 1.0f / 4.0f));
                                }
                            } else if (currentArea == MOUNTAIN_AREA) {
                                if (position.getY() + 0.5f < -1.0f) {
                                    v0.setTexCoords(new Vector2f(2.0f / 4.0f - 0.005f, 1.0f / 4.0f - 0.005f));
                                    v1.setTexCoords(new Vector2f(1.0f / 4.0f + 0.005f, 0.0f + 0.005f));
                                    v2.setTexCoords(new Vector2f(2.0f / 4.0f - 0.005f, 0.0f + 0.005f));
                                } else if(position.getY() + 0.5f < 1.0f){
                                    v0.setTexCoords(new Vector2f(2.0f / 4.0f - 0.005f, 2.0f / 4.0f - 0.005f));
                                    v1.setTexCoords(new Vector2f(1.0f / 4.0f + 0.005f, 1.0f / 4.0f + 0.005f));
                                    v2.setTexCoords(new Vector2f(2.0f / 4.0f - 0.005f, 1.0f / 4.0f + 0.005f));
                                }else {
                                    v0.setTexCoords(new Vector2f(3.0f / 4.0f, 2.0f / 4.0f));
                                    v1.setTexCoords(new Vector2f(2.0f / 4.0f, 1.0f / 4.0f));
                                    v2.setTexCoords(new Vector2f(3.0f / 4.0f, 1.0f / 4.0f));
                                }
                            }*/

                            v0.setTexCoords(new Vector2f(0.0f, 0.0f));
                            v1.setTexCoords(new Vector2f(1.0f, 0.0f));
                            v2.setTexCoords(new Vector2f(1.0f, 1.0f));

                            v0.setTangents(new Vector3f(count,0.0f,0.0f));
                            v1.setTangents(new Vector3f(count,0.0f,0.0f));
                            v2.setTangents(new Vector3f(count,0.0f,0.0f));

                            vertices.add(v0);
                            vertices.add(v1);
                            vertices.add(v2);

                            indices.add(index++);
                            indices.add(index++);
                            indices.add(index++);
                        } else if (currentHeight + 0.5f == upHeight && currentHeight + 0.5f == leftHeight && currentHeight + 0.5f == rightHeight && currentHeight - 0.5f == downHeight) { // DOWN
                            check = true;
                            Vertex v0 = new Vertex();
                            Vertex v1 = new Vertex();
                            Vertex v2 = new Vertex();
                            Vertex v3 = new Vertex();

                            v0.setPosition(new Vector3f(position.getX() - 0.5f, position.getY() + 0.5f, position.getZ() + 0.5f));
                            v1.setPosition(new Vector3f(position.getX() - 0.5f, position.getY() - 0.5f, position.getZ() - 0.5f));
                            v2.setPosition(new Vector3f(position.getX() + 0.5f, position.getY() - 0.5f, position.getZ() - 0.5f));
                            v3.setPosition(new Vector3f(position.getX() + 0.5f, position.getY() + 0.5f, position.getZ() + 0.5f));

                            v0.setNormal(new Vector3f(0.0f, 0.5f, -0.5f));
                            v1.setNormal(new Vector3f(0.0f, 0.5f, -0.5f));
                            v2.setNormal(new Vector3f(0.0f, 0.5f, -0.5f));
                            v3.setNormal(new Vector3f(0.0f, 0.5f, -0.5f));

                            setUpperBevelPlaneTexCoord(currentArea,currentHeight,v0,v1,v2,v3);
                            tangentBitangent(v0,v1,v2,v3);

                            ArrayList<Vertex> cellVertices = new ArrayList<>();
                            vertices.add(v0);
                            Vertex v = new Vertex(v0);
                            v.setTexCoords(new Vector2f(0.0f,1.0f));
                            cellVertices.add(v);
                            vertices.add(v1);
                            v = new Vertex(v1);
                            v.setTexCoords(new Vector2f(0.0f,0.0f));
                            cellVertices.add(v);
                            vertices.add(v2);
                            v = new Vertex(v2);
                            v.setTexCoords(new Vector2f(1.0f,0.0f));
                            cellVertices.add(v);
                            vertices.add(v3);
                            v = new Vertex(v3);
                            v.setTexCoords(new Vector2f(1.0f,1.0f));
                            cellVertices.add(v);

                            indices.add(index++);
                            indices.add(index++);
                            indices.add(index);
                            index -= 2;
                            indices.add(index);
                            index += 2;
                            indices.add(index++);
                            indices.add(index++);
                            ArrayList<Integer> cellIndices = new ArrayList<>(Arrays.asList(0,1,2,0,2,3));
                            Mesh cellMesh = new Mesh(cellVertices,cellIndices,null);
                            grid[x][z].setupMesh(cellMesh);
                            grid[x][z].setupTexture(textures.getCursor(),textures.getGrayZona(),textures.getRedZona(),textures.getGreenZona(),textures.getGoldZona(),textures.getBlueZona());
                            grid[x][z].setBevel(true);
                            grid[x][z].setBevelAngle(180.0f);

                            v0 = new Vertex();
                            v1 = new Vertex();
                            v2 = new Vertex();

                            v0.setPosition(new Vector3f(leftPos.getX() + 0.5f, position.getY() + 0.5f, leftPos.getZ() + 0.5f));
                            v1.setPosition(new Vector3f(leftPos.getX() + 0.5f, position.getY() - 0.5f, leftPos.getZ() + 0.5f));
                            v2.setPosition(new Vector3f(leftPos.getX() + 0.5f, position.getY() - 0.5f, leftPos.getZ() - 0.5f));

                            v0.setNormal(new Vector3f(-1.0f, 0.0f, 0.0f));
                            v1.setNormal(new Vector3f(-1.0f, 0.0f, 0.0f));
                            v2.setNormal(new Vector3f(-1.0f, 0.0f, 0.0f));

                            /*if (currentArea == PLAIN_AREA) {
                                if (leftPos.getY() < -1.0f) {
                                    v0.setTexCoords(new Vector2f(0.0f, 1.0f / 4.0f));
                                    v1.setTexCoords(new Vector2f(0.0f, 0.0f));
                                    v2.setTexCoords(new Vector2f(1.0f / 4.0f, 0.0f));
                                } else {
                                    v0.setTexCoords(new Vector2f(0.0f, 2.0f / 4.0f));
                                    v1.setTexCoords(new Vector2f(0.0f, 1.0f / 4.0f));
                                    v2.setTexCoords(new Vector2f(1.0f / 4.0f, 1.0f / 4.0f));
                                }
                            } else if (currentArea == MOUNTAIN_AREA) {
                                if (position.getY() + 0.5f < -1.0f) {
                                    v0.setTexCoords(new Vector2f(1.0f / 4.0f + 0.005f, 1.0f / 4.0f - 0.005f));
                                    v1.setTexCoords(new Vector2f(1.0f / 4.0f + 0.005f, 0.0f + 0.005f));
                                    v2.setTexCoords(new Vector2f(2.0f / 4.0f - 0.005f, 0.0f + 0.005f));
                                } else if(position.getY() + 0.5f < 1.0f){
                                    v0.setTexCoords(new Vector2f(1.0f / 4.0f + 0.005f, 2.0f / 4.0f - 0.005f));
                                    v1.setTexCoords(new Vector2f(1.0f / 4.0f + 0.005f, 1.0f / 4.0f + 0.005f));
                                    v2.setTexCoords(new Vector2f(2.0f / 4.0f - 0.005f, 1.0f / 4.0f + 0.005f));
                                }else {
                                    v0.setTexCoords(new Vector2f(2.0f / 4.0f, 2.0f / 4.0f));
                                    v1.setTexCoords(new Vector2f(2.0f / 4.0f, 1.0f / 4.0f));
                                    v2.setTexCoords(new Vector2f(3.0f / 4.0f, 1.0f / 4.0f));
                                }
                            }*/

                            v0.setTexCoords(new Vector2f(0.0f, 0.0f));
                            v1.setTexCoords(new Vector2f(1.0f, 0.0f));
                            v2.setTexCoords(new Vector2f(1.0f, 1.0f));

                            v0.setTangents(new Vector3f(count,0.0f,0.0f));
                            v1.setTangents(new Vector3f(count,0.0f,0.0f));
                            v2.setTangents(new Vector3f(count,0.0f,0.0f));

                            vertices.add(v0);
                            vertices.add(v1);
                            vertices.add(v2);

                            indices.add(index++);
                            indices.add(index++);
                            indices.add(index++);

                            v0 = new Vertex();
                            v1 = new Vertex();
                            v2 = new Vertex();

                            v0.setPosition(new Vector3f(rightPos.getX() - 0.5f, position.getY() + 0.5f, rightPos.getZ() + 0.5f));
                            v1.setPosition(new Vector3f(rightPos.getX() - 0.5f, position.getY() - 0.5f, rightPos.getZ() - 0.5f));
                            v2.setPosition(new Vector3f(rightPos.getX() - 0.5f, position.getY() - 0.5f, rightPos.getZ() + 0.5f));

                            v0.setNormal(new Vector3f(1.0f, 0.0f, 0.0f));
                            v1.setNormal(new Vector3f(1.0f, 0.0f, 0.0f));
                            v2.setNormal(new Vector3f(1.0f, 0.0f, 0.0f));

                            /*if (currentArea == PLAIN_AREA) {
                                if (rightPos.getY() < -1.0f) {
                                    v0.setTexCoords(new Vector2f(1.0f / 4.0f, 1.0f / 4.0f));
                                    v1.setTexCoords(new Vector2f(0.0f, 0.0f));
                                    v2.setTexCoords(new Vector2f(1.0f / 4.0f, 0.0f));
                                } else {
                                    v0.setTexCoords(new Vector2f(1.0f / 4.0f, 2.0f / 4.0f));
                                    v1.setTexCoords(new Vector2f(0.0f, 1.0f / 4.0f));
                                    v2.setTexCoords(new Vector2f(1.0f / 4.0f, 1.0f / 4.0f));
                                }
                            } else if (currentArea == MOUNTAIN_AREA) {
                                if (position.getY() + 0.5f < -1.0f) {
                                    v0.setTexCoords(new Vector2f(2.0f / 4.0f - 0.005f, 1.0f / 4.0f - 0.005f));
                                    v1.setTexCoords(new Vector2f(1.0f / 4.0f + 0.005f, 0.0f + 0.005f));
                                    v2.setTexCoords(new Vector2f(2.0f / 4.0f - 0.005f, 0.0f + 0.005f));
                                } else if(position.getY() + 0.5f < 1.0f){
                                    v0.setTexCoords(new Vector2f(2.0f / 4.0f - 0.005f, 2.0f / 4.0f - 0.005f));
                                    v1.setTexCoords(new Vector2f(1.0f / 4.0f + 0.005f, 1.0f / 4.0f + 0.005f));
                                    v2.setTexCoords(new Vector2f(2.0f / 4.0f - 0.005f, 1.0f / 4.0f + 0.005f));
                                }else {
                                    v0.setTexCoords(new Vector2f(3.0f / 4.0f, 2.0f / 4.0f));
                                    v1.setTexCoords(new Vector2f(2.0f / 4.0f, 1.0f / 4.0f));
                                    v2.setTexCoords(new Vector2f(3.0f / 4.0f, 1.0f / 4.0f));
                                }
                            }*/

                            v0.setTexCoords(new Vector2f(0.0f, 0.0f));
                            v1.setTexCoords(new Vector2f(1.0f, 0.0f));
                            v2.setTexCoords(new Vector2f(1.0f, 1.0f));

                            v0.setTangents(new Vector3f(count,0.0f,0.0f));
                            v1.setTangents(new Vector3f(count,0.0f,0.0f));
                            v2.setTangents(new Vector3f(count,0.0f,0.0f));

                            vertices.add(v0);
                            vertices.add(v1);
                            vertices.add(v2);

                            indices.add(index++);
                            indices.add(index++);
                            indices.add(index++);
                        }

                        if (currentHeight + 0.5 == rightHeight && currentHeight - 0.5f == leftHeight && currentHeight - 0.5f == upHeight && currentHeight - 0.5f == downHeight) { // LEFT
                            check = true;
                            Vertex v0 = new Vertex();
                            Vertex v1 = new Vertex();
                            Vertex v2 = new Vertex();
                            Vertex v3 = new Vertex();

                            v0.setPosition(new Vector3f(position.getX() + 0.5f, position.getY() + 0.5f, position.getZ() + 0.5f));
                            v1.setPosition(new Vector3f(position.getX() - 0.5f, position.getY() - 0.5f, position.getZ() + 0.5f));
                            v2.setPosition(new Vector3f(position.getX() - 0.5f, position.getY() - 0.5f, position.getZ() - 0.5f));
                            v3.setPosition(new Vector3f(position.getX() + 0.5f, position.getY() + 0.5f, position.getZ() - 0.5f));

                            v0.setNormal(new Vector3f(-0.5f, 0.5f, 0.0f));
                            v1.setNormal(new Vector3f(-0.5f, 0.5f, 0.0f));
                            v2.setNormal(new Vector3f(-0.5f, 0.5f, 0.0f));
                            v3.setNormal(new Vector3f(-0.5f, 0.5f, 0.0f));

                            setUpperBevelPlaneTexCoord(currentArea,currentHeight,v0,v1,v2,v3);
                            tangentBitangent(v0,v1,v2,v3);

                            ArrayList<Vertex> cellVertices = new ArrayList<>();
                            vertices.add(v0);
                            Vertex v = new Vertex(v0);
                            v.setTexCoords(new Vector2f(0.0f,1.0f));
                            cellVertices.add(v);
                            vertices.add(v1);
                            v = new Vertex(v1);
                            v.setTexCoords(new Vector2f(0.0f,0.0f));
                            cellVertices.add(v);
                            vertices.add(v2);
                            v = new Vertex(v2);
                            v.setTexCoords(new Vector2f(1.0f,0.0f));
                            cellVertices.add(v);
                            vertices.add(v3);
                            v = new Vertex(v3);
                            v.setTexCoords(new Vector2f(1.0f,1.0f));
                            cellVertices.add(v);

                            indices.add(index++);
                            indices.add(index++);
                            indices.add(index);
                            index -= 2;
                            indices.add(index);
                            index += 2;
                            indices.add(index++);
                            indices.add(index++);
                            ArrayList<Integer> cellIndices = new ArrayList<>(Arrays.asList(0,1,2,0,2,3));
                            Mesh cellMesh = new Mesh(cellVertices,cellIndices,null);
                            grid[x][z].setupMesh(cellMesh);
                            grid[x][z].setupTexture(textures.getCursor(),textures.getGrayZona(),textures.getRedZona(),textures.getGreenZona(),textures.getGoldZona(),textures.getBlueZona());
                            grid[x][z].setBevel(true);
                            grid[x][z].setBevelAngle(270.0f);

                            v0 = new Vertex();
                            v1 = new Vertex();
                            v2 = new Vertex();

                            v0.setPosition(new Vector3f(upPos.getX() + 0.5f, position.getY() - 0.5f, upPos.getZ() - 0.5f));
                            v1.setPosition(new Vector3f(upPos.getX() - 0.5f, position.getY() - 0.5f, upPos.getZ() - 0.5f));
                            v2.setPosition(new Vector3f(upPos.getX() + 0.5f, position.getY() + 0.5f, upPos.getZ() - 0.5f));

                            v0.setNormal(new Vector3f(0.0f, 0.0f, 1.0f));
                            v1.setNormal(new Vector3f(0.0f, 0.0f, 1.0f));
                            v2.setNormal(new Vector3f(0.0f, 0.0f, 1.0f));

                            /*if (currentArea == PLAIN_AREA) {
                                if (upPos.getY() < -1.0f) {
                                    v0.setTexCoords(new Vector2f(1.0f / 4.0f, 0.0f));
                                    v1.setTexCoords(new Vector2f(0.0f, 0.0f));
                                    v2.setTexCoords(new Vector2f(1.0f / 4.0f, 1.0f / 4.0f));
                                } else {
                                    v0.setTexCoords(new Vector2f(1.0f / 4.0f, 1.0f / 4.0f));
                                    v1.setTexCoords(new Vector2f(0.0f, 1.0f / 4.0f));
                                    v2.setTexCoords(new Vector2f(1.0f / 4.0f, 2.0f / 4.0f));
                                }
                            } else if (currentArea == MOUNTAIN_AREA) {
                                if (position.getY() + 0.5f < -1.0f) {
                                    v0.setTexCoords(new Vector2f(2.0f / 4.0f - 0.005f, 0.0f + 0.005f));
                                    v1.setTexCoords(new Vector2f(1.0f / 4.0f + 0.005f, 0.0f + 0.005f));
                                    v2.setTexCoords(new Vector2f(2.0f / 4.0f - 0.005f, 1.0f / 4.0f - 0.005f));
                                } else if(position.getY() + 0.5f < 1.0f){
                                    v0.setTexCoords(new Vector2f(2.0f / 4.0f - 0.005f, 1.0f / 4.0f + 0.005f));
                                    v1.setTexCoords(new Vector2f(1.0f / 4.0f + 0.005f, 1.0f / 4.0f + 0.005f));
                                    v2.setTexCoords(new Vector2f(2.0f / 4.0f - 0.005f, 2.0f / 4.0f - 0.005f));
                                }else {
                                    v0.setTexCoords(new Vector2f(3.0f / 4.0f, 1.0f / 4.0f));
                                    v1.setTexCoords(new Vector2f(2.0f / 4.0f, 1.0f / 4.0f));
                                    v2.setTexCoords(new Vector2f(3.0f / 4.0f, 2.0f / 4.0f));
                                }
                            }*/

                            v0.setTexCoords(new Vector2f(0.0f, 0.0f));
                            v1.setTexCoords(new Vector2f(1.0f, 0.0f));
                            v2.setTexCoords(new Vector2f(1.0f, 1.0f));

                            v0.setTangents(new Vector3f(count,0.0f,0.0f));
                            v1.setTangents(new Vector3f(count,0.0f,0.0f));
                            v2.setTangents(new Vector3f(count,0.0f,0.0f));

                            vertices.add(v0);
                            vertices.add(v1);
                            vertices.add(v2);

                            indices.add(index++);
                            indices.add(index++);
                            indices.add(index++);

                            v0 = new Vertex();
                            v1 = new Vertex();
                            v2 = new Vertex();

                            v0.setPosition(new Vector3f(downPos.getX() + 0.5f, position.getY() + 0.5f, downPos.getZ() + 0.5f));
                            v1.setPosition(new Vector3f(downPos.getX() - 0.5f, position.getY() - 0.5f, downPos.getZ() + 0.5f));
                            v2.setPosition(new Vector3f(downPos.getX() + 0.5f, position.getY() - 0.5f, downPos.getZ() + 0.5f));

                            v0.setNormal(new Vector3f(0.0f, 0.0f, -1.0f));
                            v1.setNormal(new Vector3f(0.0f, 0.0f, -1.0f));
                            v2.setNormal(new Vector3f(0.0f, 0.0f, -1.0f));

                            /*if (currentArea == PLAIN_AREA) {
                                if (downPos.getY() < -1.0f) {
                                    v0.setTexCoords(new Vector2f(1.0f / 4.0f, 1.0f / 4.0f));
                                    v1.setTexCoords(new Vector2f(0.0f, 0.0f));
                                    v2.setTexCoords(new Vector2f(1.0f / 4.0f, 0.0f));
                                } else {
                                    v0.setTexCoords(new Vector2f(1.0f / 4.0f, 2.0f / 4.0f));
                                    v1.setTexCoords(new Vector2f(0.0f, 1.0f / 4.0f));
                                    v2.setTexCoords(new Vector2f(1.0f / 4.0f, 1.0f / 4.0f));
                                }
                            } else if (currentArea == MOUNTAIN_AREA) {
                                if (position.getY() + 0.5f < -1.0f) {
                                    v0.setTexCoords(new Vector2f(2.0f / 4.0f - 0.005f, 1.0f / 4.0f - 0.005f));
                                    v1.setTexCoords(new Vector2f(1.0f / 4.0f + 0.005f, 0.0f + 0.005f));
                                    v2.setTexCoords(new Vector2f(2.0f / 4.0f - 0.005f, 0.0f + 0.005f));
                                } else if(position.getY() + 0.5f < 1.0f){
                                    v0.setTexCoords(new Vector2f(2.0f / 4.0f - 0.005f, 2.0f / 4.0f - 0.005f));
                                    v1.setTexCoords(new Vector2f(1.0f / 4.0f + 0.005f, 1.0f / 4.0f + 0.005f));
                                    v2.setTexCoords(new Vector2f(2.0f / 4.0f - 0.005f, 1.0f / 4.0f + 0.005f));
                                }else {
                                    v0.setTexCoords(new Vector2f(3.0f / 4.0f, 2.0f / 4.0f));
                                    v1.setTexCoords(new Vector2f(2.0f / 4.0f, 1.0f / 4.0f));
                                    v2.setTexCoords(new Vector2f(3.0f / 4.0f, 1.0f / 4.0f));
                                }
                            }*/

                            v0.setTexCoords(new Vector2f(0.0f, 0.0f));
                            v1.setTexCoords(new Vector2f(1.0f, 0.0f));
                            v2.setTexCoords(new Vector2f(1.0f, 1.0f));

                            v0.setTangents(new Vector3f(count,0.0f,0.0f));
                            v1.setTangents(new Vector3f(count,0.0f,0.0f));
                            v2.setTangents(new Vector3f(count,0.0f,0.0f));

                            vertices.add(v0);
                            vertices.add(v1);
                            vertices.add(v2);

                            indices.add(index++);
                            indices.add(index++);
                            indices.add(index++);
                        } else if (currentHeight + 0.5f == rightHeight && currentHeight - 0.5f == leftHeight && currentHeight + 0.5f == upHeight && currentHeight + 0.5f == downHeight) { // LEFT
                            check = true;
                            Vertex v0 = new Vertex();
                            Vertex v1 = new Vertex();
                            Vertex v2 = new Vertex();
                            Vertex v3 = new Vertex();

                            v0.setPosition(new Vector3f(position.getX() + 0.5f, position.getY() + 0.5f, position.getZ() + 0.5f));
                            v1.setPosition(new Vector3f(position.getX() - 0.5f, position.getY() - 0.5f, position.getZ() + 0.5f));
                            v2.setPosition(new Vector3f(position.getX() - 0.5f, position.getY() - 0.5f, position.getZ() - 0.5f));
                            v3.setPosition(new Vector3f(position.getX() + 0.5f, position.getY() + 0.5f, position.getZ() - 0.5f));

                            v0.setNormal(new Vector3f(-0.5f, 0.5f, 0.0f));
                            v1.setNormal(new Vector3f(-0.5f, 0.5f, 0.0f));
                            v2.setNormal(new Vector3f(-0.5f, 0.5f, 0.0f));
                            v3.setNormal(new Vector3f(-0.5f, 0.5f, 0.0f));

                            setUpperBevelPlaneTexCoord(currentArea,currentHeight,v0,v1,v2,v3);
                            tangentBitangent(v0,v1,v2,v3);

                            ArrayList<Vertex> cellVertices = new ArrayList<>();
                            vertices.add(v0);
                            Vertex v = new Vertex(v0);
                            v.setTexCoords(new Vector2f(0.0f,1.0f));
                            cellVertices.add(v);
                            vertices.add(v1);
                            v = new Vertex(v1);
                            v.setTexCoords(new Vector2f(0.0f,0.0f));
                            cellVertices.add(v);
                            vertices.add(v2);
                            v = new Vertex(v2);
                            v.setTexCoords(new Vector2f(1.0f,0.0f));
                            cellVertices.add(v);
                            vertices.add(v3);
                            v = new Vertex(v3);
                            v.setTexCoords(new Vector2f(1.0f,1.0f));
                            cellVertices.add(v);

                            indices.add(index++);
                            indices.add(index++);
                            indices.add(index);
                            index -= 2;
                            indices.add(index);
                            index += 2;
                            indices.add(index++);
                            indices.add(index++);
                            ArrayList<Integer> cellIndices = new ArrayList<>(Arrays.asList(0,1,2,0,2,3));
                            Mesh cellMesh = new Mesh(cellVertices,cellIndices,null);
                            grid[x][z].setupMesh(cellMesh);
                            grid[x][z].setupTexture(textures.getCursor(),textures.getGrayZona(),textures.getRedZona(),textures.getGreenZona(),textures.getGoldZona(),textures.getBlueZona());
                            grid[x][z].setBevel(true);
                            grid[x][z].setBevelAngle(270.0f);

                            v0 = new Vertex();
                            v1 = new Vertex();
                            v2 = new Vertex();

                            v0.setPosition(new Vector3f(upPos.getX() + 0.5f, position.getY() - 0.5f, upPos.getZ() - 0.5f));
                            v1.setPosition(new Vector3f(upPos.getX() - 0.5f, position.getY() - 0.5f, upPos.getZ() - 0.5f));
                            v2.setPosition(new Vector3f(upPos.getX() + 0.5f, position.getY() + 0.5f, upPos.getZ() - 0.5f));

                            v0.setNormal(new Vector3f(0.0f, 0.0f, 1.0f));
                            v1.setNormal(new Vector3f(0.0f, 0.0f, 1.0f));
                            v2.setNormal(new Vector3f(0.0f, 0.0f, 1.0f));

                            /*if (currentArea == PLAIN_AREA) {
                                if (upPos.getY() < -1.0f) {
                                    v0.setTexCoords(new Vector2f(1.0f / 4.0f, 0.0f));
                                    v1.setTexCoords(new Vector2f(0.0f, 0.0f));
                                    v2.setTexCoords(new Vector2f(1.0f / 4.0f, 1.0f / 4.0f));
                                } else {
                                    v0.setTexCoords(new Vector2f(1.0f / 4.0f, 1.0f / 4.0f));
                                    v1.setTexCoords(new Vector2f(0.0f, 1.0f / 4.0f));
                                    v2.setTexCoords(new Vector2f(1.0f / 4.0f, 2.0f / 4.0f));
                                }
                            } else if (currentArea == MOUNTAIN_AREA) {
                                if (position.getY() + 0.5f < -1.0f) {
                                    v0.setTexCoords(new Vector2f(2.0f / 4.0f - 0.005f, 0.0f + 0.005f));
                                    v1.setTexCoords(new Vector2f(1.0f / 4.0f + 0.005f, 0.0f + 0.005f));
                                    v2.setTexCoords(new Vector2f(2.0f / 4.0f - 0.005f, 1.0f / 4.0f - 0.005f));
                                } else if(position.getY() + 0.5f < 1.0f){
                                    v0.setTexCoords(new Vector2f(2.0f / 4.0f - 0.005f, 1.0f / 4.0f + 0.005f));
                                    v1.setTexCoords(new Vector2f(1.0f / 4.0f + 0.005f, 1.0f / 4.0f + 0.005f));
                                    v2.setTexCoords(new Vector2f(2.0f / 4.0f - 0.005f, 2.0f / 4.0f - 0.005f));
                                }else {
                                    v0.setTexCoords(new Vector2f(3.0f / 4.0f, 1.0f / 4.0f));
                                    v1.setTexCoords(new Vector2f(2.0f / 4.0f, 1.0f / 4.0f));
                                    v2.setTexCoords(new Vector2f(3.0f / 4.0f, 2.0f / 4.0f));
                                }
                            }*/

                            v0.setTexCoords(new Vector2f(0.0f, 0.0f));
                            v1.setTexCoords(new Vector2f(1.0f, 0.0f));
                            v2.setTexCoords(new Vector2f(1.0f, 1.0f));

                            v0.setTangents(new Vector3f(count,0.0f,0.0f));
                            v1.setTangents(new Vector3f(count,0.0f,0.0f));
                            v2.setTangents(new Vector3f(count,0.0f,0.0f));

                            vertices.add(v0);
                            vertices.add(v1);
                            vertices.add(v2);

                            indices.add(index++);
                            indices.add(index++);
                            indices.add(index++);

                            v0 = new Vertex();
                            v1 = new Vertex();
                            v2 = new Vertex();

                            v0.setPosition(new Vector3f(downPos.getX() + 0.5f, position.getY() + 0.5f, downPos.getZ() + 0.5f));
                            v1.setPosition(new Vector3f(downPos.getX() - 0.5f, position.getY() - 0.5f, downPos.getZ() + 0.5f));
                            v2.setPosition(new Vector3f(downPos.getX() + 0.5f, position.getY() - 0.5f, downPos.getZ() + 0.5f));

                            v0.setNormal(new Vector3f(0.0f, 0.0f, -1.0f));
                            v1.setNormal(new Vector3f(0.0f, 0.0f, -1.0f));
                            v2.setNormal(new Vector3f(0.0f, 0.0f, -1.0f));

                            /*if (currentArea == PLAIN_AREA) {
                                if (downPos.getY() < -1.0f) {
                                    v0.setTexCoords(new Vector2f(1.0f / 4.0f, 1.0f / 4.0f));
                                    v1.setTexCoords(new Vector2f(0.0f, 0.0f));
                                    v2.setTexCoords(new Vector2f(1.0f / 4.0f, 0.0f));
                                } else {
                                    v0.setTexCoords(new Vector2f(1.0f / 4.0f, 2.0f / 4.0f));
                                    v1.setTexCoords(new Vector2f(0.0f, 1.0f / 4.0f));
                                    v2.setTexCoords(new Vector2f(1.0f / 4.0f, 1.0f / 4.0f));
                                }
                            } else if (currentArea == MOUNTAIN_AREA) {
                                if (position.getY() + 0.5f < -1.0f) {
                                    v0.setTexCoords(new Vector2f(2.0f / 4.0f - 0.005f, 1.0f / 4.0f - 0.005f));
                                    v1.setTexCoords(new Vector2f(1.0f / 4.0f + 0.005f, 0.0f + 0.005f));
                                    v2.setTexCoords(new Vector2f(2.0f / 4.0f - 0.005f, 0.0f + 0.005f));
                                } else if(position.getY() + 0.5f < 1.0f){
                                    v0.setTexCoords(new Vector2f(2.0f / 4.0f - 0.005f, 2.0f / 4.0f - 0.005f));
                                    v1.setTexCoords(new Vector2f(1.0f / 4.0f + 0.005f, 1.0f / 4.0f + 0.005f));
                                    v2.setTexCoords(new Vector2f(2.0f / 4.0f - 0.005f, 1.0f / 4.0f + 0.005f));
                                }else {
                                    v0.setTexCoords(new Vector2f(3.0f / 4.0f, 2.0f / 4.0f));
                                    v1.setTexCoords(new Vector2f(2.0f / 4.0f, 1.0f / 4.0f));
                                    v2.setTexCoords(new Vector2f(3.0f / 4.0f, 1.0f / 4.0f));
                                }
                            }*/

                            v0.setTexCoords(new Vector2f(0.0f, 0.0f));
                            v1.setTexCoords(new Vector2f(1.0f, 0.0f));
                            v2.setTexCoords(new Vector2f(1.0f, 1.0f));

                            v0.setTangents(new Vector3f(count,0.0f,0.0f));
                            v1.setTangents(new Vector3f(count,0.0f,0.0f));
                            v2.setTangents(new Vector3f(count,0.0f,0.0f));

                            vertices.add(v0);
                            vertices.add(v1);
                            vertices.add(v2);

                            indices.add(index++);
                            indices.add(index++);
                            indices.add(index++);
                        }

                        if (currentHeight + 0.5 == downHeight && currentHeight - 0.5f == leftHeight && currentHeight - 0.5f == rightHeight && currentHeight - 0.5f == upHeight) { // UP
                            check = true;
                            Vertex v0 = new Vertex();
                            Vertex v1 = new Vertex();
                            Vertex v2 = new Vertex();
                            Vertex v3 = new Vertex();

                            v0.setPosition(new Vector3f(position.getX() + 0.5f, position.getY() + 0.5f, position.getZ() - 0.5f));
                            v1.setPosition(new Vector3f(position.getX() + 0.5f, position.getY() - 0.5f, position.getZ() + 0.5f));
                            v2.setPosition(new Vector3f(position.getX() - 0.5f, position.getY() - 0.5f, position.getZ() + 0.5f));
                            v3.setPosition(new Vector3f(position.getX() - 0.5f, position.getY() + 0.5f, position.getZ() - 0.5f));

                            v0.setNormal(new Vector3f(0.0f, 0.5f, 0.5f));
                            v1.setNormal(new Vector3f(0.0f, 0.5f, 0.5f));
                            v2.setNormal(new Vector3f(0.0f, 0.5f, 0.5f));
                            v3.setNormal(new Vector3f(0.0f, 0.5f, 0.5f));

                            setUpperBevelPlaneTexCoord(currentArea,currentHeight,v0,v1,v2,v3);
                            tangentBitangent(v0,v1,v2,v3);

                            ArrayList<Vertex> cellVertices = new ArrayList<>();
                            vertices.add(v0);
                            Vertex v = new Vertex(v0);
                            v.setTexCoords(new Vector2f(0.0f,1.0f));
                            cellVertices.add(v);
                            vertices.add(v1);
                            v = new Vertex(v1);
                            v.setTexCoords(new Vector2f(0.0f,0.0f));
                            cellVertices.add(v);
                            vertices.add(v2);
                            v = new Vertex(v2);
                            v.setTexCoords(new Vector2f(1.0f,0.0f));
                            cellVertices.add(v);
                            vertices.add(v3);
                            v = new Vertex(v3);
                            v.setTexCoords(new Vector2f(1.0f,1.0f));
                            cellVertices.add(v);

                            indices.add(index++);
                            indices.add(index++);
                            indices.add(index);
                            index -= 2;
                            indices.add(index);
                            index += 2;
                            indices.add(index++);
                            indices.add(index++);
                            ArrayList<Integer> cellIndices = new ArrayList<>(Arrays.asList(0,1,2,0,2,3));
                            Mesh cellMesh = new Mesh(cellVertices,cellIndices,null);
                            grid[x][z].setupMesh(cellMesh);
                            grid[x][z].setupTexture(textures.getCursor(),textures.getGrayZona(),textures.getRedZona(),textures.getGreenZona(),textures.getGoldZona(),textures.getBlueZona());
                            grid[x][z].setBevel(true);
                            grid[x][z].setBevelAngle(0.0f);

                            v0 = new Vertex();
                            v1 = new Vertex();
                            v2 = new Vertex();

                            v0.setPosition(new Vector3f(leftPos.getX() + 0.5f, position.getY() + 0.5f, leftPos.getZ() - 0.5f));
                            v1.setPosition(new Vector3f(leftPos.getX() + 0.5f, position.getY() - 0.5f, leftPos.getZ() + 0.5f));
                            v2.setPosition(new Vector3f(leftPos.getX() + 0.5f, position.getY() - 0.5f, leftPos.getZ() - 0.5f));

                            v0.setNormal(new Vector3f(-1.0f, 0.0f, 0.0f));
                            v1.setNormal(new Vector3f(-1.0f, 0.0f, 0.0f));
                            v2.setNormal(new Vector3f(-1.0f, 0.0f, 0.0f));

                            /*if (currentArea == PLAIN_AREA) {
                                if (leftPos.getY() < -1.0f) {
                                    v0.setTexCoords(new Vector2f(1.0f / 4.0f, 1.0f / 4.0f));
                                    v1.setTexCoords(new Vector2f(0.0f, 0.0f));
                                    v2.setTexCoords(new Vector2f(1.0f / 4.0f, 0.0f));
                                } else {
                                    v0.setTexCoords(new Vector2f(1.0f / 4.0f, 2.0f / 4.0f));
                                    v1.setTexCoords(new Vector2f(0.0f, 1.0f / 4.0f));
                                    v2.setTexCoords(new Vector2f(1.0f / 4.0f, 1.0f / 4.0f));
                                }
                            } else if (currentArea == MOUNTAIN_AREA) {
                                if (position.getY() + 0.5f < -1.0f) {
                                    v0.setTexCoords(new Vector2f(2.0f / 4.0f - 0.005f, 1.0f / 4.0f - 0.005f));
                                    v1.setTexCoords(new Vector2f(1.0f / 4.0f + 0.005f, 0.0f + 0.005f));
                                    v2.setTexCoords(new Vector2f(2.0f / 4.0f - 0.005f, 0.0f + 0.005f));
                                }else if (position.getY() + 0.5f < 1.0f) {
                                    v0.setTexCoords(new Vector2f(2.0f / 4.0f - 0.005f, 2.0f / 4.0f - 0.005f));
                                    v1.setTexCoords(new Vector2f(1.0f / 4.0f + 0.005f, 1.0f / 4.0f + 0.005f));
                                    v2.setTexCoords(new Vector2f(2.0f / 4.0f - 0.005f, 1.0f / 4.0f + 0.005f));
                                }else {
                                    v0.setTexCoords(new Vector2f(3.0f / 4.0f, 2.0f / 4.0f));
                                    v1.setTexCoords(new Vector2f(2.0f / 4.0f, 1.0f / 4.0f));
                                    v2.setTexCoords(new Vector2f(3.0f / 4.0f, 1.0f / 4.0f));
                                }
                            }*/

                            v0.setTexCoords(new Vector2f(0.0f, 0.0f));
                            v1.setTexCoords(new Vector2f(1.0f, 0.0f));
                            v2.setTexCoords(new Vector2f(1.0f, 1.0f));

                            v0.setTangents(new Vector3f(count,0.0f,0.0f));
                            v1.setTangents(new Vector3f(count,0.0f,0.0f));
                            v2.setTangents(new Vector3f(count,0.0f,0.0f));

                            vertices.add(v0);
                            vertices.add(v1);
                            vertices.add(v2);

                            indices.add(index++);
                            indices.add(index++);
                            indices.add(index++);

                            v0 = new Vertex();
                            v1 = new Vertex();
                            v2 = new Vertex();

                            v0.setPosition(new Vector3f(rightPos.getX() - 0.5f, position.getY() + 0.5f, rightPos.getZ() - 0.5f));
                            v1.setPosition(new Vector3f(rightPos.getX() - 0.5f, position.getY() - 0.5f, rightPos.getZ() - 0.5f));
                            v2.setPosition(new Vector3f(rightPos.getX() - 0.5f, position.getY() - 0.5f, rightPos.getZ() + 0.5f));

                            v0.setNormal(new Vector3f(1.0f, 0.0f, 0.0f));
                            v1.setNormal(new Vector3f(1.0f, 0.0f, 0.0f));
                            v2.setNormal(new Vector3f(1.0f, 0.0f, 0.0f));

                            /*if (currentArea == PLAIN_AREA) {
                                if (rightPos.getY() < -1.0f) {
                                    v0.setTexCoords(new Vector2f(0.0f, 1.0f / 4.0f));
                                    v1.setTexCoords(new Vector2f(0.0f, 0.0f));
                                    v2.setTexCoords(new Vector2f(1.0f / 4.0f, 0.0f));
                                } else {
                                    v0.setTexCoords(new Vector2f(0.0f, 2.0f / 4.0f));
                                    v1.setTexCoords(new Vector2f(0.0f, 1.0f / 4.0f));
                                    v2.setTexCoords(new Vector2f(1.0f / 4.0f, 1.0f / 4.0f));
                                }
                            } else if (currentArea == MOUNTAIN_AREA) {
                                if (position.getY() + 0.5f < -1.0f) {
                                    v0.setTexCoords(new Vector2f(1.0f / 4.0f + 0.005f, 1.0f / 4.0f - 0.005f));
                                    v1.setTexCoords(new Vector2f(1.0f / 4.0f + 0.005f, 0.0f + 0.005f));
                                    v2.setTexCoords(new Vector2f(2.0f / 4.0f - 0.005f, 0.0f + 0.005f));
                                }else if (position.getY() + 0.5f < 1.0f) {
                                    v0.setTexCoords(new Vector2f(1.0f / 4.0f + 0.005f, 2.0f / 4.0f - 0.005f));
                                    v1.setTexCoords(new Vector2f(1.0f / 4.0f + 0.005f, 1.0f / 4.0f + 0.005f));
                                    v2.setTexCoords(new Vector2f(2.0f / 4.0f - 0.005f, 1.0f / 4.0f + 0.005f));
                                }else {
                                    v0.setTexCoords(new Vector2f(2.0f / 4.0f, 2.0f / 4.0f));
                                    v1.setTexCoords(new Vector2f(2.0f / 4.0f, 1.0f / 4.0f));
                                    v2.setTexCoords(new Vector2f(3.0f / 4.0f, 1.0f / 4.0f));
                                }
                            }*/

                            v0.setTexCoords(new Vector2f(0.0f, 0.0f));
                            v1.setTexCoords(new Vector2f(1.0f, 0.0f));
                            v2.setTexCoords(new Vector2f(1.0f, 1.0f));

                            v0.setTangents(new Vector3f(count,0.0f,0.0f));
                            v1.setTangents(new Vector3f(count,0.0f,0.0f));
                            v2.setTangents(new Vector3f(count,0.0f,0.0f));

                            vertices.add(v0);
                            vertices.add(v1);
                            vertices.add(v2);

                            indices.add(index++);
                            indices.add(index++);
                            indices.add(index++);
                        } else if (currentHeight + 0.5f == downHeight && currentHeight + 0.5f == leftHeight && currentHeight + 0.5f == rightHeight && currentHeight - 0.5f == upHeight) { // UP
                            check = true;
                            Vertex v0 = new Vertex();
                            Vertex v1 = new Vertex();
                            Vertex v2 = new Vertex();
                            Vertex v3 = new Vertex();

                            v0.setPosition(new Vector3f(position.getX() + 0.5f, position.getY() + 0.5f, position.getZ() - 0.5f));
                            v1.setPosition(new Vector3f(position.getX() + 0.5f, position.getY() - 0.5f, position.getZ() + 0.5f));
                            v2.setPosition(new Vector3f(position.getX() - 0.5f, position.getY() - 0.5f, position.getZ() + 0.5f));
                            v3.setPosition(new Vector3f(position.getX() - 0.5f, position.getY() + 0.5f, position.getZ() - 0.5f));

                            v0.setNormal(new Vector3f(0.0f, 0.5f, 0.5f));
                            v1.setNormal(new Vector3f(0.0f, 0.5f, 0.5f));
                            v2.setNormal(new Vector3f(0.0f, 0.5f, 0.5f));
                            v3.setNormal(new Vector3f(0.0f, 0.5f, 0.5f));

                            setUpperBevelPlaneTexCoord(currentArea,currentHeight,v0,v1,v2,v3);
                            tangentBitangent(v0,v1,v2,v3);

                            ArrayList<Vertex> cellVertices = new ArrayList<>();
                            vertices.add(v0);
                            Vertex v = new Vertex(v0);
                            v.setTexCoords(new Vector2f(0.0f,1.0f));
                            cellVertices.add(v);
                            vertices.add(v1);
                            v = new Vertex(v1);
                            v.setTexCoords(new Vector2f(0.0f,0.0f));
                            cellVertices.add(v);
                            vertices.add(v2);
                            v = new Vertex(v2);
                            v.setTexCoords(new Vector2f(1.0f,0.0f));
                            cellVertices.add(v);
                            vertices.add(v3);
                            v = new Vertex(v3);
                            v.setTexCoords(new Vector2f(1.0f,1.0f));
                            cellVertices.add(v);

                            indices.add(index++);
                            indices.add(index++);
                            indices.add(index);
                            index -= 2;
                            indices.add(index);
                            index += 2;
                            indices.add(index++);
                            indices.add(index++);
                            ArrayList<Integer> cellIndices = new ArrayList<>(Arrays.asList(0,1,2,0,2,3));
                            Mesh cellMesh = new Mesh(cellVertices,cellIndices,null);
                            grid[x][z].setupMesh(cellMesh);
                            grid[x][z].setupTexture(textures.getCursor(),textures.getGrayZona(),textures.getRedZona(),textures.getGreenZona(),textures.getGoldZona(),textures.getBlueZona());
                            grid[x][z].setBevel(true);
                            grid[x][z].setBevelAngle(0.0f);

                            v0 = new Vertex();
                            v1 = new Vertex();
                            v2 = new Vertex();

                            v0.setPosition(new Vector3f(leftPos.getX() + 0.5f, position.getY() + 0.5f, leftPos.getZ() - 0.5f));
                            v1.setPosition(new Vector3f(leftPos.getX() + 0.5f, position.getY() - 0.5f, leftPos.getZ() + 0.5f));
                            v2.setPosition(new Vector3f(leftPos.getX() + 0.5f, position.getY() - 0.5f, leftPos.getZ() - 0.5f));

                            v0.setNormal(new Vector3f(-1.0f, 0.0f, 0.0f));
                            v1.setNormal(new Vector3f(-1.0f, 0.0f, 0.0f));
                            v2.setNormal(new Vector3f(-1.0f, 0.0f, 0.0f));

                           /* if (currentArea == PLAIN_AREA) {
                                if (leftPos.getY() < -1.0f) {
                                    v0.setTexCoords(new Vector2f(1.0f / 4.0f, 1.0f / 4.0f));
                                    v1.setTexCoords(new Vector2f(0.0f, 0.0f));
                                    v2.setTexCoords(new Vector2f(1.0f / 4.0f, 0.0f));
                                } else {
                                    v0.setTexCoords(new Vector2f(1.0f / 4.0f, 2.0f / 4.0f));
                                    v1.setTexCoords(new Vector2f(0.0f, 1.0f / 4.0f));
                                    v2.setTexCoords(new Vector2f(1.0f / 4.0f, 1.0f / 4.0f));
                                }
                            } else if (currentArea == MOUNTAIN_AREA) {
                                if (position.getY() + 0.5f < -1.0f) {
                                    v0.setTexCoords(new Vector2f(2.0f / 4.0f - 0.005f, 1.0f / 4.0f - 0.005f));
                                    v1.setTexCoords(new Vector2f(1.0f / 4.0f + 0.005f, 0.0f + 0.005f));
                                    v2.setTexCoords(new Vector2f(2.0f / 4.0f - 0.005f, 0.0f + 0.005f));
                                }else if (position.getY() + 0.5f < 1.0f) {
                                    v0.setTexCoords(new Vector2f(2.0f / 4.0f - 0.005f, 2.0f / 4.0f - 0.005f));
                                    v1.setTexCoords(new Vector2f(1.0f / 4.0f + 0.005f, 1.0f / 4.0f + 0.005f));
                                    v2.setTexCoords(new Vector2f(2.0f / 4.0f - 0.005f, 1.0f / 4.0f + 0.005f));
                                }else {
                                    v0.setTexCoords(new Vector2f(3.0f / 4.0f, 2.0f / 4.0f));
                                    v1.setTexCoords(new Vector2f(2.0f / 4.0f, 1.0f / 4.0f));
                                    v2.setTexCoords(new Vector2f(3.0f / 4.0f, 1.0f / 4.0f));
                                }
                            }*/

                            v0.setTexCoords(new Vector2f(0.0f, 0.0f));
                            v1.setTexCoords(new Vector2f(1.0f, 0.0f));
                            v2.setTexCoords(new Vector2f(1.0f, 1.0f));

                            v0.setTangents(new Vector3f(count,0.0f,0.0f));
                            v1.setTangents(new Vector3f(count,0.0f,0.0f));
                            v2.setTangents(new Vector3f(count,0.0f,0.0f));

                            vertices.add(v0);
                            vertices.add(v1);
                            vertices.add(v2);

                            indices.add(index++);
                            indices.add(index++);
                            indices.add(index++);

                            v0 = new Vertex();
                            v1 = new Vertex();
                            v2 = new Vertex();

                            v0.setPosition(new Vector3f(rightPos.getX() - 0.5f, position.getY() + 0.5f, rightPos.getZ() - 0.5f));
                            v1.setPosition(new Vector3f(rightPos.getX() - 0.5f, position.getY() - 0.5f, rightPos.getZ() - 0.5f));
                            v2.setPosition(new Vector3f(rightPos.getX() - 0.5f, position.getY() - 0.5f, rightPos.getZ() + 0.5f));

                            v0.setNormal(new Vector3f(1.0f, 0.0f, 0.0f));
                            v1.setNormal(new Vector3f(1.0f, 0.0f, 0.0f));
                            v2.setNormal(new Vector3f(1.0f, 0.0f, 0.0f));

                            /*if (currentArea == PLAIN_AREA) {
                                if (rightPos.getY() < -1.0f) {
                                    v0.setTexCoords(new Vector2f(0.0f, 1.0f / 4.0f));
                                    v1.setTexCoords(new Vector2f(0.0f, 0.0f));
                                    v2.setTexCoords(new Vector2f(1.0f / 4.0f, 0.0f));
                                } else {
                                    v0.setTexCoords(new Vector2f(0.0f, 2.0f / 4.0f));
                                    v1.setTexCoords(new Vector2f(0.0f, 1.0f / 4.0f));
                                    v2.setTexCoords(new Vector2f(1.0f / 4.0f, 1.0f / 4.0f));
                                }
                            } else if (currentArea == MOUNTAIN_AREA) {
                                if (position.getY() + 0.5f < -1.0f) {
                                    v0.setTexCoords(new Vector2f(1.0f / 4.0f + 0.005f, 1.0f / 4.0f - 0.005f));
                                    v1.setTexCoords(new Vector2f(1.0f / 4.0f + 0.005f, 0.0f + 0.005f));
                                    v2.setTexCoords(new Vector2f(2.0f / 4.0f - 0.005f, 0.0f + 0.005f));
                                }else if (position.getY() + 0.5f < 1.0f) {
                                    v0.setTexCoords(new Vector2f(1.0f / 4.0f + 0.005f, 2.0f / 4.0f - 0.005f));
                                    v1.setTexCoords(new Vector2f(1.0f / 4.0f + 0.005f, 1.0f / 4.0f + 0.005f));
                                    v2.setTexCoords(new Vector2f(2.0f / 4.0f - 0.005f, 1.0f / 4.0f + 0.005f));
                                }else {
                                    v0.setTexCoords(new Vector2f(2.0f / 4.0f, 2.0f / 4.0f));
                                    v1.setTexCoords(new Vector2f(2.0f / 4.0f, 1.0f / 4.0f));
                                    v2.setTexCoords(new Vector2f(3.0f / 4.0f, 1.0f / 4.0f));
                                }
                            }*/

                            v0.setTexCoords(new Vector2f(0.0f, 0.0f));
                            v1.setTexCoords(new Vector2f(1.0f, 0.0f));
                            v2.setTexCoords(new Vector2f(1.0f, 1.0f));

                            v0.setTangents(new Vector3f(count,0.0f,0.0f));
                            v1.setTangents(new Vector3f(count,0.0f,0.0f));
                            v2.setTangents(new Vector3f(count,0.0f,0.0f));

                            vertices.add(v0);
                            vertices.add(v1);
                            vertices.add(v2);

                            indices.add(index++);
                            indices.add(index++);
                            indices.add(index++);
                        }
                    }

                    if (currentHeight == leftHeight && currentHeight == upHeight && currentHeight == rightHeight && currentHeight == downHeight) {
                        check = true;
                        Vertex v0 = new Vertex();
                        Vertex v1 = new Vertex();
                        Vertex v2 = new Vertex();
                        Vertex v3 = new Vertex();

                        v0.setPosition(new Vector3f(position.getX() - 0.5f, position.getY(), position.getZ() + 0.5f));
                        v1.setPosition(new Vector3f(position.getX() - 0.5f, position.getY(), position.getZ() - 0.5f));
                        v2.setPosition(new Vector3f(position.getX() + 0.5f, position.getY(), position.getZ() - 0.5f));
                        v3.setPosition(new Vector3f(position.getX() + 0.5f, position.getY(), position.getZ() + 0.5f));

                        v0.setNormal(new Vector3f(0.0f, 1.0f, 0.0f));
                        v1.setNormal(new Vector3f(0.0f, 1.0f, 0.0f));
                        v2.setNormal(new Vector3f(0.0f, 1.0f, 0.0f));
                        v3.setNormal(new Vector3f(0.0f, 1.0f, 0.0f));

                        setUpperPlaneTexCoord(currentArea,position.getY(),v0,v1,v2,v3);
                        tangentBitangent(v0,v1,v2,v3);

                        ArrayList<Vertex> cellVertices = new ArrayList<>();
                        vertices.add(v0);
                        Vertex v = new Vertex(v0);
                        v.setTexCoords(new Vector2f(0.0f,1.0f));
                        cellVertices.add(v);
                        vertices.add(v1);
                        v = new Vertex(v1);
                        v.setTexCoords(new Vector2f(0.0f,0.0f));
                        cellVertices.add(v);
                        vertices.add(v2);
                        v = new Vertex(v2);
                        v.setTexCoords(new Vector2f(1.0f,0.0f));
                        cellVertices.add(v);
                        vertices.add(v3);
                        v = new Vertex(v3);
                        v.setTexCoords(new Vector2f(1.0f,1.0f));
                        cellVertices.add(v);

                        indices.add(index++);
                        indices.add(index++);
                        indices.add(index);
                        index -= 2;
                        indices.add(index);
                        index += 2;
                        indices.add(index++);
                        indices.add(index++);
                        ArrayList<Integer> cellIndices = new ArrayList<>(Arrays.asList(0,1,2,0,2,3));
                        Mesh cellMesh = new Mesh(cellVertices,cellIndices,null);
                        grid[x][z].setupMesh(cellMesh);
                        grid[x][z].setupTexture(textures.getCursor(),textures.getGrayZona(),textures.getRedZona(),textures.getGreenZona(),textures.getGoldZona(),textures.getBlueZona());
                    }

                    if (!check) {
                        Vertex v0 = new Vertex();
                        Vertex v1 = new Vertex();
                        Vertex v2 = new Vertex();
                        Vertex v3 = new Vertex();

                        v0.setPosition(new Vector3f(position.getX() - 0.5f, position.getY(), position.getZ() + 0.5f));
                        v1.setPosition(new Vector3f(position.getX() - 0.5f, position.getY(), position.getZ() - 0.5f));
                        v2.setPosition(new Vector3f(position.getX() + 0.5f, position.getY(), position.getZ() - 0.5f));
                        v3.setPosition(new Vector3f(position.getX() + 0.5f, position.getY(), position.getZ() + 0.5f));

                        v0.setNormal(new Vector3f(0.0f, 1.0f, 0.0f));
                        v1.setNormal(new Vector3f(0.0f, 1.0f, 0.0f));
                        v2.setNormal(new Vector3f(0.0f, 1.0f, 0.0f));
                        v3.setNormal(new Vector3f(0.0f, 1.0f, 0.0f));

                        setUpperPlaneTexCoord(currentArea,position.getY(),v0,v1,v2,v3);
                        tangentBitangent(v0,v1,v2,v3);

                        ArrayList<Vertex> cellVertices = new ArrayList<>();
                        vertices.add(v0);
                        Vertex v = new Vertex(v0);
                        v.setTexCoords(new Vector2f(0.0f,1.0f));
                        cellVertices.add(v);
                        vertices.add(v1);
                        v = new Vertex(v1);
                        v.setTexCoords(new Vector2f(0.0f,0.0f));
                        cellVertices.add(v);
                        vertices.add(v2);
                        v = new Vertex(v2);
                        v.setTexCoords(new Vector2f(1.0f,0.0f));
                        cellVertices.add(v);
                        vertices.add(v3);
                        v = new Vertex(v3);
                        v.setTexCoords(new Vector2f(1.0f,1.0f));
                        cellVertices.add(v);

                        indices.add(index++);
                        indices.add(index++);
                        indices.add(index);
                        index -= 2;
                        indices.add(index);
                        index += 2;
                        indices.add(index++);
                        indices.add(index++);
                        ArrayList<Integer> cellIndices = new ArrayList<>(Arrays.asList(0,1,2,0,2,3));
                        Mesh cellMesh = new Mesh(cellVertices,cellIndices,null);
                        grid[x][z].setupMesh(cellMesh);
                        grid[x][z].setupTexture(textures.getCursor(),textures.getGrayZona(),textures.getRedZona(),textures.getGreenZona(),textures.getGoldZona(),textures.getBlueZona());
                        count = 0;
                        if (leftHeight < currentHeight) {
                            for (float y = currentHeight; y > leftHeight; y--) {
                                v0 = new Vertex();
                                v1 = new Vertex();
                                v2 = new Vertex();
                                v3 = new Vertex();

                                v0.setPosition(new Vector3f(leftPos.getX() + 0.5f, y, leftPos.getZ() + 0.5f));
                                v1.setPosition(new Vector3f(leftPos.getX() + 0.5f, y - 1.0f, leftPos.getZ() + 0.5f));
                                v2.setPosition(new Vector3f(leftPos.getX() + 0.5f, y - 1.0f, leftPos.getZ() - 0.5f));
                                v3.setPosition(new Vector3f(leftPos.getX() + 0.5f, y, leftPos.getZ() - 0.5f));

                                v0.setNormal(new Vector3f(-1.0f, 0.0f, 0.0f));
                                v1.setNormal(new Vector3f(-1.0f, 0.0f, 0.0f));
                                v2.setNormal(new Vector3f(-1.0f, 0.0f, 0.0f));
                                v3.setNormal(new Vector3f(-1.0f, 0.0f, 0.0f));

                                setSidePlaneTexCoord(currentArea,y,currentHeight,v0,v1,v2,v3);
                                tangentBitangent(v0,v1,v2,v3,count);

                                vertices.add(v0);
                                vertices.add(v1);
                                vertices.add(v2);
                                vertices.add(v3);

                                indices.add(index++);
                                indices.add(index++);
                                indices.add(index);
                                index -= 2;
                                indices.add(index);
                                index += 2;
                                indices.add(index++);
                                indices.add(index++);
                                count++;
                            }
                        }
                        count = 0;
                        if (upHeight < currentHeight) {
                            for (float y = currentHeight; y > upHeight; y--) {
                                v0 = new Vertex();
                                v1 = new Vertex();
                                v2 = new Vertex();
                                v3 = new Vertex();

                                v0.setPosition(new Vector3f(upPos.getX() + 0.5f, y, upPos.getZ() - 0.5f));
                                v1.setPosition(new Vector3f(upPos.getX() + 0.5f, y - 1.0f, upPos.getZ() - 0.5f));
                                v2.setPosition(new Vector3f(upPos.getX() - 0.5f, y - 1.0f, upPos.getZ() - 0.5f));
                                v3.setPosition(new Vector3f(upPos.getX() - 0.5f, y, upPos.getZ() - 0.5f));

                                v0.setNormal(new Vector3f(0.0f, 0.0f, 1.0f));
                                v1.setNormal(new Vector3f(0.0f, 0.0f, 1.0f));
                                v2.setNormal(new Vector3f(0.0f, 0.0f, 1.0f));
                                v3.setNormal(new Vector3f(0.0f, 0.0f, 1.0f));

                                setSidePlaneTexCoord(currentArea,y,currentHeight,v0,v1,v2,v3);
                                tangentBitangent(v0,v1,v2,v3,count);

                                vertices.add(v0);
                                vertices.add(v1);
                                vertices.add(v2);
                                vertices.add(v3);

                                indices.add(index++);
                                indices.add(index++);
                                indices.add(index);
                                index -= 2;
                                indices.add(index);
                                index += 2;
                                indices.add(index++);
                                indices.add(index++);
                                count++;
                            }
                        }
                        count = 0;
                        if (rightHeight < currentHeight) {
                            for (float y = currentHeight; y > rightHeight; y--) {
                                v0 = new Vertex();
                                v1 = new Vertex();
                                v2 = new Vertex();
                                v3 = new Vertex();

                                v0.setPosition(new Vector3f(rightPos.getX() - 0.5f, y, rightPos.getZ() - 0.5f));
                                v1.setPosition(new Vector3f(rightPos.getX() - 0.5f, y - 1.0f, rightPos.getZ() - 0.5f));
                                v2.setPosition(new Vector3f(rightPos.getX() - 0.5f, y - 1.0f, rightPos.getZ() + 0.5f));
                                v3.setPosition(new Vector3f(rightPos.getX() - 0.5f, y, rightPos.getZ() + 0.5f));

                                v0.setNormal(new Vector3f(1.0f, 0.0f, 0.0f));
                                v1.setNormal(new Vector3f(1.0f, 0.0f, 0.0f));
                                v2.setNormal(new Vector3f(1.0f, 0.0f, 0.0f));
                                v3.setNormal(new Vector3f(1.0f, 0.0f, 0.0f));

                                setSidePlaneTexCoord(currentArea,y,currentHeight,v0,v1,v2,v3);
                                tangentBitangent(v0,v1,v2,v3,count);

                                vertices.add(v0);
                                vertices.add(v1);
                                vertices.add(v2);
                                vertices.add(v3);

                                indices.add(index++);
                                indices.add(index++);
                                indices.add(index);
                                index -= 2;
                                indices.add(index);
                                index += 2;
                                indices.add(index++);
                                indices.add(index++);
                                count++;
                            }
                        }
                        count = 0;
                        if (downHeight < currentHeight) {
                            for (float y = currentHeight; y > downHeight; y--) {
                                v0 = new Vertex();
                                v1 = new Vertex();
                                v2 = new Vertex();
                                v3 = new Vertex();

                                v0.setPosition(new Vector3f(downPos.getX() - 0.5f, y, downPos.getZ() + 0.5f));
                                v1.setPosition(new Vector3f(downPos.getX() - 0.5f, y - 1.0f, downPos.getZ() + 0.5f));
                                v2.setPosition(new Vector3f(downPos.getX() + 0.5f, y - 1.0f, downPos.getZ() + 0.5f));
                                v3.setPosition(new Vector3f(downPos.getX() + 0.5f, y, downPos.getZ() + 0.5f));

                                v0.setNormal(new Vector3f(0.0f, 0.0f, -1.0f));
                                v1.setNormal(new Vector3f(0.0f, 0.0f, -1.0f));
                                v2.setNormal(new Vector3f(0.0f, 0.0f, -1.0f));
                                v3.setNormal(new Vector3f(0.0f, 0.0f, -1.0f));

                                setSidePlaneTexCoord(currentArea,y,currentHeight,v0,v1,v2,v3);
                                tangentBitangent(v0,v1,v2,v3,count);

                                vertices.add(v0);
                                vertices.add(v1);
                                vertices.add(v2);
                                vertices.add(v3);

                                indices.add(index++);
                                indices.add(index++);
                                indices.add(index);
                                index -= 2;
                                indices.add(index);
                                index += 2;
                                indices.add(index++);
                                indices.add(index++);
                                count++;
                            }
                        }
                    }
                } else {
                    Vertex v0 = new Vertex();
                    Vertex v1 = new Vertex();
                    Vertex v2 = new Vertex();
                    Vertex v3 = new Vertex();

                    v0.setPosition(new Vector3f(position.getX() - 0.5f, position.getY(), position.getZ() + 0.5f));
                    v1.setPosition(new Vector3f(position.getX() - 0.5f, position.getY(), position.getZ() - 0.5f));
                    v2.setPosition(new Vector3f(position.getX() + 0.5f, position.getY(), position.getZ() - 0.5f));
                    v3.setPosition(new Vector3f(position.getX() + 0.5f, position.getY(), position.getZ() + 0.5f));

                    v0.setNormal(new Vector3f(0.0f, 1.0f, 0.0f));
                    v1.setNormal(new Vector3f(0.0f, 1.0f, 0.0f));
                    v2.setNormal(new Vector3f(0.0f, 1.0f, 0.0f));
                    v3.setNormal(new Vector3f(0.0f, 1.0f, 0.0f));

                    setUpperPlaneTexCoord(currentArea,position.getY(),v0,v1,v2,v3);
                    tangentBitangent(v0,v1,v2,v3);

                    ArrayList<Vertex> cellVertices = new ArrayList<>();
                    vertices.add(v0);
                    Vertex v = new Vertex(v0);
                    v.setTexCoords(new Vector2f(0.0f,1.0f));
                    cellVertices.add(v);
                    vertices.add(v1);
                    v = new Vertex(v1);
                    v.setTexCoords(new Vector2f(0.0f,0.0f));
                    cellVertices.add(v);
                    vertices.add(v2);
                    v = new Vertex(v2);
                    v.setTexCoords(new Vector2f(1.0f,0.0f));
                    cellVertices.add(v);
                    vertices.add(v3);
                    v = new Vertex(v3);
                    v.setTexCoords(new Vector2f(1.0f,1.0f));
                    cellVertices.add(v);

                    indices.add(index++);
                    indices.add(index++);
                    indices.add(index);
                    index -= 2;
                    indices.add(index);
                    index += 2;
                    indices.add(index++);
                    indices.add(index++);
                    ArrayList<Integer> cellIndices = new ArrayList<>(Arrays.asList(0,1,2,0,2,3));
                    Mesh cellMesh = new Mesh(cellVertices,cellIndices,null);
                    grid[x][z].setupMesh(cellMesh);
                    grid[x][z].setupTexture(textures.getCursor(),textures.getGrayZona(),textures.getRedZona(),textures.getGreenZona(),textures.getGoldZona(),textures.getBlueZona());

                    count = 0;
                    if (!left) {
                        for (int y = (int) leftPos.getY(); y >= floor; y--) {
                            v0 = new Vertex();
                            v1 = new Vertex();
                            v2 = new Vertex();
                            v3 = new Vertex();

                            v0.setPosition(new Vector3f(leftPos.getX() + 0.5f, y, leftPos.getZ() + 0.5f));
                            v1.setPosition(new Vector3f(leftPos.getX() + 0.5f, y - 1.0f, leftPos.getZ() + 0.5f));
                            v2.setPosition(new Vector3f(leftPos.getX() + 0.5f, y - 1.0f, leftPos.getZ() - 0.5f));
                            v3.setPosition(new Vector3f(leftPos.getX() + 0.5f, y, leftPos.getZ() - 0.5f));

                            v0.setNormal(new Vector3f(-1.0f, 0.0f, 0.0f));
                            v1.setNormal(new Vector3f(-1.0f, 0.0f, 0.0f));
                            v2.setNormal(new Vector3f(-1.0f, 0.0f, 0.0f));
                            v3.setNormal(new Vector3f(-1.0f, 0.0f, 0.0f));

                            setSidePlaneTexCoord(currentArea,y,leftPos.getY(),v0,v1,v2,v3);
                            tangentBitangent(v0,v1,v2,v3,count);

                            vertices.add(v0);
                            vertices.add(v1);
                            vertices.add(v2);
                            vertices.add(v3);

                            indices.add(index++);
                            indices.add(index++);
                            indices.add(index);
                            index -= 2;
                            indices.add(index);
                            index += 2;
                            indices.add(index++);
                            indices.add(index++);
                            count++;
                        }
                    } else {
                        if (leftPos.getY() < position.getY()) {
                            for (float y = position.getY(); y > leftPos.getY(); y--) {
                                v0 = new Vertex();
                                v1 = new Vertex();
                                v2 = new Vertex();
                                v3 = new Vertex();

                                v0.setPosition(new Vector3f(leftPos.getX() + 0.5f, y, leftPos.getZ() + 0.5f));
                                v1.setPosition(new Vector3f(leftPos.getX() + 0.5f, y - 1.0f, leftPos.getZ() + 0.5f));
                                v2.setPosition(new Vector3f(leftPos.getX() + 0.5f, y - 1.0f, leftPos.getZ() - 0.5f));
                                v3.setPosition(new Vector3f(leftPos.getX() + 0.5f, y, leftPos.getZ() - 0.5f));

                                v0.setNormal(new Vector3f(-1.0f, 0.0f, 0.0f));
                                v1.setNormal(new Vector3f(-1.0f, 0.0f, 0.0f));
                                v2.setNormal(new Vector3f(-1.0f, 0.0f, 0.0f));
                                v3.setNormal(new Vector3f(-1.0f, 0.0f, 0.0f));

                                setSidePlaneTexCoord(currentArea,y,position.getY(),v0,v1,v2,v3);
                                tangentBitangent(v0,v1,v2,v3,count);

                                vertices.add(v0);
                                vertices.add(v1);
                                vertices.add(v2);
                                vertices.add(v3);

                                indices.add(index++);
                                indices.add(index++);
                                indices.add(index);
                                index -= 2;
                                indices.add(index);
                                index += 2;
                                indices.add(index++);
                                indices.add(index++);
                                count++;
                            }
                        }
                    }
                    count = 0;
                    if (!up) {
                        for (int y = (int) upPos.getY(); y >= floor; y--) {
                            v0 = new Vertex();
                            v1 = new Vertex();
                            v2 = new Vertex();
                            v3 = new Vertex();

                            v0.setPosition(new Vector3f(upPos.getX() + 0.5f, y, upPos.getZ() - 0.5f));
                            v1.setPosition(new Vector3f(upPos.getX() + 0.5f, y - 1.0f, upPos.getZ() - 0.5f));
                            v2.setPosition(new Vector3f(upPos.getX() - 0.5f, y - 1.0f, upPos.getZ() - 0.5f));
                            v3.setPosition(new Vector3f(upPos.getX() - 0.5f, y, upPos.getZ() - 0.5f));

                            v0.setNormal(new Vector3f(0.0f, 0.0f, 1.0f));
                            v1.setNormal(new Vector3f(0.0f, 0.0f, 1.0f));
                            v2.setNormal(new Vector3f(0.0f, 0.0f, 1.0f));
                            v3.setNormal(new Vector3f(0.0f, 0.0f, 1.0f));

                            setSidePlaneTexCoord(currentArea,y,upPos.getY(),v0,v1,v2,v3);
                            tangentBitangent(v0,v1,v2,v3,count);

                            vertices.add(v0);
                            vertices.add(v1);
                            vertices.add(v2);
                            vertices.add(v3);

                            indices.add(index++);
                            indices.add(index++);
                            indices.add(index);
                            index -= 2;
                            indices.add(index);
                            index += 2;
                            indices.add(index++);
                            indices.add(index++);
                            count++;
                        }
                    } else {
                        if (upPos.getY() < position.getY()) {
                            for (float y = position.getY(); y > upPos.getY(); y--) {
                                v0 = new Vertex();
                                v1 = new Vertex();
                                v2 = new Vertex();
                                v3 = new Vertex();

                                v0.setPosition(new Vector3f(upPos.getX() + 0.5f, y, upPos.getZ() - 0.5f));
                                v1.setPosition(new Vector3f(upPos.getX() + 0.5f, y - 1.0f, upPos.getZ() - 0.5f));
                                v2.setPosition(new Vector3f(upPos.getX() - 0.5f, y - 1.0f, upPos.getZ() - 0.5f));
                                v3.setPosition(new Vector3f(upPos.getX() - 0.5f, y, upPos.getZ() - 0.5f));

                                v0.setNormal(new Vector3f(0.0f, 0.0f, 1.0f));
                                v1.setNormal(new Vector3f(0.0f, 0.0f, 1.0f));
                                v2.setNormal(new Vector3f(0.0f, 0.0f, 1.0f));
                                v3.setNormal(new Vector3f(0.0f, 0.0f, 1.0f));

                                setSidePlaneTexCoord(currentArea,y,position.getY(),v0,v1,v2,v3);
                                tangentBitangent(v0,v1,v2,v3,count);

                                vertices.add(v0);
                                vertices.add(v1);
                                vertices.add(v2);
                                vertices.add(v3);

                                indices.add(index++);
                                indices.add(index++);
                                indices.add(index);
                                index -= 2;
                                indices.add(index);
                                index += 2;
                                indices.add(index++);
                                indices.add(index++);
                                count++;
                            }
                        }
                    }
                    count = 0;
                    if (!right) {
                        for (int y = (int) rightPos.getY(); y >= floor; y--) {
                            v0 = new Vertex();
                            v1 = new Vertex();
                            v2 = new Vertex();
                            v3 = new Vertex();

                            v0.setPosition(new Vector3f(rightPos.getX() - 0.5f, y, rightPos.getZ() - 0.5f));
                            v1.setPosition(new Vector3f(rightPos.getX() - 0.5f, y - 1.0f, rightPos.getZ() - 0.5f));
                            v2.setPosition(new Vector3f(rightPos.getX() - 0.5f, y - 1.0f, rightPos.getZ() + 0.5f));
                            v3.setPosition(new Vector3f(rightPos.getX() - 0.5f, y, rightPos.getZ() + 0.5f));

                            v0.setNormal(new Vector3f(1.0f, 0.0f, 0.0f));
                            v1.setNormal(new Vector3f(1.0f, 0.0f, 0.0f));
                            v2.setNormal(new Vector3f(1.0f, 0.0f, 0.0f));
                            v3.setNormal(new Vector3f(1.0f, 0.0f, 0.0f));

                            setSidePlaneTexCoord(currentArea,y,rightPos.getY(),v0,v1,v2,v3);
                            tangentBitangent(v0,v1,v2,v3,count);

                            vertices.add(v0);
                            vertices.add(v1);
                            vertices.add(v2);
                            vertices.add(v3);

                            indices.add(index++);
                            indices.add(index++);
                            indices.add(index);
                            index -= 2;
                            indices.add(index);
                            index += 2;
                            indices.add(index++);
                            indices.add(index++);
                            count++;
                        }
                    } else {
                        if (rightPos.getY() < position.getY()) {
                            for (float y = position.getY(); y > rightPos.getY(); y--) {
                                v0 = new Vertex();
                                v1 = new Vertex();
                                v2 = new Vertex();
                                v3 = new Vertex();

                                v0.setPosition(new Vector3f(rightPos.getX() - 0.5f, y, rightPos.getZ() - 0.5f));
                                v1.setPosition(new Vector3f(rightPos.getX() - 0.5f, y - 1.0f, rightPos.getZ() - 0.5f));
                                v2.setPosition(new Vector3f(rightPos.getX() - 0.5f, y - 1.0f, rightPos.getZ() + 0.5f));
                                v3.setPosition(new Vector3f(rightPos.getX() - 0.5f, y, rightPos.getZ() + 0.5f));

                                v0.setNormal(new Vector3f(1.0f, 0.0f, 0.0f));
                                v1.setNormal(new Vector3f(1.0f, 0.0f, 0.0f));
                                v2.setNormal(new Vector3f(1.0f, 0.0f, 0.0f));
                                v3.setNormal(new Vector3f(1.0f, 0.0f, 0.0f));

                                setSidePlaneTexCoord(currentArea,y,position.getY(),v0,v1,v2,v3);
                                tangentBitangent(v0,v1,v2,v3,count);

                                vertices.add(v0);
                                vertices.add(v1);
                                vertices.add(v2);
                                vertices.add(v3);

                                indices.add(index++);
                                indices.add(index++);
                                indices.add(index);
                                index -= 2;
                                indices.add(index);
                                index += 2;
                                indices.add(index++);
                                indices.add(index++);
                                count++;
                            }
                        }
                    }
                    count = 0;
                    if (!down) {
                        for (int y = (int) downPos.getY(); y >= floor; y--) {
                            v0 = new Vertex();
                            v1 = new Vertex();
                            v2 = new Vertex();
                            v3 = new Vertex();

                            v0.setPosition(new Vector3f(downPos.getX() - 0.5f, y, downPos.getZ() + 0.5f));
                            v1.setPosition(new Vector3f(downPos.getX() - 0.5f, y - 1.0f, downPos.getZ() + 0.5f));
                            v2.setPosition(new Vector3f(downPos.getX() + 0.5f, y - 1.0f, downPos.getZ() + 0.5f));
                            v3.setPosition(new Vector3f(downPos.getX() + 0.5f, y, downPos.getZ() + 0.5f));

                            v0.setNormal(new Vector3f(0.0f, 0.0f, -1.0f));
                            v1.setNormal(new Vector3f(0.0f, 0.0f, -1.0f));
                            v2.setNormal(new Vector3f(0.0f, 0.0f, -1.0f));
                            v3.setNormal(new Vector3f(0.0f, 0.0f, -1.0f));

                            setSidePlaneTexCoord(currentArea,y,downPos.getY(),v0,v1,v2,v3);
                            tangentBitangent(v0,v1,v2,v3,count);

                            vertices.add(v0);
                            vertices.add(v1);
                            vertices.add(v2);
                            vertices.add(v3);

                            indices.add(index++);
                            indices.add(index++);
                            indices.add(index);
                            index -= 2;
                            indices.add(index);
                            index += 2;
                            indices.add(index++);
                            indices.add(index++);
                            count++;
                        }
                    } else {
                        if (downPos.getY() < position.getY()) {
                            for (float y = position.getY(); y > downPos.getY(); y--) {
                                v0 = new Vertex();
                                v1 = new Vertex();
                                v2 = new Vertex();
                                v3 = new Vertex();

                                v0.setPosition(new Vector3f(downPos.getX() - 0.5f, y, downPos.getZ() + 0.5f));
                                v1.setPosition(new Vector3f(downPos.getX() - 0.5f, y - 1.0f, downPos.getZ() + 0.5f));
                                v2.setPosition(new Vector3f(downPos.getX() + 0.5f, y - 1.0f, downPos.getZ() + 0.5f));
                                v3.setPosition(new Vector3f(downPos.getX() + 0.5f, y, downPos.getZ() + 0.5f));

                                v0.setNormal(new Vector3f(0.0f, 0.0f, -1.0f));
                                v1.setNormal(new Vector3f(0.0f, 0.0f, -1.0f));
                                v2.setNormal(new Vector3f(0.0f, 0.0f, -1.0f));
                                v3.setNormal(new Vector3f(0.0f, 0.0f, -1.0f));

                                setSidePlaneTexCoord(currentArea,y,position.getY(),v0,v1,v2,v3);
                                tangentBitangent(v0,v1,v2,v3,count);

                                vertices.add(v0);
                                vertices.add(v1);
                                vertices.add(v2);
                                vertices.add(v3);

                                indices.add(index++);
                                indices.add(index++);
                                indices.add(index);
                                index -= 2;
                                indices.add(index);
                                index += 2;
                                indices.add(index++);
                                indices.add(index++);
                                count++;
                            }
                        }
                    }
                }
            }
        }

        HowLong.getInformation();

        return new Mesh(new ArrayList<>(vertices),new ArrayList<>(indices),null);
    }

    private static void setUpperPlaneTexCoord(int currentArea, float currentHeight, Vertex v0, Vertex v1, Vertex v2, Vertex v3){
        /*if (currentArea == PLAIN_AREA) {
            if (currentHeight < 0) {
                v0.setTexCoords(new Vector2f(0.0f + textureOffset, 1.0f / 4.0f - textureOffset));
                v1.setTexCoords(new Vector2f(0.0f + textureOffset, 0.0f + textureOffset));
                v2.setTexCoords(new Vector2f(1.0f / 4.0f - textureOffset, 0.0f + textureOffset));
                v3.setTexCoords(new Vector2f(1.0f / 4.0f - textureOffset, 1.0f / 4.0f - textureOffset));
            } else {
                v0.setTexCoords(new Vector2f(0.0f + textureOffset, 3.0f / 4.0f - textureOffset));
                v1.setTexCoords(new Vector2f(0.0f + textureOffset, 2.0f / 4.0f + textureOffset));
                v2.setTexCoords(new Vector2f(1.0f / 4.0f - textureOffset, 2.0f / 4.0f + textureOffset));
                v3.setTexCoords(new Vector2f(1.0f / 4.0f - textureOffset, 3.0f / 4.0f - textureOffset));
            }
        } else if (currentArea == MOUNTAIN_AREA) {
            if (currentHeight < -1.0f) {
                v0.setTexCoords(new Vector2f(1.0f / 4.0f + textureOffset, 1.0f / 4.0f - textureOffset));
                v1.setTexCoords(new Vector2f(1.0f / 4.0f + textureOffset, 0.0f + textureOffset));
                v2.setTexCoords(new Vector2f(2.0f / 4.0f - textureOffset, 0.0f + textureOffset));
                v3.setTexCoords(new Vector2f(2.0f / 4.0f - textureOffset, 1.0f / 4.0f - textureOffset));
            } else {
                v0.setTexCoords(new Vector2f(1.0f / 4.0f + textureOffset, 3.0f / 4.0f - textureOffset));
                v1.setTexCoords(new Vector2f(1.0f / 4.0f + textureOffset, 2.0f / 4.0f + textureOffset));
                v2.setTexCoords(new Vector2f(2.0f / 4.0f - textureOffset, 2.0f / 4.0f + textureOffset));
                v3.setTexCoords(new Vector2f(2.0f / 4.0f - textureOffset, 3.0f / 4.0f - textureOffset));
            }
        }*/

        v0.setTexCoords(new Vector2f(0.0f, 1.0f));
        v1.setTexCoords(new Vector2f(0.0f, 0.0f));
        v2.setTexCoords(new Vector2f(1.0f, 0.0f));
        v3.setTexCoords(new Vector2f(1.0f, 1.0f));
    }

    private static void setUpperBevelPlaneTexCoord(int currentArea, float currentHeight, Vertex v0, Vertex v1, Vertex v2, Vertex v3){
        /*if (currentArea == PLAIN_AREA) {
            if (currentHeight < -1.0f) {
                v0.setTexCoords(new Vector2f(0.0f + textureOffset, 1.0f / 4.0f - textureOffset));
                v1.setTexCoords(new Vector2f(0.0f + textureOffset, 0.0f + textureOffset));
                v2.setTexCoords(new Vector2f(1.0f / 4.0f - textureOffset, 0.0f + textureOffset));
                v3.setTexCoords(new Vector2f(1.0f / 4.0f - textureOffset, 1.0f / 4.0f - textureOffset));
            } else if (currentHeight < 0.0f) {
                v0.setTexCoords(new Vector2f(0.0f + textureOffset, 2.0f / 4.0f - textureOffset));
                v1.setTexCoords(new Vector2f(0.0f + textureOffset, 1.0f / 4.0f + textureOffset));
                v2.setTexCoords(new Vector2f(1.0f / 4.0f - textureOffset, 1.0f / 4.0f + textureOffset));
                v3.setTexCoords(new Vector2f(1.0f / 4.0f - textureOffset, 2.0f / 4.0f - textureOffset));
            } else {
                v0.setTexCoords(new Vector2f(0.0f + textureOffset, 3.0f / 4.0f - textureOffset));
                v1.setTexCoords(new Vector2f(0.0f + textureOffset, 2.0f / 4.0f + textureOffset));
                v2.setTexCoords(new Vector2f(1.0f / 4.0f - textureOffset, 2.0f / 4.0f + textureOffset));
                v3.setTexCoords(new Vector2f(1.0f / 4.0f - textureOffset, 3.0f / 4.0f - textureOffset));
            }
        } else if (currentArea == MOUNTAIN_AREA) {
            if (currentHeight + 1.0f < -1.0f) {
                v0.setTexCoords(new Vector2f(1.0f / 4.0f + textureOffset, 1.0f / 4.0f - textureOffset));
                v1.setTexCoords(new Vector2f(1.0f / 4.0f + textureOffset, 0.0f + textureOffset));
                v2.setTexCoords(new Vector2f(2.0f / 4.0f - textureOffset, 0.0f + textureOffset));
                v3.setTexCoords(new Vector2f(2.0f / 4.0f - textureOffset, 1.0f / 4.0f - textureOffset));
            } else {
                v0.setTexCoords(new Vector2f(1.0f / 4.0f + textureOffset, 3.0f / 4.0f - textureOffset));
                v1.setTexCoords(new Vector2f(1.0f / 4.0f + textureOffset, 2.0f / 4.0f + textureOffset));
                v2.setTexCoords(new Vector2f(2.0f / 4.0f - textureOffset, 2.0f / 4.0f + textureOffset));
                v3.setTexCoords(new Vector2f(2.0f / 4.0f - textureOffset, 3.0f / 4.0f - textureOffset));
            }
        }*/
        v0.setTexCoords(new Vector2f(0.0f, 1.0f));
        v1.setTexCoords(new Vector2f(0.0f, 0.0f));
        v2.setTexCoords(new Vector2f(1.0f, 0.0f));
        v3.setTexCoords(new Vector2f(1.0f, 1.0f));
    }

    private static void setSidePlaneTexCoord(int currentArea, float y, float currentHeight, Vertex v0, Vertex v1, Vertex v2, Vertex v3){
        /*if (currentArea == PLAIN_AREA) {
            if (y == currentHeight) {
                if (y < 0.0f) {
                    v0.setTexCoords(new Vector2f(0.0f + textureOffset, 1.0f / 4.0f - textureOffset));
                    v1.setTexCoords(new Vector2f(0.0f + textureOffset, 0.0f + textureOffset));
                    v2.setTexCoords(new Vector2f(1.0f / 4.0f - textureOffset, 0.0f + textureOffset));
                    v3.setTexCoords(new Vector2f(1.0f / 4.0f - textureOffset, 1.0f / 4.0f - textureOffset));
                } else {
                    v0.setTexCoords(new Vector2f(0.0f + textureOffset, 2.0f / 4.0f - textureOffset));
                    v1.setTexCoords(new Vector2f(0.0f + textureOffset, 1.0f / 4.0f + textureOffset));
                    v2.setTexCoords(new Vector2f(1.0f / 4.0f - textureOffset, 1.0f / 4.0f + textureOffset));
                    v3.setTexCoords(new Vector2f(1.0f / 4.0f - textureOffset, 2.0f / 4.0f - textureOffset));
                }
            } else {
                v0.setTexCoords(new Vector2f(0.0f + textureOffset, 1.0f / 4.0f - textureOffset));
                v1.setTexCoords(new Vector2f(0.0f + textureOffset, 0.0f + textureOffset));
                v2.setTexCoords(new Vector2f(1.0f / 4.0f - textureOffset, 0.0f + textureOffset));
                v3.setTexCoords(new Vector2f(1.0f / 4.0f - textureOffset, 1.0f / 4.0f - textureOffset));
            }
        } else if (currentArea == MOUNTAIN_AREA) {
            if (y == currentHeight) {
                if (y < - 1.0f) {
                    v0.setTexCoords(new Vector2f(1.0f / 4.0f + textureOffset, 1.0f / 4.0f - textureOffset));
                    v1.setTexCoords(new Vector2f(1.0f / 4.0f + textureOffset, 0.0f + textureOffset));
                    v2.setTexCoords(new Vector2f(2.0f / 4.0f - textureOffset, 0.0f + textureOffset));
                    v3.setTexCoords(new Vector2f(2.0f / 4.0f - textureOffset, 1.0f / 4.0f - textureOffset));
                } else if( y < 1.0f){
                    v0.setTexCoords(new Vector2f(1.0f / 4.0f + textureOffset, 2.0f / 4.0f - textureOffset));
                    v1.setTexCoords(new Vector2f(1.0f / 4.0f + textureOffset, 1.0f / 4.0f + textureOffset));
                    v2.setTexCoords(new Vector2f(2.0f / 4.0f - textureOffset, 1.0f / 4.0f + textureOffset));
                    v3.setTexCoords(new Vector2f(2.0f / 4.0f - textureOffset, 2.0f / 4.0f - textureOffset));
                }else {
                    v0.setTexCoords(new Vector2f(2.0f / 4.0f + textureOffset, 2.0f / 4.0f - textureOffset));
                    v1.setTexCoords(new Vector2f(2.0f / 4.0f + textureOffset, 1.0f / 4.0f + textureOffset));
                    v2.setTexCoords(new Vector2f(3.0f / 4.0f - textureOffset, 1.0f / 4.0f + textureOffset));
                    v3.setTexCoords(new Vector2f(3.0f / 4.0f - textureOffset, 2.0f / 4.0f - textureOffset));
                }
            } else {
                if (y < 1.0f) {
                    v0.setTexCoords(new Vector2f(1.0f / 4.0f + textureOffset, 1.0f / 4.0f - textureOffset));
                    v1.setTexCoords(new Vector2f(1.0f / 4.0f + textureOffset, 0.0f + textureOffset));
                    v2.setTexCoords(new Vector2f(2.0f / 4.0f - textureOffset, 0.0f + textureOffset));
                    v3.setTexCoords(new Vector2f(2.0f / 4.0f - textureOffset, 1.0f / 4.0f - textureOffset));
                } else {
                    v0.setTexCoords(new Vector2f(2.0f / 4.0f + textureOffset, 1.0f / 4.0f - textureOffset));
                    v1.setTexCoords(new Vector2f(2.0f / 4.0f + textureOffset, 0.0f + textureOffset));
                    v2.setTexCoords(new Vector2f(3.0f / 4.0f - textureOffset, 0.0f + textureOffset));
                    v3.setTexCoords(new Vector2f(3.0f / 4.0f - textureOffset, 1.0f / 4.0f - textureOffset));
                }
            }
        }*/
        v0.setTexCoords(new Vector2f(0.0f, 1.0f));
        v1.setTexCoords(new Vector2f(0.0f, 0.0f));
        v2.setTexCoords(new Vector2f(1.0f, 0.0f));
        v3.setTexCoords(new Vector2f(1.0f, 1.0f));
    }

    private static void tangentBitangent(Vertex v0, Vertex v1, Vertex v2, Vertex v3, int count){
        v0.setTangents(new Vector3f(count,0.0f,0.0f));
        v1.setTangents(new Vector3f(count,0.0f,0.0f));
        v2.setTangents(new Vector3f(count,0.0f,0.0f));
        v3.setTangents(new Vector3f(count,0.0f,0.0f));
    }

    private static void tangentBitangent(Vertex v0, Vertex v1, Vertex v2, Vertex v3){

        Vector3f pos1 = new Vector3f(v0.getPosition());
        Vector3f pos2 = new Vector3f(v1.getPosition());
        Vector3f pos3 = new Vector3f(v2.getPosition());
        Vector3f pos4 = new Vector3f(v3.getPosition());

        Vector2f uv1 = new Vector2f(v0.getTexCoords());
        Vector2f uv2 = new Vector2f(v1.getTexCoords());
        Vector2f uv3 = new Vector2f(v2.getTexCoords());
        Vector2f uv4 = new Vector2f(v3.getTexCoords());

        // Первый треугольник
        Vector3f edge1 = pos2.sub(pos1);
        Vector3f edge2 = pos3.sub(pos1);
        Vector2f deltaUV1 = uv2.sub(uv1);
        Vector2f deltaUV2 = uv3.sub(uv1);

        float f = 1.0f / (deltaUV1.getX() * deltaUV2.getY() - deltaUV2.getX() * deltaUV1.getY());

        Vector3f tangent1 = new Vector3f();
        tangent1.setX(f * (deltaUV2.getY() * edge1.getX() - deltaUV1.getY() * edge2.getX()));
        tangent1.setY(f * (deltaUV2.getY() * edge1.getY() - deltaUV1.getY() * edge2.getY()));
        tangent1.setZ(f * (deltaUV2.getY() * edge1.getZ() - deltaUV1.getY() * edge2.getZ()));
        tangent1 = tangent1.normalize();

        Vector3f bitangent1 = new Vector3f();
        bitangent1.setX(f * (-deltaUV2.getX() * edge1.getX() + deltaUV1.getX() * edge2.getX()));
        bitangent1.setY(f * (-deltaUV2.getX() * edge1.getY() + deltaUV1.getX() * edge2.getY()));
        bitangent1.setZ(f * (-deltaUV2.getX() * edge1.getZ() + deltaUV1.getX() * edge2.getZ()));
        bitangent1 = bitangent1.normalize();

        // Второй треугольник
        edge1 = pos3.sub(pos1);
        edge2 = pos4.sub(pos1);
        deltaUV1 = uv3.sub(uv1);
        deltaUV2 = uv4.sub(uv1);

        f = 1.0f / (deltaUV1.getX() * deltaUV2.getY() - deltaUV2.getX() * deltaUV1.getY());

        Vector3f tangent2 = new Vector3f();
        tangent2.setX(f * (deltaUV2.getY() * edge1.getX() - deltaUV1.getY() * edge2.getX()));
        tangent2.setY(f * (deltaUV2.getY() * edge1.getY() - deltaUV1.getY() * edge2.getY()));
        tangent2.setZ(f * (deltaUV2.getY() * edge1.getZ() - deltaUV1.getY() * edge2.getZ()));
        tangent2 = tangent2.normalize();

        Vector3f bitangent2 = new Vector3f();
        bitangent2.setX(f * (-deltaUV2.getX() * edge1.getX() + deltaUV1.getX() * edge2.getX()));
        bitangent2.setY(f * (-deltaUV2.getX() * edge1.getY() + deltaUV1.getX() * edge2.getY()));
        bitangent2.setZ(f * (-deltaUV2.getX() * edge1.getZ() + deltaUV1.getX() * edge2.getZ()));
        bitangent2 = bitangent2.normalize();

        v0.setTangents(tangent1);
        v1.setTangents(tangent1);
        v2.setTangents(tangent2);
        v3.setTangents(tangent2);

        v0.setBitangnets(bitangent1);
        v1.setBitangnets(bitangent1);
        v2.setBitangnets(bitangent2);
        v3.setBitangnets(bitangent2);
    }
}
