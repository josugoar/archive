package _BinarySearch;

import java.util.Arrays;

public class BinarySearch {

    public static void main(final String[] args) {
        // Array must be sorted
        final int[] arr = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
        System.out.println("Array: " + Arrays.toString(arr));
        System.out.println("Index: " + search(arr, 3));
    }

    public static int search(final int[] arr, final float ret) {
        int max = arr.length;
        int min = 0;
        while (true) {
            final int idx = (int) Math.floor((max - min) / 2 + min);
            if (ret < arr[idx]) {
                max = idx - 1;
            } else if (ret > arr[idx]) {
                min = idx + 1;
            } else {
                return idx;
            }
        }
    }

}
