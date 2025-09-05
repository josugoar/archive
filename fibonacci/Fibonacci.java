package _Fibonacci;

import java.util.Arrays;

public class Fibonacci {

    public static int[] fibonacci(int max, int a, int b) {
        int[] vals = { a, b };
        int[] fibo = new int[max];
        if (max > vals.length) {
            for (int i = 0; i < vals.length; i++) {
                fibo[i] = vals[i];
            }
            for (int i = vals.length; i < max; i++) {
                fibo[i] = fibo[i - 1] + fibo[i - 2];
            }
            return fibo;
        } else {
            return Arrays.copyOfRange(vals, 0, max);
        }
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(fibonacci(10, 0, 1)));
    }

}
