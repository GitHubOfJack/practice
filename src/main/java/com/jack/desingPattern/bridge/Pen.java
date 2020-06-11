package com.jack.desingPattern.bridge;

public abstract class Pen {
    public void setColor(Color color) {
        this.color = color;
    }

    protected Color color;

    public abstract void draw(String name);

}
