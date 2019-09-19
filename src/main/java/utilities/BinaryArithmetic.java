package utilities;

public class BinaryArithmetic {

    public static boolean[] add(boolean[] value1, boolean[] value2) {
        boolean[] var1, var2;
        if (value1.length < value2.length) {
            var1 = value2.clone();
            var2 = value1.clone();
        } else {
            var1 = value1.clone();
            var2 = value2.clone();
        }
        boolean[] result = new boolean[71];
        boolean next = false;
        for (int i = 0; i < result.length; i++) {
            boolean res;
            if (i < var2.length) {
                res = var1[i] ^ var2[i] ^ next;
                next = (var1[i] && var2[i]) ^ (next && var2[i]) ^ (next && var1[i]);
            } else {
                res = next ^ var1[i];
                next = next && var1[i];
            }
            result[i] = res;
            if (i >= var2.length && !next) break;
        }
        return result;
    }

    public static boolean[] sub(boolean[] value1, boolean[] value2) {
        boolean[] var1, var2;
        var1 = value1.clone();
        var2 = value2.clone();
        additionalCode(var2);
        return add(var1, var2);
    }

    public static boolean[] multiply(boolean[] value1, boolean[] value2) {
        boolean[] var1, var2;
        if (value1.length < value2.length) {
            var1 = value2.clone();
            var2 = value1.clone();
        } else {
            var1 = value1.clone();
            var2 = value2.clone();
        }
        boolean[] result = new boolean[35];
        boolean sign = var1[var1.length - 1] && var2[var2.length - 1];
        if (var1[var1.length - 1]) {
            var1[var1.length - 1] = false;
        }
        if (var2[var2.length - 1]) {
            var2[var2.length - 1] = false;
        }
        for (boolean bit : var2) {
            if (bit) {
                result = add(var1, result);
            }
            rightShift(var1, 1);
        }
        result[result.length - 1] = sign;
        return result;
    }

    public static void leftShift(boolean[] value, int count) {
        for (int i = 0; i < count; i++) {
            for (int j = value.length - 1; j >= 1; j--) {
                value[j] = value[j - 1];
            }
            value[0] = false;
        }
    }

    public static void rightShift(boolean[] value, int count) {
        for (int i = 0; i < count; i++) {
            for (int j = 0; j <= value.length - 2; j++) {
                value[j] = value[j + 1];
            }
            value[value.length - 1] = false;
        }
    }

    static boolean[] collate(boolean[] value1, boolean[] value2) {
        boolean[] result = value1.length > value2.length ? value1.clone() : value2.clone();
        for (int i = 0; i < Math.min(value1.length, value2.length); i++) {
            result[i] = value1[i] && value2[i];
        }
        return result;
    }

    public static void additionalCode(boolean[] value) {
        for (int index = 0; index < value.length; index++) {
            value[index] = !value[index];
        }
        boolean next = true;
        for (int index = 0; index < value.length; index++) {
            boolean res = value[index] ^ next;
            next = value[index] && next;
            value[index] = res;
            if (!next) break;
        }
    }
}