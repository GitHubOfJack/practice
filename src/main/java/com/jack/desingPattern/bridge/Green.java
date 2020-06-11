package com.jack.desingPattern.bridge;

public class Green implements Color {
    @Override
    public void bepaint(String penType, String name) {
        System.out.println(penType+"green"+name);
    }
}
