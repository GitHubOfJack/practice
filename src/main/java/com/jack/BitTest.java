package com.jack;

import java.io.UnsupportedEncodingException;

/**
 * 位运算
 *
 * 两次异或同一个值，则相当于原值不变，可用于交换两个值  a^b^b 结果是a
 * 一个数字对2的N次幂取模，则可用数字&(2^n-1)替代  a%2^n == a&(2^n-1)
 * 奇偶判定 a&1=1 奇数
 * 除法*乘法 a/8 == a>>3     a*8 == a << 3
 *
 *
 * */
public class BitTest {
    public static void main(String[] args) throws UnsupportedEncodingException {
        System.out.println(100^2^2);
        String abc = encode("ddddfadfadfadfad", 10);
        System.out.println(abc);
        System.out.println(decode(abc, 10));
        System.out.println(100^0);
    }

    private static String decode(String source, int key) throws UnsupportedEncodingException {
        byte[] b = source.getBytes("UTF-8");
        for (int i=0, size=b.length; i<size; i++) {
            b[i] = (byte) (b[i]^key);
        }
        return new String(b);
    }

    public static String encode(String source, int key) throws UnsupportedEncodingException {
        byte[] b = source.getBytes("UTF-8");

        for (int i=0, size=b.length; i<size; i++) {
            b[i] = (byte) (b[i]^key);
        }
        return new String(b);
    }
}
