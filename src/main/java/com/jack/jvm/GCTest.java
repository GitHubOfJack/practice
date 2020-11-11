package com.jack.jvm;

import java.util.ArrayList;
import java.util.List;

public class GCTest {
    private static final int _1M = 1024*1024;
    public static void main(String[] args) throws InterruptedException {
        //doTest1();
        doTest2();
        System.gc();
    }

    public static void doTest1() {
        byte[] a1, a2, a3, a4;
        a1 = new byte[2*_1M];
        a2 = new byte[2*_1M];
        a3 = new byte[2*_1M];
        a4 = new byte[4*_1M];
    }

    public static void doTest2() throws InterruptedException {
        List<ByteObject> list = new ArrayList<>();
        for (int i=0; i<10000; i++) {
            ByteObject byteObject = new ByteObject();
            list.add(byteObject);
            Thread.sleep(1000);
        }
    }

    private static class ByteObject {
        private byte[] placeHolder = new byte[64*1024];
    }
}
