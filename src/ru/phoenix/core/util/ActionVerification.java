package ru.phoenix.core.util;

import ru.phoenix.core.config.Constants;
import ru.phoenix.core.kernel.Input;

import static ru.phoenix.core.config.Constants.NO_ACTION;

public class ActionVerification {

    private int counter;
    private int action;
    private boolean click;
    private int clickRate;

    public ActionVerification(){
        counter = 0;
        clickRate = 55;
        click = false;
        action = NO_ACTION;
    }

    public int mouseButton(int key){
        if (Input.getInstance().isMousePressed(key)) {
            counter++;
            if(!click) {
                click = true;
                action = Constants.HOLD;
            }
        }else if(!Input.getInstance().isMousePressed(key)){
            action = NO_ACTION;
            if(click) {
                if (counter < clickRate && !Input.getInstance().getCursorMove()) {
                    action = Constants.CLICK;
                }
                counter = 0;
                click = false;
            }
        }
        return action;
    }

    public int keyboardButton(int key){
        if (Input.getInstance().isPressed(key)) {
            counter++;
            if(!click) {
                click = true;
                action = Constants.HOLD;
            }
        }else if(!Input.getInstance().isPressed(key)){
            action = NO_ACTION;
            if(click) {
                if (counter < clickRate && !Input.getInstance().getCursorMove()) {
                    action = Constants.CLICK;
                }
                counter = 0;
                click = false;
            }
        }
        return action;
    }
}
