package zelix.utils;

import org.lwjgl.input.*;

public class MouseInputHandler
{
    public boolean clicked;
    private int button;
    
    public MouseInputHandler(final int key) {
        this.button = key;
    }
    
    public boolean canExcecute() {
        if (Mouse.isButtonDown(this.button)) {
            if (!this.clicked) {
                return this.clicked = true;
            }
        }
        else {
            this.clicked = false;
        }
        return false;
    }
}
