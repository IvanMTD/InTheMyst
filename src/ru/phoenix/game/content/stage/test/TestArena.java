package ru.phoenix.game.content.stage.test;

import com.sun.javafx.geom.Matrix3f;
import ru.phoenix.core.math.Matrix4f;
import ru.phoenix.core.math.Projection;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.game.content.block.Block;
import ru.phoenix.game.content.block.type.Dirt;
import ru.phoenix.game.content.block.type.Grass;
import ru.phoenix.game.content.stage.BattleGraund;
import ru.phoenix.game.content.stage.BattleGraundControl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestArena extends BattleGraundControl implements BattleGraund{

    private Block dirt;
    private Block grass;

    public TestArena(){
        super();

        dirt = new Dirt();
        grass = new Grass();

        List<Matrix4f> dirtInstanceMatrix = new ArrayList<>();
        List<Matrix4f> grassInstanceMatrix = new ArrayList<>();

        for(int z = -3; z <= 3; z++){
            for(int x = -3; x <= 3; x++){
                Projection projection = new Projection();
                Vector3f position = new Vector3f(x,-1,z);
                projection.setTranslation(position);
                dirtInstanceMatrix.add(projection.getModelMatrix());
            }
        }

        for(int x = -3; x <= 3; x++){
            Projection projection = new Projection();
            Vector3f position = new Vector3f(x,0,0);
            projection.setTranslation(position);
            grassInstanceMatrix.add(projection.getModelMatrix());
        }

        for(int z = -3; z <= 3; z++){
            if(z != 0) {
                Projection projection = new Projection();
                Vector3f position = new Vector3f(0, 0, z);
                projection.setTranslation(position);
                grassInstanceMatrix.add(projection.getModelMatrix());
            }
        }

        Matrix4f[] dirtMatrix = new Matrix4f[dirtInstanceMatrix.size()];
        Matrix4f[] grassMatrix = new Matrix4f[grassInstanceMatrix.size()];

        for(int i=0; i<dirtMatrix.length; i++){
            dirtMatrix[i] = dirtInstanceMatrix.get(i);
        }

        for(int i=0; i<grassMatrix.length; i++){
            grassMatrix[i] = grassInstanceMatrix.get(i);
        }

        dirt.setInstanceMatrix(dirtMatrix);
        grass.setInstanceMatrix(grassMatrix);

        List<Block> blocks = new ArrayList<>(Arrays.asList(dirt,grass));


        setBlocks(blocks,7,7);
    }
}
