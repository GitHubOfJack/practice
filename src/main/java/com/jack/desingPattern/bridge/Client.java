package com.jack.desingPattern.bridge;

/**
 *  主要点：
 *      1 两个纬度上进行扩展
 *      2 第二个纬度上有第一个纬度的对象当成属性
 * */
public class Client {
    public static void main(String[] args) {
        Color color = new Red();
        Pen pen = new SmallPen();
        pen.setColor(color);
        pen.draw("flowers");
    }
}
