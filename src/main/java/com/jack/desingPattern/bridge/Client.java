package com.jack.desingPattern.bridge;

public class Client {
    public static void main(String[] args) {
        Color color = new Red();
        Pen pen = new SmallPen();
        pen.setColor(color);
        pen.draw("flowers");
    }
}
