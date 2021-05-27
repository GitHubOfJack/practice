package com.jack.sort;

/**
 * @author 马钊
 * @date 2021-04-06 17:17
 */
public class BubbleSort {
    public static void main(String[] args) {
        int arr[] = {1,5, 9, 3, 8, 10, 7};
        bubbleSort(arr);
    }

    public static void bubbleSort(int[] arr) {
        for (int i=0; i<arr.length -1; i++) {
            for (int j=0; j<arr.length-1 -i; j++) {
                if (arr[j] > arr[j+1]) {
                    int tmp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = tmp;
                }
            }
        }
        for (int i=0; i<arr.length; i++) {
            System.out.println(arr[i]);
        }
    }
}
