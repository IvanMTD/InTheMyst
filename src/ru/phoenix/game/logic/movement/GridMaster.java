package ru.phoenix.game.logic.movement;

import ru.phoenix.core.math.Vector3f;
import ru.phoenix.game.content.object.active.property.Characteristic;
import ru.phoenix.game.logic.element.GridElement;
import ru.phoenix.game.logic.generator.GraundGenerator;

import java.util.ArrayList;
import java.util.List;

public class GridMaster {

    private static List<GridElement> actualGridElements = new ArrayList<>();

    public static void preperMove(Vector3f mainPosition, Characteristic characteristic){
        getActualGridElements();
    }

    private static void getActualGridElements(){
        actualGridElements = new ArrayList<>(GraundGenerator.getGridElements());
    }
}
