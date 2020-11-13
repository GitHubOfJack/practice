package com.jack.arithmetic;

import java.util.*;

/**
 * @author 马钊
 * @date 2020-11-08 14:48
 */
public class MaxLengthOfSubString {

    public static int lengthOfLongestSubstring1(String s) {
        if (null == s || 0 >= s.length()) {
            return 0;
        } else if (1 == s.length()) {
            return 1;
        } else {
            // 记录字符上一次出现的位置
            int[] last = new int[128];
            for(int i = 0; i < 128; i++) {
                last[i] = -1;
            }
            int n = s.length();

            int res = 0;
            int start = 0; // 窗口开始位置
            for(int i = 0; i < n; i++) {
                int index = s.charAt(i);
                start = Math.max(start, last[index] + 1);
                res   = Math.max(res, i - start + 1);
                last[index] = i;
            }

            return res;
        }
    }

    public static int lengthOfLongestSubstring2(String s) {
        int left=0, right=0;
        int maxlen = 0;
        for (; right < s.length(); right++) {
          for (int cur=left; cur<right; cur++) {
              if (s.charAt(cur) == s.charAt(right)) {
                  left = cur + 1;
                  break;
              }
          }
          if (maxlen < right-left+1) {
              maxlen = right-left+1;
          }
        }
        return maxlen;
    }

    public static void main(String[] args) {
        System.out.println(lengthOfLongestSubstring3("pwwkexp"));
        System.out.println(lengthOfLongestSubstring3("tmmzuxt"));
        System.out.println(lengthOfLongestSubstring3("aba"));
        System.out.println(lengthOfLongestSubstring3("abbca"));
        System.out.println(lengthOfLongestSubstring3("ab"));
    }

    public static int lengthOfLongestSubstring3(String s) {
        int maxLen = 0;
        char c = 0;
        int start = 0;
        Map<Character, Integer> map = new HashMap<>();
        for (int i=0; i<s.length(); i++) {
            c = s.charAt(i);
            if (map.containsKey(c)) {
                start = Math.max(map.get(c)+1, start);
            }
            maxLen = Math.max(i - start + 1 , maxLen);
            map.put(c, i);

        }
        return maxLen;
    }
}
