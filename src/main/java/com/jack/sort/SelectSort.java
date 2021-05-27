package com.jack.sort;

/**
 * @author 马钊
 * @date 2021-04-06 17:05
 */
public class SelectSort {
    public static void main(String[] args) {
        int arr[] = {1,5, 9, 3, 8, 10, 7};
        selectSort(arr);
    }

    public static void selectSort(int[] arr) {
        int length = arr.length;
        for (int i=0; i<length; i++) {
            for (int j=i+1; j<length; j++) {
                if (arr[i] > arr[j]) {
                    int tmp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = tmp;
                }
            }
        }
        for (int i=0; i<length; i++) {
            System.out.println(arr[i]);
        }
    }
}
