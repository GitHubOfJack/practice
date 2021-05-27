package com.jack.sort;

import java.util.Arrays;

/**
 * @author 马钊
 * @date 2021-04-06 17:28
 */
public class QuickSort {
    public static void main(String[] args) {
        int arr[] = {3, 5, 9, 1, 8, 10, 7};
        quickSort(arr, 0, arr.length-1);
        System.out.println(Arrays.toString(arr));

        change();

    }

    public static int[] change() {
        int digits[] = {9,9,9,9,9};
        for (int i=digits.length-1; i>-1; i--) {
            digits[i]++;
            digits[i] = digits[i] % 10;
            if (digits[i] != 0) {
                return digits;
            }
        }
        digits = new int[digits.length + 1];
        digits[0] = 1;
        return digits;
    }

    private static void quickSort(int[] arr, int low, int high) {
        if (low >= high) {
            return;
        }
        int i = low, j = high, index = arr[i];
        while (i < j) {
            while (i < j && arr[j] >= index) {
                j--;
            }
            if (i < j) {
                arr[i++] = arr[j];
            }
            while (i < j && arr[i] <= index) {
                i++;
            }
            if (i < j) {
                arr[j--] = arr[i];
            }
        }
        arr[i] = index;
        quickSort(arr, low, i-1);
        quickSort(arr, i+1, high);
    }
}
