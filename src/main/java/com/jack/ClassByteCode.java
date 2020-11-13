package com.jack;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 马钊
 * @date 2020-10-28 10:50
 */
public class ClassByteCode implements Serializable {
    private String aString = "abc";
    public final int bInt = 5;

    public static void main(String[] args) {
        HashMap<String, String> treeMap = new HashMap<>(3);
        //ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap<>(2);
        treeMap.put("1", "a");
        treeMap.put("0", "d");
        Set<String> strings = treeMap.keySet();
        for (String entry : strings) {
            System.out.println(entry);
        }
    }

    public Integer getInt() {
        return bInt;
    }
}
