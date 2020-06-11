package com.jack.desingPattern.bridge;

public class SmallPen extends Pen {
    @Override
    public void draw(String name) {
        String penType = "小号笔";
        this.color.bepaint(penType, name);
    }
}
