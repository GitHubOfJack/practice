package com.jack.desingPattern.bridge;

public class BigPen extends Pen {
    @Override
    public void draw(String name) {
        String penType = "大号笔";
        this.color.bepaint(penType, name);
    }
}
