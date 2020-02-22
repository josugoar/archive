package _SortingAlgorithms;

import java.util.Arrays;

class BubbleSort {

    public static void main(String[] args) {
        float[] arr = initializeArray(10);
        System.out.println("Original: " + Arrays.toString(arr));
        System.out.println("Sorted: " + Arrays.toString(sort(arr)));
    }

    public static float[] initializeArray(int max) {
        float[] arr = new float[max];
        for (int i = 0; i < max; i++) {
            arr[i] = (float) Math.random();
        }
        return arr;
    }

    public static float[] sort(float[] arr) {
        for (int i = 0; i < arr.length + 1; i++) {
            for (int k = 0; k < arr.length - 1; k++) {
                float temp = arr[k];
                if (arr[k] > arr[k + 1]) {
                    arr[k] = arr[k + 1];
                    arr[k + 1] = temp;
                }
            }
        }
        return arr;
    }
}
