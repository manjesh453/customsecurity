package com.security.pki.core;

import java.util.Random;

public class RandomGenerator {
    static final int[] sizeTable = new int[]{0, 9, 99, 999, 9999, 99999, 999999, 9999999, 99999999, 999999999, Integer.MAX_VALUE};
    private static Random random=new Random();

    public static String getAlphaNumericString(int n){
        String AlphaNumericString="ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvxyz";
        StringBuilder sb = new StringBuilder(n);
        for (int i=0; i<n; i++) {
            sb.append(AlphaNumericString.charAt(random.nextInt(AlphaNumericString.length())));
        }
        return sb.toString();
    }
}
