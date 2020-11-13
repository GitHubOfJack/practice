package com.jack.arithmetic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 马钊
 * @date 2020-11-09 17:07
 *
 * 给定一个字符串 s，找到 s 中最长的回文子串。你可以假设 s 的最大长度为 1000。
 *
 * 输入: "babad"
 * 输出: "bab"
 * 注意: "aba" 也是一个有效答案。
 */
public class LongestPalindrome {
    public static void main(String[] args) {
        //String s = "abc";
        String s = "ccc";
        System.out.println(longestPalindrome(s));
    }
    public static String longestPalindrome(String s) {
        byte[] bytes = s.getBytes();
        List<int[]> intList = new ArrayList<>();
        Map<Byte, List<Integer>> map = new HashMap<>();
        for (int i=0; i<bytes.length; i++) {
            List<Integer> integers = map.get(bytes[i]);
            if (null == integers) {
                integers = new ArrayList<>();
                map.put(bytes[i], integers);
            }
            if (integers.size() >= 1) {
                Integer preIndex = integers.get(integers.size() - 1);
                if (i - preIndex <= 2) {
                    intList.add(new int[] {preIndex, i});
                }
            }
            integers.add(i);
        }
        int maxstart = 0, maxend = 0, start = 0, end = 0;
        int maxLen = 0;
        for (int j = 0; j<intList.size(); j++) {
            for (Byte myB : map.keySet()) {
                List<Integer> integers = map.get(myB);
                if (integers.size() > 1) {
                    for (int i = 0; i < integers.size() - 1; i++) {
                        int[] ints = intList.get(j);
                        if ((integers.get(i) + integers.get(i + 1)) == (ints[0] + ints[1])) {
                            start = integers.get(i);
                            end = integers.get(i+1);
                            if (maxLen <= end - start) {
                                maxLen =  end - start;
                                maxstart = start;
                                maxend = end;
                            }

                        }
                    }
                }
            }
        }
        return new String(bytes, maxstart, maxend-maxstart+1);
    }


}
